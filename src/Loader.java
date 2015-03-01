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
import org.solinari.dataEngine.*;

/**
 * @author solinari
 *
 */
public class Loader {

	
	/**
	 * @param args
	 * @throws Exception 
	 */

	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String jobName = "InputDriver";
		String srcTable = "sh600292_history";
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
		Engine inputDriver = EngineFactory.getInputDriver(job.getConfiguration());
//		inputDriver.search(jobargs, searchColumns, SearchMapper.class, null);
//		inputDriver.setMapClazz(SearchMapper1.class);
		inputDriver.setMapClazz(Abc.class);
		inputDriver.setReduceClazz(SearchReducer.class);
		inputDriver.search(jobargs, searchColumns);

		
	}

}
