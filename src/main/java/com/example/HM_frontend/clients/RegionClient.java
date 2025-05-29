package com.example.HM_frontend.clients;

import com.example.HM_frontend.dtos.RegionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "hm-region", url = "${backend.base-url}/region")
public interface RegionClient {

    @PostMapping("/save")
    RegionDTO save(@RequestBody RegionDTO regionDTO, @RequestHeader(value = "Authorization", required = false) String auth);

    @GetMapping("/all")
    List<RegionDTO> findAll(@RequestHeader(value = "Authorization", required = false) String auth);

    @GetMapping("/findById/{id}")
    RegionDTO findById(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String auth);

    @PutMapping("/edit/{id}")
    RegionDTO update(@PathVariable Long id, @RequestBody RegionDTO regionDTO, @RequestHeader(value = "Authorization", required = false) String auth);

    @DeleteMapping("/delete/{id}")
    void delete(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String auth);


}
