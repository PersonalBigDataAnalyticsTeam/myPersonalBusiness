/**
 * 
 */
package org.solinari.data.collector.driver;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.solinari.data.formater.SinaHistoryStockDataFormat;
import org.solinari.data.formater.SinaHistoryStockDataFormatFactory;
import org.solinari.server.driver.sina.SinaHistoryServer;
import org.solinari.stock.db.driver.HBaseDriver;
import org.solinari.stock.db.driver.MySqlDriver;

/**
 * @author solinari
 *
 */

public class HistoryCollectorThread implements Runnable{
	private static Logger LOG = Logger.getLogger(HistoryCollectorThread.class);
	private final static String ENDL = "\n";
	private final static String SPLIT_SYMBOL = ",";
	private final static String HISTORY_STRING_TAIL = "_history";
	private final static int ROWKEY_INDEX = 0;
	private final static String[] stockHisFam = {	"todayStartPrice",
											  		"highestPrice",
											  		"price",
											  		"lowestPrice",
											  		"volume",
											  		"date"};
	private CountDownLatch signal;  	
  	private Vector stocks = new Vector();
  	
  	public HistoryCollectorThread(CountDownLatch signal){
  		this.signal = signal;
  	}
	
	public void addStockCode(String code){
		this.stocks.add(code);
	}
	
	/*
	 * 把数据写入HBase
	 * params： @table 指定stock的信息存储表
	 * 			@datas 需要存放的数据
	 * 			@row HTable的行键<对应数据的日期>
	 */
  	private void write_history_base(String table, String[] datas, String row) throws IOException{
		if (!HBaseDriver.isTableExist(table)){
			HBaseDriver.createTable(table, stockHisFam);
		}
		
		if (!HBaseDriver.isRowExisting(table, row)){
			HBaseDriver.writeRow(table, row, stockHisFam, datas);
		}
	}
  	
  	/*
  	 * 从MySql取得最近一次收集history数据的日期
  	 */
  	private Date get_last_date(String code) throws IOException, ParseException{
  		MySqlDriver sid = new MySqlDriver();
  		Date date = sid.getStockHistoryLastDate(code);
		return date;
	}
  	
  	/*
  	 * 修改MySql的最近一次收集history数据的日期
  	 */
  	private void update_last_date(String code) throws Exception{
  		Date date = new Date();
  		MySqlDriver sid = new MySqlDriver();  
  		sid.insertDate(code, date);
  		//为什么不用update方法？
  		//sid.updateDate(code, date);
	}
  	
  	private String getHistoryTableName(String code){
  		return code + HISTORY_STRING_TAIL;
  	}
  	
  	/*
  	 * 采集对应stock code的历史信息，并写入HBase中
  	 * params: @code 指定stock的code
  	 */
  	private void collectStockData(String code) throws IOException, ParseException{
  		SinaHistoryServer sinaServer = new SinaHistoryServer();
  		Date lastDate = null;

  		try {
  			lastDate = get_last_date(code);	//最近一次收集信息的时间
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LOG.error("history data collector get last update error StackTrace: " + e.getStackTrace());
		}

		String result = sinaServer.getStockHistoryInfo(code, lastDate);
		if (null == result){
			return;		//没有采集到数据
		}
		if (0 == result.length()){
			return;		//没有采集到数据
		}
		
		/*
		 * 格式化数据，使之能够写入HBase
		 */
		String[] stockHistoryDatas = result.split(ENDL);	//这里每一行代表一天的数据
		for (String stockData : stockHistoryDatas){
			String[] datas = stockData.split(SPLIT_SYMBOL);
			SinaHistoryStockDataFormat shsdf = SinaHistoryStockDataFormatFactory.getSinaHistoryStockDataFormater();
			try {
				write_history_base(getHistoryTableName(code), shsdf.dataRegulation(code, datas), datas[ROWKEY_INDEX]);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				LOG.error("history data collector write HBase error StackTrace: " + e.getStackTrace());
			}
		}
  		
		try {
			update_last_date(code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("history data collector update last date error StackTrace: " + e.getStackTrace());
		}
  	}
  	
	/*
	 * String url = "http://hq.sinajs.cn/list=sh600151,sz000830,s_sh000001,s_sz399001,s_sz399106";
	 * url = "http://hq.sinajs.cn/list=sh601006";
	 * 使用HistoryCollector之前注意使用addStockCode方法把stock都灌进来
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
					LOG.error("history data collector get stock data error StackTrace: " + e.getStackTrace());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOG.error("history data collector get stock data error StackTrace: " + e.getStackTrace());
			}
  		}
  		
  		signal.countDown();
  	}
}
