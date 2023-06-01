package com.example.demo;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.configtool.Customer.Customer;
import com.configtool.Customer.LoginRequest;
import com.configtool.Customer.Role;
import com.configtool.CustomerRepo.CustomerRepository;
import com.configtool.CustomerRepo.RoleRepository;
import com.configtool.CustomerService.CustomerService;
import com.configtool.CustomerService.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
	
	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	
	@InjectMocks
	private CustomerServiceImpl customerService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	
	@Test
	public void testGetAllCustomers() {
		List<Customer> customerList = new ArrayList<>();
		Customer customer = new Customer(1L, "sneka","snekagovind@gmail.com","sneka");
		customerList.add(customer);
		when(customerRepository.findAll()).thenReturn(customerList);
		List<Customer> result = customerService.getAllCustomers();
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals("sneka",result.get(0).getUsername());
		
	}
	
	@Test
	public void findCustomerById () {
		Customer customer = new Customer(1L,"sneka","snekagovind@gmail.com","sneka");
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		Optional<Customer> result = customerService.findCustomerById(1L);
		Assertions.assertTrue(result.isPresent());//assert
		Assertions.assertEquals(customer, result.get());
		
		
	}
	
	@Test
	public void findCustomerById_NonExistingID() {
		//Customer customer = new Customer();
		Long customerId = 1L;
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());
		Optional<Customer> result = customerService.findCustomerById(1L);
		Assertions.assertFalse(result.isPresent());
		
	}
	
	@Test
	public void updateCustomer() {
		Customer customer = new Customer(1L,"sneka","snekagovind@gmail.com","sneka");
		when(customerRepository.save(customer)).thenReturn(customer);
		Customer result = customerService.updateCustomer(customer);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(customer, result);
		verify(customerRepository).save(customer);
	}
	
	@Test
	public void deleteCustomer() {
		Long customerId = 1L;
		customerService.deleteCustomer(customerId);
		verify(customerRepository).deleteById(customerId);
	}
	
	@Test
	public void login_validCredentials() {
		String username="sneka";
		String password = "sneka";
		String encodedPassword = "$2a$10$itPP2kmM1VUJlXxvUsCDWuykAzd02x14RJM1vby7NBh0F7xjbaQ5q";
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername(username);
		loginRequest.setPassword(password);
		
		Customer customer = new Customer();
		customer.setUsername(username);
		customer.setPassword(encodedPassword);
		when(customerRepository.findByUsername(username)).thenReturn(customer);
		when(passwordEncoder.matches(password,encodedPassword)).thenReturn(true);
		Customer result = customerService.login(loginRequest, request, response);
		Assertions.assertNotNull(result);
		verify(customerRepository).findByUsername(username);
		verify(passwordEncoder).matches(password, encodedPassword);
		
		
	}
	
	@Test
	public void signup_saveCustomer() {
		Customer customer = new Customer();
		customer.setUsername("sneka");
		customer.setPassword("sneka");
		String encodedPassword = "$2a$10$itPP2kmM1VUJlXxvUsCDWuykAzd02x14RJM1vby7NBh0F7xjbaQ5q";
		
		Role userRole = new Role();
		userRole.setName("user");
		when(roleRepository.findByName("user")).thenReturn(Optional.of(userRole));
		when(customerRepository.save(customer)).thenReturn(customer);
		
		Customer result = customerService.signup(customer);
		Assertions.assertNotNull(result);
		//Assertions.assertEquals(encodedPassword, result.getPassword());
		Assertions.assertTrue(result.getRoles().contains(userRole));
		verify(passwordEncoder).encode("sneka");
		verify(roleRepository).findByName("user");
		verify(customerRepository).save(customer);
	}


	
	
	
	
	

}
