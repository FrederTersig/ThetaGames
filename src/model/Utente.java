package model;

import java.sql.Date;
/**
 * 
 * @author Federico Tersigni
 *
 */
public class Utente {
	
	private int id;
	private String email;
	private String nome;
	private String citta;
	private String cognome;
	private Date dataNascita;
	private Date dataSignup;
	private String username;
	private int ruolo;
	private int totaleEsperienza;
	private int livelloAttuale;
	private int esperienzaCap;
	
	public Utente(int id, String email, String nome, String citta, String cognome, Date dataNascita, Date dataSignup, 
			String username, int ruolo, int totaleEsperienza, int livelloAttuale) {
		
		this.id=id;
		this.email=email;
		this.nome=nome;
		this.citta=citta;
		this.cognome=cognome;
		this.dataNascita=dataNascita;
		this.dataSignup=dataSignup;
		this.username=username;
		this.ruolo=ruolo;
		this.totaleEsperienza=totaleEsperienza;
		this.livelloAttuale=livelloAttuale;
	}
	public Utente(int id,int ruolo, Date dataNascita, String username, String nome, String cognome, String email) {
		this.id=id;
		this.ruolo=ruolo;
		this.dataNascita=dataNascita;
		this.username=username;
		this.nome=nome;
		this.cognome=cognome;
		this.email=email;
	}
	public Utente(int id,int ruolo, String username, String email, Date dataSignup) {
		this.id=id;
		this.ruolo=ruolo;
		this.username=username;
		this.email=email;
		this.dataSignup=dataSignup;
	}
	public Utente(int id,int ruolo, String username) {
		this.id=id;
		this.ruolo=ruolo;
		this.username=username;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id=id;
	}
	public int getTotaleEsperienza() {
		return totaleEsperienza;
	}
	public int getLivelloAttuale() {
		return livelloAttuale;
	}
	
	public int getEsperienzaCap() {
		return esperienzaCap;
	}
	public String getEmail() {
		return email;
	}
	public String getNome() {
		return nome;
	}
	public String getCognome() {
		return cognome;
	}
	public Date getDataNascita() {
		return dataNascita;
	}
	public Date getDataSignup() {
		return dataSignup;
	}
	public String getCitta() {
		return citta;
	}
	public String getUsername() {
		return username;
	}
	public int getRuolo() {
		return ruolo;
	}
	public void setEmail(String email) {
		this.email=email;
	}
	public void setNome(String nome) {
		this.nome=nome;
	}
	public void setCognome(String cognome) {
		this.cognome=cognome;
	}
	public void setCitta(String citta) {
		this.citta=citta;
	}
	public void setUsername(String username) {
		this.username=username;
	}
	public void setRuolo(int ruolo) {
		this.ruolo=ruolo;
	}
	public void setTotExp(int exp) {
		this.totaleEsperienza = exp;
	}
	public void setLivelloAttuale(int lvl) {
		this.livelloAttuale = lvl;
	}
	public void setEsperienzaCap(int cap) {
		this.esperienzaCap = cap;
	}
	
}
