package clueGame;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private BoardCell secretPassage;
	private Boolean hasSecretPassage = false;
	
	/*
	 * constructor that is only used to set name because we do not have other information yet
	 */

	public Room(String name) {
		
		super();
		this.name = name;
		
	}
	
	//Needed getters and setters
	public Boolean getHasSecretPassage() {
		
		return hasSecretPassage;
		
	}

	public BoardCell getSecretPassage() {
		
		return secretPassage;
		
	}

	public void setSecretPassage(BoardCell secretPassage) {
		
		this.secretPassage = secretPassage;
		this.hasSecretPassage = true;
		
	}
	
	public void setCenterCell(BoardCell centerCell) {
		
		this.centerCell = centerCell;
		
	}

	public void setLabelCell(BoardCell labelCell) {
		
		this.labelCell = labelCell;
		
	}

	public String getName() {
		
		return name;
		
	}

	public BoardCell getLabelCell() {
		
		return labelCell;
		
	}

	public BoardCell getCenterCell() {
		
		return centerCell;
		
	}

}
