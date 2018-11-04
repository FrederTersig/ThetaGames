package model.dao;

import java.util.List;

import model.Trofeo;
/**
 * 
 * @author Federico Tersigni
 *
 */
public interface TrofeoDAO_interface {

	public static void insert() {
		
	}
	public static void delete() {
		
	}
	public static void update() {
		
	}
	public static int  getIDLivelloAttuale(int idUtente){
		return 0;
	}
	public static int getIdTrofeoDaLVL(int livelloTot) {
		return 0;
	}
	public static List<Trofeo> lista_trofei_utente(int id){
		return null;
	}
	public static List<Trofeo> lista_trofei_gioco(int idGioco){
		return null;
	}
	public static List<Trofeo> lista_trofei_assoc(){
		return null;
	}
	public static int getIdTrofeo(int idGioco, String titolo){
		return 0;
	}
}
