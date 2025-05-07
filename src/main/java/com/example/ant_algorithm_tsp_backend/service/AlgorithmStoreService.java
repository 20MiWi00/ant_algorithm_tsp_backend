package com.example.ant_algorithm_tsp_backend.service;

import com.example.ant_algorithm_tsp_backend.model.api.CityPathSnapshot;
import com.example.ant_algorithm_tsp_backend.model.api.EdgeSnapshot;
import com.example.ant_algorithm_tsp_backend.model.api.IterationSnapshot;
import com.example.ant_algorithm_tsp_backend.model.logic.Graph;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AlgorithmStoreService {
    private final List<IterationSnapshot> snapshots = new ArrayList<>();
    @Getter
    private Graph lastUsedGraph;

    public void clear() {
        snapshots.clear();
        lastUsedGraph = null;
    }

    public void addSnapshot(IterationSnapshot snapshot) {
        snapshots.add(snapshot);
    }

    public void setLastUsedGraph(Graph graph) {
        this.lastUsedGraph = graph;
    }

    public List<CityPathSnapshot> getSnapshotsForCity(int cityId) {
        if (snapshots.isEmpty() || lastUsedGraph == null) return List.of();

        return snapshots.stream()
                .map(snapshot -> {
                    List<Integer> path = snapshot.getPathsByStartCity().get(cityId);
                    if (path == null || path.size() < 2) return null;

                    List<EdgeSnapshot> edges = new ArrayList<>();
                    double[] totalLength = {0.0};

                    for (int i = 0; i < path.size() - 1; i++) {
                        int from = path.get(i);
                        int to = path.get(i + 1);

                        edges.add(new EdgeSnapshot(String.valueOf(from), String.valueOf(to)));

                        lastUsedGraph.getEdges().stream()
                                .filter(e -> e.getFromCityId() == from && e.getToCityId() == to)
                                .findFirst()
                                .ifPresent(edge -> totalLength[0] += edge.getDistance());
                    }

                    return new CityPathSnapshot(
                            snapshot.getIterationNumber(),
                            snapshot.getElapsedTimeMillis(),
                            totalLength[0],
                            edges
                    );
                })
                .filter(Objects::nonNull)
                .toList();
    }



}
