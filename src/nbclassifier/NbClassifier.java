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
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        String fileName = "C:\\Users\\YH Jonathan Kwok\\PycharmProjects\\tweepyPractice\\tweets.txt";
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
        
        System.out.println("Processing. . . (Reading from file)");
        while((line = br.readLine()) != null) {
            line = line.toLowerCase();
            //put lines into my collection from file
            list.add(line);
            //collect each words from the file to a map to form 
            //the category list of the big table - no duplicated
            String[] temp = line.split(" ");
            for (int i = 2; i < temp.length; i++){
                category.add(temp[i]);
            }
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
        
        //For functioning purpose, convert the set to an arraylist
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.addAll(category);
        
        //The next task is to create the main table. 
        //x axes = category(all the words) + class, y axes = each tweets
        //contents will be represented by bools, true means this tweet contains this word,
        //and false doesn't.
        //my research told me that this 2d bool array will be filled with false by default.
        System.out.println("Creating main table");
        //categoryLst.size() + 1 to includes the class at the end
        String mainTable[][] = new String [train.size()][categoryList.size() + 1];
        for (i = 0; i < train.size(); i++) {
            String[] elements = train.get(i).split(" ");
            int j;
            for (j = 0; j < categoryList.size(); j++){
                mainTable[i][j] = "false";
                //disregard the first number and the class
                for (int k = 2; k < elements.length; k++)
                    if(categoryList.get(j).equals(elements[k]))
                        mainTable[i][j] = "true";
            }
            //put the class(positive/negative/neutral) into the last slot of that row
            mainTable[i][j] = elements[1];
        }
        System.out.println("Main table SHOULD BE created");
        for (int try1 = 0; try1 < categoryList.size() + 1; try1++){
            for (int try2 = 0; try2 < train.size(); try2++){
                System.out.print(mainTable[try2][try1] + " ");
            }
            System.out.print("\n");
        }
        
        //Now construct the small tables
        ArrayList<subTable> subTables = new ArrayList<>();
        System.out.println("Constructing small tables...");
        for (int j = 0; j < categoryList.size(); j++){
            Map tempMap = new HashMap();
            for (int k = 0; k < train.size(); k++){
                //a map that contains the whole column of (t/f,class) e.g true,positive
                tempMap.put(mainTable[k][j],mainTable[k][categoryList.size()]);
            }
            subTable tempSubTable = new subTable(tempMap);
            subTables.add(tempSubTable);            
        }
        System.out.println("Small tables SHOULD BE ready\n\n");
        
        //match counter
        int mNum = 0;
        System.out.println("Let's begin the test\n");
        for (int j = 0; j < test.size(); j++){
            System.out.println("Sentence:");
            String tempLine = test.get(j);
            String[] elements = tempLine.split(" ");
            System.out.print("\t\t");
            for (int k = 2; k < elements.length; k++){
                System.out.print(elements[k] + " ");
            }
            System.out.println();
            System.out.println("\nTarget:");
            String target = elements[1];
            System.out.println("\t\t" + target);
            
            System.out.println("Prediction:");
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
            double negaTotal = subTables.get(0).getSumNega();
            double neutTotal = subTables.get(0).getSumNeut();
            
            for (int k = 0; k < subTables.size(); k++){
                if (tempBool.get(k).equals("true")){
                    //get a number out from subTables.get(k)
                    posiTotal *= subTables.get(k).gettPosi();
                    negaTotal *= subTables.get(k).gettNega();
                    neutTotal *= subTables.get(k).gettNeut();
                }                    
                else if (tempBool.get(k).equals("false")){
                    //get a number out from subTables.get(k)
                    posiTotal *= subTables.get(k).getfPosi();
                    negaTotal *= subTables.get(k).getfNega();
                    neutTotal *= subTables.get(k).getfNeut();
                }                    
                else{
                    //you are in deep trouble if you could get here...
                    System.out.println("YOU ARE IN SERIOUS TROUBLE!!");
                }
                                        
            }
            boolean match = false;
            String predict;
            if ((posiTotal > negaTotal) && (posiTotal > neutTotal))
                predict = "positive";                
            else if ((negaTotal > posiTotal) && (negaTotal > neutTotal))
                predict = "negative";
            else if ((neutTotal > posiTotal) && (neutTotal > negaTotal))
                predict = "neutral";
            else
                predict = "undefined";
            System.out.println(predict);
            
            System.out.println("Result:");
            if (target.equals(predict))
                match = true;
            
            if (match){
                System.out.println("\t\tmatch");
                mNum++;
            }
            else
                System.out.println("\t\tmissed");        
        }
        System.out.println("Accuracy:\n" + mNum/test.size());        
    }    
}
