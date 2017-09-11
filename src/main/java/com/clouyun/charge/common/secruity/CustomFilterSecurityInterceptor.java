package com.clouyun.charge.common.secruity;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年07月01日
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

/**
 * 该过滤器的主要作用就是通过spring著名的IoC生成securityMetadataSource。
 * securityMetadataSource相当于本包中自定义的CustomFilterSecurityInterceptor。
 * 该CustomFilterSecurityInterceptor的作用提从数据库提取权限和资源，装配到HashMap中，
 * 供Spring Security使用，用于权限校验。
 * fsi.setSecurityMetadataSource(mySecurityMetadataSource);
 * fsi.setAccessDecisionManager(myAccessDecisionManager);
 */
//@Component
public class CustomFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    @Autowired
    private CustomInvocationSecurityMetadataSourceService mySecurityMetadataSource;

    @Autowired
    private CustomAccessDecisionManager myAccessDecisionManager;
    // 暂时没用，调用默认实现
    //@Autowired
    //private AuthenticationManager authenticationManager;

    //@Autowired
    //@Override
    //public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
    //    super.setAccessDecisionManager(myAccessDecisionManager);
    //}

    //@Autowired
    //@Override
    //public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    //    super.setAuthenticationManager(authenticationManager);
    //}
    @PostConstruct
    public void init() {
        //super.setAuthenticationManager(authenticationManager);
        // 赋值给真实的权限拦截对象，赋值后被AbstractSecurityInterceptor.afterInvocation方法调用执行CustomAccessDecisionManager.decide方法
        super.setAccessDecisionManager(myAccessDecisionManager);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);
    }

    public Class<? extends Object> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        logger.debug("CustomInvocationSecurityMetadataSourceService.getAttributes方法返回请求URL需要权限集合");
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            // 传入权限集合后调用CustomAccessDecisionManager.decide做最后真实判断，没有权限直接抛出异常
            super.afterInvocation(token, null);
        }
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        logger.debug("返回数据库获取资源服务......");
        return this.mySecurityMetadataSource;
    }

    public void destroy() {
        logger.info("CustomFilterSecurityInterceptor被销毁......");
    }

    public void init(FilterConfig filterconfig) throws ServletException {
        logger.info("CustomFilterSecurityInterceptor被初始化......");
    }
}