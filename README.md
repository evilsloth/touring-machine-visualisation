# touring-machine-visualisation
Java Swing visualisation of touring machine.
This example adds 1 to binary number.

# Usage
Press 0 or 1 to set binary number or Backspace to clear symbol. Press left/right arrow to move the tape.
Press Next step button to advance to next state. Play button makes step every 2 seconds.

![touring](https://user-images.githubusercontent.com/6104069/32747772-1c1efbc6-c8ba-11e7-976b-85355b714a37.png)

# Custom alghoritms
Go to GUI.java and construct your own transition table.
```
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
```

Each row in transition table corresponds to states and each column corresponds to given alphabet symbols.

An entry in transition table (e.g. "q1,0,L") means "transition_state,symbol_to_write,tape_move".

tape_move can be L - left, R - right or "-" - no action.

Providing "-" to symbol_to_write means that nothing will be changed on the tape.
