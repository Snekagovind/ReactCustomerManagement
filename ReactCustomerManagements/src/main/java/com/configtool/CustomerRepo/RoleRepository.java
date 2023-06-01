package com.configtool.CustomerRepo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.configtool.Customer.Customer;
import com.configtool.Customer.Role;

public interface RoleRepository extends JpaRepository<Role,Long>{

	Optional<Role> findByName(String string);
	

}
