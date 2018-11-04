package model.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Esperienza;
import model.Gioco;
import model.Livello;
import util.Database;
/**
 * 
 * @author Federico Tersigni
 *
 */
public class LivelloDAO implements LivelloDAO_interface{
	
	/**
	 * Metodo che inserisce un livello nel DB
	 * @param livelloTot			valore del livello
	 * @param expMax				limite di punti esperienza per superare il livello
	 * 
	 * return
	 */
	public static void insert(int livelloTot, int expMax) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("livelloTot", livelloTot);
		map.put("expMax", expMax);
		try {
			Database.connect();
			Database.insertRecord("livello", map);
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
	 * Metodo che aggiorna un livello nel DB
	 * @param id				id del livello
	 * @param livelloTot		valore del livello
	 * @param expMax			limite di punti esperienza per superare il livello
	 * 
	 */
	public static void update(int id,int livelloTot, int expMax) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("livelloTot", livelloTot);
		map.put("expMax", expMax);
		try {
			Database.connect();
			Database.updateRecord("livello", map, "livello.id="+id);
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
	 * metodo che cancella un determinato livello dal DB
	 * 
	 * @param id			id del livello da cancellare
	 */
	public static void delete(int id) {
		try {
			Database.connect();
			Database.deleteRecord("livello", "livello.id="+id);
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
	 * Metodo che ritorna una map<String,Integer> che contiene i dettagli di quel determinato livello
	 * 
	 * @param idLivello				l'id del livello da esaminare
	 * @return						la map<String, Integer> che contiene i dati
	 */
	public static Map<String, Integer> dettagli_livello(int idLivello){
		Map<String, Integer> map = new HashMap<String,Integer>();
		try {
			Database.connect();
			ResultSet rs = Database.selectRecord("livello", "livello.id="+idLivello, "livello.livelloTot DESC LIMIT 1");
			while(rs.next()) {
				int id = rs.getInt("id");
				int livelloTot = rs.getInt("livelloTot"); 
				int expMax = rs.getInt("expMax");
				map.put("id", id);
				map.put("livelloTot",livelloTot);
		    	map.put("expMax", expMax);
			}
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return map;
	}
	
	/**
	 * Ritorna l'intera lista dei livelli presente nel DB che hanno un valore minore del valore del livello in argomento
	 * @param livello					Valore del livello (per esempio 1° livello, 2° livello, ecc)
	 * @return							List<Livello> che contiene i livelli minori
	 */
	public static List<Livello> lista_Livelli(int livello){ 
		ArrayList<Livello> lista=null;
		try {
			Database.connect();   		
    		ResultSet rs = Database.selectRecord("livello","livello.livelloTot <= "+livello, "livello.livelloTot");
    		lista = new ArrayList<Livello>(); 
    		while(rs.next()) {
    			int livelloTot = rs.getInt("livelloTot");
    			int expMax = rs.getInt("expMax");
    			Livello lvl = new Livello(livelloTot,expMax);
    			lista.add(lvl);   		
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
