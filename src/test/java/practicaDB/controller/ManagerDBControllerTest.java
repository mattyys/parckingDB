package practicaDB.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManagerDBControllerTest {
    
    String url = "jdbc:h2:mem:DbTest;DB_CLOSE_DELAY=-1;NON_KEYWORDS=KEY,VALUE";
    String user = "sa";
    String pass = "sa";

    @BeforeEach
    void setUp() throws Exception {
	
	try (Connection conn = DriverManager.getConnection(url, user, pass);
		Statement stmt = conn.createStatement();){

	    stmt.executeUpdate("DROP TABLE IF EXISTS COCHES;\n" + "CREATE TABLE COCHES (\n"
		    + "	MATRICULA VARCHAR(10) NOT NULL,\n" + "	MARCA VARCHAR(20) NOT NULL,\n"
		    + "	MODELO VARCHAR(20) NOT NULL,\n" + "	HORA_ENTRADA TIMESTAMP NOT NULL,\n"
		    + "	HORA_SALIDA TIMESTAMP,\n" + "	CONSTRAINT COCHES_PK PRIMARY KEY (MATRICULA)\n" + ");\n"
		    + "INSERT INTO COCHES\n" + "(MATRICULA, MARCA, MODELO, HORA_ENTRADA, HORA_SALIDA)\n" + "VALUES\n"
		    + "('aa11', 'Ford', 'Focus', '2024-06-26 14:00:01',NULL),\n"
		    + "('aa22', 'Mercedez Benz', '320i', '2024-06-26 14:10:25',2024-06-26 14:15:25),\n"
		    + "('aa33', 'VW', 'Golf', '2024-06-26 14:25:05',NULL),\n"
		    + "('aa44','Audi','A3','2024-06-26 14:30:00',2024-06-26 14:40:02)");
	    
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    @Test
    void testRegistrarEntradaCoche() {
	    
    }
	
	
   

    @Test
    void test() {
    }

}
