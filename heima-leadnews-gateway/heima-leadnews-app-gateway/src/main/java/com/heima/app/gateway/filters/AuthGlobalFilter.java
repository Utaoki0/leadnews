package com.heima.app.gateway.filters;


import cn.hutool.core.text.AntPathMatcher;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.heima.common.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取请求头
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getPath().toString();
        System.out.println("url = " + url);
        if (url.contains("/login")) {
            //登录接口直接放行
            return chain.filter(exchange);
        }
        //2.获取token
        String token = null;
        List<String> headers = request.getHeaders().get("token");
        if (headers != null && !headers.isEmpty()) {
            token = headers.get(0);
        }
        ServerHttpResponse response = exchange.getResponse();
        if (StringUtils.isBlank(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //3.解析token
        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            int i = AppJwtUtil.verifyToken(claimsBody);
            if (i == 1 || i == 2) {
                //过期
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            String userId = claimsBody.get("id") + "";
            //4.传递信息
            ServerWebExchange swe = exchange.mutate()
                    .request(builder -> builder.header("userId", userId)
                    ).build();
            return chain.filter(swe);
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
