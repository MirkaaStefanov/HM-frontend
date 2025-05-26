package com.example.HM_frontend.controllers;

import com.example.HM_frontend.clients.ProductClient;
import com.example.HM_frontend.dtos.ProductDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductClient productClient;

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("product", new ProductDTO());
        return "Product/form";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute ProductDTO product, HttpServletRequest request) throws IOException {
        String token = (String) request.getSession().getAttribute("sessionToken");
        List<String> images = new ArrayList<>();
        for (MultipartFile file : product.getImageFiles()){
            byte[] fileBytes = file.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(fileBytes);
            images.add(encodedImage);
        }
        product.setImageStrings(images);
        productClient.save(product, token);
        return "redirect:/all";
    }

    @GetMapping("/all")
    public String all(Model model, HttpServletRequest request) {
        List<ProductDTO> products = productClient.findAll();
        model.addAttribute("products", products);
        return "Product/all";
    }

//    @GetMapping("/edit/{id}")
//    public String update(@PathVariable Long id, Model model, HttpServletRequest request) {
//        String token = (String) request.getSession().getAttribute("sessionToken");
//        FirstProductDTO firstProductDTO = firstProductClient.findById(id, token);
//        model.addAttribute("product", firstProductDTO);
//        return "FirstProduct/edit";
//    }
//
//    @PostMapping("/edit/{id}")
//    public String updateSubmit(@PathVariable Long id, @ModelAttribute FirstProductDTO product, HttpServletRequest request) throws IOException {
//        String token = (String) request.getSession().getAttribute("sessionToken");
//
//        FirstProductDTO existingProduct = firstProductClient.findById(id, token);
//
//        if (product.getMultipartFile() != null && !product.getMultipartFile().isEmpty()) {
//            byte[] fileBytes = product.getMultipartFile().getBytes();
//            String encodedImage = Base64.getEncoder().encodeToString(fileBytes);
//            product.setImage(encodedImage);
//        } else {
//
//            product.setImage(existingProduct.getImage());
//        }
//
//        firstProductClient.update(id, product, token);
//        return "redirect:/firstProduct/all";
//    }

    @PostMapping("/delete/{id}")
    public String updateSubmit(@PathVariable Long id, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        productClient.delete(id, token);
        return "redirect:/firstProduct/all";
    }
}
