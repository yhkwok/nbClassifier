package nbclassifier;


import java.util.ArrayList;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author YH Jonathan Kwok
 */
public class subTable {

    public double gettPosi() {
        tPosi = ((double)tPos) / sumPos; // /2;
        if (tPosi == 0.0 /*|| tPosi < 0.0001 || tPosi > 0.1*/) tPosi = 1.0;
        return tPosi;
    }

    public double getfPosi() {
        fPosi = ((double)fPos) / sumPos; // *5;
        if (fPosi == 0.0 /*|| fPosi < 0.0001 || fPosi > 0.1*/) fPosi = 1.0;
        return fPosi;
    }

    public double gettNega() {
        tNega = ((double)tNeg) / sumNeg; // /2;
        if (tNega == 0.0 /*|| tNega < 0.0001 || tNega > 0.1*/) tNega = 1.0;
        return tNega;
    }

    public double getfNega() {
        fNega = ((double)fNeg) / sumNeg; // *5;
        if (fNega == 0.0 /*|| fNega < 0.0001 || fNega > 0.1*/) fNega = 1.0;
        return fNega;
    }

    public double gettNeut() {
        tNeut = ((double)tNeu) / sumNeu;
        if (tNeut == 0.0) tNeut = 1.0;
        return tNeut;
    }

    public double getfNeut() {
        fNeut = ((double)fNeu) / sumNeu;
        if (fNeut == 0.0) fNeut = 1.0;
        return fNeut;
    }

    public double getSumPosi() {
        sumPosi = ((double)sumPos) / total;
        return sumPosi;
    }

    public double getSumNega() {
        sumNega = ((double)sumNeg) / total;
        return sumNega;
    }

    public double getSumNeut() {
        sumNeut = ((double)sumNeu) / total;
        return sumNeut;
    }

    private int tPos = 0;
    private int fPos = 0;
    private int tNeg = 0;
    private int fNeg = 0;
    private int tNeu = 0;
    private int fNeu = 0;
    private int sumPos = 0;
    private int sumNeg = 0;
    private int sumNeu = 0;
    private int total = 0;
    private double tPosi = 0.0;
    private double fPosi = 0.0;
    private double tNega = 0.0;
    private double fNega = 0.0;
    private double tNeut = 0.0;
    private double fNeut = 0.0;
    private double sumPosi = 0.0;
    private double sumNega = 0.0;
    private double sumNeut = 0.0;

    public subTable(/*Map tempMap*/ArrayList<String> fakeMap) {
        for (int i = 0; i < fakeMap.size(); i++) {
            String temp = fakeMap.get(i);
            String parts[] = temp.split(" ");
            String value = parts[0]; //t/f
            String nePoNe = parts[1]; //pos/neg/neu
            //System.out.println(value + " " + nePoNe);
            if (value.equals("true") && nePoNe.equals("positive")) {
                tPos++;
                sumPos++;
            } else if (value.equals("false") && nePoNe.equals("positive")) {
                fPos++;
                sumPos++;
            } else if (value.equals("true") && nePoNe.equals("negative")) {
                tNeg++;
                sumNeg++;
            } else if (value.equals("false") && nePoNe.equals("negative")) {
                fNeg++;
                sumNeg++;
            } else if (value.equals("true") && nePoNe.equals("neutral")) {
                tNeu++;
                sumNeu++;
            } else if (value.equals("false") && nePoNe.equals("neutral")) {
                fNeu++;
                sumNeu++;
            } else {
                System.out.println("Error, data invalid!!");
                System.out.println(value + " & " + nePoNe);
            }
        }
        //System.out.println("End of Map");
        total = sumPos + sumNeg + sumNeu;
        //System.out.println(fakeMap.size() + " : " + sumPos + " + " + sumNeg + " + " + sumNeu + " = " + total);
        //System.out.println(tPos + " " + fPos + " " + tNeg + " " + fNeg + " " + tNeu + " " + fNeu);
    }    
}
