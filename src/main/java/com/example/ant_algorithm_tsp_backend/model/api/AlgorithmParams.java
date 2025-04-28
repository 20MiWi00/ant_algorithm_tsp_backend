package com.example.ant_algorithm_tsp_backend.model.api;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// klasa reprezentująca parametry wejściowe algorytmu mrówkowego
@Data
public class AlgorithmParams {

    @NotNull
    @Positive
    private Double alpha; // współczynnik alfa

    @NotNull
    @Positive
    private Double beta; // współczynnik beta

    @NotNull
    @PositiveOrZero
    private Double evaporation; // krok aktualizacji feromonu

    @NotNull
    @Positive
    private Integer iterations; // liczba iteracji

    @NotNull
    @Positive
    private Integer ants; // ilość mrówek
}
