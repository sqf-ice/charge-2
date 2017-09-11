package com.clouyun.charge.common.secruity;

import com.clouyun.boot.common.Constants;
import com.clouyun.boot.common.domain.ResultVo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

@Component
public class AccessDeniedHandlerEntryPoint implements AccessDeniedHandler, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(new ResultVo().setError(Constants.ACCESS_EXCEPTION,"不具备访问权限!"));
        writer.close();
    }
}
