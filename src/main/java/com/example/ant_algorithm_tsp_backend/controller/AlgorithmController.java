package com.example.ant_algorithm_tsp_backend.controller;

import com.example.ant_algorithm_tsp_backend.model.api.AlgorithmParams;
import com.example.ant_algorithm_tsp_backend.model.api.IterationSnapshot;
import com.example.ant_algorithm_tsp_backend.service.AlgorithmService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/algorithm")
public class AlgorithmController {

    @Autowired
    private AlgorithmService algorithmService;

    @PostMapping("/start")
    public ResponseEntity<List<IterationSnapshot>> startAlgorithm(@RequestBody @Valid AlgorithmParams params) {
        List<IterationSnapshot> snapshots = algorithmService.startAlgorithm(params);
        return ResponseEntity.ok(snapshots);
    }
}