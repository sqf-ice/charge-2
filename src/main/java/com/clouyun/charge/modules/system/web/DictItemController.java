package com.clouyun.charge.modules.system.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.system.service.DictItemService;
@RestController
@RequestMapping("/api/items")
public class DictItemController extends BusinessController{
	
	@Autowired
	private DictItemService dictItemService;
    /**
     * 根据typeId查询
     * @param data
     * @return
     * @throws Exception
     * 2017年3月6日
     * gaohui
     */
	@RequestMapping(value = "/qItemList", method = RequestMethod.POST)
    public ResultVo queryDictItemList(@RequestBody DataVo data){
        return dictItemService.queryDictItemList(data);
    }
	/**
     * 根据typeId和num查询
     * @param data
     * @return
     * @throws Exception
     * 2017年3月6日
     * gaohui
     */
	@RequestMapping(value = "/qItem", method = RequestMethod.POST)
    public ResultVo queryDictItem(@RequestBody DataVo data){
        return dictItemService.queryDictItem(data);
    }
    /**
     * 更新字典表
     * @param data
     * @return
     * 2017年3月6日
     * gaohui
     */
	@RequestMapping(value = "/uItembyIdAndNum",method = RequestMethod.POST)
	public ResultVo updateDictItembyIdAndNum(@RequestBody DataVo data){
		ResultVo vo = new ResultVo();
		dictItemService.updateDictItembyIdAndNum(data);
		return vo;
	}

}
