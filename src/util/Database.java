package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
/**
 * 
 * @author Federico Tersigni
 *
 */
public class Database {
	protected static String DRIVER ="com.mysql.jdbc.Driver";
	protected static String url="jdbc:mysql://localhost/oop_thetagames";
	protected static String user ="root";
	protected static String psw ="";	
	private static Connection db; 
	
	
	
	/**
	 * Metodo per connettersi al database
	 * @throws Exception (SQLException && ClassNotFoundException)
	 * 
	 */
	public static void connect() throws Exception{    
    	try{                    
    		Class.forName(DRIVER);             
    		db = DriverManager.getConnection(url,user,psw);
    	}catch(SQLException e){
    		System.out.println("Eccezione SQL");
    		System.out.println(e.getMessage());
    	}catch (ClassNotFoundException e) {
    		System.out.println("Eccezione NO CLASSE");
    		e.printStackTrace();
    	}
	}
	
	/**
     * Metodo per fare una select immettendo tabella e condizione
     * @param table         tabella da cui si richiamano i dati
     * @param condition     condizione per filtrare i dati
     * @return              restituisce il ResultSet con i dati
     * @throws java.sql.SQLException
     */
	public static ResultSet select(String table, String condition) throws SQLException{
		String query = "SELECT * FROM " + table + " WHERE " + condition;
		return Database.executeQuery(query);
	}
	/**
     * Metodo per fare una select immettendo tabella e ordine
     * @param table         tabella da cui si richiamano i dati
     * @param order     	come ordinare i dati
     * @return              restituisce il ResultSet con i dati
     * @throws java.sql.SQLException
     */
    public static ResultSet selectRecord(String table, String order) throws SQLException{
        String query = "SELECT * FROM " + table + " ORDER BY " + order;
        return Database.executeQuery(query);
    }
    /**
     * Metodo per fare una select immettendo tabella e condizione E ricevendo i dati ordinati
     * @param table         tabella da cui si richiamano i dati
     * @param condition     condizione per filtrare i dati
     * @param order         ordine da dare ai dati
     * @return              restituisce il ResultSet con i dati
     * @throws java.sql.SQLException
     */
    public static ResultSet selectRecord(String table, String condition, String order) throws SQLException{
        String query = "SELECT * FROM " + table + " WHERE " + condition + " ORDER BY " + order;
        return Database.executeQuery(query);
    }
    /**
     * Metodo per fare una select immettendo tabella e condizione E ricevendo l'elemento massimo
     * @param columnMaxMin      colonna da cui selezionare il massimo
     * @param table         	tabella da cui si richiamano i dati
     * @param condition     	condizione per filtrare i dati
     * @return              	restituisce il ResultSet con i dati
     * @throws java.sql.SQLException
     */
    public static ResultSet selectMax(String columnMaxMin, String table, String condition) throws SQLException{
    	String query="SELECT MAX("+ columnMaxMin +") AS attr FROM "+ table +" WHERE " + condition;
    	return Database.executeQuery(query);
    }
 
    
    /**
    * Select record con join tra due tabelle
    * @param table_1           nome della prima tabella
    * @param table_2           nome della seconda tabella
    * @param join_condition    condizione del join tra la tabelle
    * @param order			   ordine da dare al risultato
    * @return                  dati prelevati
    * @throws java.sql.SQLException
    */
    public static ResultSet selectJoin(String column, String table_1, String table_2, String join_condition, String order)throws SQLException{
    	String query = "SELECT "+column+" FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition + " ORDER BY "+ order ;
    	System.out.println(query);
        return Database.executeQuery(query);
    }
 
    /**
     * Metodo per fare una select usando la join e restituendo un risultato ordinato
     * @param column			Colonna/e da selezionare
     * @param table_1          	Prima tabella da cui si richiamano i dati
     * @param table_2           Secondo tabella da cui si richiamano i dati
     * @param join_condition    Join condition tra le tabelle
     * @param where_condition   condizione per filtrare i dati
     * @param order             ordine da dare ai dati
     * @return                  restituisce il ResultSet con i dati ordinati
     * @throws java.sql.SQLException
     */
    public static ResultSet selectJoin(String column,String table_1, String table_2, String join_condition, String where_condition, String order) throws SQLException{
        String query = "SELECT "+column+" FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition + " WHERE " + where_condition + " ORDER BY " + order;
        System.out.println(query);
        return Database.executeQuery(query);
    }
    /**
     * Metodo per fare una select usando la join e restituendo un risultato ordinato
     * @param column			Colonna/e da selezionare
     * @param table_1          	Prima tabella da cui si richiamano i dati
     * @param table_2           Secondo tabella da cui si richiamano i dati
     * @param join_1    		Join condition tra le tabelle
     * @param table_3           terza tabella da cui si richiamano i dati
     * @param join_2    		Join condition tra le tabelle
     * @param where_condition   condizione per filtrare i dati
     * @param order             ordine da dare ai dati
     * @return                  restituisce il ResultSet con i dati ordinati
     * @throws java.sql.SQLException
     */
    public static ResultSet selectJoin(String column,String table_1,String table_2, String join_1, String table_3, String join_2, String where_condition, String order) throws SQLException{
    	String query = "SELECT "+column+" FROM "+table_1+" JOIN "+table_2+" ON "+join_1+" JOIN "+table_3+" ON "+join_2+" WHERE "+where_condition+" ORDER BY "+order;
    	System.out.println(query);
    	return Database.executeQuery(query);
    }

    /**
     * Metodo per inserire il record nel DB
     * @param table     tabella in cui fare la insert
     * @param data      un oggetto Map<String,Object> che avrà i dati da inserire
     * @return 			restituisce boolean per comunicare la riuscita della insert : True se riesce, False altrimenti
     * @throws java.sql.SQLException
     */
    public static boolean insertRecord(String table, Map<String, Object> data) throws SQLException{
        String query = "INSERT INTO " + table + " SET ";
        Object value;
        String attr;    
        for(Map.Entry<String,Object> e:data.entrySet()){
            attr = e.getKey();
            value = e.getValue();
            if(value instanceof Integer){
                query = query + attr + " = " + value + ", ";
            }else{
                value = value.toString().replace("\'", "\\'");
                query = query + attr + " = '" + value + "', ";
            }
        }
        query = query.substring(0, query.length() - 2);
        return Database.updateQuery(query);
    }
    /**
     * Metodo per fare l'update di un record
     * @param table         tabella dove andiamo ad aggiornare i dati
     * @param data          un oggetto Map<String,Object> che avrà i dati da inserire
     * @param condition     condizione per filtrare i dati
     * @return              restituisce boolean per comunicare la riuscita della insert : True se riesce, False altrimenti
     * @throws java.sql.SQLException
     */
    public static boolean updateRecord(String table, Map<String,Object> data, String condition) throws SQLException{
        String query = "UPDATE " + table + " SET ";
        Object value;
        String attr;      
        for(Map.Entry<String,Object> e:data.entrySet()){
            attr = e.getKey();
            value = e.getValue();
            if(value instanceof String){
                value = value.toString().replace("\'", "\\'");
                query = query + attr + " = '" + value + "', ";
            }else{
                query = query + attr + " = " + value + ", ";
            }            
        }
        query = query.substring(0, query.length()-2) + " WHERE " + condition;
        return Database.updateQuery(query);
    }
    
    /**
     * Metodo per fare l'update di un record senza l'uso della Map
     * @param table         tabella dove andiamo ad aggiornare i dati
     * @param data          stringa che avrà i dati da inserire
     * @param condition     condizione per filtrare i dati
     * @return              restituisce boolean per comunicare la riuscita della insert : True se riesce, False altrimenti
     * @throws java.sql.SQLException
     */
    public static boolean updateRecord(String table, String change, String condition) throws SQLException{
    	String query = "UPDATE " + table + " SET " + change + " WHERE " + condition;
    	return Database.updateQuery(query);   	
    }
    
    /**
     * Metodo per fare la delete di un record
     * @param table         tabella in cui eliminare i dati
     * @param condition     condizione per il filtro dei dati
     * @return              true se l'eliminazione è andata a buon fine, false altrimenti
     * @throws java.sql.SQLException
     */
    public static boolean deleteRecord(String table, String condition) throws SQLException{
        String query = "DELETE FROM " + table + " WHERE " + condition;
        return Database.updateQuery(query);
    }
    
    /**
     * Count record
     * @param table         tabella in cui contare i dati
     * @param condition     condizione per il filtro dei dati
     * @return              numero dei record se la query è stata eseguita on successo, -1 altrimenti
     * @throws java.sql.SQLException
     */
    public static int countRecord(String table, String condition) throws SQLException{
        String query = "SELECT COUNT(*) FROM " + table + " WHERE " + condition;
        ResultSet record = Database.executeQuery(query);
        record.next();
        return record.getInt(1);
    }   
    /**
     * Imposta a NULL un attributo di una tabella  
     * @param table         tabella in cui è presente l'attributo
     * @param attribute     attributo da impostare a NULL
     * @param condition     condizione
     * @return
     * @throws java.sql.SQLException
     */
    public static boolean resetAttribute(String table, String attribute, String condition) throws SQLException{
        String query = "UPDATE " + table + " SET " + attribute + " = NULL WHERE " + condition;
        return Database.updateQuery(query);
    }
    
    /**
     * executeQuery personalizzata
     * @param query query da eseguire
     */
    private static ResultSet executeQuery(String query) throws SQLException{      
        Statement s1 = Database.db.createStatement();
        ResultSet records = s1.executeQuery(query);
        return records;             
    }  
    /**
     * updateQuery personalizzata
     * @param query query da eseguire
     */
    private static boolean updateQuery(String query) throws SQLException{       
        Statement s1;        
        s1 = Database.db.createStatement();
        s1.executeUpdate(query); 
        s1.close();
        return true; 
    }
    
	/**
     * Metodo per chiudere al connessione al database
     * @throws java.sql.SQLException
     */
    public static void close() throws SQLException{
        Database.db.close();
    }
	
	
}
