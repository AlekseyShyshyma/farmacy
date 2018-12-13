package com.krego.farmacy.repositories;

import com.krego.farmacy.model.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    Page<Medicine> findByManufacturerCode(Long manufacturerCode, Pageable pageable);

//    @Query(value = "SELECT * FROM medicine med WHERE med.medicine_code" +
//            " IN(SELECT sold.medicine_code FROM sold_in_period sold WHERE sold.drugstore_code IN" +
//            "(SELECT drug.drugstore_code FROM drugstore drug WHERE drug.manager_code = ?managerCode))", nativeQuery = true)
//    Page<Medicine> findByManagerCode(Long managerCode, Pageable pageable);

}
