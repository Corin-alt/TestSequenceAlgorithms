package fr.corentin;

/**
 * Classe representant une transition dans la machine à etats.
 * Une transition definit:
 * - L'etat d'arrivee quand on reçoit une entree
 * - Le caractère de sortie produit
 *
 * Par exemple:
 * Si dans l'etat 1, sur l'entree 'a', on a:
 * - toState = 2
 * - output = 'x'
 * Cela signifie que la machine:
 * - Passe à l'etat 2
 * - Produit le caractère 'x'
 */
public class Transition {
    /**
     * L'etat vers lequel la transition mène.
     * C'est l'etat dans lequel la machine se trouve après la transition.
     */
    private final int toState;

    /**
     * Le caractère produit par cette transition.
     * C'est ce que la machine "emet" quand cette transition est utilisee.
     */
    private final String output;

    /**
     * Constructeur qui initialise une nouvelle transition.
     * Les champs sont finaux car une transition ne doit pas
     * changer une fois creee.
     *
     * @param toState L'etat de destination (où la machine va)
     * @param output Le caractère produit par la transition
     */
    public Transition(int toState, String output) {
        this.toState = toState;
        this.output = output;
    }

    /**
     * @return L'etat de destination de la transition
     */
    public int getToState() {
        return toState;
    }

    /**
     * @return Le caractère produit par la transition
     */
    public String getOutput() {
        return output;
    }

    /**
     * Representation textuelle de la transition.
     * Format: "vers etat X, sortie: Y"
     */
    @Override
    public String toString() {
        return String.format("vers etat %d, sortie: %s", toState, output);
    }
}