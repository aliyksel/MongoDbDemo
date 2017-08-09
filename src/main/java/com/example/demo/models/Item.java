package com.example.demo.models;

import java.io.Serializable;
import java.math.BigDecimal;


public class Item implements Serializable{

	

	

	public void setBarcode(Long barcode) {
		this.barcode = barcode;
	}

	private long barcode;
	private String name;
	private int  price;
	
	
	public Item(long barcode, String name, int price) {
		super();
		this.barcode = barcode;
		this.name = name;
		this.price = price;
	}
	public long getBarcode() {
		return barcode;
	}
	public String getName() {
		return name;
	}
	public void setBarcode(long barcode) {
		this.barcode = barcode;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getPrice() {
		return price;
	}
	
	@Override
	public String toString() {
		return "barcode: "+ barcode+ " , name: " + name+ " , price: " +price;
	}
	
}
