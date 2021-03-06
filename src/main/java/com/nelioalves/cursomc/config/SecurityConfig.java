package com.nelioalves.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nelioalves.cursomc.security.JWTAuthenticationFilter;
import com.nelioalves.cursomc.security.JWTAuthorizationFilter;
import com.nelioalves.cursomc.security.JWTUtil;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] PUBLIC_MATHERS = { 
				"/h2-console/**"
	};
	
	
	private static final String[] PUBLIC_MATHERS_POST = { 
			"/clientes",
			"/auth/forgot/**"
};
	
	private static final String[] PUBLIC_MATHERS_GET = { 
			"/produtos/**", 
			"/categorias/**",
			"/estados/**"
	};
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UserDetailsService userDetailService;
	
	@Autowired
	private JWTUtil jwtUtil;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}

		http.cors().and().csrf().disable();
		
		http.authorizeRequests()
		        .antMatchers(HttpMethod.GET, PUBLIC_MATHERS_GET).permitAll()
		        .antMatchers(HttpMethod.POST , PUBLIC_MATHERS_POST).permitAll()
				.antMatchers(PUBLIC_MATHERS).permitAll()
				.anyRequest()
				.authenticated();
		
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil , userDetailService));

		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}
	
	
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(bCryptPasswordEncoder());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST","DELETE","PUT","GET","OPTIONS"));
		
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
		
	}

}
