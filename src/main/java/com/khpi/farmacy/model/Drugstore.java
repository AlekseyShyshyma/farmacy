package com.khpi.farmacy.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "drugstore")
public class Drugstore {

    @Id
    @Column(name = "drugstore_code")
    private long drugstoreCode;

    @NonNull
    private String address;
    @Column(name = "network_title")
    private String networkTitle;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String region;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manager_code", nullable = false)
    //@JsonIgnore
    private Manager manager;

    @JsonIgnore
    @OneToMany(mappedBy = "drugstore")
    private Set<SoldInPeriod> soldInPeriods;

}
