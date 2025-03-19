package fr.corentin.w.core;

import java.util.*;

/**
 * Core implementation of the W algorithm for state machine testing.
 * <p>
 * The W algorithm generates a discrimination tree that can distinguish between states
 * in a finite state machine by finding sequences of input/output pairs that produce
 * different behaviors for different states. This class provides methods to execute
 * the algorithm, extract discrimination sequences, and display the results.
 * </p>
 *
 * @author Corentin
 * @version 1.0
 * @see WResult
 * @see Node
 * @see Pair
 * @see Step
 * @see Transition
 */
public class WCore {

    /**
     * Executes the W algorithm on a given state transition system.
     * <p>
     * This method builds a discrimination tree that can be used to distinguish
     * between different states in the system.
     * </p>
     *
     * @param transitions Map representing the state transition system where:
     *                   - Keys are state identifiers (integers)
     *                   - Values are maps from input characters to transitions (next state and output)
     * @return A {@link WResult} containing the discrimination tree and intermediate steps
     */
    public WResult executeAlgorithm(Map<Integer, Map<Character, Transition>> transitions) {
        // Get all states
        List<Integer> states = new ArrayList<>(transitions.keySet());

        // Get all possible input/output pairs
        Set<Pair<Character, Character>> inputOutputPairs = new HashSet<>();
        for (int state : states) {
            Map<Character, Transition> stateTrans = transitions.getOrDefault(state, new HashMap<>());
            for (Map.Entry<Character, Transition> entry : stateTrans.entrySet()) {
                char input = entry.getKey();
                char output = entry.getValue().output();
                inputOutputPairs.add(new Pair<>(input, output));
            }
        }

        // Intermediate steps to track progress
        List<Step> steps = new ArrayList<>();

        // Build the tree with all states
        Node tree = buildTree(states, inputOutputPairs, transitions, steps, "");

        return new WResult(tree, steps);
    }

    /**
     * Recursively builds the discrimination tree.
     * <p>
     * This method attempts to find the best input/output pair that can discriminate
     * between the current set of states, then recursively applies the process to
     * each resulting group of states.
     * </p>
     *
     * @param currentStates List of state identifiers to discriminate
     * @param inputOutputPairs Set of available input/output pairs to try
     * @param transitions The state transition system
     * @param steps List to collect intermediate steps during execution
     * @param path Current path in the tree (for debugging)
     * @return A node in the discrimination tree
     */
    private Node buildTree(List<Integer> currentStates,
                           Set<Pair<Character, Character>> inputOutputPairs,
                           Map<Integer, Map<Character, Transition>> transitions,
                           List<Step> steps,
                           String path) {
        // If only one state, we've reached a leaf
        if (currentStates.size() <= 1) {
            return new Node(currentStates, null, true);
        }

        // Try all I/O pairs and choose the one that gives the best discrimination
        Pair<Character, Character> bestPair = null;
        Map<String, List<Integer>> bestGroups = null;
        int[] bestPartition = {0, currentStates.size()};  // [size of smallest group, size of largest group]

        for (Pair<Character, Character> inputOutputPair : inputOutputPairs) {
            Map<String, List<Integer>> groups = createGroups(currentStates, inputOutputPair, transitions);

            // Filter empty groups
            groups.entrySet().removeIf(entry -> entry.getValue().isEmpty());

            // If we can discriminate
            if (groups.size() > 1) {
                int smallest = Integer.MAX_VALUE;
                int largest = 0;

                for (List<Integer> group : groups.values()) {
                    int size = group.size();
                    smallest = Math.min(smallest, size);
                    largest = Math.max(largest, size);
                }

                // Prefer partitions that have the smallest group as large as possible
                if (smallest > bestPartition[0] ||
                        (smallest == bestPartition[0] && largest < bestPartition[1])) {
                    bestPartition[0] = smallest;
                    bestPartition[1] = largest;
                    bestPair = inputOutputPair;
                    bestGroups = groups;
                }
            }
        }

        // If we can't discriminate further
        if (bestPair == null) {
            return new Node(currentStates, null, true);
        }

        // Record intermediate step
        Step step = new Step(
                new ArrayList<>(currentStates),
                bestPair,
                new HashMap<>(bestGroups)
        );
        steps.add(step);

        // Build the tree recursively
        Node result = new Node(currentStates, bestPair, false);

        for (Map.Entry<String, List<Integer>> entry : bestGroups.entrySet()) {
            String groupLabel = entry.getKey();
            List<Integer> groupStates = entry.getValue();
            String newPath = path + " > " + groupLabel;

            Node childNode = buildTree(
                    groupStates, inputOutputPairs, transitions, steps, newPath
            );
            result.addGroup(groupLabel, childNode);
        }

        return result;
    }

    /**
     * Creates groups of states based on their response to a specific input/output pair.
     * <p>
     * States are grouped into two categories:
     * - Those that produce the expected output for the given input
     * - Those that produce a different output or have no transition for the input
     * </p>
     *
     * @param statesToGroup List of states to group
     * @param inputOutputPair The input/output pair to test
     * @param transitions The state transition system
     * @return A map where keys are group labels and values are lists of states
     */
    private Map<String, List<Integer>> createGroups(List<Integer> statesToGroup,
                                                    Pair<Character, Character> inputOutputPair,
                                                    Map<Integer, Map<Character, Transition>> transitions) {
        char input = inputOutputPair.first();
        char output = inputOutputPair.second();
        List<Integer> positiveGroup = new ArrayList<>();  // States that respond to the I/O pair
        List<Integer> negativeGroup = new ArrayList<>();  // States that don't respond to the I/O pair

        for (int state : statesToGroup) {
            Map<Character, Transition> stateTrans = transitions.get(state);
            if (stateTrans != null && stateTrans.containsKey(input)) {
                char outputObtained = stateTrans.get(input).output();
                if (outputObtained == output) {
                    positiveGroup.add(state);
                } else {
                    negativeGroup.add(state);
                }
            } else {
                negativeGroup.add(state);
            }
        }

        Map<String, List<Integer>> result = new HashMap<>();
        result.put(input + "/" + output, positiveGroup);
        result.put("not " + input + "/" + output, negativeGroup);

        return result;
    }

    /**
     * Extracts discrimination sequences for each state from the tree.
     * <p>
     * A discrimination sequence is a path from the root to a leaf node in the
     * discrimination tree, represented as a list of input/output pairs that
     * uniquely identify a state.
     * </p>
     *
     * @param tree The discrimination tree generated by the W algorithm
     * @return A map where keys are state identifiers and values are discrimination sequences
     */
    public Map<Integer, List<String>> extractDiscriminationSequences(Node tree) {
        Map<Integer, List<String>> sequences = new HashMap<>();
        traverseTree(tree, new ArrayList<>(), sequences);
        return sequences;
    }

    /**
     * Recursively traverses the discrimination tree to extract sequences.
     *
     * @param node The current node in the tree
     * @param currentSequence The sequence accumulated so far
     * @param sequences Map to collect the result
     */
    private void traverseTree(Node node, List<String> currentSequence,
                              Map<Integer, List<String>> sequences) {
        if (node.isTerminal()) {
            for (int state : node.getStates()) {
                sequences.put(state, new ArrayList<>(currentSequence));
            }
            return;
        }

        for (Map.Entry<String, Node> entry : node.getGroups().entrySet()) {
            String label = entry.getKey();
            Node subTree = entry.getValue();

            List<String> newSequence = new ArrayList<>(currentSequence);
            newSequence.add(label);
            traverseTree(subTree, newSequence, sequences);
        }
    }

    /**
     * Displays the intermediate steps of the W algorithm in a structured format.
     *
     * @param steps The list of steps performed during algorithm execution
     */
    public void displayDiscriminationSteps(List<Step> steps) {
        System.out.println("\n=== STEPS ===\n");

        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            System.out.println("Step " + (i + 1) + ":");
            System.out.println("Starting group: " + step.startingGroup());
            System.out.println("I/O pair: " + step.inputOutputPair());
            System.out.println("Resulting groups:");

            for (Map.Entry<String, List<Integer>> entry : step.resultingGroups().entrySet()) {
                System.out.println("  • Group " + entry.getKey() + ": " + entry.getValue());
            }

            System.out.println("-".repeat(50));
        }
    }

    /**
     * Displays the discrimination tree in a readable format.
     *
     * @param tree The root node of the discrimination tree
     */
    public void displayTree(Node tree) {
        displayTreeRecursive(tree, 0);
    }

    /**
     * Helper method to recursively display the tree with indentation.
     *
     * @param tree The current node to display
     * @param level The current indentation level
     */
    private void displayTreeRecursive(Node tree, int level) {
        String indent = "  ".repeat(level);

        if (tree.isTerminal()) {
            System.out.println(indent + "Terminal states: " + tree.getStates());
            return;
        }

        System.out.println(indent + "States: " + tree.getStates());
        if (tree.getInputOutputPair() != null) {
            System.out.println(indent + "I/O pair: " + tree.getInputOutputPair());
        }

        for (Map.Entry<String, Node> entry : tree.getGroups().entrySet()) {
            System.out.println(indent + "  Group " + entry.getKey() + ":");
            displayTreeRecursive(entry.getValue(), level + 2);
        }
    }

    /**
     * Displays the discrimination sequences for each state.
     *
     * @param sequences Map of state identifiers to their discrimination sequences
     */
    public void displaySequencesByState(Map<Integer, List<String>> sequences) {
        System.out.println("\n=== SEQUENCES BY STATE ===\n");

        List<Integer> states = new ArrayList<>(sequences.keySet());
        Collections.sort(states);

        for (int state : states) {
            List<String> sequence = sequences.get(state);
            String sequenceStr = String.join(" + ", sequence);
            System.out.println("State " + state + ": " + sequenceStr);
        }
    }
}