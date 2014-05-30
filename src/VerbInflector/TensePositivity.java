package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 12:55 AM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public enum TensePositivity {
    POSITIVE (1),
    NEGATIVE (2);

    private int value;

    TensePositivity(int value){
        this.value=value;
    }

    public int value(){
        return this.value;
    }
}
