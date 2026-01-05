package com.itmk.config.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Classname CustomerAuthenionException
 * @Description 自定义异常
 * @Date 2025/3/19 9:54
 * @Created by Oliver
 */
public class CustomerAuthenionException extends AuthenticationException {
    public CustomerAuthenionException(String msg) {
        super(msg);
    }
}
