package tienda;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Categorias extends JFrame {

    public Categorias() {
        setTitle("Mi tienda tecnologica");

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

        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Categorias categoriasFrame = new Categorias();


                categoriasFrame.setVisible(true);


                Categorias.this.dispose();
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


                Categorias.this.dispose();
            }
        });

        add(usuariosButton);

        ImageIcon homeIcon = new ImageIcon(getClass().getResource("images/home.png"));

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
                Tienda tienda = new Tienda();  // Crea una nueva instancia de la tienda
                tienda.setVisible(true); // Mostrar la ventana Tienda
                dispose(); // Cerrar la ventana actual
            }
        });

        add(homeButton);

        // Añadir las imágenes centradas y con hover
        JLabel videojuegosLabel = crearImagenConHover("images/videojuegos.png", windowWidth / 4, windowHeight / 2);
        videojuegosLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Videojuegos videojuegos = new Videojuegos();
                videojuegos.setVisible(true);
                dispose();
            }
        });

        JLabel electronicaLabel = crearImagenConHover("images/electronica.png", 3 * windowWidth / 4, windowHeight / 2);
        electronicaLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Electronica electronica = new Electronica();
                electronica.setVisible(true);
                dispose();
            }
        });

        add(videojuegosLabel);
        add(electronicaLabel);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBounds(0, windowHeight - 100, windowWidth, 100);
        footerPanel.setBackground(Color.GRAY);
        add(footerPanel);

        JLabel footerLabel = new JLabel("© 2024 TECHNOSHOP. Todos los derechos reservados a Ilerna.");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(footerLabel);

        // Fuerza la actualización del contenedor principal
        revalidate();
        repaint();
    }

    // Método para crear las imágenes con hover
    private JLabel crearImagenConHover(String path, int x, int y) {
        JLabel label = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image image = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH); // Redimensionar
            label.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + path);
        }

        label.setBounds(x - 125, y - 125, 250, 250); // Centrar la imagen

        label.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambiar el cursor a manita al pasar el ratón

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                label.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor como manita al pasar el ratón
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Cursor por defecto al salir
            }
        });

        return label;
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
}
