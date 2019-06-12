package com.krego.farmacy.controller;

import com.krego.farmacy.exception.BadRequestException;
import com.krego.farmacy.exception.ResourceNotFoundException;
import com.krego.farmacy.model.Drugstore;
import com.krego.farmacy.model.Manager;
import com.krego.farmacy.model.Manufacturer;
import com.krego.farmacy.model.Medicine;
import com.krego.farmacy.repositories.DrugstoreRepository;
import com.krego.farmacy.repositories.ManagerRepository;
import com.krego.farmacy.repositories.ManufacturerRepository;
import com.krego.farmacy.repositories.MedicineRepository;
import com.krego.farmacy.upload.FileStorageService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/medicine")
public class MedicineController {
    
    @Autowired
    MedicineRepository medicineRepository;

    @Autowired
    ManufacturerRepository manufacturerRepository;

    @Autowired
    DrugstoreRepository drugstoreRepository;

    @Autowired
    private FileStorageService fileStorageService;

    //GET mappings
    @GetMapping("/get")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Medicine getMedicineById(@RequestParam("medicineCode") Long medicineId) {
        return medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine", "id", medicineId));
    }

    @GetMapping("/manager")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Page<Medicine> getMedicineByManagerCode(@RequestParam("managerCode") Long managerCode, Pageable pageable) {
        return medicineRepository.findByManagerCode(managerCode, pageable);
    }

    @GetMapping("/all")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    //POST mappings
    @PostMapping("/new")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Medicine createMedicine(@RequestParam("manufacturerCode") Long manufacturerCode,
                                   @Valid @RequestBody Medicine medicine) {
        boolean ifExists = medicineRepository.existsById(medicine.getMedicineCode());
        if(ifExists) {
            throw new BadRequestException("Medicine with this code already exists");
        }
        return manufacturerRepository.findById(manufacturerCode).map(manufacturer -> {
            medicine.setManufacturer(manufacturer);
            return medicineRepository.save(medicine);
        }).orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "manufacturerCode", manufacturerCode));
    }

    //PUT mappings
    @PutMapping("/update")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Medicine updateMedicineById(@RequestParam("medicineCode") Long medicineId,
                                               @Valid @RequestBody Medicine medicineDetails) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine", "id", medicineId));

        medicine.setTitle(medicineDetails.getTitle());
        medicine.setExpirationTerm(medicineDetails.getExpirationTerm());
        medicine.setMeasurementUnit(medicineDetails.getMeasurementUnit());
        medicine.setPrice(medicineDetails.getPrice());

        return medicineRepository.save(medicine);

    }

    @DeleteMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteMedicine(@RequestParam("medicineCode") Long medicineId) {

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine", "id", medicineId));

        medicineRepository.delete(medicine);

        return ResponseEntity.ok().build();

    }

    @PostMapping("/upload")
    public List<Medicine> uploadMedicines(@RequestParam("file") MultipartFile file) throws Exception{

        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();


        ArrayList<Medicine> medicines = parse(fileName);

        for(Medicine sold : medicines) {
            medicineRepository.save(sold);
        }

        return medicines;
    }

    public ArrayList<Medicine> parse(String name) throws Exception{

        InputStream in = null;
        Workbook wb = null;
        try {
            in = new FileInputStream("uploads/" + name);
            wb = WorkbookFactory.create(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();

        ArrayList<Medicine> medicines = new ArrayList<>();

        while (it.hasNext()) {
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();

            Cell cell = cells.next();
            Long medicineCode = (long) (cell.getNumericCellValue());

            cell = cells.next();
            Double price = cell.getNumericCellValue();

            cell = cells.next();
            String title = cell.getStringCellValue();

            cell = cells.next();
            String term = cell.getStringCellValue();

            cell = cells.next();
            String measurmentUnit = cell.getStringCellValue();

            cell = cells.next();
            Long manufacturerCode = (long) cell.getNumericCellValue();

            Manufacturer manufacturer = manufacturerRepository.findById(manufacturerCode)
                    .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "manufacturerCode", manufacturerCode));

            Medicine medicine = new Medicine();
            medicine.setMedicineCode(medicineCode);
            medicine.setPrice(price);
            medicine.setTitle(title);
            medicine.setExpirationTerm(term);
            medicine.setMeasurementUnit(measurmentUnit);
            medicine.setManufacturer(manufacturer);

            medicines.add(medicine);
        }

        return medicines;
    }
}
