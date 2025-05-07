package com.example.ant_algorithm_tsp_backend.service;

import com.example.ant_algorithm_tsp_backend.model.api.*;
import com.example.ant_algorithm_tsp_backend.model.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlgorithmLogicService {

    @Autowired
    private CityLoaderService cityLoaderService;

    @Autowired
    private AlgorithmStoreService algorithmStoreService;

    public void startAlgorithm(AlgorithmParams params) {

        // --- 1. Wczytanie miast i stworzenie grafu ---
        algorithmStoreService.clear();
        List<City> cities = cityLoaderService.getCities();
        Graph graph = new Graph(cities);
        algorithmStoreService.setLastUsedGraph(graph);

        // --- 2. Pętla po iteracjach algorytmu ---
        long startTime = System.currentTimeMillis();

        for (int iteration = 0; iteration < params.getIterations(); iteration++) {

            // --- 3. Tworzenie nowych mrówek ---
            List<Ant> ants = new ArrayList<>();
            for (int antCount = 0; antCount < params.getAnts(); antCount++) {
                ants.add(new Ant(cities));
            }

            // --- 4. Przemieszczanie się mrówek ---
            for (Ant ant : ants) {
                while (!ant.getUnvisitedCities().isEmpty()) {
                    ant.moveToNextCity(graph, params);
                }

                // Powrót do miasta startowego
                int startCityId = ant.getVisitedCities().get(0);
                int endCityId = ant.getCurrentCityId();
                graph.getEdges().stream()
                        .filter(edge -> edge.getFromCityId() == endCityId && edge.getToCityId() == startCityId)
                        .findFirst()
                        .ifPresent(edge -> ant.addTourLength(edge.getDistance()));
            }

            // --- 5. Najlepsza mrówka w tej iteracji ---
            Ant bestAntThisIteration = ants.stream()
                    .min(Comparator.comparingDouble(Ant::getTourLength))
                    .orElseThrow();

            // --- 6. Aktualizacja feromonów ---
            evaporatePheromones(graph, params);
            depositPheromones(graph, bestAntThisIteration);

            // --- 7. Co 10 iteracji: snapshot ---
            if (iteration % 10 == 0 || iteration == params.getIterations() - 1) {
                long elapsedTime = System.currentTimeMillis() - startTime;

                Map<Integer, List<Integer>> paths = new HashMap<>();
                for (Ant ant : ants) {
                    int start = ant.getVisitedCities().get(0);
                    List<Integer> tour = new ArrayList<>(ant.getVisitedCities());
                    tour.add(start);
                    paths.putIfAbsent(start, tour);
                }

                IterationSnapshot snapshot = new IterationSnapshot(
                        iteration,
                        elapsedTime,
                        null,
                        paths
                );

                algorithmStoreService.addSnapshot(snapshot);
            }
        }
    }

    // --- A) Parowanie feromonów ---
    private void evaporatePheromones(Graph graph, AlgorithmParams params) {
        double evaporationRate = params.getEvaporation();
        for (Edge edge : graph.getEdges()) {
            double updatedPheromone = edge.getPheromone() * (1.0 - evaporationRate);
            edge.setPheromone(updatedPheromone);
        }
    }

    // --- B) Dodawanie feromonów po trasie najlepszej mrówki ---
    private void depositPheromones(Graph graph, Ant bestAnt) {
        List<Integer> path = bestAnt.getVisitedCities();
        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);

            graph.getEdges().stream()
                    .filter(edge -> edge.getFromCityId() == from && edge.getToCityId() == to)
                    .findFirst()
                    .ifPresent(edge -> edge.setPheromone(
                            edge.getPheromone() + 1.0 / bestAnt.getTourLength()));
        }

        // domknięcie cyklu
        int lastCity = path.get(path.size() - 1);
        int firstCity = path.get(0);
        graph.getEdges().stream()
                .filter(edge -> edge.getFromCityId() == lastCity && edge.getToCityId() == firstCity)
                .findFirst()
                .ifPresent(edge -> edge.setPheromone(
                        edge.getPheromone() + 1.0 / bestAnt.getTourLength()));
    }
}
