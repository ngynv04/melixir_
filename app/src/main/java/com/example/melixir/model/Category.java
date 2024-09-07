package com.example.melixir.model;

public class Category {

    private String NameCate;
    private Integer IdCate;

    public Category(String nameCate, Integer idCate) {
        NameCate = nameCate;
        IdCate = idCate;

    }

    public String getNameCate() {
        return NameCate;
    }

    public void setNameCate(String nameCate) {
        NameCate = nameCate;
    }

    public Integer getIdCate() {
        return IdCate;
    }

    public void setIdCate(Integer idCate) {
        IdCate = idCate;
    }
}
