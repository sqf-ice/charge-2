package com.clouyun.charge.common.utils;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.StringUtils;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年05月06日
 */
public class CommonUtils {

    public final static String PATH = "http://xqc.carenergynet.cn/";

    /**
     * 通用字段非空验证 提示：{nullMessage}不能为空!
     *
     * @param chkValue    验证字段值
     * @param nullMessage 为空时消息提示
     * @throws BizException
     */
    public static void valIsEmpty(String chkValue, String nullMessage) throws BizException {
        if (StringUtils.isBlank(chkValue))
            throw new BizException(1000012, nullMessage);
    }

    /**
     * 通用ID非空验证，ID获取方式见{@link com.clouyun.boot.common.domain.DataVo#getInt(String)}方法,提示：{nullMessage}不能为空!
     *
     * @param id          验证字段 为空时返回0
     * @param nullMessage 为空时消息提示
     * @throws BizException
     */
    public static void idIsEmpty(Integer id, String nullMessage) throws BizException {
        if (id == 0)
            throw new BizException(1000012, nullMessage);
    }

    private static Date date = new Date();
    private static int seq = 0;

    /**
     * 生产随机数
     * @description
     * @return
     */
    public static synchronized String randomNum() {
        if (seq > 9999) seq = 0;
        date.setTime(System.currentTimeMillis());
        String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", date, seq++);
        return str;
    }

    /**
     * 封装Request请求参数为Map
     * @return
     * @throws BizException
     */
    public static Map gerParamterMap(Map<String, String[]> tmp) throws Exception {
        Map data = Maps.newHashMap();
        if (tmp != null) {
            for (String key : tmp.keySet()) {
                String[] values = tmp.get(key);
                data.put(key, values[0].trim());
            }
        }
        return data;
    }

    /**
     * 转换入参默认值
     * @param vo
     */
    public static Map convertDefaultVal(DataVo vo,List<String> list){
        Map resultVo = new HashMap();
        resultVo.putAll(vo);

        if(list != null && list.size() > 0){
            for (String key: list) {
                if(vo.get(key) == null || "".equals(vo.get(key))){
                    resultVo.put(key,null);
                }else{
                    resultVo.put(key,vo.get(key));
                }
            }
        }
        return resultVo;
    }
}
