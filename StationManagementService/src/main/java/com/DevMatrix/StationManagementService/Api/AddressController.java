package com.DevMatrix.StationManagementService.Api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DevMatrix.StationManagementService.Dtos.AddressDto;
import com.DevMatrix.StationManagementService.Services.AddressServiceImp;

@RestController
@RequestMapping("/api/Addresses")
public class AddressController {

    private final AddressServiceImp _addressService;

    public AddressController(AddressServiceImp addressService) {
        this._addressService = addressService;
    }

    @PostMapping("Create")
    public ResponseEntity<AddressDto> Create(@RequestBody AddressDto addressDto) {
        AddressDto address = _addressService.createAddress(addressDto);
        return ResponseEntity.ok(address);
    }

    @PostMapping("Update/isEdit/{isEdit}")
    public ResponseEntity<AddressDto> UpdateLocation(@PathVariable boolean isEdit, @RequestParam(required = false) Long id,@RequestBody AddressDto addressDto) {

        AddressDto address = new AddressDto();
        if(isEdit){
           address = _addressService.updateAddress(id, addressDto);
        }
        else{
             address = _addressService.createAddress(addressDto);
        }
        return ResponseEntity.ok(address);
    }

    @GetMapping("GetData/{id}")
    public ResponseEntity<AddressDto> GetData(@PathVariable Long id) {
        AddressDto address = _addressService.getAddressById(id).orElseThrow();
        return ResponseEntity.ok(address);
    }

    @GetMapping("GetAll")
    public ResponseEntity<List<AddressDto>> GetAll() {
        List<AddressDto> lstAddress = _addressService.getAllAddresses();
        return ResponseEntity.ok(lstAddress);
    }

    @DeleteMapping("Delete/{id}")
    public void Delete(@PathVariable Long id) {
        _addressService.deleteAddress(id);
    }
}
