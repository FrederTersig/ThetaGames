package model.dao;
import static util.Utile.crypt;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Utente;
//insicuro se sia ottimale o meno
import model.Voto;
import util.Database;
/**
 * 
 * @author Federico Tersigni
 *
 */

public class VotoDAO implements VotoDAO_interface{
	/**
	 * inserisce un nuovo voto nel DB
	 * 
	 * @param idGioco			id del gioco
	 * @param idutente			id dell'utente
	 * @param valore			valore del voto
	 * @return					
	 */
	public static void insert(int idGioco, int idUtente, int valore) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("valore",valore);
    	map.put("idGioco", idGioco);
    	map.put("idUtente", idUtente);   	
		try {								
			Database.connect();
			Database.insertRecord("voto", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println("namingException " +e);
        }catch (SQLException e) {
        	System.out.println("sqlException " +e);
        }catch (Exception e) {
        	System.out.println("Exception " + e);
        }
	}
	
	/**
	 * Aggiorna il valore del voto nel DB
	 * 
	 * @param idGioco			id del gioco
	 * @param idutente			id dell'utente
	 * @param valore			valore del voto nuovo
	 * @return					
	 */
	public static void update(int idGioco, int idUtente, int valore) {
		Map<String, Object> map = new HashMap<String, Object>();       
    	map.put("idUtente", idUtente);
    	map.put("idGioco", idGioco);
    	map.put("valore", valore);   	
		try {
			Database.connect();
    		Database.updateRecord("voto", map, " voto.idUtente="+idUtente+" AND voto.idGioco="+idGioco);
    		Database.close();    		
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}	
	/**
	 * controlla se l'utente ha già espresso il suo voto per un determinato gioco
	 * 
	 * @param idGioco			id del gioco
	 * @param idutente			id dell'utente
	 * 
	 * @return					booleana:se True esiste, false non esiste.			
	 */
	public static boolean esisteVoto(int idGioco, int idUtente) {
		boolean esiste=true;
		try {
			Database.connect();
			ResultSet rs = Database.select("voto"," voto.idGioco= "+idGioco+" AND voto.idUtente= "+idUtente);
			if (!rs.next()) {
				esiste=false;
			}else {
				esiste=true;
			}
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		
		return esiste;
	}
	/**
	 * calcola la media dei voti di un gioco
	 * 
	 * @param idGioco			id del gioco
	 * 
	 * @return					double che ha la media dei voti presi dal gioco			
	 */
	public static double mediaVotiGioco(int idGioco) {	
		double media = 0;
		try {
			Database.connect();
			ResultSet rs = Database.select("voto", "voto.idGioco="+idGioco);
    		int somma = 0;
    		int cont = 0;
			
    		while(rs.next()) {
    			int valore = rs.getInt("valore");
    			somma += valore;
    			cont ++;
    		}
			Database.close();		
			if(somma != 0 && cont != 0) media = somma / cont;
			else media=0;
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		
		return media;
	}
	
	/**
	 * Ritorna la lista dei voti presi da un gioco
	 * 
	 * @param idGioco			id del gioco
	 * 
	 * @return					lista con i voti presi dal gioco
	 */
	public static List<Integer> listaVotiGioco(int idGioco) {
		ArrayList<Integer> lista= new ArrayList<Integer>();
		try {
			Database.connect();
			ResultSet rs = Database.select("voto", "voto.idGioco="+idGioco);
			while(rs.next()) {
				int id = rs.getInt("id");
				lista.add(id);
			}
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return lista;
	}
	/**
	 * cancella un voto nel DB
	 * 
	 * @param idVoto			id del voto
	 * 
	 * @return					
	 */
	public static void delete(int id) {

		try {
			Database.connect();
			Database.deleteRecord("voto", "voto.id="+id);
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
	}
	
	
	
}
