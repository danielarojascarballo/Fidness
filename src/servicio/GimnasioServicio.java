/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

/**
 *
 * @author daniela
 */
import excepciones.*;
import modelo.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GimnasioServicio {

    private List<Usuario> usuarios;
    private List<CategoriaEjercicio> categorias;
    private List<Ejercicio> ejercicios;

    public GimnasioServicio() {
        usuarios = new CopyOnWriteArrayList<>();
        categorias = new CopyOnWriteArrayList<>();
        ejercicios = new CopyOnWriteArrayList<>();

        
        CargaInicialThread carga = new CargaInicialThread();
        carga.start();
        try {
            carga.join(); // esperamos a que termine antes de mostrar la GUI
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    
    public Usuario iniciarSesion(String usuario, String contrasena)
            throws CredencialesInvalidasException, InterruptedException {
        Thread.sleep(400);
        for (Usuario u : usuarios) {
            if (u.iniciarSesion(usuario, contrasena)) return u;
        }
        throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
    }

    public synchronized void crearUsuario(Usuario nuevo) throws UsuarioExistenteException {
        for (Usuario u : usuarios) {
            if (u.getUsuario().equalsIgnoreCase(nuevo.getUsuario())) {
                throw new UsuarioExistenteException("El usuario ya existe");
            }
        }
        usuarios.add(nuevo);
    }

    public Usuario consultarUsuario(String nombreOUsuario) throws UsuarioNoEncontradoException {
        for (Usuario u : usuarios) {
            if (u.getUsuario().equalsIgnoreCase(nombreOUsuario) || u.getNombre().equalsIgnoreCase(nombreOUsuario)) {
                return u;
            }
        }
        throw new UsuarioNoEncontradoException("El usuario no existe");
    }

    public synchronized void eliminarUsuario(Usuario u) {
        usuarios.remove(u);
    }

    public List<Usuario> listarUsuarios() { return usuarios; }
    public int siguienteIdUsuario() { return usuarios.size() + 1; }

    
    public synchronized void crearCategoria(CategoriaEjercicio nueva) throws CategoriaExistenteException {
        for (CategoriaEjercicio c : categorias) {
            if (c.getNombre().equalsIgnoreCase(nueva.getNombre())) {
                throw new CategoriaExistenteException("La categoria ya existe");
            }
        }
        categorias.add(nueva);
    }

    public synchronized void editarCategoria(CategoriaEjercicio categoria, String nombre, String descripcion) {
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
    }

    public synchronized void eliminarCategoria(CategoriaEjercicio categoria) throws CategoriaConEjerciciosException {
        for (Ejercicio e : ejercicios) {
            if (e.getCategoria().getId() == categoria.getId()) {
                throw new CategoriaConEjerciciosException("No se puede eliminar una categoria con ejercicios activos");
            }
        }
        categorias.remove(categoria);
    }

    public List<CategoriaEjercicio> listarCategorias() { return categorias; }
    public int siguienteIdCategoria() { return categorias.size() + 1; }

    
    public synchronized void agregarEjercicio(Ejercicio e) {
        ejercicios.add(e);
    }

    public synchronized void editarEjercicio(Ejercicio e, String nombre, String descripcion, String instrucciones,
                                              int series, int repeticiones, CategoriaEjercicio categoria) {
        e.setNombre(nombre);
        e.setDescripcion(descripcion);
        e.setInstrucciones(instrucciones);
        e.setSeries(series);
        e.setRepeticiones(repeticiones);
        e.setCategoria(categoria);
    }

    public synchronized void eliminarEjercicio(Ejercicio e) {
        ejercicios.remove(e);
    }

    public List<Ejercicio> listarEjercicios() { return ejercicios; }

    public List<Ejercicio> listarPorCategoria(CategoriaEjercicio categoria) {
        List<Ejercicio> resultado = new CopyOnWriteArrayList<>();
        for (Ejercicio e : ejercicios) {
            if (e.getCategoria().getId() == categoria.getId()) resultado.add(e);
        }
        return resultado;
    }

    public int siguienteIdEjercicio() { return ejercicios.size() + 1; }

   
    private class CargaInicialThread extends Thread {
        @Override
        public void run() {
            usuarios.add(new Administrador(1, "Jeannette", "Carballo", "jeannette@fidness.com", "admin", "1234"));
            usuarios.add(new Cliente(2, "Daniela", "Rojas", "daniela@fidness.com", "dani", "abcd"));

            CategoriaEjercicio pierna = new CategoriaEjercicio(1, "Pierna", "Ejercicios de tren inferior");
            CategoriaEjercicio espalda = new CategoriaEjercicio(2, "Espalda", "Ejercicios de tren superior");
            CategoriaEjercicio pecho = new CategoriaEjercicio(3, "Pecho", "Ejercicios de empuje");
            categorias.add(pierna);
            categorias.add(espalda);
            categorias.add(pecho);

            ejercicios.add(new Ejercicio(1, "Sentadilla", "Ejercicio de piernas",
                    "Baja la cadera manteniendo la espalda recta", 4, 12, pierna));
            ejercicios.add(new Ejercicio(2, "Remo con barra", "Ejercicio de espalda",
                    "Jala la barra hacia el abdomen", 3, 10, espalda));
            ejercicios.add(new Ejercicio(3, "Press de banca", "Ejercicio de pecho",
                    "Empuja la barra hacia arriba", 4, 8, pecho));
        }
    }
}
