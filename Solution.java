package clueGame;

public class Solution {
	private Card room;
	private Card person;
	private Card weapon;
	
	// Constructor using given variables (from UML diagram)
	public Solution(Card room, Card person, Card weapon) {
		super();
		this.room = room;
		this.person = person;
		this.weapon = weapon;
	}

	// Getters for the variables - room, person, and weapon
	public Card getRoom() {
		return room;
	}

	public Card getPerson() {
		return person;
	}

	public Card getWeapon() {
		return weapon;
	}
	
}
