package model.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Gioco;
import model.Recensione;
import model.Utente;
import util.Database;
/**
 * 
 * @author Federico Tersigni
 *
 */
public class RecensioneDAO implements RecensioneDAO_interface {
	/**
	 * metodo che inserisce una recensione nel DB
	 * 
	 * @param commento			commento della recensione
	 * @param idGioco			id del gioco
	 * @param idUtente			id dell'utente
	 * @param valido			numero che rappresenza l'avvenuta accettazione della recensione che, in questo caso, viene
	 * 							settato a 0 per dire che non è stata ancora convalidata
	 * 
	 * @return					
	 */	
	public static void insert(String commento, int idGioco, int idUtente, int valido) {
		Map<String, Object> map = new HashMap<String,Object>();
		Calendar calendar = Calendar.getInstance();
		Date data = new Date(calendar.getTime().getTime());
		map.put("commento",commento);
		map.put("data", data);
    	map.put("idGioco", idGioco);
    	map.put("idUtente", idUtente);
		map.put("valido", valido);
		try {								
			Database.connect();
			Database.insertRecord("recensione", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println("namingException " +e);
        }catch (SQLException e) {
        	System.out.println("sqlException " +e);
        }catch (Exception ex) {
        	System.out.println("Exception " + ex);
        }
		map.clear();
	}
	
	/**
	 * metodo che cancella una recensione dal DB
	 * 
	 * @param id			id della recensione
	 * 
	 * @return				
	 */
	public static void delete(int id) {
		try {
			Database.connect();
			Database.deleteRecord("recensione", "recensione.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println("namingException " +e);
        }catch (SQLException e) {
        	System.out.println("sqlException " +e);
        }catch (Exception ex) {
        	System.out.println("Exception " + ex);
        }
	}
	/**
	 * metodo che convalida una recensione
	 * 
	 * @param id			id della recensione
	 * 
	 * @return					
	 */
	public static void valida(int id) {
		try {								
			Database.connect();
			Database.updateRecord("recensione", " recensione.valido=1 ", "recensione.id="+id); 		
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
	 * metodo che ritorna la lista delle recensioni che devono ancora essere valutate
	 * 
	 * @param 
	 * 
	 * @return					la lista delle recensioni da valutare
	 */
	public static List<Recensione> recensioni_daValutare(){ 
		ArrayList<Recensione> lista=null;
		try {
			Database.connect();
			ResultSet rs = Database.selectJoin("recensione.id,recensione.data, recensione.commento, utente.username, gioco.titolo", "recensione", "utente",
					"utente.id = recensione.idUtente","gioco","gioco.id = recensione.idGioco", "recensione.valido = 0","recensione.data");
    		lista = new ArrayList<Recensione>(); 
    		while(rs.next()) {
    			int id = rs.getInt("id");
    			Date data = rs.getDate("data");
    			String commento = rs.getString("commento");
    			String username = rs.getString("username");
    			String titolo = rs.getString("titolo");
    			Recensione elemento = new Recensione(id,data,commento,username,titolo);
    			lista.add(elemento);	
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
	 * metodo che ritorna la lista degli ID delle recensioni appartenenti ad un gioco
	 * 
	 * @param idGioco			id del gioco
	 * 
	 * @return					Lista integer che contiene gli id delle recensioni appartenenti ad un gioco
	 */
	public static List<Integer> idRecensioniGioco(int idGioco) {
		ArrayList<Integer> lista= new ArrayList<Integer>();
		try {
			Database.connect();
			ResultSet rs = Database.select("recensione", "recensione.idGioco="+idGioco);
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
	 * metodo che ritorna la lista delle recensioni che appartengono ad un gioco
	 * 
	 * @param idGioco			id del gioco
	 * 
	 * @return					la lista delle recensioni che appartengono ad un gioco
	 */
	public static List<Recensione> recensioni_Gioco(int idgioco){
		ArrayList<Recensione> lista=null;
		try {
			Database.connect();
			ResultSet rs = Database.selectJoin("recensione.data, recensione.commento, utente.username", "recensione", "utente",
					"recensione.idUtente=utente.id","recensione.idGioco="+idgioco+" AND recensione.valido=1","recensione.data");
    		lista = new ArrayList<Recensione>(); 
    		while(rs.next()) {
    			Date data = rs.getDate("data");
    			String commento = rs.getString("commento");
    			String username = rs.getString("username");
    			Recensione elemento = new Recensione(data,commento,username);
    			lista.add(elemento);	
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
}
