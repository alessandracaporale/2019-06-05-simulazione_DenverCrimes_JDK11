package it.polito.tdp.denvercrimes.model;

public class Adiacenza implements Comparable<Adiacenza> {
	
	int distretto;
	double peso;
	
	public Adiacenza(int distretto, double peso) {
		super();
		this.distretto = distretto;
		this.peso = peso;
	}
	
	public int getDistretto() {
		return distretto;
	}
	public void setDistretto(int distretto) {
		this.distretto = distretto;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	
	@Override
	public int compareTo (Adiacenza a) {
		String p1 = String.valueOf(this.getPeso());
		String p2 = String.valueOf(a.getPeso());
		return p1.compareTo(p2);
	}

}
