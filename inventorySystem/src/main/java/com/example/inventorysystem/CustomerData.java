package com.example.inventorysystem;

import java.util.Date;

public class CustomerData {

    private Integer customer_id;
    private String type;
    private String brand;
    private String productName;
    private Integer quantity;
    private Double price;
    private Date date;

    public CustomerData(Integer customer_id, String type, String brand, String productName, Integer quantity, Double price, Date date){
        this.customer_id = customer_id;
        this.type = type;
        this.brand = brand;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }

    public Integer getCustomer_id(){
        return customer_id;
    }
    public String getType(){
        return type;
    }
    public String getBrand(){
        return brand;
    }
    public String getProductName(){
        return productName;
    }
    public Integer getQuantity(){
        return quantity;
    }
    public Double getPrice(){
        return price;
    }
    public Date getDate(){
        return date;
    }

}
