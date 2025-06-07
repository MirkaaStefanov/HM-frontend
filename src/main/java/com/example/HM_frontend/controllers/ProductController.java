package com.example.HM_frontend.controllers;

import com.example.HM_frontend.clients.ProductClient;
import com.example.HM_frontend.clients.RegionClient;
import com.example.HM_frontend.dtos.ProductDTO;
import com.example.HM_frontend.dtos.RegionDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductClient productClient;
    private final RegionClient regionClient;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/create")
    public String create(Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        List<RegionDTO> regions = regionClient.findAll(token);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("regions", regions);
        return "Product/form";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute ProductDTO product, HttpServletRequest request) throws IOException {
        String token = (String) request.getSession().getAttribute("sessionToken");
        List<String> images = new ArrayList<>();
        for (MultipartFile file : product.getImageFiles()) {
            byte[] fileBytes = file.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(fileBytes);
            images.add(encodedImage);
        }
        RegionDTO regionDTO = regionClient.findById(product.getRegionId(), token);
        product.setRegion(regionDTO);
        product.setImageStrings(images);
        ProductDTO savedProduct = productClient.save(product, token);
        messagingTemplate.convertAndSend("/topic/new-product", savedProduct);

        return "redirect:/product/all";
    }

    @GetMapping("/all")
    public String all(Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        List<ProductDTO> products = productClient.findAll();
        List<RegionDTO> regions = regionClient.findAll(token);
        model.addAttribute("products", products);
        model.addAttribute("regions", regions);
        return "Product/all";
    }

    @GetMapping("/filter")
    public String filterProducts(
            Model model,
            HttpServletRequest request,
            @RequestParam(required = false) String globalSearchText,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minArea,
            @RequestParam(required = false) Integer maxArea,
            @RequestParam(required = false) Integer minRooms,
            @RequestParam(required = false) Integer maxRooms,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) String state
    ) {
        // No token needed for filterProducts in the backend, assuming it's a public endpoint
        List<ProductDTO> products = productClient.filterProducts(
                globalSearchText, minPrice, maxPrice, minArea, maxArea, minRooms, maxRooms, city, regionId, state
        );

        String token = (String) request.getSession().getAttribute("sessionToken");
        List<RegionDTO> regions = regionClient.findAll(token); // Always fetch regions for the dropdown

        model.addAttribute("products", products);
        model.addAttribute("regions", regions);
        return "Product/all";
    }

    @GetMapping("/{id}") // e.g., /product/123
    public String productDetail(@PathVariable Long id, Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        ProductDTO product = productClient.findById(id);
        model.addAttribute("product", product);
        return "Product/detail";
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
        return "redirect:/product/all";
    }

    @PostMapping("/like/{id}")
    @ResponseBody // <-- IMPORTANT: Tells Spring to return data, not a view
    public ResponseEntity<Void> likeProduct(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = (String) request.getSession().getAttribute("sessionToken");
            if (token == null) {
                // The user is not logged in, return "Unauthorized"
                return ResponseEntity.status(401).build();
            }
            // Call the backend API via the client
            productClient.like(id, token);

            // On success, return an HTTP 200 OK response with no body
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Error liking product id={}: {}", id, e.getMessage());
            // If anything goes wrong, return an Internal Server Error status
            return ResponseEntity.status(500).build();
        }
    }

}
