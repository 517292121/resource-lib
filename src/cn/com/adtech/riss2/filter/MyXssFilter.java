package cn.com.adtech.riss2.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 非法字符过滤器，用来处理request.getParamater中的非法字符。如<script>alert('123');</script>
 * 
 * @author MaChao
 * 
 * @create 2018年06月07日
 */
public class MyXssFilter implements Filter{  
  
    @Override  
    public void init(FilterConfig filterConfig) throws ServletException {  
          
    }  
  
    @Override  
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)  
            throws IOException, ServletException {  
    	
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        /*
         * The old implementation assumed that the application was always
         * deployed under a non-root context (for example /riss2).  With an
         * executable Spring Boot WAR the context path is normally empty, so
         * indexOf("/", 2) can return -1 and cause StringIndexOutOfBoundsException
         * for requests such as / or /index.jsp.
         */
        String requestPath = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        if (contextPath != null && !contextPath.isEmpty()
                && requestPath.startsWith(contextPath)) {
            requestPath = requestPath.substring(contextPath.length());
        }
        if (requestPath.isEmpty()) {
            requestPath = "/";
        }

        // The data API consumes the original request, so keep it unwrapped.
        boolean dataApiRequest = "/dataapi".equals(requestPath)
                || requestPath.startsWith("/dataapi/");
        if (dataApiRequest) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(new XsslHttpServletRequestWrapper(httpRequest), response);
        }
    }  
      
    @Override  
    public void destroy() {  
          
    }  
      
}  
