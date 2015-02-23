/**
 * 
 */
package org.stockAnalyzer.engine;

import java.io.IOException;
import java.text.ParseException;

import org.data.format.*;
import org.server.sina.*;
import org.stock.base.*;

/**
 * @author solinari
 *
 */
public class RealtimeCollector extends Thread {

    private final int index;
    private volatile Thread thread;
    
  	private static final String[] stockFam = {	"name", "todayStartPrice", "yestardayEndPrice",
    									"price", "highestPrice", "lowestPrice",
    									"buyPrice", "sellPrice", "volume", "money",
    									"buy1Hand", "buy1Price",
    									"buy2Hand", "buy2Price",
    									"buy3Hand", "buy3Price",
    									"buy4Hand", "buy4Price",
    									"buy5Hand", "buy5Price",
    									"sell1Hand", "sell1Price",
    									"sell2Hand", "sell2Price",
    									"sell3Hand", "sell3Price",
    									"sell4Hand", "sell4Price",
    									"sell5Hand", "sell5Price",
    									"date"
    		};
	
	public RealtimeCollector(int index) {
		this.index = index;
		thread = this;
	}

	private void write_base(String code, String[] datas, String row) throws IOException{
		if (!StoreMetaData.isTableExist(code)){
			StoreMetaData.createTable(code, stockFam);
		}
		
		if (!StoreMetaData.isRowExisting(code, row)){
			StoreMetaData.writeRow(code, row, stockFam, datas);
		}

	}
	
	/*
	 * String url = "http://hq.sinajs.cn/list=sh600151,sz000830,s_sh000001,s_sz399001,s_sz399106";
	 * url = "http://hq.sinajs.cn/list=sh601006";
	*/
	@Override
    public void run() {

		final String stock = "sh601006";
		SinaStockServer server = new SinaStockServer();
		String result = server.getStockInfo(stock);
		String[] stockDatas = result.split(",");
		SinaStockDataFormat sdf = new SinaStockDataFormat();
		try {
			stockDatas = sdf.dataRegulation(stockDatas);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			write_base(stock, stockDatas, sdf.getDate().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}