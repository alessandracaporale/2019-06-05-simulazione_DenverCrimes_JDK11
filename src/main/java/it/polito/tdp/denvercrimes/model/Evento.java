package it.polito.tdp.denvercrimes.model;

import java.time.*;

public class Evento implements Comparable<Evento> {

	private TipoEvento type;
	private LocalDateTime date;
	private Event crimine;
	
	public enum TipoEvento {
		CRIMINE,
		ARRIVA_AGENTE,
		GESTITO
	}
	
	public Evento(TipoEvento type, LocalDateTime date, Event crimine) {
		super();
		this.type = type;
		this.date = date;
		this.crimine = crimine;
	}



	@Override
	public int compareTo(Evento other) {
		return this.date.compareTo(other.date) ;
	}
	
	public TipoEvento getType() {
		return type;
	}
	public void setType(TipoEvento type) {
		this.type = type;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime time) {
		this.date = time;
	}
	public Event getCrimine() {
		return crimine;
	}
	public void setCrimine(Event crimine) {
		this.crimine = crimine;
	}
}
