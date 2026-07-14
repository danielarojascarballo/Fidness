/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author daniela
 */
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

    private List<Rutina> rutinas;

    public Cliente(int id, String nombre, String apellido, String correo,
                    String usuario, String contrasena) {
        super(id, nombre, apellido, correo, usuario, contrasena);
        this.rutinas = new ArrayList<>();
    }

    @Override
    public String obtenerRol() {
        return "Cliente";
    }

    public void consultarEjercicios() {
        System.out.println(getNombre() + " esta consultando el catalogo de ejercicios.");
    }

    public void crearRutina(Rutina rutina) {
        rutinas.add(rutina);
    }

    public List<Rutina> getRutinas() {
        return rutinas;
    }
}
