package com.alura.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Clase DatosAutor que representa la estructura de los datos de un autor recibidos desde una API.
 * La clase está anotada para ignorar propiedades desconocidas en el JSON y
 * mapear los alias de los campos JSON a los campos de la clase.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        /**
         * Nombre del autor.
         */
        @JsonAlias("name") String nombre,

        /**
         * Año de nacimiento del autor.
         */
        @JsonAlias("birth_year") Integer fechaNacimiento,

        /**
         * Año de fallecimiento del autor.
         */
        @JsonAlias("death_year") Integer fechaFallecimiento
) {
}
