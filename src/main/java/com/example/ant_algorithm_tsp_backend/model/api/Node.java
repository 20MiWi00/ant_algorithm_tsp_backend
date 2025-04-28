package com.example.ant_algorithm_tsp_backend.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
// Transponowanie miasta na potrzeby formatu biblioteki frontend
public class Node {
    private String id;
    private Double x;
    private Double y;
}

