package fr.corentin.uiosequence;

import java.util.*;

/**
 * Main class that manages a finite state machine.
 * <p>
 * This class allows:
 * - Executing input sequences and obtaining corresponding outputs
 * - Finding unique sequences that identify each state
 * - Analyzing and verifying used transitions
 * - Exploring all possible sequences up to a given length
 * </p>
 * <p>
 * The state machine is represented by a Map that associates:
 * - Each state (Integer)
 * - With a Map of possible transitions from that state
 *   - Each transition is indexed by its input character (String)
 *   - And contains the destination state and output character (Transition)
 * </p>
 *
 * @author Corentin
 * @version 1.0
 * @see Transition
 * @see Sequence
 */
public class StateMachine {
    /**
     * Maximum length of sequences to explore.
     * A value of 4 means sequences no longer than 3 characters will be searched.
     * This limit prevents combinatorial explosion in sequence exploration.
     */
    private static final int MAX_SEQUENCE_LENGTH = 4;

    /**
     * Executes an input sequence in the state machine.
     * <p>
     * For each input character:
     * 1. Checks if the transition is possible from the current state
     * 2. If yes, adds the output character to the result and transitions to the next state
     * 3. If no, returns null because the sequence is impossible
     * </p>
     *
     * @param state Initial starting state
     * @param inputSeq The sequence of input characters to execute
     * @param transitions The complete map of machine transitions
     * @return The output sequence produced, or null if impossible
     */
    public static String executeSequence(int state, String inputSeq,
                                         Map<Integer, Map<String, Transition>> transitions) {
        int currentState = state;
        StringBuilder outputSeq = new StringBuilder();

        for (char inputChar : inputSeq.toCharArray()) {
            String input = String.valueOf(inputChar);
            Map<String, Transition> stateTransitions = transitions.get(currentState);

            if (stateTransitions == null || !stateTransitions.containsKey(input)) {
                return null; // Impossible sequence
            }

            Transition trans = stateTransitions.get(input);
            outputSeq.append(trans.output()); // Add output character
            currentState = trans.toState();   // Update current state
        }

        return outputSeq.toString();
    }

    /**
     * Gets the set of transitions used during sequence execution.
     * Each transition is identified by a pair (state, input character).
     * <p>
     * This method is useful for:
     * - Checking which transitions are used
     * - Avoiding reusing the same transitions
     * - Analyzing machine behavior
     * </p>
     *
     * @param state Initial state
     * @param inputSeq Input sequence to analyze
     * @param transitions Map of possible transitions
     * @return Set of used transitions, each being a pair (state, input)
     */
    public static Set<Map.Entry<Integer, String>> getUsedTransitions(int state, String inputSeq,
                                                                     Map<Integer, Map<String, Transition>> transitions) {
        Set<Map.Entry<Integer, String>> used = new HashSet<>();
        int currentState = state;

        for (char inputChar : inputSeq.toCharArray()) {
            String input = String.valueOf(inputChar);
            Map<String, Transition> stateTransitions = transitions.get(currentState);

            if (stateTransitions != null && stateTransitions.containsKey(input)) {
                used.add(new AbstractMap.SimpleEntry<>(currentState, input));
                currentState = stateTransitions.get(input).toState();
            }
        }

        return used;
    }

    /**
     * Provides complete details of each transition used in a sequence.
     * <p>
     * For each transition, captures:
     * - Starting state
     * - Destination state
     * - Input character
     * - Output character produced
     * </p>
     * <p>
     * Especially useful for:
     * - Debugging
     * - Visualizing the path taken
     * - Verifying sequences
     * </p>
     *
     * @param state Initial state
     * @param inputSeq Sequence to analyze
     * @param transitions Map of possible transitions
     * @return Ordered list of details for each transition
     */
    public static List<TransitionDetail> getDetailedTransitions(int state, String inputSeq,
                                                                Map<Integer, Map<String, Transition>> transitions) {
        List<TransitionDetail> detailed = new ArrayList<>();
        int currentState = state;

        for (char inputChar : inputSeq.toCharArray()) {
            String input = String.valueOf(inputChar);
            Map<String, Transition> stateTransitions = transitions.get(currentState);

            if (stateTransitions != null && stateTransitions.containsKey(input)) {
                Transition trans = stateTransitions.get(input);
                detailed.add(new TransitionDetail(currentState, trans.toState(), input, trans.output()));
                currentState = trans.toState();
            }
        }

        return detailed;
    }

    /**
     * Generates all possible sequences for a given state.
     * <p>
     * The method observes several constraints:
     * - Doesn't use transitions already used elsewhere
     * - Doesn't exceed the specified maximum length
     * - Explores all valid combinations
     * </p>
     *
     * @param state Starting state
     * @param transitions All possible transitions
     * @param usedTransitions Transitions to avoid (already used)
     * @param maxLength Maximum length of sequences to generate
     * @return List of all valid sequences found
     */
    public static List<Sequence> getAllSequencesForState(int state,
                                                         Map<Integer, Map<String, Transition>> transitions,
                                                         Set<Map.Entry<Integer, String>> usedTransitions,
                                                         int maxLength) {
        List<Sequence> sequences = new ArrayList<>();
        explore(state, "", "", new HashSet<>(), sequences, transitions, usedTransitions, maxLength);
        return sequences;
    }

    /**
     * Recursive method that explores all possible sequences.
     * <p>
     * Uses a backtracking algorithm:
     * 1. Adds the current sequence if it's not empty
     * 2. If max length is reached, terminates this branch
     * 3. Otherwise, tries each possible transition
     * 4. For each valid transition, makes a recursive call
     * </p>
     *
     * @param currentState Current state in exploration
     * @param currentInput Input sequence built so far
     * @param currentOutput Corresponding output sequence
     * @param currentUsed Transitions used in this branch
     * @param sequences List to store found sequences
     * @param transitions All possible transitions
     * @param usedTransitions Transitions to avoid
     * @param maxLength Maximum length not to exceed
     */
    private static void explore(int currentState, String currentInput, String currentOutput,
                                Set<Map.Entry<Integer, String>> currentUsed, List<Sequence> sequences,
                                Map<Integer, Map<String, Transition>> transitions,
                                Set<Map.Entry<Integer, String>> usedTransitions, int maxLength) {
        if (!currentInput.isEmpty()) {
            sequences.add(new Sequence(currentInput, currentOutput, new HashSet<>(currentUsed)));
        }

        if (currentInput.length() >= maxLength) {
            return;
        }

        Map<String, Transition> stateTransitions = transitions.get(currentState);
        if (stateTransitions != null) {
            for (Map.Entry<String, Transition> entry : stateTransitions.entrySet()) {
                Map.Entry<Integer, String> newTransition =
                        new AbstractMap.SimpleEntry<>(currentState, entry.getKey());

                if (!usedTransitions.contains(newTransition)) {
                    Set<Map.Entry<Integer, String>> newUsed = new HashSet<>(currentUsed);
                    newUsed.add(newTransition);

                    explore(entry.getValue().toState(),
                            currentInput + entry.getKey(),
                            currentOutput + entry.getValue().output(),
                            newUsed, sequences, transitions, usedTransitions, maxLength);
                }
            }
        }
    }

    /**
     * Checks if a sequence is unique for a given state.
     * <p>
     * A sequence is unique if:
     * - It produces a specific output from this state
     * - No other state produces the same output with this input
     * </p>
     *
     * @param testState State to test
     * @param inputSeq Input sequence to verify
     * @param outputSeq Expected output
     * @param transitions All possible transitions
     * @return true if the sequence is unique for this state
     */
    public static boolean isSequenceUniqueForState(int testState, String inputSeq, String outputSeq,
                                                   Map<Integer, Map<String, Transition>> transitions) {
        for (int state : transitions.keySet()) {
            if (state != testState) {
                String result = executeSequence(state, inputSeq, transitions);
                if (outputSeq.equals(result)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Main method that finds unique sequences to identify each state.
     * <p>
     * For each state, it searches for the shortest sequence that:
     * 1. Produces an output unique to that state
     * 2. Only uses transitions not used by other sequences
     * </p>
     *
     * @param transitions Complete map of machine transitions
     * @return Map associating each state with its identifying sequence
     */
    public static Map<Integer, Map.Entry<String, String>> findStateIdentifyingSequences(
            Map<Integer, Map<String, Transition>> transitions) {
        Map<Integer, Map.Entry<String, String>> uniqueSequences = new HashMap<>();
        Set<Map.Entry<Integer, String>> usedTransitions = new HashSet<>();

        for (int maxLen = 1; maxLen < MAX_SEQUENCE_LENGTH; maxLen++) {
            List<Integer> states = new ArrayList<>(transitions.keySet());
            Collections.sort(states);

            for (int state : states) {
                if (!uniqueSequences.containsKey(state)) {
                    findUniqueSequenceForState(state, transitions, usedTransitions,
                            uniqueSequences, maxLen);
                }
            }
        }

        return uniqueSequences;
    }

    /**
     * Finds a unique sequence for a specific state.
     * <p>
     * Process:
     * 1. Generates all possible sequences up to maxLen
     * 2. Filters those unique to the state
     * 3. Chooses the shortest among valid sequences
     * </p>
     *
     * @param state State for which to find a sequence
     * @param transitions Map of available transitions
     * @param usedTransitions Set of already used transitions to avoid
     * @param uniqueSequences Map to store the found sequence
     * @param maxLen Maximum sequence length to try
     */
    private static void findUniqueSequenceForState(int state,
                                                   Map<Integer, Map<String, Transition>> transitions,
                                                   Set<Map.Entry<Integer, String>> usedTransitions,
                                                   Map<Integer, Map.Entry<String, String>> uniqueSequences,
                                                   int maxLen) {
        List<Sequence> sequences = getAllSequencesForState(state, transitions,
                usedTransitions, maxLen);
        List<Sequence> uniqueForState = new ArrayList<>();

        for (Sequence seq : sequences) {
            if (isSequenceUniqueForState(state, seq.input(), seq.output(), transitions) &&
                    Collections.disjoint(seq.usedTransitions(), usedTransitions)) {
                uniqueForState.add(seq);
            }
        }

        if (!uniqueForState.isEmpty()) {
            Sequence shortest = findShortestSequence(uniqueForState);
            uniqueSequences.put(state,
                    new AbstractMap.SimpleEntry<>(shortest.input(), shortest.output()));
            usedTransitions.addAll(shortest.usedTransitions());
        }
    }

    /**
     * Finds the shortest sequence from a list of sequences.
     *
     * @param sequences List of sequences to compare
     * @return The sequence with the shortest input string
     * @throws IllegalArgumentException if the list is empty
     */
    private static Sequence findShortestSequence(List<Sequence> sequences) {
        return sequences.stream()
                .min(Comparator.comparingInt(seq -> seq.input().length()))
                .orElseThrow(() -> new IllegalArgumentException("The sequence list is empty"));
    }
}