package servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Esperienza;
import model.Gioco;
import model.Livello;
import model.Trofeo;
import model.Utente;
import model.dao.EsperienzaDAO;
import model.dao.LivelloDAO;
import model.dao.TrofeoDAO;
import model.dao.UtenteDAO;
import util.Database;
import util.FreeMarker;
import util.SecurityLayer;
import util.Utile;

/**
 * @author Federico Tersigni
 */
@WebServlet("/Profilo")
public class Profilo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente
    public Utente utente;
    public int livelloUt = 1;
    public int diffExp = 0;
    public int limitExp =0;
    /**
     * Processes requests di Profilo
     *
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	System.out.println("process Request del Profilo");
    	HttpSession s = SecurityLayer.checkSession(request);
    	if(s != null){//condizione per vedere se la sessione esiste.    		
            if(s.getAttribute("id") != null && s.getAttribute("utente") != null){
            	id = (int) s.getAttribute("id");
                s.removeAttribute("ricerca");     
                utente = (Utente) s.getAttribute("utente");
                data.put("id", id);
                data.put("utente", utente);
            }else if(s.getAttribute("id") != null) {
            	//Query per riempire i dati del profilo.     
            	id = (int) s.getAttribute("id");
                try { // Try per dettagli del profilo
                 	Database.connect();                   
                 	ResultSet pr=Database.selectRecord("utente", "utente.id="+id);
                 	while(pr.next()) {
                 		int role = pr.getInt("ruolo");
             			Date dn = pr.getDate("dataNascita");
             			String username = pr.getString("username");
             			String nome = pr.getString("nome");
             			String cognome = pr.getString("cognome");
             			String emailNN = pr.getString("email");
             			Date ds = pr.getDate("dataSignup");
             			String citta = pr.getString("citta");
             			utente=new Utente(id,role,dn,username,nome,cognome,emailNN);                		
                 	}
                 	Database.close();
                }catch (SQLException e) {
                 	System.out.println(e);
                }catch (Exception e) {
                 	System.out.println(e);
                }      
                //Livello, punti esperienza, ecc
                int idLivelloUtente = TrofeoDAO.getIDLivelloAttuale(id);
                Map<String, Integer> map = LivelloDAO.dettagli_livello(idLivelloUtente);
                for(Map.Entry<String,Integer> e:map.entrySet()){
                    String attr = e.getKey();
                    int value = e.getValue();
                    System.out.println(attr);
                    System.out.println(value);
                    if(attr.equals("livelloTot")) {
                    	utente.setLivelloAttuale(value);
                    	livelloUt=value;
                    }else if(attr.equals("expMax")) {
                    	utente.setEsperienzaCap(value);
                    }
                }
                int expTot = EsperienzaDAO.conteggio_esperienza(id);
                utente.setTotExp(expTot);
                data.put("id", id);
                s.setAttribute("id", id);
                data.put("utente", utente);
                s.setAttribute("utente", utente);
            }else{
                id=0;
                data.put("id", id);    
                FreeMarker.process("home.html", data, response, getServletContext());
            }   
        }else{
            id = 0;
            data.put("id", id);    
            FreeMarker.process("home.html", data, response, getServletContext());
        }  

    	ArrayList<Esperienza> listaExp = (ArrayList<Esperienza>) EsperienzaDAO.listaEsperienza_utente(id);
    	//livelloUt
    	ArrayList<Livello> listaLvl = (ArrayList<Livello>) LivelloDAO.lista_Livelli(utente.getLivelloAttuale());//livelloUt);
    	//For per creare la stringa da far visualizzare
    	
    	ArrayList<String> listaVisualizz = new ArrayList<String>();
    	int contExp=0;
    	int contLvl=0;
    	String expStr="";
    	//1° Vediamo se listaExp e listaLvl hanno elementi. 
    	//2° For su listaExp e dentro di esso ad ogni passo c'è un check per  vedere se l'esperienza contata supera il limite exp
    	System.out.println(listaExp.size());
    	System.out.println(listaLvl.size());
    	for(int a=0; a<listaLvl.size(); a++) System.out.println(" ESPERIENZA CAP : "+ listaLvl.get(a).getExp() + " !! LIVELLO :" +listaLvl.get(a).getLivelloTot() );
    	
    	for(int i=0; i<listaExp.size(); i++) {
    		System.out.println("Inizio For per -->" + i + " ----- contLvl = "+ contLvl);
    		Livello xLvl = listaLvl.get(contLvl);
    		Esperienza xEsp = listaExp.get(i);
    		contExp += xEsp.getPunteggio();
    		expStr = xEsp.getData()+" : Hai guadagnato "+xEsp.getPunteggio()+" punti esperienza.";
    		listaVisualizz.add(expStr);
    		System.out.println("Prima della condizione");
    		if(contExp >= xLvl.getExp()) {
    			int lvl = xLvl.getLivelloTot() + 1;
    			System.out.println("La condizione> " + lvl);
    			expStr = "HAI GUADAGNATO UN LIVELLO! Sei al livello "+ lvl;
    			listaVisualizz.add(expStr);
    			contLvl++;
    		}
    		System.out.println(i + " <---- e ContLvl --> "+ contLvl);
    	}
    	System.out.println("prova qui");
    	limitExp=utente.getEsperienzaCap();
    	diffExp = limitExp - contExp ;
    	expStr = "Ti mancano "+diffExp+" punti esperienza per raggiungere il prossimo livello!";
    	listaVisualizz.add(expStr);
    	data.put("listaStringa", listaVisualizz);
    	//Fine parte "lista esperienza" inizio elenco trofei
    	ArrayList<Trofeo> listaTrofei = (ArrayList<Trofeo>) TrofeoDAO.lista_trofei_utente(id);
    	data.put("listaTrofei", listaTrofei);
    	//fine di questa parte: nota bene che i trofei vengono listati per la DATA in cui vengono acquisiti (quindi sempre PRIMA il benvenuto, tranne 
    	//questo caso);
    	
    	System.out.println("MOSTRO IL CONTENUTO DEGLI ARRAY");
    	for(int i=0; i<listaVisualizz.size();i++) System.out.println("--> "+listaVisualizz.get(i));
    	System.out.println("***********************FINE**************************");
        FreeMarker.process("profilo.html", data, response, getServletContext());      
    }

    /**
     * doGet di Profilo
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get del Profilo!");
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
     * doPost di Profilo
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs 
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("****************Post di Profilo.java!*******************");
		String action = request.getParameter("value");
		HttpSession s = SecurityLayer.checkSession(request);
		if("back".equals(action)) {
			
			if(s != null) {			
				if(s.getAttribute("utente") != null) data.put("utente", s.getAttribute("utente")); 
				else System.out.println("Premuto il tasto Home -> La sessione esiste -> non esiste un attributo 'utente'");
				System.out.println( s.getAttribute("utente") );
			}
			FreeMarker.process("home.html", data, response, getServletContext());
		}else if("changePsw".equals(action)) {
			System.out.println("Entrato in changePsw");
			String oldPsw = request.getParameter("oldpsw");
			String newPsw = request.getParameter("newpsw");
			
			boolean doChange=false;
			try {
				if(Utile.checkPsw(oldPsw,id)) {
					UtenteDAO.updatePsw(newPsw, id);
					doChange=true;
				}
			} catch (Exception e) {
				System.out.println("212 profilo" + e);
		    }
			
			if(!doChange) {
				response.sendRedirect("profilo");
			}else {
				HttpSession session = request.getSession();
		        session.invalidate();
				response.sendRedirect("");
			}			
		}else if("changeEmail".equals(action)) {
			String emailpsw = request.getParameter("emailpsw");
			String email = request.getParameter("email");
			
			boolean doChange=false;
			
			try {
				if(Utile.checkPsw(emailpsw,id)) {
					UtenteDAO.updateEmail(email, id);
					doChange=true;
				}
			} catch (Exception e) {
		           e.printStackTrace();
		    }
			
			if(!doChange) {
				response.sendRedirect("profilo");
			}else {
				HttpSession session = request.getSession();
		        session.invalidate();
				response.sendRedirect("");
			}
		}
		
	}

}
