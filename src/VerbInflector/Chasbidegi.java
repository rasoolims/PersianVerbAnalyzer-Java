package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 10:59 PM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public enum Chasbidegi {
    TANHA (0),
    NEXT (1),
    PREV (2);

    private int value;
    Chasbidegi(int value){
        this.value=value;
    }

    public int value(){
        return this.value;
    }
}
