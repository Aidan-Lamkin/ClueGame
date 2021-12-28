package clueGame;

import java.awt.Color;

public class Card {
	private String cardName;
	private CardType cardType;
	private Color color;
	/*
	 * made equals() method make sure if both room and type match
	 */
	public boolean equals(Card target) {
		if(this.cardName.equals(target.cardName)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	// Constructor and getters
	public String getCardName() {
		return cardName;
	}

	public CardType getCardType() {
		return cardType;
	}

	// Constructor using given variables (from UML diagram)
	public Card(String cardName, CardType cardType) {
		super();
		this.cardName = cardName;
		this.cardType = cardType;
	}
	/*
	 * sets color based on the color of the player who holds it
	 */
	public void updateColor(String color) {
		if(color.equals("red")) {
			this.color = Color.red;
		}
		else if(color.equals("orange")) {
			this.color = Color.orange;
		}
		else if(color.equals("green")) {
			this.color = Color.green;
		}
		else if(color.equals("blue")) {
			this.color = Color.blue;
		}
		else if(color.equals("yellow")) {
			this.color = Color.yellow;
		}
		else if(color.equals("cyan")) {
			this.color = Color.cyan;
		}
		else if(color.equals("pink")) {
			this.color = Color.pink;
		}
		
	}

	public Color getColor() {
		return color;
	}
	
}
