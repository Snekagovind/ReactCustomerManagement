package com.configtool.CustomerController;




import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.configtool.Customer.Customer;
import com.configtool.Customer.LoginRequest;
import com.configtool.CustomerService.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	

	@GetMapping("/all")
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = customerService.getAllCustomers();
		return ResponseEntity.ok(customers);

	}

	@PostMapping("auth/signup")
	public ResponseEntity<Customer> signup(@RequestBody Customer customer){

		Customer createdCustomer = customerService.signup(customer);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
	}

	@GetMapping("/display/{id}")//auth
	public ResponseEntity<Optional<Customer>> findCustomerById(@PathVariable Long id) {
		Optional<Customer> customer = customerService.findCustomerById(id);
		if (customer != null) {
			return ResponseEntity.ok(customer);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/edit/{id}")//auth
	public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {

		Optional<Customer> existingCustomer = customerService.findCustomerById(id);
		if (existingCustomer != null) {
			customer.setId(id);
			Customer updatedCustomer = customerService.updateCustomer(customer);
			return ResponseEntity.ok(updatedCustomer);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/delete/{id}")//auth
	public ResponseEntity<Customer> deleteCustomer(@PathVariable Long id) {

		Optional<Customer> existingCustomer = customerService.findCustomerById(id);
		if (existingCustomer != null) {
			customerService.deleteCustomer(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("auth/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,HttpServletRequest request,HttpServletResponse response){
		Customer customer = customerService.login(loginRequest,request,response);
		if(customer != null) {
			return ResponseEntity.ok(customer);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}


}
