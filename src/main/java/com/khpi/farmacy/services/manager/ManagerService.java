package com.khpi.farmacy.services.manager;

import com.khpi.farmacy.config.security.Roles;
import com.khpi.farmacy.exception.AppException;
import com.khpi.farmacy.exception.ResourceNotFoundException;
import com.khpi.farmacy.model.Manager;
import com.khpi.farmacy.model.Role;
import com.khpi.farmacy.repositories.ManagerRepository;
import com.khpi.farmacy.repositories.RoleRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class ManagerService {

    @Setter(onMethod_ = @Autowired)
    private ManagerRepository managerRepository;

    @Setter(onMethod_ = @Autowired)
    private PasswordEncoder passwordEncoder;

    @Setter(onMethod_ = @Autowired)
    private RoleRepository roleRepository;

    public Manager register(Manager manager) {
        if(managerRepository.existsById(manager.getManagerCode())) {
            throw new RuntimeException();
        }

        Manager newManager = new Manager.Builder().managerCode(manager.getManagerCode())
                .name(manager.getName())
                .surname(manager.getSurname())
                .patronymic(manager.getPatronymic())
                .address(manager.getAddress())
                .phoneNumber(manager.getPhoneNumber())
                .corporatePhoneNumber(manager.getCorporatePhoneNumber())
                .position(manager.getPosition())
                .build();

        newManager.setPassword(passwordEncoder.encode(manager.getPassword()));

        Role userRole = roleRepository.findByName(Roles.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        newManager.setRoles(Collections.singletonList(userRole));

        return managerRepository.save(newManager);
    }

    public Manager update(Long managerId, Manager managerDetails){
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager", "id", managerId));

        manager.setAddress(managerDetails.getAddress());
        manager.setCorporatePhoneNumber(managerDetails.getCorporatePhoneNumber());

        manager.setName(managerDetails.getName());
        manager.setPatronymic(managerDetails.getPatronymic());
        manager.setSurname(managerDetails.getSurname());
        manager.setPosition(managerDetails.getPosition());
        manager.setPhoneNumber(managerDetails.getPhoneNumber());

        return managerRepository.save(manager);
    }

    public Manager delete(Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager", "id", managerId));
        managerRepository.delete(manager);
        return manager;
    }
}
