package nbclassifier;


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
        tPosi = tPos / sumPos;
        return tPosi;
    }

    public double getfPosi() {
        fPosi = fPos / sumPos;
        return fPosi;
    }

    public double gettNega() {
        tNega = tNeg / sumNeg;
        return tNega;
    }

    public double getfNega() {
        fNega = fNeg / sumNeg;
        return fNega;
    }

    public double gettNeut() {
        tNeut = tNeu / sumNeu;
        return tNeut;
    }

    public double getfNeut() {
        fNeut = fNeu / sumNeu;
        return fNeut;
    }

    public double getSumPosi() {
        sumPosi = sumPos / total;
        return sumPosi;
    }

    public double getSumNega() {
        sumNega = sumNeg / total;
        return sumNega;
    }

    public double getSumNeut() {
        sumNeut = sumNeu / total;
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

    public subTable(Map tempMap) {
        for (Object key : tempMap.keySet()) {
            String value = key.toString(); //t/f
            String nePoNe = (String) tempMap.get(key); //pos/neg/neu
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
            }
        }
        total = sumPos + sumNeg + sumNeu;        
    }    
}
