/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author daniela
 */
import modelo.*;
import servicio.GimnasioServicio;
import javax.swing.*;
import java.awt.*;

public class MenuClienteFrame extends JFrame {

    private Cliente cliente;
    private GimnasioServicio servicio;
    private JComboBox<CategoriaEjercicio> comboCategorias;
    private DefaultListModel<Ejercicio> modeloEjercicios;
    private JList<Ejercicio> listaEjercicios;
    private JTextArea txtDetalle;

    public MenuClienteFrame(Cliente cliente, GimnasioServicio servicio) {
        this.cliente = cliente;
        this.servicio = servicio;
        initComponents();
    }

    private void initComponents() {
        setTitle("Fidness - Bienvenido " + cliente.getNombre());
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel();
        comboCategorias = new JComboBox<>(servicio.listarCategorias().toArray(new CategoriaEjercicio[0]));
        comboCategorias.addActionListener(e -> cargarEjerciciosPorCategoria());
        panelSuperior.add(new JLabel("Categoria:"));
        panelSuperior.add(comboCategorias);
        add(panelSuperior, BorderLayout.NORTH);

        modeloEjercicios = new DefaultListModel<>();
        listaEjercicios = new JList<>(modeloEjercicios);
        listaEjercicios.addListSelectionListener(e -> mostrarDetalle());

        txtDetalle = new JTextArea(6, 20);
        txtDetalle.setEditable(false);
        txtDetalle.setLineWrap(true);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(listaEjercicios), new JScrollPane(txtDetalle));
        split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);

        JButton btnCrearRutina = new JButton("Crear Rutina");
        btnCrearRutina.addActionListener(e -> new CrearRutinaFrame(cliente, servicio).setVisible(true));

        JButton btnMisRutinas = new JButton("Mis Rutinas");
        btnMisRutinas.addActionListener(e -> new MisRutinasFrame(cliente).setVisible(true));

        JButton btnCerrarSesion = new JButton("Cerrar Sesion");
        btnCerrarSesion.addActionListener(e -> {
            cliente.cerrarSesion();
            dispose();
            new LoginFrame().setVisible(true);
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCrearRutina);
        panelBotones.add(btnMisRutinas);
        panelBotones.add(btnCerrarSesion);
        add(panelBotones, BorderLayout.SOUTH);

        cargarEjerciciosPorCategoria();
    }

    private void cargarEjerciciosPorCategoria() {
        modeloEjercicios.clear();
        CategoriaEjercicio seleccionada = (CategoriaEjercicio) comboCategorias.getSelectedItem();
        if (seleccionada == null) return;
        for (Ejercicio e : servicio.listarPorCategoria(seleccionada)) {
            modeloEjercicios.addElement(e);
        }
        txtDetalle.setText("");
    }

    private void mostrarDetalle() {
        Ejercicio seleccionado = listaEjercicios.getSelectedValue();
        if (seleccionado != null) {
            txtDetalle.setText(
                "Nombre: " + seleccionado.getNombre() + "\n" +
                "Categoria: " + seleccionado.getCategoria().getNombre() + "\n" +
                "Descripcion: " + seleccionado.getDescripcion() + "\n" +
                "Instrucciones: " + seleccionado.getInstrucciones() + "\n" +
                "Series x Repeticiones: " + seleccionado.getSeries() + " x " + seleccionado.getRepeticiones()
            );
        }
    }
}
