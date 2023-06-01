package com.configtool.CustomerService;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.configtool.Customer.Customer;
import com.configtool.Customer.LoginRequest;

@Service
public interface CustomerService {

	Customer signup(Customer customer);

	Optional<Customer> findCustomerById(Long id);

	Customer updateCustomer(Customer customer);
	
      void deleteCustomer(Long id);

	Customer login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);

	List<Customer> getAllCustomers();

	

   


	




	

}
