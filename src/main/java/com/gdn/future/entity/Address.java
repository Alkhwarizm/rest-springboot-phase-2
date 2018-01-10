package com.gdn.future.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    @NotNull
    private String address;

    public Address() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
