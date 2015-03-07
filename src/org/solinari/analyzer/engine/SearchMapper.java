/**
 * 
 */
package org.solinari.analyzer.engine;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper.Context;

/**
 * @author solinari
 *
 */

/*
 * 输出时无key，数据自己组织
 */
public class SearchMapper extends  TableMapper<Text, Text> {
		
	private String[] columnFamilys;
	private final static String SEARCH_FAMILY = "search.columnfamilys";
	private final static String SEARCH_COLUMN_STRING = "search.column";
	
	private static String getColumnString(String column){
		return SEARCH_COLUMN_STRING + column;
	}
	
	/*
	 * 通过自定义子类Oerride后对row中的数据进行处理:)
	 */
	public void dataProcesser(String row, Context context) throws IOException, InterruptedException{
		/*
		 *MapperBaseClass
		 *do things you want in Override:) 
		 */
	}
	
	/*
	 * 用来从config中取得search的family和column
	 */
	private void parseSearchConf(Configuration conf){
		columnFamilys = conf.getStrings(SEARCH_FAMILY);
	}
	
	@Override
	/*
	 * (non-Javadoc)	每次取Hbase里一个row
	 * @see org.apache.hadoop.mapreduce.Mapper#map(KEYIN, VALUEIN, org.apache.hadoop.mapreduce.Mapper.Context)
	 */		
	protected void map(ImmutableBytesWritable key, Result value, Context context) throws InterruptedException, IOException {  
		Configuration conf = context.getConfiguration();
		parseSearchConf(conf);
		int size = columnFamilys.length;
		for (int i=0; i<size; i++){
			String familyString = columnFamilys[i];
			String[] columns = conf.getStrings(getColumnString(familyString));
			byte[] family = Bytes.toBytes(familyString);
			for (int j=0; j<columns.length; j++){
				byte[] column = Bytes.toBytes(columns[j]);
				String searchResult = Text.decode(value.getValue(family, column));
				dataProcesser(searchResult, context);
				//注意添加context的output 用于其他地方的分析
			}
		}
	}
}  

