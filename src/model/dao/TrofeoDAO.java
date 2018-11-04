package model.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Gioco;
import model.Trofeo;
import util.Database;
/**
 * 
 * @author Federico Tersigni
 *
 */
public class TrofeoDAO implements TrofeoDAO_interface{
	/**
	 * Inserisce trofeo nel database
	 * @param nome					nome del trofeo
	 * @param descrizione			descrizione del trofeo
	 * @param idGioco				id del gioco a cui è legata
	 * 
	 * @return						
	 */
	public static void insert(String nome, String descrizione, int idGioco) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("titolo", nome);
		map.put("descrizione", descrizione);
		map.put("idGioco", idGioco);
		try {
			Database.connect();
			Database.insertRecord("trofeo", map);
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
	 * cancella trofeo dal DB
	 * 
	 * @param id			id del trofeo da cancellare
	 * 
	 * @return						
	 */
	public static void delete(int id) {
		try {
			Database.connect();
			Database.deleteRecord("trofeo", "trofeo.id="+id);
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
	 * aggiorna le informazioni del trofeo nel DB
	 * 
	 * @param id					id del trofeo
	 * @param nome					nome del trofeo
	 * @param descrizione			descrizione del trofeo
	 * 
	 * @return						
	 */
	public static void update(int id,String nome, String descrizione) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("titolo", nome);
		map.put("descrizione", descrizione);
		try {
			Database.connect();
			Database.updateRecord("trofeo", map, "trofeo.id="+id);
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
	 * ritorna il livello attuale dell'utente
	 * 
	 * @param idUtente			id dell'utente
	 * 
	 * @return					l'id del livello che ha l'utente				
	 */
	public static int  getIDLivelloAttuale(int idUtente){
		int idLivelloUtente = 0;
		try {
			Database.connect();		
			ResultSet rs = Database.selectJoin("trofeo.idLivello", "trofeo", "trofeoutente", "trofeo.id = trofeoutente.idTrofeo", "trofeoutente.idUtente=1 AND trofeo.idLivello IS NOT NULL", "trofeo.idLivello DESC LIMIT 1");
			while(rs.next()) {
				idLivelloUtente = rs.getInt("idLivello");
			}				
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return idLivelloUtente;
	}
	
	/**
	 * ritorna l'id del trofeo che corrisponde ad un determinato livello
	 * 
	 * @param livelloTot			il valore del livello (1° livello, 2°livello, ecc)
	 * 
	 * @return						l'id del trofeo
	 */
	public static int getIdTrofeoDaLVL(int livelloTot) {
		int idTrofeo=0;
		try {
			Database.connect();   		
    		ResultSet rs = Database.selectJoin("trofeo.id", 
    				"trofeo", "livello", "livello.livelloTot= "+livelloTot+" AND trofeo.idLivello = livello.id", "trofeo.id");
    		
    		while(rs.next()) {
    			idTrofeo= rs.getInt("id");		
    		}
			Database.close();			
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return idTrofeo;
	}
	
	/**
	 * metodo che ritorna la lista dei trofei di un utente
	 * 
	 * @param idutente			id dell'utente
	 * 
	 * @return					la lista dei trofei dell'utente
	 */
	public static List<Trofeo> lista_trofei_utente(int idutente){
		ArrayList<Trofeo> lista=null;
		try {
			Database.connect();   		
    		ResultSet rs = Database.selectJoin("trofeo.id, trofeo.descrizione, trofeo.titolo, trofeoutente.data", 
    				"trofeo", "trofeoutente", "trofeoutente.idutente= "+idutente+" AND trofeo.id = trofeoutente.idTrofeo", "trofeoutente.data");
    		lista = new ArrayList<Trofeo>();
    		while(rs.next()) {
    			int id = rs.getInt("id");
    			Date data = rs.getDate("data");
    			String descrizione = rs.getString("descrizione");
    			String titolo = rs.getString("titolo"); 

    			Trofeo badge = new Trofeo(id,titolo,descrizione,data);
    			lista.add(badge);   		
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
	 * metodo che ritorna la lista dei trofei di un gioco
	 * 
	 * @param idGioco			l'id del gioco
	 * 
	 * @return					la lista dei trofei assegnati ad un gioco
	 */
	public static List<Trofeo> lista_trofei_gioco(int idGioco){
		ArrayList<Trofeo> lista=null;
		try {
			Database.connect();   		
    		ResultSet rs = Database.select("trofeo", "trofeo.idGioco="+idGioco);
    		lista = new ArrayList<Trofeo>(); // bisogna aggiungere "Gioco" in database
    		while(rs.next()) {
    			int id = rs.getInt("id");
    			//Date data = rs.getDate("data");
    			String descrizione = rs.getString("descrizione");
    			String titolo = rs.getString("titolo"); 

    			Trofeo badge = new Trofeo(id,titolo,descrizione);
    			lista.add(badge);   		
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
	 * metodo che ritorna la lista totale dei trofei associati ai giochi (quindi senza quelli associati ai livelli O il trofeo
	 * di benvenuto)
	 * 
	 * @param 
	 * 
	 * @return					la lista dei trofei associati ai giochi
	 */
	public static List<Trofeo> lista_trofei_assoc(){
		ArrayList<Trofeo> lista = null;
		try {
			Database.connect();   		
			
    		ResultSet rs = Database.selectRecord("trofeo", "trofeo.idGioco IS NOT NULL AND trofeo.idLivello IS NULL", "trofeo.titolo");
    		lista = new ArrayList<Trofeo>();
    		while(rs.next()) {
    			int id = rs.getInt("id");
    			String descrizione = rs.getString("descrizione");
    			String titolo = rs.getString("titolo"); 
    			int idGioco = rs.getInt("idGioco");
    			Trofeo badge = new Trofeo(id,titolo,descrizione, idGioco);
    			lista.add(badge);   		
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
	 * ritorna l'id del trofeo specifico associato ad un gioco
	 * 
	 * @param idGioco			id del gioco
	 * @param titolo			titolo del gioco
	 * 
	 * @return					l'id del trofeo
	 */
	public static int getIdTrofeo(int idGioco, String titolo){
		int x=0;
		String condition="trofeo.idGioco="+idGioco+" AND trofeo.titolo='"+titolo+"'";
		try {
			Database.connect();
			ResultSet rs= Database.select("trofeo", condition);
			while(rs.next()){
				x = rs.getInt("id");
			}
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return x;
		
	}
}
