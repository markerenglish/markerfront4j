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

	public List getBoxes() {
		List boxes = new LinkedList();
		String query = "SELECT box_id FROM Boxes";

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			ResultSet rst = stat.executeQuery(query);

			while(rst.next()) {
				boxid = rst.getInt("box_id");
				boxes.add(new Integer(boxid));
			}

			con.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return boxes;
	}

	public List getBoxes(int memberid) {
		List boxes = new LinkedList();
		String query = "SELECT box_id FROM Boxes WHERE member_id=" + memberid;

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			ResultSet rst = stat.executeQuery(query);

			while(rst.next()) {
				boxid = rst.getInt("box_id");
				boxes.add(new Integer(boxid));
			}

			con.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return boxes;
	}

	public boolean getBox(int boxid) {
		String query = "SELECT * FROM Boxes WHERE box_id=" + boxid;

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			ResultSet rst = stat.executeQuery(query);

			while(rst.next()) {
				this.boxid = rst.getInt("box_id");
				memberid = rst.getInt("member_id");
				boxname = rst.getString("box_name");
				sortdesc = rst.getString("sort_desc");
			}

			con.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setBox() {
		boxid = (int) (1000 + Math.random()* 1000);
		String query = "INSERT INTO Boxes (box_id, member_id, [box_name], [sort_desc]) " +
						"VALUES (" + boxid + ", " +
						+ memberid + ", " +
						"'" + boxname + "', " +
						"'" + sortdesc + "')";
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

	public boolean setBox(int boxid) {
		String query = "UPDATE Boxes SET box_name='" + boxname + "', " +
						"sort_desc='" + sortdesc + "' " +
						"WHERE box_id=" + boxid;

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

	public boolean delBox(int boxid) {
		String query1 = "DELETE FROM Boxes WHERE box_id=" + boxid;
		String query2 = "DELETE FROM Threads WHERE box_id=" + boxid;

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
			Statement stat = con.createStatement();
			stat.execute(query1);
			stat.execute(query2);
			con.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isMod(int memberid) {
			String query = "SELECT box_id FROM Boxes WHERE member_id=" + memberid;

			try {
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
				Statement stat = con.createStatement();
				ResultSet rst = stat.executeQuery(query);
				if(rst.next() == true) {
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

	public boolean assignBox(int boxid, int memberid) {
			String query = "UPDATE Boxes SET member_id=" + memberid + " " +
							"WHERE box_id=" + boxid;

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

	public boolean unassignBox(int memberid) {
				String query = "UPDATE Boxes SET member_id=0 " +
								"WHERE member_id=" + memberid;

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

	public List getUnallocBoxes() {
			List boxes = new LinkedList();
			String query = "SELECT box_id FROM Boxes WHERE member_id=0";

			try {
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				Connection con = DriverManager.getConnection("jdbc:odbc:Forum");
				Statement stat = con.createStatement();
				ResultSet rst = stat.executeQuery(query);

				while(rst.next()) {
					boxid = rst.getInt("box_id");
					boxes.add(new Integer(boxid));
				}

				con.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return boxes;
	}
}