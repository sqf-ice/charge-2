package com.clouyun.charge.modules.vehicle.service;

import com.clou.common.utils.CalendarUtils;
import com.clou.common.utils.ObjectUtils;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.vehicle.mapper.VehicleMileageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 描述: 车辆里程
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年6月27日 上午9:04:18
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class VehicleMileageService {

    private static final int BATCH_MAX_SIZE = 100;
    
    @Autowired
    VehicleMileageMapper mileageMapper;

    public String runData() throws BizException {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        String endTime = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMddHHmmss);

        cal.add(Calendar.DAY_OF_MONTH, -1);
        String startTime = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMddHHmmss);

        Map map = new HashMap();
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        Integer count = mileageMapper.queryMileageCount(map);

        if(count <= 0){
            List<Map> queryHistory = mileageMapper.queryHistory(map);
            List<Map> saveList = new ArrayList();
            if (queryHistory != null && queryHistory.size() > 0) {
                for (Map map2 : queryHistory) {
                    Map saveMap = new LinkedHashMap();

                    BigDecimal maxMileage = BigDecimal.ZERO;
                    BigDecimal minMileage = BigDecimal.ZERO;

                    if (map2.get("max") != null) {
                        maxMileage = new BigDecimal(map2.get("max").toString());
                    }
                    if (map2.get("min") != null) {
                        minMileage = new BigDecimal(map2.get("min").toString());
                    }

                    BigDecimal mileage = maxMileage.subtract(minMileage);


                    if (!ObjectUtils.isNull(map2.get("day"))) {
                        Calendar day = CalendarUtils.convertStrToCalendar(map2.get("day").toString(), CalendarUtils.yyyyMMdd);
                        int year = day.get(Calendar.YEAR);    //获取年
                        int month = day.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份

                        saveMap.put("year", year);
                        saveMap.put("month", month);
                    }

                    saveMap.put("mileage", mileage);
                    saveMap.putAll(map2);
                    saveList.add(saveMap);
                }
            }


            if (saveList.size() > 0) {
                for (int i = 0; i < saveList.size() / BATCH_MAX_SIZE + 1; i++) {
                    if ((i + 1) * BATCH_MAX_SIZE < saveList.size()) {
                        mileageMapper.batchInser(saveList.subList(i * BATCH_MAX_SIZE, (i + 1) * BATCH_MAX_SIZE));
                    } else {
                        mileageMapper.batchInser(saveList.subList(i * BATCH_MAX_SIZE, saveList.size()));
                        break;
                    }
                }
            }
        }
        return "数据处理完毕";
    }
}
