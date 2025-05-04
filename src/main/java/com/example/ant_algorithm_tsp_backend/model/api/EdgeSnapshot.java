package com.example.ant_algorithm_tsp_backend.model.api;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Obiekt skąd id --> do id
public class EdgeSnapshot {
    private String from;
    private String to;
    private String arrows = "to";

    public EdgeSnapshot(String from, String to) {
        this.from = from;
        this.to = to;
        this.arrows = "to";
    }
}
