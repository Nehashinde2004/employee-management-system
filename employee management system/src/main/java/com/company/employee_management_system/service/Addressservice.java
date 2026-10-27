package com.company.employee_management_system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.employee_management_system.dto.AddressDto;
import com.company.employee_management_system.entity.Address;

import com.company.employee_management_system.repository.copy.Addressrepository;

@Service
@Transactional
public class Addressservice {

    @Autowired
    private Addressrepository addressRepository;

    // SAVE
    public AddressDto saveAddress(AddressDto dto) {

        Address address = new Address();

        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());

        Address savedAddress = addressRepository.save(address);

        AddressDto response = new AddressDto();

        response.setAddressId(savedAddress.getAddressId());
        response.setCity(savedAddress.getCity());
        response.setState(savedAddress.getState());
        response.setPincode(savedAddress.getPincode());

        return response;
    }

    // FIND BY ID
    public AddressDto findById(int id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Address with this id is not found"));

        AddressDto response = new AddressDto();

        response.setAddressId(address.getAddressId());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPincode(address.getPincode());

        return response;
    }

    // FIND ALL
    public List<AddressDto> findAll() {

        List<Address> addresses = addressRepository.findAll();

        List<AddressDto> responseList = new ArrayList<>();

        for (Address address : addresses) {

            AddressDto dto = new AddressDto();

            dto.setAddressId(address.getAddressId());
            dto.setCity(address.getCity());
            dto.setState(address.getState());
            dto.setPincode(address.getPincode());

            responseList.add(dto);
        }

        return responseList;
    }

    // UPDATE
    public AddressDto updateAddress(AddressDto dto, int id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Address with this id is not found"));

        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());

        Address updatedAddress = addressRepository.save(address);

        AddressDto response = new AddressDto();

        response.setAddressId(updatedAddress.getAddressId());
        response.setCity(updatedAddress.getCity());
        response.setState(updatedAddress.getState());
        response.setPincode(updatedAddress.getPincode());

        return response;
    }

    // DELETE
    public void deleteAddress(int id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Address with this id is not found"));

        addressRepository.delete(address);
    }
}