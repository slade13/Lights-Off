package pl.realcode.lightsoff;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Game extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6704234675094754905L;
	public static JPanel contentPane;
	JPanel mainMenu;
	JLabel lblLightsOffGame;
	private boolean isLightOn = false;
	ButtonGroup diffGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
            try {
                Game frame = new Game();
                frame.setVisible(true);
            } catch (Exception e) {
				JOptionPane.showMessageDialog(Game.contentPane,
					"Exception was thrown: " + e.getMessage());
                e.printStackTrace();
            }
        });
	}

	/**
	 * Create the frame.
	 */
	public Game() {
		setTitle("lightsOFF::" + "Main menu");
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 400);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		mainMenu = new JPanel();
		mainMenu.setName("Main menu");
		mainMenu.setBackground(Color.BLACK);
		contentPane.add(mainMenu, "MENU");
		mainMenu.setLayout(null);
		
		JButton btnAuthor = new JButton("Author");
		btnAuthor.addActionListener(this);
		btnAuthor.setBounds(125, 220, 140, 30);
		mainMenu.add(btnAuthor);
		
		JButton btnAboutGame = new JButton("About game");
		btnAboutGame.addActionListener(this);
		btnAboutGame.setBounds(125, 180, 140, 30);
		mainMenu.add(btnAboutGame);
		
		JButton btnTutorial = new JButton("Tutorial");
		btnTutorial.addActionListener(this);
		btnTutorial.setBounds(125, 140, 140, 30);
		mainMenu.add(btnTutorial);
		
		JButton btnStartGame = new JButton("Start game");
		btnStartGame.addActionListener(this);
		btnStartGame.setBounds(125, 100, 140, 30);
		mainMenu.add(btnStartGame);
		
		lblLightsOffGame = new JLabel("Lights Off Game");
		lblLightsOffGame.setForeground(Color.WHITE);
		lblLightsOffGame.setHorizontalAlignment(SwingConstants.CENTER);
		lblLightsOffGame.setFont(new Font("Calibri", Font.PLAIN, 29));
		lblLightsOffGame.setBounds(76, 23, 232, 59);
		mainMenu.add(lblLightsOffGame);
		
		JPanel author = new JPanel();
		author.setName("Author");
		contentPane.add(author, "AUTHOR");
		author.setLayout(null);

		JTextArea aboutMeTextArea = getAboutMeTextArea();
		author.add(aboutMeTextArea);
		
		JButton btnBack = new JButton("Main menu");
		btnBack.addActionListener(this);
		btnBack.setBounds(264, 317, 100, 23);
		author.add(btnBack);
		
		JPanel aboutGame = new JPanel();
		aboutGame.setName("About game");
		contentPane.add(aboutGame, "ABOUTGAME");
		aboutGame.setLayout(null);

		JTextArea gameDescriptionTextArea = getGameDescriptionTextArea();
		aboutGame.add(gameDescriptionTextArea);
		
		JButton btnBack_1 = new JButton("Main menu");
		btnBack_1.addActionListener(this);
		btnBack_1.setBounds(264, 314, 100, 23);
		aboutGame.add(btnBack_1);
		
		JPanel startGame = new JPanel();
		startGame.setName("Start game");
		contentPane.add(startGame, "STARTGAME");
		startGame.setLayout(null);
		
		JButton btnBack_2 = new JButton("Main menu");
		btnBack_2.addActionListener(this);
		btnBack_2.setBounds(274, 327, 100, 23);
		startGame.add(btnBack_2);
		
		JLabel lblDifficulty = new JLabel("Difficulty:");
		lblDifficulty.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblDifficulty.setBounds(52, 85, 77, 23);
		startGame.add(lblDifficulty);
		
		JRadioButton diffEasy = new JRadioButton("easy");
		diffEasy.setActionCommand("GAMEPLAYEASY");
		diffEasy.setSelected(true);
		diffEasy.setBounds(135, 90, 109, 23);
		startGame.add(diffEasy);
		
		JRadioButton diffMedium = new JRadioButton("medium");
		diffMedium.setActionCommand("GAMEPLAYMEDIUM");
		diffMedium.setBounds(135, 130, 109, 23);
		startGame.add(diffMedium);
		
		JRadioButton diffHard = new JRadioButton("hard");
		diffHard.setActionCommand("GAMEPLAYHARD");
		diffHard.setBounds(135, 170, 109, 23);
		startGame.add(diffHard);
		
		// Radio Button group
		diffGroup.add(diffEasy);
		diffGroup.add(diffMedium);
		diffGroup.add(diffHard);
		
		JButton btnLetTheGame = new JButton("Let the game begin...");
		btnLetTheGame.setForeground(Color.WHITE);
		btnLetTheGame.setBackground(Color.BLACK);
		btnLetTheGame.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLetTheGame.setBounds(52, 238, 258, 55);
		startGame.add(btnLetTheGame);
		
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(8, 1));
		JLabel tut1 = new JLabel(new ImageIcon(Game.class.getResource("/Images/1.png")));
		tut1.setText("Example of solving 3x3 puzzle");
		tut1.setVerticalTextPosition(JLabel.TOP);
		tut1.setHorizontalTextPosition(JLabel.CENTER);
		jp.add(tut1);
		
		JLabel tut2 = new JLabel(new ImageIcon(Game.class.getResource("/Images/11.png")));
		tut2.setText("Step 1:");
		tut2.setVerticalTextPosition(JLabel.TOP);
		tut2.setHorizontalTextPosition(JLabel.CENTER);
		jp.add(tut2);
		
		JLabel tut3 = new JLabel(new ImageIcon(Game.class.getResource("/Images/21.png")));
		tut3.setText("Step 2:");
		tut3.setVerticalTextPosition(JLabel.TOP);
		tut3.setHorizontalTextPosition(JLabel.CENTER);
		jp.add(tut3);
		
		JLabel tut4 = new JLabel(new ImageIcon(Game.class.getResource("/Images/31.png")));
		tut4.setText("Step 3:");
		tut4.setVerticalTextPosition(JLabel.TOP);
		tut4.setHorizontalTextPosition(JLabel.CENTER);
		jp.add(tut4);
		
		JLabel tut5 = new JLabel(new ImageIcon(Game.class.getResource("/Images/41.png")));
		tut5.setText("Step 4:");
		tut5.setVerticalTextPosition(JLabel.TOP);
		tut5.setHorizontalTextPosition(JLabel.CENTER);
		jp.add(tut5);
		
		JLabel tut6 = new JLabel(new ImageIcon(Game.class.getResource("/Images/5.png")));
		tut6.setText("Solved puzzle:");
		tut6.setVerticalTextPosition(JLabel.TOP);
		tut6.setHorizontalTextPosition(JLabel.CENTER);
		jp.add(tut6);
		
		JLabel tut7 = new JLabel(new ImageIcon(Game.class.getResource("/Images/animacja.gif")));
		tut7.setText("Animation of step by step solution:");
		tut7.setVerticalTextPosition(JLabel.TOP);
		tut7.setHorizontalTextPosition(JLabel.CENTER);
		jp.add(tut7);
		
		JButton tutorialBackButton = new JButton("Main menu");
		tutorialBackButton.addActionListener(this);
		jp.add(tutorialBackButton);
		
		JScrollPane tutorial = new JScrollPane(jp);
		tutorial.setName("Tutorial");
		contentPane.add(tutorial, "TUTORIAL");		
		
		PaintPanel3 gameplayScreen3 = new PaintPanel3();
		gameplayScreen3.setBackground(Color.WHITE);
		gameplayScreen3.setBorder(null);
		gameplayScreen3.setLayout(null);
		gameplayScreen3.setName("Game Easy");
		contentPane.add(gameplayScreen3, "GAMEPLAYEASY");
		
		JLabel lblEasy = new JLabel("Easy");
		lblEasy.setBounds(194, 317, 46, 14);
		gameplayScreen3.add(lblEasy);
		
		JButton btnBack_5 = new JButton("Back");
		btnBack_5.setBounds(285, 327, 89, 23);
		gameplayScreen3.add(btnBack_5);
		
		PaintPanel4 gameplayScreen4 = new PaintPanel4();
		gameplayScreen4.setBackground(Color.WHITE);
		gameplayScreen4.setBorder(null);
		gameplayScreen4.setLayout(null);
		gameplayScreen4.setName("Game Medium");
		contentPane.add(gameplayScreen4, "GAMEPLAYMEDIUM");
		
		JLabel lblMedium = new JLabel("Medium");
		lblMedium.setBounds(170, 324, 46, 14);
		gameplayScreen4.add(lblMedium);
		
		JButton btnBack_4 = new JButton("Back");
		btnBack_4.setBounds(285, 327, 89, 23);
		gameplayScreen4.add(btnBack_4);
		
		PaintPanel5 gameplayScreen5 = new PaintPanel5();
		gameplayScreen5.setBackground(Color.WHITE);
		gameplayScreen5.setBorder(null);
		gameplayScreen5.setLayout(null);
		gameplayScreen5.setName("Game Hardcore");
		contentPane.add(gameplayScreen5, "GAMEPLAYHARD");
		
		JLabel lblHard = new JLabel("Hard");
		lblHard.setBounds(164, 321, 46, 14);
		gameplayScreen5.add(lblHard);
		
		JButton btnBack_3 = new JButton("Back");
		btnBack_3.setBounds(285, 327, 89, 23);
		gameplayScreen5.add(btnBack_3);
		
		// Main menu buttons
		
		// Title clicked
		lblLightsOffGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(isLightOn) {
					contentPane.setBackground(Color.WHITE);
					mainMenu.setBackground(Color.BLACK);
					lblLightsOffGame.setForeground(Color.WHITE);
					isLightOn=false;
				} else {
					contentPane.setBackground(Color.BLACK);
					mainMenu.setBackground(Color.WHITE);
					lblLightsOffGame.setForeground(Color.BLACK);
					isLightOn=true;
				}
			}
		});
		
		// "Start game"
		btnStartGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout card = (CardLayout) getContentPane().getLayout();
				card.show( getContentPane(), "STARTGAME" );
			}
		});
		
		// "Tutorial"
		btnTutorial.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout card = (CardLayout) getContentPane().getLayout();
				card.show( getContentPane(), "TUTORIAL" );
			}
		});
		
		// "About game"
		btnAboutGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout card = (CardLayout) getContentPane().getLayout();
				card.show( getContentPane(), "ABOUTGAME" );
			}
		});
		
		// "Author"
		btnAuthor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout card = (CardLayout) getContentPane().getLayout();
				card.show( getContentPane(), "AUTHOR" );
			}
		});
		
		// Specific Cards
		
		// Author button - "Back"
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout card = (CardLayout) getContentPane().getLayout();
				card.show( getContentPane(), "MENU" );
			}
		});
		
		// About game button - "Back"

		btnBack_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout card = (CardLayout) getContentPane().getLayout();
				card.show( getContentPane(), "MENU" );
			}
		});
		
		// Tutorial button - "Back"
		tutorialBackButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				CardLayout card = (CardLayout) getContentPane().getLayout();
				card.show( getContentPane(), "MENU" );
			}
		});
		
		// Start game - "Back"
		btnBack_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout card = (CardLayout) getContentPane().getLayout();
				card.show( getContentPane(), "MENU" );
			}
		});
		
		// Start game - "start"
		btnLetTheGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String gameScreen = diffGroup.getSelection().getActionCommand();
				CardLayout card = (CardLayout) getContentPane().getLayout();
				card.show( getContentPane(), gameScreen );
			}
		});
		
		// Events for game
		//HARD
		btnBack_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout card = (CardLayout) getContentPane().getLayout();
				card.show( getContentPane(), "STARTGAME" );
			}
		});
		//MEDIUM
		btnBack_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout card = (CardLayout) getContentPane().getLayout();
				card.show( getContentPane(), "STARTGAME" );
			}
		});
		//EASY
		btnBack_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout card = (CardLayout) getContentPane().getLayout();
				card.show( getContentPane(), "STARTGAME" );
			}
		});
		
	}

	private static JTextArea getGameDescriptionTextArea() {
		JTextArea txtrAbout = new JTextArea();
		txtrAbout.setEditable(false);
		txtrAbout.setBounds(0, 0, 374, 310);
		txtrAbout.setFont(new Font("Calibri", Font.PLAIN, 16));
		txtrAbout.setText("A one-person game played on a rectangular\r\nlattice of lamps which can be turned \r\non and off. A move consists of flipping \r\na \"switch\" inside one of the squares, \r\nthereby toggling the on/off state of this and \r\nall four vertically and horizontally adjacent \r\nsquares. Starting from a randomly chosen \r\nlight pattern, the aim is to turn all the lamps \r\noff. The problem of determining if it is \r\npossible to start from set of all lights being \r\non to all lights being off is known as the \r\n\"all-ones problem.\"");
		return txtrAbout;
	}

	private static JTextArea getAboutMeTextArea() {
		JTextArea txtrHiMyName = new JTextArea();
		txtrHiMyName.setEditable(false);
		txtrHiMyName.setFont(new Font("Calibri", Font.PLAIN, 16));
		txtrHiMyName.setLineWrap(true);
		txtrHiMyName.setText("Hi! My name is Mateusz Lubiński. \r\nI study Computer Science on The State School \r\nof Higher Professional Education in Elbląg. \r\nMy hobbies are programming, riding on a mountain bike,\r\nrunning, exercising on a gym including calisthenics \r\nbut the foremost i love to eat.");
		txtrHiMyName.setBounds(0, 0, 374, 310);
		return txtrHiMyName;
	}

	public void actionPerformed(ActionEvent ae) {
		setTitle("lightsOFF::" + ae.getActionCommand());
	}
}
