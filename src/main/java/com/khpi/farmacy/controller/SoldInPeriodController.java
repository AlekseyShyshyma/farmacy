package com.khpi.farmacy.controller;

import com.khpi.farmacy.dtos.SoldInPeriodDto;
import com.khpi.farmacy.exception.ResourceNotFoundException;
import com.khpi.farmacy.mappers.SoldInPeriodMapper;
import com.khpi.farmacy.model.Drugstore;
import com.khpi.farmacy.model.SoldInPeriod;
import com.khpi.farmacy.exception.BadRequestException;
import com.khpi.farmacy.model.Medicine;
import com.khpi.farmacy.repositories.DrugstoreRepository;
import com.khpi.farmacy.repositories.MedicineRepository;
import com.khpi.farmacy.repositories.SoldInPeriodRepository;
import com.khpi.farmacy.services.excel.exportation.ExportStrategyStorageService;
import com.khpi.farmacy.services.excel.importation.ParsingStrategyStorageService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sold")
public class SoldInPeriodController {
    
    @Setter(onMethod_ = @Autowired)
    SoldInPeriodRepository soldInPeriodRepository;

    @Setter(onMethod_ = @Autowired)
    DrugstoreRepository drugstoreRepository;

    @Setter(onMethod_ = @Autowired)
    MedicineRepository medicineRepository;

    @Setter(onMethod_ = @Autowired)
    private SoldInPeriodMapper soldInPeriodMapper;

    @Setter(onMethod_ = @Autowired)
    private ParsingStrategyStorageService parsingStrategyStorageService;

    @Setter(onMethod_ = @Autowired)
    private ExportStrategyStorageService exportStrategyStorageService;

    //GET mappings
    @GetMapping("/get")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public SoldInPeriod getSoldInPeriodById(@RequestParam("soldInPeriodCode") Long soldInPeriodId) {
        return soldInPeriodRepository.findById(soldInPeriodId)
                .orElseThrow(() -> new ResourceNotFoundException("SoldInPeriod", "id", soldInPeriodId));
    }

    @GetMapping("/manager")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Page<SoldInPeriod> getAllSoldInPeriods(@RequestParam("managerCode") Long managerCode, Pageable pageable) {

        return soldInPeriodRepository.findByDrugstore_ManagerManagerCode(managerCode, pageable);

    }

    @GetMapping("/all")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public List<SoldInPeriod> getAllSoldInPeriods() {
        return soldInPeriodRepository.findAll();
    }


    @PostMapping("/upload")
    public List<SoldInPeriod> uploadSoldInPeriod
            (@RequestParam("file") MultipartFile file) throws Exception{

        List<SoldInPeriod> soldInPeriods = parsingStrategyStorageService.parse(
                file.getInputStream(), SoldInPeriodDto.class)
                .stream()
                .map(soldInPeriodMapper::map)
                .collect(Collectors.toList());
        soldInPeriodRepository.saveAll(soldInPeriods);

        return soldInPeriods;
    }

    @PostMapping("/new")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public SoldInPeriod createSoldInPeriod(@RequestParam("drugstoreCode") Long drugstoreCode,
                                           @RequestParam("medicineCode") Long medicineCode,
                                           @Valid @RequestBody SoldInPeriod soldInPeriod) {
        boolean ifExists = soldInPeriodRepository.existsById(soldInPeriod.getSoldId());
        if(ifExists) {
            throw new BadRequestException("Sold with this code already exists");
        }

        Drugstore parentDrugstore = drugstoreRepository.findById(drugstoreCode)
                .orElseThrow(() -> new ResourceNotFoundException("Drugstore", "drugstoreCode", drugstoreCode));
        Medicine parentMedicine = medicineRepository.findById(medicineCode)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine", "medicineCode", medicineCode));
        soldInPeriod.setDrugstore(parentDrugstore);
        soldInPeriod.setMedicine(parentMedicine);

        System.out.println(soldInPeriod.getSoldId());

        return soldInPeriodRepository.save(soldInPeriod);
    }

    //PUT mappings
    @PutMapping("/update")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public SoldInPeriod updateSoldInPeriodById(@RequestParam("soldInPeriodCode") Long soldInPeriodId,
                                       @Valid @RequestBody SoldInPeriod soldInPeriodDetails) {
        SoldInPeriod soldInPeriod = soldInPeriodRepository.findById(soldInPeriodId)
                .orElseThrow(() -> new ResourceNotFoundException("SoldInPeriod", "id", soldInPeriodId));

        soldInPeriod.setAmount(soldInPeriodDetails.getAmount());
        soldInPeriod.setPeriodStart(soldInPeriodDetails.getPeriodStart());
        soldInPeriod.setPeriodEnd(soldInPeriodDetails.getPeriodEnd());
        soldInPeriod.setSum(soldInPeriodDetails.getSum());

        return soldInPeriodRepository.save(soldInPeriod);

    }

    @DeleteMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteSoldInPeriod(@RequestParam("soldInPeriodCode") Long soldInPeriodId) {

        SoldInPeriod soldInPeriod = soldInPeriodRepository.findById(soldInPeriodId)
                .orElseThrow(() -> new ResourceNotFoundException("SoldInPeriod", "id", soldInPeriodId));

        soldInPeriodRepository.delete(soldInPeriod);

        return ResponseEntity.ok().build();

    }

    @GetMapping("/export")
    @ResponseBody
    public byte[] exportExcel(HttpServletResponse httpServletResponse) throws IOException {
        List<SoldInPeriodDto> soldInPeriodDtos = soldInPeriodRepository.findAll().stream()
                .map(soldInPeriodMapper::map)
                .collect(Collectors.toList());
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=export.xlsx");
        return exportStrategyStorageService.parse(SoldInPeriodDto.class, soldInPeriodDtos);
    }
}
