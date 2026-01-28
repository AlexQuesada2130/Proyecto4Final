import dao.implementaciones.*;
import entity.*;
import conBD.ConexionBD;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;

public class Proyecto {
    static Scanner sc = new Scanner(System.in);

    static ClienteDaoImpl clienteDAO = new ClienteDaoImpl();
    static VehiculoDaoImpl vehiculoDAO = new VehiculoDaoImpl();
    static CategoriaVehiculoDaoImpl categoriaDAO = new CategoriaVehiculoDaoImpl();
    static AlquilerDaoImpl alquilerDAO = new AlquilerDaoImpl();

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n1. Gestión de Clientes");
            System.out.println("2. Gestión de Vehículos");
            System.out.println("3. Gestión de Categorías");
            System.out.println("4. Alquileres Activos");
            System.out.println("5. Nuevo Alquiler");
            System.out.println("6. Finalizar Alquiler");
            System.out.println("7. Vehículos por Categoría");
            System.out.println("0. Salir");

            String op = sc.nextLine();

            switch (op) {
                case "0" -> exit = true;
                case "1" -> gestionClientes();
                case "2" -> gestionVehiculos();
                case "3" -> gestionCategorias();
                case "4" -> mostrarAlquileresDetallados();
                case "5" -> nuevoAlquiler();
                case "6" -> finalizarAlquiler();
                case "7" -> mostrarVehiculosPorCategoria();
                default -> System.out.println("Opción no válida.");
            }
        }

        ConexionBD.close();
        System.out.println("Programa finalizado.");
    }

    public static void mostrarAlquileresDetallados() {
        List<Alquiler> lista = alquilerDAO.listarTodos();

        if (lista == null || lista.isEmpty()) {
            System.out.println("No hay alquileres registrados.");
        } else {
            for (Alquiler a : lista) {
                String cat = (a.getIdVehiculo().getCategoria() != null) ? a.getIdVehiculo().getCategoria().getNombre() : "Sin Cat";
                System.out.println("ID Alquiler: " + a.getId() +
                        " | Vehículo: " + a.getIdVehiculo().getMarca() + " " + a.getIdVehiculo().getModelo() + " (" + cat + ")" +
                        " | Cliente: " + a.getIdCliente().getNombre() + " " + a.getIdCliente().getApellidos());
            }
        }
    }

    public static void nuevoAlquiler() {
        try {
            for (Cliente c : clienteDAO.listarTodos()) {
                System.out.println(c.getId() + ". " + c.getNombre() + " " + c.getApellidos());
            }

            System.out.print("ID Cliente: ");
            int idC = Integer.parseInt(sc.nextLine());
            Cliente cliente = clienteDAO.obtenerPorId(idC);

            if (cliente == null) {
                System.out.println("Cliente no existe.");
                return;
            }

            for (Vehiculo v : vehiculoDAO.listarTodos()) {
                System.out.println(v.getId() + " - " + v.getMarca() + " " + v.getModelo() + " (" + v.getMatricula() + ")");
            }

            System.out.print("ID Vehículo: ");
            int idV = Integer.parseInt(sc.nextLine());
            Vehiculo vehiculo = vehiculoDAO.obtenerPorId(idV);

            if (vehiculo == null) {
                System.out.println("Vehículo no existe.");
                return;
            }

            Alquiler nuevo = new Alquiler();
            nuevo.setIdCliente(cliente);
            nuevo.setIdVehiculo(vehiculo);
            nuevo.setFechaContrato(new Timestamp(System.currentTimeMillis()));

            if (alquilerDAO.insertar(nuevo)) {
                System.out.println("Alquiler realizado.");
            } else {
                System.out.println("No se pudo realizar el alquiler.");
            }

        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }

    public static void finalizarAlquiler() {
        mostrarAlquileresDetallados();
        System.out.print("ID Alquiler a finalizar: ");
        try {
            int id = Integer.parseInt(sc.nextLine());
            if (alquilerDAO.eliminar(id)) {
                System.out.println("Alquiler eliminado.");
            } else {
                System.out.println("No se encontró el alquiler.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID debe ser numérico.");
        }
    }

    public static void gestionClientes() {
        boolean back = false;
        while (!back) {
            System.out.println("\n1. Listar clientes");
            System.out.println("2. Crear cliente");
            System.out.println("3. Modificar cliente");
            System.out.println("4. Borrar cliente");
            System.out.println("0. Volver");

            String op = sc.nextLine();

            switch (op) {
                case "0" -> back = true;
                case "1" -> {
                    List<Cliente> list = clienteDAO.listarTodos();
                    if (list.isEmpty()) System.out.println("No hay clientes.");
                    else for (Cliente c : list) System.out.println(c.getId() + ". " + c.getNombre() + " " + c.getApellidos());
                }
                case "2" -> {
                    System.out.print("Nombre: ");
                    String n = sc.nextLine();
                    System.out.print("Apellidos: ");
                    String a = sc.nextLine();
                    if (clienteDAO.insertar(new Cliente(n, a))) {
                        System.out.println("Cliente guardado.");
                    }
                }
                case "3" -> {
                    for (Cliente c : clienteDAO.listarTodos()) {
                        System.out.println(c.getId() + ". " + c.getNombre() + " " + c.getApellidos());
                    }
                    System.out.print("ID a modificar: ");
                    try {
                        int id = Integer.parseInt(sc.nextLine());
                        Cliente c = clienteDAO.obtenerPorId(id);

                        if (c != null) {
                            System.out.println("Nombre actual: " + c.getNombre());
                            System.out.print("Nuevo nombre: ");
                            String n = sc.nextLine();
                            if (!n.isEmpty()) c.setNombre(n);

                            System.out.println("Apellidos actuales: " + c.getApellidos());
                            System.out.print("Nuevos apellidos: ");
                            String a = sc.nextLine();
                            if (!a.isEmpty()) c.setApellidos(a);

                            if (clienteDAO.modificar(c)) System.out.println("Modificado.");
                        } else {
                            System.out.println("No encontrado.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                    }
                }
                case "4" -> {
                    for (Cliente c : clienteDAO.listarTodos()) {
                        System.out.println(c.getId() + ". " + c.getNombre() + " " + c.getApellidos());
                    }
                    System.out.print("ID a borrar: ");
                    try {
                        int id = Integer.parseInt(sc.nextLine());
                        if (clienteDAO.eliminar(id)) {
                            System.out.println("Eliminado.");
                        } else {
                            System.out.println("No se pudo borrar (posibles alquileres activos).");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                    }
                }
            }
        }
    }

    public static void gestionVehiculos() {
        boolean back = false;
        while (!back) {
            System.out.println("\n1. Listar vehículos");
            System.out.println("2. Crear vehículo");
            System.out.println("3. Modificar vehículo");
            System.out.println("4. Borrar vehículo");
            System.out.println("0. Volver");

            String op = sc.nextLine();

            switch (op) {
                case "0" -> back = true;
                case "1" -> {
                    List<Vehiculo> lista = vehiculoDAO.listarTodos();
                    if (lista.isEmpty()) System.out.println("No hay vehículos.");
                    else for (Vehiculo v : lista) System.out.println(v.getId() + " - " + v.getMarca() + " " + v.getModelo());
                }
                case "2" -> {
                    System.out.print("Marca: ");
                    String marca = sc.nextLine();
                    System.out.print("Modelo: ");
                    String modelo = sc.nextLine();
                    System.out.print("Matrícula: ");
                    String matricula = sc.nextLine();

                    List<CategoriaVehiculo> cats = categoriaDAO.listarTodos();
                    for (CategoriaVehiculo c : cats) System.out.println(c.getId() + ". " + c.getNombre());

                    System.out.print("ID Categoría: ");
                    try {
                        int idCat = Integer.parseInt(sc.nextLine());
                        CategoriaVehiculo cat = categoriaDAO.obtenerPorId(idCat);

                        if (cat != null) {
                            Vehiculo v = new Vehiculo(marca, modelo, matricula, cat);
                            if (vehiculoDAO.insertar(v)) {
                                System.out.println("Creado.");
                            }
                        } else {
                            System.out.println("Categoría no existe.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID numérico requerido.");
                    }
                }
                case "3" -> {
                    List<Vehiculo> lista = vehiculoDAO.listarTodos();
                    for (Vehiculo v : lista) System.out.println(v.getId() + " - " + v.getMarca() + " " + v.getModelo());

                    System.out.print("ID a modificar: ");
                    try {
                        int id = Integer.parseInt(sc.nextLine());
                        Vehiculo v = vehiculoDAO.obtenerPorId(id);

                        if (v != null) {
                            System.out.println("Marca actual: " + v.getMarca());
                            System.out.print("Nueva Marca: ");
                            String m = sc.nextLine();
                            if (!m.isEmpty()) v.setMarca(m);

                            System.out.println("Modelo actual: " + v.getModelo());
                            System.out.print("Nuevo Modelo: ");
                            String mod = sc.nextLine();
                            if (!mod.isEmpty()) v.setModelo(mod);

                            System.out.println("Matrícula actual: " + v.getMatricula());
                            System.out.print("Nueva Matrícula: ");
                            String mat = sc.nextLine();
                            if (!mat.isEmpty()) v.setMatricula(mat);

                            System.out.println("Categoría: " + (v.getCategoria() != null ? v.getCategoria().getNombre() : "Ninguna"));
                            System.out.print("¿Cambiar categoría? (s/n): ");
                            if (sc.nextLine().equalsIgnoreCase("s")) {
                                for (CategoriaVehiculo c : categoriaDAO.listarTodos()) System.out.println(c.getId() + ". " + c.getNombre());
                                System.out.print("Nuevo ID Categoría: ");
                                int idCat = Integer.parseInt(sc.nextLine());
                                CategoriaVehiculo nuevaCat = categoriaDAO.obtenerPorId(idCat);
                                if (nuevaCat != null) v.setCategoria(nuevaCat);
                            }

                            if (vehiculoDAO.modificar(v)) System.out.println("Actualizado.");
                        } else {
                            System.out.println("Vehículo no encontrado.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Formato incorrecto.");
                    }
                }
                case "4" -> {
                    List<Vehiculo> lista = vehiculoDAO.listarTodos();
                    for (Vehiculo v : lista) System.out.println(v.getId() + " - " + v.getMarca() + " " + v.getModelo());

                    System.out.print("ID a borrar: ");
                    try {
                        int id = Integer.parseInt(sc.nextLine());
                        if (vehiculoDAO.eliminar(id)) {
                            System.out.println("Eliminado.");
                        } else {
                            System.out.println("No se pudo eliminar.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                    }
                }
            }
        }
    }

    public static void gestionCategorias() {
        boolean back = false;
        while (!back) {
            System.out.println("\n1. Listar categorías");
            System.out.println("2. Crear categoría");
            System.out.println("3. Modificar categoría");
            System.out.println("4. Borrar categoría");
            System.out.println("0. Volver");

            String op = sc.nextLine();

            switch (op) {
                case "0" -> back = true;
                case "1" -> {
                    List<CategoriaVehiculo> lista = categoriaDAO.listarTodos();
                    if (lista.isEmpty()) System.out.println("No hay categorías.");
                    else for (CategoriaVehiculo c : lista) System.out.println(c.getId() + ". " + c.getNombre());
                }
                case "2" -> {
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    if (!nombre.isEmpty()) {
                        if (categoriaDAO.insertar(new CategoriaVehiculo(nombre)))
                            System.out.println("Creada.");
                    }
                }
                case "3" -> {
                    for (CategoriaVehiculo c : categoriaDAO.listarTodos()) {
                        System.out.println(c.getId() + ". " + c.getNombre());
                    }
                    System.out.print("ID a editar: ");
                    try {
                        int id = Integer.parseInt(sc.nextLine());
                        CategoriaVehiculo c = categoriaDAO.obtenerPorId(id);

                        if (c != null) {
                            System.out.println("Nombre actual: " + c.getNombre());
                            System.out.print("Nuevo nombre: ");
                            String n = sc.nextLine();
                            if (!n.isEmpty()) {
                                c.setNombre(n);
                                if (categoriaDAO.modificar(c)) System.out.println("Actualizada.");
                            }
                        } else {
                            System.out.println("No encontrada.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                    }
                }
                case "4" -> {
                    for (CategoriaVehiculo c : categoriaDAO.listarTodos()) {
                        System.out.println(c.getId() + ". " + c.getNombre());
                    }
                    System.out.print("ID a borrar: ");
                    try {
                        int id = Integer.parseInt(sc.nextLine());

                        List<Vehiculo> asociados = vehiculoDAO.listarPorCategoria(id);
                        int numAlquileres = 0;
                        List<Alquiler> todosAlquileres = alquilerDAO.listarTodos();

                        for (Vehiculo v : asociados) {
                            for (Alquiler a : todosAlquileres) {
                                if (a.getIdVehiculo().getId().equals(v.getId())) {
                                    numAlquileres++;
                                }
                            }
                        }
                        boolean borrar = true;
                        if (!asociados.isEmpty()) {
                            System.out.println("Hay " + asociados.size() + " vehículos asociados y " + numAlquileres + " alquileres.");
                            System.out.println("Se borrará todo lo asociado.");
                            System.out.print("¿Seguro? (s/n): ");
                            if (!sc.nextLine().equalsIgnoreCase("s")) {
                                borrar = false;
                            }
                        }

                        if (borrar) {
                            for (Vehiculo v : asociados) {
                                for (Alquiler a : todosAlquileres) {
                                    if (a.getIdVehiculo().getId().equals(v.getId())) {
                                        alquilerDAO.eliminar(a.getId());
                                    }
                                }
                                vehiculoDAO.eliminar(v.getId());
                            }
                            if (categoriaDAO.eliminar(id)) {
                                System.out.println("Eliminada.");
                            } else {
                                System.out.println("Error al eliminar.");
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                    }
                }
            }
        }
    }

    public static void mostrarVehiculosPorCategoria() {
        List<CategoriaVehiculo> cats = categoriaDAO.listarTodos();
        if (cats.isEmpty()) {
            System.out.println("No hay categorías.");
            return;
        }

        for (CategoriaVehiculo c : cats) {
            System.out.println("ID " + c.getId() + ": " + c.getNombre());
        }

        System.out.print("ID Categoría: ");
        try {
            int idCat = Integer.parseInt(sc.nextLine());
            List<Vehiculo> vehiculos = vehiculoDAO.listarPorCategoria(idCat);

            if (vehiculos.isEmpty()) {
                System.out.println("Sin vehículos.");
            } else {
                for (Vehiculo v : vehiculos) {
                    System.out.println(v.getId() + " - " + v.getMarca() + " " + v.getModelo());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }
}