package com.trafficinfosystem.demo.interceptor;

import com.trafficinfosystem.demo.constant.JwtClaimsConstant;
import com.trafficinfosystem.demo.properties.JwtProperties;
import com.trafficinfosystem.demo.utils.JwtUtil;
import com.trafficinfosystem.demo.context.BaseContext;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("JwtTokenAdminInterceptor拦截器开始工作...");
        //判断当前拦截到的是Controller的方法还是其他资源 (Determine whether the currently intercepted method is the Controller's method or other resources)
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌 (Get token from request header)
        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2、校验令牌 (Verification token)
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            String userId = claims.get(JwtClaimsConstant.USER_ID).toString();
            log.info("当前用户id：{}", userId);
            BaseContext.setCurrentId(userId);//save userId in ThreadLocal
            //3、通过，放行 (pass, let go)
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码 (Failure, response 401 status code)
            response.setStatus(401);
            return false;
        }
    }
}
