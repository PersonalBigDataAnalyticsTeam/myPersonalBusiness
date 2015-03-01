/**
 * 
 */
package org.solinari.dataEngine;

import java.io.IOException;

import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer.Context;

/**
 * @author solinari
 *
 */
public class SearchReducer extends TableReducer<Text, Text, Text>{
  	private IntWritable result = new IntWritable();

  	/*
	 * 通过自定义子类Oerride后对row中的数据进行处理:)
	 */
	public void dataProcesser(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
		/*
		 *ReduceBaseClass
		 *do things you want in Override:) 
		 */
		for (Text val : values) {
			System.out.print(val.toString() + " ");
		}
		System.out.println();
	}
	
 	@Override
  	public void reduce(Text key, Iterable<Text> values, Context context)throws IOException,InterruptedException{
 		dataProcesser(key, values, context);
	}
}
