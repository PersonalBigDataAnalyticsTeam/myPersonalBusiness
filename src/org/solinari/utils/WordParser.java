/**
 * 
 */
package org.solinari.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hwpf.extractor.WordExtractor;

/**
 * @author solinari
 *
 */
public class WordParser {
	public static void readWord2003() {
		try {
			FileInputStream fis = new FileInputStream("/home/solinari/文档/stock/股票代码查询一览表.doc");
			WordExtractor wordExtractor = new WordExtractor(fis);
			String [] ss = wordExtractor.getText().split("\n");
			
			for (String s : ss){
				if ('' == s.charAt(0)){
					String [] st = s.split("\t");
					//System.out.println(st[1]);
					String [] xt = st[1].split(" ");
					int pos = xt[0].indexOf('(');
					System.out.println(xt[0].substring(0, pos));
					
					int pos1 = xt[1].lastIndexOf('.');
					int pos2 = xt[1].lastIndexOf('/');
					System.out.println(xt[1].substring(pos2+1, pos1));
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();
		}  
    }
}
