package com.krego.farmacy.controller;

import com.krego.farmacy.exception.BadRequestException;
import com.krego.farmacy.exception.ResourceNotFoundException;
import com.krego.farmacy.model.Drugstore;
import com.krego.farmacy.model.Manager;
import com.krego.farmacy.model.Medicine;
import com.krego.farmacy.model.SoldInPeriod;
import com.krego.farmacy.repositories.DrugstoreRepository;
import com.krego.farmacy.repositories.ManagerRepository;
import com.krego.farmacy.upload.FileStorageService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/drugstore")
public class DrugstoreController {

    @Autowired
    DrugstoreRepository drugstoreRepository;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    private FileStorageService fileStorageService;

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
        if(ifExists) {
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
    public List<Drugstore> uploadDrugstores(@RequestParam("file") MultipartFile file) throws Exception{

        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();


        ArrayList<Drugstore> drugstores = parse(fileName);

        for(Drugstore sold : drugstores) {
            drugstoreRepository.save(sold);
        }

        return drugstores;
    }

    public ArrayList<Drugstore> parse(String name) throws Exception{

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

        ArrayList<Drugstore> drugstores = new ArrayList<>();

        while (it.hasNext()) {
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();

            Cell cell = cells.next();
            Long drugstoreCode = (long) (cell.getNumericCellValue());

            cell = cells.next();
            String address = cell.getStringCellValue();

            cell = cells.next();
            String networkTitle = cell.getStringCellValue();

            cell = cells.next();
            String phoneNumber = cell.getStringCellValue();

            cell = cells.next();
            String region = cell.getStringCellValue();

            cell = cells.next();
            Long managerCode = (long) cell.getNumericCellValue();

            Manager manager = managerRepository.findByManagerCode(managerCode)
                    .orElseThrow(() -> new ResourceNotFoundException("Manager", "managerCode", managerCode));

            Drugstore newDrugstore = new Drugstore();
            newDrugstore.setDrugstoreCode(drugstoreCode);
            newDrugstore.setAddress(address);
            newDrugstore.setNetworkTitle(networkTitle);
            newDrugstore.setPhoneNumber(phoneNumber);
            newDrugstore.setRegion(region);
            newDrugstore.setManager(manager);

            drugstores.add(newDrugstore);
        }

        return drugstores;
    }

}
