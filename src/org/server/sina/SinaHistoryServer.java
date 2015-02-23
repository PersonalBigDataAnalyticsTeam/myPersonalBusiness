/**
 * 
 */
package org.server.sina;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author solinari
 *
 */
public class SinaHistoryServer {
	
	private static final Log log = LogFactory.getLog(Log.class);
	
	final String sinaHistoryUrl = "http://biz.finance.sina.com.cn/stock/flash_hq/kline_data.php?&symbol="; 
	//final String sinaHistoryUrl = "http://biz.finance.sina.com.cn/stock/flash_hq/kline_data.php?&rand=random(10000)&symbol="; 
	/*+ "sz002241&" +	"end_date=20130806&begin_date=20130101&type=plain";*/
	
	private String setHttpUrl(String code, Date begindate){
		Date date=new Date();//取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		//calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
		date=calendar.getTime(); //这个时间就是日期往后推一天的结果 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(date);
		String url = sinaHistoryUrl + code + 
				"&end_date=" + dateString +
				"&begin_date=" + formatter.format(begindate) +
				"&type=plain";
	    
		return url;
	}
	
	public String getStockHistoryInfo(String code, Date begindate) {
		String result = null;
		try {
			String url = setHttpUrl(code, begindate);
			URL u = new URL(url);
			byte[] b = new byte[256];
			InputStream in = null;
			ByteArrayOutputStream bo = new ByteArrayOutputStream();

			try {
				in = u.openStream();
				int i;
				while ((i = in.read(b)) != -1) {
					bo.write(b, 0, i);
				}
				result = bo.toString();
				bo.reset();
				in.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				in.close();
			} finally {
				if (in != null) {
					in.close();
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return result;
	}
	
/*
	public void getStockHistoryInfo() {
		try{
			URL url=new URL(add);
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();//建立连接
			   int code=connection.getResponseCode();
			   if(code!=HttpURLConnection.HTTP_OK){
				   log.error("连接资源错误！"); 
			   }
			   else{
				   DataInputStream in=new DataInputStream(connection.getInputStream());//获得输入流
				   File file=new File(path);
				   RandomAccessFile rafile=new RandomAccessFile(file,"rw");//建立随机读写文件
				   byte[] buf=new byte[1024];
				   int num=in.read(buf);
				   while(num!=-1){
					   rafile.write(buf,0,buf.length);
					   rafile.skipBytes(num);
					   num=in.read(buf);
					   System.out.println(buf.toString());
				   }
				   log.error("Complete!"); 
			   }
		}
		catch(Exception e){
			log.error("Http error!!"); 
		}
	}*/
}
