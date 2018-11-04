package util;


import util.Database;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Objects.isNull;
import javax.naming.NamingException;
import java.io.*;
/**
 *
 * @author Federico
 */


public class Utile {
	
	
	/**
     * Restituisce l'id del gioco
     * @param String    			il titolo del gioco
     * @param String    			l'autore del gioco
     * @param String    			l'url del gioco
     * @return                  	l'id del gioco presente nel sistema
     */ 
	public static int checkGioco(String titolo, String autore, String url ) throws Exception {
		int w=0;
		try {
			String condition ="gioco.titolo = '" + titolo + "' AND gioco.creatore = '" + autore + "' AND gioco.url='"+url+"'";
			Database.connect();
			ResultSet r=Database.select("gioco", condition);
			while ( r.next()) {
				w = r.getInt("id");
			}
			Database.close();
		}catch (NamingException e) {
        	System.out.println("checkGioco NamingException: " + e.getMessage());
        } catch (SQLException e) {
        	System.out.println("checkGioco SQLException: " + e.getMessage());
        }
		return w;
	}
	
	
	/**
     * Restituisce l'id dell'utente che possiede l'email e la password passati per argomenti
     * @param String				email dell'utente
     * @param String				password dell'utente non criptata
     * @return                  	l'id dell'utente
     */ 
	
    public static int checkUser(String email, String pass) throws Exception {
        int w = 0;
        try {
            Database.connect();
            if (!isNull(pass)) {
            	System.out.println("CRIPTO");
                pass = crypt(pass);
            }

            String condition = "email = '" + email + "' AND password = '" + pass + "'"; 
            System.out.println(condition);
            ResultSet r = Database.select("utente", condition);        
            while (r.next()) {
                w = r.getInt("id");
            }           
            Database.close();
        } catch (NamingException e) {
        	System.out.println("CheckUser NamingException: " + e.getMessage());
        } catch (SQLException e) {
        	System.out.println("CheckUser SQLException: " + e.getMessage());
        }
        System.out.println("FINE CHECKUSER, risultato >  " + w );
        return w;
    }
    /**
     * Restituisce una booleana che spiega se la password passata in argomento è quella dell'utente che ha
     * come id quello passato in argomento.
     * 
     * @param password			password dell'utente da controllare
     * @param idTrofeo			l'id dell'utente
     * @return                  valore booleano: Se true la password appartiene all'utente, se false no.
     */ 
    public static Boolean checkPsw(String password, int id) throws Exception {
    	boolean isEqual=false;
    	try {
    		Database.connect();
    		if(!isNull(password)) {
    			password = crypt(password);
    			System.out.println(password);
    		}
    		String condition = "utente.id="+id+" AND utente.password='"+password+"'";
    		ResultSet rs=Database.select("utente", condition);
    		while(rs.next()) isEqual=true;
    		Database.close();
    	}catch (NamingException e) {
        	System.out.println("CheckUser NamingException: " + e.getMessage());
        } catch (SQLException e) {
        	System.out.println("CheckUser SQLException: " + e.getMessage());
        }
    	
    	return isEqual;
    }
    
    /**
     * Restituisce il ruolo di un determinato ID utente
     * @param id    			id dell'utente
     * @return                  ruolo dell'utente nella piattaforma
     */ 
    

    public static int checkRuolo(int id) throws Exception{
    		System.out.println("Inizio CheckRuolo");
    		int z=1;
    		try {
    			Database.connect();
    			String condition ="id = '" + id + "'";
    			ResultSet r = Database.select("utente", condition);
    			while (r.next()) {
    				z = r.getInt("ruolo");
    			}
    		}catch(NamingException e) {
    			System.out.println("CheckRuolo NamingException: " + e.getMessage());
            }catch(SQLException e) {
            	System.out.println("CheckRuolo SQLException: " + e.getMessage());
            }
            return z;
    }
    /**
     * Restituisce una booleana che spiega se il trofeo è presente nella lista dei trofei conquistati dall'utente
     * 
     * @param idUtente   		l'id dell'utente
     * @param idTrofeo			l'id del trofeo
     * @return                  valore booleano: Se true, il trofeo è stato già preso dall'utente. Se false no
     */ 
    public static boolean checkTrofeoUtente(int idUtente, int idTrofeo) throws Exception{
    	System.out.println("Inizio checkTrofeoUtente");
    	boolean check = false;
    	try {
    		Database.connect();
    		String condition ="trofeoutente.idUtente="+idUtente+" AND trofeoutente.idTrofeo="+idTrofeo;
    		ResultSet rs = Database.select("trofeoutente", condition);
    		while(rs.next()) check = true; //Esiste un trofeo
    		Database.close();
    	}catch(NamingException e) {
			System.out.println("CheckRuolo NamingException: " + e.getMessage());
        }catch(SQLException e) {
        	System.out.println("CheckRuolo SQLException: " + e.getMessage());
        }
    	
    	return check;
    }
    
    

    /**
     * Restituisce la versione criptata della stringa passata nell'argomento
     * @param string    		stringa da criptare
     * @return                  la stringa criptata, null se avviene l'eccezione
     */ 
    
    public static String crypt(String string) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = string.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }

    }
    
    /**
     * Verifica se una stringa criptata è stata generata da un'altra stringa
     * @param string_crypted    stringa criptata
     * @param to_check          stringa da verificare
     * @return                  true se la password è stata verificata, false altrimenti
     */
    public static boolean decrypt(String string_crypted, String to_check){
        if(to_check == null || string_crypted == null) return false;
        return string_crypted.equals(crypt(to_check));
    }
    
    
    /**
     * crea un codice con lettere e numeri casuali 
     * @return                 	Ritorna il codice/stringa generato.
     */
    public static String creaCodice() {

    	String letC="ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
    	StringBuilder sb = new StringBuilder();
    	Random rand = new Random();
    	for (int i=0; i<15; i++) {
    		sb.append(letC.charAt(rand.nextInt(letC.length())));
    	}

    	return sb.toString();
    }
    
    /**
     * Crea un url di un gioco e verifica che non sia già stato associato ad un altro gioco.
     * @return                  Url da assegnare al gioco (stringa)
     */
    
    public static String checkUrl() {
    	boolean unico=false;
    	String risultato="";
    	while(!unico) {
	    	risultato = creaCodice();
	    	unico =true;
	    	try {
	    		String condition ="gioco.url = 'gamez/" + risultato +"/'";
	    		Database.connect();
	    		ResultSet r=Database.select("gioco", condition);
	    		if(r.next()) {
	    			System.out.println("Esiste già un codice così");
	    			unico = false;
	    		}
	    		Database.close();
	    	}catch (NamingException e) {
	        	System.out.println("checkCodice NamingException: " + e.getMessage());
	        }catch (SQLException e) {
	        	System.out.println("checkCodice SQLException: " + e.getMessage());
	        }catch (Exception e) {
	        	System.out.println("Exception: " + e.getMessage());
	        }
    	}
    	String fine ="gamez/"+risultato+"/";
    	return fine;
    }
    
}
