package com.example.ant_algorithm_tsp_backend.model.logic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Edge {
    private int fromCityId;
    private int toCityId;
    private double distance;
    private double pheromone;
}