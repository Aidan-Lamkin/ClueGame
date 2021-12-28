package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class ComputerPlayer extends Player {
	private boolean readyForAccusation = false;
	private Solution accusation;
	// Class constructor that calls parent constructor using computer values
	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}
	/*
	 * Creates suggestion based off current room and random person and weapon that the player has not seen yet
	 */
	public Solution createSuggestion(Board board) {
		Card room = new Card(board.getRoomMap().get(board.getCell(this.getRow(), this.getColumn()).getInitial()).getName(),CardType.ROOM);
		ArrayList<Card> people = new ArrayList<Card>();
		ArrayList<Card> weapons = new ArrayList<Card>();
		
		ArrayList<Card> tempDeck = new ArrayList<Card>();
		for(Card card: board.getDeck()) {
			tempDeck.add(card);
		}
		
		for(Card card: board.getDeck()) {
			for(Card card2: this.getSeen()) {
				if(card.equals(card2)) {
					tempDeck.remove(card);
				}
			}
		}
		
		for(Card card: tempDeck) {
			if(card.getCardType() == CardType.PERSON) {
				people.add(card);
			}
			if(card.getCardType() == CardType.WEAPON) {
				weapons.add(card);
			}
		}
		
		Card person = people.get(new Random().nextInt(people.size()));
		Card weapon = weapons.get(new Random().nextInt(weapons.size()));

		return new Solution(room,person,weapon);
	}
	
	
	/*
	 * computer will select target on board based off the following criteria
	 * If there are no rooms in target list pick at random
	 * If there are seen rooms in target list pick at random
	 * If there is an unseen room in target list go to that room and update seen
	 */
	public BoardCell selectTarget(int pathLength, Board board) {
		HashSet<BoardCell> targets = new HashSet<BoardCell>();
		board.calcTargets(board.getCell(this.getRow(), this.getColumn()), pathLength);
		targets = (HashSet)board.getTargets();
		Boolean seenRoom = true;
		for (BoardCell cell: targets) {
			if (cell.getIsRoom()) {
				Card card = new Card(board.getRoomMap().get(cell.getInitial()).getName(), CardType.ROOM);
				int count = 0;
				for (Card card2: this.getSeen()) {
					if(!card.equals(card2)) {
						count++;
					}
				}
				if(count == this.getSeen().size()) {
					seenRoom = false;
				}
			}		
		}
		BoardCell selected = new BoardCell("W",0,0);
		if(seenRoom) {
			int size = targets.size();
			int rand = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
			int i = 0;
			
			for(BoardCell cell : targets){
				if (i == rand)
					selected = cell;
				i++;
			}
		}
		else {
			for(BoardCell cell:targets) {
				if(cell.getIsRoom()) {
					if(!this.getSeen().contains(new Card(board.getRoomMap().get(cell.getInitial()).getName(), CardType.ROOM))) {
						updateSeen(new Card(board.getRoomMap().get(cell.getInitial()).getName(), CardType.ROOM));
						selected = cell;
					}
				}
			}
		}
		return selected;
	}
	public Solution getAccusation() {
		return accusation;
	}
	public void setAccusation(Solution accusation) {
		this.accusation = accusation;
	}
	
	public boolean isReadyForAccusation() {
		return readyForAccusation;
	}
	public void setReadyForAccusation(boolean readyForAccusation) {
		this.readyForAccusation = readyForAccusation;
	}
}
