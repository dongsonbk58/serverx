package com.server_x.servlet;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by cuongdx on 12/25/2016.
 */

public class Apriori {

    private int numItems;
    Vector<String> candidates = new Vector<String>();
    int numTransactions; //number of transactions
    double minSup = 0.05; //minimum support for a frequent itemset
    String itemSep = " ";
    Vector<String> transaction;

    private void generateCandidates(int n) {
        Vector<String> tempCandidates = new Vector<String>(); //temporary candidate string vector
        String str1, str2; //strings that will be used for comparisons
        StringTokenizer st1, st2; //string tokenizers for the two itemsets being compared

        //if its the first set, candidates are just the numbers
        if (n == 1) {
            for (int i = 0; i < numItems; i++) {
                tempCandidates.add(Integer.toString(i));
            }
        } else if (n == 2) //second itemset is just all combinations of itemset 1
        {
            //add each itemset from the previous frequent itemsets together
            for (int i = 0; i < candidates.size(); i++) {
                st1 = new StringTokenizer(candidates.get(i));
                str1 = st1.nextToken();
                for (int j = i + 1; j < candidates.size(); j++) {
                    st2 = new StringTokenizer(candidates.elementAt(j));
                    str2 = st2.nextToken();
                    tempCandidates.add(str1 + " " + str2);
                }
            }
        } else {
            //for each itemset
            for (int i = 0; i < candidates.size(); i++) {
                //compare to the next itemset
                for (int j = i + 1; j < candidates.size(); j++) {
                    //create the strigns
                    str1 = new String();
                    str2 = new String();
                    //create the tokenizers
                    st1 = new StringTokenizer(candidates.get(i));
                    st2 = new StringTokenizer(candidates.get(j));

                    //make a string of the first n-2 tokens of the strings
                    for (int s = 0; s < n - 2; s++) {
                        str1 = str1 + " " + st1.nextToken();
                        str2 = str2 + " " + st2.nextToken();
                    }

                    //if they have the same n-2 tokens, add them together
                    if (str2.compareToIgnoreCase(str1) == 0)
                        tempCandidates.add((str1 + " " + st1.nextToken() + " " + st2.nextToken()).trim());
                }
            }
        }
        //clear the old candidates
        candidates.clear();
        //set the new ones
        candidates = new Vector<String>(tempCandidates);
        tempCandidates.clear();
    }


    /************************************************************************
     * Method Name  : calculateFrequentItemsets
     * Purpose      : Determine which candidates are frequent in the n-th itemsets
     * : from all possible candidates
     * Parameters   : n - iteger representing the current itemsets being evaluated
     * Return       : None
     *************************************************************************/
    private void calculateFrequentItemsets(int n) {
        Vector<String> frequentCandidates = new Vector<String>(); //the frequent candidates for the current itemset
        StringTokenizer st, stFile; //tokenizer for candidate and transaction
        boolean match; //whether the transaction has all the items in an itemset
        boolean trans[]; //array to hold a transaction so that can be checked
        int count[] = new int[candidates.size()]; //the number of successful matches

        //for each transaction
        for (int i = 0; i < numTransactions; i++) {
            trans = new boolean[numItems + 1];
            stFile = new StringTokenizer(transaction.get(i), itemSep); //read a line from the file to the tokenizer
            //put the contents of that line into the transaction array
            int countTokens = stFile.countTokens();
            int[] temp = new int[countTokens]; //luu gia tri cua item trong 1 transaction
            for (int j = 0; j < countTokens; j++) {
                int k = Integer.parseInt(stFile.nextToken());
                trans[k] = true; //if it is not a 0, assign the value to true
                temp[j] = k;
            }

            //check each candidate
            int sizeTransaction = 0;//neu tat ca phan tu cua 1 transaction da duoc tinh toan roi thi thoat
            for (int c = 0; c < candidates.size(); c++) {
                match = false; //reset match to false
                //tokenize the candidate so that we know what items need to be present for a match
                st = new StringTokenizer(candidates.get(c));
                //check each item in the itemset to see if it is present in the transaction
                while (st.hasMoreTokens()) {
                    match = (trans[Integer.valueOf(st.nextToken())]);
                    if (!match) //if it is not present in the transaction stop checking
                        break;
                }
                if (match) //if at this point it is a match, increase the count

                    count[c]++;


            }

        }
        for (int i = 0; i < candidates.size(); i++) {
            //  System.out.println("Candidate: " + candidates.get(c) + " with count: " + count + " % is: " + (count/(double)numItems));
            //if the count% is larger than the minSup%, add to the candidate to the frequent candidates
            if ((count[i] / (double) numTransactions) >= minSup) {
                frequentCandidates.add(candidates.get(i));
                //put the frequent itemset into the output file
            }
        }


        //if error at all in this process, catch it and print the error messate

        //clear old candidates
        candidates.clear();
        //new candidates are the old frequent candidates
        candidates = new Vector<String>(frequentCandidates);
        frequentCandidates.clear();
    }

    public void init() {
        int itemsetNumber = 0;
        do {
            //increase the itemset that is being looked at
            itemsetNumber++;

            //generate the candidates
            generateCandidates(itemsetNumber);

            //determine and display frequent itemsets
            calculateFrequentItemsets(itemsetNumber);
            System.out.println("candidate: " + candidates.size());
            if (candidates.size() != 0) {
                System.out.println("Frequent " + itemsetNumber + "-itemsets");
                System.out.println(candidates);
            }
            //if there are <=1 frequent items, then its the end. This prevents reading through the database again. When there is only one frequent itemset.
        } while (candidates.size() > 1);

    }

    public Apriori(int numTransactions,int numItems){
        this.numTransactions=numTransactions;
        this.numItems=numItems;
        init();
    }
}
