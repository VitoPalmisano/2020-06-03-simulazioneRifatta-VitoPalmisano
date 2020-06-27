package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
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
	
	public void listPlayers(double mediaMin, Map<Integer, Player> idMap){
		String sql = "SELECT DISTINCT players.PlayerID AS p, players.Name AS nome FROM actions, players WHERE players.PlayerID=actions.PlayerID GROUP BY players.PlayerID HAVING AVG(actions.Goals)>?";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setDouble(1, mediaMin);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {

				idMap.put(res.getInt("p"), new Player(res.getInt("p"), res.getString("nome")));
				
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Adiacenza> listAdiacenze(double mediaMin, Map<Integer, Player> idMap){
		String sql = "SELECT a1.PlayerID AS p1, a2.PlayerID AS p2, SUM(a1.TimePlayed) AS s1, SUM(a2.TimePlayed) AS s2 FROM actions AS a1, actions AS a2 " + 
				"WHERE a1.PlayerID>a2.PlayerID AND a1.TeamID<>a2.TeamID " + 
				"AND a1.MatchID=a2.MatchID " + 
				"AND a1.starts=1 AND a2.starts=1 " + 
				"GROUP BY a1.PlayerID, a2.PlayerID " + 
				"HAVING (s1-s2)<>0";
		List<Adiacenza> adiacenze = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				adiacenze.add(new Adiacenza(idMap.get(res.getInt("p1")), idMap.get(res.getInt("p2")), res.getInt("s1"), res.getInt("s2")));
			}
			conn.close();
			return adiacenze;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
