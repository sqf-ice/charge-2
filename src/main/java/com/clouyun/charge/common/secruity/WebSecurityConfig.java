package com.clouyun.charge.common.secruity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
// 支持spring security表达式方式控制权限
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // 是否启用jwt过滤
    @Value("${jwt.enabled}")
    private boolean enabled;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    private AccessDeniedHandlerEntryPoint accessDeniedHandler;

    // Spring会自动寻找同样类型的具体类注入，这里就是JwtUserDetailsServiceImpl了
    @Autowired
    private UserDetailsService userDetailsService;

    // 装载MD5_Salt密码编码器
    @Autowired
    public MD5SaltPasswordEncoder md5Salt;

    //@Autowired
    //private CustomInvocationSecurityMetadataSourceService mySecurityMetadataSource;
    //
    //@Autowired
    //private CustomAccessDecisionManager myAccessDecisionManager;
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(this.userDetailsService)
                // 使用BCrypt进行密码的hash
                //.passwordEncoder(passwordEncoder());
                .passwordEncoder(md5Salt);
    }


    // 装载BCrypt密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        if (enabled) {
            httpSecurity
                    // 由于使用的是JWT，我们这里不需要csrf
                    .csrf().disable()
                    // 未授权情况异常监控
                    .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                    // 验证权限异常监控
                    .exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
                    // 基于token，所以不需要session
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .authorizeRequests()
                    //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                    // 允许对于网站静态资源的无授权访问
                    .antMatchers(
                            HttpMethod.GET,
                            "/",
                            "/*.html",
                            "/favicon.ico",
                            "/**/*.html",
                            "/**/*.css",
                            "/**/*.js"
                    ).permitAll()
                    // 对于获取token的rest api要允许匿名访问
                    .antMatchers("/api/users/auth/**", "/api/users/refresh/**", "/api/users/_loginOut").permitAll()
                    // 除上面外的所有请求全部需要鉴权认证
                    .anyRequest().authenticated()
            //这里的定义为任何请求都要拦截，经过自定义的ObjectPostProcessor.这种写法会导致无token情况下不被拦截，必须要执行FilterSecurityInterceptor，而不是覆盖他
            // setSecurityMetadataSource设置CustomInvocationSecurityMetadataSourceService执行数据资源,getAttributes返回URL所需权限
            // setAccessDecisionManager赋值给真实的权限拦截对象，赋值后被AbstractSecurityInterceptor.afterInvocation方法调用执行CustomAccessDecisionManager.decide方法
            //.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            //    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
            //        fsi.setSecurityMetadataSource(mySecurityMetadataSource);
            //        fsi.setAccessDecisionManager(myAccessDecisionManager);
            //        return fsi;
            //    }
            //})
            ;

            // 添加JWT filter
            httpSecurity
                    .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
            // 禁用缓存
            httpSecurity.headers().cacheControl();
        } else {
            httpSecurity
                    // 由于使用的是JWT，我们这里不需要csrf
                    .csrf().disable();
        }
    }
    //@Bean
    //public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
    //    return new JwtAuthenticationTokenFilter();
    //}

    /*@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 省略之前写的规则部分，具体看前面的代码

        // 添加JWT filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }*/
}