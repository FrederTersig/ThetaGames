package model;

import java.sql.Date;
/**
 * 
 * @author Federico Tersigni
 *
 */
public class Gioco {
	private int id;
	private Date data;
	private String descrizione;
	private String creatore;
	private String path;
	private String nome;
	private String fileGioco;
	private String fileThumb;
	private double mediaVoto;
	public Gioco(int id, Date data, String descrizione, String path, String nome,String creatore, double mediaVoto, String fileGioco, String fileThumb) {
		this.id=id;
		this.data=data;
		this.descrizione=descrizione;
		this.path=path;
		this.nome=nome;
		this.creatore=creatore;
		this.mediaVoto=mediaVoto;
		this.fileGioco=fileGioco;
		this.fileThumb=fileThumb;
	}
	public Gioco(int id, Date data, String descrizione, String path, String nome,String creatore,String fileGioco, String fileThumb) {
		this.id=id;
		this.data=data;
		this.descrizione=descrizione;
		this.path=path;
		this.nome=nome;
		this.creatore=creatore;
		this.fileGioco=fileGioco;
		this.fileThumb=fileThumb;
	}
	
	public String getfileGioco() {
		return fileGioco;
	}
	public String getfileThumb() {
		return fileThumb;
	}
	public void setfileGioco(String fileGioco) {
		this.fileGioco = fileGioco;
	}
	public void setfileThumb(String fileThumb) {
		this.fileThumb = fileThumb;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id=id;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCreatore() {
		return creatore;
	}
	public void setCreatore(String creatore) {
		this.creatore = creatore;
	}
	public double getMediaVoto() {
		return mediaVoto;
	}
	public void setMediavoto(double mediaVoto) {
		this.mediaVoto = mediaVoto;
	}
	public String getUrlGioco() {
		return this.path+this.fileGioco;
	}
	public String getUrlThumb() {
		return this.path+this.fileThumb;
	}

}
