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

        // Logo de la tienda
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Cargar la imagen del logo
        ImageIcon logoIcon = null;
        try {
            logoIcon = new ImageIcon(getClass().getResource("/tienda/logo.png"));
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

        // Botones estilizados
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

// Acción para el botón Usuarios
        usuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear una nueva instancia de la clase Usuarios
                Usuarios usuariosFrame = new Usuarios();

                // Hacerla visible
                usuariosFrame.setVisible(true);

                // Cerrar la ventana actual (la de Tienda)
                Usuarios.this.dispose();
            }
        });

        add(usuariosButton);

        // Botón home
        ImageIcon homeIcon = new ImageIcon(getClass().getResource("/tienda/home.png"));
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
            dispose(); // Cerrar la ventana actual
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

        // Cargar imágenes de perfil y añadir MouseListener
        JLabel perfil1Label = crearImagenPerfil("/tienda/perfil1.jpeg", 1);
        perfil1Label.setBounds(windowWidth / 2 - 250, windowHeight / 2 - 100, 200, 200);  // Ajuste hacia la izquierda
        add(perfil1Label);

        JLabel perfil2Label = crearImagenPerfil("/tienda/perfil2.jpeg", 2);
        perfil2Label.setBounds(windowWidth / 2 + 50, windowHeight / 2 - 100, 200, 200);  // Ajuste hacia la derecha
        add(perfil2Label);




        // Fuerza la actualización del contenedor principal
        revalidate();
        repaint();
    }

    // Método para crear los botones con efectos
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
                // Cargar y mostrar el historial de compras del usuario
                String historial = obtenerHistorialCompras(userId);
                JOptionPane.showMessageDialog(null, historial, "Historial de Compras", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return perfilLabel;
    }

    // Método para obtener el historial de compras de un usuario
    private String obtenerHistorialCompras(int userId) {
        String historial = "No se encontró historial de compras.";
        String query = "SELECT producto_id FROM historial_compras WHERE usuario_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tienda.db");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);  // Establece el ID de usuario para la consulta

            try (ResultSet rs = stmt.executeQuery()) {
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    // Cambié "compra" por "producto_id" ya que esa es la columna que mencionas
                    sb.append("Producto ID: ").append(rs.getString("producto_id")).append("\n");
                }
                // Si hay resultados, los muestra, si no, permanece el mensaje predeterminado
                historial = sb.length() > 0 ? sb.toString() : historial;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historial;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Usuarios usuariosFrame = new Usuarios();
            usuariosFrame.setVisible(true);
        });
    }
}
