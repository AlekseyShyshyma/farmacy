package com.krego.farmacy.controller;

import com.krego.farmacy.exception.ResourceNotFoundException;
import com.krego.farmacy.model.Drugstore;
import com.krego.farmacy.repositories.DrugstoreRepository;
import com.krego.farmacy.repositories.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/drugstore")
public class DrugstoreController {

    @Autowired
    DrugstoreRepository drugstoreRepository;

    @Autowired
    ManagerRepository managerRepository;

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

}