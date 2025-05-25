package br.com.ufpr.tads.dac.msauth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        System.out.println(">>> [Request] URI: " + httpServletRequest.getRequestURI());
        System.out.println(">>> [Request] Method: " + httpServletRequest.getMethod());
        System.out.println(">>> [Request] Content-Type: " + httpServletRequest.getContentType());

        chain.doFilter(request, response);
    }
}
