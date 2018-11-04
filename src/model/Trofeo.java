package model;

import java.sql.Date;
/**
 * 
 * @author Federico Tersigni
 *
 */
public class Trofeo {
	private int id;
	private String nomeTrofeo;
	private String descrizione;
	private int idLivello;
	private int idGioco;
	private Date data;
	private String nomeGioco;
	
	
	public Trofeo(int id,String nomeTrofeo, String descrizione, Date data) {
		this.id=id;
		this.nomeTrofeo=nomeTrofeo;
		this.descrizione=descrizione;
		this.data = data;
	}
	public Trofeo(int id,String nomeTrofeo, String descrizione, int idGioco) {
		this.id=id;
		this.nomeTrofeo=nomeTrofeo;
		this.descrizione=descrizione;
		this.idGioco=idGioco;
	}
	public Trofeo(String titolo, String descrizione, int idGioco) {
		this.nomeTrofeo = titolo;
		this.descrizione = descrizione;
		this.idGioco = idGioco;
	}
	
	public Trofeo(int id,String nomeTrofeo, String descrizione) {
		this.id=id;
		this.nomeTrofeo=nomeTrofeo;
		this.descrizione=descrizione;
	}

	public int getId() {
		return id;
	}
	public String getNomeTrofeo() {
		return nomeTrofeo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public int getIdGioco() {
		return idGioco;
	}
	public String getGiocoAssociato() {
		return nomeGioco;
	}
	public void setId(int id) {
		this.id=id;
	}
	public void setNomeTrofeo(String nome) {
		this.nomeTrofeo=nome;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione=descrizione;
	}
	public void setIdGioco(int idGioco) {
		this.idGioco=idGioco;
	}
	public void setGiocoAssociato(String nomeGioco) {
		this.nomeGioco=nomeGioco;
	}
	public Date getData() {
		return this.data;
	}
	public void setData(Date data) {
		this.data=data;
	}
	
}
