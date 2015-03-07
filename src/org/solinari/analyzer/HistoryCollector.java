/**
 * 
 */
package org.solinari.analyzer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.solinari.data.format.SinaHistoryStockDataFormat;
import org.solinari.data.format.SinaHistoryStockDataFormatFactory;
import org.solinari.server.driver.sina.SinaHistoryServer;
import org.solinari.stock.db.driver.MySqlDriver;
import org.solinari.stock.db.driver.HBaseDriver;

/**
 * @author solinari
 *
 */
public class HistoryCollector  extends Thread {
	
	private final int index;
    private volatile Thread thread;
    
  	private static final String[] stockHisFam = {
  		"todayStartPrice",
  		"highestPrice",
  		"price",
  		"lowestPrice",
  		"volume",
  		"date"
  	};
  	
  	private Vector stocks = new Vector();
  	
	public HistoryCollector(int index) {
		this.index = index;
		thread = this;
	}
	
	public void setStockCode(String code){
		this.stocks.add(code);
	}
	
  	private void write_history_base(String table, String[] datas, String row) throws IOException{
		if (!HBaseDriver.isTableExist(table)){
			HBaseDriver.createTable(table, stockHisFam);
		}
		
		if (!HBaseDriver.isRowExisting(table, row)){
			HBaseDriver.writeRow(table, row, stockHisFam, datas);
		}

	}
  	
  	private Date get_last_date(String code) throws IOException, ParseException{
  		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
  		MySqlDriver sid = new MySqlDriver();
  		
  		Date date = sid.getStockHistoryLastDate(code);
		
		return date;
	}
  	
  	private void update_last_date(String code) throws Exception{
  		String tablename = code  + "_history"; 
  		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
  		Date date = new Date();
  		MySqlDriver sid = new MySqlDriver();  
  		
  		sid.insertDate(code, date);
  		
  		//sid.updateDate(code, date);
	}
  	
  	private void collectStockData(String code) throws IOException, ParseException{
  		SinaHistoryServer shs = new SinaHistoryServer();
  		Date lastDate = null;
  		try {
  			lastDate = get_last_date(code);	//最近一次收集信息的时间
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String result = shs.getStockHistoryInfo(code, lastDate);
		if (result == null){
			return;
		}
		if (0 == result.length()){
			return;
		}
		String[] stockHistoryDatas = result.split("\n");

		for (String stockData : stockHistoryDatas){
			String[] datas = stockData.split(",");
			SinaHistoryStockDataFormat shsdf = SinaHistoryStockDataFormatFactory.getSinaHistoryStockDataFormater();

			try {
				write_history_base(code+"_history", shsdf.dataRegulation(code, datas), datas[0]);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//StoreMetaData.scanner(code+"_history");
  		
		try {
			update_last_date(code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	}
  	
	/*
	 * String url = "http://hq.sinajs.cn/list=sh600151,sz000830,s_sh000001,s_sz399001,s_sz399106";
	 * url = "http://hq.sinajs.cn/list=sh601006";
	*/
  	@Override
  	public void run() {
  		for (Iterator it = this.stocks.iterator(); it.hasNext(); ) {
  			Object obj=it.next();
  	  		try {
				try {
					collectStockData(obj.toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  		}

  	}
}
