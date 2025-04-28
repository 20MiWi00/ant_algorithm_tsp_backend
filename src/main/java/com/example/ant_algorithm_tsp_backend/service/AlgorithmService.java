package com.example.ant_algorithm_tsp_backend.service;


import com.example.ant_algorithm_tsp_backend.model.api.*;
import com.example.ant_algorithm_tsp_backend.model.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlgorithmService {

    @Autowired
    private CityLoaderService cityLoaderService;

    public List<IterationSnapshot> startAlgorithm(AlgorithmParams params) {

        // --- 1. Wczytanie miast i stworzenie grafu ---
        List<City> cities = cityLoaderService.getCities();
        Graph graph = new Graph(cities);

        // --- 2. Pętla po iteracjach algorytmu ---
        List<IterationSnapshot> snapshots = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        // --- 3. W ITERACJI: Tworzenie nowych mrówek ---
        for (int iteration = 0; iteration < params.getIterations(); iteration++) {
            List<Ant> ants = new ArrayList<>();
            for (int antCount = 0; antCount < params.getAnts(); antCount++) {
                ants.add(new Ant(cities));
            }

            // --- 4. W ITERACJI: Ruch mrówek po grafie ---
            for (Ant ant : ants) {
                while (!ant.getUnvisitedCities().isEmpty()) {
                    ant.moveToNextCity(graph, params);
                }
                // --- 5. W RUCHU MRÓWKI: Powrót do miasta startowego ---
                int startCityId = ant.getVisitedCities().get(0);
                int endCityId = ant.getCurrentCityId();
                graph.getEdges().stream()
                        .filter(edge -> edge.getFromCityId() == endCityId && edge.getToCityId() == startCityId)
                        .findFirst()
                        .ifPresent(edge -> ant.addTourLength(edge.getDistance()));
            }

            // --- 6. W ITERACJI: Znajdujemy najlepszą mrówkę ---
            Ant bestAntThisIteration = ants.stream()
                    .min((a1, a2) -> Double.compare(a1.getTourLength(), a2.getTourLength()))
                    .orElseThrow();

            // --- 7. W ITERACJI: Aaktualizacja feromonów ---
            evaporatePheromones(graph, params);
            depositPheromones(graph, bestAntThisIteration);

            // --- 8. W ITERACJI: Budowa response ---
            if (iteration % 10 == 0 || iteration == params.getIterations() - 1) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                GraphSnapshot snapshot = createGraphSnapshot(graph);
                snapshots.add(new IterationSnapshot(
                        iteration,
                        elapsedTime,
                        snapshot
                ));
            }
        }

        // --- 9. Zwrócenie response ---
        return snapshots;
    }


    // --- A) FUNKCJA POMOCNICZA: Zmniejsza feromon o wskazany krok ---
    private void evaporatePheromones(Graph graph, AlgorithmParams params) {
        double evaporationRate = params.getEvaporation();
        for (Edge edge : graph.getEdges()) {
            double updatedPheromone = edge.getPheromone() * (1.0 - evaporationRate);
            edge.setPheromone(updatedPheromone);
        }
    }

    // --- B) FUNKCJA POMOCNICZA: Zwiększa feromon dla najlepszych wyników ---
    private void depositPheromones(Graph graph, Ant bestAnt) {
        List<Integer> path = bestAnt.getVisitedCities();
        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);

            graph.getEdges().stream()
                    .filter(edge -> edge.getFromCityId() == from && edge.getToCityId() == to)
                    .findFirst()
                    .ifPresent(edge -> edge.setPheromone(edge.getPheromone() + 1.0 / bestAnt.getTourLength()));
        }

        int lastCity = path.get(path.size() - 1);
        int firstCity = path.get(0);

        graph.getEdges().stream()
                .filter(edge -> edge.getFromCityId() == lastCity && edge.getToCityId() == firstCity)
                .findFirst()
                .ifPresent(edge -> edge.setPheromone(edge.getPheromone() + 1.0 / bestAnt.getTourLength()));
    }

    // --- C) FUNKCJA POMOCNICZA : Generowanie zrzutu grafu (część response API) ---
    private GraphSnapshot createGraphSnapshot(Graph graph) {
        List<Node> nodes = graph.getCities().stream()
                .map(city -> new Node(
                        String.valueOf(city.getId()),
                        city.getX(),
                        city.getY()
                ))
                .toList();

        List<Link> links = graph.getEdges().stream()
                .map(edge -> new Link(
                        String.valueOf(edge.getFromCityId()),
                        String.valueOf(edge.getToCityId())
                ))
                .toList();

        return new GraphSnapshot(nodes, links);
    }

}