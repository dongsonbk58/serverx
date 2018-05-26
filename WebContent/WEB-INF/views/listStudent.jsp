<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home Page</title>
</head>
<body>

	<jsp:include page="_header.jsp"></jsp:include>
	<jsp:include page="_menu.jsp"></jsp:include>
	<%@page import="java.sql.DriverManager"%>
	<%@page import="java.sql.ResultSet"%>
	<%@page import="java.sql.Statement"%>
	<%@page import="java.sql.PreparedStatement"%>
	<%@page import="java.sql.Connection"%>
	<%@page import="com.server_x.servlet.DBconnection"%>



	<h3>Home Page</h3>

	This is website demo for Frequent Pattern
	<br>
	<br>
	<h2 align="center">
		<font><strong>List student in Frequent Pattern</strong></font>
	</h2>
	<table align="center" cellpadding="5" cellspacing="5" border="1">
		<tr>

		</tr>
		<tr bgcolor="#A52A2A">
			<td><b>ten</b></td>
			<td><b>lop</b></td>
			<td><b>ma lop</b></td>
			<td><b>ma hoc phan</b></td>
			<td><b>ma sinh vien</b></td>
			<td><b>imei</b></td>
		</tr>
		<%
			try {
				Connection conn = DBconnection.getConnection();
				PreparedStatement Stmt = null;

				ResultSet resultSet = DBconnection.getUser(Stmt, conn);
				while (resultSet.next()) {
		%>
		<tr bgcolor="#DEB887">

			<td><%=resultSet.getString("ten")%></td>
			<td><%=resultSet.getString("lop")%></td>
			<td><%=resultSet.getString("malop")%></td>
			<td><%=resultSet.getString("mahocphan")%></td>
			<td><%=resultSet.getString("masinhvien")%></td>
			<td><%=resultSet.getString("imei")%></td>

		</tr>

		<%
			}

			} catch (Exception e) {
				e.printStackTrace();
			}
		%>
	</table>
	<jsp:include page="_footer.jsp"></jsp:include>
</body>
</html>