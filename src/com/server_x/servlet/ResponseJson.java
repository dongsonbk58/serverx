package com.server_x.servlet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.mysql.jdbc.ResultSet;

@WebServlet("/ResponseJson")
public class ResponseJson extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ResponseJson() {
		super();

	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//		String imei = request.getParameter("imei");
//		response.setContentType("text/html; charset=UTF-8");
//		response.setCharacterEncoding("utf-8");
//		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
//		PrintWriter out = response.getWriter();
//		try {
//			Connection conn = DBconnection.getConnection();
//			PreparedStatement Stmt = null;
//			ResultSet rs = DBconnection.getUser(Stmt, conn);
//			   while(rs.next()){
//				out.println(rs.getString("ten")+"............."+rs.getString("lop")+"............."+rs.getString("masinhvien")+"............."+rs.getString("mahocphan")+"............."+rs.getString("malop"));	
//			   }
//				  
//			conn.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	       RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/listStudent.jsp");
	       dispatcher.forward(request, response);
		

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);

		ArrayList<User> alluser = new ArrayList<User>();
		
		try {
			Connection conn = (Connection) DBconnection.getConnection();
			PreparedStatement Stmt = null;
			ResultSet rs = DBconnection.getUser(Stmt, conn);
			while (rs.next()) {
				User user = new User(rs.getString("ten"), rs.getString("lop"), rs.getString("masinhvien"),
						rs.getString("malop"), rs.getString("mahocphan"), rs.getString("imei"));
				alluser.add(user);
			}
			String json = new Gson().toJson(alluser);
			
			String path = getServletContext().getRealPath("WEB-INF/../");
	        try (FileWriter file = new FileWriter(path+"student.json")) {

	            file.write("{\""+"student"+"\""+":"+json.toString()+"}");
	            file.flush();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
