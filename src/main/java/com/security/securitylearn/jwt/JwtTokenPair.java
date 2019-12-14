package com.security.securitylearn.jwt;

import lombok.Data;

import java.io.Serializable;

/**
 * Token对
 * @author qian
 * @date 2019/12/12 14:44
 */
@Data
public class JwtTokenPair implements Serializable {

    private String accessToken;

    private String refreshToken;
}
