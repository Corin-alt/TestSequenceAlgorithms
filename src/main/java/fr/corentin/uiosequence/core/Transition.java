package fr.corentin.uiosequence.core;

/**
 * Class representing a transition in the state machine.
 * A transition defines:
 * - The destination state when an input is received
 * - The output character produced
 * <p>
 * For example:
 * If in state 1, on input 'a', we have:
 * - toState = 2
 * - output = 'x'
 * This means that the machine:
 * - Moves to state 2
 * - Produces the character 'x'
 *
 * @param toState The state to which the transition leads.
 *                This is the state in which the machine is after the transition.
 * @param output  The character produced by this transition.
 *                This is what the machine "emits" when this transition is used.
 */
public record Transition(int toState, String output) {
    /**
     * Constructor that initializes a new transition.
     * The fields are final because a transition should not
     * change once created.
     *
     * @param toState The destination state (where the machine goes)
     * @param output  The character produced by the transition
     */
    public Transition {
    }

    /**
     * @return The destination state of the transition
     */
    @Override
    public int toState() {
        return toState;
    }

    /**
     * @return The character produced by the transition
     */
    @Override
    public String output() {
        return output;
    }

    /**
     * Textual representation of the transition.
     * Format: "to state X, output: Y"
     */
    @Override
    public String toString() {
        return String.format("to state %d, output: %s", toState, output);
    }
}