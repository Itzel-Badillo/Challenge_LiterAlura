package com.alura.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Clase Datos que representa la estructura de los datos recibidos desde una API.
 * La clase está anotada para ignorar propiedades desconocidas en el JSON y
 * mapear los alias de los campos JSON a los campos de la clase.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Datos(
        /**
         * Número total de resultados de la búsqueda.
         */
        @JsonAlias("count") Double total,

        /**
         * Lista de resultados de libros obtenidos.
         */
        @JsonAlias("results") List<DatosLibro> resultados
) {
}

