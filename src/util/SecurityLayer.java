package util;

import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/*
 * @author Federico
 */
public class SecurityLayer {
	/**
	    * Verifica se esiste la sessione esiste
	    * @param HttpServletRequest		  		
	    * @return          				
	*/
    public static HttpSession checkSession(HttpServletRequest r) {
        boolean check = true;
        HttpSession s = r.getSession(false);
        //Controllo sull'esistenza della sessione
        if (s == null) {
            return null;
        }
        // controlla se la sessione ha l'id dell'utente
        // controlla se la sessione è scaduta
        // altrimenti procede con la sessione.
        if (s.getAttribute("userid") == null) {
            check = false;                        
        } else if ((s.getAttribute("ip") == null) || !((String) s.getAttribute("ip")).equals(r.getRemoteHost())) {
            check = false;
        } else {
            //inizio della sessione
            Calendar begin = (Calendar) s.getAttribute("inizio-sessione");
            //ultima azione
            Calendar last = (Calendar) s.getAttribute("ultima-azione");
            //data e ora correnti
            Calendar now = Calendar.getInstance();
            if (begin == null) {
                check = false;
            } else {
                //secondi trascorsi dall'inizio della sessione
                long secondsfrombegin = (now.getTimeInMillis() - begin.getTimeInMillis()) / 1000;
                //dopo tre ore la sessione scade
                if (secondsfrombegin > 3 * 60 * 60) {
                    check = false; 
                // Cancella la sessione dopo le tre ore
                } else if (last != null) {
                    //secondi trascorsi dall'ultima azione
                    long secondsfromlast = (now.getTimeInMillis() - last.getTimeInMillis()) / 1000;
                    //dopo trenta minuti dall'ultima operazione la sessione è invalidata                 
                    if (secondsfromlast > 30 * 60) {
                        check = false;
                    }
                }
            }
        }
        if (!check) {
            s.invalidate();
            return null;
        } else {
            s.setAttribute("ultima-azione", Calendar.getInstance());
            return s;
        }
    }

    /**
     * Cancella la sessione rimasta attiva e ne crea un'altra 
     * @param toTrim    stringa da elaborare
     * @return          stringa "pulita"
     */
    public static HttpSession createSession(HttpServletRequest request, String email, int userid) {
        disposeSession(request);  
        HttpSession s = request.getSession(true);
        s.setAttribute("email", email);
        s.setAttribute("ip", request.getRemoteHost());
        s.setAttribute("inizio-sessione", Calendar.getInstance());
        s.setAttribute("userid", userid);
        return s;
    }
    
    
   /**
    * invalida la sessione presente
    * @param request    
    * @return          
    */
    public static void disposeSession(HttpServletRequest request) {
    	HttpSession s = request.getSession(false);
        if (s != null) {
            s.invalidate();
        }
    }
    
}

