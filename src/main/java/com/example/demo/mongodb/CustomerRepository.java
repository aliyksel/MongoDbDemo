package com.example.demo.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.models.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {

	@Query("{ '_id' : ?0 }")
	public Customer findById(String Id);
	
}
