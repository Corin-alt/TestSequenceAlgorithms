package fr.corentin.uiosequence;

import java.util.Map;
import java.util.Set;

/**
 * Class representing an input/output sequence in the state machine.
 * A sequence is composed of:
 * - An input string (what is sent to the machine)
 * - An output string (what the machine produces)
 * - The set of transitions used
 * <p>
 * This class is used to:
 * - Store sequences found during exploration
 * - Keep track of used transitions
 * - Compare and select the best sequences
 *
 * @param input           The string of characters sent to the machine.
 *                        Example: "abc" means we send 'a', then 'b', then 'c'
 * @param output          The string of characters produced by the machine.
 *                        Example: "xyz" means the machine produced 'x', then 'y', then 'z'
 * @param usedTransitions The set of transitions used for this sequence.
 *                        Each transition is a pair (state, input character).
 *                        Example: [(1,'a'), (2,'b')] means:
 *                        - From state 1, input 'a' was used
 *                        - From state 2, input 'b' was used
 */
public record Sequence(String input, String output, Set<Map.Entry<Integer, String>> usedTransitions) {
    /**
     * Constructor that initializes a new sequence.
     * All fields are final because a sequence should not be modified
     * once created.
     *
     * @param input           The input string to send to the machine
     * @param output          The output string produced by the machine
     * @param usedTransitions The set of transitions used
     */
    public Sequence {
    }

    /**
     * @return The input string of the sequence
     */
    @Override
    public String input() {
        return input;
    }

    /**
     * @return The output string of the sequence
     */
    @Override
    public String output() {
        return output;
    }

    /**
     * @return The set of transitions used by this sequence
     */
    @Override
    public Set<Map.Entry<Integer, String>> usedTransitions() {
        return usedTransitions;
    }
}