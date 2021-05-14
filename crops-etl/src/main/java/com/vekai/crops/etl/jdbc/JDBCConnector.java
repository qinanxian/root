package com.vekai.crops.etl.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCConnector {
	private String clzName;
	private String url;
	private String username;
	private String password;

	public JDBCConnector(String clzName, String url, String username, String password) {
		this.clzName = clzName;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public Connection getConnection(){
		Connection con=null;
		try {
			Class.forName(clzName);
			con=DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public void close(Connection con){//关闭连接
		if(con!=null)
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public void close(PreparedStatement ps){//关闭语句
		if(ps!=null)
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public void close(ResultSet rs){//关闭结果集
		if(rs!=null)
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
}
