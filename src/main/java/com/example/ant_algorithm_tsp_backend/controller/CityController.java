package com.example.ant_algorithm_tsp_backend.controller;

import com.example.ant_algorithm_tsp_backend.model.logic.City;
import com.example.ant_algorithm_tsp_backend.service.CityLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityLoaderService cityLoaderService;

    @GetMapping("/load")
    public List<City> loadCities() {
        return cityLoaderService.loadCities();
    }

    @GetMapping
    public List<City> getCities() {
        return cityLoaderService.getCities();
    }
}