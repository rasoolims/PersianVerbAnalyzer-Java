package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 10:59 PM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public enum NumberType {
    INVALID (0),
    SINGULAR ( 1),
    PLURAL (2 );

    private int value;
    NumberType(int value){
        this.value=value;
    }

    public int value(){
        return this.value;
    }
}
