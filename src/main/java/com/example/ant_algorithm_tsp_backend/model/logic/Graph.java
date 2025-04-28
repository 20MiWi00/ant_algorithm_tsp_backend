package com.example.ant_algorithm_tsp_backend.model.logic;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Graph {

    private final List<City> cities;
    private final List<Edge> edges;

    public Graph(List<City> cities) {
        this.cities = cities;
        this.edges = new ArrayList<>();
        initializeEdges();
    }

    private void initializeEdges() {
        for (City from : cities) {
            for (City to : cities) {
                if (from.getId() != to.getId()) {
                    edges.add(new Edge(
                            from.getId(),
                            to.getId(),
                            calculateDistance(from, to),
                            1.0 // początkowy poziom feromonu
                    ));
                }
            }
        }
    }

    private double calculateDistance(City a, City b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}