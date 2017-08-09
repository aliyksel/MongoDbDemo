package com.example.demo.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Customer implements Serializable{

	@org.springframework.data.annotation.Id
	private String Id;
	
	public List<Bill> getBills() {
		return bills;
	}

	private String firstName;
	private String lastName;
	private int totalSpend = 0;
	
	private List<Bill> bills;
 	
	public  Customer(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public void setId(String id) {
		Id = id;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void addBill(Bill bill) {
		if(this.bills == null) {
			this.bills = new ArrayList<>();
		}
		this.bills.add(bill);
		this.totalSpend = this.totalSpend+bill.getTotalSpend();
	}


	public Customer() {}


	public String getId() {
		return Id;
	}


	public String getFirstName() {
		return firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public int getTotalSpend() {
		return totalSpend;
	}
	

	
	
}
