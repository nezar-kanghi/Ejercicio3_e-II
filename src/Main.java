import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String usuario = "RIBERA";
        String password = "ribera";

        int opcion;

        do {

            System.out.println("MENU");
            System.out.println("1. Clasificacion general");
            System.out.println("2. Clasificacion por equipos");
            System.out.println("3. Ranking etapas largas");
            System.out.println("0. Salir");

            opcion = sc.nextInt();

            switch (opcion) {

                case 1:
                    clasificacionGeneral(url, usuario, password);
                    break;

                case 2:
                    clasificacionEquipos(url, usuario, password);
                    break;

                case 3:
                    rankingEtapas(url, usuario, password);
                    break;
            }

        } while (opcion != 0);

        sc.close();
    }



    public static void clasificacionGeneral(String url,
                                            String usuario,
                                            String password) {

        try (Connection conexion = DriverManager.getConnection(url, usuario, password);
             Statement st = conexion.createStatement()) {

            String sql =
                    "SELECT c.NOMBRE, " +
                            "e.NOMBRE AS NOMBRE_EQUIPO, " +
                            "SUM(p.PUNTOS) AS puntos_totales " +
                            "FROM CICLISTA c " +
                            "JOIN EQUIPO e ON c.ID_EQUIPO = e.ID_EQUIPO " +
                            "JOIN PARTICIPACION p ON c.ID_CICLISTA = p.ID_CICLISTA " +
                            "GROUP BY c.NOMBRE, e.NOMBRE " +
                            "ORDER BY puntos_totales DESC";

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                String nombre = rs.getString("NOMBRE");
                String equipo = rs.getString("NOMBRE_EQUIPO");
                int puntos = rs.getInt("puntos_totales");

                System.out.println(nombre + " - " + equipo + " - " + puntos);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public static void clasificacionEquipos(String url,
                                            String usuario,
                                            String password) {

        try (Connection conexion = DriverManager.getConnection(url, usuario, password);
             Statement st = conexion.createStatement()) {

            String sql =
                    "SELECT e.nombre AS NOMBRE_EQUIPO, " +
                            "e.pais, " +
                            "SUM(p.puntos) AS puntos_equipo " +
                            "FROM equipo e " +
                            "JOIN ciclista c ON e.id_equipo = c.id_equipo " +
                            "JOIN participacion p ON c.id_ciclista = p.id_ciclista " +
                            "GROUP BY e.nombre, e.pais " +
                            "ORDER BY puntos_equipo DESC";

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                String equipo = rs.getString("NOMBRE_EQUIPO");
                String pais = rs.getString("pais");
                int puntos = rs.getInt("puntos_equipo");

                System.out.println(equipo + " - " + pais + " - " + puntos);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //RANKING ETAPAS
    public static void rankingEtapas(String url,
                                     String usuario,
                                     String password) {

        try (Connection conexion = DriverManager.getConnection(url, usuario, password);
             Statement st = conexion.createStatement()) {

            String sql =
                    "SELECT numero, origen, destino, distancia_km, fecha " +
                            "FROM etapa " +
                            "ORDER BY distancia_km DESC " +
                            "FETCH FIRST 3 ROWS ONLY";

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                int numero = rs.getInt("numero");
                String origen = rs.getString("origen");
                String destino = rs.getString("destino");
                int distancia = rs.getInt("distancia_km");
                Date fecha = rs.getDate("fecha");

                System.out.println(numero + " - " + origen + " - " + destino +
                        " - " + distancia + " km - " + fecha);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}