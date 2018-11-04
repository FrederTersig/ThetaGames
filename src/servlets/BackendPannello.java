package servlets;

import java.io.IOException;
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
import util.Utile;
/* BackendPannello::
 * Quando ci si connette al pannello backend si dovrebbe poter registrare ipoteticamente il ruolo e il nickname
 * dell'utente. Pensando poi che bisognerebbe mettere nella sessione anche livello, limite esperienza del livello e
 * punti esperienza acquisiti in totale nell'esperienza di gioco ci si rende conto. Il problema principale sarebbe
 * quello dell'aggiornamento dei dati delle query. L'aggiornamento dei dati se avviene dovrebbe essere fatto appena si
 * apre una nuova pagina...
 * 
 * 
 * */
/**
 * @author Federico Tersigni
 */

public class BackendPannello extends HttpServlet {
    Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente
    public int role=1;
    /**
     * Processes requests di BackendPannello
     *
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	System.out.println("process Request di BackendPannello");
    	HttpSession s = SecurityLayer.checkSession(request);
    	//Check della sessione
    	if(s != null){//condizione per vedere se la sessione esiste. 
            if(s.getAttribute("id") != null){
                id = (int) s.getAttribute("id");
            }else{
                id=1;
            }
            
            data.put("id", id);    
        }else{
            id = 1;
            data.put("id", id);
        }  
    	System.out.println("BackendPannello ---> -Id- " + id + "-Ruolo- "+role);
    	role=Utile.checkRuolo(id);
    	if(role<=1) {
    		System.out.println("---------------- Prima di BackendPannello -> Home -------------");
    		FreeMarker.process("home.html", data, response, getServletContext());
    	}else {
    		
    		data.put("ruolo", role);
    		System.out.println("----------------- Prima di BackendPannello ------------------");
            FreeMarker.process("backendPannello.html", data, response, getServletContext());
    	}
    	
    	
    }

    /**
     * doGet di BackendPannello
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get del BackendPannello!");

        try {
            processRequest(request, response);
        } catch (Exception e) {
           System.out.println(e);
        }
	}

	/**
     * doPost di BackendPannello
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs 
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

}
