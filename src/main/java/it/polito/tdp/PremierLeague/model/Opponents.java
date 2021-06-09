package it.polito.tdp.PremierLeague.model;

public class Opponents {

	private Player player;
	private double num;
	public Opponents(Player player, double num) {
		super();
		this.player = player;
		this.num = num;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public double getNum() {
		return num;
	}
	public void setNum(double num) {
		this.num = num;
	} 
	
	
}
