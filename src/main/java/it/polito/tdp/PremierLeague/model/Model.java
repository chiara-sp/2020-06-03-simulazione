package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	private Map<Integer, Player> mapPlayers;
	private PremierLeagueDAO dao;
	private SimpleDirectedWeightedGraph<Player,DefaultWeightedEdge> grafo;
	
	public Model() {
		mapPlayers= new HashMap<>();
		dao= new PremierLeagueDAO();
		dao.listAllPlayers(mapPlayers);
	}
	
	public void creaGrafo(double min) {
		grafo= new SimpleDirectedWeightedGraph<Player,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiunta vertici 
		Graphs.addAllVertices(grafo, dao.getPlayersGoal(min, mapPlayers));
		
		//aggiunta archi
	}
	
	public int numVertici() {
		return grafo.vertexSet().size();
	}
	public int numArchi() {
		return grafo.edgeSet().size();
	}
}
