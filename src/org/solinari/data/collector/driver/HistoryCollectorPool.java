/**
 * 
 */
package org.solinari.data.collector.driver;

import java.util.ArrayList;

import org.solinari.utils.ThreadPool;
import org.solinari.data.collector.driver.*;

/**
 * @author solinari
 *
 */
public class HistoryCollectorPool extends ThreadPool{
	
	private int historyCollectorNum;
	private ArrayList <String> stockArray;
	private int index;
	
	public HistoryCollectorPool(int historyCollectorNum, ArrayList <String> stockArray){
		this.historyCollectorNum = historyCollectorNum;
		this.stockArray = stockArray;
		index = 1;
	}
	
	public void preRun(Runnable thread){
		HistoryCollectorThread historyThread = (HistoryCollectorThread) thread;
		if (index < historyCollectorNum){
			historyThread.addStockCode(stockArray.get(index));
			index++;
		}
	}
}
