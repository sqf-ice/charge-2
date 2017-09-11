package com.clouyun.charge.modules.monitor.web;

import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.utils.JsonUtils;
import com.clouyun.cdzcache.imp.CDProcData;
import com.clouyun.cdzcache.obj.QPData;
import com.clouyun.cdzcache.obj.ZPData;

@RestController
@RequestMapping("/api/cdz")
public class CdzCacheDataApi {
	@Autowired
    private CDProcData cpd;
	
	public final Logger logger = LoggerFactory.getLogger(CdzCacheDataApi.class);
	
	/**
	 * 获取桩的采集实时数据
	 * @param pileAddr
	 * @param pileId
	 * @param acType  1,交流  2直流   3 交直流
	 * @return
	 */
	@RequestMapping(value = "/procData/{trmAddr}/{pileId}/{acType}", method = RequestMethod.GET)
	public ResultVo getProcData(@PathVariable("trmAddr") String trmAddr,@PathVariable("pileId") int pileId,@PathVariable("acType") byte acType ){
		ResultVo resultVo = new ResultVo();
		ZPData data = cpd.getCdzProcData(acType, pileId, trmAddr);
		for(int i=0;i<5;i++){
			SortedMap<Long, QPData> dmap = data.getQPDate(i);
			if(dmap!=null){
				logger.warn(i+":"+dmap);
			}
		}
		
		resultVo.setData(data);
		return resultVo;
	}
	
	@RequestMapping(value = "/procData1/{trmAddr}/{pileId}/{acType}", method = RequestMethod.GET) 
    public @ResponseBody ZPData getProcData1(@PathVariable("trmAddr") String trmAddr,@PathVariable("pileId") int pileId,@PathVariable("acType") byte acType ) {
		ZPData data = cpd.getCdzProcData(acType, pileId, trmAddr);
		for(int i=0;i<5;i++){
			SortedMap<Long, QPData> dmap = data.getQPDate(i);
			if(dmap!=null){
				logger.warn(i+":"+dmap);
			}
		}
        return data;
    }
	
	public static void main(String[] args){
		ResultVo resultVo = new ResultVo();
		ZPData data = new ZPData((byte)0, (byte)2, new int[]{100,100});
		SortedMap<Long, QPData> qd = new TreeMap<Long, QPData>();
		QPData pd = new QPData(220D, 23D);
		qd.put(System.currentTimeMillis(), pd);
		data.addQPData(2, qd);
		resultVo.setData(data);
		String jsonstr = JsonUtils.toJson(resultVo);
		System.out.println(jsonstr);
	}
	

}
