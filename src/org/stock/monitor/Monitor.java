/**
 * 
 */
package org.stock.monitor;

import java.util.*;
import org.stock.base.StockInfoData;
import org.stockAnalyzer.engine.*;

/**
 * @author solinari
 *
 */
public class Monitor {

	/**
	 * @param args
	 */
	private static int numOfRealtimeMonitorRequired = 1;	//记得修改
	private static int numOfParallelHistoryMonitor = 10;
	
	public static void runCollector(){
		for (int i = 0; i < numOfRealtimeMonitorRequired; i++) {
			RealtimeCollector collector = new RealtimeCollector(i);
			collector.start();
		}
	}
	
	public static void runHistoryCollector(){
		StockInfoData sid = new StockInfoData();
		ArrayList <String> array = sid.getStocksInfo(0, 100);
		int num = array.size();
		int each = num / numOfParallelHistoryMonitor;
		if (array != null){
			for (int index = 1; index <= numOfParallelHistoryMonitor; index++){
				HistoryCollector collector = new HistoryCollector(index);
				for (int i = index; i < num; i+= numOfParallelHistoryMonitor){
					if (i < array.size()){
						collector.setStockCode(array.get(i));
					}
				}
				collector.start();
			}
			/*
			for (String code : array){
				HistoryCollector collectors = new HistoryCollector(index++);
				collector.setStockCode(code);
				collector.start();
			}*/
		}
		/*
		for (int i = 0; i < numOfParallelHistoryMonitor; i++) {
			HistoryCollector collector = new HistoryCollector(i);
			for (String code : codes){
				collector.setStockCode(code);
			}
			collector.start();
		}*/
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//runCollector();
		runHistoryCollector();
		/*
		StockInfoData sid = new StockInfoData();
		ArrayList <String> array = sid.getStocksInfo(0, 10);
		if (array != null){
			runHistoryCollector();

			for (String s : array){
				System.out.println(s);
				
			}
		}*/


	}
	

}
