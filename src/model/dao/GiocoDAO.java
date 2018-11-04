package model.dao;

import java.util.Calendar;
import java.util.List;

import javax.naming.NamingException;

import java.sql.Date;
import java.sql.SQLException;

import model.Gioco;
import util.Database;
import util.Utile;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * 
 * @author Federico Tersigni
 *
 */


public class GiocoDAO implements GiocoDAO_interface{

	/**
	 * inserisci un nuovo gioco nel database
	 * 
	 * @param descrizione				descrizione del gioco
	 * @param url						path del gioco nel server
	 * @param titolo					titolo del gioco
	 * @param creatore					nome del creatore
	 * @param fileGioco					nome del file di gioco salvato nel server
	 * @param fileThumb					nome dell'icona salvata nel server
	 * @return					
	 */
	public static void insert(String descrizione, String url, String titolo, String creatore, String fileGioco, String fileThumb) {
			Map<String, Object> map = new HashMap<String,Object>();
			Calendar calendar = Calendar.getInstance();
			Date dataInvio = new Date(calendar.getTime().getTime());
			map.put("titolo",titolo);
        	map.put("descrizione", descrizione);
        	map.put("creatore", creatore);
			map.put("dataInvio", dataInvio);
			map.put("url", url);
			map.put("fileGioco", fileGioco);
			map.put("fileThumb", fileThumb);
			try {								
				Database.connect();
				Database.insertRecord("gioco", map);
				Database.close();
			}catch(NamingException e) {
	    		System.out.println("namingException " +e);
	        }catch (SQLException e) {
	        	System.out.println("sqlException " +e);
	        }catch (Exception e) {
	        	System.out.println("Exception " + e);
	        }
			map.clear();
	}
	
	/**
	 * cancella un gioco nel DB
	 * 
	 * @param id				id del gioco
	 *
	 * @return					
	 */
	public static void delete(int id) {
		try {
			Database.connect();
			Database.deleteRecord("gioco", "gioco.id="+id);
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
	 * Aggiorna un gioco nel database
	 * 
	 * @param id						id del gioco
	 * @param descrizione				descrizione del gioco
	 * @param url						path del gioco nel server
	 * @param titolo					titolo del gioco
	 * @param creatore					nome del creatore
	 * @param fileGioco					nome del file di gioco salvato nel server
	 * @param fileThumb					nome dell'icona salvata nel server
	 * @return					
	 */
	public static void update(int id,String descrizione, String url, String titolo, String creatore, String fileGioco, String fileThumb) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("titolo",titolo);
    	map.put("descrizione", descrizione);
    	map.put("creatore", creatore);
		map.put("url", url);
		map.put("fileGioco", fileGioco);
		map.put("fileThumb", fileThumb);
		try {								
			Database.connect();
			Database.updateRecord("gioco", map,"gioco.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println("namingException " +e);
        }catch (SQLException e) {
        	System.out.println("sqlException " +e);
        }catch (Exception e) {
        	System.out.println("Exception " + e);
        }
		map.clear();
	}
	/**
	 * cerca giochi nel database in base alla stringa passata in argomento
	 * 
	 * @param nome					titolo del gioco
	 * 
	 * @return						lista dei giochi che viene ottenuta
	 */
	public static List<Gioco> lista_Giochi_nome(String nome){ // Per la ricerca dei giochi
		ArrayList<Gioco> game=null;
		try {
			Database.connect();
    		ResultSet rs = Database.selectRecord("gioco","titolo LIKE '"+nome+"%'","gioco.titolo"); // qui bisogna mettere gli elementi della query
    		game = new ArrayList<Gioco>(); // bisogna aggiungere "Gioco" in database
    		while(rs.next()) {
    			int id = rs.getInt("id");
    			Date dataInvio = rs.getDate("dataInvio");
    			String descrizione = rs.getString("descrizione");
    			String url = rs.getString("url");
    			String titolo = rs.getString("titolo"); 
    			String creatore = rs.getString("creatore");
    			String fileGioco = rs.getString("fileGioco");
    			String fileThumb = rs.getString("fileThumb");
    			Gioco ngame = new Gioco(id,dataInvio,descrizione,url,titolo,creatore,fileGioco,fileThumb);
    			System.out.println(ngame.toString());
    			game.add(ngame);	
    		}
			Database.close();			
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return game;
	}
	
	/**
	 * ritorna il titolo del gioco
	 * 
	 * @param id					id del gioco
	 * 
	 * @return						il titolo del gioco
	 */
	public static String getTitoloGioco(int idGioco) {
		String titolo="";
		try {
			Database.connect();
    		ResultSet rs = Database.select("gioco","gioco.id="+idGioco); // qui bisogna mettere gli elementi della query
    		while(rs.next()) {
    			titolo = rs.getString("titolo");   			
    		}
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return titolo;
	}
	
	/**
	 * Ritorna la lista degli ultimi giochi inseriti nel sito
	 * 
	 * @return						lista dei giochi che viene ottenuta
	 */
	public static List<Gioco> lista_ultimi5giochi_inseriti(){
		ArrayList<Gioco> game=null;
		try {
			Database.connect();
    		ResultSet rs = Database.selectRecord("gioco","gioco.dataInvio DESC LIMIT 5"); 
    		game = new ArrayList<Gioco>(); 
    		while(rs.next()) {
    			int id = rs.getInt("id");
    			Date dataInvio = rs.getDate("dataInvio");
    			String descrizione = rs.getString("descrizione");
    			String url = rs.getString("url");
    			String titolo = rs.getString("titolo"); 
    			String creatore = rs.getString("creatore");
    			String fileGioco = rs.getString("fileGioco");
    			String fileThumb = rs.getString("fileThumb");
    			Gioco ngame = new Gioco(id,dataInvio,descrizione,url,titolo,creatore,fileGioco,fileThumb);
    			game.add(ngame);
    			System.out.println(ngame.toString());
    		
    		}
			Database.close();
			
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return game;
	}
	/**
	 * Ritorna la lista di tutti i giochi del sito
	 * 
	 * @return						lista dei giochi che viene ottenuta
	 */
	public static List<Gioco> lista_Giochi(){
		ArrayList<Gioco> game=null;
		try {
			Database.connect();
    		ResultSet rs = Database.selectRecord("gioco","titolo");
    		game = new ArrayList<Gioco>();
    		while(rs.next()) {
    			int id = rs.getInt("id");
    			Date dataInvio = rs.getDate("dataInvio");
    			String descrizione = rs.getString("descrizione");
    			String url = rs.getString("url");
    			String titolo = rs.getString("titolo"); 
    			String creatore = rs.getString("creatore");
    			String fileGioco = rs.getString("fileGioco");
    			String fileThumb = rs.getString("fileThumb");
    			Gioco ngame = new Gioco(id,dataInvio,descrizione,url,titolo,creatore,fileGioco,fileThumb);
    			game.add(ngame);	
    			System.out.println(ngame.toString());
    		}
			Database.close();			
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return game;
	}
}
