package com.shuttle.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/users")
public class CorsFilter extends HttpFilter implements Filter {
	
	private static final long serialVersionUID = 1L;

	public CorsFilter() {
        super();
    }

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		String method = req.getMethod();
		String origin = req.getHeader("Origin");
		
		System.out.println("요청 메서드: " + method);
		System.out.println("요청URL: "+origin);
		if(origin != null && origin.equals("https://pilot-prassign.vercel.app"))
			res.setHeader("Access-Control-Allow-Origin", origin);
		res.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		res.setHeader("Access-Control-Allow-Headers", "Content-Type");
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
