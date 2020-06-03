package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	private PremierLeagueDAO dao;
	private Map<Player, Double> idMap;
	private Graph<Player, DefaultWeightedEdge> grafo;
	
	public Model() {
		dao = new PremierLeagueDAO();
	}
	
	public String creaGrafo(double minimo) {
		idMap = new HashMap<Player, Double>();
		dao.getGiocatori(idMap, minimo);
		
		grafo = new SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, idMap.keySet());
		
		for(Player p1 : grafo.vertexSet()) {
			for(Player p2 : grafo.vertexSet()) {
				if(dao.verificaArco(p1, p2)) {
					grafo.addEdge(p1, p2);
				}
			}
		}
		
		return grafo.vertexSet().size()+" "+grafo.edgeSet().size();
	}
	
	public Player getTopPlayer(double minimo) {
		this.creaGrafo(minimo);
		Player temp = null;
		int max = 0;
		for(Player p : grafo.vertexSet()) {
			if(grafo.outDegreeOf(p)>max) {
				max = grafo.outDegreeOf(p);
				temp = p;
			}
		}
		return temp;
	}
	
	public List<Player> getPlayersBattuti(Player p){
		return Graphs.successorListOf(grafo, p);
	}
}
