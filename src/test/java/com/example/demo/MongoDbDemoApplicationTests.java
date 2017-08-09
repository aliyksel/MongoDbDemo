package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.models.Bill;
import com.example.demo.models.Customer;
import com.example.demo.models.Item;
import com.example.demo.mongodb.CustomerRepository;
import com.example.demo.mongodb.CustomerRepositoryCustomImpl;



@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {SpringMongoConfiguration.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MongoDbDemoApplicationTests {

	@Autowired
    CustomerRepository customerRepo;
	
	@Autowired
	CustomerRepositoryCustomImpl customerRepositoryCustomImpl;
	
	String customerId = "5983954bbb13061a88ca2925";
	
	@Test
	public void test1() throws Exception{
		
		Customer cust1= new Customer("Alice", "Smith");
		cust1.setId(customerId);
		Customer cust2 = new Customer("Bob", "Smith");
		
		Bill bill = new Bill();
		bill.setBillId(1);
		bill.addItem(new Item(111,"apple",80));
		bill.addItem(new Item(112,"pice",50));
		bill.addItem(new Item(113,"bread",120));
		cust1.addBill(bill);
		
		//customerService.addBilltoCustomer(bill);
		
		Customer cc = customerRepo.save(cust1);
		customerId = cc.getId();

		
		customerRepo.save(cust2);
		
		int count = customerRepo.findAll().size();
		Customer customer = customerRepo.findById(customerId);
		assertEquals(2, count);
		assertEquals(250, customer.getTotalSpend());
		assertEquals(250, customer.getBills().get(0).getTotalSpend());
		
		
		
	}
	
	@Test
	public void test2() throws Exception{
		System.out.println(" test 2 : " + customerId);
		Customer cust1= customerRepo.findById(customerId);
		
		
		Bill bill = new Bill();
		bill.setBillId(2);
		bill.addItem(new Item(111,"apple 2",80));
		bill.addItem(new Item(112,"pice 2",50));
		bill.addItem(new Item(113,"bread 2",120));
		cust1.addBill(bill);
		
		//customerService.addBilltoCustomer(bill);
		
		Customer cc = customerRepo.save(cust1);
		
		assertEquals(500, cc.getTotalSpend());
		assertEquals(2, cc.getBills().size());
		
	}


	@Test
	public void test3() {
		boolean result = customerRepositoryCustomImpl.removeBillFromCustomer(customerId, 1);
		assertTrue("Update yapılamadı", result);
		Customer customer = customerRepo.findById(customerId);
		assertEquals(250, customer.getTotalSpend());
		
	}

	
	@Test
	public void test4() {
		boolean result = customerRepositoryCustomImpl.removeItemFromCustomer(customerId, 2, 111);
		assertTrue("Update error", result);
		Customer customer = customerRepo.findById(customerId);
		assertEquals(170, customer.getTotalSpend());
	}
	
	@Test
	public void test5() throws Exception{
		boolean result = customerRepositoryCustomImpl.updatePriceToItem(customerId, 2, 112,350);
		assertTrue("Update error", result);
		Customer customer = customerRepo.findById(customerId);
		assertEquals(470, customer.getTotalSpend());
	}
	
}
