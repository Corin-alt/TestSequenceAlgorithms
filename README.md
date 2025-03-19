# TestSequenceAlgorithms

A Java implementation of two fundamental finite state machine (FSM) testing algorithms: UIO Sequence and the W Algorithm. These algorithms generate test sequences that verify the correctness of implementations based on FSM specifications.

## Project Structure

The project is organized into two main packages:

- `fr.corentin.uiosequence`: Contains the implementation of the UIO Sequence algorithm
- `fr.corentin.w`: Contains the implementation of the W Algorithm

## UIO Sequence Algorithm

The UIO (Unique Input/Output) Sequence algorithm identifies input sequences that produce unique output sequences for each state in a finite state machine.

### Key Components

- **StateMachine**: Core class that manages the FSM and provides methods for:
- Executing input sequences to obtain outputs
- Finding unique identifying sequences for each state
- Tracking and analyzing used transitions
- Exploring possible sequences up to a specified length

- **StateMachineBuilder**: Fluent builder for constructing FSMs with a chained syntax.

- **Transition**: Represents a state transition with a destination state and output.

- **TransitionDetail**: Contains complete details of a performed transition, including starting state.

- **Sequence**: Represents an input/output sequence with its used transitions.

- **UIOSeq**: Main entry point for the UIO algorithm that:
- Loads a state machine from a JSON file
- Finds identifying sequences for each state
- Analyzes and displays results

### How It Works

1. Each state in the FSM is analyzed to find sequences that produce outputs unique to that state.
2. The algorithm searches for the shortest possible sequences, up to a configurable maximum length.
3. It carefully tracks used transitions to ensure sequences are truly distinguishing.
4. Results include the identifying sequences and detailed transition paths.

## W Algorithm

The W Algorithm (also known as Chow's W-method) creates a discrimination tree that can distinguish between any two states in a finite state machine.

### Key Components

- **WCore**: Main implementation of the W algorithm that:
- Builds a discrimination tree
- Extracts distinguishing sequences
- Displays intermediate steps and results

- **Node**: Represents a node in the discrimination tree with states and input/output pairs.

- **Pair**: Utility class for representing input/output pairs.

- **Step**: Records an intermediate step in the algorithm with discrimination decisions.

- **Transition**: Represents state transitions with next state and output.

- **WResult**: Encapsulates the results of the W algorithm execution.

- **WAlgorithm**: Main entry point that demonstrates the algorithm with an example FSM.

### How It Works

1. The algorithm analyzes all possible input/output pairs to find the ones that best discriminate between states.
2. It builds a tree where each node represents a group of states and each branch represents a different response to an input.
3. States that respond differently to inputs are separated into different branches.
4. The process continues recursively until all states are uniquely identified.
5. The final discrimination tree provides unique paths to identify each state.