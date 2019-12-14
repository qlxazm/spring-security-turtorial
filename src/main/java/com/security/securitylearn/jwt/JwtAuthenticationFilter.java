package com.security.securitylearn.jwt;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.security.securitylearn.entity.SysUser;
import com.security.securitylearn.security.SimpleAuthenticationEntryPoint;
import com.security.securitylearn.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 复原前端发送到后端的token
 * @author qian
 * @date 2019/12/13 23:00
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHENTICATION_PREFIX = "Bearer ";

    /**
     * 认证如果失败，由它处理
     */
    private AuthenticationEntryPoint authenticationEntryPoint = new SimpleAuthenticationEntryPoint();
    private JwtTokenGenerator jwtTokenGenerator;
    private JwtTokenStorage jwtTokenStorage;
    private SysUserService sysUserService;

    public JwtAuthenticationFilter(JwtTokenGenerator jwtTokenGenerator, JwtTokenStorage jwtTokenStorage, SysUserService service) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtTokenStorage = jwtTokenStorage;
        this.sysUserService = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // 如果认证已经通过
        SecurityContext context = SecurityContextHolder.getContext();
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(AUTHENTICATION_PREFIX)) {
            String jwtToken = header.replace(AUTHENTICATION_PREFIX, "");
            if (StringUtils.hasText(jwtToken)) {
                authenticationTokenHandler(jwtToken, request);
            } else {
                // 带安全头，没有带token
                authenticationEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException("未找到token"));
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * 验证token正确性
     * @param accessToken
     * @param request
     */
    private void authenticationTokenHandler(String accessToken, HttpServletRequest request) {
        // 解析accessToken
        JSONObject jsonObject = null;
        try {
            jsonObject = jwtTokenGenerator.decodeAndVerify(accessToken);

            if (Objects.nonNull(jsonObject)) {

                String username = jsonObject.getStr("aud");

                // 从redis中获取缓存的token
                JwtTokenPair jwtTokenPair = jwtTokenStorage.get(username);
                if (Objects.isNull(jwtTokenPair)){
                    if (log.isDebugEnabled()) {
                        log.debug("token : {} is not in cache", accessToken);
                    }
                    throw new CredentialsExpiredException("token is not in cache");
                }

                String cachedAccessToken = jwtTokenPair.getAccessToken();

                if (accessToken.equals(cachedAccessToken)) {

                    // toke验证通过，恢复上下文
                    JSONArray jsonArray = jsonObject.getJSONArray("roles");
                    List<String> roles = jsonArray.toList(String.class);
                    String[] rolesArr = roles.toArray(new String[roles.size()]);

                    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(rolesArr);
                    SysUser sysUser = sysUserService.getSysUserByUserName(username);
                    User user = new User(username, sysUser.getEncodePassword(), authorities);

                    // 创建用户认证token
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 恢复上下文
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }else {
                    // token不匹配
                    if (log.isDebugEnabled()) {
                        log.debug("token : {} is not matched", accessToken);
                    }
                    throw new BadCredentialsException("token is not matched");
                }

            } else {
                if (log.isDebugEnabled()) {
                    log.debug("token : {} is invalid", accessToken);
                }
                throw new BadCredentialsException("token is invalid");
            }

        } catch (JwtExpiredException e) {
            // jwt 过期啦
            e.printStackTrace();
        }


    }
}
