package com.example.ant_algorithm_tsp_backend.service;

import com.example.ant_algorithm_tsp_backend.model.api.GraphSnapshot;
import com.example.ant_algorithm_tsp_backend.model.api.Node;
import com.example.ant_algorithm_tsp_backend.model.logic.City;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CityLoaderService {

    private List<City> cities = new ArrayList<>();

    public List<City> loadCities() {
        if (!cities.isEmpty()) {
            return cities;
        }

        try (var inputStream = getClass().getClassLoader().getResourceAsStream("berlin52.tsp");
             var reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            boolean readCoordinates = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.equalsIgnoreCase("NODE_COORD_SECTION")) {
                    readCoordinates = true;
                    continue;
                }
                if (line.equalsIgnoreCase("EOF")) {
                    break;
                }

                if (readCoordinates && !line.isEmpty()) {
                    String[] parts = line.split("\\s+");
                    int id = Integer.parseInt(parts[0]);
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    cities.add(City.builder()
                            .id(id)
                            .x(x)
                            .y(y)
                            .build()
                    );
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load cities from berlin52.tsp", e);
        }
        return cities;
    }

    public List<City> getCities() {
        return cities;
    }

    public GraphSnapshot getNodes(){
        List<Node> nodes = cities.stream().map(city -> new Node(
                String.valueOf(city.getId()),
                city.getX(),
                city.getY()
        )).toList();
        return new GraphSnapshot(nodes, new ArrayList<>());
    }
}
