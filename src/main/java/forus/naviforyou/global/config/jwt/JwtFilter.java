package forus.naviforyou.global.config.jwt;

import forus.naviforyou.global.common.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;
    private final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String requestURI = httpServletRequest.getRequestURI();
        String jwt = resolveToken(httpServletRequest);



        boolean shouldAuthenticate = false;

        for (String pattern : Constants.AUTHENTICATED_URIS) {
            if (pathMatcher.match(pattern, requestURI)) {
                shouldAuthenticate = true;
                break;
            }
        }

        if (shouldAuthenticate) {
            log.info("jwt requestURI={}",requestURI);
            if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            }
        }else{
            log.info("not jwt requestURI={}",requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    // Request Header 에서 토큰 정보를 꺼내오기 위한 메소드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.JWT_AUTHORITIES_KEY);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;

    }


}