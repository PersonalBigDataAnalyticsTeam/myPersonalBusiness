/**
 * 
 */
package org.stock.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.extractor.WordExtractor;

/**
 * @author solinari
 *
 */
public class StockInfoData {
	
	private static final Log log = LogFactory.getLog(Log.class);
	
	private final String dbName = "mystock";
	private static String url;
	private static String user;
	private static String pwd;
	
	public StockInfoData(){
			url="jdbc:mysql://localhost/" + dbName;
			user="root";
			pwd="123456";
	}
	
	public void createTable(String tableName){
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(url, user, pwd);
		  
			//执行SQL语句
			Statement stmt = conn.createStatement();//创建语句对象，用以执行sql语言
			stmt.execute("create table " + dbName + "." + tableName + " (id int primary key, name char(20), code char(10));");
			conn.close();
		}
		catch (Exception ex){
			System.out.println("Error : " + ex.toString());
		}
	}
	
	public void deleteTable(String tableName){
		
	}
	
	public void insertTable(String tableName, int id, String name, String code){
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(url, user, pwd);
		  
			//执行SQL语句
			Statement stmt = conn.createStatement();//创建语句对象，用以执行sql语言
			stmt.execute("insert into " + dbName + "." + tableName + " values(" +
					id + ",'" + name + "'" + ",'" + code + "'" + ");");
			conn.close();
		}
		catch (Exception ex){
			System.out.println("Error : " + ex.toString());
		}
	}
	
	public void insertDate(String code, Date date)throws Exception{
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(url, user, pwd);
		  
			//执行SQL语句
			Statement stmt = conn.createStatement();//创建语句对象，用以执行sql语言
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			String sql = "insert into " + dbName + ".lastdate values('" + code + "','" + sdf.format(date) + "')";
			stmt.execute(sql);
			
			conn.close();
		}
		catch (Exception ex){
			System.out.println("Error : " + ex.toString());
		}
	}
	
	public void updateDate(String code, Date date)throws Exception{
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(url, user, pwd);
		  
			//执行SQL语句
			Statement stmt = conn.createStatement();//创建语句对象，用以执行sql语言
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String sql = "update " + dbName + ".lastdate set date = '" + sdf.format(date) + "' where code = '" + code + "'";
			System.out.println(sql);
			stmt.execute(sql);
			
			conn.close();
		}
		catch (Exception ex){
			System.out.println("Error : " + ex.toString());
		}
	}

    public void readWord2003() {
		try {
			FileInputStream fis = new FileInputStream("/home/solinari/文档/stock/股票代码查询一览表.doc");
			WordExtractor wordExtractor = new WordExtractor(fis);
			String [] ss = wordExtractor.getText().split("\n");
			
			for (String s : ss){
				if ('' == s.charAt(0)){
					String [] st = s.split("\t");
					//System.out.println(st[1]);
					String [] xt = st[1].split(" ");
					int pos = xt[0].indexOf('(');
					System.out.println(xt[0].substring(0, pos));
					
					int pos1 = xt[1].lastIndexOf('.');
					int pos2 = xt[1].lastIndexOf('/');
					System.out.println(xt[1].substring(pos2+1, pos1));
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();
		}  
    }
    
    public ArrayList<String> getStocksInfo(int startIndex, int endIndex) {
    		ArrayList<String> ret = new ArrayList();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(url, user, pwd);
		  
			Statement stmt = conn.createStatement();
			String tableName = "codeInfo";
			String sql = "select * from " + dbName + "." + tableName + " where id >= " + startIndex + " and id < " + endIndex;
			//System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()){
				int index = Integer.parseInt(rs.getString("id"));
				if (index >= startIndex && index < endIndex){
					String code = rs.getString("code");
					ret.add(code);
				}
			}
			
			conn.close();	
		} catch (Exception e) {  
			e.printStackTrace();
		} 
		
		return ret;
    }
    
    public Date getStockHistoryLastDate(String code) {
    		Date ret = null;
    	try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(url, user, pwd);
		  
			Statement stmt = conn.createStatement();
			String tableName = "codeInfo";
			String sql = "select * from " + dbName + "." + "lastdate where code = '" + code + "'";
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()){
				ret = rs.getDate("date");
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				ret = sdf.parse("2000-01-01");

			}
			
			conn.close();	
		} catch (Exception e) {  
			e.printStackTrace();
		} 
    	
    	return ret;
    }
}
