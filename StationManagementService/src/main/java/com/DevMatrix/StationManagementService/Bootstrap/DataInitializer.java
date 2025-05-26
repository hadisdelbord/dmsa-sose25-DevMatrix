package com.DevMatrix.StationManagementService.Bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.DevMatrix.StationManagementService.Repositories.IAddressRepository;
import com.DevMatrix.StationManagementService.domain.entity.Address;
import com.DevMatrix.StationManagementService.domain.entity.PostalCode;

@Component
public class DataInitializer implements CommandLineRunner {
    private final IAddressRepository _addressRepository;
    public DataInitializer(IAddressRepository addressRepository){
        this._addressRepository = addressRepository;
    }
        @Override
    public void run(String... args) {
              if (_addressRepository.count()==0) {
                _addressRepository.save(new Address("NRW","Dortmund","s1",new PostalCode("44147"),null));
                _addressRepository.save(new Address("Hesse","Kassel","s1",new PostalCode("44146"),null));
                _addressRepository.save(new Address("Berlin","Berlin","s1",new PostalCode("44148"),null));
            System.out.println("✅ Address test data inserted.");
        }
        
    }
}
