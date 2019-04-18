package com.rohanch.bmonoddsscraper.config;

import com.rohanch.bmonoddsscraper.auth.AuthEntryPoint;
import com.rohanch.bmonoddsscraper.auth.AuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthEntryPoint authEntryPoint;

	@Autowired
	private AuthSuccessHandler authSuccessHandler;

	//TODO: implement custom failure handler so the proper cors header is set
	private SimpleUrlAuthenticationFailureHandler authFailureHandler = new SimpleUrlAuthenticationFailureHandler();

	@Override
	protected void configure(HttpSecurity http) throws Exception {//
		http
				.cors()
				.and()
				.exceptionHandling()
				.authenticationEntryPoint(authEntryPoint)
				.and()
				.formLogin()
				.successHandler(authSuccessHandler)
				.failureHandler(authFailureHandler)
				.and()
				.authorizeRequests()
				.antMatchers("/*")
				.permitAll()
				.and()
				.authorizeRequests()
				.antMatchers("/user/**", "/bet/**")
				.authenticated()
				.and()
				.csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) {
		builder.authenticationProvider(authenticationProvider());
	}

	private DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		return authProvider;
	}
}
//TODO: fix these before prod