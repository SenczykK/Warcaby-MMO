package com.senczyk.lordOfGame.entities;


public class MessageTemplate {

	private String author;
	private String reciver;
	private String option;
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getReciver() {
		return reciver;
	}
	public void setReciver(String reciver) {
		this.reciver = reciver;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	
	public void printMessage() {
		System.out.println("Message: "+this.author+" "+this.reciver+" "+this.option);
	}
	
	
}
