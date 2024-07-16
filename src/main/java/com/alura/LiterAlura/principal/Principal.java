package com.alura.LiterAlura.principal;

import com.alura.LiterAlura.model.Autor;
import com.alura.LiterAlura.model.Datos;
import com.alura.LiterAlura.model.DatosLibro;
import com.alura.LiterAlura.model.Idioma;
import com.alura.LiterAlura.model.Libro;
import com.alura.LiterAlura.repository.AutorRepository;
import com.alura.LiterAlura.repository.LibroRepository;
import com.alura.LiterAlura.service.ConsumoAPI;
import com.alura.LiterAlura.service.ConvierteDatos;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {

    // Scanner para leer entradas del usuario
    private Scanner lectura = new Scanner(System.in);
    // Servicio para consumir la API
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    // URL base de la API
    private final String URL_BASE = "https://gutendex.com/books/";
    // Servicio para convertir datos
    private ConvierteDatos conversor = new ConvierteDatos();
    // Conjunto de libros encontrados
    private Set<Libro> librosEncontrados = new HashSet<>();
    // Repositorios para interactuar con la base de datos
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    // Conjunto de libros
    private Set<Libro> libros;

    // Constructor con inyección de dependencias
    @Autowired
    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    /**
     * Muestra el menú principal de la aplicación.
     */
    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ************** GUTENDEX API ****************
                    ****************** MENÚ ********************
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = lectura.nextInt();
            lectura.nextLine();

            // Selección de opciones del menú
            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    autorAnoConcreto();
                    break;
                case 5:
                    librosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    /**
     * Obtiene los datos de un libro buscando por su título.
     * @return Datos del libro encontrado.
     */
    private DatosLibro getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar: ");
        var nombreLibro = lectura.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        Datos datos = conversor.obtenerDatos(json, Datos.class);

        if (datos.resultados().isEmpty()) {
            throw new RuntimeException("No se encontraron libros para el título dado.");
        }

        DatosLibro datosLibro = datos.resultados().get(0); // obtener el primer libro de los resultados
        return new DatosLibro(
                datosLibro.id(),
                datosLibro.titulo(),
                datosLibro.autores(),
                datosLibro.numeroDeDescargas(),
                datosLibro.lenguajes()
        );
    }

    /**
     * Busca un libro por su título y lo guarda en la base de datos.
     */
    private void buscarLibroPorTitulo() {
        DatosLibro datos = getDatosLibro();
        Set<Autor> autores = datos.autores().stream()
                .map(datoAutor -> new Autor(datoAutor.nombre(), datoAutor.fechaNacimiento(), datoAutor.fechaFallecimiento()))
                .collect(Collectors.toSet());
        Libro libro = new Libro(datos.titulo(), autores, datos.numeroDeDescargas(), new HashSet<>(datos.lenguajes()));
        libroRepository.save(libro);
        librosEncontrados.add(libro);
        System.out.println(libro.toString());
    }

    /**
     * Lista todos los libros registrados en la base de datos.
     */
    @Transactional(readOnly = true)
    private void listarLibros() {
        libros = new HashSet<>(libroRepository.findAll());

        // Inicializa las colecciones de cada libro
        libros.forEach(libro -> {
            libroRepository.findByIdWithAutoresAndLenguajes(libro.getId()).ifPresent(libroConAutores -> {
                libro.setAutores(libroConAutores.getAutores());
                libro.setLenguajes(libroConAutores.getLenguajes());
            });
        });

        // Comparador para ordenar libros por el nombre del primer autor
        Comparator<Libro> comparador = Comparator.comparing(libro ->
                libro.getAutores().isEmpty() ? "" : libro.getAutores().iterator().next().getNombreAutor()
        );

        libros.stream()
                .sorted(comparador)
                .forEach(System.out::println);
    }

    /**
     * Lista todos los autores registrados en la base de datos.
     */
    @Transactional(readOnly = true)
    private void listarAutores() {
        List<Autor> autores = autorRepository.findAllWithLibros();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.forEach(autor -> {
                System.out.println("Nombre: " + autor.getNombreAutor());
                System.out.println("Fecha de Nacimiento: " + autor.getFechaDeNacimiento());
                System.out.println("Fecha de Fallecimiento: " + (autor.getFechaDeFallecimiento() != null ? autor.getFechaDeFallecimiento() : "N/A"));
                if (autor.getLibro() != null) {
                    System.out.println("Libro: " + autor.getLibro().getTitulo());
                } else {
                    System.out.println("Libro: N/A");
                }
                System.out.println();
            });
        }
    }

    /**
     * Lista autores que vivieron en un año específico.
     */
    @Transactional(readOnly = true)
    private void autorAnoConcreto() {
        System.out.println("Escribe el año que deseas buscar: ");
        var anio = lectura.nextInt();
        lectura.nextLine();
        List<Autor> autors = autorRepository.findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(anio);
        if (autors.isEmpty()) {
            System.out.println("No se pudo encontrar a un autor que haya vivido en el año:  " + anio);
        } else {
            autors.forEach(autor -> {
                System.out.println("Nombre: " + autor.getNombreAutor());
                System.out.println("Fecha de Nacimiento: " + autor.getFechaDeNacimiento());
                System.out.println("Fecha de Fallecimiento: " + (autor.getFechaDeFallecimiento() != null ? autor.getFechaDeFallecimiento() : "N/A"));
                System.out.println("Libro: " + (autor.getLibro() != null ? autor.getLibro().getTitulo() : "N/A"));
                System.out.println();
            });
        }
    }

    /**
     * Lista libros por idioma.
     */
    @Transactional(readOnly = true)
    private void librosPorIdioma() {
        System.out.println("Por favor ingrese el idioma en el cual desea buscar los libros (es, en, fr, pt, etc.): ");
        var idiomaInput = lectura.nextLine().toLowerCase();

        try {
            Idioma idioma = Idioma.fromString(idiomaInput);
            List<Libro> libros = libroRepository.findByIdioma(idioma);

            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros en el idioma: " + idiomaInput);
            } else {
                libros.forEach(libro -> {
                    Hibernate.initialize(libro.getAutores());
                    Hibernate.initialize(libro.getLenguajes());

                    System.out.println(
                            "------ LIBRO ------" +
                                    "\nTitulo: " + libro.getTitulo() +
                                    "\nAutores: " + libro.getAutores().stream().map(Autor::getNombreAutor).collect(Collectors.joining(", ")) +
                                    "\nIdiomas: " + libro.getLenguajes().stream().map(Idioma::getDescripcion).collect(Collectors.joining(", ")) +
                                    "\nNúmero de descargas: " + libro.getNumeroDeDescargas() +
                                    "\n------------------"
                    );
                });
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Idioma no reconocido: " + idiomaInput);
        }
    }

    /**
     * Método principal para ejecutar la aplicación.
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(Principal.class, args);
    }
}
