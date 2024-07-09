package practicaDB.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// TODO: Auto-generated Javadoc
/**
 * The Class Coche.
 */
public class Coche {

	/** The marca. */
	private String marca;
	
	/** The modelo. */
	private String modelo;
	
	/** The hora entrada. */
	private LocalDateTime horaEntrada;
	
	/** The hora salida. */
	private LocalDateTime horaSalida;
	
	/** The Constant PRECIO_MINUTO. */
	private final static float PRECIO_MINUTO = 0.15f;
	
	
	/**
	 * Instantiates a new coche.
	 */
	public Coche() {
	    super();
	}

	/**
	 * Instantiates a new coche.
	 *
	 * @param marca the marca
	 * @param modelo the modelo
	 * @param horaEntrada the hora entrada
	 * @param horaSalida the hora salida
	 */
	public Coche(final String marca, final String modelo, final LocalDateTime horaEntrada,
			final LocalDateTime horaSalida) {
		super();
		this.marca = marca;
		this.modelo = modelo;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
	}

	/**
	 * Metodo que calcula la cantidad a pagar, obteniendo los minutos que hay entre la fecha de entrada y la de salida.
	 *
	 * @return the float
	 */
	public float cantidadAPagar() {
		long minutos = ChronoUnit.MINUTES.between(horaEntrada, horaSalida);

		return minutos * PRECIO_MINUTO;
	}

	/**
	 * Gets the marca.
	 *
	 * @return the marca
	 */
	public String getMarca() {
		return marca;
	}

	/**
	 * Sets the marca.
	 *
	 * @param marca the new marca
	 */
	public void setMarca(final String marca) {
		this.marca = marca;
	}

	/**
	 * Gets the modelo.
	 *
	 * @return the modelo
	 */
	public String getModelo() {
		return modelo;
	}

	/**
	 * Sets the modelo.
	 *
	 * @param modelo the new modelo
	 */
	public void setModelo(final String modelo) {
		this.modelo = modelo;
	}

	/**
	 * Gets the hora entrada.
	 *
	 * @return the hora entrada
	 */
	public LocalDateTime getHoraEntrada() {
		return horaEntrada;
	}

	/**
	 * Sets the hora entrada.
	 *
	 * @param horaEntrada the new hora entrada
	 */
	public void setHoraEntrada(final LocalDateTime horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	/**
	 * Gets the hora salida.
	 *
	 * @return the hora salida
	 */
	public LocalDateTime getHoraSalida() {
		return horaSalida;
	}

	/**
	 * Sets the hora salida.
	 *
	 * @param horaSalida the new hora salida
	 */
	public void setHoraSalida(final LocalDateTime horaSalida) {
		this.horaSalida = horaSalida;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Coche [marca=");
		builder.append(marca);
		builder.append(", modelo=");
		builder.append(modelo);
		builder.append(", horaEntrada=");
		builder.append(horaEntrada);
		builder.append(", horaSalida=");
		builder.append(horaSalida);
		builder.append("]");
		return builder.toString();
	}

}