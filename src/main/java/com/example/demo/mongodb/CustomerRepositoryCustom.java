package com.example.demo.mongodb;

import java.math.BigDecimal;

public interface CustomerRepositoryCustom {
	
	public boolean removeBillFromCustomer(String customerId, long billId);
	
	public boolean removeItemFromCustomer(String customerId, long billId, long barcode);
	
	public boolean updatePriceToItem(String customerId, long billId, long barcode, int price) throws Exception;
	
}
