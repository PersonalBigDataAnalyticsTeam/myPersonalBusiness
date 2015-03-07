/**
 * 
 */
package org.solinari.analyzer.engine;

import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;
import org.solinari.data.format.SinaHistoryStockDataFormatFactory;

/**
 * @author solinari
 *
 */
public class EngineFactory {
	private static Logger LOG = Logger.getLogger(EngineFactory.class);
	private final static String CLASS_NAME = "org.solinari.datastream.InputDriver.class";
	
	public synchronized static Engine getInputDriver(Configuration conf) {
	    String clazz = conf.get(CLASS_NAME, Engine.class.getName());
	    Engine impl = null;
	    try {
	    	LOG.debug("Using Class impl: " + clazz);
	    		Class<?> implClass = Class.forName(clazz);
	    		impl = (Engine)implClass.newInstance();
	    		impl.setConf(conf);
	      } catch (Exception e) {
	        throw new RuntimeException("Couldn't create " + clazz, e);
	      }
	    
	    return impl;
	  }
}
