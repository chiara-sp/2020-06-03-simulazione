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
	List<Player> soluzione;
	List<Player> vertici;
	double gradoMax=0;
	
	
	
	public Model() {
		mapPlayers= new HashMap<>();
		dao= new PremierLeagueDAO();
		dao.listAllPlayers(mapPlayers);
		vertici= new LinkedList<>();
	}
	
	public void creaGrafo(double min) {
		grafo= new SimpleDirectedWeightedGraph<Player,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiunta vertici
		vertici=dao.getPlayersGoal(min, mapPlayers);
		Graphs.addAllVertices(grafo, vertici);
		
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
	public List<Player> ricorsione(int k){
		this.soluzione=new LinkedList<>();
		
		List<Player> possibiliAggiunte= new LinkedList<>(vertici);
		this.gradoMax=0;
		List<Player> parziale= new LinkedList<>();
		
		cerca(0, parziale, 0, k, possibiliAggiunte);
		
		return soluzione;
	}

	private void cerca(int livello, List<Player> parziale, int grado, int k, List<Player>possibiliAggiunte) {
		// TODO Auto-generated method stub
		if(parziale.size()>k)
			return;
		if(parziale.size()==k && grado>gradoMax) {
			this.gradoMax=grado;
			soluzione= new LinkedList<>(parziale);
			return;
		}
		for(Player p: vertici) {
			double peso=0;
			for(DefaultWeightedEdge edge: grafo.outgoingEdgesOf(p)) {
				peso+=grafo.getEdgeWeight(edge);
			}
			for(DefaultWeightedEdge edge: grafo.incomingEdgesOf(p)) {
				peso-=grafo.getEdgeWeight(edge);
			}
			if(possibiliAggiunte.contains(p) && !parziale.contains(p)) {
			parziale.add(p);
			List<Player> daTogliere= new LinkedList<>();
			for(Opponents pp: this.getBattuti(p)) {
				daTogliere.add(pp.getPlayer());
			}
			possibiliAggiunte.removeAll(daTogliere);
			grado+=peso;
			cerca(livello+1,parziale,grado,k , possibiliAggiunte);
			parziale.remove(p);
			possibiliAggiunte.addAll(daTogliere);
			grado-=peso;
			}
		}
	}
	public boolean grafoCreato() {
		if(grafo==null) {
			return false;
		}
		return true;
	}

	public List<Player> getSoluzione() {
		return soluzione;
	}

	public double getGradoMax() {
		return gradoMax;
	}
	
}
