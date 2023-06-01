package com.rahul.batchapp.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class EuropeanLeague {
    @Id
    private Long id;
    private String country;
    private String club;
}
