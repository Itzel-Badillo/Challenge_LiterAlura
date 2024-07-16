package com.alura.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Clase DatosLibro que representa la estructura de los datos de un libro recibidos desde una API.
 * La clase está anotada para ignorar propiedades desconocidas en el JSON y
 * mapear los alias de los campos JSON a los campos de la clase.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        /**
         * Identificador único del libro.
         */
        @JsonAlias("id") Long id,

        /**
         * Título del libro.
         */
        @JsonAlias("title") String titulo,

        /**
         * Lista de autores del libro.
         */
        @JsonAlias("authors") List<DatosAutor> autores,

        /**
         * Número de descargas del libro.
         */
        @JsonAlias("download_count") Double numeroDeDescargas,

        /**
         * Lista de idiomas en los que está disponible el libro.
         */
        @JsonAlias("languages") List<Idioma> lenguajes) {

}
