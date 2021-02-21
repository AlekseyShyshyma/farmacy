package com.khpi.farmacy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private List<Drugstore> drugstores;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name="manager_code"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

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

    public static class Builder {

        private Manager manager;

        public Builder() {
            manager = new Manager();
        }
        public Builder managerCode(long managerCode) {
            manager.managerCode = managerCode;
            return this;
        }

        public Builder name(String name) {
            manager.name = name;
            return this;
        }

        public Builder password(String password) {
            manager.password = password;
            return this;
        }

        public Builder surname(String surname) {
            manager.surname = surname;
            return this;
        }

        public Builder patronymic(String patronymic) {
            manager.patronymic = patronymic;
            return this;
        }

        public Builder address(String address) {
            manager.address = address;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            manager.phoneNumber = phoneNumber;
            return this;
        }

        public Builder corporatePhoneNumber(String corporatePhoneNumber) {
            manager.corporatePhoneNumber = corporatePhoneNumber;
            return this;
        }

        public Builder position(String position) {
            manager.position = position;
            return this;
        }

        public Manager build() {
            return manager;
        }
    }
}
