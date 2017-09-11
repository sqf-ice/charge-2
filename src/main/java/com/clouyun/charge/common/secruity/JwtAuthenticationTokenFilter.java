package com.clouyun.charge.common.secruity;

import com.clouyun.boot.services.CacheService;
import com.clouyun.charge.common.constant.RedisKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
@Order(0)
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);

        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            // token失效后直接取不出来username
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            logger.info("checking authentication " + username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 优先从内存库去，正常情况下内存库如果没有，则说明token失效，token失效后username直接为空不进来
                JwtUser jwtUser = cacheService.get(RedisKeyEnum.JWT_USER_KEY.value + username);

                // 如果我们足够相信token中的数据，也就是我们足够相信签名token的secret的机制足够好
                // 这种情况下，我们可以不用再查询数据库，而直接采用token中的数据
                // 本例中，我们还是通过Spring Security的 @UserDetailsService 进行了数据查询
                // 但简单验证的话，你可以采用直接验证token是否合法来避免昂贵的数据查询
                // 添加补救措施
                if (jwtUser == null)
                    jwtUser = (JwtUser) this.userDetailsService.loadUserByUsername(username);

                if (jwtTokenUtil.validateToken(username, authToken, jwtUser)) {
                    // 缺少一个用户名密码的验证过滤，这块只对当前请求的request赋权限
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            jwtUser, null, jwtUser.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    logger.info("authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    //// 认证通过后重新包装request，从token中取出userId存放（只针对GET方法有效）
                    //Map<String, String[]> m = new HashMap<>(request.getParameterMap());
                    //m.put("userId", new String[] { "1" });
                    //request = new ParameterRequestWrapper(request, m);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
