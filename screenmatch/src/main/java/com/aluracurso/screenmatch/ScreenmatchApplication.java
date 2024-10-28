package com.aluracurso.screenmatch;

import com.aluracurso.screenmatch.model.DatosEpisodio;
import com.aluracurso.screenmatch.model.DatosSerie;
import com.aluracurso.screenmatch.model.DatosTemporadas;
import com.aluracurso.screenmatch.principal.EjemploStreams;
import com.aluracurso.screenmatch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.muestraElMenu();
		/*EjemploStreams ejemploStreams=new EjemploStreams();
		ejemploStreams.muestraEjemplo();*/

	}
}
