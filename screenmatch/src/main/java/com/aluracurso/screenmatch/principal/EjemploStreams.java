package com.aluracurso.screenmatch.principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void muestraEjemplo(){
        List<String>nombres = Arrays.asList("brenda","lusi","fernanda","maria","eric");
        nombres.stream()
                .sorted()
                .limit(4)
                .forEach(System.out::println);
    }
}
