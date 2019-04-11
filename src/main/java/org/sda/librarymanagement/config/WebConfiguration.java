package org.sda.librarymanagement.config;

import javax.servlet.http.HttpServlet;

import org.h2.server.web.WebServlet;
import org.sda.librarymanagement.servlets.LoginServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfiguration {
	@Bean
	ServletRegistrationBean<WebServlet> h2servletRegistration() {
		ServletRegistrationBean<WebServlet> registrationBean = new ServletRegistrationBean<WebServlet>(
				new WebServlet());
		registrationBean.addUrlMappings("/console/*");
		return registrationBean;
	}

	@Bean
	public ServletRegistrationBean<HttpServlet> loginServlet() {
		ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
		servRegBean.setServlet(new LoginServlet());
		servRegBean.addUrlMappings("/login/*");
		return servRegBean;
	}
}