/**
 * 
 */
package com.clouyun.charge.modules.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 描述: TaskInfoMapper
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年3月11日
 */
@Mapper
public interface TaskInfoMapper {
	
	/**
	 * 查询列表
	 * @param List<Map>
	 * @return
	 * gahui
	 * 2.0.0
	 */
	List<Map> get(Map map);
	/**
	 * 查询详情
	 * @param 
	 * @return
	 * gahui
	 * 2.0.0
	 */
	Map getById(Integer taskId);
	/**
	 * 查询列表
	 * @param map
	 * @return
	 */
	List queryListByPage(Map map);
	/**
	 * 返回列表总行数
	 * @param map
	 * @return
	 */
	int queryListCount(Map map);
	/**
	 * 通过Id查询返回单行数据
	 * @param map
	 * @return
	 */
	Map queryById(Map map);
	/**
	 * 检查数据唯一性
	 * @param map
	 * @return
	 */
	int checkUniqueness(Map map);
	/**
	 * 新增数据
	 * @param map
	 * @return
	 */
	int insert(Map map);
	/**
	 * 删除多条数据
	 * @param map
	 * @return
	 */
	int delete(Map map);
	/**
	 * 根据rec_id获取任务信息
	 * @return
	 * 2017年3月24日
	 * gaohui
	 */
	Map findTaskInfoByRecId(Map map);
	 /**
     * 根据recId查询任务信息
     * 版本:2.0.0
     * 作者:gaohui
     * 日期:2017年8月23日
     */
    Map getTaskInfoByRecId(Integer recId);
	/**
	 * 根据告警id更新作业信息
	 * @param map
	 * 2017年3月29日
	 * gaohui
	 */
	void updateTaskInfoByRecId(Map map);
	
	/**
	 * 场站派单排行
	 * @param vo
	 * @return
	 */
	List getTaskSubTop(DataVo vo);
	
	/**
	 * 场站派单总数
	 * @param vo
	 * @return
	 */
	int getTaskSubCount(DataVo vo);

	/**
	 * 外勤用户任务数排行
	 * @param vo
	 * @return
	 */
	List getFinishTaskTop(DataVo vo);
	
	/**
	 * 任务紧急程度完成排行
	 * @param vo
	 * @return
	 */
	List getLevelTaskTop(DataVo vo);
	
	/**
	 * 任务处理
	 * @param vo
	 */
	void updateTaskInfo(Map map);
	
	/**
	 * 任务模板项值
	 * @param vo
	 * @return
	 */
	List getTaskOptionValue(DataVo vo);
	
	/**
	 * 获取照片(照片+位置)
	 * @param vo
	 * @return
	 */
	List getTaskOptionPhoto(DataVo vo);
	
	/**
	 * 模板项位置数据
	 * @param vo
	 * @return
	 */
	List getTaskOptionPosition(DataVo vo);
	
	/**
	 * 获取物料
	 * @param vo
	 * @return
	 */
	List getTaskOptionMateriel(DataVo vo);
	
	/**
	 * 根据recId批量查询任务信息
	 * @param map
	 * @return
	 * 2017年4月11日
	 * gaohui
	 */
	List<DataVo> getTaskInfoByRedIds(Map map);
}
