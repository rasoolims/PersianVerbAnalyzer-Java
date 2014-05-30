package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 1:03 AM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public enum TensePassivity {
    ACTIVE (1), PASSIVE (2);

    private int value;

    TensePassivity(int value){
        this.value=value;
    }

    public int value(){
        return this.value;
    }
}
