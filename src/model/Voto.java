package model;

/**
 * 
 * @author Federico Tersigni
 *
 */
public class Voto {
	private int valore;	
	private int id;
	private int idUtente;
	private int idGioco;
	
	public Voto(int id, int valore, int idUtente, int idGioco) {
		this.id=id;
		this.idUtente=idUtente;
		this.idGioco=idGioco;
		this.valore=valore;
	}
	public int getId() {
		return id;
	}
	public int getIdUtente() {
		return idUtente;
	}
	public int getIdGioco() {
		return idGioco;
	}
	public int getValore() {
		return valore;
	}
	public void setValore(int valore) {
		this.valore=valore;
	}
}
