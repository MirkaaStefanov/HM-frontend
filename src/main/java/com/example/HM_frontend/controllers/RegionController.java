package com.example.HM_frontend.controllers;


import com.example.HM_frontend.clients.RegionClient;
import com.example.HM_frontend.dtos.RegionDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/regions")
@RequiredArgsConstructor
@Slf4j
public class RegionController {

    private final RegionClient regionClient;

    @GetMapping("/create")
    public String showCreateRegionForm(Model model) {
        model.addAttribute("region", new RegionDTO());
        return "Region/form";
    }

    @PostMapping("/submit")
    public String submitRegionForm(@ModelAttribute RegionDTO regionDTO, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        if (token == null) {
            return "redirect:/login";
        }
        regionClient.save(regionDTO, token);
        return "redirect:/regions/all";
    }

    @GetMapping("/all")
    public String listAllRegions(Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        List<RegionDTO> regions = regionClient.findAll(token);
        model.addAttribute("regions", regions);
        return "Region/all";
    }

    @GetMapping("/edit/{id}")
    public String showEditRegionForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        RegionDTO regionDTO = regionClient.findById(id, token);
        model.addAttribute("region", regionDTO);
        return "Region/edit";
    }

    @PostMapping("/edit/{id}")
    public String submitEditRegionForm(@PathVariable Long id, @ModelAttribute RegionDTO regionDTO, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        regionClient.update(id, regionDTO, token);
        return "redirect:/regions/all";
    }
    @PostMapping("/delete/{id}")
    public String deleteRegion(@PathVariable Long id, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        regionClient.delete(id, token);
        return "redirect:/regions/all";
    }

    /**
     * Helper method to determine if the region client's findAll method requires a token.
     * Replace this with your actual check or remove if token is always/never needed for findAll.
     * @return true if token is required, false otherwise.
     */
    private boolean regionClientRequiresTokenForFindAll() {
        // Example: return true if your RegionClient's findAll method signature includes a token.
        // return false if it doesn't.
        // This is just a placeholder.
        try {
            RegionClient.class.getMethod("findAll", String.class);
            return true; // Method findAll(String token) exists
        } catch (NoSuchMethodException e) {
            return false; // Method findAll() or findAll(somethingElse) exists
        }
    }
}
