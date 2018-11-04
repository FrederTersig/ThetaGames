package model;

import java.sql.Date;
/**
 * 
 * @author Federico Tersigni
 *
 */
public class Recensione {
	private int id;
	private int idUtente;
	private int idGioco;
	private Date data;
	private boolean valido;
	private String commento;
	private String nomeUtente;
	private String titoloGioco;
	
	public Recensione(int id, int idUtente, int idGioco, Date data, boolean valido, String commento, String nomeUtente, String titoloGioco) {
		this.id=id;
		this.idUtente=idUtente;
		this.idGioco=idGioco;
		this.data=data;
		this.valido=valido;
		this.commento=commento;
		this.nomeUtente=nomeUtente;
		this.titoloGioco=titoloGioco;
	}
	
	public Recensione(int id, int idUtente, int idGioco, Date data, boolean valido, String commento, String nomeUtente) {
		this.id=id;
		this.idUtente=idUtente;
		this.idGioco=idGioco;
		this.data=data;
		this.valido=valido;
		this.commento=commento;
		this.nomeUtente=nomeUtente;
	}
	
	public Recensione(int id, Date data, String commento, String nomeUtente, String titoloGioco) {
		this.id=id;
		this.data=data;
		this.commento=commento;
		this.nomeUtente=nomeUtente;
		this.titoloGioco=titoloGioco;
	}
	
	public Recensione(Date data, String commento, String nomeUtente) {
		this.data=data;
		this.commento=commento;
		this.nomeUtente=nomeUtente;
	}
	
	public String getTitoloGioco() {
		return titoloGioco;
	}
	
	public void setTitoloGioco(String titoloGioco) {
		this.titoloGioco = titoloGioco;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id=id;
	}
	public void setIdUtente(int idUtente) {
		this.idUtente=idUtente;
	}
	public int getIdUtente() {
		return idUtente;
	}
	public void setIdGioco(int idGioco) {
		this.idGioco=idGioco;
	}
	public int getIdGioco() {
		return idGioco;
	}
	public Date getData() {
		return data;
	}

	public boolean getValido() {
		return valido;
	}
	public String getNomeUtente() {
		return nomeUtente;
	}
	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente=nomeUtente;
	}
	public String getCommento() {
		return commento;
	}
	public void setCommento(String commento) {
		this.commento = commento;
	}
	public void setValido(Boolean valido) {
		this.valido=valido;
	}
}
