package com.marker.front;

import java.sql.*;
import java.util.*;

public class Members {
	private int memberid;
	private String username, password, firstname, lastname, email, regdate, type;

	public int getMemberid() {
		return memberid;
	}

	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean checkReg(String username) {
		String query = "SELECT member_id FROM Members WHERE [username]='" + username + "'";

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			ResultSet rst = stat.executeQuery(query);
			if(rst.next() == false) {
				con.close();
				return true;
			}
			else {
				con.close();
				return false;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean authenticate() {
		String query = "SELECT * FROM Members";
		String DbUsername = "";
		String DbPassword = "";

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			ResultSet rst = stat.executeQuery(query);

			while(rst.next()) {
				DbUsername = rst.getString("username");
				DbPassword = rst.getString("password");

				if(username.equals(DbUsername) && password.equals(DbPassword)) {
					con.close();
					return true;
				}
			}
			return false;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean regMember() {
		memberid = (int) (1000 + Math.random()* 1000);
		firstname = "";
		lastname = "";
		email = "";
		regdate = new java.util.Date().toString();
		type = "Member";

		String query = "INSERT INTO Members (member_id, [username], [password], [firstname], [lastname], [email], [regdate], [type]) " +
						"VALUES (" + memberid + ", " +
						"'" + username + "', " +
						"'" + password + "', " +
						"'" + firstname + "', " +
						"'" + lastname + "', " +
						"'" + email + "', " +
						"'" + regdate + "', " +
						"'" + type + "')";
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			stat.execute(query);
			con.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setMember(String username) {
		String password_upd = "";

		if(password != null) {
			password_upd = "password='" + password + "', ";
		}

		String query = "UPDATE Members SET " + password_upd +
						"firstname='" + firstname + "', " +
						"lastname='" + lastname + "', " +
						"email='" + email + "' WHERE [username]='" + username + "'";

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			stat.execute(query);
			con.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setMember(int memberid) {
		String password_upd = "";

		if(password != null) {
			password_upd = "password='" + password + "', ";
		}

		String query = "UPDATE Members SET " + password_upd +
						"username='" + username + "', " +
						"firstname='" + firstname + "', " +
						"lastname='" + lastname + "', " +
						"email='" + email + "', " +
						"type='" + type + "' WHERE member_id=" + memberid;

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			stat.execute(query);
			con.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean getMember(String username) {
		String query = "SELECT * FROM Members WHERE [username]='" + username + "'";

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			ResultSet rst = stat.executeQuery(query);

			while(rst.next()) {
				memberid = rst.getInt("member_id");
				this.username = rst.getString("username");
				firstname = rst.getString("firstname");
				lastname = rst.getString("lastname");
				email = rst.getString("email");
				regdate = rst.getString("regdate");
				type = rst.getString("type");
			}
			con.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean getMember(int memberid) {
		String query = "SELECT * FROM Members WHERE member_id=" + memberid;

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			ResultSet rst = stat.executeQuery(query);

			while(rst.next()) {
				this.memberid = rst.getInt("member_id");
				username = rst.getString("username");
				firstname = rst.getString("firstname");
				lastname = rst.getString("lastname");
				email = rst.getString("email");
				regdate = rst.getString("regdate");
				type = rst.getString("type");
			}
			con.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public int getNumPosts(int memberid) {
		int numMsgs = 0;
		String query = "SELECT thread_id FROM Threads WHERE member_id=" + memberid;

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			ResultSet rst = stat.executeQuery(query);

			while(rst.next()) {
				numMsgs++;
			}
			con.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return numMsgs;
	}

	public List getMembers() {
		List members = new LinkedList();
		String query = "SELECT member_id FROM Members ORDER BY member_id";

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			ResultSet rst = stat.executeQuery(query);

			while(rst.next()) {
				memberid = rst.getInt("member_id");
				members.add(new Integer(memberid));
			}

			con.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return members;
	}

	public List getMods() {
			List members = new LinkedList();
			String query = "SELECT member_id FROM Members WHERE type='Administrator' OR type='Moderator' ORDER BY member_id";

			try {
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
				Statement stat = con.createStatement();
				ResultSet rst = stat.executeQuery(query);

				while(rst.next()) {
					memberid = rst.getInt("member_id");
					members.add(new Integer(memberid));
				}

				con.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return members;
	}

	public boolean delMember(int memberid) {
		String query = "DELETE FROM Members WHERE member_id=" + memberid;

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			stat.execute(query);
			con.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}