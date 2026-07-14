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

public abstract class Usuario implements Serializable {

    private int id;
    private String nombre;
    private String apellido;
    private String correo;
    private String usuario;
    private String contrasena;

    public Usuario(int id, String nombre, String apellido, String correo,
                    String usuario, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public boolean iniciarSesion(String usuarioIngresado, String contrasenaIngresada) {
        return this.usuario.equals(usuarioIngresado) && this.contrasena.equals(contrasenaIngresada);
    }

    public void cerrarSesion() {
        System.out.println("Sesion cerrada para el usuario: " + usuario);
    }

    public void cambiarContrasena(String nuevaContrasena) {
        this.contrasena = nuevaContrasena;
    }

    // POLIMORFISMO: cada subclase decide su propio rol
    public abstract String obtenerRol();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + usuario + ") - Rol: " + obtenerRol();
    }
}
