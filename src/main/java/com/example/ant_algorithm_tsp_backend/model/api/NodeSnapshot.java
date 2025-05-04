package com.example.ant_algorithm_tsp_backend.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Transponowanie miasta na potrzeby formatu biblioteki frontend
public class NodeSnapshot {
    private String id;
    private String label;
    private double x;
    private double y;
    private boolean fixed = true;

    public NodeSnapshot(String id, double x, double y) {
        this.id = id;
        this.label = "Miasto " + id;
        this.x = x;
        this.y = y;
        this.fixed = true;
    }
}

