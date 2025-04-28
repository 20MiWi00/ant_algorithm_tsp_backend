package com.example.ant_algorithm_tsp_backend.model.logic;


import com.example.ant_algorithm_tsp_backend.model.api.AlgorithmParams;
import lombok.Getter;
import java.util.*;

@Getter
public class Ant {

    private final List<Integer> visitedCities = new ArrayList<>(); // przebyte miasta
    private final Set<Integer> unvisitedCities = new HashSet<>(); // nieprzebyte miasta
    private int currentCityId; // aktualne miasto
    private double tourLength = 0.0; // długość trasy

    public Ant(List<City> cities) {
        for (City city : cities) {
            unvisitedCities.add(city.getId());
        }
        Random random = new Random();
        int startIndex = random.nextInt(cities.size());
        City startCity = cities.get(startIndex);

        currentCityId = startCity.getId();
        visitedCities.add(currentCityId);
        unvisitedCities.remove(currentCityId);
    }

    // ruch mrówki po mapie
    public void moveToNextCity(Graph graph, AlgorithmParams params) {
        if (unvisitedCities.isEmpty()) {
            return;
        }


        int nextCityId = selectNextCity(graph, params);


        Optional<Edge> edgeOptional = graph.getEdges().stream()
                .filter(edge -> edge.getFromCityId() == currentCityId && edge.getToCityId() == nextCityId)
                .findFirst();

        if (edgeOptional.isPresent()) {
            Edge edge = edgeOptional.get();
            tourLength += edge.getDistance();
        }


        currentCityId = nextCityId;
        visitedCities.add(currentCityId);
        unvisitedCities.remove(currentCityId);
    }

    private int selectNextCity(Graph graph, AlgorithmParams params) {
        double alpha = params.getAlpha();
        double beta = params.getBeta();

        List<Edge> possibleEdges = graph.getEdges().stream()
                .filter(edge -> edge.getFromCityId() == currentCityId && unvisitedCities.contains(edge.getToCityId()))
                .toList();

        Map<Integer, Double> probabilities = new HashMap<>();
        double sum = 0.0;

        for (Edge edge : possibleEdges) {
            double pheromoneInfluence = Math.pow(edge.getPheromone(), alpha);
            double distanceInfluence = Math.pow(1.0 / edge.getDistance(), beta);
            double value = pheromoneInfluence * distanceInfluence;
            probabilities.put(edge.getToCityId(), value);
            sum += value;
        }

        // metoda ruletki
        double random = new Random().nextDouble() * sum;
        double cumulative = 0.0;

        for (Map.Entry<Integer, Double> entry : probabilities.entrySet()) {
            cumulative += entry.getValue();
            if (random <= cumulative) {
                return entry.getKey();
            }
        }


        return unvisitedCities.iterator().next();
    }

    public void addTourLength(double distance) {
        this.tourLength += distance;
    }
}