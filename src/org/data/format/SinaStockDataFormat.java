/**
 * 
 */
package org.data.format;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author solinari
 *
 */
public class SinaStockDataFormat {

	private String stockname;
	private float today_startprice;
	private float yestarday_endprice;
	private float price;
	private float highest_price;
	private float lowest_price;
	private float buy_price;
	private float sell_price;
	private long volume;
	private long money;
	private float buy1_price;
	private float buy2_price;
	private float buy3_price;
	private float buy4_price;
	private float buy5_price;
	private int buy1_hand;
	private int buy2_hand;
	private int buy3_hand;
	private int buy4_hand;
	private int buy5_hand;
	private float sell1_price;
	private float sell2_price;
	private float sell3_price;
	private float sell4_price;
	private float sell5_price;
	private int sell1_hand;
	private int sell2_hand;
	private int sell3_hand;
	private int sell4_hand;
	private int sell5_hand;
	private Date date;
	
	public String getStockname() {
		return stockname;
	}
	public void setStockname(String stockname) {
		this.stockname = stockname;
	}
	public float getToday_startprice() {
		return today_startprice;
	}
	public void setToday_startprice(float today_startprice) {
		this.today_startprice = today_startprice;
	}
	public float getYestarday_endprice() {
		return yestarday_endprice;
	}
	public void setYestarday_endprice(float yestarday_endprice) {
		this.yestarday_endprice = yestarday_endprice;
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
	public float getBuy1_price() {
		return buy1_price;
	}
	public void setBuy1_price(float buy1_price) {
		this.buy1_price = buy1_price;
	}
	public float getBuy2_price() {
		return buy2_price;
	}
	public void setBuy2_price(float buy2_price) {
		this.buy2_price = buy2_price;
	}
	public float getBuy3_price() {
		return buy3_price;
	}
	public void setBuy3_price(float buy3_price) {
		this.buy3_price = buy3_price;
	}
	public float getBuy4_price() {
		return buy4_price;
	}
	public void setBuy4_price(float buy4_price) {
		this.buy4_price = buy4_price;
	}
	public float getBuy5_price() {
		return buy5_price;
	}
	public void setBuy5_price(float buy5_price) {
		this.buy5_price = buy5_price;
	}
	public int getBuy1_hand() {
		return buy1_hand;
	}
	public void setBuy1_hand(int buy1_hand) {
		this.buy1_hand = buy1_hand;
	}
	public int getBuy2_hand() {
		return buy2_hand;
	}
	public void setBuy2_hand(int buy2_hand) {
		this.buy2_hand = buy2_hand;
	}
	public int getBuy3_hand() {
		return buy3_hand;
	}
	public void setBuy3_hand(int buy3_hand) {
		this.buy3_hand = buy3_hand;
	}
	public int getBuy4_hand() {
		return buy4_hand;
	}
	public void setBuy4_hand(int buy4_hand) {
		this.buy4_hand = buy4_hand;
	}
	public int getBuy5_hand() {
		return buy5_hand;
	}
	public void setBuy5_hand(int buy5_hand) {
		this.buy5_hand = buy5_hand;
	}
	public float getSell1_price() {
		return sell1_price;
	}
	public void setSell1_price(float sell1_price) {
		this.sell1_price = sell1_price;
	}
	public float getSell2_price() {
		return sell2_price;
	}
	public void setSell2_price(float sell2_price) {
		this.sell2_price = sell2_price;
	}
	public float getSell3_price() {
		return sell3_price;
	}
	public void setSell3_price(float sell3_price) {
		this.sell3_price = sell3_price;
	}
	public float getSell4_price() {
		return sell4_price;
	}
	public void setSell4_price(float sell4_price) {
		this.sell4_price = sell4_price;
	}
	public float getSell5_price() {
		return sell5_price;
	}
	public void setSell5_price(float sell5_price) {
		this.sell5_price = sell5_price;
	}
	public int getSell1_hand() {
		return sell1_hand;
	}
	public void setSell1_hand(int sell1_hand) {
		this.sell1_hand = sell1_hand;
	}
	public int getSell2_hand() {
		return sell2_hand;
	}
	public void setSell2_hand(int sell2_hand) {
		this.sell2_hand = sell2_hand;
	}
	public int getSell3_hand() {
		return sell3_hand;
	}
	public void setSell3_hand(int sell3_hand) {
		this.sell3_hand = sell3_hand;
	}
	public int getSell4_hand() {
		return sell4_hand;
	}
	public void setSell4_hand(int sell4_hand) {
		this.sell4_hand = sell4_hand;
	}
	public int getSell5_hand() {
		return sell5_hand;
	}
	public void setSell5_hand(int sell5_hand) {
		this.sell5_hand = sell5_hand;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public float getBuy_price() {
		return buy_price;
	}
	public void setBuy_price(float buy_price) {
		this.buy_price = buy_price;
	}
	public float getSell_price() {
		return sell_price;
	}
	public void setSell_price(float sell_price) {
		this.sell_price = sell_price;
	}
	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
	
	public void setData(String[] data)throws ParseException{
		this.setStockname(data[0]);
		this.setToday_startprice(Float.parseFloat(data[1]));
		this.setYestarday_endprice(Float.parseFloat(data[2]));
		this.setPrice(Float.parseFloat(data[3]));
		this.setHighest_price(Float.parseFloat(data[4]));
		this.setLowest_price(Float.parseFloat(data[5]));
		this.setBuy_price(Float.parseFloat(data[6]));
		this.setSell_price(Float.parseFloat(data[7]));
		this.setVolume(Long.parseLong(data[8]));
		this.setMoney(Long.parseLong(data[9]));
		this.setBuy1_hand(Integer.parseInt(data[10]));
		this.setBuy1_price(Float.parseFloat(data[11]));
		this.setBuy2_hand(Integer.parseInt(data[12]));
		this.setBuy2_price(Float.parseFloat(data[13]));
		this.setBuy3_hand(Integer.parseInt(data[14]));
		this.setBuy3_price(Float.parseFloat(data[15]));
		this.setBuy4_hand(Integer.parseInt(data[16]));
		this.setBuy4_price(Float.parseFloat(data[17]));
		this.setBuy5_hand(Integer.parseInt(data[18]));
		this.setBuy5_price(Float.parseFloat(data[19]));
		this.setSell1_hand(Integer.parseInt(data[20]));
		this.setSell1_price(Float.parseFloat(data[21]));
		this.setSell2_hand(Integer.parseInt(data[22]));
		this.setSell2_price(Float.parseFloat(data[23]));
		this.setSell3_hand(Integer.parseInt(data[24]));
		this.setSell3_price(Float.parseFloat(data[25]));
		this.setSell4_hand(Integer.parseInt(data[26]));
		this.setSell4_price(Float.parseFloat(data[27]));
		this.setSell5_hand(Integer.parseInt(data[28]));
		this.setSell5_price(Float.parseFloat(data[29]));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date = (Date) sdf.parse(data[30]);
		SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm:ss");
		Date time = (Date) sdf2.parse(data[31]);
		date.setHours(time.getHours());
		date.setMinutes(time.getMinutes());
		date.setSeconds(time.getSeconds());
		this.setDate(date);
	}
	
	
	public String[] dataRegulation(String[] data) throws ParseException{
		setData(data);
		String[] returnvalue = {
				this.getStockname(),
				Float.toString(this.getToday_startprice()),
				Float.toString(this.getYestarday_endprice()),
				Float.toString(this.getPrice()),
				Float.toString(this.getHighest_price()),
				Float.toString(this.getLowest_price()),
				Float.toString(this.getBuy_price()),
				Float.toString(this.getSell_price()),
				Long.toString(this.getVolume()),
				Long.toString(this.getMoney()),
				Long.toString(this.getBuy1_hand()),
				Float.toString(this.getBuy1_price()),
				Long.toString(this.getBuy2_hand()),
				Float.toString(this.getBuy2_price()),
				Long.toString(this.getBuy3_hand()),
				Float.toString(this.getBuy3_price()),
				Long.toString(this.getBuy4_hand()),
				Float.toString(this.getBuy4_price()),
				Long.toString(this.getBuy5_hand()),
				Float.toString(this.getBuy5_price()),
				Long.toString(this.getSell1_hand()),
				Float.toString(this.getSell1_price()),
				Long.toString(this.getSell2_hand()),
				Float.toString(this.getSell2_price()),
				Long.toString(this.getSell3_hand()),
				Float.toString(this.getSell3_price()),
				Long.toString(this.getSell4_hand()),
				Float.toString(this.getSell4_price()),
				Long.toString(this.getSell5_hand()),
				Float.toString(this.getSell5_price()),
				this.getDate().toGMTString()
		};
		return returnvalue;		
	}
}
