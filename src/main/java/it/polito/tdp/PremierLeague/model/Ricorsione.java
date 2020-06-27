package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Ricorsione {
	
	private int numGiocatori;
	private List<Player> dreamTeam;
	private int gradoTeam;
	private Graph<Player, DefaultWeightedEdge> grafo;
	
	public Ricorsione(int numGiocatori, Graph<Player, DefaultWeightedEdge> grafo) {
		super();
		this.numGiocatori = numGiocatori;
		this.grafo = grafo;
	}

	public void ricorsione() {
		
		dreamTeam = new ArrayList<Player>();
		gradoTeam = 0;
		
		List<Player> parziale = new ArrayList<Player>();
		List<Player> giocatori = new ArrayList<Player>(grafo.vertexSet());
		
		for(Player p : giocatori) {
			p.setGrado(grafo.outDegreeOf(p)-grafo.inDegreeOf(p));
		}
		
		cerca(0, parziale, giocatori);
		
	}
	
	private void cerca(int grado, List<Player> parziale, List<Player> giocatori) {
		
		if(parziale.size()==numGiocatori) {
			if(grado>gradoTeam) {
				dreamTeam = new ArrayList<Player>(parziale);
				gradoTeam = grado;
			}
			return;
		}
		
		for(Player p : giocatori) {
			if(!parziale.contains(p)) {
				parziale.add(p);
				List<Player> restanti = new ArrayList<Player>(giocatori);
				restanti.removeAll(Graphs.successorListOf(grafo, p));
				restanti.remove(p);
				cerca(grado+p.getGrado(), parziale, restanti);
				parziale.remove(p);
			}
		}
		
	}

	public int getGradoTeam() {
		return gradoTeam;
	}
	
	public List<Player> getTeam(){
		return dreamTeam;
	}
}
