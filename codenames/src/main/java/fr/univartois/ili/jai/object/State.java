package fr.univartois.ili.jai.object;

public enum State {
	START,
	END,
	BLUESPY,
	BLUEDECODER,
	REDSPY,
	REDDECODER;

	public State getNext() {
		int index = (ordinal() + 1) % values().length > 1 ? ordinal() : 1;
		return values()[(index + 1) % values().length];
	}
}
