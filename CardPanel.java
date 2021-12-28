package clueGame;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardPanel extends JPanel{
	private static Player player;
	
	
	private  Set<Card> peopleHand = new HashSet<Card>();
	private  Set<Card> weaponsHand = new HashSet<Card>();
	private  Set <Card> roomsHand = new HashSet<Card>();
	
	private  Set<Card> peopleSeen = new HashSet<Card>();
	private  Set<Card> weaponsSeen = new HashSet<Card>();
	private  Set <Card> roomsSeen = new HashSet<Card>();
	
	private  JPanel pSeen = new JPanel();
	private  JPanel rSeen = new JPanel();
	private  JPanel wSeen = new JPanel();
	/*
	 * Constructor that sets up 3 sub panels and instance variables
	 */
	public CardPanel(Board board) {
		JPanel panel = new JPanel();
		this.setPlayer(board.getPlayers().get(0));
		this.setCards();
		this.setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));

		this.setLayout(new GridLayout(3,0));
		this.add(peoplePanel());
		this.add(roomPanel());
		this.add(weaponPanel());
		
	}
	/*
	 * Shows person cards and iterates over both the hand and seen lists
	 */
	public JPanel peoplePanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		panel.setLayout(new GridLayout(2,0));
		
		JPanel hand = new JPanel();
		if(peopleHand.size() == 0) {
			hand.setLayout(new GridLayout(2,0));
			JLabel inHand = new JLabel("In Hand:");
			inHand.setSize(200,20);
			panel.add(inHand);
			JTextField text = new JTextField(20);
			text.setEditable(false);
			text.setText("None");
			hand.add(text);
		}
		else {
			hand.setLayout(new GridLayout(peopleHand.size() + 1,0));
			JLabel inHand = new JLabel("In Hand:");
			inHand.setSize(200,20);
			panel.add(inHand);
			for(Card card: peopleHand) {
				JTextField text = new JTextField(20);
				text.setEditable(false);
				text.setText(card.getCardName());
				text.setBackground(card.getColor());
				hand.add(text);
			}
		}
		panel.add(hand);
		
		
		
		
		if(peopleSeen.size() == 0) {
			pSeen.setLayout(new GridLayout(2,0));
			JLabel isSeen = new JLabel("Seen:");
			isSeen.setSize(200,20);
			panel.add(isSeen);
			JTextField text = new JTextField(20);
			text.setEditable(false);
			text.setText("None");
			pSeen.add(text);
		}
		else {
			pSeen.setLayout(new GridLayout(peopleSeen.size() + 1,0));
			JLabel isSeen = new JLabel("Seen:");
			isSeen.setSize(200,20);
			panel.add(isSeen);
			for(Card card: peopleSeen) {
				JTextField text = new JTextField(20);
				text.setEditable(false);
				text.setText(card.getCardName());
				text.setBackground(card.getColor());
				pSeen.add(text);
			}
		}
		panel.add(pSeen);
		return panel;
	}
	/*
	 * Shows room cards and iterates over both the hand and seen lists
	 */
	public JPanel roomPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Room"));
		panel.setLayout(new GridLayout(2,0));
		JPanel hand = new JPanel();
		if(roomsHand.size() == 0) {
			hand.setLayout(new GridLayout(2,0));
			JLabel inHand = new JLabel("In Hand:");
			inHand.setSize(200,20);
			panel.add(inHand);
			JTextField text = new JTextField(20);
			text.setEditable(false);
			text.setText("None");
			hand.add(text);
		}
		else {
			hand.setLayout(new GridLayout(roomsHand.size() + 1,0));
			JLabel inHand = new JLabel("In Hand:");
			inHand.setSize(200,20);
			panel.add(inHand);
			for(Card card: roomsHand) {
				JTextField text = new JTextField(20);
				text.setEditable(false);
				text.setText(card.getCardName());
				text.setBackground(card.getColor());
				hand.add(text);
			}
		}
		panel.add(hand);
		
		
		
		if(roomsSeen.size() == 0) {
			rSeen.setLayout(new GridLayout(2,0));
			JLabel isSeen = new JLabel("Seen:");
			isSeen.setSize(200,20);

			panel.add(isSeen);
			JTextField text = new JTextField(20);
			text.setEditable(false);
			text.setText("None");
			rSeen.add(text);
		}
		else {
			rSeen.setLayout(new GridLayout(roomsSeen.size() + 1,0));
			JLabel isSeen = new JLabel("Seen:");
			isSeen.setSize(200,20);

			panel.add(isSeen);
			for(Card card: roomsSeen) {
				JTextField text = new JTextField(20);
				text.setEditable(false);
				text.setText(card.getCardName());
				text.setBackground(card.getColor());
				rSeen.add(text);
			}
		}
		panel.add(rSeen);
		return panel;
	}
	/*
	 * Shows weapon cards and iterates over both the hand and seen lists
	 */
	public JPanel weaponPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		panel.setLayout(new GridLayout(2,0));
		
		
		JPanel hand = new JPanel();
		if(weaponsHand.size() == 0) {
			hand.setLayout(new GridLayout(1,0));
			JLabel inHand = new JLabel("In Hand:");
			inHand.setSize(200,20);
			panel.add(inHand);
			JTextField text = new JTextField(20);
			text.setEditable(false);
			text.setText("None");
			hand.add(text);
		}
		else {
			hand.setLayout(new GridLayout(weaponsHand.size() + 1, 0));
			JLabel inHand = new JLabel("In Hand:");
			inHand.setSize(200,20);
			panel.add(inHand);
			for(Card card: weaponsHand) {
				JTextField text = new JTextField(20);
				text.setEditable(false);
				text.setText(card.getCardName());
				text.setBackground(card.getColor());
				hand.add(text);
			}
		}
		panel.add(hand);
		
		if(weaponsSeen.size() == 0) {
			wSeen.setLayout(new GridLayout(2,0));
			JLabel isSeen = new JLabel("Seen:");
			isSeen.setSize(200,20);

			panel.add(isSeen);
			JTextField text = new JTextField(20);
			text.setEditable(false);
			text.setText("None");
			wSeen.add(text);
		}
		else {
			wSeen.setLayout(new GridLayout(weaponsSeen.size() + 1,0));
			JLabel isSeen = new JLabel("Seen:");
			isSeen.setSize(200,20);

			panel.add(isSeen);
			for(Card card: weaponsSeen) {
				JTextField text = new JTextField(20);
				text.setEditable(false);
				text.setText(card.getCardName());
				text.setBackground(card.getColor());
				wSeen.add(text);
			}
		}
		panel.add(wSeen);
		return panel;
	}
	/*
	 * declares player variable and updates hand and seen to show example
	 */
	public void setPlayer(Player player) {
		this.player = player;
		
	}
	/*
	 * fills card lists based on type and whether it is in the hand or in seen
	 */
	public void setCards() {
		for(Card card:player.getHand()) {
			if(card.getCardType() == CardType.PERSON) {
				peopleHand.add(card);
			}
			else if(card.getCardType() == CardType.ROOM) {
				roomsHand.add(card);
			}
			else if(card.getCardType() == CardType.WEAPON) {
				weaponsHand.add(card);
			}
		}
		for(Card card:player.getSeen()) {
			if(card.getCardType() == CardType.PERSON) {
				peopleSeen.add(card);
			}
			else if(card.getCardType() == CardType.ROOM) {
				roomsSeen.add(card);
			}
			else if(card.getCardType() == CardType.WEAPON) {
				weaponsSeen.add(card);
			}
		}
		
	}
	/*
	 * method that updates which card the human player has seen and displays them
	 */
	public void updateSeen(Board board) {
			this.removeAll();
			this.revalidate();
			this.setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
			this.setLayout(new GridLayout(3,0));
			
			this.setCards();
			pSeen.removeAll();
			this.add(peoplePanel());
			rSeen.removeAll();
			this.add(roomPanel());
			wSeen.removeAll();
			this.add(weaponPanel());
			this.repaint();
		}
	}
	

