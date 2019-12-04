package com.senczyk.lordOfGame.entities;

import java.util.ArrayList;
import java.util.List;

public class BoardTemplate {

	private Player author;
	private Player reciver;
	
	public Player getAuthor() {
		return author;
	}

	public void setAuthor(Player author) {
		this.author = author;
	}

	public Player getReciver() {
		return reciver;
	}

	public void setReciver(Player reciver) {
		this.reciver = reciver;
	}
	public void printBoardgame() {
		System.out.println("Board:"+this.author.getName()+" "+this.reciver.getName());
	}
	public void printBoardgamePaws() {
		this.author.getPaws().stream().forEach(p->{
			System.out.println("Paw:"+p.getX()+" "+p.getY());
		});
		
	}

	private class Player{
		
		private String name;
		private List<PawEntity> paws;

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
}
