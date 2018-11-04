package servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.sql.Date;

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
import model.Utente;
import model.dao.UtenteDAO;

/**
 * @author Federico
 */
@WebServlet("/BackendGestioneUtenza")
public class BackendGestioneUtenza extends HttpServlet {
    Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; 
    public int ruolo=0;
    /**
     * Processes requests di BackendGestioneUtenza
     *
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	System.out.println("process Request di BackendGestioneUtenza");
    	HttpSession s = SecurityLayer.checkSession(request);
    	if(s != null){
            if(s.getAttribute("id") != null){
                id = (int) s.getAttribute("id");
            }else{         	
            	response.sendRedirect("home");
            }
            data.put("id", id);  
            if(s.getAttribute("ruolo") != null) ruolo = (int) s.getAttribute("ruolo");
            else ruolo=Utile.checkRuolo(id);
            data.put("ruolo", ruolo);
        }else{
        	id = 0;
            data.put("id", id);  
            FreeMarker.process("home.html", data, response, getServletContext());
        } 
    	/** fai query per vedere se ruolo a) non è presente nella sessione, b) è diverso da quello che c'era nella 
    	 * sessione. Fatto ciò dovrai registrarlo con il nome di "ruolo" semplicemente, e lo si userà così
    	 * tramite freemarker Serve per capire quali tasti si possono mettere vicino gli utenti*/
    	//Inizio Query per Lista Utenti
    	ArrayList<Utente> users=null;
    	int numUtenti=0;
    	try {
    		Database.connect();
    		ResultSet rs = Database.selectRecord("utente", "id <> "+id);
    		users = new ArrayList<Utente>();
    		while(rs.next()) {
    			//Bisogna prendere dalla select i seguenti campi di ogni utente: 1) Nickname, 2) Ruolo, 3)dataIscrizione, 4)email ---> Utente base > Mod / UnMod ; Admin / UnAdmin; || Moderatore > UnMod || Admin > UnAdmin (Tranne se stessi)
    			int id = rs.getInt("id");
    			int ruolo = rs.getInt("ruolo");
    			String username = rs.getString("username");
    			String email = rs.getString("email");
    			Date dataSignup = rs.getDate("dataSignup");
    			Utente lista = new Utente(id,ruolo,username,email,dataSignup);
    			users.add(lista);
    			numUtenti +=1;
    		}    		
    		Database.close();
    	}catch(NamingException e) {
    		System.out.println("NamingException > " + e);
        }catch (SQLException e) {
        	System.out.println("SQLException > " + e);
        }catch (Exception e) {
        	System.out.println("Exception > " + e);
        }
    	data.put("numUtenti", numUtenti);
    	data.put("utenti", users);
    	
    	
        FreeMarker.process("backendGestioneUtenza.html", data, response, getServletContext());
    }

    /**
     * doGet di BackendGestioneUtenza
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get del BackendGestioneUtenza!");
 
        try {
            processRequest(request, response);
        } catch (Exception e) {
           e.printStackTrace();
        }
	}

	/**
     * doPost di BackendGestioneUtenza
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs 
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("value");
		System.out.println("Quale valore al doPost? - " + action);
		
		if("rimuoviUtente".equals(action)) {
			String valore = request.getParameter("utente");
        	Integer idUtenteSelezionato = Integer.valueOf(valore);
        	System.out.println("L'id dell'utente selezionato è: "+idUtenteSelezionato);
			UtenteDAO.delete(idUtenteSelezionato);		
			response.sendRedirect("backendGestioneUtenza");
		}else if("removeMod".equals(action)) {
			System.out.println("rimuovi moderatore");
			String valore = request.getParameter("utente");
			Integer idUtenteSelezionato = Integer.valueOf(valore);
			UtenteDAO.retrocedi_mod(idUtenteSelezionato);
			response.sendRedirect("backendGestioneUtenza");
		}else if("addAdmin".equals(action)) {
			System.out.println("aggiungi admin");
			String valore = request.getParameter("utente");
			Integer idUtenteSelezionato = Integer.valueOf(valore);
			UtenteDAO.promuovi_mod(idUtenteSelezionato);
			response.sendRedirect("backendGestioneUtenza");
		}else if("removeAdmin".equals(action)) {
			System.out.println("rimuovi admin");
			String valore = request.getParameter("utente");
			Integer idUtenteSelezionato = Integer.valueOf(valore);
			UtenteDAO.retrocedi_admin(idUtenteSelezionato);
			response.sendRedirect("backendGestioneUtenza"); 
		}else if("addMod".equals(action)) {
			System.out.println("aggiungi moderatore");
			String valore = request.getParameter("utente");
			Integer idUtenteSelezionato = Integer.valueOf(valore);
			UtenteDAO.promuovi_utente(idUtenteSelezionato);
			response.sendRedirect("backendGestioneUtenza");
		}
		
	}

}
