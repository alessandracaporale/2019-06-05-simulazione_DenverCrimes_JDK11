package it.polito.tdp.denvercrimes.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import java.math.*;

import it.polito.tdp.denvercrimes.db.EventsDAO;

public class Model {

	EventsDAO dao = new EventsDAO();
	Graph<Integer, DefaultWeightedEdge> graph;
	
	public Model() {
	}


	public List<Integer> getAllYears() {
		return dao.getAllYears();
	}
	
	public List<Integer> getDistrict() {
		return dao.getDistrict();
	}
	
	public void creaGrafo(int anno) {
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici
		Graphs.addAllVertices(graph, this.getDistrict());
		
		//archi
		for(Integer v1 : this.graph.vertexSet()) {
			for (Integer v2 : this.graph.vertexSet()) {
				if (v1 != v2) {
					if(this.graph.getEdge(v1, v2) == null) {
						Double latMediaV1 = dao.getLatMedia(v1, anno);
						Double latMediaV2 = dao.getLatMedia(v2, anno);
						Double lonMediaV1 = dao.getLonMedia(v1, anno);
						Double lonMediaV2 = dao.getLonMedia(v2, anno);
						//Double distanzaMedia = LatLngTool.distance(new LatLng(latMediaV1, lonMediaV1), new LatLng(latMediaV2, LonMediaV2), LengthUnit.KILOMETER);
						Double distanzaMedia = Math.sqrt(Math.pow(latMediaV1-latMediaV2, 2) + Math.pow(lonMediaV1-lonMediaV2, 2))*1000;
						
						Graphs.addEdgeWithVertices(graph, v1, v2, distanzaMedia);
						
					}
				}
			}
		}
		System.out.println("Grafo creato!\n#vertici = "+graph.vertexSet().size()+"\n#archi = "+graph.edgeSet().size());
	}
	
	public List<Adiacenza> adiacenze(int distretto) {
		List<Integer> adiacenzeId = Graphs.neighborListOf(graph, distretto); //lista di id distretti
		List<Adiacenza> adiacenze = new ArrayList<>(); //lista di adiacenze con id distretto + peso
		for (Integer i : adiacenzeId) {
			Adiacenza a = new Adiacenza(i, graph.getEdgeWeight(graph.getEdge(distretto, i)));
			adiacenze.add(a);
		}
		Collections.sort(adiacenze);
		return adiacenze;
	}
	
	
	public int simula(Integer anno, Integer mese, Integer giorno, Integer N) {
		Simulator sim = new Simulator();
		sim.init(N, anno, mese, giorno, graph);
		return sim.run();
	}
	
}
