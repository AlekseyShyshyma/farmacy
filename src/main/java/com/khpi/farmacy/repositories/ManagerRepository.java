package com.khpi.farmacy.repositories;

import com.khpi.farmacy.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByManagerCode(Long code);

    Manager findAllByManagerCode(Long code);
}
