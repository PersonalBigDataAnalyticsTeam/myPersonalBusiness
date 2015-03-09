/**
 * 
 */
package org.solinari.data.formater;

import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;
import org.solinari.analyzer.engine.Engine;
import org.solinari.data.collector.DataCollector;

/**
 * @author solinari
 * Factory for SinaHistoryStockDataFormat
 */
public class SinaHistoryStockDataFormatFactory {
	private static Logger LOG = Logger.getLogger(SinaHistoryStockDataFormatFactory.class);
	private final static String CLASS_NAME = "org.solinari.data.formater.SinaHistoryStockDataFormat";
	
	public synchronized static SinaHistoryStockDataFormat getSinaHistoryStockDataFormater() {
		SinaHistoryStockDataFormat impl;
	    try {
	    	LOG.debug("Using Class impl: " + CLASS_NAME);
	    		impl = (SinaHistoryStockDataFormat) Class.forName(CLASS_NAME).newInstance();
	      } catch (Exception e) {
	        throw new RuntimeException("Couldn't create " + CLASS_NAME);
	      }
	    
	    return impl;
	  }
}
