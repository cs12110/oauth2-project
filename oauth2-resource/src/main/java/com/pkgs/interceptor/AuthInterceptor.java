package com.pkgs.interceptor;

import com.alibaba.fastjson.JSON;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限校验拦截器
 *
 * @author huanghuapeng create at 2019/4/11 9:16
 * @version 1.0.0
 */
@Component
@Aspect
public class AuthInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Around("execution(public * com.pkgs.ctrl..*(..))")
    public Object checkAuth(ProceedingJoinPoint pjp) {
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != requestAttributes) {
                //获取客户端传来的OAuth资源请求
                HttpServletRequest req = requestAttributes.getRequest();
                OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(req, ParameterStyle.QUERY);


                //获取Access Token
                String accessToken = oauthRequest.getAccessToken();
                
                if (null == accessToken || "".equals(accessToken.trim())) {
                    logger.error("Auth doesn't permit");
                } else {
                    logger.info("Auth token: {}", accessToken);
                    return pjp.proceed();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("{}", e.getMessage());
        }

        return JSON.toJSONString(buildErrMap());
    }


    private Map<String, Object> buildErrMap() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", 404);
        map.put("message", "auth token is wrong,please check again");

        return map;
    }
}
