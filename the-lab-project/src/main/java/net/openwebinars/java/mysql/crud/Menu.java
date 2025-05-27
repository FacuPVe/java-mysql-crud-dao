package net.openwebinars.java.mysql.crud;

import net.openwebinars.java.mysql.crud.dao.*;
import net.openwebinars.java.mysql.crud.model.*;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class Menu {

    private KeyboardReader reader;
    private EmpleadoDao empleadoDao;
    private ProductoDao productoDao;
    private CategoriaDao categoriaDao;

    public Menu() {
        reader = new KeyboardReader();
        
        empleadoDao = EmpleadoDaoImpl.getInstance();
        productoDao = ProductoDaoImpl.getInstance();
        categoriaDao = CategoriaDaoImpl.getInstance();
    }

    public void init() {
        int opcion;

        do {
            menuPrincipal();
            opcion = reader.nextInt();

            switch (opcion) {
                case 1:
                    menuEmpleados();
                    break;
                case 2:
                    menuProductos();
                    break;
                case 3:
                    menuCategorias();
                    break;
                case 0:
                    System.out.println("\nSaliendo del programa...\n");
                    break;
                default:
                    System.err.println("\nEl número introducido no se corresponde con una operación válida\n\n");
            }
        } while (opcion != 0);
    }

    private void menuPrincipal() {
        System.out.println("SISTEMA DE GESTIÓN");
        System.out.println("=================\n");
        System.out.println("\n-> Introduzca una opción de entre las siguientes\n");
        System.out.println("0: Salir");
        System.out.println("1: Gestión de Empleados");
        System.out.println("2: Gestión de Productos");
        System.out.println("3: Gestión de Categorías");
        System.out.print("\nOpción: ");
    }

    private void menuEmpleados() {
        int opcion;

        do {
            menu();
            opcion = reader.nextInt();

            switch (opcion) {
                case 1:
                    listAll();
                    break;
                case 2:
                    listById();
                    break;
                case 3:
                    insert();
                    break;
                case 4:
                    update();
                    break;
                case 5:
                    delete();
                    break;
                case 0:
                    System.out.println("\nVolviendo al menú principal...\n");
                    break;
                default:
                    System.err.println("\nEl número introducido no se corresponde con una operación válida\n\n");
            }
        } while (opcion != 0);
    }

    public void listAll() {
        System.out.println("\nLISTADO DE TODOS LOS EMPLEADOS");
        System.out.println("--------------------------------\n");

        try {
            List<Empleado> result = empleadoDao.getAll();

            if (result.isEmpty())
                System.out.println("No hay empleados registrados en la base de datos");
            else {
                printCabeceraTablaEmpleado();
                result.forEach(this::printEmpleado);
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("\n");
    }

    public void listById() {
        System.out.println("\nBÚSQUEDA DE EMPLEADOS POR ID");
        System.out.println("------------------------------\n");

        try {
            System.out.print("Introduzca el ID del empleado a buscar: ");
            int id = reader.nextInt();

            Empleado empleado = empleadoDao.getById(id);

            if (empleado == null)
                System.out.println("No hay empleados registrados en la base de datos con ese ID");
            else {
                printCabeceraTablaEmpleado();
                printEmpleado(empleado);
            }
            System.out.println("\n");
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
    }

    public void update() {
        System.out.println("\nACTUALIZACIÓN DE UN EMPLEADO");
        System.out.println("------------------------------\n");

        try {
            System.out.print("Introduzca el ID del empleado a buscar: ");
            int id = reader.nextInt();

            Empleado empleado = empleadoDao.getById(id);

            if (empleado == null)
                System.out.println("No hay empleados registrados en la base de datos con ese ID");
            else {
                printCabeceraTablaEmpleado();
                printEmpleado(empleado);
                System.out.println("\n");

                System.out.printf("Introduzca el nombre (sin apellidos) del empleado (%s): ", empleado.getNombre());
                String nombre = reader.nextLine();
                nombre = (nombre.isBlank()) ? empleado.getNombre() : nombre;

                System.out.printf("Introduzca los apellidos del empleado (%s): ", empleado.getApellidos());
                String apellidos = reader.nextLine();
                apellidos = (apellidos.isBlank()) ? empleado.getApellidos() : apellidos;

                System.out.printf("Introduzca la fecha de nacimiento del empleado (formato dd/MM/aaaa) (%s): ",
                        empleado.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                String strFechaNacimiento = reader.nextLine();
                LocalDate fechaNacimiento = (strFechaNacimiento.isBlank()) ? empleado.getFechaNacimiento() :
                         LocalDate.parse(strFechaNacimiento, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                System.out.printf("Introduzca el puesto del empleado (%s): ", empleado.getPuesto());
                String puesto = reader.nextLine();
                puesto = (puesto.isBlank()) ? empleado.getPuesto() : puesto;

                System.out.printf("Introduzca el email del nuevo empleado (%s): ", empleado.getEmail());
                String email = reader.nextLine();
                email = (email.isBlank()) ? empleado.getEmail() : email;

                empleado.setNombre(nombre);
                empleado.setApellidos(apellidos);
                empleado.setFechaNacimiento(fechaNacimiento);
                empleado.setPuesto(puesto);
                empleado.setEmail(email);

                empleadoDao.update(empleado);

                System.out.println("");
                System.out.printf("Empleado con ID %s actualizado", empleado.getId_empleado());
                System.out.println("");
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("");
    }

    public void delete() {
        System.out.println("\nBORRADO DE UN EMPLEADO");
        System.out.println("--------------------------\n");

        try {
            System.out.print("Introduzca el ID del empleado a borrar: ");
            int id = reader.nextInt();

            System.out.printf("¿Está usted seguro de querer eliminar el empleado con ID=%s? (s/n): ", id);
            String borrar = reader.nextLine();

            if (borrar.equalsIgnoreCase("s")) {
                empleadoDao.delete(id);
                System.out.printf("El empleado con ID %s se ha borrado\n", id);
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("");
    }

    private void printCabeceraTablaEmpleado() {
        System.out.printf("%2s %30s %8s %10s %25s", "ID", "NOMBRE", "FEC. NAC.", "PUESTO", "EMAIL");
        System.out.println("");
        IntStream.range(1, 100).forEach(x -> System.out.print("-"));
        System.out.println("\n");
    }

    private void printEmpleado(Empleado emp) {
        System.out.printf("%2s %30s %9s %10s %25s\n",
                emp.getId_empleado(),
                emp.getNombre() + " " + emp.getApellidos(),
                emp.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yy")),
                emp.getPuesto(),
                emp.getEmail());
    }

    private void printCabeceraTablaProducto() {
        System.out.printf("%2s %30s %10s %8s %8s", "ID", "NOMBRE", "PRECIO", "STOCK", "CATEGORIA");
        System.out.println("");
        IntStream.range(1, 100).forEach(x -> System.out.print("-"));
        System.out.println("\n");
    }

    private void printProducto(Producto prod) {
        System.out.printf("%2s %30s %10.2f %8d %8d\n",
                prod.getId_producto(),
                prod.getNombre(),
                prod.getPrecio(),
                prod.getStock(),
                prod.getId_categoria());
    }

    private void printCabeceraTablaCategoria() {
        System.out.printf("%2s %30s %50s", "ID", "NOMBRE", "DESCRIPCIÓN");
        System.out.println("");
        IntStream.range(1, 100).forEach(x -> System.out.print("-"));
        System.out.println("\n");
    }

    private void printCategoria(Categoria cat) {
        System.out.printf("%2s %30s %50s\n",
                cat.getId_categoria(),
                cat.getNombre(),
                cat.getDescripcion());
    }

    public void menu() {
        System.out.println("SISTEMA DE GESTIÓN DE EMPLEADOS");
        System.out.println("===============================\n");
        System.out.println("\n-> Introduzca una opción de entre las siguientes\n");
        System.out.println("0: Volver al menú principal");
        System.out.println("1: Listar todos los empleados");
        System.out.println("2: Listar un empleado por su ID");
        System.out.println("3: Insertar un nuevo empleado");
        System.out.println("4: Actualizar un empleado");
        System.out.println("5: Eliminar un empleado");
        System.out.print("\nOpción: ");
    }

    public void insert() {
        System.out.println("\nINSERCIÓN DE UN NUEVO EMPLEADO");
        System.out.println("------------------------------\n");

        System.out.print("Introduzca el nombre (sin apellidos) del empleado: ");
        String nombre = reader.nextLine();

        System.out.print("Introduzca los apellidos del empleado: ");
        String apellidos = reader.nextLine();

        System.out.print("Introduzca la fecha de nacimiento del empleado (formato dd/MM/aaaa): ");
        LocalDate fechaNacimiento = reader.nextLocalDate();

        System.out.print("Introduzca el puesto del empleado: ");
        String puesto = reader.nextLine();

        System.out.print("Introduzca el email del nuevo empleado: ");
        String email = reader.nextLine();

        try {
            empleadoDao.add(new Empleado(nombre, apellidos, fechaNacimiento, puesto, email));
            System.out.println("Nuevo empleado registrado");
        } catch (SQLException ex) {
            System.err.println("Error insertando el nuevo registro en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("");
    }

    private void menuProductos() {
        int opcion;

        do {
            menuProductosOpciones();
            opcion = reader.nextInt();

            switch (opcion) {
                case 1:
                    listAllProductos();
                    break;
                case 2:
                    listProductoById();
                    break;
                case 3:
                    insertProducto();
                    break;
                case 4:
                    updateProducto();
                    break;
                case 5:
                    deleteProducto();
                    break;
                case 6:
                    listProductosByCategoria();
                    break;
                case 0:
                    System.out.println("\nVolviendo al menú principal...\n");
                    break;
                default:
                    System.err.println("\nEl número introducido no se corresponde con una operación válida\n\n");
            }
        } while (opcion != 0);
    }

    private void menuProductosOpciones() {
        System.out.println("SISTEMA DE GESTIÓN DE PRODUCTOS");
        System.out.println("===============================\n");
        System.out.println("\n-> Introduzca una opción de entre las siguientes\n");
        System.out.println("0: Volver al menú principal");
        System.out.println("1: Listar todos los productos");
        System.out.println("2: Listar un producto por su ID");
        System.out.println("3: Insertar un nuevo producto");
        System.out.println("4: Actualizar un producto");
        System.out.println("5: Eliminar un producto");
        System.out.println("6: Listar productos por categoría");
        System.out.print("\nOpción: ");
    }

    private void listAllProductos() {
        System.out.println("\nLISTADO DE TODOS LOS PRODUCTOS");
        System.out.println("--------------------------------\n");

        try {
            List<Producto> result = productoDao.getAll();

            if (result.isEmpty())
                System.out.println("No hay productos registrados en la base de datos");
            else {
                printCabeceraTablaProducto();
                result.forEach(this::printProducto);
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("\n");
    }

    private void listProductoById() {
        System.out.println("\nBÚSQUEDA DE PRODUCTOS POR ID");
        System.out.println("------------------------------\n");

        try {
            System.out.print("Introduzca el ID del producto a buscar: ");
            int id = reader.nextInt();

            Producto producto = productoDao.getById(id);

            if (producto == null)
                System.out.println("No hay productos registrados en la base de datos con ese ID");
            else {
                printCabeceraTablaProducto();
                printProducto(producto);
            }
            System.out.println("\n");
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
    }

    private void insertProducto() {
        System.out.println("\nINSERCIÓN DE UN NUEVO PRODUCTO");
        System.out.println("------------------------------\n");

        System.out.print("Introduzca el nombre del producto: ");
        String nombre = reader.nextLine();

        System.out.print("Introduzca el precio del producto: ");
        double precio = reader.nextDouble();

        System.out.print("Introduzca el stock del producto: ");
        int stock = reader.nextInt();

        System.out.print("Introduzca el ID de la categoría: ");
        int idCategoria = reader.nextInt();

        try {
            productoDao.add(new Producto(nombre, precio, stock, idCategoria));
            System.out.println("Nuevo producto registrado");
        } catch (SQLException ex) {
            System.err.println("Error insertando el nuevo registro en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("");
    }

    private void updateProducto() {
        System.out.println("\nACTUALIZACIÓN DE UN PRODUCTO");
        System.out.println("------------------------------\n");

        try {
            System.out.print("Introduzca el ID del producto a actualizar: ");
            int id = reader.nextInt();

            Producto producto = productoDao.getById(id);

            if (producto == null)
                System.out.println("No hay productos registrados en la base de datos con ese ID");
            else {
                printCabeceraTablaProducto();
                printProducto(producto);
                System.out.println("\n");

                System.out.printf("Introduzca el nombre del producto (%s): ", producto.getNombre());
                String nombre = reader.nextLine();
                nombre = (nombre.isBlank()) ? producto.getNombre() : nombre;

                System.out.printf("Introduzca el precio del producto (%.2f): ", producto.getPrecio());
                String strPrecio = reader.nextLine();
                double precio = (strPrecio.isBlank()) ? producto.getPrecio() : Double.parseDouble(strPrecio);

                System.out.printf("Introduzca el stock del producto (%d): ", producto.getStock());
                String strStock = reader.nextLine();
                int stock = (strStock.isBlank()) ? producto.getStock() : Integer.parseInt(strStock);

                System.out.printf("Introduzca el ID de la categoría (%d): ", producto.getId_categoria());
                String strCategoria = reader.nextLine();
                int idCategoria = (strCategoria.isBlank()) ? producto.getId_categoria() : Integer.parseInt(strCategoria);

                producto.setNombre(nombre);
                producto.setPrecio(precio);
                producto.setStock(stock);
                producto.setId_categoria(idCategoria);

                productoDao.update(producto);

                System.out.println("");
                System.out.printf("Producto con ID %s actualizado", producto.getId_producto());
                System.out.println("");
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("");
    }

    private void deleteProducto() {
        System.out.println("\nBORRADO DE UN PRODUCTO");
        System.out.println("--------------------------\n");

        try {
            System.out.print("Introduzca el ID del producto a borrar: ");
            int id = reader.nextInt();

            System.out.printf("¿Está usted seguro de querer eliminar el producto con ID=%s? (s/n): ", id);
            String borrar = reader.nextLine();

            if (borrar.equalsIgnoreCase("s")) {
                productoDao.delete(id);
                System.out.printf("El producto con ID %s se ha borrado\n", id);
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("");
    }

    private void listProductosByCategoria() {
        System.out.println("\nLISTADO DE PRODUCTOS POR CATEGORÍA");
        System.out.println("----------------------------------\n");

        try {
            System.out.print("Introduzca el ID de la categoría: ");
            int categoriaId = reader.nextInt();

            List<Producto> result = productoDao.getByCategoria(categoriaId);

            if (result.isEmpty())
                System.out.println("No hay productos registrados en la base de datos para esa categoría");
            else {
                printCabeceraTablaProducto();
                result.forEach(this::printProducto);
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("\n");
    }

    private void menuCategorias() {
        int opcion;

        do {
            menuCategoriasOpciones();
            opcion = reader.nextInt();

            switch (opcion) {
                case 1:
                    listAllCategorias();
                    break;
                case 2:
                    listCategoriaById();
                    break;
                case 3:
                    insertCategoria();
                    break;
                case 4:
                    updateCategoria();
                    break;
                case 5:
                    deleteCategoria();
                    break;
                case 0:
                    System.out.println("\nVolviendo al menú principal...\n");
                    break;
                default:
                    System.err.println("\nEl número introducido no se corresponde con una operación válida\n\n");
            }
        } while (opcion != 0);
    }

    private void menuCategoriasOpciones() {
        System.out.println("SISTEMA DE GESTIÓN DE CATEGORÍAS");
        System.out.println("================================\n");
        System.out.println("\n-> Introduzca una opción de entre las siguientes\n");
        System.out.println("0: Volver al menú principal");
        System.out.println("1: Listar todas las categorías");
        System.out.println("2: Listar una categoría por su ID");
        System.out.println("3: Insertar una nueva categoría");
        System.out.println("4: Actualizar una categoría");
        System.out.println("5: Eliminar una categoría");
        System.out.print("\nOpción: ");
    }

    private void listAllCategorias() {
        System.out.println("\nLISTADO DE TODAS LAS CATEGORÍAS");
        System.out.println("--------------------------------\n");

        try {
            List<Categoria> result = categoriaDao.getAll();

            if (result.isEmpty())
                System.out.println("No hay categorías registradas en la base de datos");
            else {
                printCabeceraTablaCategoria();
                result.forEach(this::printCategoria);
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("\n");
    }

    private void listCategoriaById() {
        System.out.println("\nBÚSQUEDA DE CATEGORÍAS POR ID");
        System.out.println("------------------------------\n");

        try {
            System.out.print("Introduzca el ID de la categoría a buscar: ");
            int id = reader.nextInt();

            Categoria categoria = categoriaDao.getById(id);

            if (categoria == null)
                System.out.println("No hay categorías registradas en la base de datos con ese ID");
            else {
                printCabeceraTablaCategoria();
                printCategoria(categoria);
            }
            System.out.println("\n");
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
    }

    private void insertCategoria() {
        System.out.println("\nINSERCIÓN DE UNA NUEVA CATEGORÍA");
        System.out.println("--------------------------------\n");

        System.out.print("Introduzca el nombre de la categoría: ");
        String nombre = reader.nextLine();

        System.out.print("Introduzca la descripción de la categoría: ");
        String descripcion = reader.nextLine();

        try {
            categoriaDao.add(new Categoria(nombre, descripcion));
            System.out.println("Nueva categoría registrada");
        } catch (SQLException ex) {
            System.err.println("Error insertando el nuevo registro en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("");
    }

    private void updateCategoria() {
        System.out.println("\nACTUALIZACIÓN DE UNA CATEGORÍA");
        System.out.println("------------------------------\n");

        try {
            System.out.print("Introduzca el ID de la categoría a actualizar: ");
            int id = reader.nextInt();

            Categoria categoria = categoriaDao.getById(id);

            if (categoria == null)
                System.out.println("No hay categorías registradas en la base de datos con ese ID");
            else {
                printCabeceraTablaCategoria();
                printCategoria(categoria);
                System.out.println("\n");

                System.out.printf("Introduzca el nombre de la categoría (%s): ", categoria.getNombre());
                String nombre = reader.nextLine();
                nombre = (nombre.isBlank()) ? categoria.getNombre() : nombre;

                System.out.printf("Introduzca la descripción de la categoría (%s): ", categoria.getDescripcion());
                String descripcion = reader.nextLine();
                descripcion = (descripcion.isBlank()) ? categoria.getDescripcion() : descripcion;

                categoria.setNombre(nombre);
                categoria.setDescripcion(descripcion);

                categoriaDao.update(categoria);

                System.out.println("");
                System.out.printf("Categoría con ID %s actualizada", categoria.getId_categoria());
                System.out.println("");
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("");
    }

    private void deleteCategoria() {
        System.out.println("\nBORRADO DE UNA CATEGORÍA");
        System.out.println("--------------------------\n");

        try {
            System.out.print("Introduzca el ID de la categoría a borrar: ");
            int id = reader.nextInt();

            System.out.printf("¿Está usted seguro de querer eliminar la categoría con ID=%s? (s/n): ", id);
            String borrar = reader.nextLine();

            if (borrar.equalsIgnoreCase("s")) {
                categoriaDao.delete(id);
                System.out.printf("La categoría con ID %s se ha borrado\n", id);
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("");
    }

    static class KeyboardReader {
        BufferedReader br;
        StringTokenizer st;

        public KeyboardReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException ex) {
                    System.err.println("Error leyendo del teclado");
                    ex.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        LocalDate nextLocalDate() {
            return LocalDate.parse(next(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        String nextLine() {
            String str = "";
            try {
                if (st.hasMoreElements())
                    str = st.nextToken("\n");
                else
                    str = br.readLine();
            } catch (IOException ex) {
                System.err.println("Error leyendo del teclado");
                ex.printStackTrace();
            }
            return str;
        }
    }
}
