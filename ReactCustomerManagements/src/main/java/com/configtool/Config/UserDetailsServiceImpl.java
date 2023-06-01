package com.configtool.Config;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.configtool.Customer.Customer;

import com.configtool.CustomerRepo.CustomerRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private CustomerRepository customerRepository;

	
		@Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        Customer customer = customerRepository.findByUsername(username);
	        if (customer == null) {
	            throw new UsernameNotFoundException("Invalid username or password");
	        }
	        Set<GrantedAuthority> authorities = customer
	        		.getRoles()
	        		.stream() 
	        		.map((role) -> new SimpleGrantedAuthority(role.getName()))
	        		.collect(Collectors.toSet());
	        return new MyUserDetails(customer);//org.springframework.security.core.userdetails.User(customer.getUsername(),customer.getPassword(),authorities);
	        }

	        
			
	    

		

}
