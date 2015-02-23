/**
 * 
 */
package org.server.sina;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * @author solinari
 *
 */
public class SinaStockServer {

	final static String sinaUrl = "http://hq.sinajs.cn/list=";
	
	public String getStockInfo(String stockCode){
		String url = sinaUrl + stockCode;
		String result = null;
		
		try {
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
				//String[] datas = result.split(",");
				return result;
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
	
}

