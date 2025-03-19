package fr.corentin.uiosequence;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Main class demonstrating the use of StateMachineBuilder
 * and the analysis of state sequences.
 * <p>
 * This class allows:
 * - Building a complete state machine
 * - Finding unique sequences for each state
 * - Analyzing and displaying detailed results
 * - Verifying transition uniqueness
 */
public class UIOSeq {

    private static String STATE_MACHINE_FILENAME = "uio/state_machine.json";

    /**
     * Main entry point of the program.
     * <p>
     * Complete process:
     * 1. State machine construction with the builder
     * - Definition of all states
     * - Configuration of possible transitions
     * 2. Search for identifying sequences
     * - For each state, finds a unique sequence
     * 3. Analysis and display of results
     * - Sequences found
     * - States without solution
     * - Transition verification
     *
     * @param args command line arguments (optional: path to JSON file)
     */
    public static void main(String[] args) {
        Map<Integer, Map<String, Transition>> stateMachine;

        try {
            InputStream inputStream = UIOSeq.class.getClassLoader().getResourceAsStream(STATE_MACHINE_FILENAME);
            if (inputStream == null) {
                throw new IOException("Resource not found: " + STATE_MACHINE_FILENAME);
            }

            stateMachine = buildStateMachineFromJson(inputStream);
            System.out.println("State machine loaded from resources: " + STATE_MACHINE_FILENAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<Integer, Map.Entry<String, String>> identifyingSequences =
                StateMachine.findStateIdentifyingSequences(stateMachine);

        analyzeAndPrintResults(stateMachine, identifyingSequences);
    }

    /**
     * Builds the state machine by reading from a JSON file.
     * Expected JSON format:
     * {
     * "states": {
     * "1": {
     * "transitions": {
     * "a": {"toState": 5, "output": "b"},
     * "d": {"toState": 2, "output": "w"}
     * }
     * },
     * "2": {
     * ...
     * }
     * }
     * }
     *
     * @param inputStream Path to the JSON file containing the definition
     * @return Complete transition map of the machine
     * @throws IOException If the file cannot be read or parsed
     */
    private static Map<Integer, Map<String, Transition>> buildStateMachineFromJson(InputStream inputStream)
            throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            JsonObject rootObject = gson.fromJson(reader, JsonObject.class);

            StateMachineBuilder builder = new StateMachineBuilder();
            JsonObject statesObject = rootObject.getAsJsonObject("states");

            for (Map.Entry<String, JsonElement> stateEntry : statesObject.entrySet()) {
                String stateKey = stateEntry.getKey();
                int stateId = Integer.parseInt(stateKey);
                JsonObject stateObject = stateEntry.getValue().getAsJsonObject();
                JsonObject transitionsObject = stateObject.getAsJsonObject("transitions");

                builder.state(stateId);

                for (Map.Entry<String, JsonElement> transEntry : transitionsObject.entrySet()) {
                    String input = transEntry.getKey();
                    JsonObject transObject = transEntry.getValue().getAsJsonObject();
                    int toState = transObject.get("toState").getAsInt();
                    String output = transObject.get("output").getAsString();

                    builder.onInput(input, toState, output);
                }
            }

            return builder.build();
        }
    }

    /**
     * Analyzes and displays the results of the found sequences.
     * <p>
     * This method:
     * 1. Identifies states without solution
     * 2. Displays found sequences
     * 3. Checks used transitions
     * <p>
     * The analysis is complete and verifies:
     * - Presence of sequences for each state
     * - Uniqueness of transitions
     * - Details of each transition
     *
     * @param transitions          The complete state machine
     * @param identifyingSequences The sequences found for each state
     */
    private static void analyzeAndPrintResults(
            Map<Integer, Map<String, Transition>> transitions,
            Map<Integer, Map.Entry<String, String>> identifyingSequences) {

        // 1. Find states without solution
        Set<Integer> statesWithoutSolution = findStatesWithoutSolution(transitions, identifyingSequences);

        // 2. Display found sequences
        printSequences(identifyingSequences, statesWithoutSolution);

        // 3. Check used transitions
        verifyAndPrintTransitions(identifyingSequences, transitions);
    }

    /**
     * Finds states that don't have an identifying sequence.
     * <p>
     * Process:
     * 1. Gets all states in the machine
     * 2. Gets states with a solution
     * 3. Computes the difference to find those without solution
     * <p>
     * Example:
     * - All states: [1, 2, 3, 4, 5]
     * - States with solution: [1, 2, 4]
     * - States without solution: [3, 5]
     *
     * @param transitions          The complete state machine
     * @param identifyingSequences The found sequences
     * @return Set of states without identifying sequence
     */
    private static Set<Integer> findStatesWithoutSolution(
            Map<Integer, Map<String, Transition>> transitions,
            Map<Integer, Map.Entry<String, String>> identifyingSequences) {
        // Get all states in the machine
        Set<Integer> allStates = transitions.keySet();
        // Get states that have a sequence
        Set<Integer> statesWithSolution = identifyingSequences.keySet();
        // Create a copy to avoid modifying the original
        Set<Integer> statesWithoutSolution = new HashSet<>(allStates);
        // Remove states that have a solution
        statesWithoutSolution.removeAll(statesWithSolution);
        return statesWithoutSolution;
    }

    /**
     * Displays the found sequences and states without solution.
     * <p>
     * Display format:
     * For each state with solution:
     * - State number
     * - Input sequence
     * - Corresponding output sequence
     * <p>
     * For states without solution:
     * - List of states
     * - Explanatory message
     *
     * @param identifyingSequences  Map of sequences found per state
     * @param statesWithoutSolution Set of states without solution
     */
    private static void printSequences(
            Map<Integer, Map.Entry<String, String>> identifyingSequences,
            Set<Integer> statesWithoutSolution) {

        // First display found sequences
        System.out.println("Unique sequences identifying each state:");
        // Use TreeSet to display states in order
        for (int state : new TreeSet<>(identifyingSequences.keySet())) {
            Map.Entry<String, String> seq = identifyingSequences.get(state);
            System.out.printf("State %d: input='%s', output='%s'%n",
                    state, seq.getKey(), seq.getValue());
        }

        // Then display states without solution
        if (!statesWithoutSolution.isEmpty()) {
            System.out.println("\nStates without identifying sequence found:");
            for (int state : new TreeSet<>(statesWithoutSolution)) {
                System.out.printf("State %d: No unique sequence possible " +
                        "with the given constraints%n", state);
            }
        }
    }

    /**
     * Checks and displays the details of used transitions.
     * <p>
     * For each state with a sequence:
     * 1. Gets the transition details
     * 2. Displays the complete path
     * 3. Checks if there are duplicate transitions
     * <p>
     * Example of transition:
     * State 1 -> State 5 on input 'a', output: 'b'
     *
     * @param identifyingSequences The sequences to check
     * @param transitions          The complete state machine
     */
    private static void verifyAndPrintTransitions(
            Map<Integer, Map.Entry<String, String>> identifyingSequences,
            Map<Integer, Map<String, Transition>> transitions) {

        System.out.println("\nVerification of used transitions:");
        // Track all used transitions
        Set<Map.Entry<Integer, String>> allUsedTransitions = new HashSet<>();

        // Analyze each state in order
        for (int testState : new TreeSet<>(identifyingSequences.keySet())) {
            Map.Entry<String, String> seq = identifyingSequences.get(testState);

            // Get transition details for this state
            List<TransitionDetail> detailedTransitions =
                    StateMachine.getDetailedTransitions(testState, seq.getKey(), transitions);
            Set<Map.Entry<Integer, String>> transitionsUsed =
                    StateMachine.getUsedTransitions(testState, seq.getKey(), transitions);

            // Display details and check duplicates
            printTransitionDetails(testState, seq, detailedTransitions);
            checkDuplicateTransitions(transitionsUsed, allUsedTransitions);

            // Add transitions to the global set
            allUsedTransitions.addAll(transitionsUsed);
        }
    }

    /**
     * Displays the details of a transition sequence.
     * <p>
     * Display format for each transition:
     * (start_state, input) : start_state -> end_state, input/output
     * <p>
     * Example:
     * (1, a) : state 1 -> state 5, input/output: a/b
     *
     * @param testState           Current state being analyzed
     * @param seq                 Sequence to display
     * @param detailedTransitions List of transitions to detail
     */
    private static void printTransitionDetails(
            int testState,
            Map.Entry<String, String> seq,
            List<TransitionDetail> detailedTransitions) {

        System.out.printf("%nFor state %d - sequence: input='%s', output='%s'%n",
                testState, seq.getKey(), seq.getValue());
        System.out.println("Used transitions:");

        // Display each transition in detail
        for (TransitionDetail trans : detailedTransitions) {
            System.out.printf("  (%d, %s) : state %d -> state %d, input/output: %s/%s%n",
                    trans.fromState(), trans.input(), trans.fromState(),
                    trans.toState(), trans.input(), trans.output());
        }
    }

    /**
     * Checks and displays duplicate transitions.
     * <p>
     * A transition is duplicate if:
     * - It appears in the new transitions
     * - AND it already exists in previous transitions
     * <p>
     * Purpose: Ensure each transition is used only once
     * to guarantee sequence uniqueness.
     *
     * @param newTransitions      New transitions to check
     * @param existingTransitions Transitions already used
     */
    private static void checkDuplicateTransitions(
            Set<Map.Entry<Integer, String>> newTransitions,
            Set<Map.Entry<Integer, String>> existingTransitions) {

        // Create a copy for verification
        Set<Map.Entry<Integer, String>> duplicates = new HashSet<>(newTransitions);
        // Keep only transitions that already exist
        duplicates.retainAll(existingTransitions);

        // Display a warning if duplicates are found
        if (!duplicates.isEmpty()) {
            System.out.println("WARNING: Reused transitions: " + duplicates);
        }
    }
}