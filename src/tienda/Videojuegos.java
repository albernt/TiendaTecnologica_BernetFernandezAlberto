package tienda;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.*;

public class Videojuegos extends JFrame {

    public Videojuegos() {
        setTitle("Productos de Videojuegos");

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

        // Botón productos
        JButton productosButton = crearBotonConEfectos(
                "Productos",
                Color.DARK_GRAY,
                Color.WHITE,
                Color.LIGHT_GRAY,
                Color.LIGHT_GRAY
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

        // Llamar al método para cargar los productos de videojuegos
        obtenerProductos("Videojuegos");

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
                boton.setBackground(colorPresionado);
            } else if (boton.getModel().isRollover()) {
                boton.setBackground(colorHover);
            } else {
                boton.setBackground(fondo);
            }
        });

        return boton;
    }

    // Método para obtener los productos de la categoría "Videojuegos" desde la base de datos
    // Método para obtener los productos de la categoría "Videojuegos" desde la base de datos
    private void obtenerProductos(String categoria) {
        String sql = "SELECT p.nombre, p.precio FROM productos p "
                + "JOIN categorias c ON p.categoria_id = c.id "
                + "WHERE c.nombre = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tienda.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asumimos que la categoría es el nombre de la categoría, como "Videojuegos"
            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();

            // Panel contenedor para los productos
            JPanel contenedorProductos = new JPanel();
            contenedorProductos.setLayout(new GridLayout(0, 3, 20, 20)); // Layout con filas automáticas y 3 columnas
            contenedorProductos.setBackground(Color.BLACK);
            contenedorProductos.setPreferredSize(new Dimension(1200, 800));

            // Recorrer los resultados y crear un panel para cada producto
            while (rs.next()) {
                String nombreProducto = rs.getString("nombre");
                double precioProducto = rs.getDouble("precio");

                // Crear panel para el producto con fondo blanco y bordes
                JPanel productoPanel = new JPanel();
                productoPanel.setBackground(Color.WHITE); // Fondo blanco
                productoPanel.setBorder(new LineBorder(Color.BLACK, 2)); // Borde negro
                productoPanel.setLayout(new BorderLayout());
                productoPanel.setPreferredSize(new Dimension(200, 200)); // Hacerlo cuadrado

                JLabel productoLabel = new JLabel("<html><b>" + nombreProducto + "</b><br/>Precio: $" + precioProducto + "</html>", SwingConstants.CENTER);
                productoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                productoLabel.setForeground(Color.BLACK);

                productoPanel.add(productoLabel, BorderLayout.CENTER);

                contenedorProductos.add(productoPanel);
            }

            // Crear un JScrollPane para agregar los productos si son muchos
            JScrollPane scrollPane = new JScrollPane(contenedorProductos);
            scrollPane.setBounds(0, 250, getWidth(), getHeight() - 350);
            add(scrollPane);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
