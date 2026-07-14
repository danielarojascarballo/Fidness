/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author daniela
 */
import excepciones.RutinaVaciaException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Rutina implements Serializable {

    private int id;
    private String nombre;
    private Date fechaCreacion;
    private Cliente cliente;
    private List<Ejercicio> listaEjercicios;

    public Rutina(int id, String nombre, Cliente cliente) {
        this.id = id;
        this.nombre = nombre;
        this.cliente = cliente;
        this.fechaCreacion = new Date();
        this.listaEjercicios = new ArrayList<>();
    }

    public void agregarEjercicio(Ejercicio ejercicio) {
        listaEjercicios.add(ejercicio);
    }

    public void eliminarEjercicio(Ejercicio ejercicio) {
        listaEjercicios.remove(ejercicio);
    }

    public String generarDetalleExportable() throws RutinaVaciaException {
        if (listaEjercicios.isEmpty()) {
            throw new RutinaVaciaException("No se puede exportar una rutina vacia.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Rutina: ").append(nombre).append("\n");
        sb.append("Cliente: ").append(cliente.getNombre()).append(" ").append(cliente.getApellido()).append("\n");
        sb.append("Fecha: ").append(fechaCreacion).append("\n");
        sb.append("--------------------------------\n");
        for (Ejercicio e : listaEjercicios) {
            sb.append("- ").append(e.getNombre())
              .append(" | ").append(e.getSeries()).append(" series x ")
              .append(e.getRepeticiones()).append(" repeticiones")
              .append(" | Categoria: ").append(e.getCategoria().getNombre())
              .append("\n");
        }
        return sb.toString();
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public Cliente getCliente() { return cliente; }
    public List<Ejercicio> getListaEjercicios() { return listaEjercicios; }

    @Override
    public String toString() {
        return nombre + " (" + listaEjercicios.size() + " ejercicios)";
    }
}
