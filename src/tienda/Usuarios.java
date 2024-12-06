package tienda;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Usuarios extends JFrame {

    public Usuarios() {
        setTitle("Gestión de Usuarios");

        int windowWidth = 1500;
        int windowHeight = 950;

        // Configuración de la ventana principal
        setSize(windowWidth, windowHeight);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(Color.BLACK);
        setLocationRelativeTo(null);

        // Logo
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Cargar la imagen del logo
        ImageIcon logoIcon = null;
        try {
            logoIcon = new ImageIcon(getClass().getResource("images/logo.png"));
            Image scaledImage = logoIcon.getImage().getScaledInstance(250, 180, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledImage);
        } catch (NullPointerException e) {
            System.err.println("No se pudo cargar el logo. Verifica la ruta y la ubicación del archivo.");
        }

        if (logoIcon != null) {
            logoLabel.setIcon(logoIcon);
        } else {
            logoLabel.setText("Logo no encontrado");
            logoLabel.setForeground(Color.RED);
        }

        logoLabel.setBounds(10, 50, 260, 160);
        logoLabel.setBackground(Color.BLACK);
        logoLabel.setOpaque(true);
        logoLabel.setBorder(new LineBorder(Color.white, 5, true));

        add(logoLabel);

        // Botones
        int buttonWidth = 150;
        int buttonHeight = 40;
        int buttonSpacing = 10;

        JButton productosButton = crearBotonConEfectos(
                "Productos",
                Color.DARK_GRAY,
                Color.WHITE,
                Color.LIGHT_GRAY, // Color al pasar el ratón
                Color.LIGHT_GRAY  // Color al presionar
        );
        productosButton.setBounds(windowWidth - (2 * buttonWidth + buttonSpacing + 20), 20, buttonWidth, buttonHeight);
        // Acción para el botón Productos
        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Categorias categoriasFrame = new Categorias();


                categoriasFrame.setVisible(true);


                Usuarios.this.dispose();
            }
        });


        add(productosButton);

        JButton usuariosButton = crearBotonConEfectos(
                "Usuarios",
                Color.DARK_GRAY,
                Color.WHITE,
                Color.LIGHT_GRAY,
                Color.LIGHT_GRAY
        );
        usuariosButton.setBounds(windowWidth - (buttonWidth + 20), 20, buttonWidth, buttonHeight);


        usuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Usuarios usuariosFrame = new Usuarios();


                usuariosFrame.setVisible(true);


                Usuarios.this.dispose();
            }
        });

        add(usuariosButton);

        // Botón home
        ImageIcon homeIcon = new ImageIcon(getClass().getResource("images/home.png"));
        Image scaledHomeImage = homeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        homeIcon = new ImageIcon(scaledHomeImage);
        JButton homeButton = new JButton(homeIcon);
        homeButton.setBounds(windowWidth - (2 * 150 + 10 + 20) - 40, 25, 30, 30);

        homeButton.setBorder(null);
        homeButton.setContentAreaFilled(false);
        homeButton.setFocusPainted(false);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        homeButton.addActionListener(e -> {
            Tienda tienda = new Tienda();
            tienda.setVisible(true);
            dispose();
        });

        add(homeButton);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBounds(0, windowHeight - 100, windowWidth, 100);
        footerPanel.setBackground(Color.GRAY);
        add(footerPanel);

        JLabel footerLabel = new JLabel("© 2024 TECHNOSHOP. Todos los derechos reservados a Ilerna.");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(footerLabel);

        // Cargamos las imagenes de los perfiles y agregamos el MouseListener
        JLabel perfil1Label = crearImagenPerfil("images/perfil1.jpeg", 1);
        perfil1Label.setBounds(windowWidth / 2 - 250, windowHeight / 2 - 100, 200, 200);
        add(perfil1Label);

        JLabel perfil2Label = crearImagenPerfil("images/perfil2.jpeg", 2);
        perfil2Label.setBounds(windowWidth / 2 + 50, windowHeight / 2 - 100, 200, 200);
        add(perfil2Label);


        revalidate();
        repaint();
    }


    private JButton crearBotonConEfectos(String texto, Color fondo, Color textoColor, Color colorHover, Color colorPresionado) {
        JButton boton = new JButton(texto);
        boton.setBackground(fondo);
        boton.setForeground(textoColor);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setBorder(new LineBorder(Color.green, 2, true));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.getModel().addChangeListener(e -> {
            if (boton.getModel().isPressed()) {
                boton.setBackground(colorPresionado);
            } else if (boton.getModel().isRollover()) {
                boton.setBackground(colorHover);
            } else {
                boton.setBackground(fondo);
            }
        });

        return boton;
    }

    // Método para crear la imagen del perfil y añadir el MouseListener
    private JLabel crearImagenPerfil(String imagePath, int userId) {
        JLabel perfilLabel = new JLabel();
        ImageIcon perfilIcon = new ImageIcon(getClass().getResource(imagePath));
        Image perfilImage = perfilIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        perfilIcon = new ImageIcon(perfilImage);
        perfilLabel.setIcon(perfilIcon);

        perfilLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        perfilLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                String historial = obtenerHistorialCompras(userId);
                JOptionPane.showMessageDialog(null, historial, "Historial de Compras", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return perfilLabel;
    }



    //Inner join para sacar el nombre del producto usando el id del historial
    private String obtenerHistorialCompras(int userId) {
        String historial = "No se encontró historial de compras.";
        String query = "SELECT p.nombre "
                + "FROM historial_compras h "
                + "JOIN productos p ON h.producto_id = p.id "
                + "WHERE h.usuario_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tienda.db");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {

                    sb.append("Producto: ").append(rs.getString("nombre")).append("\n");
                }

                historial = sb.length() > 0 ? sb.toString() : historial;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historial;
    }





}
