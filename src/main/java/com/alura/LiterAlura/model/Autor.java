package com.alura.LiterAlura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autor {

    // Identificador único para cada autor
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del autor, debe ser único
    @Column(unique = true)
    private String nombreAutor;

    // Año de nacimiento del autor
    private Integer fechaDeNacimiento;

    // Año de fallecimiento del autor (puede ser null si el autor está vivo)
    private Integer fechaDeFallecimiento;

    // Relación muchos a uno con la entidad Libro, con carga perezosa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_id")
    private Libro libro;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Autor() {
    }

    /**
     * Constructor que inicializa los atributos del autor usando un objeto DatosAutor.
     * @param datosAutor Objeto que contiene los datos del autor.
     */
    public Autor(DatosAutor datosAutor) {
        this.nombreAutor = datosAutor.nombre();
        this.fechaDeNacimiento = datosAutor.fechaNacimiento();
        this.fechaDeFallecimiento = datosAutor.fechaFallecimiento();
    }

    /**
     * Constructor que inicializa los atributos del autor.
     * @param nombreAutor Nombre del autor.
     * @param fechaDeNacimiento Año de nacimiento del autor.
     * @param fechaDeFallecimiento Año de fallecimiento del autor.
     */
    public Autor(String nombreAutor, Integer fechaDeNacimiento, Integer fechaDeFallecimiento) {
        this.nombreAutor = nombreAutor;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    @Override
    public String toString() {
        return "*********** Autor ***********\n" +
                "Nombre: " + nombreAutor + "\n" +
                "Año de nacimiento: " + fechaDeNacimiento + "\n" +
                "Año de fallecimiento: " + fechaDeFallecimiento + "\n" +
                "Libro: " + libro + "\n";
    }

    // Getters y setters

    /**
     * Obtiene el identificador único del autor.
     * @return Identificador del autor.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del autor.
     * @param id Identificador del autor.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del autor.
     * @return Nombre del autor.
     */
    public String getNombreAutor() {
        return nombreAutor;
    }

    /**
     * Establece el nombre del autor.
     * @param nombreAutor Nombre del autor.
     */
    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    /**
     * Obtiene el año de nacimiento del autor.
     * @return Año de nacimiento del autor.
     */
    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    /**
     * Establece el año de nacimiento del autor.
     * @param fechaDeNacimiento Año de nacimiento del autor.
     */
    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    /**
     * Obtiene el año de fallecimiento del autor.
     * @return Año de fallecimiento del autor.
     */
    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    /**
     * Establece el año de fallecimiento del autor.
     * @param fechaDeFallecimiento Año de fallecimiento del autor.
     */
    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    /**
     * Obtiene el libro asociado al autor.
     * @return Libro asociado al autor.
     */
    public Libro getLibro() {
        return libro;
    }

    /**
     * Establece el libro asociado al autor.
     * @param libro Libro asociado al autor.
     */
    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}
