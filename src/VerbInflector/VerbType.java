package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 1:06 AM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public enum VerbType {
    SADEH (1),
    PISHVANDI ( 2),
    MORAKKAB ( 4),
    MORAKKABPISHVANDI ( 8),
    MORAKKABHARFE_EZAFEH ( 16),
    EBAARATFELI ( 32),
    LAZEM_TAKFELI ( 64),
    AYANDEH_PISHVANDI ( 128 );

    private int value;

    VerbType(int value){
        this.value=value;
    }

    public int value(){
        return this.value;
    }
}
