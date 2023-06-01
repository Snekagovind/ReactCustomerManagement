package com.configtool.CustomerService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.configtool.Customer.Customer;
import com.configtool.Customer.LoginRequest;
import com.configtool.Customer.Role;
import com.configtool.CustomerRepo.CustomerRepository;
import com.configtool.CustomerRepo.RoleRepository;


@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	
	@Override
	public Customer signup(Customer customer) {
		
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("user").get();
        roles.add(userRole);
        customer.setRoles(roles);
        return customerRepository.save(customer);
    }
	
	
	@Override
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	@Override
	public Optional<Customer> findCustomerById(Long id) {
		return customerRepository.findById(id);
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public void deleteCustomer(Long id) {
		 customerRepository.deleteById(id);
	}

	@Override
	public Customer login(LoginRequest loginRequest,HttpServletRequest request,HttpServletResponse response) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		Customer customer=customerRepository.findByUsername(username);
		if(passwordEncoder.matches(password, customer.getPassword())) {
			String token=UUID.randomUUID().toString();
			Cookie cookie=new Cookie("remember-me",token);
			cookie.setMaxAge(7*24*60*60);
			cookie.setPath("/");
			response.addCookie(cookie);
			customer.setToken(token);
			customerRepository.save(customer);
			return customer;
		}
		return null;
	}
	

	
}

	

	
