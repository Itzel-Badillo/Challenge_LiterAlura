package com.alura.LiterAlura.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase Libro que representa un libro en una base de datos.
 */
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Autor> autores;

    private Double numeroDeDescargas;

    @ElementCollection(targetClass = Idioma.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "lenguajes", joinColumns = @JoinColumn(name = "libro_id"))
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "lenguaje")
    private Set<Idioma> lenguajes;

    /**
     * Constructor por defecto.
     */
    public Libro() {}

    /**
     * Constructor con parámetros.
     *
     * @param titulo El título del libro.
     * @param autores Los autores del libro.
     * @param numeroDeDescargas El número de descargas del libro.
     * @param lenguajes Los lenguajes en los que está disponible el libro.
     */
    public Libro(String titulo, Set<Autor> autores, Double numeroDeDescargas, Set<Idioma> lenguajes) {
        this.titulo = titulo;
        this.autores = autores;
        this.numeroDeDescargas = numeroDeDescargas;
        this.lenguajes = lenguajes;
    }

    /**
     * Devuelve una representación en cadena del libro.
     *
     * @return Una cadena que representa el libro.
     */
    @Override
    public String toString() {
        return "*********** Libro ***********\n" +
                "Titulo: " + titulo + "\n" +
                "Autor: " + autores.stream().map(Autor::getNombreAutor).collect(Collectors.joining(", ")) + "\n" +
                "Lenguaje: " + lenguajes + "\n" +
                "Número de descargas: " + numeroDeDescargas + "\n";
    }

    // Getters y Setters

    /**
     * Obtiene el ID del libro.
     *
     * @return El ID del libro.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del libro.
     *
     * @param id El ID del libro.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el título del libro.
     *
     * @return El título del libro.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del libro.
     *
     * @param titulo El título del libro.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene los autores del libro.
     *
     * @return Los autores del libro.
     */
    public Set<Autor> getAutores() {
        return autores;
    }

    /**
     * Establece los autores del libro.
     *
     * @param autores Los autores del libro.
     */
    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

    /**
     * Obtiene el número de descargas del libro.
     *
     * @return El número de descargas del libro.
     */
    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    /**
     * Establece el número de descargas del libro.
     *
     * @param numeroDeDescargas El número de descargas del libro.
     */
    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    /**
     * Obtiene los lenguajes en los que está disponible el libro.
     *
     * @return Los lenguajes en los que está disponible el libro.
     */
    public Set<Idioma> getLenguajes() {
        return lenguajes;
    }

    /**
     * Establece los lenguajes en los que está disponible el libro.
     *
     * @param lenguajes Los lenguajes en los que está disponible el libro.
     */
    public void setLenguajes(Set<Idioma> lenguajes) {
        this.lenguajes = lenguajes;
    }
}
