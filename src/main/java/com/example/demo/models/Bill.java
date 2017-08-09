package com.example.demo.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


public class Bill implements Serializable{

	private long billId;
	private String marketName;
	private Date date;
	private int totalSpend = 0;
	private List<Item> items = null;
	private List<Bill> content;
	
	public List<Bill> getContent() {
		return content;
	}
	public void setContent(List<Bill> content) {
		this.content = content;
	}
	public void setBillId(long billId) {
		this.billId = billId;
	}
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setTotalSpend(int totalSpend) {
		this.totalSpend = totalSpend;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}

	public long getBillId() {
		return billId;
	}
	public String getMarketName() {
		return marketName;
	}
	public Date getDate() {
		return date;
	}
	public int getTotalSpend() {
		return totalSpend;
	}
	public void addItem(Item item) {
		if(items == null) {
			items = new ArrayList<Item>();
		}
		items.add(item);
		totalSpend = totalSpend + item.getPrice();
	}
}
