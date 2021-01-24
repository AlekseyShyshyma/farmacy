package com.khpi.farmacy.controller;

import com.khpi.farmacy.dtos.MedicineDto;
import com.khpi.farmacy.exception.BadRequestException;
import com.khpi.farmacy.exception.ResourceNotFoundException;
import com.khpi.farmacy.mappers.MedicineMapper;
import com.khpi.farmacy.model.Medicine;
import com.khpi.farmacy.repositories.ManufacturerRepository;
import com.khpi.farmacy.repositories.MedicineRepository;
import com.khpi.farmacy.services.excel.importation.ParsingStrategyStorageService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/medicine")
public class MedicineController {

    @Setter(onMethod_ = @Autowired)
    private MedicineRepository medicineRepository;

    @Setter(onMethod_ = @Autowired)
    private MedicineMapper medicineMapper;

    @Setter(onMethod_ = @Autowired)
    private ManufacturerRepository manufacturerRepository;

    @Setter(onMethod_ = @Autowired)
    private ParsingStrategyStorageService parsingStrategyStorageService;


    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Medicine>> getAllMedicines() {
        return new ResponseEntity<>(medicineRepository.findAll(),
                HttpStatus.OK);
    }


    @GetMapping("/get")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Medicine> getMedicineById(@RequestParam("medicineCode") Long medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine",
                        "id", medicineId));

        return new ResponseEntity<>(medicine, HttpStatus.OK);
    }


    @GetMapping("/manager")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<Medicine>> getMedicineByManagerCode(@RequestParam("managerCode") Long managerCode,
                                                   Pageable pageable) {

        return new ResponseEntity<>(medicineRepository.findByManagerCode(
                managerCode, pageable),
                HttpStatus.OK);
    }


    @PostMapping("/new")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Medicine> createMedicine(@RequestParam("manufacturerCode") Long manufacturerCode,
                                   @Valid @RequestBody Medicine medicine) {

        boolean ifExists = medicineRepository.existsById(medicine.getMedicineCode());
        if (ifExists) {
            throw new BadRequestException("Medicine with this code already exists");
        }

        Medicine medicine1 = manufacturerRepository.findById(manufacturerCode).map(manufacturer -> {
            medicine.setManufacturer(manufacturer);
            return medicineRepository.save(medicine);
        }).orElseThrow(() -> new ResourceNotFoundException("Manufacturer",
                "manufacturerCode", manufacturerCode));

        return new ResponseEntity<>(medicine1, HttpStatus.OK);
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Medicine> updateMedicineById(@RequestParam("medicineCode") Long medicineId,
                                                       @Valid @RequestBody Medicine medicineDetails) {

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine", "id", medicineId));

        medicine.setTitle(medicineDetails.getTitle());
        medicine.setExpirationTerm(medicineDetails.getExpirationTerm());
        medicine.setMeasurementUnit(medicineDetails.getMeasurementUnit());
        medicine.setPrice(medicineDetails.getPrice());

        medicineRepository.save(medicine);
        return new ResponseEntity<>(medicine, HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteMedicine(@RequestParam("medicineCode") Long medicineId) {

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine", "id", medicineId));
        medicineRepository.delete(medicine);

        return ResponseEntity.ok().build();

    }


    @PostMapping("/upload")
    public ResponseEntity<List<Medicine>> uploadMedicines(
            @RequestParam("file") MultipartFile file) throws Exception {

        List<Medicine> medicines = parsingStrategyStorageService.parse(
                file.getInputStream(), MedicineDto.class)
                .stream()
                .map(medicineMapper::map)
                .collect(Collectors.toList());
        medicineRepository.saveAll(medicines);

        return new ResponseEntity<>(medicines, HttpStatus.OK);
    }

}
