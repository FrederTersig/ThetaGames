package model;

import java.sql.Date;
/**
 * 
 * @author Federico Tersigni
 *
 */
public class Esperienza {
	private int id;
	private int punteggio;
	private Date data;
	
	public Esperienza(int id, int punteggio, Date data) {
		this.id=id;
		this.punteggio=punteggio;
		this.data=data;
	}
	public int getId() {
		return id;
	}
	public int getPunteggio() {
		return punteggio;
	}
	public Date getData() {
		return data;
	}
}
