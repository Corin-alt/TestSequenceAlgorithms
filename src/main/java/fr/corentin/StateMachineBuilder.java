package fr.corentin;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builder pour construire la machine à etats de manière fluide.
 * Permet de creer facilement les transitions avec une syntaxe chainee.
 */
public class StateMachineBuilder {
    private final Map<Integer, Map<String, Transition>> transitions;
    private int currentState;

    public StateMachineBuilder() {
        this.transitions = new LinkedHashMap<>();
    }

    /**
     * Commence la definition des transitions pour un etat.
     */
    public StateMachineBuilder state(int state) {
        currentState = state;
        transitions.putIfAbsent(state, new LinkedHashMap<>());
        return this;
    }

    /**
     * Ajoute une transition pour l'etat courant.
     */
    public StateMachineBuilder onInput(String input, int toState, String output) {
        transitions.get(currentState).put(input, new Transition(toState, output));
        return this;
    }

    /**
     * Construit la machine à etats finale.
     */
    public Map<Integer, Map<String, Transition>> build() {
        return transitions;
    }
}