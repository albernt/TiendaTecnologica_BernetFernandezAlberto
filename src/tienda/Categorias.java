package tienda;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


}
