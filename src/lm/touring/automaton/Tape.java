package lm.touring.automaton;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Tape {
	public static final String EMPTY_SYMBOL = "~";
	public static final int EMPTY_BUFFOR_SIZE = 10;
	private List<String> tape;
	private int headIndex = EMPTY_BUFFOR_SIZE;
	
	public Tape() {
		String[] emptyValues = new String[EMPTY_BUFFOR_SIZE * 2 + 1];
		Arrays.fill(emptyValues, EMPTY_SYMBOL);
		tape = new LinkedList<>(Arrays.asList(emptyValues));
	}
	
	public void moveLeft() {
		if (headIndex <= EMPTY_BUFFOR_SIZE) {
			tape.add(0, EMPTY_SYMBOL);
		} else {
			--headIndex;
		}
	}
	
	public void moveRight() {
		++headIndex;
		if (headIndex >= tape.size() - EMPTY_BUFFOR_SIZE) {
			tape.add(EMPTY_SYMBOL);
		}
	}
	
	public String read() {
		return tape.get(headIndex);
	}
	
	public void write(String symbolToWrite) {
		tape.set(headIndex, symbolToWrite);
	}
	
	@Override
	public String toString() {
		String value = "|";
		
		for (int i = 0; i < tape.size(); i++) {
			if (i == headIndex)
				value += "|";
			value += tape.get(i) + "|";
			if (i == headIndex)
				value += "|";
		}
		
		return value;
	}
	
	
	public List<String> getSymbolList() {
		return tape;
	}
	
	public int getCurrentHeadPosition() {
		return headIndex;
	}
}
