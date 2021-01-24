package com.khpi.farmacy.mappers;

import com.khpi.farmacy.model.Drugstore;
import com.khpi.farmacy.model.Manager;
import com.khpi.farmacy.repositories.ManagerRepository;
import com.khpi.farmacy.dtos.DrugstoreDto;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DrugstoreMapper {

    @Setter(onMethod_ = @Autowired)
    private ManagerRepository managerRepository;


    public Drugstore map(DrugstoreDto drugstoreDto) {

        Drugstore drugstore = new Drugstore();
        BeanUtils.copyProperties(drugstoreDto, drugstore);

        Optional<Manager> byManagerCode = managerRepository
                .findById(drugstoreDto.getManagerCode());

        if (byManagerCode.isPresent()) {
            Manager manager = byManagerCode.get();
            drugstore.setManager(manager);
        }
        return drugstore;
    }
}
