package com.pkgs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * OauthServerController: 授权
 * <p>
 * AccessTokenController: 校权
 *
 * @author huanghuapeng create at 2019/4/10 17:24
 * @version 1.0.0
 */
@SpringBootApplication
public class AuthServerApp {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApp.class, args);
    }
}
