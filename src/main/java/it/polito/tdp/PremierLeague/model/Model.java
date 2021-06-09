package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
		List<Adiacenza> adiacenze= dao.getAdiacenze(mapPlayers);
		for(Adiacenza a: adiacenze) {
			if( grafo .vertexSet().contains(a.getP2())&& grafo.vertexSet().contains(a.getP1())){
			if(a.getPeso()>0 ) {
				Graphs.addEdge(grafo,a.getP1(), a.getP2(), a.getPeso());
			}
			else if(a.getPeso()<0) {
				Graphs.addEdge(grafo, a.getP2(), a.getP1(), -a.getPeso());
			}
		}
		}
	}
	
	public int numVertici() {
		return grafo.vertexSet().size();
	}
	public int numArchi() {
		return grafo.edgeSet().size();
	}
	public Player topPlayer() {
		if(grafo==null)
			return null;
		
		Player top=null;
		double maxDegree=0;
		for (Player p: grafo.vertexSet()) {
			double deg= grafo.outDegreeOf(p);
			if(deg>maxDegree) {
				maxDegree=deg;
				top=p;
			}
				
		}
		return top;
	}
	public List<Opponents> getBattuti(Player top){
		if(grafo==null || top==null)
			return null;
		
		List<Opponents> result= new LinkedList<>();
		
		for(DefaultWeightedEdge e: grafo.outgoingEdgesOf(top) ) {
			result.add(new Opponents(grafo.getEdgeTarget(e), grafo.getEdgeWeight(e)));
		}
		return result;
	}
}
