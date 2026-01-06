package com.hypercube.evisa.approver.api.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hypercube.evisa.approver.api.util.EmployeeUserDetailsService;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Configuration
@EnableWebSecurity
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	/**
	 *
	 */
	@Autowired(required = true)
	EmployeeUserDetailsService myUserDetailsService;

	/**
	 *
	 */
	@Autowired(required = true)
	JwtRequestFilter jwtRequestFilter;

	/**
	 *
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
		log.info("loaded -=-=-=- AuthenticationManagerBuilder");
		authManagerBuilder.userDetailsService(myUserDetailsService);
	}

	/**
	 *
	 */
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		log.info("loaded -=-=-=- authenticationManagerBean");
		return super.authenticationManagerBean();
	}

	/**
	 *
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("loaded -=-=-=- http");
		disableCsrf(http.csrf());

		/*
		 * http.csrf().disable().authorizeRequests()
		 * .antMatchers("/v1/api/authenticaate/registeruser/")
		 * .permitAll().anyRequest().authenticated().and().exceptionHandling().and().
		 * sessionManagement() .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		 *
		 * http.addFilterBefore(jwtRequestFilter,
		 * UsernamePasswordAuthenticationFilter.class);
		 */
	}

	/**
	 *
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				// All of Spring Security will ignore the requests.
				// '/{[path:[^\\.]*}' is to avoid all the angular internal URLs
				.antMatchers("/").antMatchers("/web/**").antMatchers("/ping/**").antMatchers("/assets/**")
				.antMatchers("/images/**").antMatchers("/error/**").antMatchers("/resources/**")
				.antMatchers("/static/**").antMatchers("/favicon.ico").antMatchers("/**/*.html").antMatchers("/app/**")
				.antMatchers("/**/*.css").antMatchers("/**/*.js").antMatchers("/**");
	}

	/**
	 * @return
	 */
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		log.info("loaded -=-=-=- getPasswordEncoder");
		return new BCryptPasswordEncoder();
	}

	/**
	 * @param csrfConfigurer
	 */
	private void disableCsrf(CsrfConfigurer<HttpSecurity> csrfConfigurer) {
		csrfConfigurer.disable();
	}

}

