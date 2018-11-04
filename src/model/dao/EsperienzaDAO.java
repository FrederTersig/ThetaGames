package model.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import model.Esperienza;
import model.Trofeo;
import util.Database;
	/**
	 * 
	 * @author Federico Tersigni
	 *
	 */
public class EsperienzaDAO implements EsperienzaDAO_interface{
	/**
	 * conta e calcola la somma totale di tutta l'esperienza maturata dall'utente
	 * 
	 * @param idutente			id dell'utente
	 * @return					il totale dell'operazione
	 */
	public static int conteggio_esperienza(int idutente) {
		int totale=0;
		try {
			Database.connect();   		
    		ResultSet rs = Database.selectJoin("esperienza.totPunti", 
    				"esperienza", "exputente", "exputente.idutente= "+idutente+" AND esperienza.id = exputente.idEsperienza", "esperienza.totPunti");
    		while(rs.next()) {
    			int punti = rs.getInt("totPunti");
    			totale += punti;
    		}
			Database.close();			
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return totale;
	}
	
	/**
	 * Mostra un elenco dell'esperienza maturata dall'utente
	 * 
	 * @param idutente			id dell'utente
	 * @return					la lista di tutta l'esperienza che ha maturato l'utente
	 */
	public static List<Esperienza> listaEsperienza_utente(int idutente){
			ArrayList<Esperienza> lista=null;
			try {
				Database.connect();   		
	    		ResultSet rs = Database.selectJoin("esperienza.id, esperienza.totPunti, exputente.data", 
	    				"esperienza", "exputente", "exputente.idutente= "+idutente+" AND esperienza.id = exputente.idEsperienza", "exputente.data");
	    		lista = new ArrayList<Esperienza>(); 
	    		while(rs.next()) {
	    			int id = rs.getInt("id");
	    			int totPunti = rs.getInt("totPunti");
	    			Date data = rs.getDate("data");

	    			Esperienza exp = new Esperienza(id,totPunti,data);
	    			lista.add(exp);   		
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
