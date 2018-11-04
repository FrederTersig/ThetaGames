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

import model.Gioco;
import model.Voto;
import model.dao.GiocoDAO;
import model.dao.RecensioneDAO;
import model.dao.VotoDAO;
import util.FreeMarker;
import util.SecurityLayer;

/**
 * @author Federico
 */

public class BackendGestioneGioco extends HttpServlet {
	
    Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente
    /**
     * Processes requests di BackendGestioneGioco
     *
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	System.out.println("process Request di BackendGestioneGioco");
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
    	
    	ArrayList<Gioco> lista = (ArrayList<Gioco>) GiocoDAO.lista_Giochi();
    	data.put("lista", lista);
    	
        FreeMarker.process("backendGestioneGioco.html", data, response, getServletContext());
    }

    /**
     * doGet di BackendGestioneGioco
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get del BackendGestioneGioco!");
        try {
            processRequest(request, response);
        } catch (Exception e) {
           e.printStackTrace();
        }
	}

	/**
     * doPost di BackendGestioneGioco
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs 
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("****************Post di RicercaGiochi.java!*******************");
		String action = request.getParameter("value");
		System.out.println(action);
		if("back".equals(action)) {
			
		}else if("rimuovi".equals(action)) {
			String valore = request.getParameter("cancellazioneGioco");
			System.out.println("quale valore??" + valore);
			Integer idGiocoSel = Integer.valueOf(valore);
			// Prima cancellazione voti
			// poi cancellazione recensioni
			// infine cancellazione gioco
			ArrayList<Integer> listaVoti = (ArrayList<Integer>) VotoDAO.listaVotiGioco(idGiocoSel);
			for(int i=0; i<listaVoti.size();i++) VotoDAO.delete(listaVoti.get(i));
			
			ArrayList<Integer> listaRecensioni = (ArrayList<Integer>) RecensioneDAO.idRecensioniGioco(idGiocoSel);
			for(int i=0; i<listaRecensioni.size();i++) RecensioneDAO.delete(listaRecensioni.get(i));
			
			
			GiocoDAO.delete(idGiocoSel);
			response.sendRedirect("backendGestioneGioco");
		}
	}

}
