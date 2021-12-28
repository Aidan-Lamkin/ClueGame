package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class BoardCell {

	private int row, column;
	private char initial;
	private char secretPassage;
	private DoorDirection doorDirection;
	private Set<BoardCell> adjList;
	private Boolean roomLabel = false;
	private Boolean roomCenter = false;
	private Boolean isDoorway = false;
	private Boolean isOccupied = false;
	private Boolean isSecretPassage = false;
	private Boolean isRoom = false;
	
	
	/*
	 * draws cell to board based on behavior and if a player is on the cell
	 */
	public void draw(Graphics g, int x, int y, int width, int height, Board board) {
		if(board.getTargets().contains(this)){
			g.setColor(Color.MAGENTA);
			g.drawRect(x,y,width,height);
			g.fillRect(x, y, width, height);
			return;
		}
		if(isRoom) {
			if(initial == 'X') {
				g.setColor(Color.BLACK);
				g.drawRect(x,y,width,height);
				g.fillRect(x, y, width, height);
			}
			else {
				g.setColor(Color.darkGray);
				g.drawRect(x,y,width,height);
				g.fillRect(x, y, width, height);
				if(roomLabel) {
					String roomName = board.getRoom(this).getName();
					g.setColor(Color.green);
					g.drawString(roomName,x,y - 10);
				}
			}
		}
		
		
		if(initial == 'W'){
			g.setColor(Color.BLACK);
			g.drawRect(x,y,width,height);
			g.fillRect(x, y, width, height);
			g.setColor(Color.WHITE);
			g.drawRect(x + 2,y + 2,width - 2,height - 2);
			g.fillRect(x + 2, y + 2, width - 2, height - 2);
			if(isDoorway) {
				if(doorDirection == DoorDirection.DOWN) {
					g.setColor(Color.cyan);
					g.drawRect(x, y + height, width, height / 4);
					g.fillRect(x, y + height, width, height / 4);

				}
				if(doorDirection == DoorDirection.UP) {
					g.setColor(Color.cyan);
					g.drawRect(x, y - height/4, width, height / 4);
					g.fillRect(x, y - height/4, width, height / 4);
				}
				if(doorDirection == DoorDirection.RIGHT) {
					g.setColor(Color.cyan);
					g.drawRect(x + width, y , width/4, height);
					g.fillRect(x +  width, y , width/4, height);
				}
				if(doorDirection == DoorDirection.LEFT) {
					g.setColor(Color.cyan);
					g.drawRect(x - width/4, y , width/4, height);
					g.fillRect(x - width/4, y , width/4, height);
				}
			}
			
		}
		if(isOccupied) {
			ArrayList<Player> pieces = new ArrayList<Player>();
			int offset = 0;
			for(Player player: board.getPlayers()) {
				if(player.getRow() == row && player.getColumn() == column) {
					pieces.add(player);
				}
			}
			int count = 0;
			for(Player player: pieces) {
				Color color = Color.WHITE;
				if(player.getColor().equals("red")) {
					color = Color.red;
				}
				else if(player.getColor().equals("orange")) {
					color = Color.orange;
				}
				else if(player.getColor().equals("green")) {
					color = Color.green;
				}
				else if(player.getColor().equals("blue")) {
					color = Color.blue;
				}
				else if(player.getColor().equals("yellow")) {
					color = Color.yellow;
				}
				else if(player.getColor().equals("cyan")) {
					color = Color.cyan;
				}
				else if(player.getColor().equals("pink")) {
					color = Color.pink;
				}
		
				g.setColor(Color.BLACK);
				g.drawOval(player.getColumn() * width+ 4 + (10 * count), player.getRow() * height + 4, width - 8, height - 8);
				g.fillOval(player.getColumn() * width + 4 + (10 * count),player.getRow() * height + 4,width - 8,height - 8);
				g.setColor(color);
				g.drawOval(player.getColumn() * width + 6 + (10 * count), player.getRow() * height + 6, width - 12, height - 12);
				g.fillOval(player.getColumn() * width + 6 + (10 * count),player.getRow() * height + 6,width - 12,height - 12);
				count++;
			}
		}
	}
	/*
	 * constructor that takes in string, row and column and sets values based on initial and second character
	 */
	public BoardCell(String name, int row, int col ) {
		
		super();
		adjList = new HashSet<BoardCell>();
		this.row = row;
		this.column = col;
		this.initial = name.charAt(0);
		behaviorSetter(name);
		
	}
	/*
	 * sets properties based on string
	 */
	private void behaviorSetter(String name) {
		if(initial != 'W') {
			isRoom = true;
		}
		if(name.length() > 1) {
			if(name.charAt(1) == '*'){
				this.roomCenter = true;
			}
			else if(name.charAt(1) == '#') {
				this.roomLabel = true;
			}
			else if(name.charAt(1) == '^') {
				this.doorDirection = DoorDirection.UP;
				this.isDoorway = true;
			}
			else if(name.charAt(1) == 'v') {
				this.doorDirection = DoorDirection.DOWN;
				this.isDoorway = true;
			}
			else if(name.charAt(1) == '<') {
				this.doorDirection = DoorDirection.LEFT;
				this.isDoorway = true;
			}
			else if(name.charAt(1) == '>') {
				this.doorDirection = DoorDirection.RIGHT;
				this.isDoorway = true;
			}
			else {
				this.secretPassage = name.charAt(1);
				isSecretPassage = true;
			}
		}
	}
	
	//Needed getters and setters
	public Boolean getIsRoom() {
		
		return isRoom;
		
	}

	public Boolean getIsSecretPassage() {
		
		return isSecretPassage;
		
	}
	
	public Boolean getIsOccupied() {
		
		return isOccupied;
		
	}

	public void addAdjacencies(BoardCell cell) {
		
		adjList.add(cell);
		
	}
	
	public Set<BoardCell> getAdjList() {
		
		return adjList;
		
	}

	public char getInitial() {
		
		return initial;
		
	}
	
	public boolean isDoorway() {
		
		return isDoorway;
		
	}


	public DoorDirection getDoorDirection() {
		
		return doorDirection;
		
	}


	public boolean isLabel() {
		
		return roomLabel;
		
	}


	public boolean isRoomCenter() {
		
		return roomCenter;
		
	}


	public char getSecretPassage() {
		
		return secretPassage;
		
	}

	public void setOccupied(boolean b) {
		
		this.isOccupied = b;
		
	}
	
	public int getRow() {
		
		return row;
		
	}
	public int getColumn() {
		
		return column;
		
	}

}
