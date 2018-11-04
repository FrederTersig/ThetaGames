package servlets;

import util.Database;
import util.FreeMarker;
import util.SecurityLayer;
import util.Utile;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Gioco;
import model.Utente;
import model.dao.EsperienzaDAO;
import model.dao.GiocoDAO;
import model.dao.LivelloDAO;
import model.dao.TrofeoDAO;
import model.dao.VotoDAO;
/**
 * @author Federico
 */
public class RicercaGiochi extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente
    public int ruolo=1; //ruolo dell'utente {1=normale,2=moderatore,3=admin}
    public Utente utente;
    /**
     * Processes requests di RicercaGiochi
     *
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	System.out.println("process Request di Ricerca Giochi");
    	HttpSession s = SecurityLayer.checkSession(request);

    	
    	if(s != null){//condizione per vedere se la sessione esiste. 
            if(s.getAttribute("id") != null && s.getAttribute("utente") != null){
                id = (int) s.getAttribute("id");
  
                utente = (Utente) s.getAttribute("utente");
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
    	String ricercaStr = (String) request.getSession(true).getAttribute("ricerca");
    	
    	if(ricercaStr != "") {
    		data.put("ricerca", ricercaStr);
    		ArrayList<Gioco> lista = (ArrayList<Gioco>) GiocoDAO.lista_Giochi_nome(ricercaStr);
    		int totElementi = lista.size();
    		for(int i=0; i<totElementi; i++) {
        		int idGioco = lista.get(i).getId();
        		double mediaVoti= VotoDAO.mediaVotiGioco(idGioco);
        		lista.get(i).setMediavoto(mediaVoti);
        	}
    		data.put("giochi", lista);
    		data.put("totElementi", totElementi);

    	}else {
    		data.put("ricerca", "");
    		ArrayList<Gioco> lista = (ArrayList<Gioco>) GiocoDAO.lista_Giochi();
    		int totElementi = lista.size();
    		for(int i=0; i<totElementi; i++) {
        		int idGioco = lista.get(i).getId();
        		double mediaVoti= VotoDAO.mediaVotiGioco(idGioco);
        		lista.get(i).setMediavoto(mediaVoti);
        	}
    		data.put("giochi", lista);
    		data.put("totElementi", totElementi);
    	}
	
    	
    	
    	System.out.println("-----------Prima del FreeMarker RICERCA GIOCHI------------");
        FreeMarker.process("ricercaGiochi.html", data, response, getServletContext());      
    }
    
    /**
     * doGet di RicercaGiochi
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get Ricerca Giochi");
        //HttpSession session = request.getSession();
        //session.invalidate();
        //id=0;
        try {
            processRequest(request, response);
        } catch (Exception e) {
           e.printStackTrace();
        }
	}

    /**
     * doPost di RicercaGiochi
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs 
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("****************Post di RicercaGiochi.java!*******************");
		String action = request.getParameter("value");
		if("back".equals(action)) {
			HttpSession s = SecurityLayer.checkSession(request);
			if(s != null) {
				
				if(s.getAttribute("utente") != null) data.put("utente", s.getAttribute("utente")); 
				else System.out.println("Premuto il tasto Home -> La sessione esiste -> non esiste un attributo 'utente'");
				System.out.println( s.getAttribute("utente") );
			}
			FreeMarker.process("home.html", data, response, getServletContext());
		}else if("gioco".equals(action)){
            System.out.println("Cliccato Gioco, procediamo!");          
	           
	        int idGioco = Integer.parseInt(request.getParameter("dettagliGioco"));
	        HttpSession s = SecurityLayer.checkSession(request);
	    	if(s != null){//condizione per vedere se la sessione esiste. 
	            
	            s.setAttribute("idGioco", idGioco);
			    System.out.println("ID del gioco cliccato è: --> " + idGioco);
			    response.sendRedirect("detGioco");        
	        }

        }
		
	}
}
