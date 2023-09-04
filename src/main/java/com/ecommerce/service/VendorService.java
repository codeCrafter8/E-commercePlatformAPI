package com.ecommerce.service;

import com.ecommerce.dto.AppUserDto;
import com.ecommerce.dto.VendorDto;
import com.ecommerce.exception.DuplicateResourceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.AppUserMapper;
import com.ecommerce.mapper.VendorMapper;
import com.ecommerce.model.AppUser;
import com.ecommerce.model.Company;
import com.ecommerce.model.Vendor;
import com.ecommerce.payload.request.CreateVendorRequest;
import com.ecommerce.payload.request.UpdateAppUserRequest;
import com.ecommerce.payload.request.UpdateVendorRequest;
import com.ecommerce.repository.AppUserRepository;
import com.ecommerce.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VendorService {
    private final AppUserRepository appUserRepository;
    private final VendorRepository vendorRepository;
    public List<VendorDto> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        return vendors.stream().map(VendorMapper::map)
                .collect(Collectors.toList());
    }

    public VendorDto getVendorById(final Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor with id [%s] not found".formatted(vendorId))
                );
        return VendorMapper.map(vendor);
    }

    public Long createVendor(CreateVendorRequest createRequest) {
        final boolean emailTaken = appUserRepository.existsByEmail(createRequest.email())
                || vendorRepository.existsByEmail(createRequest.email());
        if(emailTaken){
            throw new DuplicateResourceException("Email already taken");
        }

        Vendor vendor = VendorMapper.map(createRequest);
        vendor = vendorRepository.save(vendor);

        return vendor.getId();
    }

    public void updateVendor(final Long vendorId, final UpdateVendorRequest updateRequest) {
        // TODO: for JPA use .getReferenceById(customerId) as it does does not bring object into memory and instead a reference
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor with id [%s] not found".formatted(vendorId))
                );
        //TODO: wonder if password can be changed
        vendor.setCompany(new Company(
                updateRequest.companyCountry(),
                updateRequest.taxNumber()
        ));
        vendor.setPhoneNumber(updateRequest.phoneNumber());

        if (updateRequest.email() != null && !updateRequest.email().equals(vendor.getEmail())) {
            if (appUserRepository.existsByEmail(updateRequest.email())
            || vendorRepository.existsByEmail(updateRequest.email())) {
                throw new DuplicateResourceException(
                        "Email already taken."
                );
            }
            vendor.setEmail(updateRequest.email());
        }

        vendorRepository.save(vendor);
    }

    public void deleteVendor(final Long vendorId) {
        vendorRepository.deleteById(vendorId);
    }
}
