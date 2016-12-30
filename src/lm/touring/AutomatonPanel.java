package lm.touring;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import lm.touring.automaton.Automaton;
import lm.touring.automaton.Tape;

public class AutomatonPanel extends JPanel {
	private static final long serialVersionUID = 5556441524115950064L;
	private Automaton automaton;
	private Tape tape;
	private static final int WIDTH = 1100;
	private static final int HEIGHT = 500;
	private static final int BOX_SIZE = 60;
	private static final int BOX_UPPER_BORDER = HEIGHT /2 - BOX_SIZE / 2;
	private static final int MIDDLE_BOX_LEFT_BORDER = WIDTH /2 - BOX_SIZE / 2;
	private float lastX = MIDDLE_BOX_LEFT_BORDER;
	private int lastHeadIndex = 10;
	private int lastTapeLength;
	
	public AutomatonPanel(Automaton automaton) {
		this.automaton = automaton;
		this.tape = automaton.getTape();
		
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "leftAction");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "rightAction");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("BACK_SPACE"), "emptyAction");
		
		for (String symbol : automaton.getAlphabet()) {
			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(symbol), symbol + "Action");
		}
		registerKeyActions();
		
		Timer timer = new Timer(17, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();
			}
		});
		timer.start();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1100, 500);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
		
		for (int i = 0; i < tape.getSymbolList().size(); i++) {
			String symbol = tape.getSymbolList().get(i);
			int middleIndex = tape.getCurrentHeadPosition();
			
			if (lastHeadIndex < tape.getCurrentHeadPosition()) {
				lastX = MIDDLE_BOX_LEFT_BORDER + 60;
			} else if (lastHeadIndex > tape.getCurrentHeadPosition() || lastTapeLength != tape.getSymbolList().size()) {
				lastX = MIDDLE_BOX_LEFT_BORDER - 60;
			}
			lastHeadIndex = tape.getCurrentHeadPosition();
			lastTapeLength = tape.getSymbolList().size();
		
			if (lastX > MIDDLE_BOX_LEFT_BORDER + 0.1) {
				lastX -= 0.02;
			} else if (lastX < MIDDLE_BOX_LEFT_BORDER - 0.1) {
				lastX += 0.02;
			}
			
			int x = (int)lastX - (middleIndex - i) * BOX_SIZE;
			Color boxColor = Color.WHITE;
			Color textColor = Color.BLACK;
			
			if (i == middleIndex) {
				boxColor = Color.ORANGE;
				textColor = Color.RED;
			}
			
			drawBox(g, x, symbol, boxColor, textColor);
		}
	}
	
	private void drawBox(Graphics g, int x, String symbol, Color boxColor, Color textColor) {
		g.setColor(boxColor);
		g.fillRect(x, BOX_UPPER_BORDER, BOX_SIZE, BOX_SIZE);
		g.setColor(Color.BLACK);
		g.drawRect(x, BOX_UPPER_BORDER, BOX_SIZE, BOX_SIZE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int textX = x + BOX_SIZE / 2 - metrics.stringWidth(symbol) / 2;
		int textY = BOX_UPPER_BORDER + BOX_SIZE / 2 - metrics.getHeight() / 2 + metrics.getAscent();
		g.setColor(textColor);
		g.drawString(symbol, textX, textY);
		
		repaint();
	}
	
	private void moveTapeLeft() {
		tape.moveLeft();
	}
	
	private void moveTapeRight() {
		tape.moveRight();
	}
	
	private void writeToTape(String symbol) {
		tape.write(symbol);
	}
	
	private void registerKeyActions() {
		getActionMap().put("leftAction", new AbstractAction() {	
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				moveTapeLeft();
			}
		});
		
		getActionMap().put("rightAction", new AbstractAction() {	
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				moveTapeRight();
			}
		});
		
		getActionMap().put("emptyAction", new AbstractAction() {	
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				writeToTape("~");
			}
		});
		
		for (String symbol : automaton.getAlphabet()) {
			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(symbol), symbol + "Action");
			getActionMap().put(symbol + "Action", new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					writeToTape(symbol);
				}
			});
		}
	}
}
