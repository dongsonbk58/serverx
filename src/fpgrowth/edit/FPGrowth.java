/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpgrowth.edit;

import java.io.IOException;

import fp.MemoryLogger;


/**
 *
 * @author Administrator
 */
public class FPGrowth {

    /**
     * @param args the command line arguments
     */
    static double threshold = 1000;
    static String file = "pumsb.dat";
    static double databaseSize;

    public static void main(String[] args) throws IOException {
        if (file.compareTo("pumsb.dat") == 0) {
            databaseSize = 3416;

        } else if (file.compareTo("accidents.dat") == 0) {
            databaseSize = 340183;

        } else if (file.compareTo("connect.dat") == 0) {
            databaseSize = 67557;

        } else if (file.compareTo("T10I4D100K.dat") == 0) {
            databaseSize = 100000;

        }else if (file.compareTo("permisiontransaction.txt") == 0) {
            databaseSize = 67700;

        }
        long startTimestamp;  // start time
        long endTimestamp;
        // record start time

        startTimestamp = System.currentTimeMillis();
        MemoryLogger.getInstance().reset();
        AlgoFPGrowth fp = new AlgoFPGrowth();
        fp.runAlgorithm(file, "result.txt", threshold / databaseSize);
        // check the memory usage
        MemoryLogger.getInstance().checkMemory();
        // save endtime
        endTimestamp = System.currentTimeMillis();
        System.out.println("=============  FP-GROWTH  =============");
        long temps = endTimestamp - startTimestamp;
        System.out.println(" Transactions count from database : " + fp.transactionCount);
        System.out.print(" Max memory usage: " + MemoryLogger.getInstance().getMaxMemory() + " mb \n");
        System.out.println(" Frequent itemsets count : " + fp.itemsetCount);
        System.out.println(" Total time ~ " + temps + " ms");
        System.out.println("===================================================");
    }

}
