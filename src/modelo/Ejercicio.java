/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author daniela
 */
import java.io.Serializable;

public class Ejercicio implements Serializable {

    private int id;
    private String nombre;
    private String descripcion;
    private String instrucciones;
    private int series;
    private int repeticiones;
    private CategoriaEjercicio categoria;

    public Ejercicio(int id, String nombre, String descripcion, String instrucciones,
                      int series, int repeticiones, CategoriaEjercicio categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.instrucciones = instrucciones;
        this.series = series;
        this.repeticiones = repeticiones;
        this.categoria = categoria;
    }

    public CategoriaEjercicio getCategoria() { return categoria; }
    public void setCategoria(CategoriaEjercicio categoria) { this.categoria = categoria; }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }
    public int getSeries() { return series; }
    public void setSeries(int series) { this.series = series; }
    public int getRepeticiones() { return repeticiones; }
    public void setRepeticiones(int repeticiones) { this.repeticiones = repeticiones; }

    @Override
    public String toString() {
        return nombre + " (" + categoria.getNombre() + ") - " + series + "x" + repeticiones;
    }
}
