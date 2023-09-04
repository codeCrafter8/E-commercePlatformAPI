package com.ecommerce.mapper;

import com.ecommerce.dto.AppUserDto;
import com.ecommerce.dto.VendorDto;
import com.ecommerce.model.AppUser;
import com.ecommerce.model.Company;
import com.ecommerce.model.Vendor;
import com.ecommerce.payload.request.CreateVendorRequest;

public class VendorMapper {
    public static VendorDto map(final Vendor vendor) {
        return new VendorDto(
                vendor.getId(),
                vendor.getEmail(),
                vendor.getUsername(),
                vendor.getPhoneNumber(),
                vendor.getCompany().getCompanyCountry(),
                vendor.getCompany().getTaxNumber()
        );
    }
    public static Vendor map(final CreateVendorRequest createRequest){
        return new Vendor(
                new Company(
                        createRequest.companyCountry(),
                        createRequest.taxNumber()
                ),
                createRequest.email(),
                createRequest.username(),
                createRequest.password(),
                createRequest.phoneNumber()
        );
    }
}
