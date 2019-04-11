package com.pkgs.ctrl;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * https://blog.csdn.net/jing12062011/article/details/78147306
 *
 * @author huanghuapeng create at 2019/4/10 17:29
 * @version 1.0.0
 */
@Controller
@RequestMapping("/oauth/client/")
public class AuthClientController {

    private static final Logger logger = LoggerFactory.getLogger(AuthClientController.class);

    private static final String CLIENT_PREFIX_URL = "http://localhost:8081/oauth/client/";
    private static final String SERVER_PREFIX_URL = "http://localhost:8082/oauth/server/";
    private static final String RESOURCE_PREFIX_URL = "http://localhost:8083/oauth/resource/";


    /**
     * step1: 请求授权()
     *
     * @return String 重定向的url地址
     */
    @RequestMapping("/ask")
    public String ask() {
        logger.info("Step1: 请求授权");
        String requestUrl = null;
        try {
            String clientId = "clientId";
            String clientSecret = "clientSecret";
            String accessTokenUrl = "responseCode";
            String redirectUrl = CLIENT_PREFIX_URL + "/callbackCode";
            String responseType = "code";
            OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                    .authorizationLocation(accessTokenUrl)
                    .setResponseType(responseType)
                    .setClientId(clientId)
                    .setParameter("password", clientSecret)
                    .setRedirectURI(redirectUrl)
                    .buildQueryMessage();
            requestUrl = oAuthClientRequest.getLocationUri();
            logger.info("Request url:{}", requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:" + SERVER_PREFIX_URL + requestUrl;
    }

    @RequestMapping("/callbackCode")
    public Object callback(HttpServletRequest request) {
        logger.info("Step3: callback");

        String clientId = "clientId";
        String clientSecret = "clientSecret";
        String accessTokenUrl = SERVER_PREFIX_URL + "responseAccessToken";
        String redirectUrl = CLIENT_PREFIX_URL + "/accessToken";

        String code = request.getParameter("code");
        logger.info("code:{}", code);
        try {
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                    .tokenLocation(accessTokenUrl)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setCode(code)
                    .setRedirectURI(redirectUrl)
                    .buildQueryMessage();
            OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(oAuthClientRequest, OAuth.HttpMethod.POST);

            //获取服务端返回过来的access token
            String accessToken = oAuthResponse.getAccessToken();

            //查看access token是否过期
            Long expiresIn = oAuthResponse.getExpiresIn();
            logger.info("Client[callback] token:{}, expires:{} ", accessToken, expiresIn);

            return "redirect:" + CLIENT_PREFIX_URL + "/accessToken?accessToken=" + accessToken;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping("/accessToken")
    @ResponseBody
    public Object accessToken(String accessToken) {
        logger.info("Step5: 请求资源");
        String resourceUrl = RESOURCE_PREFIX_URL + "/product/list";
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        try {
            OAuthClientRequest resourceRequest = new OAuthBearerClientRequest(resourceUrl)
                    .setAccessToken(accessToken)
                    .buildQueryMessage();

            OAuthResourceResponse resourceResponse = oAuthClient.resource(
                    resourceRequest,
                    OAuth.HttpMethod.GET,
                    OAuthResourceResponse.class);
            String value = resourceResponse.getBody();
            logger.info("value:{}", value);

            return value;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}", e.getMessage());
        }
        return null;
    }


}
