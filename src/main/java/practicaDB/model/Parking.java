package practicaDB.model;

import java.util.HashMap;
import java.util.Map;

import practicaDB.controller.ManagerDBController;

public class Parking {

    final Map<String, Coche> parking;
    private ManagerDBController managerDBController;

    public Parking() {
	parking = new HashMap<>();
    }

    public Parking(ManagerDBController managerDBController) {
	this.managerDBController = managerDBController;
	parking = new HashMap<>();
    }

    /**
     * Metodo que comprueba si existe un coche en el sistema
     * 
     * @param matricula
     * @return
     */
    public boolean existeCoche(final String matricula) {

	return managerDBController.search(matricula) != null;

	// return parking.containsKey(matricula);
    }

    /**
     * Metodo que obtiene un coche del sistema
     * 
     * @param matricula
     * @return
     */
    public Coche getCoche(final String matricula) {

	return managerDBController.search(matricula);

	// return parking.get(matricula);
    }

    /**
     * Metodo que añade un coche al sistema
     * 
     * @param matricula
     * @param coche
     */
    public void putCoche(final String matricula, final Coche coche) {

	managerDBController.insert(matricula, coche);

	// parking.put(matricula, coche);
    }

    /**
     * Metodo que imprime todos los coches del sistema, tanto los que estan dentro
     * del parking como los que no.
     */
    public void imprimirCochesSistema() {
	parking.clear();
	parking.putAll(managerDBController.searchAll());
	try {
	    parking.forEach((k, v) -> {
		System.out.println("Matricula: " + k + " Datos del " + v);
	    });
	} catch (Exception ex) {
	    System.out.println("Error al imprimir coches en el sistema");
	}
    }

    /**
     * Metodo que imprime los coches que estan dentro del parking (horaSalida =
     * null)
     */
    public void imprimirCochesParking() {
	
	parking.clear();
	parking.putAll(managerDBController.searchAllFilterHoraSalida());
	
	try {
	    parking.forEach((k, v) -> {

		System.out.println("Matricula: " + k + " Datos del " + v);

	    });
	} catch (Exception ex) {
	    System.out.println("Error al imprimir coches en el parking");
	}
    }

    /**
     * Método que calcula la cantidad a pagar por un coche según el tiempo que ha
     * estado dentro del parking
     * 
     * @param matricula
     */
    public void cantidadAPagar(final String matricula) {
	parking.clear();
	parking.putAll(managerDBController.searchAll());
	if (matricula != null) {
	    System.out.println("Cantidad a pagar $ " + parking.get(matricula).cantidadAPagar());
	}
    }

}