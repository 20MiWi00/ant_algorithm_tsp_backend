package com.example.ant_algorithm_tsp_backend.model.api;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Link {
    private String source;
    private String target;
}
