package it.polito.tdp.PremierLeague.db;

import it.polito.tdp.PremierLeague.model.Player;

public class TestDao {

	public static void main(String[] args) {
		TestDao testDao = new TestDao();
		testDao.run();
	}
	
	public void run() {
		PremierLeagueDAO dao = new PremierLeagueDAO();
		
		System.out.println(dao.verificaArco(new Player(12297, null), new Player(17500, null)));
	}

}
