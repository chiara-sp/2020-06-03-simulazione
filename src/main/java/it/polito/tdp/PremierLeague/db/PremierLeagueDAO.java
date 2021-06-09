package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public void listAllPlayers(Map<Integer,Player> mapPlayers){
		String sql = "SELECT * FROM Players";
		//Map<Integer,Player> result = new HashMap<Integer,Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				mapPlayers.put(player.getPlayerID(), player);
			}
			conn.close();
			//return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			//return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Player> getPlayersGoal(double minimo, Map<Integer,Player> mapPlayers){
		String sql= "select `PlayerID`, avg(`Goals`) as peso "
				+ "from actions "
				+ "group by `PlayerID` "
				+ "having peso>?";
		List<Player> result= new LinkedList<>();
		
		

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, minimo);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player p= mapPlayers.get(res.getInt("PlayerID"));
				result.add(p);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Adiacenza> getAdiacenze(Map<Integer,Player> mapPlayers){
		
		String sql="select a1.`PlayerID` as p1, a2.`PlayerID` as p2,(SUM(a1.`TimePlayed`)- SUM(a2.`TimePlayed`)) as peso "
				+ "from actions a1, actions a2 "
				+ "where a1.`PlayerID`<a2.`PlayerID` and a1.`MatchID`=a2.`MatchID` and a1.`TeamID`!= a2.`TeamID` and a1.`Starts`=1 and a2.`Starts`=1 "
				+ "group by a1.`PlayerID`, a2.`PlayerID` ";
		List<Adiacenza> result= new LinkedList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player p1= mapPlayers.get(res.getInt("p1"));
				Player p2= mapPlayers.get(res.getInt("p2"));
				if(p1!= null && p2!=null) {
					Adiacenza a= new Adiacenza(p1,p2,res.getDouble("peso"));
					result.add(a);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
