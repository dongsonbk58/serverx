package com.server_x.servlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public HomeServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ArrayList<User> alluser = new ArrayList<User>();
		


		ServletOutputStream outputStream = response.getOutputStream();
		response.setContentType("application/json;charset=UTF-8");
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
			outputStream.print("{\""+"student"+"\""+":"+json.toString()+"}");
			
			String path = getServletContext().getRealPath("WEB-INF/../");
	        try (FileWriter file = new FileWriter(path+"student.json")) {

	        	file.write("{\"" + "student" + "\"" + ":" + json.toString() + "}");
	            file.flush();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


	}
}