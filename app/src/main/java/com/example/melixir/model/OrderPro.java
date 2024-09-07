package com.example.melixir.model;

import java.util.Date;

public class OrderPro {
    private int ID;
    private Date DateOrder;
    private int IDCus;
    private String AddressDelivery;

    public OrderPro(int ID, Date dateorder, int idcus, String adress) {
        this.ID = ID;
        this.DateOrder = dateorder;
        this.IDCus = idcus;
        this.AddressDelivery = adress;
    }

    // Getters and setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getDateorder() {
        return DateOrder;
    }

    public void setDateorder(Date dateorder) {
        this.DateOrder = dateorder;
    }

    public int getIdcus() {
        return IDCus;
    }

    public void setIdcus(int idcus) {
        this.IDCus = idcus;
    }

    public String getAdress() {
        return AddressDelivery;
    }

    public void setAdress(String adress) {
        this.AddressDelivery = adress;
    }
}
