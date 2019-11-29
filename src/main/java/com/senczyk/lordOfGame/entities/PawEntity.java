package com.senczyk.lordOfGame.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="paws_list")
public class PawEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	private int x;
	private int y;
	private boolean queen;
	
	@ManyToOne
	@JoinColumn(name = "player_id")
	private PlayerEntity playerId;
	
	public PawEntity() {}
	public PawEntity(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean isQueen() {
		return queen;
	}
	public void setQueen(boolean queen) {
		this.queen = queen;
	}
	
	
}
