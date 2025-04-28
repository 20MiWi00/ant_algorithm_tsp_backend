package com.example.ant_algorithm_tsp_backend.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GraphSnapshot {
    private List<Node> nodes;
    private List<Link> links;
}
