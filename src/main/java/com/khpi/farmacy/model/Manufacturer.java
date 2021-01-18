package com.khpi.farmacy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "manufacturer")
public class Manufacturer {

    @Id
    @Column(name = "manufacturer_code")
    private long code;

    @NonNull
    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "firm_title")
    private String firmTitle;

    @JsonIgnore
    @OneToMany(mappedBy = "manufacturer", orphanRemoval = true)
    private Set<Medicine> medicines;

}
