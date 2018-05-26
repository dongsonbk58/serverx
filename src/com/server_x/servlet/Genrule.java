package com.server_x.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.naming.Context;

public class Genrule {
	public ArrayList<Files> ListFile = new ArrayList<Files>();
	public int[] kq;
	public String result = "";
	public int[] list_per_in_rule1;
	public int[] list_per_in_rule2;
	public int[] list_per_in_line1;
	public int[] list_per_in_line2;
    public int trigger = 0;

	 public ArrayList<String>listRule=new ArrayList<String>();

	    public int[] cloneItemSetMinusOneItem(int[] itemset,
	                                          Integer itemToRemove) {
	        // create the new itemset
	        int[] newItemset = new int[itemset.length - 1];
	        int i = 0;
	        // for each item in this itemset
	        for (int j = 0; j < itemset.length; j++) {
	            // copy the item except if it is the item that should be excluded
	            if (itemset[j] != itemToRemove) {
	                newItemset[i++] = itemset[j];
	            }
	        }
	        return newItemset; // return the copy
	    }
	    
	    
	    public void run(int[] lk) {
	        {
	            // create a variable H1 for recursion
	            List<int[]> H1_for_recursion = new ArrayList<int[]>();

	            // For each itemset "itemsetSize1" of size 1 that is member of lk
	            for (int item : lk) {
	                int itemsetHm_P_1[] = new int[]{item};

	                // make a copy of lk without items from hm_P_1
	                int[] itemset_Lk_minus_hm_P_1 = cloneItemSetMinusOneItem(lk,
	                        item);

	                // Now we will calculate the support and confidence
	                // of the rule: itemset_Lk_minus_hm_P_1 ==> hm_P_1
	                // int support = calculateSupport(itemset_Lk_minus_hm_P_1); //
	                // THIS COULD BE
	                // // OPTIMIZED ?
	                // double supportAsDouble = (double) support;

	                // calculate the confidence of the rule :
	                // itemset_Lk_minus_hm_P_1 ==> hm_P_1
	                // double conf = lk.getAbsoluteSupport() / supportAsDouble;

	                // if the confidence is lower than minconf
	                // if(conf < minconf || Double.isInfinite(conf)){
	                // continue;
	                // }

	                // double lift = 0;
	                int supportHm_P_1 = 0;
	                // if the user is using the minlift threshold, we will need
	                // to also calculate the lift of the rule:
	                // itemset_Lk_minus_hm_P_1 ==> hm_P_1

	                // If we are here, it means that the rule respect the minconf
	                // and minlift parameters.
	                // Therefore, we output the rule.
	                // saveRule(itemset_Lk_minus_hm_P_1, support, itemsetHm_P_1,
	                saveRule(itemset_Lk_minus_hm_P_1, itemsetHm_P_1);
	                // supportHm_P_1, lk.getAbsoluteSupport(), conf, lift);

	                // Then we keep the itemset hm_P_1 to find more rules using this
	                // itemset and lk.
	                H1_for_recursion.add(itemsetHm_P_1);
	                // ================ END OF WHAT I HAVE ADDED
	            }
	            // Finally, we make a recursive call to continue explores rules that
	            // can be made with "lk"
	            try {
	                apGenrules(lk.length, 1, lk, H1_for_recursion);
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	    }
	    public String findRuleWithMultiThread(ArrayList<String> listRules, double minconf, Context context, Thread[] threads, Thread[] threadsRule, ArrayList<Files> listfiles)
	            throws Exception {
	        File path = new File(Common.directory+"/fileConf/" + minconf);
	        System.out.println(listRules.size());
	        int size = listRules.size();
	        ListFile = listfiles;
	        result = "";
	        int lengthttt = listRules.get(0).replace(" : ", " ").split(" ").length;
	        ThreadGroup tg2 = new ThreadGroup("ThreadGroup2");
	        for (int threadsRuleCount = 0; threadsRuleCount < threadsRule.length; threadsRuleCount++) {
	            int ltemp = (int) (size / threadsRule.length);
	            int lstart = threadsRuleCount * ltemp;
	            int lend = size - (threadsRule.length - threadsRuleCount - 1) * ltemp;
	            threadsRule[threadsRuleCount] = new Thread(tg2, new RulesThread(lstart, lend, listRules) {
	                @Override
	                public void run() {
	                    out1:
	                    for (int u = lstart; u < lend; u++) {
	                        String rule = listRules.get(u);
	                        for (int item = ListFile.size()-1;item>=0; item--) {
	                            Files file = ListFile.get(item);
	                            int filelengthbegin = file.getLengthbegin();
	                            int filelengthtail = file.getLengthtail();
	                            if (filelengthbegin >= (lengthttt - 1) && filelengthtail >= lengthttt) {
	                                try {
	                                    ArrayList<String> listruleinfile = ListRuleInFile(file.getName(), minconf);
	                                    ThreadGroup tg1 = new ThreadGroup("ThreadGroup");
	                                    for (int k = 0; k < threads.length; k++) {

	                                        int b = file.getCountline();
	                                        int temp = (int) (b / threads.length);
	                                        int start = k * temp;
	                                        int end = b - (threads.length - k - 1) * temp;
	                                        threads[k] = new Thread(tg1, new FindThread(start, end, listruleinfile, rule) {
	                                            @Override
	                                            public void run() {
	                                                list_per_in_rule1 = converStringtoArray(rule.split(":")[0]);
	                                                list_per_in_rule2 = converStringtoArray(rule.split(":")[1]);
	                                                int length = rule.replace(" :", " ").split(" ").length;
	                                                inner:
	                                                for (int i = start; i < end; i++) {
	                                                    String temp1 = listruleinfile.get(i);
	                                                    int lengthPer = temp1.replace(" :", " ").split(" ").length;
	                                                    if (lengthPer >= length) {
	                                                        list_per_in_line1 = converStringtoArray(temp1.split(":")[0]);
	                                                        list_per_in_line2 = converStringtoArray(temp1.split(":")[1]);
	                                                        if (list_per_in_line1.length >= list_per_in_rule1.length
	                                                                && list_per_in_line2.length >= list_per_in_rule2.length) {
	                                                            boolean part1 = arrayContainArray(list_per_in_line1, list_per_in_rule1);
	                                                            boolean part2 = arrayContainArray(list_per_in_line2, list_per_in_rule2);
	                                                            if (part1 == true
	                                                                    && part2 == true) {
	                                                                System.out.println(file.getName());
	                                                                System.out.println(Arrays.toString(list_per_in_line1) +"  "+Arrays.toString(list_per_in_rule1));
	                                                                System.out.println(Arrays.toString(list_per_in_line2) +"  "+Arrays.toString(list_per_in_rule2));
	                                                                result = "normal";
	                                                                trigger = 1;
	                                                                for (int k = 0; k < threads.length; k++) {
	                                                                    if (threads[k] != null) {
	                                                                        if (threads[k].isAlive() == true) {
	                                                                            threads[k].interrupt();
	                                                                        }
	                                                                    }
	                                                                }
	                                                                break inner;
	                                                            }
	                                                        }
	                                                    }
	                                                }
	                                            }
	                                        });
	                                        if (!threads[k].isAlive()) {
	                                            threads[k].start();
	                                        }
	                                    }
	                                } catch (IOException ex) {
//	                                    Logger.getLogger(Genrule.class.getName()).log(Level.SEVERE, null, ex);
	                                }
	                            }
	                            if (result == "normal") {
	                                break out1;
	                            }else{
	                                result = "Abnormal App";
	                            }
	                        }
	                    }
	                    if (result == "normal") {
	                        for (int k = 0; k < threadsRule.length; k++) {
	                            if (threadsRule[k] != null) {
	                                if (threadsRule[k].isAlive() == true) {
	                                    threadsRule[k].interrupt();
	                                }
	                            }
	                        }
	                    }else {
	                        result = "Abnormal App";
	                    }
	                }
	            });
	            if (!threadsRule[threadsRuleCount].isAlive()) {
	                threadsRule[threadsRuleCount].start();
	            }

	        }
	        System.out.print("");
	        while (true) {
	            System.out.print("");
	            if (trigger == 1) {
	                if (result != "normal") {
	                    result = "Abnormal App";
	                }
	                break;
	            }
	        }
	        return result;
	    }

	    
	    
	    public String findRuleWithMultiThread(ArrayList<String> listRules, double minconf, Context context, Thread[] threads, ArrayList<Files> listfiles)
				throws Exception {
			File path = new File(Common.directory+"/fileConf/" + minconf);
//			System.out.println(listRules.size());
			ListFile = listfiles;
			kq = new int[listRules.size()];
			int lengthttt = listRules.get(0).replace(" : ", " ").split(" ").length;
			for (int i = 0; i < kq.length; i++) {
				kq[i] = 0;
			}
			result = "";
			out1:
			for (String rule : listRules) {
				for (Files file : ListFile) {
					int filelengthbegin = file.getLengthbegin();
					int filelengthtail = file.getLengthtail();
					if (filelengthbegin >= lengthttt - 1 && filelengthtail >= lengthttt){
						ArrayList<String> listruleinfile = ListRuleInFile(file.getName(), minconf);
						ThreadGroup tg1 = new ThreadGroup("ThreadGroup");
						for (int k = 0; k < threads.length; k++) {
							int b = file.getCountline();
							int temp = (int) (b / threads.length);
							int start = k * temp;
							int end = b - (threads.length - k - 1) * temp;
							threads[k] = new Thread(tg1, new FindThread(start, end, listruleinfile, rule) {
								@Override
								public void run() {
									list_per_in_rule1 = converStringtoArray(rule.split(":")[0]);
									list_per_in_rule2 = converStringtoArray(rule.split(":")[1]);
									int length = rule.replace(" : ", " ").split(" ").length;
									inner: for (int i = start; i < end; i++) {
										String temp1 = listruleinfile.get(i);
										int lengthPer = temp1.replace(" : ", " ").split(" ").length;
										if (lengthPer >= length) {
											list_per_in_line1 = converStringtoArray(temp1.split(":")[0]);
											list_per_in_line2 = converStringtoArray(temp1.split(":")[1]);
											if (list_per_in_line1.length >= list_per_in_rule1.length
													&& list_per_in_line2.length >= list_per_in_rule2.length) {
												boolean part1 = arrayContainArray(list_per_in_rule1, list_per_in_line1);
												boolean part2 = arrayContainArray(list_per_in_rule2, list_per_in_line2);
												if ( part1== true
														&& part2 == true) {
													result = "normal";
													for (int k = 0; k < threads.length; k++) {
														if (threads[k].isAlive() == true) {
															threads[k].interrupt();
														}
													}
													break inner;
												}
											}
										}
									}
								}
							});
							threads[k].start();
						}
					}
					if(result == "normal"){
						break out1;
					}else{
						result = "Abnormal App";
					}
				}
				if(result == "normal"){
					break out1;
				}else{
					result = "Abnormal App";
				}
			}
			return result;
		}
	    
		public ArrayList<String> ListRuleInFile(String name, double minconf) throws IOException {
			File path = new File(Common.directory+"/fileConf/" + minconf);
			ArrayList<String> a = new ArrayList<String>();
			File file = new File(path, name);
			FileReader fileReader;
			fileReader = new FileReader(file);
			BufferedReader bfr = new BufferedReader(fileReader);
			String line;
			while ((line = bfr.readLine()) != null) {
				a.add(line);
			}
			fileReader.close();
			bfr.close();
			return a;
		}
	    
		public static boolean arrayContainArray(int[] a, int[] b) {
			boolean test = false;
			int count = 0;
			for (int item : b){
				for(int item1: a){
					if(item1 == item){
						count ++;
					}
				}
			}
			if(count == b.length){
				test = true;
			}
			return test;
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
		
	    public void apGenrules(int k, int m, int[] lk, List<int[]> Hm)
	            throws IOException {

	        // if the itemset "lk" that is used to generate rules is larger than the
	        // size of itemsets in "Hm"
	        if (k > m + 1) {
	            // Create a list that we will be used to store itemsets for the
	            // recursive call
	            List<int[]> Hm_plus_1_for_recursion = new ArrayList<int[]>();

	            // generate candidates using Hm
	            List<int[]> Hm_plus_1 = generateCandidateSizeK(Hm);

	            // for each such candidates
	            for (int[] hm_P_1 : Hm_plus_1) {

	                // We subtract the candidate from the itemset "lk"
	                int[] itemset_Lk_minus_hm_P_1 = cloneItemSetMinusAnItemset(lk,
	                        hm_P_1);

	                // We will now calculate the support of the rule Lk/(hm_P_1) ==>
	                // hm_P_1
	                // we need it to calculate the confidence

	                // if the confidence is not enough than we don't need to
	                // consider
	                // the rule Lk/(hm_P_1) ==> hm_P_1 anymore so we continue

	                // if the user is using the minlift threshold, then we will need
	                // to calculate the lift of the
	                // rule as well and check if the lift is higher or equal to
	                // minlift.

	                // The rule has passed the confidence and lift threshold
	                // requirements,
	                // so we can output it
	                saveRule(itemset_Lk_minus_hm_P_1, hm_P_1);
	                // supportHm_P_1, lk.getAbsoluteSupport(), conf, lift);

	                // if k == m+1, then we cannot explore further rules using Lk
	                // since Lk will be too small.
	                if (k != m + 1) {
	                    Hm_plus_1_for_recursion.add(hm_P_1);
	                }

	            }
	            // recursive call to apGenRules to find more rules using "lk"
	            apGenrules(k, m + 1, lk, Hm_plus_1_for_recursion);
	        }

	    }

	    public List<int[]> generateCandidateSizeK(List<int[]> levelK_1) {
	        // create a variable to store candidates
	        List<int[]> candidates = new ArrayList<int[]>();

	        // For each itemset I1 and I2 of level k-1
	        loop1:
	        for (int i = 0; i < levelK_1.size(); i++) {
	            int[] itemset1 = levelK_1.get(i);
	            loop2:
	            for (int j = i + 1; j < levelK_1.size(); j++) {
	                int[] itemset2 = levelK_1.get(j);

	                // we compare items of itemset1 and itemset2.
	                // If they have all the same k-1 items and the last item of
	                // itemset1 is smaller than
	                // the last item of itemset2, we will combine them to generate a
	                // candidate
	                for (int k = 0; k < itemset1.length; k++) {
	                    // if they are the last items
	                    if (k == itemset1.length - 1) {
	                        // the one from itemset1 should be smaller (lexical
	                        // order)
	                        // and different from the one of itemset2
	                        if (itemset1[k] >= itemset2[k]) {
	                            continue loop1;
	                        }
	                    }
	                    // if they are not the last items, and
	                    else if (itemset1[k] < itemset2[k]) {
	                        continue loop2; // we continue searching
	                    } else if (itemset1[k] > itemset2[k]) {
	                        continue loop1; // we stop searching: because of lexical
	                        // order
	                    }
	                }

	                // Create a new candidate by combining itemset1 and itemset2
	                int lastItem1 = itemset1[itemset1.length - 1];
	                int lastItem2 = itemset2[itemset2.length - 1];
	                int newItemset[];
	                if (lastItem1 < lastItem2) {
	                    // Create a new candidate by combining itemset1 and itemset2
	                    newItemset = new int[itemset1.length + 1];
	                    System.arraycopy(itemset1, 0, newItemset, 0,
	                            itemset1.length);
	                    newItemset[itemset1.length] = lastItem2;
	                    candidates.add(newItemset);
	                } else {
	                    // Create a new candidate by combining itemset1 and itemset2
	                    newItemset = new int[itemset1.length + 1];
	                    System.arraycopy(itemset2, 0, newItemset, 0,
	                            itemset2.length);
	                    newItemset[itemset2.length] = lastItem1;
	                    candidates.add(newItemset);
	                }

	            }
	        }
	        // return the set of candidates
	        return candidates;
	    }

	    public int[] cloneItemSetMinusAnItemset(int[] itemset,
	                                            int[] itemsetToNotKeep) {
	        // create a new itemset
	        int[] newItemset = new int[itemset.length - itemsetToNotKeep.length];
	        int i = 0;
	        // for each item of this itemset
	        for (int j = 0; j < itemset.length; j++) {
	            // copy the item except if it is not an item that should be excluded
	            if (Arrays.binarySearch(itemsetToNotKeep, itemset[j]) < 0) {
	                newItemset[i++] = itemset[j];
	            }
	        }
	        return newItemset; // return the copy
	    }


	    public void saveRule(int[] itemset1, int[] itemset2) {
	        StringBuilder buffer = new StringBuilder();
	        // write itemset 1
	        for (int i = 0; i < itemset1.length; i++) {
	            buffer.append(itemset1[i]);
	            if (i != itemset1.length - 1) {
	                buffer.append(" ");
	            }
	        }
	        // write separator
	        buffer.append(" : ");
	        // write itemset 2
	        for (int i = 0; i < itemset2.length; i++) {
	            buffer.append(itemset2[i]);
	            if (i != itemset2.length - 1) {
	                buffer.append(" ");
	            }
	        }
	        listRule.add(buffer.toString());
	    }
	    
	    public String findRule1(Context context) {
	        for(String rule:listRule)
	        try {
//	        	Path currentRelativePath = Paths.get("");
//	        	String path = currentRelativePath.toAbsolutePath().toString();
	        	URL url = getClass().getResource("conf.txt");
	        	File file = new File(url.toURI());
	        	FileReader fileReader = new FileReader(file);
	    		BufferedReader bfr = new BufferedReader(fileReader);
	            String line;
	            while ((line = bfr.readLine()) != null) {
	                if (line.equals(rule))
	                    return "normal";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return "malware";
	    }

	    public boolean findRule(Context context) {
	        for(String rule:listRule)
	        try {
	        	URL url = getClass().getResource("conf.txt");
	        	File file = new File(url.toURI());
	        	FileReader fileReader = new FileReader(file);
	    		BufferedReader bfr = new BufferedReader(fileReader);
	            String line;
	            while ((line = bfr.readLine()) != null) {
	                if (line.equals(rule))
	                    return true;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	    }

	    public Genrule(int[] rule,Context context) {
	        run(rule);

	    }

	    public void genRule(int []rule){
	        StringBuilder sb=new StringBuilder();
	        for(int i:rule)
	            for(int j:rule)
	                if(i!=j){
	                    sb.append(i+" : "+j);
	                    listRule.add(sb.toString());
	                }
	    }
	    
	    
	    
}
