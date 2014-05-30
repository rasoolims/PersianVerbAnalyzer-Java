package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 12:49 AM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public enum TenseFormationType {
    //todo for  GOZASHTEH_BAEED_ELTEZAMI and GOZASHTEH_BAEED_ESTEMRARI_ELTEZAMI
    TenseFormationType_NONE (0),
    HAAL_SAADEH_EKHBARI (1),
    HAAL_ELTEZAMI (2),
    HAAL_SAADEH (4),
    AMR (8),
    GOZASHTEH_SADEH (32),
    GOZASHTEH_ESTEMRAARI (64),
    GOZASHTEH_NAGHLI_SADEH (128),
    GOZASHTEH_NAGHLI_ESTEMRAARI (256),
    GOZASHTEH_BAEED (512),
    GOZASHTEH_ELTEZAMI (1024),
    PAYEH_MAFOOLI (2048),
    AAYANDEH (4096),
    GOZASHTEH_ABAD (8192),
    GOZASHTEH_BAEED_ELTEZAMI(16384),
    GOZASHTEH_BAEED_ESTEMRARI_ELTEZAMI(32768);


    private int value;
    TenseFormationType(int value){
        this.value=value;
    }

    public int value(){
        return this.value;
    }
}
