package clueGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Board extends JPanel implements MouseListener{
	
	private BoardCell [][] grid;
	private int numRows;
	private int numColumns;
	
	private String layoutConfigFile;
	private String setupConfigFile;
	
	private Map<Character, Room> roomMap = new HashMap<Character, Room>();
	
	private static Board theInstance= new Board();
	
	private HashSet<BoardCell> targets;
	private HashSet<BoardCell> visited;
	
	private ArrayList<Player> players = new ArrayList<Player>();

	private ArrayList<Card> deck = new ArrayList<Card>();
	
	private ArrayList<String> weapons = new ArrayList<String>();
	
	private Solution theAnswer;
	
	private Player disprover;
	
	private int cellWidth;
	private int cellHeight;
	
	private Player currentPlayer;

	private boolean humanPlayerFinished = false;
	
	private boolean humanReadyForAccusation = false;
	 // constructor is private to ensure only one can be created
	private Board() {
		
		super() ;
		addMouseListener(this);
		
	}
	/*
	 * error message when player did not finish turn
	 */
	public void finishTurn() {
		JOptionPane.showConfirmDialog(this, "Please Finish Your Turn.");
	}
	// this method returns the only Board
	public static Board getInstance() {
		
		return theInstance;
		
	}
	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize() throws BadConfigFormatException{
		//declare memory here because of singleton pattern
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		
		loadSetupConfig();
		
		loadLayoutConfig();
		
		createAdjacencies();
		
		currentPlayer = players.get(0);
		
		playMusic();
	}
	/*
	 * loops over background music for the duration of the game
	 */
	public void playMusic() {
		File f = new File("./" + "data/Music.wav");
	    AudioInputStream audioIn = null;
		try {
			audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}  
	    Clip clip;
		try {
			clip = AudioSystem.getClip();
			try {
				clip.open(audioIn);
			} catch (IOException e) {
				e.printStackTrace();
			}
		    clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	/*
	 * plays sound whenever button is pressed or mouse is clicked on board
	 */
	public void playButtonClick() {
		File f = new File("./" + "data/ButtonClick.wav");
	    AudioInputStream audioIn = null;
		try {
			audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}  
	    Clip clip;
		try {
			clip = AudioSystem.getClip();
			try {
				clip.open(audioIn);
			} catch (IOException e) {
				e.printStackTrace();
			}
		    clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		    
	}
	/*
	 * creates screen where human can create an accusation then displays the end screen
	 */
	public Solution humanAccusation(Player player) {
		JDialog accusation = new JDialog();
		accusation.setSize(300,300);
		accusation.setLayout(new GridLayout(4,0));
		
		JPanel first = new JPanel();
		JLabel roomLabel = new JLabel("Room:");
		first.add(roomLabel);
		JComboBox roomCombo = new JComboBox();
		for(char key: roomMap.keySet()) {
			roomCombo.addItem(roomMap.get(key).getName());
		}
		first.add(roomCombo);
		accusation.add(first);
		
		JPanel second = new JPanel();
		JLabel personLabel = new JLabel("Person:");
		second.add(personLabel);
		JComboBox personCombo = new JComboBox();
		for(Player player2: players) {
			personCombo.addItem(player2.getName());
		}
		second.add(personCombo);
		accusation.add(second);
		
		JPanel third = new JPanel();
		JLabel weaponLabel = new JLabel("Weapon:");
		third.add(weaponLabel);
		JComboBox weaponCombo = new JComboBox();
		for(String weapon: weapons) {
			weaponCombo.addItem(weapon);
		}
		third.add(weaponCombo);
		accusation.add(third);
		
		JPanel fourth = new JPanel();
		JButton submit = new JButton("Submit");
		fourth.add(submit);
		submit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				playButtonClick();
				String person = (String) personCombo.getSelectedItem();
				String weapon = (String) weaponCombo.getSelectedItem();
				
				Card roomCard = new Card(roomMap.get(getCell(currentPlayer.getRow(),currentPlayer.getColumn()).getInitial()).getName(), CardType.ROOM);
				Card personCard = new Card(person,CardType.PERSON);
				Card weaponCard = new Card(weapon, CardType.ROOM);
				Solution accused = new Solution(roomCard,personCard,weaponCard);
				Boolean won = checkAccusation(accused);
				accusation.dispose();
				endScreen(won, player, accused);
			}
		});
		JButton cancel = new JButton("Cancel");
		fourth.add(cancel);
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				playButtonClick();
				accusation.dispose();
			}
		});
		accusation.add(fourth);
		
		accusation.setVisible(true);
		return theAnswer;
	}
	/*
	 * error that displays if human tries to make accusation when it is not there turn
	 */
	public void accusationErrorMessage() {
		JOptionPane.showMessageDialog(theInstance, "You can not make an accusation when it is not your turn.");
	}
	/*
	 * displays if the accusation made is incorrect or correct. 
	 * Once the screen is closed the program ends
	 */
	public void endScreen(boolean won, Player player, Solution Accusation) {
		if(won == false && player instanceof ComputerPlayer) {
			return;
		}
		JFrame frame = new JFrame();
		frame.setSize(700,100);
		JTextField label = new JTextField();
		label.setEditable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		if(won == true) {
			label.setText(player.getName() + "'s accusation of " + Accusation.getRoom().getCardName() + ", " + Accusation.getPerson().getCardName() + ", and " + Accusation.getWeapon().getCardName() + " is correct. " + player.getName() + " won the game!");
			frame.add(label);
		}
		else if(won == false && player instanceof HumanPlayer) {
			label.setText(player.getName() + "'s accusation of " + Accusation.getRoom().getCardName() + ", " + Accusation.getPerson().getCardName() + ", and " + Accusation.getWeapon().getCardName() + " is incorrect. You lost!");
			frame.add(label);
		}
	}
	/*
	 * draws board to panel by calling each board cell's draw method
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		int width = this.getWidth();
		int height = this.getHeight();
		
		cellWidth = width / numColumns;
		cellHeight = height / numRows;
		
		updateOccupied();
		
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numColumns; c++) {
				BoardCell cell = getCell(r,c);
				if(!cell.getIsRoom()) {
					cell.draw(g2, c * cellWidth, r * cellHeight,cellWidth, cellHeight, this);
				}
			}
		}
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numColumns; c++) {
				BoardCell cell = getCell(r,c);
				if(cell.isLabel()) {
					cell.draw(g2, c * cellWidth, r * cellHeight,cellWidth, cellHeight, this);
				}
			}
		}
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numColumns; c++) {
				BoardCell cell = getCell(r,c);
				if(cell.getIsRoom()) {
					cell.draw(g2, c * cellWidth, r * cellHeight,cellWidth, cellHeight,this);
				}
			}
		}
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numColumns; c++) {
				BoardCell cell = getCell(r,c);
				if(cell.isDoorway()) {
					cell.draw(g2, c * cellWidth, r * cellHeight,cellWidth, cellHeight, this);
				}
			}
		}
		
		
		for(BoardCell cell: targets) {
			cell.draw(g2, cell.getColumn() * width, cell.getRow() * height, cellWidth, cellHeight, this);
		}
		
		for(Player player: players) {
			BoardCell cell = getCell(player.getRow(),player.getColumn());
			cell.draw(g2, player.getColumn() * cellWidth, player.getRow() * cellHeight,cellWidth, cellHeight, this);
		}
		
		
	}
	/*
	 * method that makes suer mouse click is on target
	 */
	public boolean targetClicked(int mouseX, int mouseY, int x, int y) {
		Rectangle cell = new Rectangle(x,y,cellWidth,cellHeight);
		if(cell.contains(new Point(mouseX,mouseY))) {
			return true;
		}
		return false;
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	/*
	 * if mouse is clicked check to see if it target, then see if it players turn and move player accordingly or display error message
	 * if it is not players turn do nothing
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		playButtonClick();
		BoardCell destination = null;
		for(BoardCell target: targets) {
			if(targetClicked(e.getX(), e.getY(), target.getColumn()* cellWidth, target.getRow() * cellHeight)){
				destination = target;
				break;
			}
		}
		if(destination != null && currentPlayer instanceof HumanPlayer) {
			getCell(currentPlayer.getRow(),currentPlayer.getColumn()).setOccupied(false);
			currentPlayer.setLocation(destination.getRow(), destination.getColumn());
			humanPlayerFinished = true;
			humanReadyForAccusation = false;
			targets.clear();
			this.repaint();
			if(getCell(currentPlayer.getRow(),currentPlayer.getColumn()).isRoomCenter()) {
				
				JDialog suggestion = new JDialog();
				suggestion.setSize(300,300);
				suggestion.setLayout(new GridLayout(4,0));
				
				JPanel first = new JPanel();
				JLabel roomLabel = new JLabel("Room:");
				first.add(roomLabel);
				JTextField room = new JTextField();
				room.setEditable(false);
				room.setText(roomMap.get(getCell(currentPlayer.getRow(),currentPlayer.getColumn()).getInitial()).getName());
				first.add(room);
				suggestion.add(first);
				
				JPanel second = new JPanel();
				JLabel personLabel = new JLabel("Person:");
				second.add(personLabel);
				JComboBox personCombo = new JComboBox();
				for(Player player: players) {
					personCombo.addItem(player.getName());
				}
				second.add(personCombo);
				suggestion.add(second);
				
				JPanel third = new JPanel();
				JLabel weaponLabel = new JLabel("Weapon:");
				third.add(weaponLabel);
				JComboBox weaponCombo = new JComboBox();
				for(String weapon: weapons) {
					weaponCombo.addItem(weapon);
				}
				third.add(weaponCombo);
				suggestion.add(third);
				
				JPanel fourth = new JPanel();
				JButton submit = new JButton("Submit");
				fourth.add(submit);
				submit.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						String person = (String) personCombo.getSelectedItem();
						String weapon = (String) weaponCombo.getSelectedItem();
						
						Card roomCard = new Card(roomMap.get(getCell(currentPlayer.getRow(),currentPlayer.getColumn()).getInitial()).getName(), CardType.ROOM);
						Card personCard = new Card(person,CardType.PERSON);
						Card weaponCard = new Card(weapon, CardType.ROOM);
						Solution suggested = new Solution(roomCard,personCard,weaponCard);
						handleSuggestion(suggested, players,currentPlayer);
						suggestion.dispose();
					}
				});
				JButton cancel = new JButton("Cancel");
				fourth.add(cancel);
				cancel.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {

						suggestion.dispose();
					}
				});
				suggestion.add(fourth);
				
				suggestion.setVisible(true);
			}
		}
		else if(destination == null && currentPlayer instanceof HumanPlayer) {
			JOptionPane.showMessageDialog(theInstance, "Please Select Valid Target.");
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	/*
	 * plays turn for each player, computers are done automatically while human waits on mouse click
	 */
	public void playTurn(Player player, int roll) {
		currentPlayer = player;
		BoardCell startCell = getCell(player.getRow(),player.getColumn());
		if(player instanceof HumanPlayer) {
			humanPlayerFinished = false;
			humanReadyForAccusation = true;
			calcTargets(startCell,roll);
			this.repaint();
		}
		else if(player instanceof ComputerPlayer){
			if(((ComputerPlayer) player).isReadyForAccusation()) {
				boolean won = checkAccusation(((ComputerPlayer) player).getAccusation());
				endScreen(won, player, ((ComputerPlayer)player).getAccusation());
			}
			else {
				BoardCell destination = ((ComputerPlayer) player).selectTarget(roll, this);
				if(destination.isRoomCenter()) {
					getCell(currentPlayer.getRow(),currentPlayer.getColumn()).setOccupied(false);
					player.setLocation(destination.getRow(), destination.getColumn());
					Solution suggestion = ((ComputerPlayer) player).createSuggestion(theInstance);
					handleSuggestion(suggestion, players, player);
					targets.clear();
					this.repaint();
				}
				else {
					getCell(currentPlayer.getRow(),currentPlayer.getColumn()).setOccupied(false);
					player.setLocation(destination.getRow(), destination.getColumn());
					targets.clear();
					this.repaint();
				}
			}
		}
	}
	
	
	/*
	 * creates adjacencies for all cells on board based on their behaviors(room center, doorway, walkway, etc.)
	 */
    public void createAdjacencies() {
    	
    	for(int r = 0; r < numRows; r++) {
    		for(int c = 0; c < numColumns; c++) {
    			
    			BoardCell cell = grid[r][c];
    			if(cell.isDoorway()) {
    				DoorDirection direction = cell.getDoorDirection();
    				char roomInitial = doorDirectionAdjacency(r, c, direction, 'C'); // initialized this using the entire function versus just setting to 'C'
    				
    				BoardCell roomCenter = roomMap.get(roomInitial).getCenterCell(); // getting the center cell of the current room
    				grid[r][c].addAdjacencies(roomCenter); 
    				roomCenter.addAdjacencies(grid[r][c]);
    				
    				walkwayAdjacency(r, c);
    				
    			}
    			else if(cell.getIsSecretPassage()) {
    				
    				Room current = roomMap.get(cell.getInitial());
	    			Room destination = roomMap.get(cell.getSecretPassage()); // gets secret passage cell of current room, if one exists
	    			current.getCenterCell().addAdjacencies(destination.getCenterCell());
	    			destination.getCenterCell().addAdjacencies(current.getCenterCell());

    			}
    			else if(cell.getInitial() == 'W') {
    				walkwayAdjacency(r, c);
    			}
    			
    		}
    	}
    	
    }
    
    /*
     * Creates adjacencies by checking to see if cardinal direction adjacent spaces are walkways
     */
	private void walkwayAdjacency(int r, int c) {
		
		if(r != 0 && grid[r - 1][c].getInitial() == 'W') {
			grid[r][c].addAdjacencies(grid[r - 1][c]);
		}
		if(r != numRows - 1 && grid[r + 1][c].getInitial() == 'W') {
			grid[r][c].addAdjacencies(grid[r + 1][c]);
		}
		if(c != 0 && grid[r][c - 1].getInitial() == 'W') {
			grid[r][c].addAdjacencies(grid[r][c - 1]);
		}
		if(c != numColumns - 1 && grid[r][c + 1].getInitial() == 'W') {
			grid[r][c].addAdjacencies(grid[r][c + 1]);
		}
		
	}
	
    /*
     * gets the initial of the room that a door is leading into so adjacencies can be calculated easier
     */
	private char doorDirectionAdjacency(int r, int c, DoorDirection direction, char roomInitial) {
		
		if(direction == DoorDirection.UP) {
			roomInitial = grid[r - 1][c].getInitial();
		}
		else if(direction == DoorDirection.DOWN) {
			roomInitial = grid[r + 1][c].getInitial();
		}
		else if(direction == DoorDirection.LEFT) {
			roomInitial = grid[r][c - 1].getInitial();
		}
		else if(direction == DoorDirection.RIGHT) {
			roomInitial = grid[r][c + 1].getInitial();
		}
		return roomInitial;
		
	}

    /*
     * reads in setup file and fills out roomMap and checks to see setup file is formatted correctly
     */
	public void loadSetupConfig() throws BadConfigFormatException {
    	
    	FileReader reader;
		try {
			reader = new FileReader(setupConfigFile);
			Scanner in = new Scanner(reader);
			while (in.hasNextLine()) {
				String [] line = in.nextLine().split(", ");
				fillData(line);
			}
			in.close();
			
			fillDeck();
			createAnswer();
			dealDeck();
			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		
    }
	/*
	 * fills data based off line read from file
	 */
	private void fillData(String[] line) throws BadConfigFormatException {
		if (line.length == 3) {
			String type = line[0].trim(); // initialize a variable to get type (either room or space)
			String name = line[1].trim(); // initialize a variable to get name of room/space
			char initial = line[2].trim().charAt(0); // initialize a variable to get the character of the room/space
			if (!type.equalsIgnoreCase("room") && !type.equalsIgnoreCase("space")) { 
				throw new BadConfigFormatException("Invalid room type: " + type);
			}
			roomMap.put(initial, new Room(name));
		}
		else if(line.length == 5) { // reads in player data using same method as above
			String type = line[0].trim(); // initialize a player type variable 
			String name = line[1].trim(); // initialize the player name
			String color = line[2].trim(); // initialize the player color
			int row = Integer.parseInt(line[3].trim());
			int column = Integer.parseInt(line[4].trim());
			if (!type.equalsIgnoreCase("Human") && !type.equalsIgnoreCase("Computer")) { 
				throw new BadConfigFormatException("Invalid player type: " + type);
			}
			if(type.equals("Human")) {
				players.add(new HumanPlayer(name,color,row,column));
			}
			else if(type.equals("Computer")) {
				players.add(new ComputerPlayer(name, color, row,column));
			}
		}
		else if(line.length == 2) { // reads in weapon data using same method
			String type = line[0].trim(); // initialize weapon type
			String name = line[1].trim(); // initialize weapon name
			if (!type.equalsIgnoreCase("Weapon")) { 
				throw new BadConfigFormatException("Invalid weapon type: " + type);
			}
			weapons.add(name);
		}
	}
    /*
     * fills deck with cards from weapons list, players list, and roomMap
     */
	public void fillDeck() {
		for(char initial: roomMap.keySet()){
			if(initial != 'W' && initial != 'X') {
				deck.add(new Card(roomMap.get(initial).getName(), CardType.ROOM));
			}
		}
		for(Player player: players) {
			deck.add(new Card(player.getName(), CardType.PERSON));
		}
		for(String weapon: weapons) {
			deck.add(new Card(weapon, CardType.WEAPON));
		}
	}
	/*
	 * Randomly picks a room, person, and weapon to make the solution
	 */
	public void createAnswer(){
		ArrayList<Card> rooms = new ArrayList<Card>();
		ArrayList<Card> people = new ArrayList<Card>();
		ArrayList<Card> w = new ArrayList<Card>();
		for(Card card: deck) {
			if(card.getCardType() == CardType.ROOM) {
				rooms.add(card);
			}
			else if(card.getCardType() == CardType.PERSON) {
				people.add(card);
			}
			else if(card.getCardType() == CardType.WEAPON) {
				w.add(card);
			}
		}
		
		// Randomly creating the card
		Random rand = new Random(System.currentTimeMillis());
		Card person = people.get(rand.nextInt(people.size()));
		Card weapon = w.get(rand.nextInt(weapons.size()));
		Card room = rooms.get(rand.nextInt(rooms.size()));
		
		theAnswer = new Solution(room,person,weapon);
	}
	/*
	 * deals each player randomly selected cards from the deck until the deck is empty
	 */
	public void dealDeck() {
		ArrayList<Card> tempDeck = new ArrayList<Card>();
		for(Card card: deck) {
			tempDeck.add(card);
		}
		tempDeck.remove(theAnswer.getPerson());
		tempDeck.remove(theAnswer.getRoom());
		tempDeck.remove(theAnswer.getWeapon());
		Random rand = new Random(System.currentTimeMillis());
		while(!tempDeck.isEmpty()) {
			for(Player player: players) {
				Card card = tempDeck.get(rand.nextInt(tempDeck.size()));
				player.updateHand(card);
				card.updateColor(player.getColor());
				tempDeck.remove(card);
				if(tempDeck.isEmpty()) {
					break;
				}
			}
		}
	}
	/*
	 * reads in layout file and checks valid dimensions, then calls fillGrid to declare all boardcells for the board
	 */
    public void loadLayoutConfig() throws BadConfigFormatException {
    	
    	FileReader reader;
		try {
			
			reader = new FileReader(layoutConfigFile);
			Scanner in = new Scanner(reader);
			
			ArrayList<String[]> tempGrid = new ArrayList<String[]>();
			String[] line = in.nextLine().split(",");
			numColumns = line.length;
			tempGrid.add(line);
			while (in.hasNextLine()) {
				line = in.nextLine().split(",");
				tempGrid.add(line);
			}
			
			numRows = tempGrid.size();
			
			for (String[] row : tempGrid) {
				if (row.length != numColumns) {
					throw new BadConfigFormatException();
				}
			}
			
			grid = new BoardCell[numRows][numColumns];
			
			fillGrid(tempGrid);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
    }
    
    /*
     * calls boardcell constructor based on the full string read on for the cell and its row and column
     * also declares each rooms center, label, and secret passage
     */
	private void fillGrid(ArrayList<String[]> tempGrid) throws BadConfigFormatException {
		
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numColumns; c++) {
				String cell = tempGrid.get(r)[c];
				if (!roomMap.containsKey(cell.charAt(0))) {
					throw new BadConfigFormatException("Cell symbol " + cell.charAt(0) + " not found.");
				}
				BoardCell boardCell = new BoardCell(cell, r, c); // creating a variable for current board cell so the code is easier to follow
				char initial = cell.charAt(0);  // creating a variable for the current room initial
				Room roomAt = roomMap.get(initial); // creating a variable for the room based on the initial
				
				if(boardCell.isRoomCenter()) {
					roomAt.setCenterCell(boardCell);
				}
				else if(boardCell.isLabel()) {
					roomAt.setLabelCell(boardCell);
				}
				else if(boardCell.getSecretPassage() != ' ') {
					roomAt.setSecretPassage(boardCell);
				}
				
				grid[r][c] = boardCell;
			}
		}
		
		updateOccupied();
	}
	/*
	 * sets occupied for all players to true
	 */
	public void updateOccupied() {
		
		for(Player player: players) {
			BoardCell cell = getCell(player.getRow(),player.getColumn());
			cell.setOccupied(true);
		}
	}
    
	/*
	 * clears memory of previous targets then calculates new targets based on startCell and path length
	 * Uses recursive function findAllTargets
	 */
	public void calcTargets(BoardCell startCell, int path) {
		
		targets.clear();
		visited.clear();
		visited.add(startCell);
		if(currentPlayer.isMovedBySuggestion()) {
			targets.add(startCell);
			currentPlayer.setMovedBySuggestion(false);
		}
		findAllTargets(startCell, path);
		
		
	}
	
	/*
	 * recursive function that findsAllTargets
	 * if adjCell is visited, isOccupied and not a roomCenter ignore it, or a non center room cell ignore it
	 * if adjCell is roomCenter add to targets and do not go any more forward
	 * else of the path length is 1 add adjcell to targets
	 * else decrease path length by 1 and call findAllTargtes again
	 */
	public void findAllTargets(BoardCell startCell, int pathlength) {	
		
		for(BoardCell adjCell: startCell.getAdjList()) {
			if(visited.contains(adjCell)) {
				
			}
			else if(adjCell.getIsOccupied() && !adjCell.isRoomCenter()) {//if occupied can not interact with space so continue
				
			}
			else if(adjCell.isRoomCenter()) {//if room can move there but not move further
				targets.add(adjCell);
				
			}
			else if(adjCell.getIsRoom()) {
				
			}
			else {
				visited.add(adjCell);
				if(pathlength == 1) {
					targets.add(adjCell);
				}
				else {
					findAllTargets(adjCell, pathlength - 1);
				}
				visited.remove(adjCell);
			}
			
		}
		
	}
	
    /*
     * sets the files for the board
     * had to add "data/" to the files because they are inside the data folder
     */
	public void setConfigFiles(String string, String string2) {
		
		this.layoutConfigFile = "data/" + string;
		this.setupConfigFile = "data/" + string2;
		
	}
	/*
	 * Individually checks to see if all 3 cards match, if they do returns true, else returns false
	 */
	public Boolean checkAccusation(Solution accusation) {
		if (!accusation.getRoom().equals(theAnswer.getRoom())) {
			return false;
		}
		else if (!accusation.getPerson().equals(theAnswer.getPerson())) {
			return false;
		}
		else if (!accusation.getWeapon().equals(theAnswer.getWeapon())) {
			return false;
		} 
		else {
			return true;
		}
		
	}
	/*
	 * First reorders the players into new ArrayList called newOrder to try and disprove the suggestion
	 * Then player by player it will try and disprove the suggestion by using the player's diproveSuggestion method
	 * Then saves whoever disproves the suggestion to player variable disprover to see if correct order was used
	 * Accuser can not disprove themselves
	 * if no one can disprove returns null
	 */
	public Card handleSuggestion(Solution suggestion, ArrayList<Player> tempPlayers, Player currentPlayer) {
		Player accusedPlayer;
		for(Player player2: players) {
			if(player2.getName().equals(suggestion.getPerson().getCardName()) && !(currentPlayer.getName().equals(player2.getName()))) {
				accusedPlayer = player2;
				getCell(accusedPlayer.getRow(),accusedPlayer.getColumn()).setOccupied(false);
				accusedPlayer.setLocation(currentPlayer.getRow(), currentPlayer.getColumn());
				accusedPlayer.setMovedBySuggestion(true);
				repaint();
				break;
			}
		}
		ArrayList<Player> newOrder = reorderPlayers(tempPlayers, currentPlayer);
		GameControlPanel.setGuess(suggestion.getPerson().getCardName() + ", " + suggestion.getRoom().getCardName() +", " + suggestion.getWeapon().getCardName(), currentPlayer.getColor());
		for(Player player: newOrder) {
			if(player.disproveSuggestion(suggestion) == null) {
				continue;
			}
			else {
				disprover = player;
				repaint();
				Card card = player.disproveSuggestion(suggestion);
				if(currentPlayer instanceof HumanPlayer) {
					GameControlPanel.setGuessResult(card.getCardName(), disprover.getColor());
				}
				else {
					GameControlPanel.setGuessResult("Suggestion Disproven", disprover.getColor());
				}
				currentPlayer.updateSeen(card);
				if(currentPlayer instanceof HumanPlayer) {
					ClueGame.getCardPanel().updateSeen(theInstance);
				}
				return card;
			}
		}
		disprover = null;
		GameControlPanel.setGuessResult("No new clue", "white");
		if(currentPlayer instanceof ComputerPlayer) {
			boolean hasCard = false;
			for(Card card: currentPlayer.getHand()) {
				if(card.equals(suggestion.getPerson()) || card.equals(suggestion.getRoom()) || card.equals(suggestion.getWeapon())) {
					hasCard = true;
					break;
				}
			}
			if(hasCard == false) {
				((ComputerPlayer) currentPlayer).setReadyForAccusation(true);
				((ComputerPlayer) currentPlayer).setAccusation(suggestion);
			}
		}
		return null;
	}
	/*
	 * reorders the list of players in order of turn for handling suggestion
	 */
	private ArrayList<Player> reorderPlayers(ArrayList<Player> tempPlayers, Player currentPlayer) {
		ArrayList<Player> newOrder = new ArrayList<Player>();
		
		int startIndex;
		if(tempPlayers.indexOf(currentPlayer) == tempPlayers.size() - 1) {
			startIndex = 0;
		}
		else {
			startIndex = tempPlayers.indexOf(currentPlayer) + 1;
		}
		while(newOrder.size() < tempPlayers.size() - 1) {
			if(startIndex == tempPlayers.size() - 1) {
				newOrder.add(tempPlayers.get(startIndex));
				startIndex = 0;
			}
			else {
				newOrder.add(tempPlayers.get(startIndex));
				startIndex++;
			}
		}
		return newOrder;
	}
	
	//Needed getters and setters
	public Room getRoom(char c) {
		
		return roomMap.get(c);
		
	}
	
	public int getNumRows() {
		
		return numRows;
		
	}
	
	public int getNumColumns() {
		
		return numColumns;
		
	}
	
	public BoardCell getCell(int i, int j) {
		
		return grid[i][j];
		
	}
	
	public Room getRoom(BoardCell cell) {
		
		return roomMap.get(cell.getInitial());
		
	}
	
	public Set<BoardCell> getAdjList(int i, int j) {
		
		return grid[i][j].getAdjList();
		
	}
	
	public Set<BoardCell> getTargets() {
		
		return targets;
		
	}
	
	public ArrayList<Player> getPlayers() {
		
		return players;
		
	}
	
	public ArrayList<Card> getDeck() {
		
		return deck;
		
	}
	
	public Map<Character, Room> getRoomMap() {
		
		return roomMap;
		
	}
	
	public ArrayList<String> getWeapons() {
		
		return weapons;
		
	}
	
	public Solution getTheAnswer() {
		
		return theAnswer;
		
	}
	public Player getDisprover() {
		
		return disprover;
		
	}
	public boolean isHumanReadyForAccusation() {
		return humanReadyForAccusation;
	}
	public BoardCell[][] getGrid() {
		return grid;
	}
	public boolean isHumanPlayerFinished() {
		return humanPlayerFinished;
	}
	
	
	
	
}
