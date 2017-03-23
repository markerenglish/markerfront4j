package com.marker.front;

import java.sql.*;
import java.util.*;

public class Threads {
	private int threadid, parentid, boxid, memberid;
	private long postdate;
	private String subject, posttext;
	private final int LAST_POSTS = 5;

	public int getThreadid() {
		return threadid;
	}

	public void setThreadid(int threadid) {
		this.threadid = threadid;
	}

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public int getBoxid() {
		return boxid;
	}

	public void setBoxid(int boxid) {
		this.boxid = boxid;
	}

	public int getMemberid() {
		return memberid;
	}

	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPosttext() {
		return posttext;
	}

	public void setPosttext(String posttext) {
		this.posttext = posttext;
	}

	public long getPostdate() {
		return postdate;
	}

	public void setPostdate(long postdate) {
		this.postdate = postdate;
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


	public int getNumThreads(int boxid) {
		int numThreads = 0;
		String query = "SELECT thread_id FROM threads WHERE box_id=? AND parent_id=0 ORDER BY post_date DESC";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, boxid);
			ResultSet rst = stat.executeQuery();

			while(rst.next()) {
				numThreads++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			closeConn(conn, stat);
		}
		return numThreads;
	}

	public int getNumMsgs(int boxid) {
		int numMsgs = 0;
		String query = "SELECT thread_id FROM threads WHERE box_id=?";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, boxid);
			ResultSet rst = stat.executeQuery();

			while(rst.next()) {
				numMsgs++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			closeConn(conn, stat);
		}
		return numMsgs;
	}

	public List<Integer> getThreads(int boxid) {
		List<Integer> threads = new LinkedList<Integer>();
		String query = "SELECT thread_id FROM threads WHERE box_id=? AND parent_id=0 ORDER BY post_date DESC";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, boxid);
			ResultSet rst = stat.executeQuery();

			while(rst.next()) {
				threadid = rst.getInt("thread_id");
				threads.add(new Integer(threadid));
			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			closeConn(conn, stat);
		}
		return threads;
	}

	public List<Integer> getReplies(int parentid) {
		List<Integer> threads = new LinkedList<Integer>();
		String query = "SELECT thread_id FROM threads WHERE parent_id=? ORDER BY post_date";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, parentid);
			ResultSet rst = stat.executeQuery();

			while(rst.next()) {
				threadid = rst.getInt("thread_id");
				threads.add(new Integer(threadid));
			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			closeConn(conn, stat);
		}
		return threads;
	}

	public List<Integer> getLastThreads(int boxid) {
		List<Integer> threads = new LinkedList<Integer>();
		int count = 0;
		String query = "SELECT thread_id FROM threads WHERE box_id=? AND parent_id=0 ORDER BY post_date DESC";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, boxid);
			ResultSet rst = stat.executeQuery();

			if(getNumThreads(boxid) <= 5) {
				while(rst.next()) {
					threadid = rst.getInt("thread_id");
					threads.add(new Integer(threadid));
				}
			}
			else {
				while(rst.next() && (count < LAST_POSTS)) {
					count++;
					threadid = rst.getInt("thread_id");
					threads.add(new Integer(threadid));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			closeConn(conn, stat);
		}
		return threads;
	}

	public boolean getThread(int threadid) {
		String query = 
				"SELECT thread_id, paraent_id, box_id, member_id, subject, post_text, post_date FROM threads WHERE thread_id=?";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, threadid);
			ResultSet rst = stat.executeQuery();

			while(rst.next()) {
				this.threadid = rst.getInt("thread_id");
				parentid = rst.getInt("parent_id");
				boxid = rst.getInt("box_id");
				memberid = rst.getInt("member_id");
				subject = rst.getString("subject");
				posttext = rst.getString("post_text");
				postdate = rst.getLong("post_date");
			}

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}

	public boolean postThread() {
		parentid = 0;
		postdate = new java.util.Date().getTime();
		String query = "INSERT INTO threads (parent_id, box_id, member_id, subject, post_text, post_date) " +
						"VALUES (?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, parentid);
			stat.setInt(2, boxid);
			stat.setInt(3, memberid);
            stat.setString(4, subject);
            stat.setString(5, posttext);
            stat.setDate(6, new java.sql.Date(postdate));
			stat.executeUpdate();
			
			ResultSet tableKeys = stat.getGeneratedKeys();
			tableKeys.next();
			threadid = tableKeys.getInt(1);
			
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}

	public boolean replyThread() {
		postdate = new java.util.Date().getTime();
		String query = "INSERT INTO threads (parent_id, box_id, member_id, subject, post_text, post_date) " +
						"VALUES (?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, parentid);
			stat.setInt(2, boxid);
			stat.setInt(3, memberid);
            stat.setString(4, subject);
            stat.setString(5, posttext);
            stat.setDate(6, new java.sql.Date(postdate));
			stat.executeUpdate();
			ResultSet tableKeys = stat.getGeneratedKeys();
			tableKeys.next();
			threadid = tableKeys.getInt(1);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}

	public boolean setThread(int threadid) {
		postdate = new java.util.Date().getTime();
    	String query = "UPDATE threads SET subject=?, post_text=?, post_date=? WHERE thread_id=?";
	    Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setString(1, subject);
			stat.setString(2,  posttext);
			stat.setDate(3,  new java.sql.Date(postdate));
			stat.setInt(4,  threadid);
			stat.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}

	public boolean delThread(int threadid) {
		getThread(threadid);
		String query1 = "DELETE FROM threads WHERE thread_id=?";
		String query2 = "DELETE FROM threads WHERE thread_id=?  OR parent_id=?";

		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			if(parentid == 0){
			    stat = conn.prepareStatement(query2);
			    stat.setInt(1, threadid);
			    stat.setInt(2, threadid);
			}
			else{
				stat = conn.prepareStatement(query1);
			    stat.setInt(1, threadid);
			}
			
			stat.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}
}