package com.gdn.future.controller;

import com.gdn.future.entity.Address;
import com.gdn.future.entity.Customer;
import com.gdn.future.repository.AddressRepository;
import com.gdn.future.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AddressRepository addressRepository;

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return ResponseEntity.ok(customerRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable(value = "id") BigInteger id){
        Optional<Customer> customerOptional = customerRepository.findOne(id);
        if(!customerOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customerOptional.get());
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer){
        return ResponseEntity.ok(customerRepository.save(customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable(value = "id") BigInteger id,
                                                   @Valid @RequestBody Customer customerNew){
        Optional<Customer> customerOptional = customerRepository.findOne(id);
        if(!customerOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Customer customerOld = customerOptional.get();
        customerOld.setName(customerNew.getName());

        Customer customerUpdate = customerRepository.save(customerOld);
        return ResponseEntity.ok(customerUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable(value = "id") BigInteger id){
        Optional<Customer> customerOptional = customerRepository.findOne(id);
        if(!customerOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Customer customer = customerOptional.get();
        customerRepository.delete(customer);
        return ResponseEntity.ok().build();
    }


    /**
     * Nested address
     */

    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<Address>> getAllAddresses(@PathVariable(value = "id") BigInteger customerId){
        Optional<Customer> customerOptional = customerRepository.findOne(customerId);
        if(!customerOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customerOptional.get().getAddressList());
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<Customer> createAddress(@PathVariable(value = "id") BigInteger customerId,
                                                 @Valid @RequestBody Address address){
        Optional<Customer> customerOptional = customerRepository.findOne(customerId);
        if(!customerOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Customer customer = customerOptional.get();
        customer.getAddressList().add(address);

        customer = customerRepository.save(customer);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable(value = "customerId") BigInteger customerId,
                                                 @PathVariable(value = "addressId") BigInteger addressId,
                                                 @Valid @RequestBody Address addressNew){
        Optional<Customer> customerOptional = customerRepository.findOne(customerId);
        if(!customerOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Optional<Address> addressOptional = addressRepository.findOne(addressId);
        if(!addressOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Customer customer = customerOptional.get();
        Address addressOld = addressOptional.get();

        for (Address address:customer.getAddressList()) {
            if(address.getId().equals(addressOld.getId())){
                addressOld.setAddress(addressNew.getAddress());
                Address addressUpdate = addressRepository.save(addressOld);
                return ResponseEntity.ok(addressUpdate);
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<Address> deleteAddress(@PathVariable(value = "customerId") BigInteger customerId,
                                                  @PathVariable(value = "addressId") BigInteger addressId){
        Optional<Customer> customerOptional = customerRepository.findOne(customerId);
        if(!customerOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Optional<Address> addressOptional = addressRepository.findOne(addressId);
        if(!addressOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Customer customer = customerOptional.get();
        Address addressOld = addressOptional.get();

        for (Address address:customer.getAddressList()) {
            if(address.getId().equals(addressOld.getId())){
                addressRepository.delete(addressOld);
                return ResponseEntity.ok(addressOld);
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
