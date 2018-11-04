package servlets;

/*File Upload Imports*/
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.util.Iterator;
import java.util.List;
/**/
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;


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
import model.Gioco;
import model.dao.GiocoDAO;
/**
 * Servlet implementation class BackendAggiungiGioco
 */
@WebServlet("/BackendAggiungiGioco")
public class BackendAggiungiGioco extends HttpServlet {
    Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; 
    public int ruolo=0;
    
    /**
     * Processes requests di BackendAggiungiGioco
     *
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	System.out.println("process Request di BackendAggiungiGioco");
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
        	response.sendRedirect("home");
        } 
    	/** fai query per vedere se ruolo a) non è presente nella sessione, b) è diverso da quello che c'era nella 
    	 * sessione. Fatto ciò dovrai registrarlo con il nome di "ruolo" semplicemente, e lo si userà così
    	 * tramite freemarker Serve per capire quali tasti si possono mettere vicino gli utenti*/
    	
    	System.out.println("Finito il processRequest di backendAggiungiGioco");
    	
        FreeMarker.process("backendAggiungiGioco.html", data, response, getServletContext());
    }

    /**
     * doGet di BackendAggiungiGioco
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get del BackendAggiungiGioco!");
 
        try {
            processRequest(request, response);
        } catch (Exception e) {
           e.printStackTrace();
        }
	}

	/**
     * doPost di BackendAggiungiGioco
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs 
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(" Sono nel POST di BackendAggiungiGioco!!!! ----> PROCEDE?");
		String action = request.getParameter("value");
		System.out.println("*****::: PREMUTO TASTO :::***** " + action);
		String nomeFile="";
		Map<String, Object> map = new HashMap<String,Object>();
		/* Prima Parte : Controllo che non ci sia un gioco */		
		try { // CHIUDO PRIMO TRY
			String titolo = "";
			String creatore = "";
			String descrizione = "";
			String url = "";
			String fileGioco = "";
			String fileThumb = "";
			
				/* INIZIO File Upload */
				System.out.println("PASSATO IL CHECK");
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		        if (isMultipart) {
		            FileItemFactory factory = new DiskFileItemFactory();
		            ServletFileUpload upload = new ServletFileUpload(factory);
		            System.out.println("MULTIPART");
		            try {
		            	System.out.println("DENTRO IL TRY");
		                List items = upload.parseRequest(request);
		                Iterator iterator = items.iterator();
		                while (iterator.hasNext()) {
		                    FileItem item = (FileItem) iterator.next();
		                    System.out.println("DENTRO IL WHILE");
		                    if (!item.isFormField()) {
		                    	System.out.println("AGGIUNTA FILE??");
		                        String fileName = item.getName();
		                        String root = getServletContext().getRealPath("/"); 
		                        
		                        if(url.equals("")) url = Utile.checkUrl();
		                        File path = new File(root + "/"+url);
	
		                        
		                        //urlGioco =  "gamez/" + code + "/" + fileName;
		                        //System.out.println("Nome File: "+ fileName);
		                        //System.out.println("Nome Root: "+ root);
		                        if (!path.exists()) {
		                            boolean status = path.mkdirs();
		                        }
		
		                        
		                        if(FilenameUtils.isExtension(fileName, "png")) {
		                        	fileThumb = fileName;
		                        }else {
		                        	fileGioco = fileName;	                        	
		                        }
		                        File uploadedFile = new File(path + "/" + fileName);
		                          
		                        
		                        //System.out.println(uploadedFile.getAbsolutePath());
		                        
		                        item.write(uploadedFile);
		                        
		                    }else{
		                    	String nome = item.getFieldName();
		                    	String value = item.getString();	                    	
		                    	if(nome.equals("titolo")) {
		                    		//map.put("titolo",value);
		                    		titolo=value;
		                    	}else if(nome.equals("descrizione")) {
		                    		//map.put("descrizione", value);
		                    		descrizione=value;
		                    	}else if(nome.equals("creatore")) {
		                    		//map.put("creatore", value);
		                    		creatore=value;
		                    	}
		                    	
		                    	System.out.println(nome);
		                    	System.out.println(value);
		                    }
		                }
		            } catch (FileUploadException e) {
		               System.out.println("FileUploadException-> "+ e);
		            } catch (Exception e) {
		            	System.out.println("Exception-> "+ e);
		            }
		        }/* FINE File Upload */
		        
		        
		        System.out.println("PROCEDO CON IL CHECK");
				int chkG =Utile.checkGioco(titolo, creatore, url);
				if(chkG == 0) { // APRO CHECK 1°
					GiocoDAO.insert(descrizione, url, titolo, creatore, fileGioco, fileThumb);					
				}else {
					System.out.println(" Sto nell'ELSE ");
					response.sendRedirect("backendAggiungiGioco");
				}
		}catch(NamingException e) {
    		System.out.println("naming! --> " +e);
        }catch (SQLException e) {
        	System.out.println( "SQL!! ->" +e);
        }catch (Exception e) {
        	System.out.println( "Eccezione generica -> " +e);
        }// CHIUDO PRIMO TRY
		System.out.println("Gioco Inserito?");
		FreeMarker.process("backendAggiungiGioco.html", data, response, getServletContext());
	}

}