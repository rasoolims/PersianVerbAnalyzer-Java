package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 9:24 PM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

/**
 * A reimplementation of KeyValuePair in Java
 * @param <K> key of type K
 * @param <V> value of type V
 */
public class KeyValuePair<K,V> {
    private K key;
    private V value;

    public KeyValuePair(K key, V value) {
        this.key=key;
        this.value=value;
    }

    public K getKey(){
        return key;
    }

    public V getValue(){
        return value;
    }
}
