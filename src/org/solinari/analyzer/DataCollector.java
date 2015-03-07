/**
 * 
 */
package org.solinari.analyzer;

import java.util.*;
import org.apache.log4j.Logger;
import org.solinari.analyzer.engine.*;
import org.solinari.stock.db.driver.MySqlDriver;
/**
 * @author solinari
 *
 */
//后续改写成线程池的实现
public class DataCollector {

	/**
	 * @param args
	 * int realTimeCollectorNum 指定实时数据采集的线程数
	 * int historyCollectorNum 指定历史数据采集的线程数
	 */
	private static Logger LOG = Logger.getLogger(DataCollector.class);
	
	private int realTimeCollectorNum = 10;
	private int historyCollectorNum = 10;

	public void setRealTimeMonitorNum(int realTimeMonitorNum) {
		this.realTimeCollectorNum = realTimeMonitorNum;
	}
	
	public void setHistoryMonitorNum(int historyMonitorNum) {
		this.historyCollectorNum = historyMonitorNum;
	}
	
	//代调试和改动
	public void runCollector(){
//		for (int i = 0; i < realTimeCollectorNum; i++) {
//			RealtimeCollector collector = new RealtimeCollector(i);
//			collector.start();
//		}
	}
	
	public void runHistoryCollector(){
		//获取待采集历史数据的stock列表
		MySqlDriver sid = new MySqlDriver();
		ArrayList <String> stockArray = sid.getStocksInfo(0, 100);
		int stockNum = stockArray.size();
		int each = stockNum / historyCollectorNum;
		
		LOG.debug("start history data collecting with thread " + historyCollectorNum);
		
		if (stockArray != null){
			for (int index = 1; index <= historyCollectorNum; index++){
				HistoryCollector collector = new HistoryCollector(index);
				for (int i = index; i < stockNum; i+= historyCollectorNum){
					if (i < stockArray.size()){
						collector.setStockCode(stockArray.get(i));
					}
				}
				collector.start();
			}
		}
	}

}
