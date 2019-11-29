package com.senczyk.lordOfGame.entities;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="games")
public class GameEntity {
	
	@Id
	private int id;
	
	@OneToOne
	@JoinColumn(name = "player1_id")
	private PlayerEntity player1;
	@OneToOne
	@JoinColumn(name = "player2_id")
	private PlayerEntity player2;
	
	public GameEntity() {}
	
	public GameEntity(PlayerEntity player1, PlayerEntity player2){
		this.player1 = player1;
		this.player2 = player2;
		setPawsAtStart();
	}
	
	public void playerMovement(PawEntity pawOld, PawEntity pawNew) {
		if( true ) {
			player1.getPaws().get(player1.getPaws().indexOf(pawOld)).setX(pawNew.getX());
			player1.getPaws().get(player1.getPaws().indexOf(pawOld)).setY(pawNew.getY());
			System.out.println("Player1: "+player1.getPaws().get(player1.getPaws().indexOf(pawOld)));
		}else{
			player2.getPaws().contains(pawOld);
			
		}
	}
	
	private final void setPawsAtStart() {
		List<PawEntity> pawsTemp = new ArrayList<>();
		
		pawsTemp.add(new PawEntity( 0, 1 ));
		pawsTemp.add(new PawEntity( 0, 3 ));
		pawsTemp.add(new PawEntity( 0, 5 ));
		pawsTemp.add(new PawEntity( 0, 7 ));
		pawsTemp.add(new PawEntity( 1, 0 ));
		pawsTemp.add(new PawEntity( 1, 2 ));
		pawsTemp.add(new PawEntity( 1, 4 ));
		pawsTemp.add(new PawEntity( 1, 6 ));
		pawsTemp.add(new PawEntity( 2, 1 ));
		pawsTemp.add(new PawEntity( 2, 3 ));
		pawsTemp.add(new PawEntity( 2, 5 ));
		pawsTemp.add(new PawEntity( 2, 7 ));
		
		player1.setPaws(pawsTemp);
		
		pawsTemp.clear();
		
		pawsTemp.add(new PawEntity( 5, 0 ));
		pawsTemp.add(new PawEntity( 5, 2 ));
		pawsTemp.add(new PawEntity( 5, 4 ));
		pawsTemp.add(new PawEntity( 5, 6 ));
		pawsTemp.add(new PawEntity( 6, 1 ));
		pawsTemp.add(new PawEntity( 6, 3 ));
		pawsTemp.add(new PawEntity( 6, 5 ));
		pawsTemp.add(new PawEntity( 6, 7 ));
		pawsTemp.add(new PawEntity( 7, 0 ));
		pawsTemp.add(new PawEntity( 7, 2 ));
		pawsTemp.add(new PawEntity( 7, 4 ));
		pawsTemp.add(new PawEntity( 7, 6 ));
		
		player2.setPaws(pawsTemp);
	}

	public PlayerEntity getPlayer1() {
		return player1;
	}

	public void setPlayer1(PlayerEntity player1) {
		this.player1 = player1;
	}

	public PlayerEntity getPlayer2() {
		return player2;
	}

	public void setPlayer2(PlayerEntity player2) {
		this.player2 = player2;
	}
}
