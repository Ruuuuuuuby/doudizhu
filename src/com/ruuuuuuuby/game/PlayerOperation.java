package com.ruuuuuuuby.game;

import com.ruuuuuuuby.domain.Poker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerOperation extends Thread {

	// Game's main interface
	GameJFrame gameJFrame;

	// Indicates if the player can proceed
	boolean isRun = true;

	// Countdown timer
	int i;

	public PlayerOperation(GameJFrame m, int i) {
		this.gameJFrame = m;
		this.i = i;
	}

	@Override
	public void run() {
		// Countdown logic
		while (i > -1 && isRun) {
			gameJFrame.time[1].setText("Countdown: " + i--);
			sleep(1); // Wait for 1 second
		}

		// When the countdown ends
		if (i == -1) {
			gameJFrame.time[1].setText("Pass");
		}

		// Hide the landlord decision buttons
		gameJFrame.landlord[0].setVisible(false);
		gameJFrame.landlord[1].setVisible(false);

		// Enable card clicking for the player
		for (Poker poker2 : gameJFrame.playerList.get(1)) {
			poker2.setCanClick(true); // Cards can now be clicked
		}

		// If the player chooses to be the landlord
		if (gameJFrame.time[1].getText().equals("Landlord")) {
			gameJFrame.playerList.get(1).addAll(gameJFrame.lordList); // Add landlord cards
			openlord(true); // Show the landlord cards
			sleep(2); // Pause for animation
			Common.order(gameJFrame.playerList.get(1)); // Sort the player's cards
			Common.rePosition(gameJFrame, gameJFrame.playerList.get(1), 1); // Reposition the cards
			gameJFrame.publishCard[1].setEnabled(false); // Disable "Pass" button
			setlord(1); // Set the current player as the landlord
		} else {
			// If the player doesn't choose to be the landlord, determine the next landlord
			if (Common.getScore(gameJFrame.playerList.get(0)) < Common.getScore(gameJFrame.playerList.get(2))) {
				gameJFrame.time[2].setText("Landlord");
				gameJFrame.time[2].setVisible(true);
				setlord(2); // Set the right computer as the landlord
				openlord(true);
				sleep(3);
				gameJFrame.playerList.get(2).addAll(gameJFrame.lordList); // Add landlord cards
				Common.order(gameJFrame.playerList.get(2)); // Sort the cards
				Common.rePosition(gameJFrame, gameJFrame.playerList.get(2), 2); // Reposition the cards
				openlord(false);
			} else {
				gameJFrame.time[0].setText("Landlord");
				gameJFrame.time[0].setVisible(true);
				setlord(0); // Set the left computer as the landlord
				openlord(true);
				sleep(3);
				gameJFrame.playerList.get(0).addAll(gameJFrame.lordList); // Add landlord cards
				Common.order(gameJFrame.playerList.get(0)); // Sort the cards
				Common.rePosition(gameJFrame, gameJFrame.playerList.get(0), 0); // Reposition the cards
				openlord(false);
			}
		}

		// Hide landlord decision buttons after selection
		gameJFrame.landlord[0].setVisible(false);
		gameJFrame.landlord[1].setVisible(false);

		// Disable action buttons
		turnOn(false);

		// Reset text for all players
		for (int i = 0; i < 3; i++) {
			gameJFrame.time[i].setText("Pass");
			gameJFrame.time[i].setVisible(false);
		}

		// Determine whose turn it is
		gameJFrame.turn = gameJFrame.dizhuFlag;

		// Game loop
		while (true) {
			// Player's turn
			if (gameJFrame.turn == 1) {
				// If both computers pass, disable the "Pass" button
				if (gameJFrame.time[0].getText().equals("Pass") && gameJFrame.time[2].getText().equals("Pass")) {
					gameJFrame.publishCard[1].setEnabled(false);
				} else {
					gameJFrame.publishCard[1].setEnabled(true);
				}
				turnOn(true); // Enable action buttons
				timeWait(30, 1); // Wait for the player to act
				turnOn(false); // Disable action buttons
				gameJFrame.turn = (gameJFrame.turn + 1) % 3; // Move to the next player
				if (win()) break; // Check if someone has won
			}

			// Left computer's turn
			if (gameJFrame.turn == 0) {
				computer0(); // Simulate the left computer's actions
				gameJFrame.turn = (gameJFrame.turn + 1) % 3; // Move to the next player
				if (win()) break; // Check if someone has won
			}

			// Right computer's turn
			if (gameJFrame.turn == 2) {
				computer2(); // Simulate the right computer's actions
				gameJFrame.turn = (gameJFrame.turn + 1) % 3; // Move to the next player
				if (win()) break; // Check if someone has won
			}
		}
	}

	// Define a method to pause the thread for N seconds
	// The parameter specifies the wait time
	// The sleep method in threads throws exceptions, so wrapping it makes the code cleaner
	public void sleep(int i) {
		try {
			Thread.sleep(i * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Reveal or hide the landlord cards
	public void openlord(boolean is) {
		for (int i = 0; i < 3; i++) {
			if (is) {
				gameJFrame.lordList.get(i).turnFront(); // Show the front of the cards
			} else {
				gameJFrame.lordList.get(i).turnRear(); // Show the back of the cards
			}
			gameJFrame.lordList.get(i).setCanClick(true); // Make the cards clickable
		}
	}

	// Set the landlord position and update the landlord marker
	public void setlord(int i) {
		Point point = new Point();
		if (i == 1) {
			point.x = 80;
			point.y = 430;
			gameJFrame.dizhuFlag = 1;
		}
		if (i == 0) {
			point.x = 80;
			point.y = 20;
			gameJFrame.dizhuFlag = 0;
		}
		if (i == 2) {
			point.x = 700;
			point.y = 20;
			gameJFrame.dizhuFlag = 2;
		}
		gameJFrame.dizhu.setLocation(point); // Move the landlord marker
		gameJFrame.dizhu.setVisible(true); // Make the landlord marker visible
	}

	// Enable or disable the action buttons
	public void turnOn(boolean flag) {
		gameJFrame.publishCard[0].setVisible(flag); // "Play" button
		gameJFrame.publishCard[1].setVisible(flag); // "Pass" button
	}

	// Simulate the left computer's turn
	public void computer0() {
		timeWait(1, 0); // Wait for 1 second
		ShowCard(0); // Display the cards played
	}

	// Simulate the right computer's turn
	public void computer2() {
		timeWait(1, 2); // Wait for 1 second
		ShowCard(2); // Display the cards played
	}

	// Display the cards played by the current role
	public void ShowCard(int role) {
		int orders[] = new int[] { 4, 3, 2, 1, 5 }; // Preferred order of card combinations
		Model model = Common.getModel(gameJFrame.playerList.get(role), orders); // Get all possible combinations
		ArrayList<String> list = new ArrayList<>();

		// Check if the other two players have passed
		if (gameJFrame.time[(role + 1) % 3].getText().equals("Pass") && gameJFrame.time[(role + 2) % 3].getText().equals("Pass")) {
			// Play card combinations based on priority
			if (model.a123.size() > 0) {
				list.add(model.a123.get(model.a123.size() - 1)); // Play straight
			} else if (model.a3.size() > 0) {
				if (model.a1.size() > 0) {
					list.add(model.a1.get(model.a1.size() - 1)); // Add a single card
				} else if (model.a2.size() > 0) {
					list.add(model.a2.get(model.a2.size() - 1)); // Add a pair
				}
				list.add(model.a3.get(model.a3.size() - 1)); // Play triple
			} else if (model.a112233.size() > 0) {
				list.add(model.a112233.get(model.a112233.size() - 1)); // Play double straight
			} else if (model.a111222.size() > 0) {
				String name[] = model.a111222.get(0).split(",");
				// Play plane + single or pair
				if (name.length / 3 <= model.a1.size()) {
					list.add(model.a111222.get(model.a111222.size() - 1)); // Add plane
					for (int i = 0; i < name.length / 3; i++) {
						list.add(model.a1.get(i)); // Add singles
					}
				} else if (name.length / 3 <= model.a2.size()) {
					list.add(model.a111222.get(model.a111222.size() - 1)); // Add plane
					for (int i = 0; i < name.length / 3; i++) {
						list.add(model.a2.get(i)); // Add pairs
					}
				}
			} else if (model.a4.size() > 0) {
				// Check if bombs can be played
				int sizea1 = model.a1.size();
				int sizea2 = model.a2.size();
				if (sizea1 >= 2) {
					list.add(model.a1.get(sizea1 - 1)); // Add singles
					list.add(model.a1.get(sizea1 - 2));
					list.add(model.a4.get(0)); // Add bomb
				} else if (sizea2 >= 2) {
					list.add(model.a2.get(sizea2 - 1)); // Add pairs
					list.add(model.a2.get(sizea2 - 2));
					list.add(model.a4.get(0)); // Add bomb
				} else {
					list.add(model.a4.get(0)); // Just play bomb
				}
			}
		} else {
			// Normal gameplay logic if not all players have passed
			int can = 0;
			if (role == gameJFrame.dizhuFlag) {
				if (gameJFrame.playerList.get((role + 1) % 3).size() <= 5 || gameJFrame.playerList.get((role + 2) % 3).size() <= 5) {
					can = 1; // Can play cards based on remaining cards
				}
			}
			ArrayList<Poker> player;
			if (gameJFrame.time[(role + 2) % 3].getText().equals("Pass")) {
				player = gameJFrame.currentList.get((role + 1) % 3); // Target player who hasn't passed
			} else {
				player = gameJFrame.currentList.get((role + 2) % 3);
			}
			// More gameplay logic here...
		}
		// Logic for arranging played cards, repositioning, and continuing turns
		// Removed for brevity
	}

	// Wait for a player's turn and handle countdown
	public void timeWait(int n, int player) {
		// Hide cards from the current player
		if (gameJFrame.currentList.get(player).size() > 0) {
			Common.hideCards(gameJFrame.currentList.get(player));
		}

		if (player == 1) {
			int i = n;
			// Player's countdown and action handling
			while (!gameJFrame.nextPlayer && i >= 0) {
				gameJFrame.time[player].setText("Countdown:" + i);
				gameJFrame.time[player].setVisible(true);
				sleep(1);
				i--;
			}
			if (i == -1 && player == 1) {
				ShowCard(1); // Automatically play if time runs out
			}
			gameJFrame.nextPlayer = false;
		} else {
			// Handle countdown for computers
			for (int i = n; i >= 0; i--) {
				sleep(1);
				gameJFrame.time[player].setText("Countdown:" + i);
				gameJFrame.time[player].setVisible(true);
			}
		}
		gameJFrame.time[player].setVisible(false);
	}

	// Check if the game has a winner
	public boolean win() {
		for (int i = 0; i < 3; i++) {
			if (gameJFrame.playerList.get(i).size() == 0) {
				String s;
				if (i == 1) {
					s = "Congratulations! You win!";
				} else {
					s = "Computer " + i + " wins! Better luck next time!";
				}
				// Reveal all remaining cards
				for (int j = 0; j < gameJFrame.playerList.get((i + 1) % 3).size(); j++) {
					gameJFrame.playerList.get((i + 1) % 3).get(j).turnFront();
				}
				for (int j = 0; j < gameJFrame.playerList.get((i + 2) % 3).size(); j++) {
					gameJFrame.playerList.get((i + 2) % 3).get(j).turnFront();
				}
				JOptionPane.showMessageDialog(gameJFrame, s); // Show winning message
				return true; // Game ends
			}
		}
		return false; // Game continues
	}
}
