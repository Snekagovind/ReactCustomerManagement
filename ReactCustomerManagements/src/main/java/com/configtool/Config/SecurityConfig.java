package com.configtool.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.configtool.CustomerRepo.CustomerRepository;
import com.configtool.CustomerService.CustomerService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Override

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

	//in memory

	//auth.inMemoryAuthentication().withUser("kamatchi").password(passwordEncoder().encode("123")).authorities("user");
	//auth.inMemoryAuthentication().withUser("sneka").password(passwordEncoder().encode("sneka")).authorities("ROLE_ADMIN");
	
	auth.userDetailsService(userDetailsService());

	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/customers/auth/**").permitAll()
		.antMatchers("/customers/all").hasAnyAuthority("admin")
		.antMatchers("/customers/display/**").hasAuthority("user")
		.antMatchers("/customers/edit/**","/customers/delete/**").hasAuthority("admin")
		.anyRequest().authenticated();
		
		 
		 
		//http.formLogin();
		http.httpBasic();
	}
	
	
	
	
	
	
	
	
	
	
}
