package com.khpi.farmacy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medicine")
public class Medicine {

    @Id
    @Column(name = "medicine_code")
    private long medicineCode;

    @NonNull
    private String title;
    @Column(name = "expiration_term")
    private String expirationTerm;
    private double price;
    @Column(name = "measurement_unit")
    private String measurementUnit;

    //@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manufacturer_code", nullable = false)
    private Manufacturer manufacturer;

    @JsonIgnore
    @OneToMany(mappedBy = "medicine")
    @Nullable
    private Set<SoldInPeriod> soldInPeriods;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sold_in_period",
            joinColumns = @JoinColumn(name="medicine_code"),
            inverseJoinColumns = @JoinColumn(name = "drugstore_code"))
    private Set<Medicine> drugstores = new HashSet<>();

}
