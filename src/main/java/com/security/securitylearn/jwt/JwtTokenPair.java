package com.security.securitylearn.jwt;

import lombok.Data;

/**
 * Token对
 * @author qian
 * @date 2019/12/12 14:44
 */
@Data
public class JwtTokenPair {

    private String accessToken;

    private String refreshToken;
}
