package com.example.ant_algorithm_tsp_backend.model.logic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class City {
    private int id;
    private double x;
    private double y;
}
