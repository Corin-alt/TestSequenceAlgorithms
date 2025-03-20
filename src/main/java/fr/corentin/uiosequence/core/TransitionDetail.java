package fr.corentin.uiosequence.core;

/**
 * Class that captures all the details of a performed transition.
 * Unlike the Transition class that defines a possible transition,
 * TransitionDetail represents a transition that has actually been used,
 * with its starting state included.
 * <p>
 * This class is used to:
 * - Trace the execution of a sequence
 * - Debug the behavior of the machine
 * - Visualize the path taken
 *
 * @param fromState The state from which the transition was performed.
 *                  This is the state the machine was in before the transition.
 * @param toState   The state to which the machine moved.
 *                  This is the final state after the transition.
 * @param input     The character that triggered the transition.
 *                  This is the input that was provided to the machine.
 * @param output    The character produced by the transition.
 *                  This is what the machine emitted during the transition.
 */
public record TransitionDetail(int fromState, int toState, String input, String output) {
    /**
     * Constructor that records all the details of a transition.
     * Example of use:
     * new TransitionDetail(1, 2, "a", "x") means:
     * - The machine was in state 1
     * - It received the character "a"
     * - It moved to state 2
     * - It produced the character "x"
     *
     * @param fromState Starting state
     * @param toState   Destination state
     * @param input     Character received
     * @param output    Character produced
     */
    public TransitionDetail {
    }

    /**
     * @return The state from which the transition was performed
     */
    @Override
    public int fromState() {
        return fromState;
    }

    /**
     * @return The state to which the machine moved
     */
    @Override
    public int toState() {
        return toState;
    }

    /**
     * @return The character that triggered the transition
     */
    @Override
    public String input() {
        return input;
    }

    /**
     * @return The character produced during the transition
     */
    @Override
    public String output() {
        return output;
    }

    /**
     * Detailed textual representation of the transition.
     * Format: "State X -> State Y on input A, output: B"
     */
    @Override
    public String toString() {
        return String.format("State %d -> State %d on input %s, output: %s",
                fromState, toState, input, output);
    }
}