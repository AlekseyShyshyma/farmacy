package com.krego.farmacy.repositories;

import com.krego.farmacy.model.SoldInPeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoldInPeriodRepository extends JpaRepository<SoldInPeriod, Long> {

    Page<SoldInPeriod> findByDrugstoreDrugstoreCode(Long drugstoreCode, Pageable pageable);
    Page<SoldInPeriod> findByMedicineMedicineCode(Long medicineCode, Pageable pageable);

}
