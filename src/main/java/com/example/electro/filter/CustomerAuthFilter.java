package com.example.electro.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "customerAuthFilter")
public class CustomerAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = ((HttpServletRequest) request);
        HttpSession httpSession = httpRequest.getSession(false);
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpSession != null && httpSession.getAttribute("customer-id") != null) {
                chain.doFilter(request, response);
                return;
        }
        else {
            httpResponse.sendRedirect("/ecommerce/web/auth/login.jsp");
            return;
        }
    }

}
