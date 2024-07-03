package practicaDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import practicaDB.dto.CocheDTO;

public class TestDB {

    private static final String DB = "jdbc:h2:./src/main/resources/database/db_parking";
    private static final String USER = "sa";
    private static final String PASS = "sa";
//    private static final String FIRST_QUERY = "DROP TABLE IF EXISTS COCHES;\n"
//    	+ "CREATE TABLE COCHES (\n"
//    	+ "	MATRICULA VARCHAR(10) NOT NULL,\n"
//    	+ "	MARCA VARCHAR(20) NOT NULL,\n"
//    	+ "	MODELO VARCHAR(20) NOT NULL,\n"
//    	+ "	HORA_ENTRADA TIMESTAMP NOT NULL,\n"
//    	+ "	HORA_SALIDA TIMESTAMP,\n"
//    	+ "	CONSTRAINT COCHES_PK PRIMARY KEY (MATRICULA)\n"
//    	+ ");\n"
//      	+ "INSERT INTO COCHES\n"
//    	+ "(MATRICULA, MARCA, MODELO, HORA_ENTRADA, HORA_SALIDA)\n"
//    	+ "VALUES\n"
//    	+ "('aa11', 'Ford', 'Focus', '2024-06-26 14:00:01',NULL),\n"
//    	+ "('aa22', 'Mercedez Benz', '320i', '2024-06-26 14:10:25',NULL),\n"
//    	+ "('aa33', 'VW', 'Golf', '2024-06-26 14:25:05',NULL),\n"
//    	+ "('aa44','Audi','A3','2024-06-26 14:30:00',NULL),\n"
//    	+ "('aa55','Seat','Ibiza','2024-06-26 14:45:00',NULL),\n"
//    	+ "('aa66','Renault','Clio','2024-06-26 14:50:00',NULL),\n"
//    	+ "('aa77','Peugeot','308','2024-06-26 15:00:00',NULL),\n"
//    	+ "('aa88','Citroen','C4','2024-06-26 15:10:00',NULL),\n"
//    	+ "('aa99','Toyota','Corolla','2024-06-26 15:20:00',NULL),\n"
//    	+ "('aa00','Hyundai','i30','2024-06-26 15:30:00',NULL);";

    public static void main(String[] args) {
	// TODO Auto-generated method stub
	
	Map<String,CocheDTO> coches = new HashMap<>();
	CocheDTO coche = null;

	try(Connection conn = DriverManager.getConnection(DB, USER, PASS);	    
	    Statement stmt = conn.createStatement();)
		 {
	    //stmt.executeUpdate(FIRST_QUERY);
	    
	    ResultSet rs = stmt.executeQuery("SELECT * FROM COCHES");
	    
	    while(rs.next()) {
		coche = new CocheDTO();
		coche.setMatricula(rs.getString(1));
		coche.setMarca(rs.getString(2));
		coche.setModelo(rs.getString(3));
		coche.setHoraEntrada(rs.getTimestamp(4).toLocalDateTime());
		Optional<LocalDateTime> horaSalida = Optional.ofNullable(rs.getTimestamp(5)).map(t -> t.toLocalDateTime());
		coche.setHoraSalida(horaSalida.orElse(null));
		
		coches.put(coche.getMatricula(), coche);
	    }
	    
	    
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	coches.forEach((k,v) -> {
	    System.out.println("Matricula: " + k + " Datos del coche: " + v.getMarca() + ""
	    	+ " " + v.getModelo() + " " + v.getHoraEntrada() + " " + v.getHoraSalida());
        });
    }
}

