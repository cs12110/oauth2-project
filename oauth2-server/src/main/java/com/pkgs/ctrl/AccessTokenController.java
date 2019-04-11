package com.pkgs.ctrl;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huanghuapeng create at 2019/4/10 19:18
 * @version 1.0.0
 */
@Controller
@RequestMapping("/oauth/server/")
public class AccessTokenController {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenController.class);

    /**
     * 获取客户端的code码，向客户端返回access token
     */
    @RequestMapping(value = "/responseAccessToken", method = RequestMethod.POST)
    public HttpEntity token(HttpServletRequest request) {
        logger.info("ResponseAccessToken");
        OAuthResponse response;
        OAuthIssuer oauthIssuerImpl;

        try {
            //构建OAuth请求
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

            // 进行用户权限校验,判读用户是否具有resourceUrl的访问权限
            String clientId = oauthRequest.getClientId();
            String clientSecret = oauthRequest.getClientSecret();
            String resourceUrl = oauthRequest.getParam("resourceUrl");

            boolean vip = isVip(clientId, clientSecret);

            logger.info("vip is:{} to visit:{}", vip, resourceUrl);
            if (vip) {
                //生成Access Token
                oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                final String accessToken = oauthIssuerImpl.accessToken();
                logger.info("AccessToken:{}", accessToken);
                //生成OAuth响应
                response = OAuthASResponse
                        .tokenResponse(HttpServletResponse.SC_OK)
                        .setAccessToken(accessToken + "haiyannnn")
                        .buildJSONMessage();
                //根据OAuthResponse生成ResponseEntity
                return new ResponseEntity<>(response.getBody(),
                        HttpStatus.valueOf(response.getResponseStatus()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isVip(String account, String password) {
        int num = (int) (Math.random() * 2);
        return num == 0;
    }
}
