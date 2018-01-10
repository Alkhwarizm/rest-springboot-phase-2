package com.gdn.future.controller;

import com.gdn.future.entity.Address;
import com.gdn.future.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    AddressRepository addressRepository;


    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresss(){
        return ResponseEntity.ok(addressRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddress(@PathVariable(value = "id") BigInteger id){
        Optional<Address> addressOptional = addressRepository.findOne(id);
        if(!addressOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(addressOptional.get());
    }

    @PostMapping
    public ResponseEntity<Address> createAddress(@Valid @RequestBody Address address){
        return ResponseEntity.ok(addressRepository.save(address));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable(value = "id") BigInteger id,
                                                   @Valid @RequestBody Address addressNew){
        Optional<Address> addressOptional = addressRepository.findOne(id);
        if(!addressOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Address addressOld = addressOptional.get();
        addressOld.setAddress(addressNew.getAddress());

        Address addressUpdate = addressRepository.save(addressOld);
        return ResponseEntity.ok(addressUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Address> deleteAddress(@PathVariable(value = "id") BigInteger id){
        Optional<Address> addressOptional = addressRepository.findOne(id);
        if(!addressOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Address address = addressOptional.get();
        addressRepository.delete(address);
        return ResponseEntity.ok().build();
    }

}
