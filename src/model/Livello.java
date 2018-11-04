package model;

/**
 * 
 * @author Federico Tersigni
 *
 */

public class Livello {
	private int id;
	private int livelloTot;
	private int expMax;
	
	public Livello (int id, int livelloTot, int expMax) {
		this.id=id;
		this.livelloTot=livelloTot;
		this.expMax=expMax;
	}
	public Livello (int livelloTot, int expMax) {
		this.livelloTot = livelloTot;
		this.expMax=expMax;
	}
	public int getLivelloTot() {
		return this.livelloTot;
	}
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id=id;
	}
	public int getExp() {
		return this.expMax;
	}
	public void setLivelloTot(int numero) {
		this.livelloTot = numero;
	}
	public void setExp(int numero) {
		this.expMax=numero;
	}
}
