package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL extends Database {
	
	private String server = "";
	private String user = "";
	private String pass = "";
	private Connection conn;
	private Statement stmt;
	
	public MySQL(String server, String user, String pass) {
		this.server = server;
		this.user = user;
		this.pass = pass;
		connect();
	}
	
	private boolean connect() {
		if(conn == null) {
			try {
				conn = DriverManager.getConnection(server, user, pass);
				stmt = conn.createStatement();
			} catch (SQLException e) {
				conn = null;
				stmt = null;
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public boolean setColm(String table, String key, String val) {
		if(!connect()) return false;
		try {
			stmt.execute("UPDATE " + table + " SET " + val + " WHERE " + key + ";");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public ResultSet select(String table, String key, String sel) {
		if(!connect()) return null;
		try {
			return stmt.executeQuery("SELECT " + sel + " FROM " + table + " WHERE " + key + ";");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Statement getStmt() {
		return stmt;
	}
}
