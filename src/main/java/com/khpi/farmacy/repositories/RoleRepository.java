package com.khpi.farmacy.repositories;

import com.khpi.farmacy.helpers.RoleName;
import com.khpi.farmacy.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
    Role findAllByName(RoleName roleName);
}