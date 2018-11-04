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
import model.Trofeo;
import model.Voto;
import model.dao.GiocoDAO;
import model.dao.RecensioneDAO;
import model.dao.TrofeoDAO;
import model.dao.VotoDAO;
import util.FreeMarker;
import util.SecurityLayer;

/**
 * @author Federico
 */

public class BackendGestioneTrofeo extends HttpServlet {
	
    Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente
    
    /**
     * Processes requests di BackendGestioneTrofeo
     *
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     */
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	System.out.println("process Request di BackendGestioneTrofeo");
    	HttpSession s = SecurityLayer.checkSession(request);
    	System.out.println("fine checkSession");
    	//Check della sessione
    	if(s != null){//condizione per vedere se la sessione esiste. 
            if(s.getAttribute("id") != null){
                id = (int) s.getAttribute("id");
            }else{
                id=0;
            }
            data.put("id", id);    
        }else{
            id = 0;
            data.put("id", id);
        }  
    	
    	ArrayList<Trofeo> lista= (ArrayList<Trofeo>) TrofeoDAO.lista_trofei_assoc();
    	for(int i=0; i<lista.size();i++) {
    		int x = lista.get(i).getIdGioco();
    		String xTitolo = GiocoDAO.getTitoloGioco(x);
    		lista.get(i).setGiocoAssociato(xTitolo);
    	}
    	data.put("listaTrofei", lista);
    	ArrayList<Gioco> listaGiochi = (ArrayList<Gioco>) GiocoDAO.lista_Giochi();
    	data.put("listaGiochi", listaGiochi);
        FreeMarker.process("backendGestioneTrofeo.html", data, response, getServletContext());
    }

    /**
     * doGet di BackendGestioneTrofeo
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get del BackendGestioneTrofeo!");
        try {
            processRequest(request, response);
        } catch (Exception e) {
           e.printStackTrace();
        }
	}

	/**
     * doPost di BackendGestioneTrofeo
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs 
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("****************Post di GestioneTrofeo!*******************");
		String action = request.getParameter("value");
		System.out.println(action);
		if("back".equals(action)) {
			
		}else if("rimuovi".equals(action)) {
			String valore = request.getParameter("cancellaTrofeo");
			System.out.println("quale valore??" + valore);
			Integer idTrofeoCanc = Integer.valueOf(valore);
			// Prima cancellazione voti
			// poi cancellazione recensioni
			// infine cancellazione gioco
			TrofeoDAO.delete(idTrofeoCanc);
			//cancellare anche trofeoutente -> ? 
			response.sendRedirect("backendGestioneTrofeo");
		}else if("inviaTrofeo".equals(action)) {
			//titolo, descrizione, valore  dalla select option "selezGioco"
			String valore = request.getParameter("selezGioco");
			
			Integer idGiocoSelez = Integer.valueOf(valore);
			System.out.println("id del gioco selezionato --> "+idGiocoSelez);
			String titolo = request.getParameter("titolo");
			String descrizione = request.getParameter("descrizione");
			TrofeoDAO.insert(titolo, descrizione, idGiocoSelez);
			response.sendRedirect("backendGestioneTrofeo");
			
		}
	}

}
