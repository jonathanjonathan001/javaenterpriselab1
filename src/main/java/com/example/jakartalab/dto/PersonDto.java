package com.example.jakartalab.dto;

public class PersonDto {

    private Long id;

    private String name;

    private String cellNumber;

    public PersonDto(Long id, String name, String cellNumber) {
        this.id = id;
        this.name = name;
        this.cellNumber = cellNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }
}
