package com.khpi.farmacy.mappers;

import com.khpi.farmacy.model.Manufacturer;
import com.khpi.farmacy.model.Medicine;
import com.khpi.farmacy.dtos.MedicineDto;
import com.khpi.farmacy.repositories.ManufacturerRepository;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicineMapper {

    @Setter(onMethod_ = @Autowired)
    private ManufacturerRepository manufacturerRepository;


    public Medicine map(MedicineDto medicineDto) {

        Medicine medicine = new Medicine();
        BeanUtils.copyProperties(medicineDto, medicine);

        Optional<Manufacturer> byCode = Optional.ofNullable
                (manufacturerRepository
                        .findByCode(medicineDto.getManufacturerCode()));

        if(byCode.isPresent()) {
            Manufacturer manufacturer = byCode.get();
            medicine.setManufacturer(manufacturer);

        }
        return medicine;
    }

    public MedicineDto map(Medicine medicine){
        MedicineDto medicineDto = new MedicineDto();
        BeanUtils.copyProperties(medicine, medicineDto);
        medicineDto.setManufacturerCode(medicine.getManufacturer().getCode());
        return medicineDto;
    }
}
