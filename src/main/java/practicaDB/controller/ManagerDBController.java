package practicaDB.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import practicaDB.db.ManagerDbAbstract;
import practicaDB.dto.CocheDTO;
import practicaDB.model.Coche;

public class ManagerDBController extends ManagerDbAbstract {
    
    protected static final String UPDATE_CAR_IN_PARKING = "UPDATE Coches SET Hora_entrada=?, Hora_salida=? WHERE Matricula=?";

    Connection conn = null;
    // de aca inicializa la conexion y la utiliza en los metodos
    private final DateTimeFormatter DB_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public final DateTimeFormatter DATE_FORMAT_INFO = DateTimeFormatter.ofPattern("HH:mm:ss / dd-MM-yyyy");

    public ManagerDBController() {
	try {
	    conn = super.iniConexion();
	} catch (ClassNotFoundException | SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void closeConnection() {
	try {
	    conn.close();
	    System.out.println("Conexion cerrada...");
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    public int update(String matricula, LocalDateTime horaSalida) {
	// TODO Auto-generated method stub
	try (PreparedStatement pstmt = conn.prepareStatement(ManagerDbAbstract.UPDATE)) {
	    pstmt.setTimestamp(1, Timestamp.valueOf(horaSalida.format(DB_TIME_FORMATTER)));
	    pstmt.setString(2, matricula);
	    pstmt.executeUpdate();

	    return 1;

	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return 0;
    }

    @Override
    public int insert(String matricula, Coche coche) {
	// TODO Auto-generated method stub

	try (PreparedStatement pstmt = conn.prepareStatement(ManagerDbAbstract.INSERT)) {

	    pstmt.setString(1, matricula);
	    pstmt.setString(2, coche.getMarca());
	    pstmt.setString(3, coche.getModelo());
	    pstmt.setTimestamp(4, Timestamp.valueOf(coche.getHoraEntrada().format(DB_TIME_FORMATTER)));
	    pstmt.setTimestamp(5, null);
	    pstmt.executeUpdate();
	    return 1;

	} catch (SQLException e) {
	    e.printStackTrace();

	}

	return 0;
    }

    @Override
    public Coche search(String matricula) {
	// TODO Auto-generated method stub
	CocheDTO coche = null;
	try (PreparedStatement pstmt = conn.prepareStatement(ManagerDbAbstract.SELECT_BY_MATRICULA)) {
	    pstmt.setString(1, matricula);
	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		coche = new CocheDTO();
		coche.setMatricula(matricula);
		coche.setMarca(rs.getString(1));
		coche.setModelo(rs.getString(2));
		coche.setHoraEntrada(rs.getTimestamp(3).toLocalDateTime());
		Optional<LocalDateTime> horaSalida = Optional.ofNullable(rs.getTimestamp(4))
			.map(t -> t.toLocalDateTime());
		coche.setHoraSalida(horaSalida.orElse(null));

		System.out.println("Coche encontrado...");
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return coche;
    }

    @Override
    public Map<String, Coche> searchAll() {
	Map<String, Coche> cochesAll = new HashMap<String, Coche>();

	try (PreparedStatement pstmt = conn.prepareStatement(ManagerDbAbstract.SELECT_ALL);) {

	    ResultSet rs = pstmt.executeQuery();

	    CocheDTO coche = null;

	    while (rs.next()) {
		coche = new CocheDTO();
		coche.setMatricula(rs.getString(1));
		coche.setMarca(rs.getString(2));
		coche.setModelo(rs.getString(3));
		coche.setHoraEntrada(rs.getTimestamp(4).toLocalDateTime());
		Optional<LocalDateTime> horaSalida = Optional.ofNullable(rs.getTimestamp(5))
			.map(t -> t.toLocalDateTime());
		coche.setHoraSalida(horaSalida.orElse(null));

		cochesAll.put(coche.getMatricula(), coche);
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return cochesAll;
    }

    @Override
    public Map<String, Coche> searchAllFilterHoraSalida() {
	// TODO Auto-generated method stub
	Map<String, Coche> cochesHoraSalida = new HashMap<String, Coche>();

	try (PreparedStatement pstmt = conn.prepareStatement(ManagerDbAbstract.SELECT_ALL_WITHOUT_HORA_SALIDA);) {

	    ResultSet rs = pstmt.executeQuery();

	    CocheDTO coche = null;

	    while (rs.next()) {
		coche = new CocheDTO();
		coche.setMatricula(rs.getString(1));
		coche.setMarca(rs.getString(2));
		coche.setModelo(rs.getString(3));
		coche.setHoraEntrada(rs.getTimestamp(4).toLocalDateTime());
		Optional<LocalDateTime> horaSalida = Optional.ofNullable(rs.getTimestamp(5))
			.map(t -> t.toLocalDateTime());
		coche.setHoraSalida(horaSalida.orElse(null));

		cochesHoraSalida.put(coche.getMatricula(), coche);
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return cochesHoraSalida;
    }
    
    public int updateCarInParcking(CocheDTO cocheDTO) {
	try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_CAR_IN_PARKING)) {
	    pstmt.setTimestamp(1, Timestamp.valueOf(cocheDTO.getHoraEntrada().format(DB_TIME_FORMATTER)));
	    pstmt.setTimestamp(2, null);
	    pstmt.setString(3, cocheDTO.getMatricula());
	    pstmt.executeUpdate();
	    return 1;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	
	
	return 0;
    }
    
    public String mensajeConeccion() {
	return "Conectado a ManagerDBController...";
    }

}
