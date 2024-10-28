package com.aluracurso.screenmatch;

public interface IConvierteDatos {
    <T> T obtenerDatos (String json, Class<T>clase);
}
