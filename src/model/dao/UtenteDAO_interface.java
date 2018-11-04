package model.dao;

import static util.Utile.crypt;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Utente;
import util.Database;
/**
 * 
 * @author Federico Tersigni
 *
 */
public interface UtenteDAO_interface {
	public static void insert() {
		
	}
	public static void delete() {
		
	}
	public static void update() {
		
	}	
	public static void updatePsw(String password, int id) {
		
	}
	public static void updateEmail(String email, int id) {
		
	}
	public static void promuovi_utente(int id){
		
	}
	public static void promuovi_mod(int id){
		
	}
	public static void retrocedi_mod(int id){
		
	}
	public static void retrocedi_admin(int id){
		
	}
	public static void addTrofeoUtente(int idUtente, int idTrofeo) {
		
	}
	
	public static void addEsperienzaUtente(int idUtente, int idEsperienza, Date data) {
	
	}
	
	
	public static List<Utente> lista_utenti(){
		return null;
	}
	
}
