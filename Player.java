package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public abstract class Player {
	private String name;
	private String color;
	private int row, column;
	private ArrayList<Card> hand = new ArrayList<Card>();
	private HashSet<Card> seen = new HashSet<Card>();
	private boolean movedBySuggestion = false;
	
	/*
	 * abstract player class with information for a name color
	 * starting location and hand
	 * 
	 * child classes are human and computer player
	 */
	public ArrayList<Card> getHand() {
		return hand;
	}

	// Player constructor with given variables
	public Player(String name, String color, int row, int column) {
		super();
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
	}
	/*
	 * Checks to see which cards would disprove suggestion
	 * If one card disproves suggestion then card is returned
	 * If multiple do then picks one at random
	 * If the player can not disprove the suggestion then returns null
	 */
	public Card disproveSuggestion(Solution suggestion) {
		ArrayList<Card> matches = new ArrayList<Card>();
		for(Card card: hand) {
			if(card.equals(suggestion.getPerson()) || card.equals(suggestion.getRoom()) || card.equals(suggestion.getWeapon())) {
				matches.add(card);
			}
		}
		if(matches.size() == 1) {
			return matches.get(0);
		}
		else if(matches.size() > 1) { 
			Random rand = new Random();
			return matches.get(rand.nextInt(matches.size()));
			
		}
		else {
			return null;
		}
	}
	
	public String getColor() {
		return color;
	}

	public void clearHand() {
		hand.clear();
	}
	public void updateHand(Card card) {
		hand.add(card);
	}

	public String getName() {
		return name;
	}
	
	public void setLocation(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public void updateSeen(Card seenCard) {
		seen.add(seenCard);
	}
	
	public HashSet<Card> getSeen() {
		return seen;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public boolean isMovedBySuggestion() {
		return movedBySuggestion;
	}

	public void setMovedBySuggestion(boolean movedBySuggestion) {
		this.movedBySuggestion = movedBySuggestion;
	}
	
	
}
