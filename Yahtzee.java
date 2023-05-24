/*Caitlyn Brodie and Lucus Burg
Yahtzee*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.Timer;

public class Yahtzee
{
	//for the GUI
	JFrame theWindow;
	Container thePane;
	//the main panel
		MyPanel gamePanel;
	MyPanel buttonPanel, leftPanel, dicePanel, scorePanel;
	MyPanel grid [];
	JLabel boxes [][];
	JLabel currRoll, highScore;
	JButton buttons [];
	JButton diceButtons [];
	JButton newGame, roll, zero, quit;
	MyListener theListener;

	public static final int SIDES = 6;

	//Call Die class
	//Always 5 dice
		Die dice [] = new Die[5];

	//ScoreCard class - 6 games to play
		ScoreCard scoreCard [] = new ScoreCard[6];
	//Initalize variables
	int rollNum = 0, round = 1, tempScore = 0;
	boolean scored = false, zeroing = false, rolling = false;
	Timer timer;
	Clip yahtzeeClip, rollingClip;

	//Menu
	int delay = 60, hiScore;
	JButton save, close;
	JFrame preferences;
	JSlider slider;
	JLabel currVal;
	SliderChangeListener sliderListener;

	//Main class to play the yahtzee class
	public static void main(String [] args)
	{
		new Yahtzee();
	}

//Constructor
	public Yahtzee()
	{
		//Initalize the from the die and score card classes
		for (int i = 0; i < 5; i++)
			dice[i] = new Die(SIDES);
		for (int i = 0; i < 6; i++)
			scoreCard[i] = new ScoreCard();
		setup();
		forceReRoll();
	}

	public void setup()
	{
		theWindow = new JFrame("YAHTZEE - by Caitlyn and Lucus");
		thePane = theWindow.getContentPane();
		thePane.setLayout(new GridLayout(1, 2));
		theWindow.setLocation(150, 150);

		gamePanel = new MyPanel(1000, 540, 1, 2);
		gamePanel.addKeyListener(new InputHandler());
		gamePanel.setFocusable(true);	// needed for keyboard input

		leftPanel = new MyPanel(550, 540, 3, 1);
		JLabel yahtzee = new JLabel("        YAHTZEE");
		yahtzee.setFont(new Font("Arial Bold", Font.PLAIN, 72));
		yahtzee.setForeground(Color.PINK);
		leftPanel.add(yahtzee);

		theListener = new MyListener();

		setupDicePanel();
		leftPanel.add(dicePanel);

		setupButtonPanel();
		leftPanel.add(buttonPanel);
		gamePanel.add(leftPanel);

		setupScorePanel();
		gamePanel.add(scorePanel);

		thePane.add(gamePanel);
		theWindow.pack();
		theWindow.setVisible(true);

		// for the rolling animation
		timer = new Timer(delay, theListener);
		timer.start();
		timer.stop();
	}

	public void setupDicePanel()
	{
		dicePanel = new MyPanel(400, 180, 1, 7);
		diceButtons = new JButton[5];

		//Blank to space out
		dicePanel.add(new JPanel());
		for (int i = 0; i < 5; i++)
		{
			diceButtons[i] = new JButton();
			diceButtons[i].setFont(new Font("Helvetica", Font.PLAIN, 50));
			diceButtons[i].addActionListener(theListener);
			dicePanel.add(diceButtons[i]);
		}
		//Blank to space out
		dicePanel.add(new JPanel());

		//Add question marks to buttons
		questionMarks();
	}

	public void setupButtonPanel()
	{
		buttonPanel = new MyPanel(550, 40, 2, 4);

		currRoll = new JLabel("  Current Roll: " + rollNum);
		currRoll.setFont(new Font("Helvetica", Font.PLAIN, 16));
		buttonPanel.add(currRoll);

		//Blank for spacing
		buttonPanel.add(new JLabel());
		buttonPanel.add(new JLabel());

		try
		{
			ObjectInputStream IS = new ObjectInputStream(new FileInputStream("highscore"));
			hiScore = IS.readInt();
			IS.close();
		}
		catch (Exception e)
		{
			try{
			System.out.println("Creating highscore file");
			ObjectOutputStream OS = new ObjectOutputStream(new FileOutputStream("highscore"));
			OS.writeInt(150);
			hiScore = 150;
			OS.close();
			}
			catch (Exception f)
			{
				System.out.println(f + "\nException writing");
			}
		}


		highScore = new JLabel("High Score: " + hiScore);
		buttonPanel.add(highScore);

		newGame = new JButton("New Game [N]");
		newGame.addActionListener(theListener);
		buttonPanel.add(newGame);
		roll = new JButton("Roll [R]");
		roll.addActionListener(theListener);
		buttonPanel.add(roll);
		zero = new JButton("Zero [Z]");
		zero.addActionListener(theListener);
		buttonPanel.add(zero);
		quit = new JButton("Quit");
		quit.addActionListener(theListener);
		buttonPanel.add(quit);

	}

	public void setupScorePanel()
	{
		scorePanel = new MyPanel(400,540);
		grid = new MyPanel[7];
		boxes = new JLabel[22][7];
		JLabel l0;
		buttons = new JButton[14];

		grid[0] = new MyPanel(120, 540, 22, 1);
		for (int i = 1; i < 7; i++)
		{
			grid[i] = new MyPanel(40, 540, 22, 1);
			l0 = new JLabel("  #" + i);
			l0.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
			boxes[0][i] = l0;
			for (int j = 1; j < 10; j++)
			{
				boxes[j][i] = new JLabel();
			boxes[j][i].setBorder(new MatteBorder(0, 1, 1, 1, Color.BLACK));
			}
			boxes[10][i] = new JLabel();
			boxes[10][i].setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));

			for (int j = 11; j < 22; j++)
			{
				boxes[j][i] = new JLabel();
				boxes[j][i].setBorder(
								new MatteBorder(0, 1, 1, 1, Color.BLACK));
			}
			for (int j = 0; j < 22; j++)
				grid[i].add(boxes[j][i]);
		}

		JLabel l1 = new JLabel("  Upper Section");
		l1.setFont(new Font("Helvetica", Font.BOLD, 14));
		l1.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
		grid[0].add(l1);
		buttons[0] = new JButton("Aces   \t [1]");
		buttons[1] = new JButton("Twos   \t [2]");
		buttons[2] = new JButton("Threes \t[3]");
		buttons[3] = new JButton("Fours  \t [4]");
		buttons[4] = new JButton("Fives  \t [5]");
		buttons[5] = new JButton("Sixes  \t [6]");
		for (int i = 0; i < 6; i++)
		{
			grid[0].add(buttons[i]);
		}
		JLabel l2 = new JLabel("  TOTAL SCORE");
		l2.setFont(new Font("Helvetica", Font.PLAIN, 14));
		l2.setBorder(new MatteBorder(2, 2, 1, 2, Color.BLACK));
		grid[0].add(l2);
		JLabel l3 = new JLabel("  BONUS");
		l3.setFont(new Font("Helvetica", Font.PLAIN, 14));
		l3.setBorder(new MatteBorder(1, 2, 1, 2, Color.BLACK));
		grid[0].add(l3);
		JLabel l4 = new JLabel("  TOTAL");
		l4.setFont(new Font("Helvetica", Font.PLAIN, 14));
		l4.setBorder(new MatteBorder(1, 2, 2, 2, Color.BLACK));
		grid[0].add(l4);
		JLabel l5 = new JLabel("LOWER SECTION");
		l5.setFont(new Font("Helvetica", Font.BOLD, 14));
		grid[0].add(l5);
		buttons[6] = new JButton("3 of a Kind");
		buttons[7] = new JButton("4 of a Kind");
		buttons[8] = new JButton("Full House");
		buttons[9] = new JButton("Sm. Straight");
		buttons[10] = new JButton("Lg Straight");
		buttons[11] = new JButton("YAHTZEE");
		buttons[12] = new JButton("Chance");
		for (int i = 6; i < 13; i++)
			grid[0].add(buttons[i]);

		JLabel l6 = new JLabel("  Total Lower");
		l6.setFont(new Font("Helvetica", Font.PLAIN, 14));
		l6.setBorder(new MatteBorder(2, 2, 1, 2, Color.BLACK));
		grid[0].add(l6);
		JLabel l7 = new JLabel("  Total Upper");
		l7.setFont(new Font("Helvetica", Font.PLAIN, 14));
		l7.setBorder(new MatteBorder(1, 2, 1, 2, Color.BLACK));
		grid[0].add(l7);
		JLabel l8 = new JLabel("  GRAND TOTAL");
		l8.setFont(new Font("Helvetica", Font.BOLD, 14));
		l8.setBorder(new MatteBorder(1, 2, 2, 2, Color.BLACK));
		grid[0].add(l8);

		for (int i = 0; i < 7; i++)
			scorePanel.add(grid[i]);
		for (int i = 0; i < 13; i++)
			buttons[i].addActionListener(theListener);

	}

	public void enableButtons()
	{
		for (int i = 0; i < 6; i++)
		{
			buttons[i].setEnabled(scoreCard[round-1].num(i+1, dice));
		}
		buttons[6].setEnabled(scoreCard[round-1].numOfAKind(3, dice));
		buttons[7].setEnabled(scoreCard[round-1].numOfAKind(4, dice));
		buttons[8].setEnabled(scoreCard[round-1].fullHouse(dice));
		buttons[9].setEnabled(scoreCard[round-1].numStraight(4, dice));
		buttons[10].setEnabled(scoreCard[round-1].numStraight(5, dice));
		buttons[11].setEnabled(scoreCard[round-1].numOfAKind(5, dice));
		buttons[12].setEnabled(scoreCard[round-1].chance());

		for (int i = 0; i < 12; i++)
			if (scoreCard[round-1].getScore(i) > -1)
				buttons[i].setEnabled(false);

		if (scoreCard[round-1].numOfAKind(5, dice))
		{
			yahtzeeClip.stop();
			yahtzeeClip.setFramePosition(0);
			yahtzeeClip.start();
		}

		zero.setEnabled(true);
	}

	public void roll()
	{
		//Roll the dice
		timer.restart();
		rolling = true;

		if (rollNum < 3)
		{
			rollingAnimation();
			rollNum = (rollNum + 1) % 4;
		}
		if (rollNum == 3)
			roll.setEnabled(false);

		currRoll.setText("  Current Roll: " + rollNum);
	}

	public void rollingAnimation()
	{
		for (int i = 0; i < 5; i++)
			if (!dice[i].tf() || diceButtons[i].getText() == "?")
			{
				if (dice[i].tf())
					toggleDie(i);
				dice[i].roll();
				diceButtons[i].setText(Integer.toString(dice[i].value()));
			}
	}

	public void zero()
	{
		zeroing = true;
		for (int i = 0; i < 12; i++)
			if (scoreCard[round-1].getScore(i) == -1)
					buttons[i].setEnabled(true);
		roll.setEnabled(false);
		zero.setEnabled(false);
	}

	public void questionMarks()
	{
		for (int i = 0; i < 5; i++)
			diceButtons[i].setText("?");
	}

	public void toggleDie(int n)
	{
		if (!dice[n].tf())
			diceButtons[n].setFont(new Font("Helvetica", Font.BOLD, 65));
		else
			diceButtons[n].setFont(new Font("Helvetica", Font.PLAIN, 50));
		dice[n].toggletf();
	}

	public void score(int n, int score)
	{
		scoreCard[round-1].setScore(n, score);
		if (n > 5)
			n += 4;
		for (int i = 0; i < 5; i++)
			if (dice[i].tf())
				toggleDie(i);
		zeroing = false;
		if (n != 17)
			scored = true;
		tempScore = 0;

		// score the one they just did
		boxes[n+1][round].setText("  " + Integer.toString(score));

		//recalculate the totals display
		boxes[7][round].setText("  " +
				Integer.toString(scoreCard[round-1].upperScore()));
		boxes[9][round].setText("  " +
				Integer.toString(scoreCard[round-1].totalUpperScore()));
		boxes[19][round].setText("  " +
				Integer.toString(scoreCard[round-1].lowerScore()));
		boxes[20][round].setText("  " +
				Integer.toString(scoreCard[round-1].totalUpperScore()));
		boxes[21][round].setText("  " +
				Integer.toString(scoreCard[round-1].grandTotal()));
		if (n != 17)
			forceReRoll();
		scored = false;
		questionMarks();
	}

	public void forceReRoll()
	{
		for (int i = 0; i < 13; i++)
			buttons[i].setEnabled(false);
		zero.setEnabled(false);
		roll.setEnabled(true);
		rollNum = 0;
		currRoll.setText("  Current Roll: " + rollNum);
		scored = false;
		if (scoreCard[round-1].filled())
			forceNewGame();
	}

	public void forceNewGame()
	{
		for (int i = 0; i < 13; i++)
			buttons[i].setEnabled(false);
		zero.setEnabled(false);
		roll.setEnabled(false);
		if (scoreCard[round-1].grandTotal() > hiScore)
		{
			hiScore = scoreCard[round-1].grandTotal();
			JOptionPane.showMessageDialog(null,
			"Congratulations- you set a new high score: " + hiScore + "!",
			"New High Score!!", JOptionPane.INFORMATION_MESSAGE);
			try{
			ObjectOutputStream OS = new ObjectOutputStream(
					new FileOutputStream("highscore"));
			OS.writeInt(hiScore);
			OS.close();
			}
			catch (Exception f)
			{
				System.out.println(f + "\nException writing");
			}
			highScore.setText("High Score: " + hiScore);
		}

	}

	public int sum()
	{
		return (dice[0].value() + dice[1].value() + dice[2].value()
					+ dice[3].value() + dice[4].value());
	}

	private class InputHandler extends KeyAdapter
	{
	    public void keyPressed(KeyEvent e)
		{
			if (rolling)
			{
				timer.stop();
				rolling = false;
				enableButtons();
			}
			else {
	        if (e.getKeyCode() == KeyEvent.VK_R && roll.isEnabled())
		        roll();
		    if (e.getKeyCode() == KeyEvent.VK_A && diceButtons[0].isEnabled())
		        toggleDie(0);
		    if (e.getKeyCode() == KeyEvent.VK_S && diceButtons[1].isEnabled())
		        toggleDie(1);
		    if (e.getKeyCode() == KeyEvent.VK_D && diceButtons[2].isEnabled())
		        toggleDie(2);
		    if (e.getKeyCode() == KeyEvent.VK_F && diceButtons[3].isEnabled())
		        toggleDie(3);
		    if (e.getKeyCode() == KeyEvent.VK_G && diceButtons[4].isEnabled())
		        toggleDie(4);
			if (e.getKeyCode() == KeyEvent.VK_Z && zero.isEnabled())
				zero();
			if (e.getKeyCode() == KeyEvent.VK_N && newGame.isEnabled())
			{
				if (JOptionPane.showConfirmDialog(null, "Are you sure you"
				 	 + " want to start a new game?",	"New Game",
						JOptionPane.YES_NO_OPTION) == 0)
				{
					if (round < 6)
					{
						round++;
						questionMarks();
						forceReRoll();
					}
					else
						new Yahtzee();
				}
			}
			}

	    }
	}


	private class SliderChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent e)
		{
            String adjust = "";
            if (slider.getValueIsAdjusting())
            	currVal.setText("Current value: " + slider.getValue());
			delay = slider.getValue();
			timer.setDelay(delay);
        }
    };

	private class MyListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			gamePanel.grabFocus();
			if (rolling)
			{
				if (e.getSource() == timer)
					rollingAnimation();
				if (e.getSource()  != timer)
				{
					timer.stop();
					rolling = false;
					enableButtons();
				}
			}
			else
			if (zeroing)
			{
				for (int i = 0; i < 12; i++)
					if (e.getSource() == buttons[i])
					{
						score(i, 0);
						zeroing = false;
						scored = true;
						roll.setEnabled(true);
						forceReRoll();
					}
			}
			else if (!rolling)
			{
				for (int i = 0; i < 5; i++)
					if (e.getSource() == diceButtons[i])
						toggleDie(i);
				if (e.getSource() == newGame)
				{
					if (JOptionPane.showConfirmDialog(null, "Are you sure you"
					 	 + " want to start a new game?",	"New Game",
							JOptionPane.YES_NO_OPTION) == 0)
					{
						if (round < 6)
						{
							round++;
							questionMarks();
							forceReRoll();
						}
						else
							new Yahtzee();
					}
				}

				if (e.getSource() == roll)
					roll();
				if (e.getSource() == zero)
					zero();
				if (e.getSource() == quit)
					System.exit(0);
				for (int i = 0; i < 6; i++)
					if (e.getSource() == buttons[i])
					{
						for (int j = 0; j < 5; j++)
							if (dice[j].value() == i+1)
								tempScore += i+1;
						score(i, tempScore);
					}
				if (e.getSource() == buttons[6])
					score(6, sum());
				if (e.getSource() == buttons[7])
					score(7, sum());
				if (e.getSource() == buttons[8])
					score(8, 25);
				if (e.getSource() == buttons[9])
					score(9, 30);
				if (e.getSource() == buttons[10])
					score(10, 40);
				if (e.getSource() == buttons[11])
					score(11, 50);
				if (e.getSource() == buttons[12])
					score(12, sum());
				if (e.getSource() == buttons[13])
					score(13, 100);
				if (e.getSource() == close)
					preferences.setVisible(false);

			}
		}
	}
}
