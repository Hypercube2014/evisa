package com.hypercube.evisa.applicant.api.configuration;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hypercube.evisa.applicant.api.util.ApplicantUserDetailsService;
import com.hypercube.evisa.applicant.api.model.RateLimiterFilter;

/**
 * @author sivasreenivas
 *
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringSecurityConfiguration.class);

    /**
     * 
     */
    @Autowired(required = true)
    ApplicantUserDetailsService myUserDetailsService;

    /**
     * 
     */
    @Autowired(required = true)
    JwtRequestFilter jwtRequestFilter;

    // DÉSACTIVÉ TEMPORAIREMENT : RateLimiter commenté
    // @Autowired(required = true)
    //RateLimiterFilter rateLimiterFilter;
    /**
     *
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
        LOGGER.info("loaded -=-=-=- AuthenticationManagerBuilder");
        authManagerBuilder.userDetailsService(myUserDetailsService);
    }

    /**
     *
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        LOGGER.info("loaded -=-=-=- authenticationManagerBean");
        return super.authenticationManagerBean();
    }

    /**
     *
     */
    // @Override
    // protected void configure(HttpSecurity http) throws Exception {

    //     LOGGER.info("loaded -=-=-=- http");
    //     disableCsrf(http.csrf());
        
    //     http
    //     .cors()
    //     .and()
    //     .headers(headers -> headers
    //     .contentSecurityPolicy("default-src 'self'; script-src 'self'; style-src 'self';")
    //     .and()
    //     .addHeaderWriter((request, response) -> {
    //         response.setHeader("Permissions-Policy", "geolocation=(), camera=(), microphone=()");
    //     })
    //     .addHeaderWriter((request, response) -> {
    //         response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
    //     })
    //     .addHeaderWriter((request, response) -> {
    //         response.setHeader("X-Content-Type-Options", "nosniff");
    //     })
    //     .addHeaderWriter((request, response) -> {
    //         response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
    //     })
    //     .addHeaderWriter(new ContentSecurityPolicyHeaderWriter()));
        
    //       http.csrf().disable().authorizeRequests()
    //       .antMatchers("/v1/api/authenticaate/registeruser").permitAll()
    //       .antMatchers("/validateuser").permitAll()     
    //       .antMatchers("/v1/api/applicant/getFaqdetails").permitAll()      
    //       .anyRequest().authenticated().and().exceptionHandling().and().
    //       sessionManagement()
    //       .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
          
    //       http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    // }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.info("Loaded SecurityFilterChain");

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeRequests(auth -> auth
                .antMatchers("/v1/api/authenticaate/registeruser", "/validateuser", "/verify-account/**", "/v1/api/applicant/forgetpassword/**", "/api/stripe/webhook","/api/stripe/webhook/**").permitAll()
                .antMatchers("/v1/api/applicant/getFaqdetails","/v1/api/contactus").permitAll()
                // Swagger URLs autorisées
                .antMatchers("/swagger-ui/**", "/swagger-ui.html", "/v2/api-docs", "/swagger-resources/**", "/webjars/**").permitAll() //desactiver en prod
                // .antMatchers("/v1/api/payment/checkout").permitAll()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers
                .contentSecurityPolicy("default-src 'self'; script-src 'self'; style-src 'self';")
                .and()
                .addHeaderWriter((request, response) -> response.setHeader("Permissions-Policy", "geolocation=(), camera=(), microphone=()"))
                .addHeaderWriter((request, response) -> response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin"))
                .addHeaderWriter((request, response) -> response.setHeader("X-Frame-Options", "SAMEORIGIN"))
                .addHeaderWriter((request, response) -> response.setHeader("X-Content-Type-Options", "nosniff"))
                .addHeaderWriter((request, response) -> response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload"))
            )
            // DÉSACTIVÉ TEMPORAIREMENT : RateLimiter bloque les requêtes
            // .addFilterBefore(rateLimiterFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter
    }
    /**
    *
    */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                // All of Spring Security will ignore the requests.
                // '/{[path:[^\\.]*}' is to avoid all the angualr internal urls
                .antMatchers("/").antMatchers("/web/**").antMatchers("/ping/**").antMatchers("/assets/**")
                .antMatchers("/images/**").antMatchers("/error/**").antMatchers("/resources/**")
                .antMatchers("/static/**").antMatchers("/favicon.ico").antMatchers("/**/*.html").antMatchers("/app/**")
                .antMatchers("/**/*.css").antMatchers("/**/*.js").antMatchers("/**/*.js.map").antMatchers("/**/*.ttf").antMatchers("/**/*.woff");
    }

    // @Bean
    // public WebMvcConfigurer corsConfigurer() {
    //     return new WebMvcConfigurer() {
    //         @Override
    //         public void addCorsMappings(CorsRegistry registry) {
    //             registry.addMapping("/**")
    //                     .allowedOrigins("http://localhost:4200")
    //                     .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    //                     .allowedHeaders("*")
    //                     .allowCredentials(true);
    //         }
    //     };
    // }


    /**
     * @return
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        LOGGER.info("loaded -=-=-=- getPasswordEncoder");
        return new BCryptPasswordEncoder();
    }

    /**
     * @param csrfConfigurer
     */
    private void disableCsrf(CsrfConfigurer<HttpSecurity> csrfConfigurer) {
        csrfConfigurer.disable();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "https://evisav2.gouv.dj", "https://www.evisa.gouv.dj", "http://www.evisa.gouv.dj"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // Enable cookies (if needed)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
