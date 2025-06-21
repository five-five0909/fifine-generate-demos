package com.huawei.itoffer.filter; // 包名请根据您的项目结构修改

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 可以从 web.xml 读取配置的编码，如果未配置则使用默认的 UTF-8
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null) {
            encoding = encodingParam;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 设置请求编码
        request.setCharacterEncoding(encoding);

        // 将请求传递给下一个过滤器或目标资源
        chain.doFilter(request, response);

        // 只对动态内容设置响应编码
        if (response.getContentType() != null && response.getContentType().startsWith("text/html")) {
            response.setContentType("text/html;charset=" + encoding);
        }
    }

    @Override
    public void destroy() {
        // 清理资源
    }
}