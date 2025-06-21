package com.huawei.itoffer.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.DispatcherType;
/**
 * Servlet Filter implementation class ApplicantAuthorityFilter
 */
@WebFilter(
        urlPatterns = { "/applicant/*" }, 
        servletNames = { "com.huawei.itoffer.servlet.JobApplyServlet" }, 
        dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD })
public class ApplicantAuthorityFilter implements Filter {

    public void destroy() {
        
    }
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        // 判断被拦截的请求用户是否处于登录状态
        if (session.getAttribute("applicantID") == null) {
            // 获取被拦截的请求地址及参数
            String requestURI = req.getRequestURI();
            String queryString = req.getQueryString();
            String requestPath = requestURI;
            if (queryString != null && !queryString.isEmpty()) {
                requestPath += "?" + queryString;
            }
            // 将请求地址保存到request对象中转发到登录页面
            req.setAttribute("requestPath", requestPath);
            req.getRequestDispatcher("/login.jsp")
                    .forward(request, response);
            return;
        } else {
            chain.doFilter(request, response);
        }
    }
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
