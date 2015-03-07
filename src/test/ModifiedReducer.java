package test;
import java.io.IOException;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.solinari.analyzer.engine.SearchReducer;

/**
 * 
 */

/**
 * @author solinari
 *
 */
public class ModifiedReducer extends SearchReducer{
	@Override
	public void dataProcesser(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
		System.out.println("reduce: ");
		for (Text val : values) {
			System.out.print(val.toString() + " ");
		}
		System.out.println();
//		context.write(new Text(""), new IntWritable(1));
	}
}
