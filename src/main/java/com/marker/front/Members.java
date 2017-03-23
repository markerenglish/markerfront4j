package com.marker.front;

import java.sql.*;
import java.util.*;

public class Members {

	private int memberid;
	private String username, password, firstname, lastname, email, type;
	private String regdate;

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
		String query = "SELECT member_id FROM members WHERE username = ?";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
	        conn = createConn();
	        stat = conn.prepareStatement(query);
	        stat.setString(1, username);
			ResultSet rst = stat.executeQuery();
			if(rst.next() == false) {
				conn.close();
				return true;
			}
			else {
				conn.close();
				return false;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeConn(conn, stat);
		}
	}

	private Connection createConn() throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");

		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.91.136/markerdb", 
				"marker", "mattl");

		return conn;
	}
	
	private void closeConn(Connection conn, Statement... statements){
		try{
			for(Statement stat:statements){
			    if(stat != null && !stat.isClosed())
				    stat.close();
			}
			if(conn != null && !conn.isClosed())
		    conn.close();
		}
		catch(Exception e) {
		    e.printStackTrace();
		}
	}
	
	public boolean authenticate() {
		String query = "SELECT * FROM members";
		String DbUsername = "";
		String DbPassword = "";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			ResultSet rst = stat.executeQuery();

			while(rst.next()) {
				DbUsername = rst.getString("username");
				DbPassword = rst.getString("password");

				if(username.equals(DbUsername) && password.equals(DbPassword)) {
					return true;
				}
			}
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}

	public boolean regMember() {
		Connection conn = null;
		PreparedStatement stat = null;

		firstname = "";
		lastname = "";
		email = "";
		type = "Mem";

		String query = 
				"INSERT INTO members (username, password, firstname, lastname, email, regdate, type) VALUES (?,?,?,?,?,?,?)";

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setString(1, username);
			stat.setString(2, password);
			stat.setString(3, firstname);
			stat.setString(4, lastname);
			stat.setString(5, email);
			stat.setDate(6, new java.sql.Date(new java.util.Date().getTime()));
			stat.setString(7, type);
			stat.executeUpdate();
			ResultSet tableKeys = stat.getGeneratedKeys();
			tableKeys.next();
			memberid = tableKeys.getInt(1);

			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}

	public boolean setMember(String username) {
		Connection conn = null;
		PreparedStatement stat = null;
		String query = null;

		try {
			if(password != null) {
				query = "UPDATE members SET password=?, firstname=?, lastname=?, email=? WHERE username=?";
			} else {
				query = "UPDATE members SET firstname=?, lastname=?, email=? WHERE username=?";
			}
			conn = createConn();
			stat = conn.prepareStatement(query);
			int index = 0;
        	if(password != null){
			    stat.setString(1, password);
			    index++;
			}
			stat.setString(++index, firstname);
			stat.setString(++index, lastname);
			stat.setString(++index, email);
			stat.setString(++index, username);
			stat.executeUpdate();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}

	public boolean setMember(int memberid) {
		Connection conn = null;
		PreparedStatement stat = null;
		String query = null;

		try {
			if(password != null) {
				query = "UPDATE members SET password=?, username=?, firstname=?, lastname=?, email=?, type=? WHERE member_id=?";
			} else {
				query = "UPDATE members SET username=?, firstname=?, lastname=?, email=?, type=? WHERE member_id=?";
			}
			conn = createConn();
			stat = conn.prepareStatement(query);
			int index = 0;
        	if(password != null){
			    stat.setString(1, password);
			    index++;
			}
			stat.setString(++index, username);
			stat.setString(++index, firstname);
			stat.setString(++index, lastname);
			stat.setString(++index, email);
			stat.setString(++index, type);
			stat.setInt(++index, memberid);
			stat.executeUpdate();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}

	public boolean getMember(String username) {
		String query = "SELECT * FROM members WHERE username=?";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setString(1, username);
			ResultSet rst = stat.executeQuery();

			while(rst.next()) {
				memberid = rst.getInt("member_id");
				this.username = rst.getString("username");
				firstname = rst.getString("firstname");
				lastname = rst.getString("lastname");
				email = rst.getString("email");
				regdate = rst.getString("regdate");
				type = rst.getString("type");
			}
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}

	public boolean getMember(int memberid) {
		String query = "SELECT * FROM members WHERE member_id=?";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, memberid);
			ResultSet rst = stat.executeQuery();

			while(rst.next()) {
				this.memberid = rst.getInt("member_id");
				username = rst.getString("username");
				firstname = rst.getString("firstname");
				lastname = rst.getString("lastname");
				email = rst.getString("email");
				regdate = rst.getString("regdate");
				type = rst.getString("type");
			}
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}

	public int getNumPosts(int memberid) {
		int numMsgs = 0;
		String query = "SELECT thread_id FROM threads WHERE member_id = ?";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, memberid);
			ResultSet rst = stat.executeQuery();

			while(rst.next()) {
				numMsgs++;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}finally{
			closeConn(conn, stat);
		}
		return numMsgs;
	}

	public List<Integer> getMembers() {
		List<Integer> members = new LinkedList<Integer>();
		String query = "SELECT member_id FROM Members ORDER BY member_id";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			ResultSet rst = stat.executeQuery();

			while(rst.next()) {
				memberid = rst.getInt("member_id");
				members.add(new Integer(memberid));
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}finally{
			closeConn(conn, stat);
		}
		return members;
	}

	public List<Integer> getMods() {
			List<Integer> members = new LinkedList<Integer>();
			String query = "SELECT member_id FROM members WHERE type='Adm' OR type='Mod' ORDER BY member_id";
			//Administrator or Moderator
			Connection conn = null;
			PreparedStatement stat = null;

			try {
				conn = createConn();
				stat = conn.prepareStatement(query);
				ResultSet rst = stat.executeQuery();

				while(rst.next()) {
					memberid = rst.getInt("member_id");
					members.add(new Integer(memberid));
				}

			}
			catch(Exception e) {
				e.printStackTrace();
			}finally{
				closeConn(conn, stat);
			}
			return members;
	}

	public boolean delMember(int memberid) {
		String query = "DELETE FROM members WHERE member_id = ?";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, memberid);
			stat.execute();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}
}