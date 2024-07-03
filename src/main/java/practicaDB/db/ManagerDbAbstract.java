package practicaDB.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Map;

import practicaDB.model.Coche;



public abstract class ManagerDbAbstract {

	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:./src/main/resources/database/db_parking";

	// Database credentials
	static final String USER = "sa";
	static final String PASS = "sa";

	static final String CREATE_TABLE = "CREATE TABLE Coches (Matricula VARCHAR (10) NOT NULL PRIMARY KEY, Marca VARCHAR (10) NOT NULL, Modelo VARCHAR(20) NOT NULL, Hora_entrada DATETIME NOT NULL, Hora_salida DATETIME)";
	protected static final String SELECT_BY_MATRICULA = "SELECT Marca, Modelo, Hora_entrada, Hora_salida FROM Coches WHERE Matricula=?";
	protected static final String INSERT = "INSERT INTO Coches (Matricula, Marca, Modelo, Hora_entrada, Hora_salida) VALUES (?, ?, ?, ?, ?)";
	protected static final String UPDATE = "UPDATE Coches SET Hora_salida=? WHERE Matricula=?";
	protected static final String SELECT_ALL = "SELECT Matricula, Marca, Modelo, Hora_entrada, Hora_salida FROM Coches";
	protected static final String SELECT_ALL_WITHOUT_HORA_SALIDA = "SELECT Matricula, Marca, Modelo, Hora_entrada, Hora_salida FROM Coches WHERE Hora_salida IS NULL";

	public ManagerDbAbstract() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Inicializa conexion a base de datos
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	protected Connection iniConexion() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER);
		Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
		System.out.println("Conectando a base de datos...");
		return connection;
	}

	/**
	 * Crea tabla si no existe
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void crearTabla() throws ClassNotFoundException, SQLException {
		try (Connection connection = iniConexion()) {
			try (Statement statement = connection.createStatement()) {
				// Execute a query
				System.out.println("Creando tabla en la base de datos especificada...");
				statement.executeUpdate(CREATE_TABLE);
				System.out.println("Creada tabla en la base de datos especificada...");
			} catch(SQLException sqlE) {
				System.out.println("Ya existe la tabla");
			}
		}
	}
	
	

	/**
	 * Actualiza un registro por matricula con la hora de salida
	 * @param matricula
	 * @param horaSalida
	 * @return
	 */
	public abstract int update(final String matricula, final LocalDateTime horaSalida);
	
	

	/**
	 * Inserta un registro en base de datos
	 * 
	 * @param matricula
	 * @param coche
	 * @return
	 */
	public abstract int insert(final String matricula, final Coche coche);
	
	

	/**
	 * Recupera un coche buscando por matricula
	 * @param matricula
	 * @return
	 */
	public abstract Coche search(final String matricula);
	
	

	/**
	 * Busca todos los coches del sistema
	 * @return
	 */
	public abstract Map<String, Coche> searchAll();
	
	

	/**
	 * Busca todos los coches del sistema filtrando por hora de salida igual a NULL
	 * @return
	 */
	public abstract Map<String, Coche> searchAllFilterHoraSalida();

}
