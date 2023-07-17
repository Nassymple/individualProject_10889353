package com.example.inventorysystem;

import java.sql.Date;

public class productData {
    private Integer productId;
    private String type;
    private String brand;
    private String productName;
    private Double price;
    private String status;
    private String image;
    private Date date;


    //the constructor
    public productData(Integer productId, String type, String brand,String productName, Double price, String status, String image, Date date){
        this.productId = productId;
        this.type = type;
        this.brand = brand;
        this.productName = productName;
        this.price = price;
        this.status = status;
        this.image = image;
        this.date = date;
    }

    public Integer getProductId(){
        return productId;
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

    public Double getPrice(){
        return price;
    }

    public String getStatus(){
        return status;
    }

    public String getImage(){
        return image;
    }

    public Date getDate(){
        return date;
    }
}
