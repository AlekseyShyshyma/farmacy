package com.krego.farmacy.controller;

import com.krego.farmacy.exception.BadRequestException;
import com.krego.farmacy.exception.ResourceNotFoundException;
import com.krego.farmacy.model.Manufacturer;
import com.krego.farmacy.repositories.ManufacturerRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/manufacturer")
public class ManufacturerController {

    @Setter(onMethod_ = @Autowired)
    ManufacturerRepository manufacturerRepository;

    //GET mappings
    @GetMapping("/get")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Manufacturer getManufacturerById(@RequestParam("manufacturerCode") Long manufacturerId) {
        return manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", manufacturerId));
    }

    @GetMapping("/all")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    //POST mappings
    @PostMapping("/new")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Manufacturer createManufacturer(@Valid @RequestBody Manufacturer manufacturer) {
        boolean ifExists = manufacturerRepository.existsById(manufacturer.getCode());
        if(ifExists) {
            throw new BadRequestException("Manufacturer with this code already exists");
        }
        return manufacturerRepository.save(manufacturer);
    }

    //PUT mappings
    @PutMapping("/update")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteManufacturer(@RequestParam("manufacturerCode") Long manufacturerId) {

        Manufacturer manufacturer = manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", manufacturerId));

        manufacturerRepository.delete(manufacturer);

        return ResponseEntity.ok().build();

    }

}
