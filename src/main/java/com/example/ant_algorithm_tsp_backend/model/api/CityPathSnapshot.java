package com.example.ant_algorithm_tsp_backend.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CityPathSnapshot {
    private int iteration;
    private long elapsedTimeMillis;
    private double pathLength;
    private List<EdgeSnapshot> edges;
}
