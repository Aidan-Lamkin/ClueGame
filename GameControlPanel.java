package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {
	private static JTextField guess = new JTextField(20);
	private static JTextField guessResult = new JTextField(20);
	private Player player;
	private JTextField playerRoll = new JTextField(5);
	private JTextField name = new JTextField(20);
	private Color color = Color.white;

	
	/**
	 * Constructor for the panel, it does 90% of the work
	 */
	public GameControlPanel(Board board)  {
		setTurn(board.getPlayers().get(0), 5);
		setGuess( "I have no guess!", player.getColor());
		setGuessResult( "So you have nothing?", player.getColor());
		this.setLayout(new GridLayout(2,0));
		JPanel panel = new JPanel();
		JPanel panel1 = createTurnPanel(board);
		panel.add(panel1);
		JPanel panel2 = createGuessPanel();
		panel.add(panel2);
		add(panel);
	}
	/*
	 * creates top half
	 */
	private JPanel createTurnPanel(Board board) {
		JPanel panel = new JPanel();
		JButton button = new JButton();
		panel.setLayout(new GridLayout(1,4));
		JPanel panel1 = createWhoseTurnPanel();
		panel.add(panel1);
		JPanel panel2 = createRollPanel();
		panel.add(panel2);
		button = createMakeAccusationButton();
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				board.playButtonClick();
				if(player instanceof HumanPlayer && board.isHumanReadyForAccusation()){
					board.humanAccusation(player);
				}
				else {
					board.accusationErrorMessage();
				}
			}
		});
		panel.add(button);
		button = createNextButton();
		/*
		 * waits for player to finish turn then allows next button to work, displays error if player has
		 * not finished turn
		 */
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				board.playButtonClick();
				if(player instanceof HumanPlayer) {
					boolean finished = board.isHumanPlayerFinished();
					if(finished == false) {
						board.finishTurn();
						return;
					}
				}
				int index = board.getPlayers().indexOf(player);
				if(index == board.getPlayers().size() - 1) {
					player = board.getPlayers().get(0);
				}
				else {
					player = board.getPlayers().get(index + 1);
				}
				ClueGame.nextTurn(player);
			}
		});
		panel.add(button);
		return panel;
		
	}
	/*
	 * creates bottom half
	 */
	private JPanel createGuessPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		JPanel panel1 = createBottomLeftPanel();
		panel.add(panel1);
		JPanel panel2 = createBottomRightPanel();
		panel.add(panel2);
		return panel;
		
	}
	/*
	 * creates guess panel
	 */
	private JPanel createBottomLeftPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		panel.setLayout(new GridLayout(1,0));
		guess.setEditable(false);
		panel.add(guess);
		return panel;
	}
	/*
	 * creates guess result panel
	 */
	private JPanel createBottomRightPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		panel.setLayout(new GridLayout(1,0));
		guessResult.setEditable(false);
		panel.add(guessResult);
		return panel;
	}
	/*
	 * creates whose turn panel
	 */
	private JPanel createWhoseTurnPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,0));
		JLabel label = new JLabel("Whose turn?");
		panel.add(label);
		name.setEditable(false);
		name.setBackground(color);
		panel.add(name);
		return panel;
		
	}
	/*
	 * creates roll panel
	 */
	private JPanel createRollPanel () {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Roll");
		panel.add(label);
		playerRoll.setEditable(false);
		panel.add(playerRoll);
		return panel;
		
	}
	/*
	 * creates make accusation button
	 */
	private JButton createMakeAccusationButton() {
		JButton button = new JButton("Make Accusation");
		return button;
		
	}
	/*
	 * creates next button
	 */
	private JButton createNextButton() {
		JButton button = new JButton("NEXT!");
		return button;
	}
	
	
	//Needed setters
	public static void setGuessResult(String string, String color) {
		guessResult.setText(string);
		guessResult.setBackground(chooseColor(color));
	}

	public static void setGuess(String string, String color) {
		guess.setText(string);
		guess.setBackground(chooseColor(color));
	}

	public void setTurn(Player player, int i) {
		this.player = player;	
		name.setText(player.getName());
		setColor(player.getColor());
		name.setBackground(color);
		playerRoll.setText(String.valueOf(i));
		
	}
	
	public void setColor(String str) {
		
		if(str.equals("red")) {
			color = Color.red;
		}
		else if(str.equals("orange")) {
			color = Color.orange;
		}
		else if(str.equals("green")) {
			color = Color.green;
		}
		else if(str.equals("blue")) {
			color = Color.blue;
		}
		else if(str.equals("yellow")) {
			color = Color.yellow;
		}
		else if(str.equals("cyan")) {
			color = Color.cyan;
		}
		else if(str.equals("pink")) {
			color = Color.pink;
		}
		
	}
	
	public static Color chooseColor(String str) {
		Color color2 = Color.white;
		if(str.equals("red")) {
			color2 = Color.red;
		}
		else if(str.equals("orange")) {
			color2 = Color.orange;
		}
		else if(str.equals("green")) {
			color2 = Color.green;
		}
		else if(str.equals("blue")) {
			color2 = Color.blue;
		}
		else if(str.equals("yellow")) {
			color2 = Color.yellow;
		}
		else if(str.equals("cyan")) {
			color2 = Color.cyan;
		}
		else if(str.equals("pink")) {
			color2 = Color.pink;
		}
		return color2;
	}
	
	
}
