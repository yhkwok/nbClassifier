/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nbclassifier;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author YH Jonathan Kwok
 */
public class NbClassifier {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    
    //source: (http://stackoverflow.com/questions/4429995/how-do-you-remove-repeated-characters-in-a-string)
    public static String removeRepeatedChars(String input, int maxRepeat)
    {
        if(input.length() == 0)return input;
        
        char[] chars = input.toCharArray();
        String b = "";
        b += chars[0];// = new StringBuilder;
        char lastChar = chars[0];
        int repeat = 0;
        for(int i=1;i<input.length();i++){
            //repeated less then twice
            if(chars[i] == lastChar && ++repeat < maxRepeat) {
                b += chars[i];
            }
            //repeated too many times
            else if(chars[i] == lastChar && ++repeat >= maxRepeat) {
                
            }
            //not repeated
            else {
                b += chars[i];
                repeat=0;
                lastChar = chars[i];
            }
        }
        return b;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        String fileName = "C:\\Users\\YH Jonathan Kwok\\PycharmProjects\\tweepyPractice\\tweets.txt"; //testTrainTweets.txt"; //
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        /*
        FileInputStream fstream;
        fstream = new FileInputStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));*/
        //lines collection
        ArrayList<String> list = new ArrayList<>();
        String line;
        //category collection (make it a set to prevent duplication)
        Set<String> category = new HashSet<>();
        //int positiveCount = 0;
        //int negativeCount = 0;
        System.out.println("Processing. . . (Reading from file)");
        while((line = br.readLine()) != null) {
            line = line.toLowerCase();
            //put lines into my collection from file
            line = removeRepeatedChars(line, 2);
            line = line.replaceAll("[^0-9a-z ]", " ");
            list.add(line);                        
        }
        
        //new dataset found (http://thinknook.com/twitter-sentiment-analysis-training-corpus-dataset-2012-09-22/)
        //idea of making new testing easy tweets (http://www.laurentluce.com/posts/twitter-sentiment-analysis-using-python-and-nltk/)
        fileName = "C:\\Users\\YH Jonathan Kwok\\PycharmProjects\\tweepyPractice\\SentimentAnalysisDataset.txt"; //testTestTweets.txt"; //
        fr = new FileReader(fileName);
        br = new BufferedReader(fr);
        int lineCounter = 0;
        while((line = br.readLine()) != null && lineCounter < 35000){
            line = line.toLowerCase();
            line = removeRepeatedChars(line, 2);
            line = line.replaceAll("[^0-9a-z ]", " ");
            list.add(line);
            lineCounter++;
        }
              
        System.out.println("Tweets added to list and category set created");
        //Now, category has all the words without duplicated,
        //and list has all the lines collected.
        
        //shuffle the list
        System.out.println("Shuffling list");
        Collections.shuffle(list);
        System.out.println("List shuffled");
        
        //split the list into testing set and training set
        //70% train, 30% testing
        System.out.println("Spliting list to training set (70%) and testing set (30%)");
        ArrayList<String> train = new ArrayList<>();
        ArrayList<String> test = new ArrayList<>();
        int i;
        for (i = 0; i < (list.size() * 0.7); i++)
            train.add(list.get(i));
        for ( ; i < list.size(); i++)
            test.add(list.get(i));
        System.out.println("List splited");
        
        for (int k = 0; k < train.size(); k++){
            String[] temp = train.get(k).split(" ");
            for (int j = 2; j < temp.length; j++){
                //Improve the dictionary by shorten those words with too many repeated characters
                //and remove those useless space
                //System.out.println("Before: " + temp[i]);
                temp[j] = removeRepeatedChars(temp[j], 2);
                //System.out.println("After: " + temp[i]);
                if (!temp[j].equals(" ")){
                    temp[j] = temp[j].replaceAll("[^0-9a-z]", "");
                    category.add(temp[j]);
                }
            }
        }
               
        //For functioning purpose, convert the set to an arraylist
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.addAll(category);
        Collections.sort(categoryList);
        //testing purpose, output the dictionary words to a file
        FileWriter writer = new FileWriter("output.txt"); 
        for(String str: categoryList) {
            writer.write(str + "\n");
        }
        writer.close();
        
        //The next task is to create the main table. 
        //x axes = category(all the words) + class, y axes = each tweets
        //contents will be represented by bools, true means this tweet contains this word,
        //and false doesn't.
        //my research told me that this 2d bool array will be filled with false by default.
        System.out.println("Creating main table");
        //categoryLst.size() + 1 to includes the class at the end
        String mainTable[][] = new String [train.size()][categoryList.size() + 1];
        for (i = 0; i < train.size(); i++) {
            String delim1 = " ";
            String delim2 = ",";
            String temp = train.get(i);
            temp = temp.replaceAll(delim2, delim1);
            String[] elements = temp.split(delim1);
            int j;
            for (j = 0; j < categoryList.size(); j++){
                mainTable[i][j] = "false";
                //disregard the first number and the class
                for (int k = 2; k < elements.length; k++)
                    if(categoryList.get(j).equals(elements[k]))
                        mainTable[i][j] = "true";
            }
            //put the class(positive/negative/neutral) into the last slot of that row
            /*if (elements[1].equals("positive") || elements[1].equals("negative"))
                mainTable[i][j] = elements[1];
            else{*/
                if (elements[1].equals("1") || elements[1].equals("positive")){
                    String temp1 = "positive";
                    mainTable[i][j] = temp1;
                    //System.out.println(mainTable[i][j]);
                }                    
                else if (elements[1].equals("0") || elements[1].equals("negative")){
                    String temp1 = "negative";
                    mainTable[i][j] = temp1;
                    //System.out.println(mainTable[i][j]);
                }
                else if (elements[1].equals("neutral"))
                    mainTable[i][j] = elements[1];
            //}
                
        }
        System.out.println("Main table SHOULD BE created");
        
        /*//testing purpose - to see how the mainTable looks like
        for (int try2 = 0; try2 < train.size(); try2++){
            for (int try1 = 0; try1 < categoryList.size() + 1; try1++){
                System.out.print(mainTable[try2][try1] + " ");
            }
            System.out.print("\n");
        }*/
        
        //Now construct the small tables
        ArrayList<subTable> subTables = new ArrayList<>();
        System.out.println("Constructing small tables...");
        for (int j = 0; j < categoryList.size(); j++){
            //Map tempMap = new HashMap();
            ArrayList<String> fakeMap = new ArrayList<>();
            for (int k = 0; k < train.size(); k++){
                //a map that contains the whole column of (t/f,class) e.g true,positive
                //System.out.println("j = " + j + "; k = " + k);
                //System.out.println(mainTable[k][j] + " " + mainTable[k][categoryList.size()]);
                String temp = mainTable[k][j] + " " + mainTable[k][categoryList.size()];
                //tempMap.put(mainTable[k][j],mainTable[k][categoryList.size()]);
                fakeMap.add(temp);
            }
            
            subTable tempSubTable = new subTable(fakeMap);
            subTables.add(tempSubTable);            
        }
        System.out.println("Small tables SHOULD BE ready\n\n");
        
        //match counter
        int mNum = 0;
        System.out.println("Let's begin the test\n");
        int posiMiss = 0;
        int negaMiss = 0;
        for (int j = 0; j < test.size(); j++){
            //System.out.print("\nSentence:");
            String tempLine = test.get(j);
            String delim1 = " ";
            String delim2 = ",";
            tempLine = tempLine.replaceAll(delim2, delim1);
            String[] elements = tempLine.split(delim1);
            //System.out.print("\t");
            for (int k = 2; k < elements.length; k++){
                //System.out.print(elements[k] + " ");
            }
            //System.out.print("\nTarget:");
            String target = elements[1];
            /*if (elements[1].equals("positive") || elements[1].equals("negative"))
                target = elements[1];
            else{*/
                if (elements[1].equals("1") || elements[1].equals("positive")){
                    String temp1 = "positive";
                    target = temp1;
                }                    
                else if (elements[1].equals("0") || elements[1].equals("negative")){
                    String temp1 = "negative";
                    target = temp1;
                }
                else if (elements[1].equals("neutral"))
                    target = elements[1];
            //}            
            //System.out.println("\t\t" + target);
            
            //System.out.print("Prediction:");
            ArrayList<String> tempBool = new ArrayList<>();
            for (int l = 0; l < categoryList.size(); l++){
                boolean found = false;
                for (int k = 2; k < elements.length; k++){
                    //compare each word in the sentence against the category list 
                    if (categoryList.get(l).equals(elements[k]))
                        found = true;
                }
                if (found)
                    tempBool.add("true");
                else
                    tempBool.add("false");
            }
            
            double posiTotal = subTables.get(0).getSumPosi();
            //System.out.println(subTables.get(0).getSumPosi());
            double negaTotal = subTables.get(0).getSumNega();
            //System.out.println(subTables.get(0).getSumNega());
            double neutTotal = subTables.get(0).getSumNeut();
            //System.out.println(subTables.get(0).getSumNeut());
            
            for (int k = 0; k < subTables.size(); k++){
                if (tempBool.get(k).equals("true")){
                    //get a number out from subTables.get(k)
                    /*if(subTables.get(k).gettPosi() == 0.0) System.out.println("0.0 ignored!!!");
                    else*/ posiTotal *= subTables.get(k).gettPosi();
                    //System.out.println(subTables.get(k).gettPosi());
                    /*if(subTables.get(k).gettNega() == 0.0) System.out.println("0.0 ignored!!!");
                    else*/ negaTotal *= subTables.get(k).gettNega();
                    //System.out.println(subTables.get(k).gettNega());
                    /*if(subTables.get(k).gettNeut() == 0.0) System.out.println("0.0 ignored!!!");
                    else*/ neutTotal *= subTables.get(k).gettNeut();
                    //System.out.println(subTables.get(k).gettNeut());
                }                    
                else if (tempBool.get(k).equals("false")){
                    //get a number out from subTables.get(k)
                    /*if(subTables.get(k).getfPosi() == 0.0) System.out.println("0.0 ignored!!!");
                    else*/ posiTotal *= subTables.get(k).getfPosi();
                    //System.out.println(subTables.get(k).getfPosi());
                    /*if(subTables.get(k).getfNega() == 0.0) System.out.println("0.0 ignored!!!");
                    else*/ negaTotal *= subTables.get(k).getfNega();
                    //System.out.println(subTables.get(k).getfNega());
                    /*if(subTables.get(k).getfNeut() == 0.0) System.out.println("0.0 ignored!!!");
                    else*/ neutTotal *= subTables.get(k).getfNeut();
                    //System.out.println(subTables.get(k).getfNeut());
                }                    
                else{
                    //you are in deep trouble if you could get here...
                    System.out.println("YOU ARE IN SERIOUS TROUBLE!!");
                }
                                        
            }
            boolean match = false;
            String predict;
            //posiTotal *= 10;
            //negaTotal *= 10;
            if ((posiTotal > negaTotal))// && (posiTotal > neutTotal))
                predict = "positive";                
            else if ((negaTotal > posiTotal))// && (negaTotal > neutTotal))
                predict = "negative";
            /*else if ((neutTotal > posiTotal) && (neutTotal > negaTotal))
                predict = "neutral";*/
            else
                predict = "undefined";
            //System.out.println("\t" + predict);
            
            //System.out.print("Result:");
            if (target.equals(predict))
                match = true;
            
            if (match){
                //System.out.println("\t\tmatch");
                mNum++;
            }
            else{
                //System.out.println("\t\tmissed " + target + " " + predict);        
                //System.out.println(posiTotal + " " + negaTotal);// + " " + neutTotal);
                if (posiTotal > negaTotal)
                    posiMiss++;
                else
                    negaMiss++;
            }
            System.out.println(posiTotal + " " + negaTotal);// + " " + neutTotal);
        }        
        System.out.println("\n\nAccuracy:\t" + (((double)mNum)/test.size()*100) + "%");        
        System.out.println("Wrong guesses results - Positive: " + posiMiss + " Negative: " + negaMiss);
    }    
}
