/**
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.util.Strings;
import org.apache.hadoop.mapreduce.Job;
import org.solinari.analyzer.engine.*;
import org.solinari.data.collector.DataCollector;

/**
 * @author solinari
 *
 */
public class Loader {

	
	/**
	 * @param args
	 * @throws Exception 
	 */

	public static void loadHbaseMapReduce() throws Exception{
		String jobName = "InputDriver";
//		String srcTable = "sh600292_history";
		String srcTable = "sh600016_history";
		String dstTable = "nonetable";
		String[] jobargs = {jobName, srcTable, dstTable};
		HashMap searchColumns = new HashMap();
		String a = "date";
		String[] b = {"date"};
		searchColumns.put(a, b);
		a = "price";
		String[] c = {"price"};
		searchColumns.put(a, c);
		
		Configuration conf = new Configuration();
		Job job = new Job(conf, jobName);
		Engine engine = EngineFactory.getInputDriver(job.getConfiguration());
//		inputDriver.search(jobargs, searchColumns, SearchMapper.class, null);
//		inputDriver.setMapClazz(SearchMapper1.class);
		engine.setMapClazz(test.ModifiedMapper.class);
		engine.setReduceClazz(test.ModifiedReducer.class);
		engine.run(jobargs, searchColumns);
	}
	
	public static void loadDataCollector(){
		DataCollector monitor = new DataCollector();
		monitor.runHistoryCollector();
//		monitor.runCollector();
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		loadHbaseMapReduce();
//		loadDataCollector();
		
	}

}
