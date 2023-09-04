package com.ecommerce.controller;

import com.ecommerce.dto.AppUserDto;
import com.ecommerce.dto.VendorDto;
import com.ecommerce.payload.request.CreateAppUserRequest;
import com.ecommerce.payload.request.CreateVendorRequest;
import com.ecommerce.payload.request.UpdateAppUserRequest;
import com.ecommerce.payload.request.UpdateVendorRequest;
import com.ecommerce.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/vendors")
public class VendorController {
    private final VendorService vendorService;
    @GetMapping
    public ResponseEntity<List<VendorDto>> getAllVendors() {
        List<VendorDto> vendors = vendorService.getAllVendors();
        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<VendorDto> getVendorById(@PathVariable("id") Long vendorId) {
        VendorDto vendor = vendorService.getVendorById(vendorId);
        return new ResponseEntity(vendor, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> createVendor(@RequestBody CreateVendorRequest createRequest) {
        Long savedVendorId = vendorService.createVendor(createRequest);
        return new ResponseEntity<>(savedVendorId, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateVendor(@PathVariable("id") Long vendorId,
                                        @RequestBody UpdateVendorRequest updateRequest) {
        vendorService.updateVendor(vendorId, updateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable("id") Long vendorId) {
        vendorService.deleteVendor(vendorId);
        return new ResponseEntity<>("Vendor successfully deleted.", HttpStatus.OK);
    }
}
