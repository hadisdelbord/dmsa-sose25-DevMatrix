package com.DevMatrix.StationManagementService.Api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.DevMatrix.StationManagementService.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/ChargingStations")
public class ChargingStationController {
    private ChargingStationService _chargingStationService;
    private final JwtUtil jwtUtil;
    
    ChargingStationController(ChargingStationService chargingStationService,
     ChargingStationMaper chargingStationMapper, JwtUtil jwtUtil){
        this._chargingStationService = chargingStationService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("GetAll")
    public ResponseEntity<List<ChargingStationDto>> GetAll(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
         if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
         }
         String email = jwtUtil.extractEmail(token);
        List<ChargingStationDto> lstAddress = _chargingStationService.getAllStations(email);
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
    public ResponseEntity<ChargingStationDto> UpdateChargingStation(@PathVariable boolean isEdit, @RequestParam(required = false) Long id,
    @RequestBody ChargingStationDto stationDto,HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
    }

    String email = jwtUtil.extractEmail(token);
        ChargingStationDto station = new ChargingStationDto();
        if(isEdit){
           station = _chargingStationService.updateStation(id, stationDto);
        }
        else{
            station = _chargingStationService.createStation(stationDto,email);
        }
        return ResponseEntity.ok(station);
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
