package com.khpi.farmacy.mappers;

import com.khpi.farmacy.model.Drugstore;
import com.khpi.farmacy.model.Medicine;
import com.khpi.farmacy.model.SoldInPeriod;
import com.khpi.farmacy.repositories.DrugstoreRepository;
import com.khpi.farmacy.repositories.MedicineRepository;
import com.khpi.farmacy.dtos.SoldInPeriodDto;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SoldInPeriodMapper {

    @Setter(onMethod_ = @Autowired)
    private DrugstoreRepository drugstoreRepository;

    @Setter(onMethod_ = @Autowired)
    private MedicineRepository medicineRepository;

    public SoldInPeriod map(SoldInPeriodDto soldInPeriodDto) {

        SoldInPeriod soldInPeriod = new SoldInPeriod();
        BeanUtils.copyProperties(soldInPeriodDto, soldInPeriod);

        Optional<Drugstore> byDrugstoreCode = drugstoreRepository
                .findById(soldInPeriodDto.getDrugstoreCode());

        Optional<Medicine> byMedicineCode = medicineRepository
                .findById(soldInPeriodDto.getMedicineCode());

        if (byDrugstoreCode.isPresent()) {
            Drugstore drugstore = byDrugstoreCode.get();
            soldInPeriod.setDrugstore(drugstore);
        }

        if (byMedicineCode.isPresent()) {
            Medicine medicine = byMedicineCode.get();
            soldInPeriod.setMedicine(medicine);
        }

        return soldInPeriod;
    }
}
