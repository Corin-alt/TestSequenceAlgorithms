package fr.corentin;

import java.util.HashMap;
import java.util.Map;

public class Graph {
    private final Map<Integer, State> states;

    public Graph() {
        this.states = new HashMap<>();
    }

    public void addState(int stateId) {
        states.putIfAbsent(stateId, new State(stateId));
    }

    public void addTransition(int fromState, int toState, char input, char output) {
        addState(fromState);
        addState(toState);
        Transition transition = new Transition(fromState, toState, input, output);
        states.get(fromState).addTransition(transition);
    }

    public Map<Integer, State> getStates() {
        return states;
    }
}