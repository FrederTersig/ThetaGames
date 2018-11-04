package model.dao;

import java.util.List;

import model.Voto;
/**
 * 
 * @author Federico Tersigni
 *
 */
public interface VotoDAO_interface {
	public static void insert() {
		
	}
	public static void delete(int id) {
		
	}
	public static void update() {
		
	}	
	public static boolean esisteVoto(int idGioco, int idUtente) {
		return false;
	}
	public static int mediaVotiGioco() {	
		return -1;
	}

	public static List<Integer> listaVotiGioco(int idGioco) {
		return null;
	}
}
