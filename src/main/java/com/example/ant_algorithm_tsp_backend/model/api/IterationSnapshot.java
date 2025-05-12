package com.example.ant_algorithm_tsp_backend.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IterationSnapshot {
    private int iterationNumber;
    private long elapsedTimeMillis;
    private GraphSnapshot graph;
    private Map<Integer, List<Integer>> pathsByStartCity;
}

