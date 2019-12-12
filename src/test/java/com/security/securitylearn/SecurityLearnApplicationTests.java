package com.security.securitylearn;

import com.security.securitylearn.jwt.JwtTokenGenerator;
import com.security.securitylearn.jwt.JwtTokenPair;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.HashSet;


@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
class SecurityLearnApplicationTests {

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Test
    void jwtTest() {
        HashSet<String> roles = new HashSet<>();
        HashMap<String, String> additional = new HashMap<>();
        additional.put("username", "test111");
        JwtTokenPair tokenPair = jwtTokenGenerator.jwtTokenPair("qlx", roles, additional);
        System.out.println(tokenPair);
    }

}
