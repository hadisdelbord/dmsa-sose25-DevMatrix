package com.DevMatrix.StationManagementService.Api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DevMatrix.StationManagementService.Dtos.AvailableSlotDto;
import com.DevMatrix.StationManagementService.Dtos.OfferAndStationDto;
import com.DevMatrix.StationManagementService.Dtos.OfferSlotDto;
import com.DevMatrix.StationManagementService.Services.OfferSlotService;

@CrossOrigin(origins = "http://localhost:5173")
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

    @GetMapping("GetOfferByStation/station/{stationId}")
public ResponseEntity<List<OfferSlotDto>> getOffersByStationAndDate(
    @PathVariable Long stationId,
    @RequestParam("slotDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate slotDate
) {
    List<OfferSlotDto> slots = _offerSlotService.GetByStationIdAndDate(stationId, slotDate);
    return ResponseEntity.ok(slots);
}


    @PostMapping("CreateOrUpdate")
    public ResponseEntity<String> createOrUpdateAll(@RequestBody List<OfferSlotDto> dtos) {
         _offerSlotService.createOrUpdateAll(dtos); // No return value needed if it's just a save
    return ResponseEntity.ok("Offer slots saved successfully.");
    }
    


    @PutMapping("UpdateOffer/OfferId/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody OfferSlotDto dto) {
        var result = _offerSlotService.update(id, dto);
        if(result != null){
            return ResponseEntity.ok().build();
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
    @GetMapping("GetAvailableOffer/postalcode/{postalCode}")
    public ResponseEntity<List<AvailableSlotDto>> GetAvailableOffer(@PathVariable String postalCode) {
        var offerslotWithAddress = _offerSlotService.GetAvailableOffers(postalCode);
        return ResponseEntity.ok(offerslotWithAddress);
    }

    @GetMapping("GetOfferById/OfferId/{offer_id}")
    public ResponseEntity<OfferAndStationDto> GetOfferById(@PathVariable Long offer_id) {
        var offerslotWithStation = _offerSlotService.GetOfferWithStationById(offer_id);
        return offerslotWithStation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
