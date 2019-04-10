package com.pkgs.ctrl;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huanghuapeng create at 2019/4/10 19:21
 * @version 1.0.0
 */
@Controller
@RequestMapping("/oauth/server/")
public class UserInfoController {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @RequestMapping("/userInfo")
    @ResponseBody
    public String userInfo(HttpServletRequest request) throws OAuthSystemException {
        logger.info("UserInfo");
        try {
            //获取客户端传来的OAuth资源请求
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
            //获取Access Token
            String accessToken = oauthRequest.getAccessToken();
            logger.info("AccessToken:{}", accessToken);

            //返回用户名 
            //User user=userService.selectByPrimaryKey(1);
            String username = accessToken + "---" + Math.random() + "----" + " haiyan";

            logger.info("UserName:{}", username);
            return username;
        } catch (OAuthProblemException e) {
            e.printStackTrace();
            return e.getMessage();
        }


    }
}
