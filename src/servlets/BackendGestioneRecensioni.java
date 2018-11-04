package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.FreeMarker;
import util.SecurityLayer;

import model.Gioco;
import model.Utente;
import model.Recensione;
import model.dao.RecensioneDAO;
/**
 * @author Federico
 */
@WebServlet("/BackendGestioneRecensioni")
public class BackendGestioneRecensioni extends HttpServlet {
    Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente
    public int totRecensioni=0;
    
    /**
     * Processes requests di BackendGestioneTrofeo
     *
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	System.out.println("process Request di BackendGestioneRecensioni");
    	HttpSession s = SecurityLayer.checkSession(request);
    	System.out.println("fine checkSession");
    	//Check della sessione
    	if(s != null){//condizione per vedere se la sessione esiste. 
            if(s.getAttribute("id") != null){
                id = (int) s.getAttribute("id");
            }else{
                id=0;
            }
            System.out.println("ID ?? > " + id );
            data.put("id", id);    
        }else{
            id = 0;
            data.put("id", id);
        }  
    	//Fine check della sessione
    	ArrayList<Recensione> recValutare = (ArrayList<Recensione>) RecensioneDAO.recensioni_daValutare();
    	totRecensioni = recValutare.size();
    	data.put("totRecensioni", totRecensioni);
    	data.put("recensioni", recValutare);
    	// Bisogna prendere il titolo del gioco e il nickname.
        FreeMarker.process("backendGestioneRecensioni.html", data, response, getServletContext());
    }

    /**
     * doGet di BackendGestioneRecensioni
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get del BackendGestioneRecensioni!");
 
        try {
            processRequest(request, response);
        } catch (Exception e) {
           e.printStackTrace();
        }
	}

	/**
     * doPost di BackendGestioneRecensioni
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs 
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("POST di BACKEND! ********************************************************************* --> " +id );
        //String action = request.getParameter("value");
        //System.out.println("*********** >> " +action+ " << ***********");
        //prova per la checkbox
        String[] recValide = request.getParameterValues("validaRecensione");
        if(recValide != null) {
        	System.out.println(recValide.length+" ---> Numero di elementi checkati: CONVALIDA");
        	for(int i=0;i<recValide.length; i++) {
        		System.out.println("---->> "+recValide[i]);
        		Integer idRecensione = Integer.valueOf(recValide[i]);
        		RecensioneDAO.valida(idRecensione);
        		System.out.println(idRecensione+" <----");
        	}
        	System.out.println("Convalida completata!");
        }else {
        	System.out.println("Non sono state selezionate recensioni da convalidare");
        }
        String[] recNegate = request.getParameterValues("rifiutaRecensione");
        if(recNegate != null) {
        	System.out.println(recNegate.length+" ---> Numero di elementi checkati: RIFIUTO");
        	for(int i=0;i<recNegate.length; i++) {
        		System.out.println("---->> "+recNegate[i]);
        		Integer idRecensione = Integer.valueOf(recNegate[i]);
        		RecensioneDAO.delete(idRecensione);
        		System.out.println(idRecensione+" <----");
        	}
        	System.out.println("Rifiuto completato!");
        }else {
        	System.out.println("Non sono state selezionate recensioni da rifiutare");
        }
        
        
        response.sendRedirect("backendGestioneRecensioni");
	}

}
