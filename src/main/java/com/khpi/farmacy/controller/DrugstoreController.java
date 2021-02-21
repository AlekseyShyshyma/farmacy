package com.khpi.farmacy.controller;

import com.khpi.farmacy.dtos.DrugstoreDto;
import com.khpi.farmacy.exception.ResourceNotFoundException;
import com.khpi.farmacy.mappers.DrugstoreMapper;
import com.khpi.farmacy.model.Drugstore;
import com.khpi.farmacy.exception.BadRequestException;
import com.khpi.farmacy.repositories.DrugstoreRepository;
import com.khpi.farmacy.repositories.ManagerRepository;
import com.khpi.farmacy.services.excel.exportation.ExportStrategyStorageService;
import com.khpi.farmacy.services.excel.importation.ParsingStrategyStorageService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/drugstore")
public class DrugstoreController {

    @Setter(onMethod_ = @Autowired)
    DrugstoreRepository drugstoreRepository;

    @Setter(onMethod_ = @Autowired)
    ManagerRepository managerRepository;

    @Setter(onMethod_ = @Autowired)
    private DrugstoreMapper drugstoreMapper;

    @Setter(onMethod_ = @Autowired)
    private ParsingStrategyStorageService parsingStrategyStorageService;

    @Setter(onMethod_ = @Autowired)
    private ExportStrategyStorageService exportStrategyStorageService;


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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteDrugstore(@RequestParam("drugstoreCode") Long drugstoreId) {

        Drugstore Drugstore = drugstoreRepository.findById(drugstoreId)
                .orElseThrow(() -> new ResourceNotFoundException("Drugstore", "id", drugstoreId));

        drugstoreRepository.delete(Drugstore);

        return ResponseEntity.ok().build();

    }

    @ResponseBody
    @PostMapping("/upload")
    public List<Drugstore> uploadDrugstores(@RequestParam("file") MultipartFile file) throws Exception {
        List<Drugstore> parsedDrugstores = parsingStrategyStorageService.parse(
                file.getInputStream(), DrugstoreDto.class)
                .stream()
                .map(drugstoreMapper::map)
                .collect(Collectors.toList());

        drugstoreRepository.saveAll(parsedDrugstores);
        return parsedDrugstores;
    }

    @GetMapping("/export")
    @ResponseBody
    public byte[] exportExcel(HttpServletResponse httpServletResponse) throws IOException {
        List<DrugstoreDto> drugstoreDtos = drugstoreRepository.findAll().stream()
                .map(drugstoreMapper::map)
                .collect(Collectors.toList());
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=export.xlsx");
        return exportStrategyStorageService.parse(DrugstoreDto.class, drugstoreDtos);
    }

}
