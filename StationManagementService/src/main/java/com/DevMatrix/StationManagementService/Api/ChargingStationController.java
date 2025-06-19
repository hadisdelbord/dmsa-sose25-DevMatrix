package com.DevMatrix.StationManagementService.Api;

import java.util.List;

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

import com.DevMatrix.StationManagementService.Dtos.AddressDto;
import com.DevMatrix.StationManagementService.Dtos.ChargingStationDto;
import com.DevMatrix.StationManagementService.Mapper.ChargingStationMaper;
import com.DevMatrix.StationManagementService.Services.ChargingStationService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("api/ChargingStations")
public class ChargingStationController {
    private ChargingStationService _chargingStationService;
    
    ChargingStationController(ChargingStationService chargingStationService,
     ChargingStationMaper chargingStationMapper){
        this._chargingStationService = chargingStationService;
    }

    @GetMapping("GetAll")
    public ResponseEntity<List<ChargingStationDto>> GetAll() {
        List<ChargingStationDto> lstAddress = _chargingStationService.getAllStations();
        return ResponseEntity.ok(lstAddress);
    }

    @GetMapping("GetStation/StationId/{id}")
    public ResponseEntity<ChargingStationDto> getById(@PathVariable Long id) {
        ChargingStationDto stationDto = _chargingStationService.getStationById(id);
        if(stationDto != null){
            return ResponseEntity.ok(stationDto);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("UpdateStation/isEdit/{isEdit}")
    public ResponseEntity<ChargingStationDto> UpdateChargingStation(@PathVariable boolean isEdit, @RequestParam(required = false) Long id,@RequestBody ChargingStationDto stationDto) {
        ChargingStationDto station = new ChargingStationDto();
        if(isEdit){
           station = _chargingStationService.updateStation(id, stationDto);
        }
        else{
            station = _chargingStationService.createStation(stationDto);
        }
        return ResponseEntity.ok(station);
    }

    @PostMapping("CreateStation")
    public ResponseEntity<ChargingStationDto> createStation(@RequestBody ChargingStationDto dto) {
        ChargingStationDto created = _chargingStationService.createStation(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("UpdateStation/StationId/{id}")
    public ResponseEntity<ChargingStationDto> updateStation(@PathVariable Long id, @RequestBody ChargingStationDto dto) {
        ChargingStationDto stationDto = _chargingStationService.updateStation(id, dto);
        if(stationDto != null){
            return ResponseEntity.ok(stationDto);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("DeleteStation/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        if (_chargingStationService.deleteStation(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
