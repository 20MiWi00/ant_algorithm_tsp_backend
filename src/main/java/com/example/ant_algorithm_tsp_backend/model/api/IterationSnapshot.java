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

    // Możesz ustawić na `GraphSnapshot` lub `null`, jeśli niepotrzebny
    private GraphSnapshot graph;

    // 🔥 Najważniejsze: mapa tras (startCityId → lista odwiedzonych miast)
    private Map<Integer, List<Integer>> pathsByStartCity;
}