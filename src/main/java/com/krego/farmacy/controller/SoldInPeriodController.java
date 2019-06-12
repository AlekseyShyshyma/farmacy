package com.krego.farmacy.controller;

import com.krego.farmacy.exception.BadRequestException;
import com.krego.farmacy.exception.ResourceNotFoundException;
import com.krego.farmacy.model.Drugstore;
import com.krego.farmacy.model.Medicine;
import com.krego.farmacy.model.SoldInPeriod;
import com.krego.farmacy.repositories.DrugstoreRepository;
import com.krego.farmacy.repositories.MedicineRepository;
import com.krego.farmacy.repositories.SoldInPeriodRepository;
import com.krego.farmacy.upload.FileStorageService;
import com.krego.farmacy.upload.UploadFileResponse;
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
@RequestMapping("/api/sold")
public class SoldInPeriodController {
    
    @Autowired
    SoldInPeriodRepository soldInPeriodRepository;

    @Autowired
    DrugstoreRepository drugstoreRepository;

    @Autowired
    MedicineRepository medicineRepository;

    @Autowired
    private FileStorageService fileStorageService;

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

    //POST mappings
    @PostMapping("/upload")
    //@PreAuthorize("hasRole('USER')")
    public List<SoldInPeriod> uploadSoldInPeriod(@RequestParam("file") MultipartFile file) throws Exception{

        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();


        ArrayList<SoldInPeriod> solds = parse(fileName);

        for(SoldInPeriod sold : solds) {
            soldInPeriodRepository.save(sold);
        }

        return solds;
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

    public ArrayList<SoldInPeriod> parse(String name) throws Exception{


        char[] periodStart = new char[10];
        char[] periodEnd = new char[10];

        for(int i = 0; i < name.length(); i++) {
            if(name.charAt(i) == '_') {
                name.getChars(++i, i+10, periodStart, 0);
                i+=11;
                name.getChars(i, i+10, periodEnd, 0);
                break;
            }
        }

        InputStream in = null;
        Workbook wb = null;
        try {
            in = new FileInputStream("uploads/" + name);
            wb = WorkbookFactory.create(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String startPeriod = new String(periodStart);
        String endPeriod = new String(periodEnd);

        System.out.println(startPeriod);
        System.out.println(endPeriod);

        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();

        ArrayList<SoldInPeriod> soldInPeriods = new ArrayList<>();

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = sdf1.parse(startPeriod);

        Date startDate = new Date(date.getTime());

        date = sdf1.parse(endPeriod);
        Date endDate = new Date(date.getTime());

        while (it.hasNext()) {
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();

            Cell cell = cells.next();
            Long soldId = (long) (cell.getNumericCellValue());

            cell = cells.next();
            Integer amount = (int) (cell.getNumericCellValue());

            cell = cells.next();
            Double sum = (double) (cell.getNumericCellValue());

            cell = cells.next();
            Long drugstoreCode = (long) (cell.getNumericCellValue());

            cell = cells.next();
            Long medicineCode = (long) (cell.getNumericCellValue());

            SoldInPeriod soldInPeriod = new SoldInPeriod(soldId, startDate, endDate, sum, amount);

            Drugstore parentDrugstore = drugstoreRepository.findById(drugstoreCode)
                    .orElseThrow(() -> new ResourceNotFoundException("Drugstore", "drugstoreCode", drugstoreCode));
            Medicine parentMedicine = medicineRepository.findById(medicineCode)
                    .orElseThrow(() -> new ResourceNotFoundException("Medicine", "medicineCode", medicineCode));
            soldInPeriod.setDrugstore(parentDrugstore);
            soldInPeriod.setMedicine(parentMedicine);

            soldInPeriods.add(soldInPeriod);
        }

        return soldInPeriods;
    }
}
