package fr.corentin;

public class Transition {
    private final int fromState;
    private final int toState;
    private final char input;
    private final char output;

    public Transition(int fromState, int toState, char input, char output) {
        this.fromState = fromState;
        this.toState = toState;
        this.input = input;
        this.output = output;
    }

    public int getFromState() { return fromState; }
    public int getToState() { return toState; }
    public char getInput() { return input; }
    public char getOutput() { return output; }
}