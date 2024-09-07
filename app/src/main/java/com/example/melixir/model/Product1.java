package com.example.melixir.model;

import java.io.Serializable;

public class Product1 implements Serializable {
    private Integer ProductID;
    private String NamePro;
    private String DescriptionPro;
    private Integer CateID;
    private String Price;
    private String DiscountedPrice;
    private byte[] ImagePro;


    public Product1(Integer productID, String namePro, String descriptionPro, Integer cateID, String price,String discountedPrice,byte[] imagePro) {
        ProductID = productID;
        NamePro = namePro;
        DescriptionPro = descriptionPro;
        CateID = cateID;
        Price = price;
        DiscountedPrice = discountedPrice;
        ImagePro = imagePro;
    }

    public Integer getProductID() {
        return ProductID;
    }

    public void setProductID(Integer productID) {
        ProductID = productID;
    }

    public String getNamePro() {
        return NamePro;
    }

    public void setNamePro(String namePro) {
        NamePro = namePro;
    }

    public String getDescriptionPro() {
        return DescriptionPro;
    }

    public void setDescriptionPro(String descriptionPro) {
        DescriptionPro = descriptionPro;
    }

    public Integer getCateID() {
        return CateID;
    }

    public void setCateID(Integer cateID) {
        CateID = cateID;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
    public String getDiscountedPrice() {
        return DiscountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        DiscountedPrice = discountedPrice;
    }


    public byte[] getImagePro() {
        return ImagePro;
    }

    public void setImagePro(byte[] imagePro) {
        ImagePro = imagePro;
    }
    public double calculateDiscountedPrice(double originalPrice) {
        return originalPrice * 0.8; // Giảm giá 20%
    }


    public String toString(){
        return ProductID + "\t" + NamePro + "\t" + DescriptionPro + "\t" + CateID + "\t" + Price;
    }
}