/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author daniela
 */
import excepciones.CredencialesInvalidasException;
import modelo.*;
import servicio.GimnasioServicio;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JLabel lblMensaje;
    private GimnasioServicio servicio;

    public LoginFrame() {
        servicio = new GimnasioServicio();
        initComponents();
    }

    private void initComponents() {
        setTitle("Fidness - Iniciar Sesion");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Usuario:"), gbc);
        txtUsuario = new JTextField(15);
        gbc.gridx = 1;
        add(txtUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Contraseña:"), gbc);
        txtContrasena = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtContrasena, gbc);

        btnIngresar = new JButton("Iniciar Sesion");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(btnIngresar, gbc);

        lblMensaje = new JLabel(" ");
        gbc.gridy = 3;
        add(lblMensaje, gbc);

        btnIngresar.addActionListener(e -> intentarLogin());
    }

    private void intentarLogin() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        btnIngresar.setEnabled(false);
        lblMensaje.setForeground(Color.BLUE);
        lblMensaje.setText("Validando credenciales...");

        SwingWorker<Usuario, Void> worker = new SwingWorker<>() {
            CredencialesInvalidasException errorCredenciales = null;

            @Override
            protected Usuario doInBackground() throws Exception {
                try {
                    return servicio.iniciarSesion(usuario, contrasena);
                } catch (CredencialesInvalidasException ex) {
                    errorCredenciales = ex;
                    return null;
                }
            }

            @Override
            protected void done() {
                btnIngresar.setEnabled(true);
                if (errorCredenciales != null) {
                    lblMensaje.setForeground(Color.RED);
                    lblMensaje.setText(errorCredenciales.getMessage());
                    return;
                }
                try {
                    Usuario u = get();
                    dispose();
                    
                    if (u instanceof Administrador) {
                        new MenuAdministradorFrame((Administrador) u, servicio).setVisible(true);
                    } else if (u instanceof Cliente) {
                        new MenuClienteFrame((Cliente) u, servicio).setVisible(true);
                    }
                } catch (Exception ex) {
                    lblMensaje.setForeground(Color.RED);
                    lblMensaje.setText("Error inesperado: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }
}
