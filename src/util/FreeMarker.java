package util;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import java.io.File;

/**
 *	@author Federico
 * 
 */
public class FreeMarker {
   
	
	/**
     * 
     * @param data              dati da inserire nel template          
     * @param path_template     pathname del template da caricare
     * @param response          
     * @param servlet_context
     * @throws IOException
     */
    public static void process(String path_template, Map<String, Object> data, HttpServletResponse response, ServletContext servlet_context) throws IOException{
        
        response.setContentType("text/html;charset=UTF-8");        
        // Configurazione del freemarker
        Configuration cfg = new Configuration(); 
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.ITALY);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setServletContextForTemplateLoading(servlet_context, "/Template");
        Template template = cfg.getTemplate(path_template);
        PrintWriter out = response.getWriter();        
        try{
            template.process(data, out);           
        } catch (TemplateException e) {     
            System.out.println("TemplateException Freemarker.java: " + e);
        } finally{
            out.flush();
            out.close(); 
        }
    }
}
