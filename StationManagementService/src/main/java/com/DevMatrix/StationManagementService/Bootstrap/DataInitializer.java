package com.DevMatrix.StationManagementService.Bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.DevMatrix.StationManagementService.Repositories.IAddressRepository;
import com.DevMatrix.StationManagementService.domain.entity.Address;
import com.DevMatrix.StationManagementService.domain.entity.GeoLocation;
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
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Kampstraße 1", new PostalCode("44137"), new GeoLocation(51.5142, 7.4681), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Brückstraße 20", new PostalCode("44135"), new GeoLocation(51.5156, 7.4589), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Westenhellweg 85", new PostalCode("44137"), new GeoLocation(51.5143, 7.4624), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Ostwall 60", new PostalCode("44135"), new GeoLocation(51.5138, 7.4692), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Rheinische Straße 12", new PostalCode("44137"), new GeoLocation(51.5156, 7.4703), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Märkische Straße 50", new PostalCode("44141"), new GeoLocation(51.5198, 7.4512), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Hansastraße 90", new PostalCode("44137"), new GeoLocation(51.5167, 7.4634), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Bornstraße 276", new PostalCode("44145"), new GeoLocation(51.5089, 7.4456), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Mallinckrodtstraße 25", new PostalCode("44145"), new GeoLocation(51.5076, 7.4498), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Hohe Straße 20", new PostalCode("44149"), new GeoLocation(51.5234, 7.4389), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Bodelschwinghstraße 15", new PostalCode("44357"), new GeoLocation(51.5312, 7.4867), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Feldstraße 34", new PostalCode("44141"), new GeoLocation(51.5201, 7.4556), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Saarlandstraße 130", new PostalCode("44139"), new GeoLocation(51.5043, 7.4723), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Lindemannstraße 79", new PostalCode("44137"), new GeoLocation(51.5129, 7.4645), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Sonnenstraße 45", new PostalCode("44139"), new GeoLocation(51.5067, 7.4689), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Königswall 24", new PostalCode("44137"), new GeoLocation(51.5178, 7.4601), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Phoenixseestraße 8", new PostalCode("44263"), new GeoLocation(51.4934, 7.5123), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Wittener Straße 14", new PostalCode("44149"), new GeoLocation(51.5189, 7.4298), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Schüruferstraße 180", new PostalCode("44269"), new GeoLocation(51.4876, 7.5234), null));
        _addressRepository.save(new Address("North Rhine-Westphalia", "Dortmund", "Rahmer Straße 50", new PostalCode("44369"), new GeoLocation(51.5456, 7.5089), null));
            System.out.println("✅ Address test data inserted.");
        }
        
    }
}
