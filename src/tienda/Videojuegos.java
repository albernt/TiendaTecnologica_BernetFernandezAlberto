package tienda;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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


                Videojuegos.this.dispose();
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
                Videojuegos.this.dispose();
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


        obtenerProductos("Videojuegos");


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


    private void obtenerProductos(String categoria) {

        String sql = "SELECT p.id, p.nombre, p.precio FROM productos p "
                + "JOIN categorias c ON p.categoria_id = c.id "
                + "WHERE c.nombre = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tienda.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setString(1, "Videojuegos");
            ResultSet rs = stmt.executeQuery();


            JPanel contenedorProductos = new JPanel();
            contenedorProductos.setLayout(new GridLayout(0, 3, 20, 20));
            contenedorProductos.setBackground(Color.BLACK);
            contenedorProductos.setPreferredSize(new Dimension(1200, 800));


            while (rs.next()) {
                String nombreProducto = rs.getString("nombre");
                double precioProducto = rs.getDouble("precio");
                int idProducto = rs.getInt("id");


                JPanel productoPanel = new JPanel();
                productoPanel.setBackground(Color.WHITE); // Fondo blanco
                productoPanel.setBorder(new LineBorder(Color.BLACK, 2));
                productoPanel.setLayout(new BorderLayout());
                productoPanel.setPreferredSize(new Dimension(200, 200));

                JLabel productoLabel = new JLabel("<html><b>" + nombreProducto + "</b><br/>Precio: $" + precioProducto + "</html>", SwingConstants.CENTER);
                productoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                productoLabel.setForeground(Color.BLACK);

                productoPanel.add(productoLabel, BorderLayout.CENTER);


                JButton compraButton = new JButton("Comprar");
                compraButton.setBackground(Color.GREEN);
                compraButton.setForeground(Color.WHITE);
                compraButton.setFont(new Font("Arial", Font.BOLD, 30));
                compraButton.setFocusPainted(false);
                compraButton.setCursor(new Cursor(Cursor.HAND_CURSOR));


                compraButton.addActionListener(e -> {
                    añadirAlHistorial(idProducto);
                });

                productoPanel.add(compraButton, BorderLayout.SOUTH);

                contenedorProductos.add(productoPanel);
            }


            JScrollPane scrollPane = new JScrollPane(contenedorProductos);
            scrollPane.setBounds(0, 250, getWidth(), getHeight() - 350);
            add(scrollPane);

            // Agregar el MouseWheelListener para hacer que la pantalla baje más rápido
            scrollPane.addMouseWheelListener(e -> {
                int scrollAmount = e.getUnitsToScroll();
                int adjustedAmount = scrollAmount * 3;    // Ajuste la velocidad

                JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
                int newValue = verticalScrollBar.getValue() + adjustedAmount;

                // Asegurarse de que no se salga de los límites
                int maxValue = verticalScrollBar.getMaximum() - verticalScrollBar.getVisibleAmount();
                if (newValue < 0) newValue = 0;
                if (newValue > maxValue) newValue = maxValue;

                verticalScrollBar.setValue(newValue);
            });

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para añadir al historial de compras
    private void añadirAlHistorial(int idProducto) {

        String[] opciones = {"1", "2"};
        String idUsuario = (String) JOptionPane.showInputDialog(
                this,
                "¿Eres el usuario con ID 1 o 2?",
                "Selecciona tu ID de usuario",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        // Si el usuario cancela o no elige una opción, no hacer nada
        if (idUsuario == null) {
            return;
        }

        // Convertir el ID de usuario elegido a entero
        int usuarioId = Integer.parseInt(idUsuario);

        // Preparar la consulta para añadir al historial de compras
        String sql = "INSERT INTO historial_compras (producto_id, usuario_id, fecha) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tienda.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // parámetros en la consulta
            stmt.setInt(1, idProducto);
            stmt.setInt(2, usuarioId);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));


            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Producto añadido al historial de compras.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al añadir el producto al historial.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al añadir al historial de compras: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



}
