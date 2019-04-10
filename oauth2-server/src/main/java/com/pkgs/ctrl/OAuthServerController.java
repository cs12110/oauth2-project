package com.pkgs.ctrl;

import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huanghuapeng create at 2019/4/10 19:14
 * @version 1.0.0
 */
@Controller
@RequestMapping("/oauth/server/")
public class OAuthServerController {

    private static final Logger logger = LoggerFactory.getLogger(OAuthServerController.class);

    @RequestMapping("/responseCode")
    public Object toShowUser(HttpServletRequest request) {
        logger.info("ResponseCode");
        try {
            //构建OAuth授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            logger.info("{},{}", oauthRequest.getClientId(), oauthRequest.getClientSecret());

            if (isNotEmptyClientId(oauthRequest.getClientId())) {
                //设置授权码
                String authorizationCode = "authorizationCode";
                //利用oauth授权请求设置responseType，目前仅支持CODE，另外还有TOKEN
                //String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);

                //进行OAuth响应构建
                OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);

                //设置授权码
                builder.setCode(authorizationCode);

                //得到到客户端重定向地址
                String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);

                //构建响应
                final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();

                logger.info("ResponseCode ,callback:{}", response.getLocationUri());
                String responseUri = response.getLocationUri();
                return "redirect:" + responseUri;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private boolean isNotEmptyClientId(String clientId) {
        return null != clientId && !"".equals(clientId);
    }


}
