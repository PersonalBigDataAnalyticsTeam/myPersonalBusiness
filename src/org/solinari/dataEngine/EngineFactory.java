/**
 * 
 */
package org.solinari.dataEngine;

import org.apache.hadoop.conf.Configuration;

/**
 * @author solinari
 *
 */
public class EngineFactory {

	private static String CLASS_NAME = "org.solinari.datastream.InputDriver.class";
	
	public synchronized static Engine getInputDriver(Configuration conf) {
	    String clazz = conf.get(CLASS_NAME, Engine.class.getName());
	    Engine impl = null;
	    try {
//		        LOG.info("Using InputDriver impl: " + clazz);
		        Class<?> implClass = Class.forName(clazz);
		        impl = (Engine)implClass.newInstance();
		        impl.setConf(conf);
	      } catch (Exception e) {
	        throw new RuntimeException("Couldn't create " + clazz, e);
	      }
	    
	    return impl;
	  }
}
