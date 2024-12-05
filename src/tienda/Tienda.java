package tienda;

import javax.swing.*;
import java.awt.*;

public class Tienda extends JFrame {

    public Tienda() {
        setTitle("Mi tienda tecnológica");

        int windowWidth = 1500;
        int windowHeight = 950;

        // Configuración de la ventana principal
        setSize(windowWidth, windowHeight);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(Color.BLACK);
    }
}
