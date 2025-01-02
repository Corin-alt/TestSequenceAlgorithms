package fr.corentin;

import java.util.*;

public class Main {
    private record TransitionData(int from, int to, char input, char output) {}
    private record TestCase(int startState, String input, List<String> expected) {}

    public static void main(String[] args) {
        Graph graph = createGraph();
        runTests(graph);
    }

    private static Graph createGraph() {
        Graph graph = new Graph();
        TransitionData[] transitions = {
                new TransitionData(1, 2, 'a', 'z'),
                new TransitionData(2, 3, 'b', 't'),
                new TransitionData(2, 4, 'a', 'x'),
                new TransitionData(3, 1, 'b', 'x'),
                new TransitionData(3, 2, 'a', 'x'),
                new TransitionData(4, 2, 'a', 'y'),
                new TransitionData(4, 5, 'b', 'x'),
                new TransitionData(5, 3, 'a', 'z')
        };

        for (TransitionData t : transitions) {
            graph.addTransition(t.from, t.to, t.input, t.output);
        }
        return graph;
    }

    private static void runTests(Graph graph) {
        TestCase[] testCases = {
                new TestCase(1, "ab", List.of("zt", "zx")),
                new TestCase(2, "aa", List.of("xx")),
                new TestCase(3, "bab", List.of("xzt", "xzx"))
        };

        for (TestCase test : testCases) {
            StateMachine.testSequence(graph, test.startState, test.input, test.expected);
        }
    }
}