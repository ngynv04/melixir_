package com.example.melixir.model;

public class OrderDetail {
    private int ID;
    private int IDProduct;
    private int IDOrder;
    private int Quantity;
    private double UnitPrice;

    public OrderDetail(int ID, int IDProduct, int IDOrder, int quantity, double unitPrice) {
        this.ID = ID;
        this.IDProduct = IDProduct;
        this.IDOrder = IDOrder;
        this.Quantity = quantity;
        this.UnitPrice = unitPrice;
    }

    // Getters and setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIDProduct() {
        return IDProduct;
    }

    public void setIDProduct(int IDProduct) {
        this.IDProduct = IDProduct;
    }

    public int getIDOrder() {
        return IDOrder;
    }

    public void setIDOrder(int IDOrder) {
        this.IDOrder = IDOrder;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        this.Quantity = quantity;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.UnitPrice = unitPrice;
    }
}

