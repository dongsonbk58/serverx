package com.server_x.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.jdbc.DatabaseMetaData;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

/**
 * Servlet implementation class FileServlet
 */
@WebServlet("/FileServlet")
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int maxMemSize = 4 * 1024;
	private String filePath;
	private File file;
	public static final double MIN_SUP = 0.02;
	public static final double MIN_CONF = 0.75;
	static ArrayList<String> transacsion = new ArrayList<String>();
	public static Context context;
	public static ArrayList<Application> listApp = new ArrayList<Application>();
	public static int number_thread = 100;
	public static int number_thread_Rules = 10;
	static Map<String, Integer> map = new HashMap<String, Integer>();



	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileServlet() {
		super();
	}

	public void init() {
		// Get the file location where it would be stored.
		filePath = getServletContext().getInitParameter("file-upload");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/homeView.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		JsonObject json = new JsonObject();
		String imei = request.getParameter("imei");
		String ip = request.getParameter("ip");
		String ten = request.getParameter("ten");
		String lop = request.getParameter("lop");
		String malop = request.getParameter("malop");
		String mahocphan = request.getParameter("mahocphan");
		String masinhvien = request.getParameter("masinhvien");

		User user = new User(ten, lop, masinhvien, malop, mahocphan, imei);
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			try {
				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (!item.isFormField()) {
						String fileName = item.getName();
						System.out.println("result: " + item.getName());
						File path = new File(Common.directory+ "/fileuploads");
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}
						File uploadedFile = new File(path + "/" + fileName);
						item.write(uploadedFile);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Context context = null;
		File file = new File(Utils.getDirectory(), "transaction_" + imei + ".txt");
		FileReader fileReader = new FileReader(file);
		String temp;
		ArrayList<String> listRule = new ArrayList<String>();
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((temp = bufferedReader.readLine()) != null) {
			listRule.add(temp);
		}

		//		try {
		//			Connection conn = DBconnection.getConnection();
		//			PreparedStatement Stmt = null;
		//			Statement st = null;
		//			if (DBconnection.checkTableUser(Stmt, conn) == false) {
		//				DBconnection.createTableUser(Stmt, conn);
		//			}
		//			
		//				if (DBconnection.checkTable(Stmt, conn, imei) == true && DBconnection.checkUser(conn, st, user) == true) {
		//					System.out.println("mobile data is exist");
		//					if(ten != null){
		//					DBconnection.updateUser(Stmt, conn, user);
		//					}
		//				} else {
		//					
		//					if (DBconnection.checkTable(Stmt, conn, imei) == true
		//							&& DBconnection.checkUser(conn, st, user) == false) {
		//						if(ten != null){
		//						DBconnection.insertTableUser(Stmt, conn, user);
		//						}
		//					} else {
		//						
		//						DBconnection.createTable(Stmt, conn, imei);
		//						if(ten != null){
		//						DBconnection.insertTableUser(Stmt, conn, user);
		//						}
		//					}
		//				}
		//			
		//			for (String list : listRule) {
		//				DBconnection.insertTable(Stmt, conn, list, imei);
		//			}
		//			conn.close();
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}

		//		ArrayList<User> alluser = new ArrayList<User>();
		//
		//		try {
		//			Connection conn = (Connection) DBconnection.getConnection();
		//			PreparedStatement Stmt = null;
		//			ResultSet rs = DBconnection.getUser(Stmt, conn);
		//			while (rs.next()) {
		//				User user1 = new User(rs.getString("ten"), rs.getString("lop"), rs.getString("masinhvien"),
		//						rs.getString("malop"), rs.getString("mahocphan"), rs.getString("imei"));
		//				alluser.add(user1);
		//			}
		//			String json1 = new Gson().toJson(alluser);
		//
		//			String path = getServletContext().getRealPath("WEB-INF/../");
		//			try (FileWriter file1 = new FileWriter(path + "student.json")) {
		//
		//				file1.write("{\"" + "student" + "\"" + ":" + json1.toString() + "}");
		//				file1.flush();
		//
		//			} catch (IOException e) {
		//				e.printStackTrace();
		//			}
		//			conn.close();
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}

		try {
			System.out.println("complete");
			System.out.println(ip);
			ArrayList<KQ> a = checkMalware("transaction_" + imei + ".txt");
//			ArrayList<KQ> a = checkMalware("transaction_000000000000000.txt");
			String json1 = new Gson().toJson(a);

			String path = getServletContext().getRealPath("WEB-INF/../");
			try (FileWriter file1 = new FileWriter(path + "kq.json")) {

				file1.write("{\"" + "kq" + "\"" + ":" + json1.toString()+"}");
				file1.flush();
				

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getwhiteList(File inputFolder, int threshold) {
		ArrayList<String> whiteList = new ArrayList<String>();
		File allfilecheck = new File(Common.directory+"/allapptowhitelist");
		if (inputFolder.exists()) {
			File[] files = inputFolder.listFiles();
			for (int i = 0; i < files.length; i++) {
				try {
					FileReader fileReader = new FileReader(files[i]);
					BufferedReader bfr = new BufferedReader(fileReader);
					String line;
					while ((line = bfr.readLine()) != null) {
						String a = line.split(":")[0];
						if (!map.containsKey(a)) {
							map.put(a, 1);
						} else {
							int count = map.get(a);
							map.replace(a, count + 1);
						}
					}
					bfr.close();
					fileReader.close();
				} catch (Exception e) {
				}
			}
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				if (entry.getValue() > threshold) {
					whiteList.add(entry.getKey());
				}
			}
			for (String item : getlistaddtowhitelist(allfilecheck)) {
				if (!whiteList.contains(item)) {
					whiteList.add(item);
				}
			}
		}
		return whiteList;
	}

	public static ArrayList<String> getlistaddtowhitelist(File inputFolder) {
		ArrayList<String> whiteList = new ArrayList<String>();
		if (inputFolder.exists()) {
			File[] files = inputFolder.listFiles();
			for (int i = 0; i < files.length; i++) {
				try {
					FileReader fileReader = new FileReader(files[i]);
					BufferedReader bfr = new BufferedReader(fileReader);
					String line;
					while ((line = bfr.readLine()) != null) {
						String a = line.split(":")[0];
						if (!whiteList.contains(a)) {
							whiteList.add(a);
						}
					}
					bfr.close();
					fileReader.close();
				} catch (Exception e) {
				}
			}
		}
		return whiteList;
	}

	public static ArrayList<KQ> checkMalware(String fileInput) throws Exception {
		KQ kq1;
		ArrayList<KQ> KQjson = new ArrayList<KQ>();
		File file = new File(Utils.getDirectory(), fileInput);
		FileReader fileReader = new FileReader(file);
		Context context = null;
		String temp;
		ArrayList<String> listRule = new ArrayList<String>();
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((temp = bufferedReader.readLine()) != null) {
			listRule.add(temp);
		}
		fileReader.close();
		bufferedReader.close();
		File file1 = new File(Common.directory+"/whitelist");
		File file2 = new File(Common.directory+"/malware");
		ArrayList<String> whiteList = getwhiteList(file1, 30);
		ArrayList<String> malwareList = getlistaddtowhitelist(file2);

		Thread[] threads = new Thread[number_thread];
		File path = new File(Common.directory+"/fileConf/" + MIN_CONF);
		ArrayList<Files> listfiless = ListFileInDir(path);
		ArrayList<String> listlist = ListRuleInFile(listfiless.get(listfiless.size() - 1).getName(), MIN_CONF);
		String lastrule = listlist.get(listlist.size() - 1);
		int lastlength = lastrule.replace(" : ", " ").split(" ").length;
		ArrayList<Files> files = ListFileInDirWithCountLineForSplit(path, MIN_CONF);

		try {
			for (String list : listRule) {
				String[] list1 = list.split(":");
				if (malwareList.contains(list1[0])) {
					System.out.println("result: " + list1[0] + "==> " + list1[1] + "==>Abnormal App");
					kq1 = new KQ(list1[0],"Abnormal App");
					KQjson.add(kq1);
				}else {
					if (whiteList.contains(list1[0])) {
						System.out.println("result: " + list1[0] + "==> " + list1[1] + "==>normal");
						kq1 = new KQ(list1[0],"normal");
						KQjson.add(kq1);
					} else {
						if (list1.length > 2) {
							String[] f = list1[1].split(" ");
							if (f.length == 0 || f.length == 1) {
								System.out.println(
										"result: " + list1[0] + "==> " + list1[1] + "==> " + list1[2] + "=>normal");
								kq1 = new KQ(list1[0],"normal");
								KQjson.add(kq1);
							} else {
								if (f.length > lastlength) {
									System.err.println(
											"result: " + list1[0] + "==> " + list1[1] + "==> " + list1[2] + "=>Abnormal App");
									kq1 = new KQ(list1[0],"Abnormal App");
									KQjson.add(kq1);
								} else {
									String[] ary = list1[1].split(" ");
									int[] a = Utils.strArrayToIntArray(ary);
									a = quickSort(a, 0, a.length - 1);
									Genrule g = new Genrule(a, context);
									String c = Arrays.toString(a).replaceAll(", ", " ").replaceAll("[\\[\\]]", "");
									String b = g.findRuleWithMultiThread(g.listRule, MIN_CONF, context, threads, files);
									if (b == "Abnormal App") {
										System.err
										.println("result: " + list1[0] + "==> " + c + "==> " + list1[2] + "=>" + b);
									} else {
										System.out
										.println("result: " + list1[0] + "==> " + c + "==> " + list1[2] + "=>" + b);
									}
									kq1 = new KQ(list1[0],b);
									KQjson.add(kq1);
								}
							}
						} else {
							System.out.println("result: " + list1[0] + "==> normal");
							kq1 = new KQ(list1[0] ,"normal");
							KQjson.add(kq1);
						}
					}
				}
			}
		} catch (NumberFormatException e) {
		}
		return KQjson;
	}


	public static void checkMalware(String fileInput, String fileOutput) throws Exception {
		File file = new File(Utils.getDirectory(), fileInput);
		FileReader fileReader = new FileReader(file);
		Context context = null;
		String temp;
		ArrayList<String> listRule = new ArrayList<String>();
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((temp = bufferedReader.readLine()) != null) {
			listRule.add(temp);
		}
		fileReader.close();
		bufferedReader.close();
		File file1 = new File(Common.directory+"/whitelist");
		ArrayList<String> whiteList = getwhiteList(file1, 30);

		Thread[] threads = new Thread[number_thread];
		Thread[] threadsRule = new Thread[number_thread_Rules];
		Thread[] threadsRule1;
		File path = new File(Common.directory+"/fileConf/" + MIN_CONF);
		ArrayList<Files> listfiless = ListFileInDir(path);
		ArrayList<String> listlist = ListRuleInFile(listfiless.get(listfiless.size() - 1).getName(), MIN_CONF);
		String lastrule = listlist.get(listlist.size() - 1);
		int lastlength = lastrule.replace(" : ", " ").split(" ").length;
		ArrayList<Files> files = ListFileInDirWithCountLineForSplit(path, MIN_CONF);

		try {
			for (String list : listRule) {
				String b = "";
				String[] list1 = list.split(":");
				if (whiteList.contains(list1[0])) {
					System.out.println("result: " + list1[0] + "==> " + list1[1] + "==>normal (whitelist)");
				} else {
					if (list1.length > 2) {
						String[] f = list1[1].split(" ");
						if (f.length == 0 || f.length == 1) {
							System.out.println(
									"result: " + list1[0] + "==> " + list1[1] + "==> " + list1[2] + "==>normal");
						} else {
							if (f.length > lastlength) {
								System.err.println(
										"result: " + list1[0] + "==> " + list1[1] + "==> " + list1[2] + "=>Abnormal App");
							} else {
								String[] ary = list1[1].split(" ");
								int[] a = Utils.strArrayToIntArray(ary);
								a = quickSort(a, 0, a.length - 1);
								Genrule g = new Genrule(a, context);
								String c = Arrays.toString(a).replaceAll(", ", " ").replaceAll("[\\[\\]]", "");
								if (g.listRule.size() > number_thread_Rules) {
									b = g.findRuleWithMultiThread(g.listRule, MIN_CONF, context, threads, threadsRule, files);
								}else{
									threadsRule1 = new Thread[g.listRule.size()];
									b = g.findRuleWithMultiThread(g.listRule, MIN_CONF, context, threads, threadsRule1, files);
								}

								if (b == "Abnormal App") {
									System.err
									.println("result: " + list1[0] + "==> " + c + "==> " + list1[2] + "=>" + b);
								} else {
									System.out
									.println("result: " + list1[0] + "==> " + c + "==> " + list1[2] + "=>" + b);
								}
							}
						}
					} else {
						System.out.println("result: " + list1[0] + "==> normal");
					}
				}
			}
		} catch (NumberFormatException e) {
		}
	}

	public static ArrayList<Files> ListFileInDirWithCountLine(File dir, double minconf) throws IOException {
		File path = new File(Common.directory+"/fileConf/" + minconf);
		ArrayList<Files> a = new ArrayList<Files>();
		int count = 0;
		int total = 0;
		for (File file : dir.listFiles()) {
			count++;
		}
		for (int i = 0; i < count; i++) {
			File file = new File(path, "conf" + i + ".txt");
			FileReader fileReader;
			fileReader = new FileReader(file);
			BufferedReader bfr = new BufferedReader(fileReader);
			String line;
			while ((line = bfr.readLine()) != null) {
				total++;
			}
			a.add(new Files("conf" + i + ".txt", total));
			total = 0;
			bfr.close();
			fileReader.close();
		}
		return a;
	}

	public static ArrayList<Files> ListFileInDirWithCountLineForSplit(File dir, double minconf) throws IOException {
		File path = new File(Common.directory+"/fileConf/" + minconf);
		ArrayList<Files> a = new ArrayList<Files>();
		ArrayList<String> b = new ArrayList<String>();
		int count = 0;
		int total = 0;
		int lengthbegin = 0;
		int lengthtail = 0;
		for (File file : dir.listFiles()) {
			count++;
		}
		for (int i = count - 1; i >= 0; i--) {
			File file = new File(path, "conf" + i + ".txt");
			FileReader fileReader;
			fileReader = new FileReader(file);
			BufferedReader bfr = new BufferedReader(fileReader);
			String line;
			while ((line = bfr.readLine()) != null) {
				b.add(line);
			}
			lengthbegin = b.get(0).replaceAll(":", "").split(" ").length;
			lengthtail = b.get(b.size() - 1).replaceAll(":", "").split(" ").length;
			total = b.size();
			bfr.close();
			fileReader.close();
			b.clear();
			a.add(new Files("conf" + i + ".txt", total, lengthbegin, lengthtail));
		}
		return a;
	}

	public final static void clearConsole() {
		try {
			final String os = System.getProperty("os.name");

			if (os.contains("Windows")) {
				Runtime.getRuntime().exec("cls");
			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (final Exception e) {
			// Handle any exceptions.
		}
	}

	public static ArrayList<Files> ListFileInDir(File dir) {
		ArrayList<Files> a = new ArrayList<Files>();
		int count = 0;
		for (File file : dir.listFiles()) {
			count++;
		}
		for (int i = 0; i < count; i++) {
			a.add(new Files("conf" + i + ".txt", false));
		}
		return a;
	}

	public static ArrayList<String> ListRuleInFile(String name, double minconf) throws IOException {
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

	public static int[] quickSort(int[] arr, int low, int high) {
		if (arr == null || arr.length == 0)
			return null;

		if (low >= high)
			return null;

		// pick the pivot
		int middle = low + (high - low) / 2;
		int pivot = arr[middle];

		// make left < pivot and right > pivot
		int i = low, j = high;
		while (i <= j) {
			while (arr[i] < pivot) {
				i++;
			}

			while (arr[j] > pivot) {
				j--;
			}

			if (i <= j) {
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
				i++;
				j--;
			}
		}

		// recursively sort two sub parts
		if (low < j)
			quickSort(arr, low, j);

		if (high > i)
			quickSort(arr, i, high);

		return arr;
	}

}
