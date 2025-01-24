package fr.corentin;

/**
 * Classe qui capture tous les details d'une transition effectuee.
 * Contrairement à la classe Transition qui definit une transition possible,
 * TransitionDetail represente une transition qui a ete reellement utilisee,
 * avec son etat de depart en plus.
 *
 * Cette classe est utilisee pour:
 * - Tracer l'execution d'une sequence
 * - Deboguer le comportement de la machine
 * - Visualiser le chemin emprunte
 */
public class TransitionDetail {
    /**
     * L'etat depuis lequel la transition a ete effectuee.
     * C'est l'etat dans lequel etait la machine avant la transition.
     */
    private final int fromState;

    /**
     * L'etat vers lequel la machine est allee.
     * C'est l'etat final après la transition.
     */
    private final int toState;

    /**
     * Le caractère qui a declenche la transition.
     * C'est l'entree qui a ete fournie à la machine.
     */
    private final String input;

    /**
     * Le caractère produit par la transition.
     * C'est ce que la machine a emis pendant la transition.
     */
    private final String output;

    /**
     * Constructeur qui enregistre tous les details d'une transition.
     * Exemple d'utilisation:
     * new TransitionDetail(1, 2, "a", "x") signifie:
     * - La machine etait dans l'etat 1
     * - Elle a reçu le caractère "a"
     * - Elle est passee à l'etat 2
     * - Elle a produit le caractère "x"
     *
     * @param fromState Etat de depart
     * @param toState Etat d'arrivee
     * @param input Caractère reçu
     * @param output Caractère produit
     */
    public TransitionDetail(int fromState, int toState, String input, String output) {
        this.fromState = fromState;
        this.toState = toState;
        this.input = input;
        this.output = output;
    }

    /**
     * @return L'etat depuis lequel la transition a ete effectuee
     */
    public int getFromState() {
        return fromState;
    }

    /**
     * @return L'etat vers lequel la machine est allee
     */
    public int getToState() {
        return toState;
    }

    /**
     * @return Le caractère qui a declenche la transition
     */
    public String getInput() {
        return input;
    }

    /**
     * @return Le caractère produit pendant la transition
     */
    public String getOutput() {
        return output;
    }

    /**
     * Representation textuelle detaillee de la transition.
     * Format: "Etat X -> Etat Y sur entree A, sortie: B"
     */
    @Override
    public String toString() {
        return String.format("Etat %d -> Etat %d sur entree %s, sortie: %s",
                fromState, toState, input, output);
    }
}