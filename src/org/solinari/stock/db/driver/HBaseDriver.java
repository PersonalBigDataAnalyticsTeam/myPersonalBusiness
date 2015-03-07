/**
 * 
 */
package org.solinari.stock.db.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.commons.logging.Log;

/**
 * @author solinari
 * 管理与HBase相关的读写流程
 * HBase用于存放stock的各种数据，历史/实时
 */
public class HBaseDriver {
	private final static Log LOG = LogFactory.getLog(HBaseDriver.class);
	private final static String HBASE_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
	private final static String LOCAL_HOST = "localhost";
	
	private static Configuration conf = null;
	static{
		conf = HBaseConfiguration.create();
    	conf.set(HBASE_ZOOKEEPER_QUORUM, LOCAL_HOST);	//设置为自己的RegionServers
	}

    /*
    public StoreMetaData(){
    	this.conf = HBaseConfiguration.create();
    	this.conf.set("hbase.zookeeper.quorum", "localhost");	//设置为自己的RegionServers
    }*/
	
	public static boolean isTableExist(String tablename) throws IOException{
		HBaseAdmin admin = new HBaseAdmin(conf);
		return admin.tableExists(tablename);
	}
	
	public static void createTable(String tablename, String[] cfs) throws IOException {
    		HBaseAdmin admin = new HBaseAdmin(conf);
    		
        if (admin.tableExists(tablename)) {
        	LOG.debug("表"+tablename+"已经存在"); //System.out.println("表"+tablename+"已经存在");
        }
        else {
        		HTableDescriptor tabledesc = new HTableDescriptor(tablename);
        	for (int i = 0; i < cfs.length; i++) {
        			tabledesc.addFamily(new HColumnDescriptor(cfs[i]));
        	}
        	
        	try{
        			admin.createTable(tabledesc);
        		LOG.debug("表"+tablename+"创建成功！"); //System.out.println("表"+tablename+"创建成功！");
        	}
        	catch (Exception ex){
        		LOG.error("HBase create table failed table: " + tablename + " StackTrace: " + ex.getStackTrace());
        	}
        }
	}
    
	public static void deleteTable(String tablename) throws IOException {
		HBaseAdmin admin;
		
		try {
			admin = new HBaseAdmin(conf);
			admin.disableTable(tablename);
			admin.deleteTable(tablename);
			LOG.debug("表“+tablename+”删除成功！");	//System.out.println("表“+tablename+”删除成功！");
		} catch (MasterNotRunningException e) {
	            // TODO Auto-generated catch block
			LOG.error("表“+tablename+”删除成功！");
//			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
	            // TODO Auto-generated catch block
			LOG.error("in HBase table delete ZooKeeper connection exception StackTrace: " + e.getStackTrace());
		}        
	}
    
	public static void writeRow(String tablename, String row, String[] cfs, String[] values) throws IOException {	//表 行键 列族
		try {
			HTable table = new HTable(conf, tablename);
			Put put = new Put(Bytes.toBytes(row));	// 一个PUT代表一行数据，再NEW一个PUT表示第二行数据,每行一个唯一的ROWKEY，此处rowkey为row

			for (int j = 0; j < cfs.length; j++) {
				/*
				 * put方法的三个参数ColumnFamily Column Value
				 */
				put.add(Bytes.toBytes(cfs[j]), Bytes.toBytes(cfs[j]), Bytes.toBytes(values[j]));
				table.put(put);
			}

			LOG.debug("HBase write row success!");	//System.out.println("插入行成功！");
		} catch (IOException e) {
			LOG.error("HBase write row failed row: " + row + " StackTrace: " + e.getStackTrace());
//			LOG.error(null, e);	//e.printStackTrace();
		}
	}
    
	public static void deleteRow(String tablename, String rowkey) throws IOException {
		HTable table = new HTable(conf, tablename);
		List list = new ArrayList();
		Delete d1 = new Delete(rowkey.getBytes());
		list.add(d1);
		try{
			table.delete(list);
			LOG.debug("HBase write row success! row: " + rowkey);
//			System.out.println("删除行成功！");
		} catch (IOException e) {
			LOG.error("HBase delete row failed row: " + rowkey + " StackTrace: " + e.getStackTrace());
		}
	}

	public static boolean isRowExisting(String tablename, String rowkey) throws IOException {
		HTable table = new HTable(conf, tablename);
		Get get = new Get(rowkey.getBytes());
		Result rs = table.get(get);
		return (!rs.isEmpty());
	}
	
	public static Result selectRow(String tablename, String rowkey) throws IOException {
		HTable table = new HTable(conf, tablename);
		Get g = new Get(rowkey.getBytes());
		Result rs = table.get(g);
		return rs;
	}
    		
	public static void scanner(String tablename) {
		try {
			HTable table = new HTable(conf, tablename);
			Scan s = new Scan();
			ResultScanner rs = table.getScanner(s);

			for (Result r : rs) {
				KeyValue[] kv = r.raw();
				for (int i = 0; i < kv.length; i ++) {
					System.out.print(new String(kv[i].getRow()) + " ");
					System.out.print(new String(kv[i].getFamily()) + ":");
					System.out.print(new String(kv[i].getQualifier()) + " ");
					System.out.print(kv[i].getTimestamp() + " ");
					System.out.println(new String(kv[i].getValue()));
				}
			}
			LOG.debug("HBase scan success!");
		} catch (IOException e) {
			LOG.error("HBase scan failed StackTrace: " + e.getStackTrace());
//			e.printStackTrace();
		}
	}
    
	public static void run() throws IOException {
		//String[] fam = {"fam1", "fam2", "fam3"};
		//createTable("table1", fam);
		//writeRow("table1", "row1", fam, fam);
		//writeRow("table1", "row2", fam);
		//selectRow("table1", "row2");
		//scanner("table1");        
	}
    
/*
	private final static String stock = "sh601006";
	
	public static class Map extends TableMapper<Text, Text> {

		public void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
			SinaStockServer server = new SinaStockServer();
			String stockDatas = server.getStockInfo(stock);
			Text result = new Text(stockDatas);
			context.write(new Text(stock), result);
			System.out.println(result);
		}
	}*/
}
