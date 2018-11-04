package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.Database;
import util.FreeMarker;
import util.SecurityLayer;
import util.Utile;

import model.Gioco;
import model.Utente;
import model.Recensione;
import model.Trofeo;
import model.dao.EsperienzaDAO;
import model.dao.LivelloDAO;
import model.dao.RecensioneDAO;
import model.dao.TrofeoDAO;
import model.dao.UtenteDAO;
import model.dao.VotoDAO;
/**
 * @author Federico Tersigni
 */
@WebServlet("/DetGioco")
public class DetGioco extends HttpServlet {
	
    Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente
    public int idGioco=0;
    public Utente utente;
    /**
     * Processes requests di DetGioco
     *
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	System.out.println("process Request del DetGioco");
    	HttpSession s = SecurityLayer.checkSession(request);
    	//Check della sessione    	

    	if(s != null){//condizione per vedere se la sessione esiste. 
            if(s.getAttribute("id") != null && s.getAttribute("utente") != null && s.getAttribute("idGioco") != null){
                id = (int) s.getAttribute("id");
                s.removeAttribute("ricerca");     
                utente = (Utente) s.getAttribute("utente");
                idGioco = (int) s.getAttribute("idGioco");
            }else{
                id=0;
                FreeMarker.process("home.html", data, response, getServletContext());
            }       
        }else{
            id = 0;
            FreeMarker.process("home.html", data, response, getServletContext());
        } 
    	data.put("id", id);  
    	data.put("utente", utente);
    	
    	
    	System.out.println("Creo l'oggetto Gioco");
    	Gioco game = null; 	
    	try {
    		Database.connect();
    		ResultSet rs = Database.select("gioco", "gioco.id="+idGioco);
    		if(rs.next()) {
	    		String titolo = rs.getString("titolo");
	    		Date dataRilascio = rs.getDate("dataInvio");
	    		String descrizione = rs.getString("descrizione");
	    		String url = rs.getString("url");
	    		String autore = rs.getString("creatore");
	    		String fileGioco = rs.getString("fileGioco");
    			String fileThumb = rs.getString("fileThumb");
	    		game = new Gioco(idGioco, dataRilascio, descrizione, url, titolo, autore,fileGioco,fileThumb);
    		}
    		Database.close();
    		
    	}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);        
        }
    	
		data.put("gioco", game);
		
		//lista Trofei presenti per il gioco
		ArrayList<Trofeo> giocoTrofei = (ArrayList<Trofeo>) TrofeoDAO.lista_trofei_gioco(idGioco);
		data.put("trofei", giocoTrofei);
		
		
		//parte della media VOTI
		double mediaVoti= VotoDAO.mediaVotiGioco(idGioco);
		data.put("mediaVoti", mediaVoti);
		
		//parte della lista delle RECENSIONI
    	ArrayList<Recensione> recensioni = (ArrayList<Recensione>) RecensioneDAO.recensioni_Gioco(idGioco);
    	int totRecensioni = recensioni.size();
    	data.put("totRecensioni", totRecensioni);
    	if(totRecensioni != 0) {
	    	data.put("recensioni", recensioni);	    	
	    	System.out.println("L'id del gioco >>>> " + idGioco + " ||| NUMERO DI ELEMENTI ::: " + totRecensioni);
    	}
    	
    	
        FreeMarker.process("detGioco.html", data, response, getServletContext());      
    }

    /**
     * doGet di DetGioco
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get di DetGioco!");

        try {
            processRequest(request, response);
        } catch (Exception e) {
           e.printStackTrace();
        }
	}

	/**
     * doPost di DetGioco
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs 
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("POST di dettGioco.java!!");
        String action = request.getParameter("value");
        if("newExp".equals(action)) {
        	String valore = request.getParameter("input");
        	int idEsperienza=0;
        	switch(valore){
        		case "easy": 	idEsperienza=1;
        						break;
        		case "medium":	idEsperienza=2;
        						break;
        		case "hard":	idEsperienza=3;
        						break;
        		case "vhard":	idEsperienza=4;
        						break;
        		default:		idEsperienza=0;
        						break;       		
        	}
        	Calendar calendar = Calendar.getInstance();
            Date dataAssExp = new Date(calendar.getTime().getTime()); 
            UtenteDAO.addEsperienzaUtente(id, idEsperienza, dataAssExp);

            int expGioc = EsperienzaDAO.conteggio_esperienza(id);  
            int idLivelloAttuale = TrofeoDAO.getIDLivelloAttuale(id);
            int expLimit=0;
            int lvlGioc=0;
            Map<String, Integer> mapLiv = new HashMap<String,Integer>();
            mapLiv = LivelloDAO.dettagli_livello(idLivelloAttuale);
            for(Map.Entry<String,Integer> e:mapLiv.entrySet()){
            	String attr = e.getKey();
                int value = e.getValue();
                System.out.println(attr);
                System.out.println(value);
                if(attr.equals("livelloTot")) {
                	lvlGioc=value;
                }else if(attr.equals("expMax")) {
                	expLimit=value;
                }
            }
            if(expGioc > expLimit) {    
            	int idTrofeo = TrofeoDAO.getIdTrofeoDaLVL(lvlGioc+1);
            	try {
    				if(!Utile.checkTrofeoUtente(id,idTrofeo)) {
    					System.out.println("Sei passato di livello!!");
    	            	
    	            	UtenteDAO.addTrofeoUtente(id, idTrofeo, dataAssExp);
    	            	System.out.println("Passaggio avvenuto! hai il trofeo!");
    				}
    				
    			} catch (Exception e) {
    				System.out.println("179 detGioco : "+e);
    			}
         	
            }
        }else if("inviaRecensione".equals(action)){
        	System.out.println("invia la recensione scritta dall'utente!");
        	String testoRecensione = request.getParameter("input");
        	System.out.println(testoRecensione);
        	if(!testoRecensione.equals("")) {
        		System.out.println("Il testo della recensione NON è vuoto");
        		RecensioneDAO.insert(testoRecensione, idGioco, id, 0);
        	}
        }else if("inviaVoto".equals(action)) {
        	System.out.println("invia il voto!");
        	String valore = request.getParameter("input");
        	Integer valoreVoto = Integer.valueOf(valore);
        	//incomincio la insert dell'invia voto : SE è già presente un voto per questo gioco si fa l'update altrimenti insert
        	if(VotoDAO.esisteVoto(idGioco, id)==false) { // Se FALSE devi fare la insert del nuovo voto
        		System.out.println("procedo con l'inserimento!! "+idGioco+"_"+id+"_"+valoreVoto);
        		VotoDAO.insert(idGioco, id, valoreVoto);
        	}else{
        		System.out.println("procedo con l'update "+idGioco+"_"+id+"_"+valoreVoto);
        		VotoDAO.update(idGioco, id, valoreVoto);
        	}
        	System.out.println("Ho finito di mettere il voto");
        }else if("addTrofeo".equals(action)) {
        	System.out.println("Acquisito nuovo trofeo!");
        	String valore = request.getParameter("input");
        	//Inserisci nel DB il nuovo trofeo riconosciuto dal titolo allo specifico giocatore // trofeoUtente
        	HttpSession s = SecurityLayer.checkSession(request);
        	if(idGioco==0 || id == 0) {	
        		if(s != null){//condizione per vedere se la sessione esiste. 
            		if(s.getAttribute("id") != null){
                        id = (int) s.getAttribute("id");
                    }else{
                    	System.out.println("detGioco215");
                    	response.sendRedirect("");
                    }
                   
                    if(s.getAttribute("idGioco") != null){
                        idGioco = (int) s.getAttribute("idGioco");
                    }else{
                        response.sendRedirect("");
                    }        
                }else {
                	response.sendRedirect("");
                }
        		
        	}
        	int codiceTrofeo=TrofeoDAO.getIdTrofeo(idGioco, valore);
        	try {
				if(!Utile.checkTrofeoUtente(id,codiceTrofeo)) {
					Calendar calendar = Calendar.getInstance();
		            Date dataAssT = new Date(calendar.getTime().getTime()); 
					UtenteDAO.addTrofeoUtente(id, codiceTrofeo, dataAssT);
					System.out.println("Assegnato il trofeo all'utente che sta giocando!!");
				}
				
			} catch (Exception e) {
				System.out.println("301 detGioco : "+e);
			}
        }
	}

}
