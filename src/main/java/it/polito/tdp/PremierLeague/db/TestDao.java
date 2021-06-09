package it.polito.tdp.PremierLeague.db;

import java.util.HashMap;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Player;

public class TestDao {

	public static void main(String[] args) {
		TestDao testDao = new TestDao();
		testDao.run();
	}
	
	public void run() {
		PremierLeagueDAO dao = new PremierLeagueDAO();
		//System.out.println("Players:");
		//System.out.println(dao.listAllPlayers());
		//System.out.println("Actions:");
		//System.out.println(dao.listAllActions());
		
		Map<Integer,Player> mappa= new HashMap<>();
		dao.listAllPlayers(mappa);
		
		System.out.println(mappa.toString());
	}

}
