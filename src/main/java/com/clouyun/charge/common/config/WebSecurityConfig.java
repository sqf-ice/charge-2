package com.clouyun.charge.common.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*@Configuration
@EnableWebSecurity*/
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       /* http
                .authorizeRequests()
                .antMatchers("*//*", "/home").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();*/
        /*http.authorizeRequests().anyRequest().fullyAuthenticated().and().
                httpBasic().and().
                csrf().disable();*/
        /*http.authorizeRequests().anyRequest().anonymous().and().
                httpBasic().and().
                csrf().disable();*/
    }

   /* @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }*/
}