package com.example.HM_frontend.clients;

import com.example.HM_frontend.dtos.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "hm-products", url = "${backend.base-url}/products")
public interface ProductClient {

    @PostMapping("/save")
    ProductDTO save(@RequestBody ProductDTO productDTO, @RequestHeader("Authorization") String auth);

    @GetMapping("/all")
    List<ProductDTO> findAll(@RequestHeader("Authorization") String auth);

    @GetMapping("/findById/{id}")
    ProductDTO findById(@PathVariable Long id, @RequestHeader("Authorization") String auth);

    @PutMapping("/edit/{id}")
    ProductDTO update(@PathVariable Long id, @RequestBody ProductDTO firstProductDTO, @RequestHeader("Authorization") String auth);

    @DeleteMapping("/delete/{id}")
    void delete(@PathVariable Long id, @RequestHeader("Authorization") String auth);
}
