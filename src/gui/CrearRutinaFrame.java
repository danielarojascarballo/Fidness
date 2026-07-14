/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author daniela
 */
import excepciones.NombreRutinaVacioException;
import excepciones.RutinaVaciaException;
import modelo.*;
import servicio.GimnasioServicio;
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CrearRutinaFrame extends JFrame {

    private Cliente cliente;
    private GimnasioServicio servicio;
    private JTextField txtNombreRutina;
    private JComboBox<CategoriaEjercicio> comboCategorias;
    private JList<Ejercicio> listaDisponibles;
    private DefaultListModel<Ejercicio> modeloDisponibles;
    private JList<Ejercicio> listaSeleccionados;
    private DefaultListModel<Ejercicio> modeloSeleccionados;

    public CrearRutinaFrame(Cliente cliente, GimnasioServicio servicio) {
        this.cliente = cliente;
        this.servicio = servicio;
        initComponents();
    }

    private void initComponents() {
        setTitle("Crear Rutina");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel();
        panelSuperior.add(new JLabel("Nombre de la rutina:"));
        txtNombreRutina = new JTextField(15);
        panelSuperior.add(txtNombreRutina);
        comboCategorias = new JComboBox<>(servicio.listarCategorias().toArray(new CategoriaEjercicio[0]));
        comboCategorias.addActionListener(e -> cargarDisponibles());
        panelSuperior.add(new JLabel("Categoria:"));
        panelSuperior.add(comboCategorias);
        add(panelSuperior, BorderLayout.NORTH);

        modeloDisponibles = new DefaultListModel<>();
        listaDisponibles = new JList<>(modeloDisponibles);
        modeloSeleccionados = new DefaultListModel<>();
        listaSeleccionados = new JList<>(modeloSeleccionados);

        JPanel panelCentro = new JPanel(new GridLayout(1, 3, 10, 10));
        panelCentro.add(new JScrollPane(listaDisponibles));

        JPanel panelFlechas = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton btnAgregar = new JButton("Agregar >>");
        JButton btnQuitar = new JButton("<< Quitar");
        btnAgregar.addActionListener(e -> moverSeleccionado(listaDisponibles, modeloSeleccionados));
        btnQuitar.addActionListener(e -> moverSeleccionado(listaSeleccionados, modeloDisponibles));
        panelFlechas.add(btnAgregar);
        panelFlechas.add(btnQuitar);
        panelCentro.add(panelFlechas);

        panelCentro.add(new JScrollPane(listaSeleccionados));
        add(panelCentro, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Guardar y Exportar Rutina");
        btnGuardar.addActionListener(e -> guardarRutina());
        add(btnGuardar, BorderLayout.SOUTH);

        cargarDisponibles();
    }

    private void cargarDisponibles() {
        modeloDisponibles.clear();
        CategoriaEjercicio cat = (CategoriaEjercicio) comboCategorias.getSelectedItem();
        if (cat == null) return;
        for (Ejercicio e : servicio.listarPorCategoria(cat)) {
            if (!modeloSeleccionados.contains(e)) {
                modeloDisponibles.addElement(e);
            }
        }
    }

    private void moverSeleccionado(JList<Ejercicio> origen, DefaultListModel<Ejercicio> destino) {
        Ejercicio e = origen.getSelectedValue();
        if (e != null) {
            destino.addElement(e);
            ((DefaultListModel<Ejercicio>) origen.getModel()).removeElement(e);
        }
    }

    private void guardarRutina() {
        try {
            String nombre = txtNombreRutina.getText().trim();
            if (nombre.isEmpty()) {
                throw new NombreRutinaVacioException("El nombre de la rutina es obligatorio.");
            }

            Rutina rutina = new Rutina(cliente.getRutinas().size() + 1, nombre, cliente);
            for (int i = 0; i < modeloSeleccionados.size(); i++) {
                rutina.agregarEjercicio(modeloSeleccionados.get(i));
            }

            String detalle = rutina.generarDetalleExportable();
            cliente.crearRutina(rutina);
            exportarArchivo(rutina, detalle);

            JOptionPane.showMessageDialog(this, "Rutina guardada y exportada correctamente.");
            dispose();

        } catch (NombreRutinaVacioException | RutinaVaciaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarArchivo(Rutina rutina, String detalle) {
        String nombreArchivo = "rutina_" + rutina.getNombre().replaceAll("\\s+", "_") + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.print(detalle);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo generar el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
