package tienda;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Categorias extends JFrame {

    public Categorias() {
        setTitle("Mi tienda to guapa");

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
        add(productosButton);

        JButton usuariosButton = crearBotonConEfectos(
                "Usuarios",
                Color.DARK_GRAY,
                Color.WHITE,
                Color.LIGHT_GRAY,
                Color.LIGHT_GRAY
        );
        usuariosButton.setBounds(windowWidth - (buttonWidth + 20), 20, buttonWidth, buttonHeight);
        add(usuariosButton);

        ImageIcon homeIcon = new ImageIcon(getClass().getResource("/tienda/home.png"));

        Image scaledHomeImage = homeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Tamaño más pequeño
        homeIcon = new ImageIcon(scaledHomeImage);

        JButton homeButton = new JButton(homeIcon);
        homeButton.setBounds(windowWidth - (2 * buttonWidth + buttonSpacing + 20) - 40, 25, 30, 30);

        homeButton.setBorder(null);
        homeButton.setContentAreaFilled(false);
        homeButton.setFocusPainted(false);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí debes agregar la acción para volver a la tienda
                // Suponiendo que la tienda sea otra ventana (JFrame)
                // Si ya tienes una instancia de la tienda, simplemente hazla visible
                Tienda tienda = new Tienda();  // Crea una nueva instancia de la tienda
                tienda.setVisible(true); // Mostrar la ventana Tienda
                dispose(); // Cerrar la ventana actual
            }
        });

        // Agregar el botón a la ventana
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

        // Llamar al método para cargar las categorías
        obtenerCategorias();

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

        // ChangeListener para modificar colores según el estado del boton
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

    // Método para obtener las categorías desde la base de datos y crear los JLabel
    // Método para obtener las categorías desde la base de datos y crear los JLabel dentro de paneles cuadrados
    // Método para obtener las categorías desde la base de datos y crear los JLabel dentro de paneles cuadrados
    private void obtenerCategorias() {
        String sql = "SELECT nombre FROM categorias";  // Asegúrate de que la tabla 'categorias' existe
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tienda.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Panel contenedor para las categorías
            JPanel contenedorCategorias = new JPanel();
            contenedorCategorias.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Alineación centrada y espaciado entre los paneles
            contenedorCategorias.setBackground(Color.BLACK); // Fondo negro para el contenedor

            // Recorrer los resultados y crear los cuadrados para cada categoría
            while (rs.next()) {
                String categoriaNombre = rs.getString("nombre");

                // Crear un JPanel para cada categoría con borde blanco y forma cuadrada
                JPanel categoriaPanel = new JPanel();
                categoriaPanel.setBackground(Color.BLACK);
                categoriaPanel.setBorder(new LineBorder(Color.WHITE, 2)); // Borde blanco
                categoriaPanel.setLayout(new BorderLayout()); // Usamos BorderLayout para centrar el texto

                // Crear un JLabel con el nombre de la categoría
                JLabel categoriaLabel = new JLabel(categoriaNombre, SwingConstants.CENTER);
                categoriaLabel.setFont(new Font("Arial", Font.PLAIN, 18));
                categoriaLabel.setForeground(Color.WHITE);

                categoriaPanel.add(categoriaLabel, BorderLayout.CENTER); // Centrar el texto en el panel

                // Establecer tamaño cuadrado para cada panel
                categoriaPanel.setPreferredSize(new Dimension(150, 150)); // Tamaño cuadrado de 150x150 píxeles

                // Agregar el panel de la categoría al contenedor
                contenedorCategorias.add(categoriaPanel);
            }

            // Agregar el contenedor de categorías al JFrame
            JScrollPane scrollPane = new JScrollPane(contenedorCategorias);
            scrollPane.setBounds(0, 250, getWidth(), getHeight() - 350); // Ajustar el tamaño y posición del contenedor
            add(scrollPane);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
