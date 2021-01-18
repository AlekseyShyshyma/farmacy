package com.khpi.farmacy.helpers;

import com.khpi.farmacy.model.Manager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private long managerCode;
    private String name;
    private String password;
    private String surname;
    private String patronymic;
    private String address;
    private String phoneNumber;
    private String corporatePhoneNumber;
    private String position;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String name, String password, String surname, String patronymic, String address,
                String phoneNumber, String corporatePhoneNumber, String position, Collection<? extends GrantedAuthority> authorities) {
        this.managerCode = id;
        this.name = name;
        this.surname = surname;
        this.position = position;
        this.password = password;
        this.authorities = authorities;
        this.patronymic = patronymic;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.corporatePhoneNumber = corporatePhoneNumber;

    }

    public static UserPrincipal create(Manager user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new UserPrincipal(
                user.getManagerCode(),
                user.getName(),
                user.getPassword(),
                user.getSurname(),
                user.getPatronymic(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getCorporatePhoneNumber(),
                user.getPosition(),
                authorities
        );
    }

    @Override
    public String getUsername() {
        return Long.toString(managerCode);
    }

    public long getManagerCode() {
        return managerCode;
    }

    public void setManagerCode(long managerCode) {
        this.managerCode = managerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCorporatePhoneNumber() {
        return corporatePhoneNumber;
    }

    public void setCorporatePhoneNumber(String corporatePhoneNumber) {
        this.corporatePhoneNumber = corporatePhoneNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(managerCode, that.managerCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(managerCode);
    }

}
