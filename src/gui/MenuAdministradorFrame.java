/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author daniela
 */

import excepciones.*;
import modelo.*;
import servicio.GimnasioServicio;
import javax.swing.*;
import java.awt.*;

public class MenuAdministradorFrame extends JFrame {

    private Administrador admin;
    private GimnasioServicio servicio;

    private DefaultListModel<Ejercicio> modeloEjercicios;
    private JList<Ejercicio> listaEjercicios;

    private DefaultListModel<Usuario> modeloUsuarios;
    private JList<Usuario> listaUsuarios;

    private DefaultListModel<CategoriaEjercicio> modeloCategorias;
    private JList<CategoriaEjercicio> listaCategorias;

    public MenuAdministradorFrame(Administrador admin, GimnasioServicio servicio) {
        this.admin = admin;
        this.servicio = servicio;
        initComponents();
    }

    private void initComponents() {
        setTitle("Panel de Administrador - " + admin.getNombre());
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Ejercicios", crearPanelEjercicios());
        tabs.addTab("Usuarios", crearPanelUsuarios());
        tabs.addTab("Categorias", crearPanelCategorias());
        add(tabs, BorderLayout.CENTER);

        JButton btnCerrarSesion = new JButton("Cerrar Sesion");
        btnCerrarSesion.addActionListener(e -> {
            admin.cerrarSesion();
            dispose();
            new LoginFrame().setVisible(true);
        });
        JPanel panelSur = new JPanel();
        panelSur.add(btnCerrarSesion);
        add(panelSur, BorderLayout.SOUTH);
    }

    private JPanel crearPanelEjercicios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        modeloEjercicios = new DefaultListModel<>();
        listaEjercicios = new JList<>(modeloEjercicios);
        panel.add(new JScrollPane(listaEjercicios), BorderLayout.CENTER);
        cargarEjercicios();

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        btnAgregar.addActionListener(e -> agregarEjercicio());
        btnEditar.addActionListener(e -> editarEjercicio());
        btnEliminar.addActionListener(e -> eliminarEjercicio());

        JPanel botones = new JPanel();
        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarEjercicios() {
        modeloEjercicios.clear();
        for (Ejercicio e : servicio.listarEjercicios()) modeloEjercicios.addElement(e);
    }

    private void agregarEjercicio() {
        JComboBox<CategoriaEjercicio> comboCategorias = new JComboBox<>(
                servicio.listarCategorias().toArray(new CategoriaEjercicio[0]));
        if (comboCategorias.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Primero debe existir al menos una categoria.");
            return;
        }
        JTextField txtNombre = new JTextField();
        JTextField txtDescripcion = new JTextField();
        JTextField txtInstrucciones = new JTextField();
        JTextField txtSeries = new JTextField();
        JTextField txtRepeticiones = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Categoria:")); panel.add(comboCategorias);
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Descripcion:")); panel.add(txtDescripcion);
        panel.add(new JLabel("Instrucciones:")); panel.add(txtInstrucciones);
        panel.add(new JLabel("Series:")); panel.add(txtSeries);
        panel.add(new JLabel("Repeticiones:")); panel.add(txtRepeticiones);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Agregar Ejercicio",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                if (txtNombre.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("El nombre del ejercicio es obligatorio");
                }
                int series = Integer.parseInt(txtSeries.getText().trim());
                int repeticiones = Integer.parseInt(txtRepeticiones.getText().trim());
                Ejercicio nuevo = new Ejercicio(servicio.siguienteIdEjercicio(),
                        txtNombre.getText().trim(), txtDescripcion.getText().trim(),
                        txtInstrucciones.getText().trim(), series, repeticiones,
                        (CategoriaEjercicio) comboCategorias.getSelectedItem());
                servicio.agregarEjercicio(nuevo);
                cargarEjercicios();
                JOptionPane.showMessageDialog(this, "Ejercicio agregado correctamente.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Series y repeticiones deben ser numeros.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarEjercicio() {
        Ejercicio seleccionado = listaEjercicios.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un ejercicio primero.");
            return;
        }
        JTextField txtNombre = new JTextField(seleccionado.getNombre());
        JTextField txtDescripcion = new JTextField(seleccionado.getDescripcion());
        JTextField txtInstrucciones = new JTextField(seleccionado.getInstrucciones());
        JTextField txtSeries = new JTextField(String.valueOf(seleccionado.getSeries()));
        JTextField txtRepeticiones = new JTextField(String.valueOf(seleccionado.getRepeticiones()));

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Descripcion:")); panel.add(txtDescripcion);
        panel.add(new JLabel("Instrucciones:")); panel.add(txtInstrucciones);
        panel.add(new JLabel("Series:")); panel.add(txtSeries);
        panel.add(new JLabel("Repeticiones:")); panel.add(txtRepeticiones);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Editar Ejercicio",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                int series = Integer.parseInt(txtSeries.getText().trim());
                int repeticiones = Integer.parseInt(txtRepeticiones.getText().trim());
                servicio.editarEjercicio(seleccionado, txtNombre.getText().trim(),
                        txtDescripcion.getText().trim(), txtInstrucciones.getText().trim(),
                        series, repeticiones, seleccionado.getCategoria());
                cargarEjercicios();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Series y repeticiones deben ser numeros.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarEjercicio() {
        Ejercicio seleccionado = listaEjercicios.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un ejercicio primero.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar " + seleccionado.getNombre() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            servicio.eliminarEjercicio(seleccionado);
            cargarEjercicios();
        }
    }

    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        modeloUsuarios = new DefaultListModel<>();
        listaUsuarios = new JList<>(modeloUsuarios);
        panel.add(new JScrollPane(listaUsuarios), BorderLayout.CENTER);
        cargarUsuarios();

        JButton btnCrear = new JButton("Crear Usuario");
        JButton btnConsultar = new JButton("Consultar");
        JButton btnEliminar = new JButton("Eliminar");
        btnCrear.addActionListener(e -> crearUsuario());
        btnConsultar.addActionListener(e -> consultarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());

        JPanel botones = new JPanel();
        botones.add(btnCrear);
        botones.add(btnConsultar);
        botones.add(btnEliminar);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarUsuarios() {
        modeloUsuarios.clear();
        for (Usuario u : servicio.listarUsuarios()) modeloUsuarios.addElement(u);
    }

    private void crearUsuario() {
        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtUsuario = new JTextField();
        JPasswordField txtContrasena = new JPasswordField();
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Cliente", "Administrador"});

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Apellido:")); panel.add(txtApellido);
        panel.add(new JLabel("Correo:")); panel.add(txtCorreo);
        panel.add(new JLabel("Usuario:")); panel.add(txtUsuario);
        panel.add(new JLabel("Contraseña:")); panel.add(txtContrasena);
        panel.add(new JLabel("Tipo:")); panel.add(comboTipo);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Crear Usuario",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                if (txtUsuario.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Nombre y usuario son obligatorios.");
                }
                int id = servicio.siguienteIdUsuario();
                String contrasena = new String(txtContrasena.getPassword());
                Usuario nuevo;
                if (comboTipo.getSelectedItem().equals("Administrador")) {
                    nuevo = new Administrador(id, txtNombre.getText().trim(), txtApellido.getText().trim(),
                            txtCorreo.getText().trim(), txtUsuario.getText().trim(), contrasena);
                } else {
                    nuevo = new Cliente(id, txtNombre.getText().trim(), txtApellido.getText().trim(),
                            txtCorreo.getText().trim(), txtUsuario.getText().trim(), contrasena);
                }
                servicio.crearUsuario(nuevo);
                cargarUsuarios();
                JOptionPane.showMessageDialog(this, "Usuario creado correctamente.");
            } catch (UsuarioExistenteException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void consultarUsuario() {
        String buscado = JOptionPane.showInputDialog(this, "Nombre o usuario a buscar:");
        if (buscado == null || buscado.trim().isEmpty()) return;
        try {
            Usuario u = servicio.consultarUsuario(buscado.trim());
            JOptionPane.showMessageDialog(this, u.toString());
        } catch (UsuarioNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUsuario() {
        Usuario seleccionado = listaUsuarios.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario primero.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar a " + seleccionado.getNombre() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            servicio.eliminarUsuario(seleccionado);
            cargarUsuarios();
        }
    }

    private JPanel crearPanelCategorias() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        modeloCategorias = new DefaultListModel<>();
        listaCategorias = new JList<>(modeloCategorias);
        panel.add(new JScrollPane(listaCategorias), BorderLayout.CENTER);
        cargarCategorias();

        JButton btnCrear = new JButton("Crear");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        btnCrear.addActionListener(e -> crearCategoria());
        btnEditar.addActionListener(e -> editarCategoria());
        btnEliminar.addActionListener(e -> eliminarCategoria());

        JPanel botones = new JPanel();
        botones.add(btnCrear);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarCategorias() {
        modeloCategorias.clear();
        for (CategoriaEjercicio c : servicio.listarCategorias()) modeloCategorias.addElement(c);
    }

    private void crearCategoria() {
        JTextField txtNombre = new JTextField();
        JTextField txtDescripcion = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Descripcion:")); panel.add(txtDescripcion);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Crear Categoria",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                if (txtNombre.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("El nombre es obligatorio.");
                }
                CategoriaEjercicio nueva = new CategoriaEjercicio(servicio.siguienteIdCategoria(),
                        txtNombre.getText().trim(), txtDescripcion.getText().trim());
                servicio.crearCategoria(nueva);
                cargarCategorias();
            } catch (CategoriaExistenteException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarCategoria() {
        CategoriaEjercicio seleccionada = listaCategorias.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoria primero.");
            return;
        }
        JTextField txtNombre = new JTextField(seleccionada.getNombre());
        JTextField txtDescripcion = new JTextField(seleccionada.getDescripcion());
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Descripcion:")); panel.add(txtDescripcion);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Editar Categoria",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            servicio.editarCategoria(seleccionada, txtNombre.getText().trim(), txtDescripcion.getText().trim());
            cargarCategorias();
        }
    }

    private void eliminarCategoria() {
        CategoriaEjercicio seleccionada = listaCategorias.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoria primero.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar " + seleccionada.getNombre() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                servicio.eliminarCategoria(seleccionada);
                cargarCategorias();
            } catch (CategoriaConEjerciciosException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}