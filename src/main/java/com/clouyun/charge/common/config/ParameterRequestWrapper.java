package com.clouyun.charge.common.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

/**
 * 描述: request参数包装器
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年06月15日
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> params;

    public ParameterRequestWrapper(HttpServletRequest request, Map<String, String[]> newParams) {
        super(request);
        this.params = newParams;
        // 重新整合旧的请求，防止自定义参数和提交参数重复
        renewParameterMap(request);
    }

    @Override
    public String getParameter(String name) {
        String result = "";

        Object v = params.get(name);
        if (v == null) {
            result = null;
        } else if (v instanceof String[]) {
            String[] strArr = (String[]) v;
            if (strArr.length > 0) {
                result = strArr[0];
            } else {
                result = null;
            }
        } else if (v instanceof String) {
            result = (String) v;
        } else {
            result = v.toString();
        }

        return result;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return params;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new Vector<>(params.keySet()).elements();
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] result = null;

        Object v = params.get(name);
        if (v == null) {
            result = null;
        } else if (v instanceof String[]) {
            result = (String[]) v;
        } else if (v instanceof String) {
            result = new String[]{(String) v};
        } else {
            result = new String[]{v.toString()};
        }

        return result;
    }

    private void renewParameterMap(HttpServletRequest req) {

        String queryString = req.getQueryString();

        if (queryString != null && queryString.trim().length() > 0) {
            String[] params = queryString.split("&");

            for (int i = 0; i < params.length; i++) {
                int splitIndex = params[i].indexOf("=");
                if (splitIndex == -1) {
                    continue;
                }

                String key = params[i].substring(0, splitIndex);

                if (!this.params.containsKey(key)) {
                    if (splitIndex < params[i].length()) {
                        String value = params[i].substring(splitIndex + 1);
                        this.params.put(key, new String[]{value});
                    }
                }
            }
        }
    }

}