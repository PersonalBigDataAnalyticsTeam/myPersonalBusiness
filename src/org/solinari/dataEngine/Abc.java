package org.solinari.dataEngine;

import java.io.IOException;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class Abc extends SearchMapper{
	@Override
	public void dataProcesser(String row, Context context){
//		System.out.println(row);
		System.out.println("Child");
		try {
			context.write(new Text(""), new Text(row));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
