package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 1:07 AM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public enum VerbTransitivity {
    Transitive (1),
    InTransitive (2),
    BiTransitive (4);

    private int value;

    VerbTransitivity(int value){
        this.value=value;
    }

    public int value(){
        return this.value;
    }
}
