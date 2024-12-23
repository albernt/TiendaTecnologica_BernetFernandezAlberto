package tienda;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    private static final String DB_PATH = "tienda.db"; // Ruta de la bd

    public static void main(String[] args) {
        // Llamamos a metodo de creacion de la base de datos
        crearBaseDeDatos();

        // Y al metodo para insertar la info del json a la bd
        insertarProductosDesdeJSON();

        // Crear y mostrar la ventana
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new Tienda();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }


    private static void crearBaseDeDatos() {
        String url = "jdbc:sqlite:" + DB_PATH;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Conexión exitosa a la base de datos.");
                Statement stmt = conn.createStatement();


                String sqlCategorias = """
                        CREATE TABLE IF NOT EXISTS categorias (
                            id INTEGER PRIMARY KEY,
                            nombre TEXT NOT NULL
                        );
                        """;
                stmt.execute(sqlCategorias);


                String sqlProductos = """
                        CREATE TABLE IF NOT EXISTS productos (
                            id INTEGER PRIMARY KEY,
                            nombre TEXT NOT NULL,
                            precio REAL,
                            descripcion TEXT,
                            categoria_id INTEGER,
                            FOREIGN KEY(categoria_id) REFERENCES categorias(id)
                        );
                        """;
                stmt.execute(sqlProductos);


                String sqlUsuarios = """
                        CREATE TABLE IF NOT EXISTS usuarios (
                            id INTEGER PRIMARY KEY,
                            nombre TEXT NOT NULL,
                            email TEXT NOT NULL,
                            direccion TEXT
                        );
                        """;
                stmt.execute(sqlUsuarios);


                String sqlHistorialCompras = """
                        CREATE TABLE IF NOT EXISTS historial_compras (
                            id INTEGER PRIMARY KEY,
                            usuario_id INTEGER,
                            producto_id INTEGER,
                            cantidad INTEGER,
                            fecha TEXT,
                            FOREIGN KEY(usuario_id) REFERENCES usuarios(id),
                            FOREIGN KEY(producto_id) REFERENCES productos(id)
                        );
                        """;
                stmt.execute(sqlHistorialCompras);


                String sqlCaracteristicas = """
                        CREATE TABLE IF NOT EXISTS caracteristicas (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            producto_id INTEGER,
                            clave TEXT,
                            valor TEXT,
                            FOREIGN KEY(producto_id) REFERENCES productos(id)
                        );
                        """;
                stmt.execute(sqlCaracteristicas);


                String sqlImagenes = """
                        CREATE TABLE IF NOT EXISTS imagenes (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            producto_id INTEGER,
                            imagen TEXT,
                            FOREIGN KEY(producto_id) REFERENCES productos(id)
                        );
                        """;
                stmt.execute(sqlImagenes);


                String sqlInventario = """
                        CREATE TABLE IF NOT EXISTS inventario (
                            producto_id INTEGER PRIMARY KEY,
                            cantidad INTEGER,
                            FOREIGN KEY(producto_id) REFERENCES productos(id)
                        );
                        """;
                stmt.execute(sqlInventario);

                System.out.println("Tablas creadas con éxito.");
            }
        } catch (Exception e) {
            System.out.println("Error al crear las tablas.");
            e.printStackTrace();
        }
    }


    public static void insertarProductosDesdeJSON() {
        String url = "jdbc:sqlite:tienda.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);

            // Cargamos el archivo JSON como recurso

            InputStream inputStream = Main.class.getResourceAsStream("/tienda/tienda.json");
            if (inputStream == null) {
                throw new FileNotFoundException("No se encontró el archivo tienda.json en el directorio esperado.");
            }
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            JSONObject tienda = (JSONObject) jsonObject.get("tienda");
            JSONArray categorias = (JSONArray) tienda.get("categorias");


            String sqlInsertCategoria = "INSERT INTO categorias (id, nombre) VALUES (?, ?)";
            try (PreparedStatement stmtCategoria = conn.prepareStatement(sqlInsertCategoria)) {
                for (Object catObj : categorias) {
                    JSONObject categoria = (JSONObject) catObj;
                    int categoriaId = ((Long) categoria.get("id")).intValue();
                    String categoriaNombre = (String) categoria.get("nombre");

                    stmtCategoria.setInt(1, categoriaId);
                    stmtCategoria.setString(2, categoriaNombre);
                    stmtCategoria.executeUpdate();
                }
            }


            String sqlInsertProducto = "INSERT INTO productos (id, nombre, precio, descripcion, categoria_id) VALUES (?, ?, ?, ?, ?)";
            String sqlInsertCaracteristica = "INSERT INTO caracteristicas (producto_id, clave, valor) VALUES (?, ?, ?)";
            String sqlInsertImagen = "INSERT INTO imagenes (producto_id, imagen) VALUES (?, ?)";
            String sqlInsertInventario = "INSERT INTO inventario (producto_id, cantidad) VALUES (?, ?)";

            try (PreparedStatement stmtProducto = conn.prepareStatement(sqlInsertProducto);
                 PreparedStatement stmtCaracteristica = conn.prepareStatement(sqlInsertCaracteristica);
                 PreparedStatement stmtImagen = conn.prepareStatement(sqlInsertImagen);
                 PreparedStatement stmtInventario = conn.prepareStatement(sqlInsertInventario)) {

                for (Object catObj : categorias) {
                    JSONObject categoria = (JSONObject) catObj;
                    JSONArray productos = (JSONArray) categoria.get("productos");

                    for (Object prodObj : productos) {
                        JSONObject producto = (JSONObject) prodObj;

                        int productoId = ((Long) producto.get("id")).intValue();
                        String nombre = (String) producto.get("nombre");
                        double precio = (Double) producto.get("precio");
                        String descripcion = (String) producto.get("descripcion");
                        int categoriaId = ((Long) categoria.get("id")).intValue();


                        stmtProducto.setInt(1, productoId);
                        stmtProducto.setString(2, nombre);
                        stmtProducto.setDouble(3, precio);
                        stmtProducto.setString(4, descripcion);
                        stmtProducto.setInt(5, categoriaId);
                        stmtProducto.executeUpdate();


                        JSONObject caracteristicas = (JSONObject) producto.get("caracteristicas");
                        if (caracteristicas != null) {
                            for (Object clave : caracteristicas.keySet()) {
                                stmtCaracteristica.setInt(1, productoId);
                                stmtCaracteristica.setString(2, (String) clave);
                                stmtCaracteristica.setString(3, (String) caracteristicas.get(clave));
                                stmtCaracteristica.executeUpdate();
                            }
                        }


                        JSONArray imagenes = (JSONArray) producto.get("imagenes");
                        if (imagenes != null) {
                            for (Object img : imagenes) {
                                stmtImagen.setInt(1, productoId);
                                stmtImagen.setString(2, (String) img);
                                stmtImagen.executeUpdate();
                            }
                        }


                        Long inventario = (Long) producto.get("inventario");
                        if (inventario != null) {
                            stmtInventario.setInt(1, productoId);
                            stmtInventario.setInt(2, inventario.intValue());
                            stmtInventario.executeUpdate();
                        }
                    }
                }
            }



            conn.commit();
            System.out.println("Datos insertados correctamente desde el archivo JSON.");
        } catch (IOException | ParseException e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al insertar datos en la base de datos: " + e.getMessage());
        }
    }
}