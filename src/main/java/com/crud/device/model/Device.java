package com.crud.device.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
public class Device {
    @Id
    @GeneratedValue
    Long id;
    String name;
    String brand;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date creationTime;

    public Device() {
    }

    public Device(String name, String brand) {
        this.name = name;
        this.brand = brand;
    }

    public Device(Long id,String name, String brand, Date creationTime) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.creationTime = creationTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Date getCreationTime() {
        return creationTime;
    }

}
