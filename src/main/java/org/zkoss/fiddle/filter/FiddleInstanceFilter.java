package org.zkoss.fiddle.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.zkoss.fiddle.instance.InstanceManager;
import org.zkoss.fiddle.model.Instance;

public class FiddleInstanceFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest httprequest = ((HttpServletRequest) request);

		Instance in = new Instance();
		in.setName(httprequest.getParameter("name"));
		in.setPath(httprequest.getParameter("path"));
		in.setVersion(httprequest.getParameter("ver"));

		try {
			InstanceManager im = InstanceManager.getInstance();
			im.addInstance(in);
			response.getWriter().println("true");
		} catch (Exception e) {
			response.getWriter().println("false");
		}
		response.getWriter().close();

	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}
}
