package com.arrested.lbmmo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

	@Autowired
	private DataSource dataSource;
	
	private static final String USER_BY_USERNAME_QUERY = 
			"select username,password,enabled from user_account where username = ?";
	private static final String AUTHORITY_BY_USERNAME_QUERY =
			"select username, role from user_account u, user_role ur where u.id = ur.user_id and u.username = ?";	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery(USER_BY_USERNAME_QUERY)
			.authoritiesByUsernameQuery(AUTHORITY_BY_USERNAME_QUERY)
			.passwordEncoder(new BCryptPasswordEncoder());
	}
		
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// Not auth required for options so that ajax works
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/**").permitAll();

		// Account creation doesn't require auth
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/service/account-creation/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/service/account-creation/**").permitAll();
		
		// Services require auth
		http.authorizeRequests().antMatchers(HttpMethod.GET,"/service/**").hasAuthority("USER").and().httpBasic();
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/service/**").hasAuthority("USER").and().httpBasic();

		http.csrf().disable();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
