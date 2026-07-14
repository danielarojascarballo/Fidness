/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author daniela
 */
import excepciones.RutinaVaciaException;
import modelo.Cliente;
import modelo.Rutina;
import javax.swing.*;
import java.awt.*;

public class MisRutinasFrame extends JFrame {

    private Cliente cliente;
    private JList<Rutina> listaRutinas;
    private DefaultListModel<Rutina> modelo;
    private JTextArea txtDetalle;

    public MisRutinasFrame(Cliente cliente) {
        this.cliente = cliente;
        initComponents();
    }

    private void initComponents() {
        setTitle("Mis Rutinas - " + cliente.getNombre());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modelo = new DefaultListModel<>();
        for (Rutina r : cliente.getRutinas()) {
            modelo.addElement(r);
        }
        listaRutinas = new JList<>(modelo);
        listaRutinas.addListSelectionListener(e -> mostrarDetalle());

        txtDetalle = new JTextArea();
        txtDetalle.setEditable(false);

        if (modelo.isEmpty()) {
            add(new JLabel("No tienes rutinas guardadas aun.", SwingConstants.CENTER), BorderLayout.CENTER);
        } else {
            JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                    new JScrollPane(listaRutinas), new JScrollPane(txtDetalle));
            split.setResizeWeight(0.4);
            add(split, BorderLayout.CENTER);
        }
    }

    private void mostrarDetalle() {
        Rutina r = listaRutinas.getSelectedValue();
        if (r != null) {
            try {
                txtDetalle.setText(r.generarDetalleExportable());
            } catch (RutinaVaciaException ex) {
                txtDetalle.setText(ex.getMessage());
            }
        }
    }
}
