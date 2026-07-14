/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author daniela
 */
public class Administrador extends Usuario {

    public Administrador(int id, String nombre, String apellido, String correo,
                          String usuario, String contrasena) {
        super(id, nombre, apellido, correo, usuario, contrasena);
    }

    @Override
    public String obtenerRol() {
        return "Administrador";
    }

    public void gestionarUsuarios() {
        System.out.println(getNombre() + " esta gestionando usuarios.");
    }

    public void gestionarEjercicios() {
        System.out.println(getNombre() + " esta gestionando el catalogo de ejercicios.");
    }

    public void gestionarCategorias() {
        System.out.println(getNombre() + " esta gestionando las categorias.");
    }
}