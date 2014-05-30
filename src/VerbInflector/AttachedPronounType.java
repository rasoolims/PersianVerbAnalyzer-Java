package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 12:42 AM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public enum AttachedPronounType {
    AttachedPronoun_NONE(1),
    FIRST_PERSON_SINGULAR (2),
    SECOND_PERSON_SINGULAR (4),
    THIRD_PERSON_SINGULAR (8),
    FIRST_PERSON_PLURAL (16),
    SECOND_PERSON_PLURAL (32),
    THIRD_PERSON_PLURAL (64) ;

    private int value;

    AttachedPronounType(int value){
        this.value=value;
    }

    public int value(){
        return value;
    }
}
