package com.server_x.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import javax.naming.Context;

import fp.FPGrowth;
import fpgrowth.AlgoAgrawalFaster94;
import fpgrowth.AlgoFPGrowth;
import fpgrowth.Itemset;
import fpgrowth.Itemsets;


public class Utils {
    public static ArrayList<Permission> listPermission;
    public static int numTransactions;

    public static final double MIN_SUP = 0.1;
    public static final double MIN_CONF = 0.5;
    static ArrayList<String> transacsion = new ArrayList<String>();
    public static ArrayList<Application> listApp = new ArrayList<Application>();

    public static File getDirectory() {
    	 File path = new File(Common.directory+"/fileuploads");
        if (!path.exists())
        	path.mkdir();
        return path;
    }

    public static String fileToPath(File file)
            throws UnsupportedEncodingException, MalformedURLException {
        URL url = file.toURI().toURL();
        return URLDecoder.decode(url.getPath(), "UTF-8");
    }

    public static File createFile(String filename) {
        File file = null;
        File directory = getDirectory();
        file = new File(directory, filename);
        try {
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
        } catch (Exception ex) {

        }
        return file;
    }

    public static void run(Context context ,String imei) throws FileNotFoundException,
            IOException {
    	int c = genfiletran(context, imei);
        genData(context, imei, c);
    }
    
    public static int genfiletran(Context context, String imei) {
    	File input = new File(getDirectory(), "transaction_"+imei+".txt");
    	createFile("transaction"+imei+".txt");
    	File output = new File(getDirectory(), "transaction"+imei+".txt");
		FileReader fileReader;
		PrintWriter pw;
		int count = 0;
		
		try {
			fileReader = new FileReader(input);
	        pw = new PrintWriter(output);
			String temp;
			 ArrayList<String> listRule = new ArrayList<String>();
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			try {
				while ((temp = bufferedReader.readLine()) != null) {
				     listRule.add(temp);
				 }
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(String list:listRule){
				String[] list1 = list.split(":");
				if(list1.length > 1){
					pw.print(list1[1]);
					count++;
					pw.println();
				}else{
				}
			
			}
		pw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return count;
    }

    
    
    public static void genData(Context context, String imei, int count) {
        String input = null;
        File trans = new File(getDirectory(), "transaction"+imei+".txt");
        try {
            input = fileToPath(trans);
            String outputSup = fileToPath(createFile("Sup_"+imei+".txt"));
            String outputConf = fileToPath(createFile("Conf_"+imei+".txt"));
            
//             STEP 1: Applying the FP-GROWTH algorithm to find frequent itemsets
            AlgoFPGrowth fpgrowth = new AlgoFPGrowth();
            Itemsets patterns = fpgrowth.runAlgorithm(input, outputSup, MIN_SUP);
            int databaseSize = fpgrowth.getDatabaseSize();
//            System.out.println(""+databaseSize);
            
            
//            FPGrowth fp = new FPGrowth(trans, (int)Math.round(MIN_SUP*count));
//    		int index = 0;
//    	    Itemsets patterns =new Itemsets("patterns");
//    		for(String item : fp.frequentPatterns.keySet()){
//    			String[] str = item.split(" ");
//    			int[] itemsetArray = Utils.strArrayToIntArray(str);
//    			Arrays.sort(itemsetArray);
//    			Itemset itemsetObj = new Itemset(itemsetArray);
//    			System.out.println("STT"+index+":"+itemsetObj+":"+fp.frequentPatterns.get(item));
//    	        itemsetObj.setAbsoluteSupport(fp.frequentPatterns.get(item));
//    	        patterns.addItemset(itemsetObj, str.length);
//    	        index++;
//    		}
            
            // patterns.printItemsets(databaseSize);
            // STEP 2: Generating all rules from the set of frequent itemsets (based
            // on Agrawal & Srikant, 94)
            AlgoAgrawalFaster94 algoAgrawal = new AlgoAgrawalFaster94();
            // the next line run the algorithm.
            // Note: we pass null as output file path, because we don't want
            // to save the result to a file, but keep it into memory.
//            algoAgrawal.runAlgorithm(patterns  , outputConf,
//            		fp.transactionCount, MIN_CONF);
            algoAgrawal.runAlgorithm(patterns  , outputConf,
            		databaseSize, MIN_CONF);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static Application scanApp(Context context, Application app) {
        boolean b;
        if(app!=null) {
            int list[] = app.getListPermission();
            System.out.println("length: " + list.length);
            if (list.length == 0)
                app.setResult(true);
            else if (list.length < 10) {
                Genrule g = new Genrule(list, context);
                b = g.findRule(context);
                app.setResult(b);
                System.out.println("result: " + app.getName());
            }
        }

        return app;

    }

    public static int[] converStringtoArray(String s) {
        StringTokenizer st = new StringTokenizer(s, " ");
        int arr[] = new int[st.countTokens()];
        int count = 0;
        while (st.hasMoreTokens())
            arr[count++] = Integer.parseInt(st.nextToken());
        Arrays.sort(arr);
        return arr;
    }
    
    
    public static int[] strArrayToIntArray(String[] a){
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = Integer.parseInt(a[i]);
        }
        return b;
    }


}
