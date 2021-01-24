package com.khpi.farmacy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
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
    private List<Medicine> medicines;

}
