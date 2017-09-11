package com.clouyun.charge.common.secruity;


import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.services.CacheService;
import com.clouyun.charge.common.constant.RedisKeyEnum;
import com.clouyun.charge.modules.system.service.UserService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 最核心的地方，就是提供某个资源对应的权限定义，即getAttributes方法返回的结果。 此类在初始化时，应该取到所有资源及其对应角色的定义。
 */
@Service
public class CustomInvocationSecurityMetadataSourceService implements
        FilterInvocationSecurityMetadataSource {

    @Autowired
    private UserService userService;
    @Autowired
    private CacheService cacheService;

    /**
     * 平台是一个资源对应一个权限，不存在一个资源可以有多个权限可访问的情况，后期出现多个权限对应一个资源再扩展
     */
    @PostConstruct
    private void loadResourceDefine() {
        // 在Web服务器启动时，提取系统中的所有权限。</span>
        List<DataVo> list = userService.getAllPermTarget();
         /* 应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色和平台自定义权限，
            理论上一个资源可以由多个权限来访问，现阶段只有一个。
		 */
        Map<String, Collection<String>> resourceMap = new HashMap<>();
        for (DataVo dataVo : list) {
            //ConfigAttribute ca = new SecurityConfig(dataVo.getString("permission"));
            // 因为是RESTful风格API，所有需要带上请求method
            String url = dataVo.getString("method") + "_" + dataVo.getString("target");
            // 一个URL对应一个资源，所有权限直接赋值集合(备注：SecurityConfig类不可被反序列化，用String存储)
            //resourceMap.put(url, Lists.<ConfigAttribute>newArrayList(new SecurityConfig(dataVo.getString("permission"))));
            resourceMap.put(url, Lists.newArrayList(dataVo.getString("permission")));
        }
        cacheService.set(RedisKeyEnum.RESOURCE_AUTH_KEY.value, resourceMap);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    // 根据URL，找到相关的权限配置。
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // object 是一个URL，被用户请求的url。
        FilterInvocation filterInvocation = (FilterInvocation) object;
        Map<String, Collection<String>> resourceMap = cacheService.get(RedisKeyEnum.RESOURCE_AUTH_KEY.value);
        // 重试一次后仍为空，那么所有请求会当做公共资源处理
        if (resourceMap == null || resourceMap.isEmpty()) {
            loadResourceDefine();
            resourceMap = cacheService.get(RedisKeyEnum.RESOURCE_AUTH_KEY.value);
        }
        // 因为我们用的是Restful风格API，要携带请求method类型才能匹配是否有权限
        Iterator<String> ite = resourceMap.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next();
            String[] method_url = key.split("_", 2);
            RequestMatcher requestMatcher = new AntPathRequestMatcher(method_url[1], method_url[0]);
            if (requestMatcher.matches(filterInvocation.getHttpRequest())) {
                Collection<String> strAuth = resourceMap.get(key);
                return CollectionUtils.collect(strAuth, new Transformer() {
                    @Override
                    public Object transform(Object o) {
                        return new SecurityConfig(o.toString());
                    }
                });
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

}

