package com.DevMatrix.StationManagementService.Api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.DevMatrix.StationManagementService.Dtos.OfferSlotDto;
import com.DevMatrix.StationManagementService.Services.OfferSlotService;

@RestController
@RequestMapping("api/OfferSlots")
public class OfferSlotController {
    private OfferSlotService _offerSlotService;

    public OfferSlotController(OfferSlotService offerSlotService){
        this._offerSlotService = offerSlotService;
    }

     @GetMapping("GetAll")
    public ResponseEntity<List<OfferSlotDto>> GetAll() {
        List<OfferSlotDto> lstOffers = _offerSlotService.getAll();
        return ResponseEntity.ok(lstOffers);
    }

    @GetMapping("GetOffer/OfferId/{id}")
    public ResponseEntity<OfferSlotDto> getById(@PathVariable Long id) {
        return _offerSlotService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("Create")
    public ResponseEntity<OfferSlotDto> create(@RequestBody OfferSlotDto dto) {
        return ResponseEntity.ok(_offerSlotService.create(dto));
    }
    @PutMapping("UpdateOffer/OfferId/{id}")
    public ResponseEntity<OfferSlotDto> update(@PathVariable Long id, @RequestBody OfferSlotDto dto) {
        var offerResult = _offerSlotService.update(id, dto);
        if(offerResult != null){
            return ResponseEntity.ok(offerResult);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("DeleteOffer/OfferId/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (_offerSlotService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
