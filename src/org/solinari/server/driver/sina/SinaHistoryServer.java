/**
 * 
 */
package org.solinari.server.driver.sina;

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
 * 从sina获取原始历史数据网页
 */
public class SinaHistoryServer {
	
	private static final Log LOG = LogFactory.getLog(SinaHistoryServer.class);
	
	private final static String SINA_HISTORY_URL = "http://biz.finance.sina.com.cn/stock/flash_hq/kline_data.php?&symbol="; 
//	final String sinaHistoryUrl = "http://biz.finance.sina.com.cn/stock/flash_hq/kline_data.php?&rand=random(10000)&symbol="; 
//	/*+ "sz002241&" +	"end_date=20130806&begin_date=20130101&type=plain";*/
	
	/*
	 * 根据stock的代码和启示抓取日期，生成HTTP访问的URL，抓取的时间是从begindate到当前为止
	 * code stock代码
	 * begindate时间类型，抓取的起始日期，截止日期为now
	 */
	private String setHttpUrl(String code, Date begindate){
		Date date=new Date();	//取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		//calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
		date=calendar.getTime(); //这个时间就是日期往后推一天的结果 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(date);
		String url = 	SINA_HISTORY_URL + code + "&end_date=" + dateString +
					"&begin_date=" + formatter.format(begindate) + "&type=plain";
	    
		return url;
	}
	
	/*
	 * 抓取对应stock的历史数据信息
	 * code stock的代码
	 * begindate 抓取的起始时间，抓取时间阶段为begindate到now
	 */
	public String getStockHistoryInfo(String code, Date begindate) {
		String result = null;
		
		try {
			URL url = new URL(setHttpUrl(code, begindate));
			byte[] buffer = new byte[256];
			InputStream inputstream = null;
			ByteArrayOutputStream outputstream = new ByteArrayOutputStream();

			try {
				inputstream = url.openStream();
				int i;
				while ((i = inputstream.read(buffer)) != -1) {
					outputstream.write(buffer, 0, i);
				}
				result = outputstream.toString();
				outputstream.reset();
				inputstream.close();
				LOG.debug("catch sina history stock info success code: " + code);
			} 
			catch (Exception e) {
				LOG.error("catch sina history stock info failed error: " + e.getMessage());
				inputstream.close();
			}
			finally {
				if (inputstream != null) {
					inputstream.close();
				}
			}
		}
		catch (Exception ex) {
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
