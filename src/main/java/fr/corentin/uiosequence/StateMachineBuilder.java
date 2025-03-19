package fr.corentin.uiosequence;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builder for constructing state machines in a fluent manner.
 * Allows easy creation of transitions with a chained syntax.
 *
 * @author Corentin
 * @version 1.0
 * @see StateMachine
 * @see Transition
 */
public class StateMachineBuilder {
    private final Map<Integer, Map<String, Transition>> transitions;
    private int currentState;

    /**
     * Creates a new state machine builder with an empty transition map.
     */
    public StateMachineBuilder() {
        this.transitions = new LinkedHashMap<>();
    }

    /**
     * Begins defining transitions for a specific state.
     *
     * @param state The state to define transitions for
     * @return This builder for method chaining
     */
    public StateMachineBuilder state(int state) {
        currentState = state;
        transitions.putIfAbsent(state, new LinkedHashMap<>());
        return this;
    }

    /**
     * Adds a transition for the current state.
     *
     * @param input The input character triggering the transition
     * @param toState The destination state
     * @param output The output character produced
     * @return This builder for method chaining
     */
    public StateMachineBuilder onInput(String input, int toState, String output) {
        transitions.get(currentState).put(input, new Transition(toState, output));
        return this;
    }

    /**
     * Builds the final state machine.
     *
     * @return A map representing the complete state machine transitions
     */
    public Map<Integer, Map<String, Transition>> build() {
        return transitions;
    }
}