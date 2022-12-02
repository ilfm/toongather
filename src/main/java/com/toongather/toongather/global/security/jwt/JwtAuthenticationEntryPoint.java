package com.toongather.toongather.global.security.jwt;

import com.toongather.toongather.global.common.error.CommonError;
import com.toongather.toongather.global.common.error.CommonRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 유효한 자격증명을 제공하지 않고 접근하려 할때 401 unauthorized 에러 리턴
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.info("exception entrypoint");
        CommonError error = (CommonError) request.getAttribute("exception");
        if(error != null) setResponse(response, error);
        else response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

    }


    private void setResponse(HttpServletResponse response, CommonError error) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"message\" : \"" + error.getMessage()
          + "\", \"code\" : \"" +  error.getCode()
          + "\", \"status\" : " + error.getStatus()
          + ", \"errors\" : [ ] }");
    }

}
