package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;
import javafx.scene.shape.Arc;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	private Ricorsione r;
	
	public Model() {
		dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo(double mediaMin) {
		
		idMap = new HashMap<Integer, Player>();
		grafo = new SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		dao.listPlayers(mediaMin, idMap);
		
		Graphs.addAllVertices(grafo, idMap.values());
		
		List<Adiacenza> adiacenze = dao.listAdiacenze(mediaMin, idMap);
		
		for(Adiacenza a : adiacenze) {
			if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
				if((a.getT1()-a.getT2())>0) {
					Graphs.addEdgeWithVertices(grafo, a.getP1(), a.getP2(), (a.getT1()-a.getT2()));
				}else if((a.getT1()-a.getT2())<0) {
					Graphs.addEdgeWithVertices(grafo, a.getP2(), a.getP1(), (a.getT2()-a.getT1()));
				}
			}
		}
	}

	public int getNumVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return grafo.edgeSet().size();
	}
	
	public Player topPlayer(){
		
		Player topPlayer = null;
		
		List<Player> giocatori = new ArrayList<Player>(grafo.vertexSet());
		int num = 0;
		
		for(Player p : giocatori) {
			if(Graphs.successorListOf(grafo, p).size()>num) {
				topPlayer = p;
				num = Graphs.successorListOf(grafo, p).size();
			}
		}
		
		return topPlayer;
	}

	public List<Player> getAvversari(Player player) {
		
		List<Player> avversari = new ArrayList<Player>();
		List<DefaultWeightedEdge> archi = new ArrayList<DefaultWeightedEdge>(grafo.outgoingEdgesOf(player));
		
		Collections.sort(archi, new Comparator<DefaultWeightedEdge>() {
			public int compare(DefaultWeightedEdge e1, DefaultWeightedEdge e2) {
				return (int)grafo.getEdgeWeight(e2)-(int)grafo.getEdgeWeight(e1);
			}
		});
		
		for(DefaultWeightedEdge e : archi) {
			avversari.add(grafo.getEdgeTarget(e));
		}
		
		return avversari;
	}
	
	public void ricorsione(int numGiocatori) {
		r = new Ricorsione(numGiocatori, grafo);
		r.ricorsione();
	}
	
	public Integer getGrado() {
		return r.getGradoTeam();
	}
	
	public List<Player> getTeam(){
		return r.getTeam();
	}
	
}
