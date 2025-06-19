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
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Kampstraße 1", new PostalCode("44137"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Brückstraße 20", new PostalCode("44135"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Westenhellweg 85", new PostalCode("44137"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Ostwall 60", new PostalCode("44135"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Rheinische Straße 12", new PostalCode("44137"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Märkische Straße 50", new PostalCode("44141"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Hansastraße 90", new PostalCode("44137"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Bornstraße 276", new PostalCode("44145"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Mallinckrodtstraße 25", new PostalCode("44145"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Hohe Straße 20", new PostalCode("44149"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Bodelschwinghstraße 15", new PostalCode("44357"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Feldstraße 34", new PostalCode("44141"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Saarlandstraße 130", new PostalCode("44139"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Lindemannstraße 79", new PostalCode("44137"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Sonnenstraße 45", new PostalCode("44139"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Königswall 24", new PostalCode("44137"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Phoenixseestraße 8", new PostalCode("44263"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Wittener Straße 14", new PostalCode("44149"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Schüruferstraße 180", new PostalCode("44269"), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Rahmer Straße 50", new PostalCode("44369"), null));
            System.out.println("✅ Address test data inserted.");
        }
        
    }
}
