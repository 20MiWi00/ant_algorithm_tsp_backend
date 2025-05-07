package com.example.ant_algorithm_tsp_backend.controller;

import com.example.ant_algorithm_tsp_backend.model.api.AlgorithmParams;
import com.example.ant_algorithm_tsp_backend.model.api.CityPathSnapshot;
import com.example.ant_algorithm_tsp_backend.service.AlgorithmLogicService;
import com.example.ant_algorithm_tsp_backend.service.AlgorithmStoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/algorithm")
public class AlgorithmController {

    @Autowired
    private AlgorithmLogicService algorithmLogicService;
    @Autowired
    private AlgorithmStoreService algorithmStoreService;

    @PostMapping
    public ResponseEntity<String> startAlgorithm(@RequestBody @Valid AlgorithmParams params) {
        algorithmLogicService.startAlgorithm(params);
        return ResponseEntity.ok("Algorithm completed");
    }

    @GetMapping("/paths/{cityId}")
    public ResponseEntity<List<CityPathSnapshot>> getPathForCity(@PathVariable int cityId) {
        List<CityPathSnapshot> result = algorithmStoreService.getSnapshotsForCity(cityId);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}