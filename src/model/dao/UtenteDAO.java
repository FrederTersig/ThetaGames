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

import model.Recensione;
import model.Utente;
import util.Database;


/**
 * 
 * @author Federico Tersigni
 *
 */


public class UtenteDAO implements UtenteDAO_interface {
	/**
	 * inserisce un nuovo utente nel DB
	 * 
	 * @param nome				nome dell'utente
	 * @param cognome			cognome dell'utente
	 * @param email				email dell'utente
	 * @param citta				citta dell'utente
	 * @param dataNascita		data di nascita dell'utente
	 * @param username			username dell'utente
	 * @param password			password dell'utente
	 * @param ruolo				ruolo dell'utente nel sito (1 utente normale, 2 moderatore, 3 admin)
	 * @param dataSignup		data iscrizione utente nel sito
	 * @return					
	 */
	
	public static void insert(String nome, String cognome, String email, String citta, Date dataNascita, String username, String password, int ruolo, Date dataSignup ) {
		Map<String, Object> map = new HashMap<String, Object>();       
    	map.put("nome", nome);
    	map.put("cognome", cognome);
    	map.put("email", email);
    	map.put("citta", citta);
    	map.put("dataNascita", dataNascita);
    	map.put("username", username);
    	map.put("password", crypt(password));
    	map.put("ruolo", ruolo); // ruolo dell'utente normale 
    	map.put("dataSignup", dataSignup);
		try {
			System.out.println("Non esiste un altro utente con queste credenziali nel DB");
			Database.connect();
    		Database.insertRecord("utente", map);

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
	 * cancella un utente dal DB
	 * 
	 * @param id			id dell'utente
	 * @return					
	 */
	public static void delete(int id) {
		try {
			Database.connect();
			Database.deleteRecord("utente", "utente.id="+id);
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
	 * aggiorna un utente esistente nel db
	 * 
	 * @param nome				nome dell'utente
	 * @param cognome			cognome dell'utente
	 * @param email				email dell'utente
	 * @param citta				citta dell'utente
	 * @param dataNascita		data di nascita dell'utente
	 * @param username			username dell'utente
	 * @param password			password dell'utente
	 * @param ruolo				ruolo dell'utente nel sito (1 utente normale, 2 moderatore, 3 admin)
	 * @param dataSignup		data iscrizione utente nel sito
	 * @return					
	 */
	public static void update(int id, String nome, String cognome, String email, String citta, Date dataNascita, String username, String password, int ruolo, Date dataSignup ) {
		Map<String, Object> map = new HashMap<String, Object>();
        
    	map.put("nome", nome);
    	map.put("cognome", cognome);
    	map.put("email", email);
    	map.put("citta", citta);
    	map.put("dataNascita", dataNascita);
    	map.put("username", username);
    	map.put("password", crypt(password));
    	map.put("ruolo", ruolo); // ruolo dell'utente normale 
    	map.put("dataSignup", dataSignup);
		try {
			Database.connect();
    		Database.updateRecord("utente", map, "utente.id="+id);
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
	 * aggiorna la password dell'utente
	 * 
	 * @param password			password dell'utente
	 * @param id				id dell'utente
	 * @return					
	 */
	public static void updatePsw(String password, int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		String condition="utente.id="+id;
		try {
			Database.connect();
			map.put("password", crypt(password));
    		Database.updateRecord("utente", map, condition);
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
	 * aggiorna l'email dell'utente
	 * 
	 * @param email				email dell'utente
	 * @param id				id dell'utente
	 * @return					
	 */
	public static void updateEmail(String email, int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		String condition="utente.id="+id;
		try {
			Database.connect();
			map.put("email", email);
    		Database.updateRecord("utente", map, condition);
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
	 * promuove il ruolo dell'utente a moderatore
	 * 
	 * @param id				id dell'utente
	 * @return					
	 */
	public static void promuovi_utente(int id) {
		try {
		Database.connect();
		Database.updateRecord("utente", "utente.ruolo=2", "utente.id="+id); 		
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
	 * promuove il ruolo del moderatore ad admin
	 * 
	 * @param id				id dell'utente
	 * @return					
	 */
	public static void promuovi_mod(int id) {
		try {
		Database.connect();
		Database.updateRecord("utente", "utente.ruolo=3", "utente.id="+id); 		
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
	 * retrocede il ruolo del moderatore ad utnete
	 * 
	 * @param id				id dell'utente
	 * @return					
	 */
	public static void retrocedi_mod(int id) {
		try {
		Database.connect();
		Database.updateRecord("utente", "utente.ruolo=1", "utente.id="+id); 		
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
	 * retrocede il ruolo dell'amministratore a moderatore
	 * 
	 * @param id				id dell'utente
	 * @return					
	 */
	public static void retrocedi_admin(int id) {
		try {
		Database.connect();
		Database.updateRecord("utente", "utente.ruolo=2", "utente.id="+id); 		
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
	 * aggiungi un trofeo all'utente specifico
	 * 
	 * @param idUtente				id dell'utente
	 * @param idTrofeo				id del trofeo
	 * @param data					data in cui è stato acquisito questo trofeo
	 * @return					
	 */
	public static void addTrofeoUtente(int idUtente, int idTrofeo, Date data) {
		Map<String, Object> map = new HashMap<String, Object>();
    	//Il nuovo utente  parte al 1° livello con 0 punti esperienza totali
        
    	map.put("idUtente", idUtente);
    	map.put("idTrofeo", idTrofeo);
    	map.put("data", data);     
    	try {
			Database.connect();
    		Database.insertRecord("trofeoutente", map);
    		Database.close();
    		
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
		map.clear();
	}
	
	/**
	 * aggiungi un quantitativo di punti esperienza all'utente
	 * 
	 * @param idUtente				id dell'utente
	 * @param idEsperienza			id del quantitativo di punti esperienza
	 * @param data					data in cui è stato acquisito questo quantitativo di punti esperienza
	 * @return					
	 */
	public static void addEsperienzaUtente(int idUtente, int idEsperienza, Date data) {
    	
    	System.out.println("Sto per mandare i PX");
    	Map<String, Object> map = new HashMap<String, Object>();        		
    	map.put("idUtente", idUtente);
    	map.put("idEsperienza", idEsperienza);
    	map.put("data", data);      	
        try {
			Database.connect();
			Database.insertRecord("exputente", map);
			Database.close();
			map.clear();
    	}catch (NamingException e) {
            System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);
        }
		
	}

	/**
	 * metodo che ritorna la lista degli utenti del sistema
	 * 
	 * 
	 * @return			 lista degli utenti	
	 */
	public static List<Utente> lista_utenti(){
		ArrayList<Utente> lista=null;
		try {
			Database.connect();
			ResultSet rs = Database.selectRecord("utente", "utente.username");
    		lista = new ArrayList<Utente>();
    		while(rs.next()) {
    			int id = rs.getInt("id");
    			int ruolo = rs.getInt("ruolo");
    			String username = rs.getString("username");
    			String email = rs.getString("email");
    			Date dataSignup = rs.getDate("dataSignup");
    			
    			Utente elemento = new Utente(id,ruolo,username,email,dataSignup);
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
