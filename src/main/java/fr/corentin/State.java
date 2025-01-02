package fr.corentin;

import java.util.ArrayList;
import java.util.List;

public class State {
    private final int id;
    private final List<Transition> transitions;

    public State(int stateId) {
        this.id = stateId;
        this.transitions = new ArrayList<>();
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);
    }

    public int getId() {
        return id;
    }
}