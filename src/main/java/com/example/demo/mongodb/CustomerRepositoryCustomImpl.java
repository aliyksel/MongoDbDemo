package com.example.demo.mongodb;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.bind;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Customer;
import com.example.demo.models.Item;
import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;


@Repository
public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {

	 @Autowired
	 protected MongoTemplate mongoTemplate;
	 
	 class ResultItem {
		 List<Integer> totalSpend;
		 List<Integer> billSpend;
		public List<Integer> getTotalSpend() {
			return totalSpend;
		}
		
		public int getTotalSpendLiteral() {
			return totalSpend.get(0).intValue();
		}
		
		public void setTotalSpend(List<Integer> totalSpend) {
			this.totalSpend = totalSpend;
		}
		public List<Integer> getBillSpend() {
			return billSpend;
		}
		public int getBillSpendLiterel() {
			return billSpend.get(0).intValue();
		}
		public void setBillSpend(List<Integer> billSpend) {
			this.billSpend = billSpend;
		}
		
		public Item getItem() {
			return item.get(0);
		}

		public void setItem(List<Item> item) {
			this.item = item;
		}

		List<Item> item;
		
	 }
	 
	 
	 
	 private ResultItem getBillSpend(String customerId, long billId, long barcode){
		 
		 List<AggregationOperation> operations = new ArrayList<AggregationOperation>();
		 
		 operations.add(match(Criteria.where("_id").is(customerId)));
		 operations.add(	unwind("bills")) ;
		 operations.add(	match(Criteria.where("bills.billId").is(billId)));
		 
		 GroupOperation group = group().addToSet("totalSpend").as("totalSpend").addToSet("bills.totalSpend").as("billSpend");
		 
		 if(barcode > 0) {
			 operations.add(unwind("bills.items"));
			 operations.add(match(Criteria.where("bills.items.barcode").is(barcode)));
			 group = group.addToSet("bills.items").as("item");
		 }
		 operations.add(group);
		 
		 Aggregation aggregation = newAggregation(operations);
		 
		 AggregationResults<ResultItem> item = mongoTemplate.aggregate(aggregation, Customer.class, ResultItem.class);
		 
		 return item.getUniqueMappedResult();
		 
		 
	 }
	@Override
	public boolean removeBillFromCustomer(String customerId, long billId) {
		
		ResultItem resultItem = getBillSpend(customerId, billId,0);
		
		Update updateObj = new Update()
		         .pull("bills", new BasicDBObject("billId",billId)).inc("totalSpend", -1 * resultItem.getBillSpendLiterel());
				
		WriteResult result = mongoTemplate.updateMulti(query(where("_id").is(customerId).and("totalSpend").is(resultItem.getTotalSpendLiteral())), updateObj, Customer.class);
		return result.isUpdateOfExisting();

	}

	@Override
	public boolean removeItemFromCustomer(String customerId, long billId, long barcode) {
		ResultItem foundItem = getBillSpend(customerId, billId, barcode);
		
		Update updateObj = new Update()
		         .pull("bills.$.items", new BasicDBObject("barcode",barcode)).inc("bills.$.totalSpend", -1 * foundItem.getItem().getPrice()).inc("totalSpend", -1 * foundItem.getItem().getPrice());
				
		WriteResult result = mongoTemplate.updateMulti(query(where("_id").is(customerId).andOperator(where("bills.billId").is(billId).
				and("totalSpend").is(foundItem.getTotalSpendLiteral()))), updateObj, Customer.class);
		return result.isUpdateOfExisting();

	}
	
	

	@Override
	public boolean updatePriceToItem(String customerId, long billId, long barcode, int price) throws Exception {

		ResultItem foundItem = getBillSpend(customerId, billId, barcode);
		
		int oldPrice = foundItem.getItem().getPrice();
		foundItem.getItem().setPrice(price);

		Update updateObj = new Update()
				.push("bills.$.items", foundItem.getItem()).inc("bills.$.totalSpend", price - oldPrice).inc("totalSpend", price-oldPrice);
		mongoTemplate.updateMulti(query(where("_id").is(customerId).and("totalSpend").is(foundItem.getTotalSpendLiteral()).and("bills").elemMatch(where("billId").is(billId).and("totalSpend").is(foundItem.getBillSpendLiterel()))),updateObj,Customer.class);
				
		updateObj = new Update()
		         .pull("bills.$.items", query(where("barcode").is(barcode).and("price").is(oldPrice)));
		mongoTemplate.updateMulti(query(where("_id").is(customerId).and("bills.billId").is(billId)),updateObj,Customer.class);         
		         

		
		return true;
	}
	
	

}