package com.example.melixir.model;

import java.io.Serializable;

public class Product implements Serializable {
    private Integer ProductID;
    private String NamePro;
    private String DescriptionPro;
    private String Price;
    private byte[] ImagePro; // Thay đổi kiểu dữ liệu của trường hình ảnh

    public Product() {
    }

    public Product(Integer productID, String namePro, String descriptionPro, String price, byte[] imagePro) {
        ProductID = productID;
        NamePro = namePro;
        DescriptionPro = descriptionPro;
        Price = price;
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public byte[] getImagePro() {
        return ImagePro;
    }

    public void setImagePro(byte[] imagePro) {
        ImagePro = imagePro;
    }

    @Override
    public String toString() {
        return ProductID+"\t"+ NamePro+"\t"+DescriptionPro+"\t"+Price+"\t"+ ImagePro;
    }
}