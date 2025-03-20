package fr.corentin.uiosequence;

import fr.corentin.uiosequence.core.StateMachine;
import fr.corentin.uiosequence.core.StateMachineUtility;
import fr.corentin.uiosequence.core.Transition;

import java.io.IOException;
import java.io.InputStream;

/**
 * Main class demonstrating the use of StateMachineBuilder
 * and the analysis of state sequences.
 * <p>
 * This class serves as the entry point for the UIO sequence analysis.
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
        java.util.Map<Integer, java.util.Map<String, Transition>> stateMachine;

        try {
            InputStream inputStream = UIOSeq.class.getClassLoader().getResourceAsStream(STATE_MACHINE_FILENAME);
            if (inputStream == null) {
                throw new IOException("Resource not found: " + STATE_MACHINE_FILENAME);
            }

            stateMachine = StateMachineUtility.buildStateMachineFromJson(inputStream);
            System.out.println("State machine loaded from resources: " + STATE_MACHINE_FILENAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        java.util.Map<Integer, java.util.Map.Entry<String, String>> identifyingSequences =
                StateMachine.findStateIdentifyingSequences(stateMachine);

        StateMachineUtility.analyzeAndPrintResults(stateMachine, identifyingSequences);
    }
}