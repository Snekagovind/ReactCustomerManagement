package com.configtool.CustomerRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.configtool.Customer.Customer;
import com.configtool.Customer.LoginRequest;


@Repository

public interface CustomerRepository extends JpaRepository<Customer, Long>{

	Customer findByUsername(String username);

	

	

}
