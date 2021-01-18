package com.khpi.farmacy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "manager")
public class Manager {

    @Id
    @Column(name = "manager_code")
    private long managerCode;

    @NonNull
    private String name;
    private String password;
    private String surname;
    private String patronymic;
    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "corporate_phone_number")
    private String corporatePhoneNumber;
    private String position;

    @JsonIgnore
    @OneToMany(mappedBy = "manager")
    private Set<Drugstore> drugstores;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name="manager_code"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Manager(long managerCode, String name, String password, String surname, String patronymic,
                   String address, String phoneNumber, String corporatePhoneNumber, String position) {
        this.managerCode = managerCode;
        this.name = name;
        this.password = password;
        this.surname = surname;
        this.patronymic = patronymic;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.corporatePhoneNumber = corporatePhoneNumber;
        this.position = position;
    }
}
