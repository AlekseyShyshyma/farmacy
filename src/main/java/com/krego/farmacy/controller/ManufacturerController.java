package com.krego.farmacy.controller;

import com.krego.farmacy.exception.ResourceNotFoundException;
import com.krego.farmacy.model.Manufacturer;
import com.krego.farmacy.repositories.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/manufacturer")
public class ManufacturerController {

    @Autowired
    ManufacturerRepository manufacturerRepository;

    //GET mappings
    @GetMapping("/get")
    @ResponseBody
    public Manufacturer getManufacturerById(@RequestParam("manufacturerCode") Long manufacturerId) {
        return manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", manufacturerId));
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    //POST mappings
    @PostMapping("/new")
    @ResponseBody
    public Manufacturer createManufacturer(@Valid @RequestBody Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    //PUT mappings
    @PutMapping("/update")
    @ResponseBody
    public Manufacturer updateManufacturerById(@RequestParam("manufacturerCode") Long manufacturerId,
                                         @Valid @RequestBody Manufacturer manufacturerDetails) {
        Manufacturer manufacturer = manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", manufacturerId));

        manufacturer.setAddress(manufacturerDetails.getAddress());

        manufacturer.setFirmTitle(manufacturerDetails.getFirmTitle());
        manufacturer.setPhoneNumber(manufacturerDetails.getPhoneNumber());

        return manufacturerRepository.save(manufacturer);

    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteManufacturer(@RequestParam("manufacturerCode") Long manufacturerId) {

        Manufacturer manufacturer = manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", manufacturerId));

        manufacturerRepository.delete(manufacturer);

        return ResponseEntity.ok().build();

    }

}
