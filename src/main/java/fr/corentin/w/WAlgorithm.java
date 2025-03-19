package fr.corentin.w;

import fr.corentin.w.core.Transition;
import fr.corentin.w.core.WCore;
import fr.corentin.w.core.WResult;

import java.util.*;

/**
 * Main class for launching the W algorithm for state discrimination
 */
public class WAlgorithm {

    public static void main(String[] args) {
        // Example usage with the provided graph
        Map<Integer, Map<Character, Transition>> transitions = new HashMap<>();

        // State 1
        Map<Character, Transition> trans1 = new HashMap<>();
        trans1.put('a', new Transition(2, 'z'));
        trans1.put('b', new Transition(3, 't'));
        transitions.put(1, trans1);

        // State 2
        Map<Character, Transition> trans2 = new HashMap<>();
        trans2.put('c', new Transition(3, 'x'));
        transitions.put(2, trans2);

        // State 3
        Map<Character, Transition> trans3 = new HashMap<>();
        trans3.put('a', new Transition(4, 'x'));
        transitions.put(3, trans3);

        // State 4
        Map<Character, Transition> trans4 = new HashMap<>();
        trans4.put('b', new Transition(1, 't'));
        transitions.put(4, trans4);

        // Execute the algorithm and display the results
        WCore core = new WCore();
        WResult result = core.executeAlgorithm(transitions);
        core.displayDiscriminationSteps(result.steps());

        System.out.println("\n=== FINAL TREE ===\n");
        core.displayTree(result.tree());

        // Extract and display discrimination sequences by state
        Map<Integer, List<String>> sequences = core.extractDiscriminationSequences(result.tree());
        core.displaySequencesByState(sequences);
    }
}