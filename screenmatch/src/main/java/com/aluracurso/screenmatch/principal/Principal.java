package com.aluracurso.screenmatch.principal;

import com.aluracurso.screenmatch.ConsumoAPI;
import com.aluracurso.screenmatch.ConvierteDatos;
import com.aluracurso.screenmatch.model.DatosEpisodio;
import com.aluracurso.screenmatch.model.DatosSerie;
import com.aluracurso.screenmatch.model.DatosTemporadas;
import com.aluracurso.screenmatch.model.Episodio;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE="https://www.omdbapi.com/?t=";
    private final String API_KEY="&apikey=4a968531";
    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraElMenu() {
        System.out.println("Por favor ingresa el nombre de las serie que deseas buscar");
        //Busca los datos de las serie de forma general
        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //Busca los datos de todas las temporadas

        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
            json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+")
                    + "&Season=" + i + API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporadas);

        }
        //temporadas.forEach(System.out::println);

        //Mostrar solo el titulo de los episodios para las temporadas

        for (int i = 0; i < datos.totalDeTemporadas(); i++) {
            List<DatosEpisodio> episodiosTemporadas = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporadas.size(); j++) {
                System.out.println(episodiosTemporadas.get(j).titulo());
            }
        }
        //temporadas.forEach(t -> t.episodios().forEach(e-> System.out.println(e.titulo())));

        //Lista avanzada
        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .peek(e -> System.out.println("Segunfo filtro ordenacion" + e))
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());
        episodios.forEach(System.out::println);

        //Busqueda por fecha

        /*System.out.println("Por favor indica el año apatir del cual deseas ver los episodios");
        var fecha= teclado.nextLine();
        LocalDate fechaBusqueda=LocalDate.of(Integer.parseInt(fecha),1,1);

        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e->e.getFechaDeLanzamiento()!= null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
                .forEach(e-> System.out.println(
                        "Temporada "+ e.getTemporada()+
                                "Episodio "+ e.getTitulo()+
                                "Fecha de Lanzamiento " + e.getFechaDeLanzamiento().format(dtf)
                ));*/

        //Busca episodios por pedazos  del titulo
        /*System.out.println("Search");
        var search = teclado.nextLine();
        Optional<Episodio> episodioBusqueda = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(search.toUpperCase()))
                .findFirst();
        if (episodioBusqueda.isPresent()){
            System.out.println(episodioBusqueda.get());
        }else{
            System.out.println("No hay resultados");
        }*/
        Map<Integer, Double> evalucionesPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evalucionesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println(est.getAverage());
        System.out.println(est.getMax());
        System.out.println(est.getMin());
    }
}
