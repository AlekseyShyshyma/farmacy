package com.krego.farmacy.mappers;

import com.krego.farmacy.model.Drugstore;
import com.krego.farmacy.model.Manager;
import com.krego.farmacy.repositories.ManagerRepository;
import com.krego.farmacy.services.criteria.ManagerCriteria;
import com.krego.farmacy.services.importing.dtos.DrugstoreDto;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DrugstoreMapper {

    @Setter(onMethod_ = @Autowired)
    private ManagerCriteria managerCriteria;

    @Setter(onMethod_ = @Autowired)
    private ManagerRepository managerRepository;

    public Drugstore map(DrugstoreDto drugstoreDto) {
        Drugstore drugstore = new Drugstore();
        BeanUtils.copyProperties(drugstoreDto, drugstore);

        Optional<Manager> byManagerCode = managerRepository.findById(drugstoreDto.getManagerCode());
        if (byManagerCode.isPresent()) {
            Manager manager = byManagerCode.get();
            manager.getDrugstores().add(drugstore);
            drugstore.setManager(manager);
        }
        return drugstore;
    }
}
