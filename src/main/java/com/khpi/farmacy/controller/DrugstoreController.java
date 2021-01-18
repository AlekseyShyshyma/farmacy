package com.khpi.farmacy.controller;

import com.khpi.farmacy.exception.ResourceNotFoundException;
import com.khpi.farmacy.mappers.DrugstoreMapper;
import com.khpi.farmacy.model.Drugstore;
import com.khpi.farmacy.services.excel.importation.parsers.AbstractParser;
import com.khpi.farmacy.exception.BadRequestException;
import com.khpi.farmacy.repositories.DrugstoreRepository;
import com.khpi.farmacy.repositories.ManagerRepository;
import com.khpi.farmacy.services.excel.importation.dtos.DrugstoreDto;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/drugstore")
public class DrugstoreController {

    @Setter(onMethod_ = @Autowired)
    DrugstoreRepository drugstoreRepository;

    @Setter(onMethod_ = @Autowired)
    ManagerRepository managerRepository;

    @Setter(onMethod_ = @Autowired)
    private DrugstoreMapper drugstoreMapper;

    @Setter(onMethod_ = {@Autowired, @Qualifier("drugstoreParser")})
    private AbstractParser<DrugstoreDto> parser;

    //GET mappings
    @GetMapping("/get")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Drugstore getDrugstoreById(@RequestParam("drugstoreCode") Long drugstoreCode) {
        return drugstoreRepository.findById(drugstoreCode)
                .orElseThrow(() -> new ResourceNotFoundException("Drugstore", "id", drugstoreCode));
    }

    @GetMapping("/all")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public List<Drugstore> getAllDrugstores() {
        return drugstoreRepository.findAll();
    }

    @GetMapping("/manager")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Page<Drugstore> getAllDrugstoresByManager(@RequestParam("managerCode") Long managerCode, Pageable pageable) {

        return drugstoreRepository.findByManagerManagerCode(managerCode, pageable);

    }

    //POST mappings
    @PostMapping("/new")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Drugstore createDrugstore(@RequestParam("managerCode") Long managerCode, @Valid @RequestBody Drugstore drugstore) {

        boolean ifExists = drugstoreRepository.existsById(drugstore.getDrugstoreCode());
        if (ifExists) {
            throw new BadRequestException("Drugstore with this code already exists");
        }

        return managerRepository.findById(managerCode).map(manager -> {
            drugstore.setManager(manager);
            return drugstoreRepository.save(drugstore);
        }).orElseThrow(() -> new ResourceNotFoundException("ManagerId", "manager_code", managerCode));

    }

    //PUT mappings
    @PutMapping("/update")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Drugstore updateDrugstoreById(@RequestParam("drugstoreCode") Long drugstoreId,
                                         @Valid @RequestBody Drugstore drugstoreDetails) {
        Drugstore Drugstore = drugstoreRepository.findById(drugstoreId)
                .orElseThrow(() -> new ResourceNotFoundException("Drugstore", "id", drugstoreId));

        Drugstore.setAddress(drugstoreDetails.getAddress());

        Drugstore.setNetworkTitle(drugstoreDetails.getNetworkTitle());
        Drugstore.setPhoneNumber(drugstoreDetails.getPhoneNumber());
        Drugstore.setRegion(drugstoreDetails.getRegion());

        return drugstoreRepository.save(Drugstore);

    }

    @DeleteMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteDrugstore(@RequestParam("drugstoreCode") Long drugstoreId) {

        Drugstore Drugstore = drugstoreRepository.findById(drugstoreId)
                .orElseThrow(() -> new ResourceNotFoundException("Drugstore", "id", drugstoreId));

        drugstoreRepository.delete(Drugstore);

        return ResponseEntity.ok().build();

    }

    @PostMapping("/upload")
    public List<Drugstore> uploadDrugstores(@RequestParam("file") MultipartFile file) throws Exception {
        Optional<List<DrugstoreDto>> parsedDrugstores = parser.parse(file.getInputStream());
        if (parsedDrugstores.isPresent()) {
            List<Drugstore> drugstores = parsedDrugstores.get().stream()
                    .map(drugstoreMapper::map)
                    .collect(Collectors.toList());
            drugstoreRepository.saveAll(drugstores);

            return drugstores;
        }
        return Collections.emptyList();
    }

}
