package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Date;
import java.util.Calendar;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static util.Utile.crypt;
import model.Utente;
import model.dao.UtenteDAO;
import util.Database;
import util.FreeMarker;
import util.SecurityLayer;
import util.Utile;

/**
 * @author Federico Tersigni
 */
@WebServlet("/Registrazione")
public class Registrazione extends HttpServlet {
	
    Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente
    
    /**
     * Processes requests di Registrazione
     *
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	System.out.println("process Request della registrazione");
    	HttpSession s = SecurityLayer.checkSession(request);
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
    	
    	
        FreeMarker.process("registrazione.html", data, response, getServletContext());
    }

    /**
     * doGet di Registrazione
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get della Registrazione!");
        HttpSession session = request.getSession();
        session.invalidate();
        id=0;
        try {
            processRequest(request, response);
        } catch (Exception e) {
           e.printStackTrace();
        }
	}

	/**
     * doPost di Registrazione
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs 
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// DoPost della registrazione dell'account::
		System.out.println("Inizio il doPost");
		String action = request.getParameter("value");
		if("sign".equals(action)) {
			//Prendo tutti i dati scritti dall'utente
			String nome = request.getParameter("nome");
            String cognome = request.getParameter("cognome");
            String dataNascita = request.getParameter("dataNascita");
            String email = request.getParameter("email");
            String citta = request.getParameter("citta");      
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            Calendar calendar = Calendar.getInstance();
            Date dataSignup = new Date(calendar.getTime().getTime());
            try {
            	int check = Utile.checkUser(email, password);
            	if(check==0) {
            		//conversione di dataNascita
            		SimpleDateFormat data_a = new SimpleDateFormat("yyyy-MM-dd");
            		java.util.Date date = data_a.parse(dataNascita);
            		java.sql.Date sqlDataNascita = new java.sql.Date(date.getTime());
            		//fine conversione
            		UtenteDAO.insert(nome, cognome, email, citta, sqlDataNascita, username, password, 1, dataSignup);
            	}else {
            		System.out.println("Esiste già un utente con queste credenziali nel DB");
            		//Resetta e ritorna in registrazione.html
            		response.sendRedirect("registrazione");
            	}
            	response.sendRedirect("home");
            }catch (NamingException e) {
                Logger.getLogger(Utente.class.getName()).log(Level.SEVERE, null, e);
            } catch (SQLException e) {
            	e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
  
		}// FINE if per registrare l'account
	}

}
