package clueGame;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class ClueGame extends JFrame{
	private static Board board;
	private static GameControlPanel controlPanel;
	private static CardPanel cardPanel;
	
	/*
	 * creates main game frame and adds needed panels
	 */
	public ClueGame() {
		this.setSize(1750,1000);
		controlPanel = new GameControlPanel(board);
		this.add(controlPanel,BorderLayout.SOUTH);
		cardPanel = new CardPanel(board);
		this.add(cardPanel,BorderLayout.EAST);
		this.add(board, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		this.setVisible(true); // make it visible
		
	}
	/*
	 * updates roll and player then displays it on control panel
	 */
	public static void nextTurn(Player player) {
		Random rand = new Random(System.currentTimeMillis());
		int roll = rand.nextInt(6) + 1;
		controlPanel.setTurn(player,roll);
		board.playTurn(player, roll);
	}
	
	public static CardPanel getCardPanel() {
		return cardPanel;
	}
	

	
	/*
	 * creates frame and initial message
	 */
	
	public static void main(String[] args) throws BadConfigFormatException, MalformedURLException, LineUnavailableException, IOException, UnsupportedAudioFileException {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
		ClueGame frame = new ClueGame();  // create the frame 
		JOptionPane.showMessageDialog(frame, "You are " + board.getPlayers().get(0).getName() + ". Can you find the solution before the computer players?");
		nextTurn(board.getPlayers().get(0));

	}
	
	
}
