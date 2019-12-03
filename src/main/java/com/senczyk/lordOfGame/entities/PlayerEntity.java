package com.senczyk.lordOfGame.entities;

import java.time.LocalDate;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="players")
public class PlayerEntity {
	
	@Id
	private String name;
	
	@Transient
	private List<PawEntity> paws;
	
	@Column(name = "white_black")
	private String whiteBlack;
	
	@Column(name = "last_login")
	private LocalDate lastLogin;
	
	public PlayerEntity() {  }
	
	public PlayerEntity(String name){
		this.name = name;
		paws = new ArrayList<>();
	}

	public LocalDate getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDate localDate) {
		this.lastLogin = localDate;
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

	public String getWhiteBlack() {
		return whiteBlack;
	}

	public void setWhiteBlack(String whiteBlack) {
		this.whiteBlack = whiteBlack;
	}
	
}
