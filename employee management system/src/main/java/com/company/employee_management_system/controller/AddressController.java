package com.company.employee_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.employee_management_system.dto.AddressDto;
import com.company.employee_management_system.service.Addressservice;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private Addressservice addressService;

    // SAVE
    @PostMapping
    public ResponseEntity<AddressDto> saveAddress(
            @Valid @RequestBody AddressDto dto) {

        AddressDto address = addressService.saveAddress(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(address);
    }

    // FIND BY ID
    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> findById(
            @PathVariable int id) {

        AddressDto address = addressService.findById(id);

        return ResponseEntity.ok(address);
    }

    // FIND ALL
    @GetMapping
    public ResponseEntity<List<AddressDto>> findAll() {

        List<AddressDto> addresses = addressService.findAll();

        return ResponseEntity.ok(addresses);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(
            @Valid @RequestBody AddressDto dto,
            @PathVariable int id) {

        AddressDto address = addressService.updateAddress(dto, id);

        return ResponseEntity.ok(address);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(
            @PathVariable int id) {

        addressService.deleteAddress(id);

        return ResponseEntity.ok("Address deleted successfully");
    }
}