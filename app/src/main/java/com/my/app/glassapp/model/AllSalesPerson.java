package com.my.app.glassapp.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AllSalesPerson{

	@SerializedName("AllSalesPerson")
	private List<AllSalesPersonItem> allSalesPerson;

	public void setAllSalesPerson(List<AllSalesPersonItem> allSalesPerson){
		this.allSalesPerson = allSalesPerson;
	}

	public List<AllSalesPersonItem> getAllSalesPerson(){
		return allSalesPerson;
	}
}