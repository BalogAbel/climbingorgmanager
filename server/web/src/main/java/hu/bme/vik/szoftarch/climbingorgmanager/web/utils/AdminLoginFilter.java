package hu.bme.vik.szoftarch.climbingorgmanager.web.utils;

import hu.bme.vik.szoftarch.climbingorgmanager.web.beans.AuthBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/app/admin/*")
public class AdminLoginFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		AuthBean authBean = (AuthBean) (request).getSession().getAttribute("authBean");

		if (authBean == null || authBean.getUser() == null || !authBean.getUser().isAdmin()) {
			response.sendRedirect(request.getContextPath() + "/login.xhtml");
		} else {
			filterChain.doFilter(servletRequest, servletResponse);
		}

	}

	@Override
	public void destroy() {

	}
}
