package com.example.ant_algorithm_tsp_backend.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
// Zbiór połączeń i punktów w stanie N-tej iteracji
public class GraphSnapshot {
    private List<NodeSnapshot> nodes;
    private List<EdgeSnapshot> edges;
}
