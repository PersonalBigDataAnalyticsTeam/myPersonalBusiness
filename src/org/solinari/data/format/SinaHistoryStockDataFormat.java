/**
 * 
 */
package org.solinari.data.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @author solinari
 *
 */
public class SinaHistoryStockDataFormat {
	private static Logger LOG = Logger.getLogger(SinaHistoryStockDataFormat.class);
	private float today_startprice;
	private float price;
	private float highest_price;
	private float lowest_price;
	private long volume;
	private Date date;
	
	public float getToday_startprice() {
		return today_startprice;
	}
	public void setToday_startprice(float today_startprice) {
		this.today_startprice = today_startprice;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getHighest_price() {
		return highest_price;
	}
	public void setHighest_price(float highest_price) {
		this.highest_price = highest_price;
	}
	public float getLowest_price() {
		return lowest_price;
	}
	public void setLowest_price(float lowest_price) {
		this.lowest_price = lowest_price;
	}
	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	private void setData(String[] data) throws ParseException {
		this.setToday_startprice(Float.parseFloat(data[1]));
		this.setHighest_price(Float.parseFloat(data[2]));
		this.setPrice(Float.parseFloat(data[3]));
		this.setLowest_price(Float.parseFloat(data[4]));
		this.setVolume(Integer.parseInt(data[5]));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		date = (Date) sdf.parse(data[0]);
		this.setDate(date);
	}
	
	public String[] dataRegulation(String code, String[] data)throws ParseException {
		setData(data);
		String[] returnvalue = {
				Float.toString(this.getToday_startprice()),
				Float.toString(this.getPrice()),
				Float.toString(this.getHighest_price()),
				Float.toString(this.getLowest_price()),
				Long.toString(this.getVolume()),
				this.getDate().toString()
		};
		
		LOG.debug("Sina History Stock Data:"+
					"Date" + returnvalue[5] +
					"Today_startprice" + returnvalue[0] +
					"Price" + returnvalue[1] +
					"Highest_price" + returnvalue[2] +
					"Lowest_price" + returnvalue[3] +
					"Volume" + returnvalue[4]);
		
		return returnvalue;		
	}
}
