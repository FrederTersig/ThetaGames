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
 * @author Federico Tersigni
 */

public class Home extends HttpServlet {
	
       
	/**
     * Processes requests di Home
     *
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     */
	Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente
    public int ruolo=1; //ruolo dell'utente {1=normale,2=moderatore,3=admin}
    public Utente utente;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	System.out.println("process Request della Home");
    	HttpSession s = SecurityLayer.checkSession(request);
    	if(s != null){//condizione per vedere se la sessione esiste. 
    		System.out.println("ENTRO QUI ANCHE QUANDO NON SONO CONNESSO??");
            if(s.getAttribute("id") != null && s.getAttribute("utente") != null){
                id = (int) s.getAttribute("id");
                s.removeAttribute("ricerca");     
                utente = (Utente) s.getAttribute("utente");
            }else{
                id=0;
            }
            System.out.println("Process Request Home ->  ID =" + id );           
        }else{
            id = 0;
        }  
    	
    	data.put("id", id);    
    	data.put("utente", utente);
    	ArrayList<Gioco> game = (ArrayList<Gioco>) GiocoDAO.lista_ultimi5giochi_inseriti();
    	for(int i=0; i<game.size(); i++) {
    		int idGioco = game.get(i).getId();
    		double mediaVoti= VotoDAO.mediaVotiGioco(idGioco);
    		game.get(i).setMediavoto(mediaVoti);
    	}
    	
    	data.put("giochi", game);   	
    	String ricercaStr = (String) request.getSession(true).getAttribute("ricerca");
    	if(ricercaStr != "") {
    		request.getSession(true).removeAttribute("ricerca");
    	}
    	
    	System.out.println("-----------Prima del FreeMarker HOME------------");
        FreeMarker.process("home.html", data, response, getServletContext());      
    }
    
    /**
     * doGet di Home
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get di Home!");
        try {
        	
            processRequest(request, response);
        } catch (Exception e) {
           e.printStackTrace();
        }
	}

	/**
     * doPost di Home
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs 
     */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("POST di Home! Di seguito id --> " +id );
        String action = request.getParameter("value");
        System.out.println("************************************************************************** >> " +action+ " << **********************************************************");
        
        if(id==0) {//Se non si ha un id, quindi se non si è connessi
        	//System.out.println("id=0 -> provo doPost");
        	//System.out.println("Valore di action -->" + action);
	        if("signin".equals(action)) {
	        	System.out.println("Verso la registrazione");                             
	            response.sendRedirect("Registrazione");
	        }else if("gioco".equals(action)){
	        	response.sendRedirect("");
	        }else if("login".equals(action)){ // SE il metodo post è il login....
	            System.out.println("Verso il login ");
	            String email = request.getParameter("email");
	            String password = request.getParameter("password");    
	            //Check su email && password per vedere se l'utente esiste nel DB (1 sì,0 no)
	            System.out.println("email :: " + email + " || "+ "password " + password); 
	            if(!(null == email) && !(null == password)) {
	            	try {
	            		id=Utile.checkUser(email, password);
	            		System.out.println("ID DELL'UTENTE CHE HA FATTO IL LOGIN -->" + id);
	            	}catch(Exception e) {
	            		e.printStackTrace();
	            	}
	            }
	            System.out.println("Ecco l'id!" + id);
	            if(id==0) {
	            	System.out.println("Utente non presente nel DB!");
	            	data.put("id", 0);
	            	FreeMarker.process("home.html", data, response, getServletContext());
	            }else {
	            	System.out.println("Utente presente nel DB! procedo!!");
	            	try{ 
	                    HttpSession s = SecurityLayer.createSession(request, email, id);
	                    System.out.println("Sessione Creata, Connesso!");
	                    data.put("id",id);
	                    s.setAttribute("id", id);
	                    Utente profilo=null;
	                    try {
	                    	Database.connect();
	                        id = (int) s.getAttribute("id");
	                    	ResultSet pr=Database.selectRecord("utente", "utente.id="+id);
	                    	while(pr.next()) {
	                    		int role = pr.getInt("ruolo");
	                			Date dn = pr.getDate("dataNascita");
	                			String username = pr.getString("username");
	                			System.out.println("USERNAME ::::::"+username);
	                			String nome = pr.getString("nome");
	                			String cognome = pr.getString("cognome");
	                			String emailNN = pr.getString("email");
	                			Date ds = pr.getDate("dataSignup");
	                			String citta = pr.getString("citta");
	                			profilo=new Utente(id,role,dn,username,nome,cognome,emailNN);
	                    		
	                    	}
	                    	Database.close();
	                    }catch (SQLException e) {
	                    	System.out.println(e);
	                    }catch (Exception e) {
	                    	System.out.println(e);
	                    }
	                    int idLivelloUtente = TrofeoDAO.getIDLivelloAttuale(id);
	                    System.out.println("ID RISULTATO::::" + idLivelloUtente);
	                    Map<String, Integer> map = LivelloDAO.dettagli_livello(idLivelloUtente);
	                    System.out.println("Prendo livello e limite :: CHECK SE GIUSTO O MENO");
	                    for(Map.Entry<String,Integer> e:map.entrySet()){
	                        String attr = e.getKey();
	                        int value = e.getValue();
	                        System.out.println(attr);
	                        System.out.println(value);
	                        if(attr.equals("livelloTot")) {
	                        	profilo.setLivelloAttuale(value);
	                        }else if(attr.equals("expMax")) {
	                        	profilo.setEsperienzaCap(value);
	                        }
	                    }
	                    
	                    int expTot = EsperienzaDAO.conteggio_esperienza(id);
	                    profilo.setTotExp(expTot);
	                    data.put("utente", profilo);
	                    s.setAttribute("utente", profilo);
	                    FreeMarker.process("home.html", data, response, getServletContext());
	                }catch(Exception e2){
	                    System.out.println("Errore nel creare la sessione");
	                }
	            }
	        }
        }else {//Se si è connessi
	        if("logout".equals(action)){
	            System.out.println("** CLICCATO LOGOUT POSIZIONATO IN HOME **");
	            try{
	                SecurityLayer.disposeSession(request); 
	                id=0; 
	                data.put("id",id);
	                response.sendRedirect("");
	            }catch(Exception e3){
	                e3.printStackTrace();
	            }
	        }else if("search".equals(action)){
	           System.out.println("Stiamo ricercando una stringa precisa");
	           String searchStringa = request.getParameter("ricerca");
		       HttpSession s = SecurityLayer.checkSession(request);
		       s.setAttribute("ricerca", searchStringa);
		       data.put("ricerca", searchStringa);
		       response.sendRedirect("ricercaGiochi"); 
	           
	        }else if("listaCompleta".equals(action)){
	        	System.out.println("Lista completa dei giochi");
		           String searchStringa = "";
			       HttpSession s = SecurityLayer.checkSession(request);
			       s.setAttribute("ricerca", searchStringa);
			       data.put("ricerca", searchStringa);
			       response.sendRedirect("ricercaGiochi"); 
	        }else if("gioco".equals(action)){
	            System.out.println("Cliccato Gioco, procediamo!");          	           
		        int idGioco = Integer.parseInt(request.getParameter("dettagliGioco"));
		        HttpSession s = SecurityLayer.checkSession(request);
		    	if(s != null){//condizione per vedere se la sessione esiste. 		            
		            s.setAttribute("idGioco", idGioco);
				    response.sendRedirect("detGioco");        
		        }
	        }
        }
		/*Fine del doPost*/
	}

}
