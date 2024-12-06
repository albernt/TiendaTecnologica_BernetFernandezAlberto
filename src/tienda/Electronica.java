package tienda;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.*;

public class Electronica extends JFrame {

    public Electronica() {
        setTitle("Productos de Electrónica");

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
            logoIcon = new ImageIcon(getClass().getResource("/tienda/logo.png")); // Ruta relativa al paquete
            Image scaledImage = logoIcon.getImage().getScaledInstance(250, 180, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledImage);
        } catch (NullPointerException e) {
            System.err.println("No se pudo cargar el logo. Verifica la ruta y la ubicación del archivo.");
        }

        if (logoIcon != null) {
            logoLabel.setIcon(logoIcon); // Asignar el icono al JLabel
        } else {
            logoLabel.setText("Logo no encontrado");
            logoLabel.setForeground(Color.RED);
        }

        logoLabel.setBounds(10, 50, 260, 160);
        logoLabel.setBackground(Color.BLACK);
        logoLabel.setOpaque(true);
        logoLabel.setBorder(new LineBorder(Color.white, 5, true));

        add(logoLabel);

        // Botón productos (se puede modificar si quieres ir a la categoría de productos generales)
        JButton productosButton = crearBotonConEfectos(
                "Productos",
                Color.DARK_GRAY,
                Color.WHITE,
                Color.LIGHT_GRAY, // Color al pasar el ratón
                Color.LIGHT_GRAY  // Color al presionar
        );
        productosButton.setBounds(windowWidth - (2 * 150 + 10 + 20), 20, 150, 40);
        add(productosButton);

        // Botón usuarios
        JButton usuariosButton = crearBotonConEfectos(
                "Usuarios",
                Color.DARK_GRAY,
                Color.WHITE,
                Color.LIGHT_GRAY,
                Color.LIGHT_GRAY
        );
        usuariosButton.setBounds(windowWidth - (150 + 20), 20, 150, 40);
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

        // Llamar al método para cargar los productos de electrónica
        obtenerProductos("Electrónica");

        // Fuerza la actualización del contenedor principal
        revalidate();
        repaint();
    }

    // Método para crear botones estilizados
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
                boton.setBackground(colorPresionado); // Cambiar color al presionar
            } else if (boton.getModel().isRollover()) {
                boton.setBackground(colorHover); // Cambiar color al pasar el ratón
            } else {
                boton.setBackground(fondo); // Restaurar color original
            }
        });

        return boton;
    }

    // Método para obtener los productos de la categoría "Electrónica" desde la base de datos
    private void obtenerProductos(String categoria) {
        String sql = "SELECT nombre, precio FROM productos WHERE categoria = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tienda.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria); // Establecer la categoría (Electrónica)
            ResultSet rs = stmt.executeQuery();

            // Panel contenedor para los productos
            JPanel contenedorProductos = new JPanel();
            contenedorProductos.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Alineación centrada
            contenedorProductos.setBackground(Color.BLACK); // Fondo negro para el contenedor

            // Recorrer los resultados y crear un panel para cada producto
            while (rs.next()) {
                String nombreProducto = rs.getString("nombre");
                double precioProducto = rs.getDouble("precio");

                // Crear un JPanel para cada producto con borde blanco
                JPanel productoPanel = new JPanel();
                productoPanel.setBackground(Color.BLACK);
                productoPanel.setBorder(new LineBorder(Color.WHITE, 2)); // Borde blanco
                productoPanel.setLayout(new BorderLayout());

                // Crear un JLabel con el nombre del producto y el precio
                JLabel productoLabel = new JLabel("<html><b>" + nombreProducto + "</b><br/>Precio: $" + precioProducto + "</html>", SwingConstants.CENTER);
                productoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                productoLabel.setForeground(Color.WHITE);

                productoPanel.add(productoLabel, BorderLayout.CENTER);

                // Establecer tamaño cuadrado para cada panel
                productoPanel.setPreferredSize(new Dimension(150, 150)); // Tamaño de cada panel

                // Agregar el panel del producto al contenedor
                contenedorProductos.add(productoPanel);
            }

            // Agregar el contenedor de productos al JFrame
            JScrollPane scrollPane = new JScrollPane(contenedorProductos);
            scrollPane.setBounds(0, 250, getWidth(), getHeight() - 350);
            add(scrollPane);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
