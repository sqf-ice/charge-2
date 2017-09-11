package com.clouyun.charge.common.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadXml {
	private static Logger logger = LoggerFactory.getLogger(ReadXml.class);
	private static Map<Integer, Map<Integer, String>> map;

	private static ReadXml instance = new ReadXml();

	private ReadXml() {}
	public static ReadXml getInstance() {
		return instance;
	}
	/**
     * 解析企业库xml文件
     * @throws JDOMException
     * @throws IOException
     * return {企业ID={key1=value,key2=value}}
	 * @throws ConfigurationException 
     */
	public static void read() throws JDOMException,
			IOException, ConfigurationException {
		if (map == null) {
			map = new HashMap<Integer, Map<Integer,String>>();
			XMLConfiguration xc = new XMLConfiguration("config/keys.xml");
			logger.info(xc.getBasePath());
			/*
			//打印节点
			Iterator<String> itr=xc.getKeys();
			while(itr.hasNext()){
				logger.info("节点:"+itr.next());
			}*/
			int companySize = xc.getMaxIndex("company");
			//logger.info("companySize:"+companySize);
			int keySize = 0;
			for (int i = 0; i <= companySize ; i++) {
				Map<Integer, String> passMap = new HashMap<Integer, String>();
				Integer orgId = xc.getInt("company("+i+")[@orgId]");
				//String name = xc.getString("company("+i+")[@name]");
				keySize = xc.getMaxIndex("company("+i+").key");
				for (int j = 0; j < keySize; j++) {
					Integer key = xc.getInt("company("+i+").key("+j+")[@name]");
					String pass = xc.getString("company("+i+").key("+j+")");
					//logger.info("company("+i+") > key:"+key+",pass:"+pass);
					passMap.put(key, pass);
				}
				map.put(orgId, passMap);
			}
		}
	}

	public Map<Integer, Map<Integer, String>> getOrgMap() throws JDOMException,
			IOException, ConfigurationException {
		if (map == null)
			read();
			return map;
	}
	/**
	 * 重读xml文件
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws ConfigurationException 
	 */
	public synchronized void reRead() throws JDOMException, IOException, ConfigurationException {
		map.clear();
		read();
	};
}
