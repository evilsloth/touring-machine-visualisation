package lm.touring;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lm.touring.automaton.Automaton;

public class GUI extends JFrame {
	private static final long serialVersionUID = -8900010760721989010L;
	private JButton nextStepButton = new JButton("Next step");
	private JButton playButton = new JButton("Play");
	private JLabel currentStateLabel = new JLabel();
	private JPanel canvasPanel;
	private Automaton automaton;
	private boolean isPlaying = false;

	public GUI() {
		automaton = new Automaton.AutomatonBuilder()
				.transitionTable(new String[][] {
					{"q1,-,L", "q1,-,L", "q1,-,L"},
					{"q2,1,R", "q1,0,L", "q2,1,R"},
					{"q2,-,R", "q2,-,R", "q3,-,L"},
					{"q4,1,-", "q3,0,L", "q4,1,-"},
					{"q4,-,-", "q4,-,-", "q4,-,-"}
				})
				.states("q0", "q1", "q2", "q3", "q4")
				.alphabet("0", "1", "~")
				.finalStates("q4")
				.build();
		
		initUI();
	}

	private void initUI() {
		setTitle("Touring");
		setSize(1100, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout());
		getContentPane().add(mainPanel);

		JPanel panelButtons = new JPanel();
		panelButtons.add(nextStepButton);
		panelButtons.add(playButton);
		
		mainPanel.add(panelButtons, BorderLayout.NORTH);
		
		canvasPanel = new AutomatonPanel(automaton);
		mainPanel.add(canvasPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(currentStateLabel);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		
		nextStepButton.addActionListener((ActionEvent e) -> {
			automaton.transition();
			updateStateLabel();
		});
		
		playButton.addActionListener((ActionEvent e) -> {
			if (isPlaying) {
				isPlaying = false;
				playButton.setText("Play");
				nextStepButton.setEnabled(true);
			} else {
				isPlaying = true;
				playButton.setText("Pause");
				nextStepButton.setEnabled(false);
			}
		});
		
		initAutoplayLoop();
		updateStateLabel();
	}
	
	private void initAutoplayLoop() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (isPlaying) {
						automaton.transition();
						updateStateLabel();
					}
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	private void updateStateLabel() {
		currentStateLabel.setText(automaton.getStateHistory().toString() + " => " + automaton.getCurrentState());
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			GUI gui = new GUI();
			gui.setVisible(true);
		});
	}
}
