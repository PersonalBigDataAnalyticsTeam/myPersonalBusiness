/**
 * 这个类主要用来对HBase中存储的数据进行select操作，需要从外部调用search接口
 * 
 */
package org.solinari.analyzer.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

/**
 * @author solinari
 *
 */
public class Engine extends Configured{
	private static Logger LOG = Logger.getLogger(Engine.class);

	private final static int JOB_NAME_INDEX = 0;
	private final static int SRC_TABLE_INDEX = 1;
	private final static int DST_TABLE_INDEX = 2;
	private final static String HBASE_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
	private final static String LOCAL_HOST = "localhost";
	private final static String SEARCH_FAMILY = "search.columnfamilys";
	private final static String SEARCH_COLUMN_STRING = "search.column";
	
	private Class<? extends TableMapper> mapClazz = SearchMapper.class;
	private Class<? extends TableReducer> reduceClazz = null;
//	private Class<?> mapKeyClazz = null;
//	private Class<?> mapValueClazz = null;
//	private Class<?> reduceKeyClazz = null;
//	private Class<?> reduceValueClazz = null;

	public void setMapClazz(Class<? extends TableMapper> mapClazz) {
		this.mapClazz = mapClazz;
	}	
	
	public void setReduceClazz(Class<? extends TableReducer> reduceClazz) {
		this.reduceClazz = reduceClazz;
	}

	private static String getColumnString(String column){
		return SEARCH_COLUMN_STRING + column;
	}
	
	private void setSearchConf(Configuration conf, Scan scan, HashMap searchColumns){
		Iterator iterator = searchColumns.entrySet().iterator();
		List<String> list=new ArrayList<String>();
		
		conf.set(HBASE_ZOOKEEPER_QUORUM, LOCAL_HOST);
		while (iterator.hasNext()){   
			Map.Entry mapentry = (Map.Entry) iterator.next();
			String key = mapentry.getKey().toString();
			String [] values = (String[]) mapentry.getValue();
			conf.setStrings(getColumnString(key), values);
			list.add(key);
			for (int i=0; i<values.length; i++){
				scan.addColumn(Bytes.toBytes(key), Bytes.toBytes(values[i]));
			}
		}
 
		int size=list.size();
		String[] columnFamilys = (String[])list.toArray(new String[size]);
		conf.setStrings(SEARCH_FAMILY, columnFamilys);
	}
	
	/*
	 * search接口提供外部搜索HBase的功能，调用者需要在HashMap中指定search使用的HBase列族和列
	 * 使用列族和列的格式如下
	 * Map的每个键值对代表一个列族，每个键值对中的key是String型，表示family；值是一个String[]，表示family下的列
	 * 存入Map的列只是需要做search的列，并不是HBase中的所有的列
	 */
	public int run(String[] args, HashMap searchColumns)throws Exception{
		String jobName = args[JOB_NAME_INDEX];
		String srcTable = args[SRC_TABLE_INDEX];
		String dstTable = args[DST_TABLE_INDEX];
		
		Configuration conf = HBaseConfiguration.create();
		Scan scan = new Scan();
		setSearchConf(conf, scan, searchColumns);

		Job job = new Job(conf, jobName);
		job.setJarByClass(Engine.class);
		
		//根据这里的设置决定map的输出格式和热度测的输入格式
		TableMapReduceUtil.initTableMapperJob(srcTable, scan, mapClazz, Text.class, Text.class, job);
		TableMapReduceUtil.initTableReducerJob(dstTable, reduceClazz, job);
		
		job.waitForCompletion(true);
		return job.isSuccessful()?0:1;
	}
}

