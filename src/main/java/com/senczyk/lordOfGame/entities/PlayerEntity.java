package com.senczyk.lordOfGame.entities;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="players")
public class PlayerEntity {
	
	@Id
	private String name;
	
	@OneToMany( mappedBy = "playerId")
	private List<PawEntity> paws;
	
	public PlayerEntity() {  }
	
	public PlayerEntity(String name){
		this.name = name;
		paws = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PawEntity> getPaws() {
		return paws;
	}
	public void setPaws(List<PawEntity> paws) {
		this.paws = paws;
	}
}
