package com.marker.front;

import java.sql.*;
import java.util.*;

public class Boxes {
	private int boxid, memberid;
	private String boxname, sortdesc;


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

	public String getBoxname() {
		return boxname;
	}

	public void setBoxname(String boxname) {
		this.boxname = boxname;
	}

	public String getSortdesc() {
		return sortdesc;
	}

	public void setSortdesc(String sortdesc) {
		this.sortdesc = sortdesc;
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

	public List<Integer> getBoxes() {
		List<Integer> boxes = new LinkedList<Integer>();
		String query = "SELECT box_id FROM Boxes";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			ResultSet rst = stat.executeQuery(query);

			while(rst.next()) {
				boxid = rst.getInt("box_id");
				boxes.add(new Integer(boxid));
			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			closeConn(conn, stat);
		}
		return boxes;
	}

	public List<Integer> getBoxes(int memberid) {
		List<Integer> boxes = new LinkedList<Integer>();
		String query = "SELECT box_id FROM Boxes WHERE member_id=?";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, memberid);
			ResultSet rst = stat.executeQuery(query);

			while(rst.next()) {
				boxid = rst.getInt("box_id");
				boxes.add(new Integer(boxid));
			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			closeConn(conn, stat);
		}
		return boxes;
	}

	public boolean getBox(int boxid) {
		String query = "SELECT box_id, member_id, box_name, sort_desc FROM Boxes WHERE box_id=?";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, boxid);
			ResultSet rst = stat.executeQuery(query);

			while(rst.next()) {
				this.boxid = rst.getInt("box_id");
				memberid = rst.getInt("member_id");
				boxname = rst.getString("box_name");
				sortdesc = rst.getString("sort_desc");
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}

	public boolean setBox() {
		String query = 
				"INSERT INTO Boxes (box_id, member_id, box_name, sort_desc) VALUES (default, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setInt(1, memberid);
			stat.setString(2, boxname);
			stat.setString(3, sortdesc);
			stat.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}

	public boolean setBox(int boxid) {
		String query = "UPDATE Boxes SET box_name=?, sort_desc=? WHERE box_id=?";

		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = createConn();
			stat = conn.prepareStatement(query);
			stat.setString(1, boxname);
			stat.setString(2, sortdesc);
			stat.setInt(3, boxid);
			stat.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat);
		}
	}

	public boolean delBox(int boxid) {
		String query1 = "DELETE FROM Boxes WHERE box_id=?";
		String query2 = "DELETE FROM Threads WHERE box_id=?";
		Connection conn = null;
		PreparedStatement stat1 = null, stat2 = null;

		try {
			conn = createConn();
			stat1 = conn.prepareStatement(query1);
			stat2 = conn.prepareStatement(query2);
			stat1.executeUpdate();
			stat2.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeConn(conn, stat1, stat2);
		}
	}

	public boolean isMod(int memberid) {
			String query = "SELECT box_id FROM Boxes WHERE member_id=?";
			Connection conn = null;
			PreparedStatement stat = null;

			try {
				conn = createConn();
				stat = conn.prepareStatement(query);
				stat.setInt(1, memberid);
				ResultSet rst = stat.executeQuery();
				if(rst.next() == true) {
					return true;
				}
				else {
					return false;
				}
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}finally{
				closeConn(conn, stat);
			}
	}

	public boolean assignBox(int boxid, int memberid) {
			String query = "UPDATE Boxes SET member_id=? WHERE box_id=?";
			Connection conn = null;
			PreparedStatement stat = null;

			try {
				conn = createConn();
				stat = conn.prepareStatement(query);
				stat.setInt(1, memberid);
				stat.setInt(2, boxid);
				stat.executeUpdate();
				return true;
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}finally{
				closeConn(conn, stat);
			}
	}

	public boolean unassignBox(int memberid) {
				String query = "UPDATE Boxes SET member_id=0 WHERE member_id=?";
				Connection conn = null;
				PreparedStatement stat = null;

				try {
					conn = createConn();
					stat = conn.prepareStatement(query);
					stat.setInt(1, memberid);
					stat.executeUpdate();
					return true;
				}catch(Exception e) {
					e.printStackTrace();
					return false;
				}finally{
					closeConn(conn, stat);
				}
	}

	public List<Integer> getUnallocBoxes() {
			List<Integer> boxes = new LinkedList<Integer>();
			String query = "SELECT box_id FROM Boxes WHERE member_id=0";
			Connection conn = null;
			PreparedStatement stat = null;

			try {
				conn = createConn();
				stat = conn.prepareStatement(query);
				ResultSet rst = stat.executeQuery();

				while(rst.next()) {
					boxid = rst.getInt("box_id");
					boxes.add(new Integer(boxid));
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally{
				closeConn(conn, stat);
			}
			return boxes;
	}
}