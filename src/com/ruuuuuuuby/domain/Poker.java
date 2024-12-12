package com.ruuuuuuuby.domain;

import com.ruuuuuuuby.game.GameJFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Poker extends JLabel implements MouseListener {

	// The main game interface
	GameJFrame gameJFrame;
	// The name of the card
	String name;
	// Whether the card shows its front or back
	boolean up;
	// Whether the card is clickable
	boolean canClick = false;
	// Current state, whether the card is clicked
	boolean clicked = false;

	public Poker(GameJFrame m, String name, boolean up) {
		this.gameJFrame = m;
		this.name = name;
		this.up = up;
		// Determine whether the card is showing its front or back
		if (this.up) {
			this.turnFront();
		} else {
			this.turnRear();
		}
		// Set the card's width and height
		this.setSize(71, 96);
		// Display the card
		this.setVisible(true);
		// Add a mouse listener to each card
		this.addMouseListener(this);
	}



	public Poker(GameJFrame gameJFrame, String name, boolean up, boolean canClick, boolean clicked) {
		this.gameJFrame = gameJFrame;
		this.name = name;
		this.up = up;
		this.canClick = canClick;
		this.clicked = clicked;
	}

	// Display the front of the card
	public void turnFront() {
		this.setIcon(new ImageIcon("image/poker/" + name + ".png"));
		this.up = true;
	}

	// Display the back of the card
	public void turnRear() {
		this.setIcon(new ImageIcon("image/poker/rear.png"));
		this.up = false;
	}

	// When playing a card, clicking the card:
	// - Moves the card up by 20 pixels on the first click
	// - Moves the card down by 20 pixels on the second click
	@Override
	public void mouseClicked(MouseEvent e) {
		if (canClick) {
			Point from = this.getLocation();
			int step;
			if (clicked) {
				step = 20;
			} else {
				step = -20;
			}
			clicked = !clicked;
			Point to = new Point(from.x, from.y + step);
			this.setLocation(to);
		}
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Get the game interface
	 * @return gameJFrame
	 */
	public GameJFrame getGameJFrame() {
		return gameJFrame;
	}

	/**
	 * Set the game interface
	 * @param gameJFrame
	 */
	public void setGameJFrame(GameJFrame gameJFrame) {
		this.gameJFrame = gameJFrame;
	}

	/**
	 * Get the name of the card
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the card
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Check if the card is showing its front
	 * @return up
	 */
	public boolean isUp() {
		return up;
	}

	/**
	 * Set whether the card shows its front
	 * @param up
	 */
	public void setUp(boolean up) {
		this.up = up;
	}

	/**
	 * Check if the card is clickable
	 * @return canClick
	 */
	public boolean isCanClick() {
		return canClick;
	}

	/**
	 * Set whether the card is clickable
	 * @param canClick
	 */
	public void setCanClick(boolean canClick) {
		this.canClick = canClick;
	}

	/**
	 * Check if the card is clicked
	 * @return clicked
	 */
	public boolean isClicked() {
		return clicked;
	}

	/**
	 * Set whether the card is clicked
	 * @param clicked
	 */
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}

	public String toString() {
		return "Poker{gameJFrame = " + gameJFrame + ", name = " + name + ", up = " + up + ", canClick = " + canClick + ", clicked = " + clicked + "}";
	}
}