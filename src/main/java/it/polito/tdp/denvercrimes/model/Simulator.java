package it.polito.tdp.denvercrimes.model;

import java.util.*;
import java.util.PriorityQueue;

import it.polito.tdp.denvercrimes.db.*;
import it.polito.tdp.denvercrimes.model.*;
import it.polito.tdp.denvercrimes.model.Evento.TipoEvento;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {

	//TIPI DI EVENTO
	//1. Evento criminoso:
		//a. La centrale seleziona l'agente più vicino
		//b. Se non ci sono disponibilità --> crimine mal gestito
		//c. Se c'è un agente libero --> setto l'agente a occupato
	//2. Arrivo dell'agente sul posto
		//a. Definisco la durata dell'intervento
		//b. Controllo se il crimine è mal gestito (= ritardo)
	//3. Crimine TERMINATO
		//a. "Libero" l'agente, che torna disponibile
	
	//Eventi
	private PriorityQueue<Evento> queue;
	
	//Parametri di simulazione
	private int N;
	private int anno;
	private int mese;
	private int giorno;
	
	//Stato del sistema
	private Graph<Integer, DefaultWeightedEdge> graph;
	private Map<Integer, Integer> agenti; //key:distretto, value:#agenti liberi
	
	//Misure in uscita
	private int malGestiti;
	
	
	public void init(int N, int anno, int mese, int giorno, Graph<Integer, DefaultWeightedEdge> graph) {
		this.N = N;
		this.anno = anno;
		this.mese = mese;
		this.giorno = giorno;
		this.graph = graph;
		
		this.malGestiti = 0;
		this.agenti = new HashMap<>();
		for (Integer i : graph.vertexSet()) {
			this.agenti.put(i, 0);
		}
		
		//scelgo la centrale e posiziono gli N agenti nel distretto
		EventsDAO dao = new EventsDAO();
		Integer minD = dao.getDistrettoMin(anno);
		this.agenti.put(minD, N);
		
		//creo e inizializzo la coda
		this.queue = new PriorityQueue<Evento>();
		for(Event e : dao.listAllEventsByDate(anno, mese, giorno)) {
			Evento evento = new Evento(TipoEvento.CRIMINE, e.getReported_date(), e);
			queue.add(evento);
		}
	}
	
	public int run() {
		Evento e;
		while ((e = queue.poll()) != null) {
			switch(e.getType()) {
				case CRIMINE:
					System.out.print("NUOVO CRIMINE! "+e.getCrimine().getIncident_id());
					//cerco l'agente libero più vicino
					Integer partenza = null;
					partenza = cercaAgente(e.getCrimine().getDistrict_id());
					if (partenza != null) {
						//c'è un agente libero in partenza --> setto l'agente come occupata
						this.agenti.put(partenza, this.agenti.get(partenza)-1);
						//cerco di capire quanto ci metterà l'agente libero ad arrivare sul posto
						Double distanza;
						if(partenza.equals(e.getCrimine().getDistrict_id())) {
							distanza = 0.0;
						}
						else {
							distanza = this.graph.getEdgeWeight(this.graph.getEdge(partenza, e.getCrimine().getDistrict_id()));
						}
						Long seconds = (long) ((distanza*1000)/(60/3.6));
						Evento nuovo = new Evento(TipoEvento.ARRIVA_AGENTE, e.getDate().plusSeconds(seconds), e.getCrimine());
						this.queue.add(nuovo);
					}
					else {
						//non ci sono agenti liberi --> crimine mal gestito
						System.out.println("CRIMINE "+e.getCrimine().getIncident_id()+" MAL GESTITO!");
						this.malGestiti++;
					}
					break;
				case ARRIVA_AGENTE:
					System.out.println("ARRIVA GENTE PER CRIMINE "+e.getCrimine().getIncident_id());
					Long duration = getDurata(e.getCrimine().getOffense_category_id());
					this.queue.add(new Evento(TipoEvento.GESTITO, e.getDate().plusSeconds(duration), e.getCrimine()));
					//controllo se il crimine è mal gestito
					if(e.getDate().isAfter(e.getCrimine().getReported_date().plusMinutes(15))) {
						System.out.println("CRIMINE "+e.getCrimine().getIncident_id()+" MAL GESTITO!");
						this.malGestiti++;
					}
					break;
				case GESTITO:
					System.out.println("CRIMINE "+e.getCrimine().getDistrict_id()+" GESTITO");
					this.agenti.put(e.getCrimine().getDistrict_id(), this.agenti.get(e.getCrimine().getDistrict_id())+1);
					break;
			}
		}
		return this.malGestiti;
	}
	
	
	
	private Long getDurata (String offense_category_id) {
		if(offense_category_id.equals("all_other_crimes")) {
			Random r = new Random();
			if(r.nextDouble() > 0.5) {
				return Long.valueOf(2*60*60);
			}
			else {
				return Long.valueOf(1*60*60);
			}
		}
		else {
			return Long.valueOf(2*60*60);
		}
	}
	
	
	private int cercaAgente(int district_id) {
		Double distanza = Double.MAX_VALUE;
		Integer distretto = null;
		
		for(Integer d : this.agenti.keySet()) {
			if (this.agenti.get(d) > 0) {
				if(district_id == d) {
					distanza = 0.0;
					distretto = d;
				}
				else if (this.graph.getEdgeWeight(this.graph.getEdge(district_id, d)) < distanza) {
					distanza = this.graph.getEdgeWeight(this.graph.getEdge(district_id, d));
					distretto = d;
				}
			}
		}
		return distretto;
	}
	
	
}
