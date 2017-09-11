package com.clouyun.charge.common.secruity;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.system.mapper.UserMapper;
import com.clouyun.charge.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

//@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    /*@Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
    }*/

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户

        List<DataVo> users = userMapper.getUserByName(username);
        DataVo user = users.get(0);

        try {
            List roles = userService.findUserRoleNames(user.getInt("id"));
            user.put("roles", roles);
        } catch (BizException e) {
            e.printStackTrace();
        }


        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            JwtUser jwtUser = JwtUserFactory.create(user);
            return jwtUser;
        }
    }
}
