/**
 * 
 */
package org.solinari.stock.db.driver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.solinari.analyzer.engine.Engine;

/**
 * @author solinari
 *这个类通过mysql获取stock的代码和名称信息，知道有哪些stock待分析
 */
public class MySqlDriver {
	private static Logger LOG = Logger.getLogger(MySqlDriver.class);
	private final static String DBNAME = "mystock";
	private final static String UPDATE_DATE_TABLE = "lastdate";
	private final static String STOCK_CODE_TABLE = "codeInfo";
	
	private String url = "jdbc:mysql://localhost/" + DBNAME;
	private String user = "root";
	private String pwd = "123456";
	
	public MySqlDriver(){
		
	}
	
	public MySqlDriver(String url, String user, String pwd){
		this.url = url;
		this.user = user;
		this.pwd = pwd;
	}
	
	public void createTable(String tableName){
		try{
//			Class.forName("com.mysql.jdbc.Driver").newInstance();		//JDK1.5以后自动加载驱动，不用再写了
			Connection conn = DriverManager.getConnection(url, user, pwd);

			//执行SQL语句
			Statement stmt = conn.createStatement();//创建语句对象，用以执行sql语言
			stmt.execute("create table " + DBNAME + "." + tableName + " (id int primary key, name char(20), code char(10));");
			conn.close();
			LOG.debug("Create MySql table success: " + DBNAME + "." + tableName);
		}
		catch (Exception ex){
			LOG.error("Create MySql table failed: " + DBNAME + "." + tableName);
//			System.out.println("Error : " + ex.toString());
		}
	}
	
	public void deleteTable(String tableName){
		
	}
	
	public void insertTable(String tableName, int id, String name, String code){
		try{
			Connection conn = DriverManager.getConnection(url, user, pwd);
		  
			//执行SQL语句
			Statement stmt = conn.createStatement();
			stmt.execute("insert into " + DBNAME + "." + tableName + " values(" +
					id + ",'" + name + "'" + ",'" + code + "'" + ");");
			conn.close();
			LOG.debug("Insert into MySql table success: " + DBNAME + "." + tableName + " content: " + name + code);
		}
		catch (Exception ex){
			LOG.error("Insert into MySql table failed: " + DBNAME + "." + tableName);
//			System.out.println("Error : " + ex.toString());
		}
	}
	
	public void insertDate(String code, Date date)throws Exception{
		try{
			Connection conn = DriverManager.getConnection(url, user, pwd);
		  
			//执行SQL语句
			Statement stmt = conn.createStatement();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
//			String sql = "insert into " + DBNAME + ".lastdate values('" + code + "','" + sdf.format(date) + "')";
			String sql = "insert into " + DBNAME + "." + UPDATE_DATE_TABLE + " values('" + code + "','" + sdf.format(date) + "')";
			stmt.execute(sql);
			conn.close();
			LOG.debug("Insert into MySql table success: " + DBNAME + "." + UPDATE_DATE_TABLE + " code: " + code + " date: " + date);
		}
		catch (Exception ex){
			LOG.error("Insert into MySql table failed: " + DBNAME + "." + UPDATE_DATE_TABLE);
//			System.out.println("Error : " + ex.toString());
		}
	}
	
	public void updateDate(String code, Date date)throws Exception{
		try{
			Connection conn = DriverManager.getConnection(url, user, pwd);
		  
			//执行SQL语句
			Statement stmt = conn.createStatement();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			String sql = "update " + DBNAME + ".lastdate set date = '" + sdf.format(date) + "' where code = '" + code + "'";
			String sql = "update " + DBNAME + "." + UPDATE_DATE_TABLE + " set date = '" + sdf.format(date) + "' where code = '" + code + "'";
			stmt.execute(sql);
			conn.close();
			LOG.debug("update MySql table success: " + DBNAME + "." + UPDATE_DATE_TABLE + " code: " + code + " date: " + date);
		}
		catch (Exception ex){
			LOG.error("update MySql table failed: " + DBNAME + "." + UPDATE_DATE_TABLE);
//			System.out.println("Error : " + ex.toString());
		}
	}
    
    public ArrayList<String> getStocksInfo(int startIndex, int endIndex){
    		ArrayList<String> ret = new ArrayList();
    		
		try {
			Connection conn = DriverManager.getConnection(url, user, pwd);

			Statement stmt = conn.createStatement();
//			String tableName = "codeInfo";
			String sql = "select * from " + DBNAME + "." + STOCK_CODE_TABLE + " where id >= " + startIndex + " and id < " + endIndex;
			ResultSet rs = stmt.executeQuery(sql);
			LOG.debug("Get info from MySql table success: " + DBNAME + "." + UPDATE_DATE_TABLE);
			while (rs.next()){
				int index = Integer.parseInt(rs.getString("id"));
				if (index >= startIndex && index < endIndex){
					String code = rs.getString("code");
					ret.add(code);
					LOG.debug("MySql Stock Code: " + code);
				}
			}
			conn.close();	
		} catch (Exception e) {
			LOG.error("Get info from MySql table failed: " + DBNAME + "." + UPDATE_DATE_TABLE);
//			e.printStackTrace();
		} 
		
		return ret;
    }
    
    /*
     * 获取stock最近一次跟新history信息的日期
     */
    public Date getStockHistoryLastDate(String code) {
    		Date ret = null;
    		
    	try {
			Connection conn = DriverManager.getConnection(url, user, pwd);
		  
			Statement stmt = conn.createStatement();
//			String tableName = "codeInfo";
//			String sql = "select * from " + DBNAME + "." + "lastdate where code = '" + code + "'";
			String sql = "select * from " + DBNAME + "." + UPDATE_DATE_TABLE + " where code = '" + code + "'";
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()){
				ret = rs.getDate("date");
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				ret = sdf.parse("2000-01-01");
			}
			conn.close();	
			LOG.debug("Last update date: " + ret.toString());
		} catch (Exception e) {
			LOG.error("Get Stock Last update date error code: " + code);
//			e.printStackTrace();
		} 
    	
    	return ret;
    }
}
