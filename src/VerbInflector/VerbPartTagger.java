package VerbInflector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 12:06 PM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class VerbPartTagger {
    /**
     * An Object containing all sufficient and necessary data : verb dictionary
     */
    private static VerbList verbDic = null;

    private static String verbDicPath = "";

    /**
     * The conjVector instanceof a Vector containing all possible words that may stop a verb from getting a progressifier
     */
    public static Vector<String> ConjVector = new Vector<String>();

    public static void ConstructConjVector() {
        ConjVector = new Vector<String>();
        ConjVector.add("و");
        ConjVector.add("که");
        ConjVector.add("اما");
        ConjVector.add("تا");
        ConjVector.add("گرچه");
        ConjVector.add("اگرچه");
        ConjVector.add("چرا");
        ConjVector.add("یا");
        ConjVector.add("زیرا");
        ConjVector.add("اگر");
        ConjVector.add("لیکن");
        ConjVector.add("چون");
        ConjVector.add("همچنین");
        ConjVector.add("چرا‌که");
        ConjVector.add("ولی");
        ConjVector.add("هر‌چند");
        ConjVector.add("چراکه");
        ConjVector.add("-");
        ConjVector.add("هرچند");
        ConjVector.add("وگرنه");
        ConjVector.add("چنانچه");
        ConjVector.add("بلکه");
        ConjVector.add("والا");
        ConjVector.add("هرچه");
        ConjVector.add("ولی‌");
        ConjVector.add("ولیکن");
        ConjVector.add("بس‌که");
        ConjVector.add("ولو");
        ConjVector.add("لکن");
        ConjVector.add("یعنی");
        ConjVector.add("هنوز");
        ConjVector.add("مگر");
        ConjVector.add("خواه");
        ConjVector.add("پس");
        ConjVector.add("چو");
        ConjVector.add("اینکه");
        ConjVector.add("چه");
        ConjVector.add("بنابراین");
        ConjVector.add("الی");
        ConjVector.add("وقتی");
        ConjVector.add("اگه");
        ConjVector.add("منتهی");
        ConjVector.add("،");
        ConjVector.add("اگرنه");
        ConjVector.add("منتها");
        ConjVector.add("بی‌آنکه");
        ConjVector.add("والّا");
    }

    public static HashMap<String, Integer> StaticDic = new HashMap<String, Integer>();

    /**
     * retrieves verb Strings from dictionary
     */
    private static HashMap<Integer, Vector<VerbInflection>> GetVerbParts(String[] sentence, String[] posSentence) throws IOException {
        if (ConjVector.size() == 0)
            ConstructConjVector();
        HashMap<Integer, Vector<VerbInflection>> dic = new HashMap<Integer, Vector<VerbInflection>>();
        if (verbDic == null) {
            verbDic = new VerbList(verbDicPath);
        }
        for (int i = 0; i < sentence.length; i++) {
            if (posSentence[i].equals("V") || posSentence[i].equals("N") || posSentence[i].equals("ADJ")) {
                boolean add = false;

                //     dic.add(i, VerbList.VerbShapes.containsKey(sentence[i]) ? VerbList.VerbShapes.get(sentence[i]] : null);

                if (posSentence[i].equals("V")) {
                    dic.put(i, VerbList.VerbShapes.containsKey(sentence[i]) ? VerbList.VerbShapes.get(sentence[i]) : null);
                    add = true;
                } else if (posSentence[i].equals("N") || posSentence[i].equals("ADJ")) {
                    if (VerbList.VerbShapes.containsKey(sentence[i])) {
                        Vector<VerbInflection> data = VerbList.VerbShapes.get(sentence[i]);
                        for (VerbInflection verbInflection : data) {
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                                add = true;
                                dic.put(i, data);
                                break;
                            }
                        }

                    }
                    if (!add) {
                        dic.put(i, null);
                    }
                }
                if (dic.get(i) == null) {
                    boolean find = false;
                    if (sentence[i].startsWith("می")) {
                        String newSen = "می‌" + sentence[i].substring(2);
                        if (VerbList.VerbShapes.containsKey(newSen)) {
                            sentence[i] = newSen;
                            find = true;
                            dic.put(i, VerbList.VerbShapes.get(newSen));
                        }
                    } else if (sentence[i].startsWith("نمی")) {
                        String newSen = "نمی‌" + sentence[i].substring(2);
                        if (VerbList.VerbShapes.containsKey(newSen)) {
                            sentence[i] = newSen;
                            find = true;
                            dic.put(i, VerbList.VerbShapes.get(newSen));
                        }
                    } else if (sentence[i].contains("ئی")) {
                        String newSen = sentence[i].replace("ئی", "یی");
                        if (VerbList.VerbShapes.containsKey(newSen)) {
                            sentence[i] = newSen;
                            find = true;
                            dic.put(i, VerbList.VerbShapes.get(newSen));
                        }
                    }

                    if (!find) {
                        if (!StaticDic.containsKey(sentence[i])) {
                            StaticDic.put(sentence[i], 1);
                        } else {
                            StaticDic.put(sentence[i], StaticDic.get(sentence[i] + 1));
                        }
                    }
                }
            } else {
                dic.put(i, null);
            }
        }
        return dic;
    }

    /**
     * retrieves verb Strings from dictionary
     */
    private static HashMap<Integer, Vector<VerbInflection>> GetVerbParts(String[] sentence) throws IOException {
        if (ConjVector.size() == 0)
            ConstructConjVector();
        HashMap<Integer, Vector<VerbInflection>> dic = new HashMap<Integer, Vector<VerbInflection>>();
        if (verbDic == null) {
            verbDic = new VerbList(verbDicPath);
        }
        for (int i = 0; i < sentence.length; i++) {
            dic.put(i, VerbList.VerbShapes.containsKey(sentence[i]) ? VerbList.VerbShapes.get(sentence[i]) : null);
        }
        return dic;
    }

    /**
     * reset the verb dictionary according to the verb dictionary path
     */
    private static void ResetDicPath(String newDicPath) throws IOException {
        if (!newDicPath.equals(verbDicPath)) {
            verbDicPath = newDicPath;
            verbDic = new VerbList(verbDicPath);
        }
    }

    /**
     * finds simple and prefix verbs (do not consider compound verbs)
     */
    public static HashMap<Integer, KeyValuePair<String, Object>> AnalyzeSentence(String[] sentence, String[] posSentence, String[] posTokens, String[] lemmas, String[] outlemmas) throws IOException {
        HashMap<Integer, KeyValuePair<String, Object>> bestDic = new HashMap<Integer, KeyValuePair<String, Object>>();
        HashMap<Integer, KeyValuePair<String, VerbInflection>> initDic = GetVerbTokens(sentence, posSentence, posTokens, lemmas, outlemmas);

        HashMap<Integer, Integer> mostamars = new HashMap<Integer, Integer>();
        for (int i = 0; i < initDic.size(); i++) {
            if (initDic.get(i).getValue() != null) {
                VerbInflection verbInflection = initDic.get(i).getValue();

                if (verbInflection.VerbRoot.Type == VerbType.SADEH &&
                        verbInflection.ZamirPeyvasteh == AttachedPronounType.AttachedPronoun_NONE &&
                        verbInflection.Positivity == TensePositivity.POSITIVE &&
                        (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH || verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI) &&
                        verbInflection.VerbRoot.PastTenseRoot.equals("داشت")) {
                    int key = i;
                    int value = -1;
                    for (int j = i + 1; j < initDic.size(); j++) {
                        if (initDic.get(i).getValue() != null) {
                            VerbInflection newinfl = initDic.get(i).getValue();
                            if (newinfl.Positivity == TensePositivity.POSITIVE &&
                                    newinfl.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    !newinfl.VerbRoot.PastTenseRoot.equals("داشت") && !newinfl.VerbRoot.PresentTenseRoot.equals("است") && !newinfl.VerbRoot.PresentTenseRoot.equals("هست")) {
                                value = j;
                                break;
                            } else
                                break;
                        }
                        if (ConjVector.contains(sentence[j]))
                            break;
                    }
                    if (value > 0) {
                        mostamars.put(key, value);
                    }
                }

                if (verbInflection.VerbRoot.Type == VerbType.SADEH &&
                        verbInflection.ZamirPeyvasteh == AttachedPronounType.AttachedPronoun_NONE &&
                        verbInflection.Positivity == TensePositivity.POSITIVE &&
                        verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                        verbInflection.VerbRoot.PastTenseRoot.equals("داشت")) {
                    int key = i;
                    int value = -1;
                    for (int j = i + 1; j < initDic.size(); j++) {
                        if (initDic.get(j).getValue() != null) {
                            VerbInflection newinfl = initDic.get(j).getValue();

                            if (newinfl.Positivity == TensePositivity.POSITIVE &&
                                    newinfl.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    !newinfl.VerbRoot.PastTenseRoot.equals("داشت")) {
                                value = j;
                                break;
                            } else
                                break;

                        }
                        if (ConjVector.contains(sentence[j]))
                            break;
                    }
                    if (value > 0) {
                        mostamars.put(key, value);
                    }
                }
            }
        }
        for (int i = 0; i < initDic.size(); i++) {
            if (initDic.get(i).getValue() != null) {
                if (mostamars.containsKey(i)) {
                    MostamarSaz mostamarVal = new MostamarSaz((VerbInflection) initDic.get(i).getValue(), -1, "");
                    if (initDic.get(i).getValue().TenseForm == TenseFormationType.HAAL_SAADEH || initDic.get(i).getValue().TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                        mostamarVal = new MostamarSaz((VerbInflection) initDic.get(i).getValue(), mostamars.get(i), "MOSTAMAR_SAAZ_HAAL");
                    }
                    if (initDic.get(i).getValue().TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                        mostamarVal = new MostamarSaz((VerbInflection) initDic.get(i).getValue(), mostamars.get(i), "MOSTAMAR_SAAZ_GOZASHTEH");

                    }
                    bestDic.put(i, new KeyValuePair<String, Object>(initDic.get(i).getKey(), mostamarVal));
                } else {
                    bestDic.put(i, new KeyValuePair<String, Object>(initDic.get(i).getKey(), initDic.get(i).getValue()));

                }
            } else {
                bestDic.put(i, new KeyValuePair<String, Object>(initDic.get(i).getKey(), initDic.get(i).getValue()));
            }
        }
        return bestDic;
    }

    /**
     * finds simple and prefix verbs (do not consider compound verbs)
     */
    public static HashMap<Integer, KeyValuePair<String, Object>> AnalyzeSentence(String[] sentence) throws IOException {
        HashMap<Integer, KeyValuePair<String, Object>> bestDic = new HashMap<Integer, KeyValuePair<String, Object>>();
        HashMap<Integer, KeyValuePair<String, VerbInflection>> initDic = GetVerbTokens(sentence);

        HashMap<Integer, Integer> mostamars = new HashMap<Integer, Integer>();
        for (int i = 0; i < initDic.size(); i++) {
            if (initDic.get(i).getValue() != null) {
                VerbInflection verbInflection = initDic.get(i).getValue();

                if (verbInflection.VerbRoot.Type == VerbType.SADEH &&
                        verbInflection.ZamirPeyvasteh == AttachedPronounType.AttachedPronoun_NONE &&
                        verbInflection.Positivity == TensePositivity.POSITIVE &&
                        (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH || verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI) &&
                        verbInflection.VerbRoot.PastTenseRoot.equals("داشت")) {
                    int key = i;
                    int value = -1;
                    for (int j = i + 1; j < initDic.size(); j++) {
                        if (initDic.get(j).getValue() != null) {
                            VerbInflection newinfl = initDic.get(j).getValue();
                            if (newinfl.Positivity == TensePositivity.POSITIVE &&
                                    (newinfl.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI) &&
                                    !newinfl.VerbRoot.PastTenseRoot.equals("داشت") && !newinfl.VerbRoot.PresentTenseRoot.equals("است") && !newinfl.VerbRoot.PresentTenseRoot.equals("هست")) {
                                value = j;
                                break;
                            } else
                                break;
                        }
                        if (ConjVector.contains(sentence[j]))
                            break;
                    }
                    if (value > 0) {
                        mostamars.put(key, value);
                    }
                }

                if (verbInflection.VerbRoot.Type == VerbType.SADEH &&
                        verbInflection.ZamirPeyvasteh == AttachedPronounType.AttachedPronoun_NONE &&
                        verbInflection.Positivity == TensePositivity.POSITIVE &&
                        verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                        verbInflection.VerbRoot.PastTenseRoot.equals("داشت")) {
                    int key = i;
                    int value = -1;
                    for (int j = i + 1; j < initDic.size(); j++) {
                        if (initDic.get(j).getValue() != null) {
                            VerbInflection newinfl = initDic.get(j).getValue();

                            if (newinfl.Positivity == TensePositivity.POSITIVE &&
                                    newinfl.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    !newinfl.VerbRoot.PastTenseRoot.equals("داشت")) {
                                value = j;
                                break;
                            } else
                                break;
                        }
                        if (ConjVector.contains(sentence[j]))
                            break;
                    }
                    if (value > 0) {
                        mostamars.put(key, value);
                    }
                }
            }
        }
        for (int i = 0; i < initDic.size(); i++) {
            if (initDic.get(i).getValue() != null) {
                if (mostamars.containsKey(i)) {
                    MostamarSaz mostamarVal = new MostamarSaz((VerbInflection) initDic.get(i).getValue(), -1, "");
                    if (initDic.get(i).getValue().TenseForm == TenseFormationType.HAAL_SAADEH || initDic.get(i).getValue().TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                        mostamarVal = new MostamarSaz((VerbInflection) initDic.get(i).getValue(), mostamars.get(i), "MOSTAMAR_SAAZ_HAAL");
                    }
                    if (initDic.get(i).getValue().TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                        mostamarVal = new MostamarSaz((VerbInflection) initDic.get(i).getValue(), mostamars.get(i), "MOSTAMAR_SAAZ_GOZASHTEH");

                    }
                    bestDic.put(i, new KeyValuePair<String, Object>(initDic.get(i).getKey(), mostamarVal));
                } else {
                    bestDic.put(i, new KeyValuePair<String, Object>(initDic.get(i).getKey(), initDic.get(i).getValue()));

                }
            } else {
                bestDic.put(i, new KeyValuePair<String, Object>(initDic.get(i).getKey(), initDic.get(i).getValue()));
            }
        }
        return bestDic;
    }

    /**
     * creates an Object set of words,verbs and their inflections
     */
    public static HashMap<Integer, KeyValuePair<String, Object>> AnalyzeSentenceConsiderCompoundVerbs(String[] sentence, String[] posSentence, String[] posTokens, String[] lemmas) throws IOException {
        String[] outlemmas = new String[2];
        HashMap<Integer, KeyValuePair<String, Object>> bestDic = AnalyzeSentence(sentence, posSentence, posTokens, lemmas, outlemmas);
        for (int i = posTokens.length - 1; i >= 0; i--) {
            if (bestDic.get(i).getValue() instanceof VerbInflection) {
                boolean ispassive = false;
                Verb verbValue = ((VerbInflection) bestDic.get(i).getValue()).VerbRoot;
                if (((VerbInflection) bestDic.get(i).getValue()).Passivity == TensePassivity.PASSIVE)
                    ispassive = true;
                if (VerbList.CompoundVerbDic.containsKey(verbValue)) {
                    for (int j = i - 1; j >= 0; j--) {
                        if (posTokens[j].equals("DET")) {

                            continue;
                        }
                        if (posTokens[j].equals("N")) {

                            if (VerbList.CompoundVerbDic.get(verbValue).containsKey(bestDic.get(j).getKey())) {
                                if (j > 0 &&
                                        VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).containsKey(
                                                bestDic.get(j - 1).getKey())) {

                                    if (!ispassive || (ispassive && VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).get(bestDic.get(j - 1).getKey()))) {
                                        KeyValuePair<String, Object> item1 = new KeyValuePair<String, Object>(bestDic.get(j).getKey(),
                                                new KeyValuePair<String, Integer>(
                                                        "NON-VERBAL-ELEMENT", i));
                                        bestDic.put(j, item1);
                                        KeyValuePair<String, Object> item2 = new KeyValuePair<String, Object>(bestDic.get(j - 1).getKey(),
                                                new KeyValuePair<String, Integer>(
                                                        "VERBAL-PREPOSIOTION", i));
                                        bestDic.put(j - 1, item2);
                                        i = j - 2;
                                        break;
                                    }

                                } else if ((VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).containsKey("") ||
                                        VerbList.CompoundVerbDic.get(verbValue).get(outlemmas[j]).containsKey("")) &&
                                        !posTokens[j].equals("P")) {
                                    if (j <= 1 || !(j > 1 && (VerbList.CompoundVerbDic.get(verbValue).containsKey(bestDic.get(j - 1).getKey()) && (VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j - 1).getKey())).containsKey("")))) {
                                        if (!ispassive || (ispassive && (VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).get(""))))    //todo strange condition
                                        {
                                            KeyValuePair<String, Object> item1 = new KeyValuePair<String, Object>(bestDic.get(j).getKey(),
                                                    new KeyValuePair
                                                            <String, Integer>(
                                                            "NON-VERBAL-ELEMENT", i));
                                            bestDic.put(j, item1);
                                            i = j - 1;
                                            break;
                                        }
                                    }
                                }
                            } else if (VerbList.CompoundVerbDic.get(verbValue).containsKey(outlemmas[j])) {
                                if (VerbList.CompoundVerbDic.get(verbValue).get(outlemmas[j]).containsKey("")) {
                                    if (VerbList.CompoundVerbDic.get(verbValue).get(outlemmas[j]).containsKey("") &&
                                            !posTokens[j].equals("P")) {
                                        if (!ispassive || (ispassive && VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).get(""))) //strange condition
                                        {
                                            KeyValuePair item1 = new KeyValuePair<String, Object>(bestDic.get(j).getKey(),
                                                    new KeyValuePair
                                                            <String, Integer>(
                                                            "NON-VERBAL-ELEMENT", i));
                                            bestDic.put(j, item1);
                                            i = j - 1;
                                            break;
                                        }
                                    }
                                }
                            }
                        } else if (posTokens[j].equals("V") || posTokens[j].equals("PUNC") || posTokens[j].equals("ADV") ||
                                posTokens[j].equals("POSTP") || sentence[j].equals("را")) {
                            i = j - 1;
                            if (posTokens[j].equals("V"))
                                i = j + 1;
                            break;
                        }
                    }
                }
            } else if (bestDic.get(i).getValue() instanceof MostamarSaz) {
                MostamarSaz thisValue = (MostamarSaz) (bestDic.get(i).getValue());
                if (thisValue.Type.equals("MOSTAMAR_SAAZ_HAAL") || thisValue.Type.equals("MOSTAMAR_SAAZ_GOZASHTEH")) {
                    Verb verbValue = thisValue.Inflection.VerbRoot;
                    boolean ispassive = false;
                    if (thisValue.Inflection.Passivity == TensePassivity.PASSIVE)
                        ispassive = true;
                    if (VerbList.CompoundVerbDic.containsKey(verbValue)) {
                        for (int j = i - 1; j >= 0; j--) {
                            if (posTokens[j].equals("DET")) {

                                continue;
                            }
                            if (posTokens[j].equals("N")) {

                                if (VerbList.CompoundVerbDic.get(verbValue).containsKey(bestDic.get(j).getKey())) {
                                    if (j > 0 &&
                                            VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).containsKey(
                                                    bestDic.get(j - 1).getKey())) {
                                        if (!ispassive || (ispassive && VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).get(bestDic.get(j - 1).getKey()))) {
                                            KeyValuePair item1 = new KeyValuePair<String, Object>(bestDic.get(j).getKey(),
                                                    new KeyValuePair<String, Integer>(
                                                            "NON-VERBAL-ELEMENT", i));
                                            bestDic.put(j, item1);
                                            KeyValuePair item2 = new KeyValuePair<String, Object>(bestDic.get(j - 1).getKey(),
                                                    new KeyValuePair<String, Integer>(
                                                            "VERBAL-PREPOSIOTION", i));
                                            bestDic.put(j - 1, item2);
                                            i = j - 2;
                                            break;
                                        }

                                    } else if ((VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).containsKey("") ||
                                            VerbList.CompoundVerbDic.get(verbValue).get(outlemmas[j]).containsKey("")) &&
                                            !posTokens[j].equals("P")) {
                                        if (j <= 1 || !(j > 1 && (VerbList.CompoundVerbDic.get(verbValue).containsKey(bestDic.get(j - 1).getKey()) && VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j - 1).getKey()).containsKey("")))) {
                                            if (!ispassive || (ispassive && VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).get(""))) {
                                                KeyValuePair<String, Object> item1 = new KeyValuePair<String, Object>(bestDic.get(j).getKey(),
                                                        new KeyValuePair
                                                                <String, Integer>(
                                                                "NON-VERBAL-ELEMENT", i));
                                                bestDic.put(j, item1);
                                                bestDic.put(i, new KeyValuePair<String, Object>(bestDic.get(i).getKey(), thisValue.Inflection));
                                                i = j - 1;
                                                break;
                                            }
                                        }
                                    }
                                } else if (VerbList.CompoundVerbDic.get(verbValue).containsKey(outlemmas[j])) {
                                    if (VerbList.CompoundVerbDic.get(verbValue).get(outlemmas[j]).containsKey("")) {
                                        if (VerbList.CompoundVerbDic.get(verbValue).get(outlemmas[j]).containsKey("") &&
                                                !posTokens[j].equals("P")) {
                                            if (!ispassive || (ispassive && VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).get(""))) {
                                                KeyValuePair<String, Object> item1 = new KeyValuePair<String, Object>(bestDic.get(j).getKey(),
                                                        new KeyValuePair
                                                                <String, Integer>(
                                                                "NON-VERBAL-ELEMENT", i));
                                                bestDic.put(j, item1);
                                                bestDic.put(i, new KeyValuePair<String, Object>(bestDic.get(i).getKey(), thisValue.Inflection));
                                                i = j - 1;
                                                break;
                                            }
                                        }
                                    }
                                }
                            } else if (posTokens[j].equals("V") || posTokens[j].equals("PUNC") || posTokens[j].equals("ADV") ||
                                    posTokens[j].equals("POSTP") || sentence[j].equals("را")) {
                                i = j - 1;
                                if (posTokens[j].equals("V"))
                                    i = j + 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return bestDic;
    }

    /**
     * creates an Object set of words, verbs and inflections of sentence words
     */
    public static HashMap<Integer, KeyValuePair<String, Object>> AnalyzeSentenceConsiderCompoundVerbs(String[] sentence) throws IOException {
        HashMap<Integer, KeyValuePair<String, Object>> bestDic = AnalyzeSentence(sentence);
        for (int i = bestDic.size() - 1; i >= 0; i--) {
            if (bestDic.get(i).getValue() instanceof VerbInflection) {
                Verb verbValue = ((VerbInflection) bestDic.get(i).getValue()).VerbRoot;
                boolean ispassive = false;
                if (((VerbInflection) bestDic.get(i).getValue()).Passivity == TensePassivity.PASSIVE)
                    ispassive = true;
                if (VerbList.CompoundVerbDic.containsKey(verbValue)) {
                    for (int j = i - 1; j >= 0; j--) {
                        if (bestDic.get(j).getValue() instanceof VerbInflection) {
                            i = j - 1;
                            break;
                        }
                        if (VerbList.CompoundVerbDic.get(verbValue).containsKey(bestDic.get(j).getKey())) {
                            if (j > 0 &&
                                    VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).containsKey(
                                            bestDic.get(j - 1).getKey())) {
                                if (!ispassive || (ispassive && VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).get(bestDic.get(j - 1).getKey()))) {
                                    KeyValuePair<String, Object> item1 = new KeyValuePair<String, Object>(bestDic.get(j).getKey(),
                                            new KeyValuePair<String, Integer>(
                                                    "NON-VERBAL-ELEMENT", i));
                                    bestDic.put(j, item1);
                                    KeyValuePair<String, Object> item2 = new KeyValuePair<String, Object>(bestDic.get(j - 1).getKey(),
                                            new KeyValuePair<String, Integer>(
                                                    "VERBAL-PREPOSIOTION", i));
                                    bestDic.put(j - 1, item2);
                                    i = j - 2;
                                    break;
                                }
                            } else if (VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).containsKey("")) {
                                if (!ispassive || (ispassive && VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).get(""))) {
                                    KeyValuePair<String, Object> item1 = new KeyValuePair<String, Object>(bestDic.get(j).getKey(),
                                            new KeyValuePair<String, Integer>(
                                                    "NON-VERBAL-ELEMENT", i));
                                    bestDic.put(j, item1);
                                    i = j - 1;
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (bestDic.get(i).getValue() instanceof MostamarSaz) {
                MostamarSaz thisValue = (MostamarSaz) (bestDic.get(i).getValue());
                if (thisValue.Type.equals("MOSTAMAR_SAAZ_HAAL") || thisValue.Type.equals("MOSTAMAR_SAAZ_GOZASHTEH")) {
                    Verb verbValue = thisValue.Inflection.VerbRoot;
                    boolean ispassive = false;
                    if (thisValue.Inflection.Passivity == TensePassivity.PASSIVE)
                        ispassive = true;
                    if (VerbList.CompoundVerbDic.containsKey(verbValue)) {
                        for (int j = i - 1; j >= 0; j--) {
                            if (bestDic.get(j).getValue() instanceof VerbInflection) {
                                i = j - 1;
                                break;
                            }
                            if (VerbList.CompoundVerbDic.get(verbValue).containsKey(bestDic.get(j).getKey())) {
                                if (j > 0 &&
                                        VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).containsKey(
                                                bestDic.get(j - 1).getKey())) {
                                    if (!ispassive || (ispassive && VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).get(bestDic.get(j - 1).getKey()))) {
                                        KeyValuePair<String, Object> item1 = new KeyValuePair<String, Object>(bestDic.get(j).getKey(),
                                                new KeyValuePair<String, Integer>(
                                                        "NON-VERBAL-ELEMENT", i));
                                        bestDic.put(j, item1);
                                        KeyValuePair<String, Object> item2 = new KeyValuePair<String, Object>(bestDic.get(j - 1).getKey(),
                                                new KeyValuePair<String, Integer>(
                                                        "VERBAL-PREPOSIOTION", i));
                                        bestDic.put(j - 1, item2);
                                        bestDic.put(i, new KeyValuePair<String, Object>(bestDic.get(i).getKey(), thisValue.Inflection));
                                        i = j - 2;
                                        break;
                                    }
                                } else if (VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).containsKey("")) {
                                    if (!ispassive || (ispassive && VerbList.CompoundVerbDic.get(verbValue).get(bestDic.get(j).getKey()).get(""))) {
                                        KeyValuePair<String, Object> item1 = new KeyValuePair<String, Object>(bestDic.get(j).getKey(),
                                                new KeyValuePair<String, Integer>(
                                                        "NON-VERBAL-ELEMENT", i));
                                        bestDic.put(j, item1);
                                        bestDic.put(i, new KeyValuePair<String, Object>(bestDic.get(i).getKey(), thisValue.Inflection));
                                        i = j - 1;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return bestDic;
    }

    /**
     * returns a partial  dependency tree
     */
    public static HashMap<Integer, KeyValuePair<String, KeyValuePair<Integer, Object>>> MakePartialTree(String[] sentence, String[] posSentence, String[] posTokens, String[] lemmas, String dicPath) throws IOException {
        ResetDicPath(dicPath);
        HashMap<Integer, KeyValuePair<String, Object>> dic = AnalyzeSentenceConsiderCompoundVerbs(sentence, posSentence, posTokens, lemmas);
        HashMap<Integer, KeyValuePair<String, KeyValuePair<Integer, Object>>> partialTree = new HashMap<Integer, KeyValuePair<String, KeyValuePair<Integer, Object>>>();
        for (int key : dic.keySet()) {
            String value = dic.get(key).getKey();
            if (dic.get(key).getValue() instanceof VerbInflection) {
                partialTree.put(key, new KeyValuePair<String, KeyValuePair<Integer, Object>>(value, new KeyValuePair<Integer, Object>(-1, dic.get(key).getValue())));

            } else if (dic.get(key).getValue() instanceof MostamarSaz) {
                MostamarSaz newValue = (MostamarSaz) dic.get(key).getValue();
                partialTree.put(key, new KeyValuePair<String, KeyValuePair<Integer, Object>>(value, new KeyValuePair<Integer, Object>(newValue.Head, newValue)));

            } else if (((KeyValuePair<String, Object>) dic.get(key)).getValue() instanceof Integer) {
                KeyValuePair<String, Integer> newValue = (KeyValuePair<String, Integer>) dic.get(key).getValue();
                if (key > 0 && ((KeyValuePair<String, Object>) dic.get(key - 1)).getValue() instanceof Integer) {
                    KeyValuePair<String, Integer> prevValue = (KeyValuePair<String, Integer>) dic.get(key - 1).getValue();
                    if (prevValue.getKey().equals("VERBAL-PREPOSIOTION"))
                        partialTree.put(key,
                                new KeyValuePair<String, KeyValuePair<Integer, Object>>(value,
                                        new KeyValuePair
                                                <Integer, Object>(
                                                key - 1,
                                                "POSDEP")));
                    else {

                        partialTree.put(key,
                                new KeyValuePair<String, KeyValuePair<Integer, Object>>(value,
                                        new KeyValuePair
                                                <Integer, Object>(
                                                newValue.getValue() - 1,
                                                newValue.getKey())));
                    }

                } else {
                    partialTree.put(key, new KeyValuePair<String, KeyValuePair<Integer, Object>>(value, new KeyValuePair<Integer, Object>(newValue.getValue(), newValue.getKey())));
                }
            } else {
                partialTree.put(key, new KeyValuePair<String, KeyValuePair<Integer, Object>>(value, new KeyValuePair<Integer, Object>(-1, "")));
            }

        }
        return partialTree;
    }

    /**
     * returns a partial  dependency tree
     */
    public static HashMap<Integer, KeyValuePair<String, KeyValuePair<Integer, Object>>> MakePartialTree(String[] sentence, String dicPath) throws IOException {
        ResetDicPath(dicPath);
        HashMap<Integer, KeyValuePair<String, Object>> dic = AnalyzeSentenceConsiderCompoundVerbs(sentence);
        HashMap<Integer, KeyValuePair<String, KeyValuePair<Integer, Object>>> partialTree = new HashMap<Integer, KeyValuePair<String, KeyValuePair<Integer, Object>>>();
        for (int key : dic.keySet()) {
            String value = dic.get(key).getKey();
            if (dic.get(key).getValue() instanceof VerbInflection) {
                partialTree.put(key, new KeyValuePair<String, KeyValuePair<Integer, Object>>(value, new KeyValuePair<Integer, Object>(-1, dic.get(key).getValue())));

            } else if (dic.get(key).getValue() instanceof MostamarSaz) {
                MostamarSaz newValue = (MostamarSaz) dic.get(key).getValue();
                partialTree.put(key, new KeyValuePair<String, KeyValuePair<Integer, Object>>(value, new KeyValuePair<Integer, Object>(newValue.Head, newValue)));

            } else if (((KeyValuePair<String, Object>) dic.get(key)).getValue() instanceof Integer) {
                KeyValuePair<String, Integer> newValue = (KeyValuePair<String, Integer>) dic.get(key).getValue();
                if (key > 0 && ((KeyValuePair<String, Object>) dic.get(key - 1)).getValue() instanceof Integer) {
                    KeyValuePair<String, Integer> prevValue = (KeyValuePair<String, Integer>) dic.get(key - 1).getValue();
                    if (prevValue.getKey().equals("VERBAL-PREPOSIOTION"))
                        partialTree.put(key,
                                new KeyValuePair<String, KeyValuePair<Integer, Object>>(value,
                                        new KeyValuePair
                                                <Integer, Object>(
                                                key - 1,
                                                "POSDEP")));
                    else {

                        partialTree.put(key,
                                new KeyValuePair<String, KeyValuePair<Integer, Object>>(value,
                                        new KeyValuePair
                                                <Integer, Object>(
                                                newValue.getValue() - 1,
                                                newValue.getKey())));
                    }

                } else {
                    partialTree.put(key, new KeyValuePair<String, KeyValuePair<Integer, Object>>(value, new KeyValuePair<Integer, Object>(newValue.getValue(), newValue.getKey())));
                }
            } else {
                partialTree.put(key, new KeyValuePair<String, KeyValuePair<Integer, Object>>(value, new KeyValuePair<Integer, Object>(-1, "")));

            }

        }
        return partialTree;
    }

    /**
     * Get a very initial data about verbal information of each token : the sentence
     */
    private static HashMap<Integer, Vector<Integer>> getVerbStates(String[] sentence, String[] posSentence) throws IOException {
        HashMap<Integer, Vector<VerbInflection>> inflectionList = GetVerbParts(sentence, posSentence);

        HashMap<Integer, Vector<Integer>> stateList = new HashMap<Integer, Vector<Integer>>();
        stateList.put(-1, new Vector<Integer>());
        stateList.get(-1).add(0);
        String tempPishvand = "";
        for (int i = 0; i < inflectionList.size(); i++) {
            stateList.put(i, new Vector<Integer>());

            Vector<VerbInflection> valuePair = inflectionList.get(i);

            if (valuePair == null) {
                if (stateList.get(i - 1).contains(6)) {
                    if (!stateList.get(i - 1).contains(38))
                        stateList.get(i - 1).add(38);
                    if( stateList.get(i - 1).contains(6))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(6));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(-1)) {
                    if (!stateList.get(i - 1).contains(48))
                        stateList.get(i - 1).add(48);
                    if( stateList.get(i - 1).contains(-1))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(-3)) {
                    if (!stateList.get(i - 1).contains(52))
                        stateList.get(i - 1).add(52);
                    if( stateList.get(i - 1).contains(-3))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-3));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(8)) {
                    if (!stateList.get(i - 1).contains(35))
                        stateList.get(i - 1).add(35);
                    if( stateList.get(i - 1).contains(8))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(8));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(5)) {
                    if (!stateList.get(i - 1).contains(35))
                        stateList.get(i - 1).add(35);
                    if( stateList.get(i - 1).contains(5))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(5));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(7)) {
                    if (!stateList.get(i - 1).contains(40))
                        stateList.get(i - 1).add(40);
                    if( stateList.get(i - 1).contains(7))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(7));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(9)) {
                    if (!stateList.get(i - 1).contains(45))
                        stateList.get(i - 1).add(45);
                    if( stateList.get(i - 1).contains(9))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(1)) {
                    if (!stateList.get(i - 1).contains(27))
                        stateList.get(i - 1).add(27);
                    if( stateList.get(i - 1).contains(1))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(4)) {
                    if (!stateList.get(i - 1).contains(34))
                        stateList.get(i - 1).add(34);
                    if( stateList.get(i - 1).contains(4))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                }
                stateList.get(i).add(0);
            } else {
                int counter = 0;
                boolean hasgozashtehsadegh = false;
                for (VerbInflection verbInflection : valuePair) {
                    if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                        hasgozashtehsadegh = true;
                        break;
                    }
                }
                for (VerbInflection verbInflection : valuePair) {
                    counter++;

                        /*
                         * State -3:
                         * State -2:
                         * State -1:
                         * State 0: initial form
                         * State 1: "Payeh Mafooli"
                         * State 2: "Xahaed"
                         * State 3: "Payeh Mafooli" + "Xahaed"
                         * State 4: "Payeh Mafooli"
                         * State 5: "Payeh Mafooli" + "Shodeh"
                         * State 6: "Payeh Mafooli"
                         * State 7: "Payeh Mafooli" + "Shodeh"
                         * State 8: "Payeh Mafooli" + "Shodeh"
                         * State 9: "Payeh Mafooli" + "Shodeh"
                         */

                    /// state 0

                    if (stateList.get(i - 1).contains(0) || (stateList.get(i - 1).size() == 0 && stateList.get(i).size() == 0) || (stateList.get(i - 1).size() > 0 && stateList.get(i - 1).get(0) > 9)) {
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(10))
                                stateList.get(i).add(10);
                        }

                        if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(14))
                                stateList.get(i).add(14);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(15))
                                stateList.get(i).add(15);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(17))
                                stateList.get(i).add(17);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(18))
                                stateList.get(i).add(18);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(20))
                                stateList.get(i).add(20);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                            if (!stateList.get(i).contains(46))
                                stateList.get(i).add(46);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                            if (!stateList.get(i).contains(21))
                                stateList.get(i).add(21);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR) {
                            if (!stateList.get(i).contains(6))
                                stateList.get(i).add(6);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR) {
                            if (!stateList.get(i).contains(3) && !stateList.get(i).contains(5) && !stateList.get(i).contains(7)) {
                                if (!stateList.get(i).contains(-1))
                                    stateList.get(i).add(-1);
                            }

                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد") && verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                            if (!stateList.get(i).contains(47))
                                stateList.get(i).add(47);
                        }

                        if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(16))
                                stateList.get(i).add(16);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(19))
                                stateList.get(i).add(19);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("خواست")) {
                            if (!stateList.get(i).contains(2))
                                stateList.get(i).add(2);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                (verbInflection.VerbRoot.PastTenseRoot.equals("بایست") || verbInflection.VerbRoot.PresentTenseRoot.equals("توان"))) {
                            if (!stateList.get(i).contains(53))
                                stateList.get(i).add(53);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                verbInflection.VerbRoot.Type == VerbType.AYANDEH_PISHVANDI) {
                            if (!stateList.get(i).contains(-2))
                                stateList.get(i).add(-2);
                            tempPishvand = verbInflection.VerbRoot.Prefix;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(11))
                                stateList.get(i).add(11);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(3) && !stateList.get(i).contains(5) && !stateList.get(i).contains(7)) {
                                if (!stateList.get(i).contains(12))
                                    stateList.get(i).add(12);
                            }
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(13))
                                stateList.get(i).add(13);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(1))
                                stateList.get(i).add(1);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(9))
                                stateList.get(i).add(9);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Positivity == TensePositivity.NEGATIVE) {
                            if (!stateList.get(i).contains(4))
                                stateList.get(i).add(4);
                        }
                    }


                    /// state 1

                    if (stateList.get(i - 1).contains(1)) {
                        boolean find1 = false;
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(22))
                                stateList.get(i).add(22);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(23))
                                stateList.get(i).add(23);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(24))
                                stateList.get(i).add(24);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(25))
                                stateList.get(i).add(25);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(26))
                                stateList.get(i).add(26);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(29))
                                stateList.get(i).add(29);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(30))
                                stateList.get(i).add(30);
                            find1 = true;
                        }
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.Positivity == TensePositivity.POSITIVE &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(27))
                                stateList.get(i).add(27);
                            find1 = true;
                        }
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("هست") &&
                                verbInflection.Positivity == TensePositivity.NEGATIVE &&
                                verbInflection.ZamirPeyvasteh == AttachedPronounType.AttachedPronoun_NONE &&
                                verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(27))
                                stateList.get(i).add(27);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(5))
                                stateList.get(i).add(5);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH && verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(-3))
                                stateList.get(i).add(-3);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH && verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(51))
                                stateList.get(i).add(51);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(7))
                                stateList.get(i).add(7);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(28))
                                stateList.get(i).add(28);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("خواست") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(3))
                                stateList.get(i).add(3);
                            find1 = true;
                        }
                        if (!find1) {
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    (verbInflection.VerbRoot.PastTenseRoot.equals("کرد") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("گشت") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("نمود") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("فرمود") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("ساخت")) &&
                                    verbInflection.VerbRoot.Type == VerbType.SADEH) {
                                if (!stateList.get(i).contains(17))
                                    stateList.get(i).add(17);
                                if( stateList.get(i - 1).contains(1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(0);
                            } else if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                    (verbInflection.VerbRoot.PastTenseRoot.equals("کرد") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("گشت") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("نمود") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("فرمود") || verbInflection.VerbRoot.PastTenseRoot.equals("ساخت")) &&
                                    verbInflection.VerbRoot.Type == VerbType.SADEH) {
                                if (!stateList.get(i).contains(14))
                                    stateList.get(i).add(14);
                                if( stateList.get(i - 1).contains(1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(0);
                            } else if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    (verbInflection.VerbRoot.PastTenseRoot.equals("کرد") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("گشت") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("نمود") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("فرمود") || verbInflection.VerbRoot.PastTenseRoot.equals("ساخت")) &&
                                    verbInflection.VerbRoot.Type == VerbType.SADEH) {
                                if (!stateList.get(i).contains(10))
                                    stateList.get(i).add(10);
                                if( stateList.get(i - 1).contains(1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(0);
                            } else if (counter == valuePair.size()) {
                                if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!(verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                            verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                            verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                            verbInflection.Positivity == TensePositivity.POSITIVE)) {
                                        if (!stateList.get(i).contains(10))
                                            stateList.get(i).add(10);
                                        if( stateList.get(i - 1).contains(1))
                                            stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                        stateList.get(i - 1).add(27);
                                    }
                                }
                                if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!(verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                            verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                            verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                            verbInflection.Positivity == TensePositivity.POSITIVE)) {
                                        if (!stateList.get(i).contains(14))
                                            stateList.get(i).add(14);
                                        if( stateList.get(i - 1).contains(1))
                                            stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                        stateList.get(i - 1).add(27);
                                    }
                                }
                                if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(15))
                                        stateList.get(i).add(15);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(17))
                                        stateList.get(i).add(17);
                                    if (
                                            !(verbInflection.VerbRoot.PastTenseRoot.equals("کرد") &&
                                                    verbInflection.VerbRoot.Type == VerbType.SADEH)) {

                                        if( stateList.get(i - 1).contains(1))
                                            stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                        stateList.get(i - 1).add(27);
                                    }
                                }
                                if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(18))
                                        stateList.get(i).add(18);
                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(20))
                                        stateList.get(i).add(20);
                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm ==
                                        TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                        verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                    if (!stateList.get(i).contains(21))
                                        stateList.get(i).add(21);
                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm ==
                                        TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                        verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                        verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                    if (!stateList.get(i).contains(6))
                                        stateList.get(i).add(6);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }

                                if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                        verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(16))
                                        stateList.get(i).add(16);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                        verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(19))
                                        stateList.get(i).add(19);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                        verbInflection.VerbRoot.PastTenseRoot.equals("خواست")) {
                                    if (!stateList.get(i).contains(2))
                                        stateList.get(i).add(2);
                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);

                                }
                                if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                        verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(11))
                                        stateList.get(i).add(11);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                        verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(13))
                                        stateList.get(i).add(13);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);

                                }
                                if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                        verbInflection.Positivity == TensePositivity.POSITIVE) {
                                    if (!stateList.get(i).contains(1))
                                        stateList.get(i).add(1);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                        verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(9))
                                        stateList.get(i).add(9);
                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);

                                }
                                if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                        verbInflection.Positivity == TensePositivity.NEGATIVE) {
                                    if (!stateList.get(i).contains(4))
                                        stateList.get(i).add(4);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                            }
                        } else {
                            if( stateList.get(i - 1).contains(1))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                        }
                    }


                    /// state 5

                    if (stateList.get(i - 1).contains(5)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(35))
                                stateList.get(i).add(35);
                            if( stateList.get(i - 1).contains(5))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(5));  //todo different from C#
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        }
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("هست") &&
                                verbInflection.Positivity == TensePositivity.NEGATIVE &&
                                verbInflection.ZamirPeyvasteh == AttachedPronounType.AttachedPronoun_NONE &&
                                verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(35))
                                stateList.get(i).add(35);
                            if( stateList.get(i - 1).contains(5))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(5));  //todo different from C#
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(36))
                                stateList.get(i).add(36);
                            if( stateList.get(i - 1).contains(5))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(5));  //todo different from C#
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        }
                        if ((verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH || verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI) &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(37))
                                stateList.get(i).add(37);
                            if( stateList.get(i - 1).contains(5))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(5));  //todo different from C#
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        }
                    }


                    /// state 2

                    if (stateList.get(i - 1).contains(2)) {
                        if (verbInflection.IsPayehFelMasdari() && !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH && verbInflection.Positivity == TensePositivity.POSITIVE)//ToCheck
                        {
                            stateList.get(i).clear();
                            if (!stateList.get(i).contains(31))
                                stateList.get(i).add(31);
                            stateList.get(i - 1).clear();
                        }
                        if (verbInflection.IsPayehFelMasdari() && verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            stateList.get(i).clear();
                            if (!stateList.get(i).contains(32))
                                stateList.get(i).add(32);
                            stateList.get(i - 1).clear();
                        }
                    }


                    /// state -2

                    if (stateList.get(i - 1).contains(-2)) {
                        if (verbInflection.IsPayehFelMasdari() && !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH && VerbList.VerbPishvandiDic.get(tempPishvand).contains(verbInflection.VerbRoot.PastTenseRoot + "|" + verbInflection.VerbRoot.PresentTenseRoot)) {
                            stateList.get(i).clear();
                            if (!stateList.get(i).contains(31))
                                stateList.get(i).add(31);
                            stateList.get(i - 1).clear();
                        } else if (verbInflection.IsPayehFelMasdari() && verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH && VerbList.VerbPishvandiDic.get(tempPishvand).contains(verbInflection.VerbRoot.PastTenseRoot + "|" + verbInflection.VerbRoot.PresentTenseRoot)) {
                            stateList.get(i).clear();
                            if (!stateList.get(i).contains(32))
                                stateList.get(i).add(32);
                            stateList.get(i - 1).clear();
                        } else {
                            stateList.get(i - 1).clear();
                            stateList.get(i).clear();
                            stateList.get(i - 1).add(0);
                            if (hasgozashtehsadegh)
                                stateList.get(i - 1).add(-2);
                            stateList.get(i).add(0);
                        }
                    }


                    /// state -3

                    if (stateList.get(i - 1).contains(-3)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(52))
                                stateList.get(i).add(52);
                            if( stateList.get(i - 1).contains(-3))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-3));  //todo different from C#
                        }
                    }


                    /// state 3

                    if (stateList.get(i - 1).contains(3)) {
                        if (verbInflection.IsPayehFelMasdari() && verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            stateList.get(i).clear();
                            if (!stateList.get(i).contains(33))
                                stateList.get(i).add(33);
                            stateList.get(i - 1).clear();
                        }
                    }


                    /// state 4

                    if (stateList.get(i - 1).contains(4)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(34))
                                stateList.get(i).add(34);
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                        }
                        if (verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(41))
                                stateList.get(i).add(41);
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                        }
                        if (verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(42))
                                stateList.get(i).add(42);
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                        }
                        if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH && verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(-3))
                                stateList.get(i).add(-3);
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH && verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(51))
                                stateList.get(i).add(51);
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                        }
                        if (verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            if (!stateList.get(i).contains(8))
                                stateList.get(i).add(8);
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!(verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                    verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                    verbInflection.Positivity == TensePositivity.POSITIVE)) {
                                if (!stateList.get(i).contains(10))
                                    stateList.get(i).add(10);

                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                        }

                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                (verbInflection.VerbRoot.PastTenseRoot.equals("کرد") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("گشت") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("نمود") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("فرمود") || verbInflection.VerbRoot.PastTenseRoot.equals("ساخت")) &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(17))
                                stateList.get(i).add(17);
                            if( stateList.get(i - 1).contains(1))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                            stateList.get(i - 1).add(0);
                        } else if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                (verbInflection.VerbRoot.PastTenseRoot.equals("کرد") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("گشت") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("نمود") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("فرمود") || verbInflection.VerbRoot.PastTenseRoot.equals("ساخت")) &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(14))
                                stateList.get(i).add(14);
                            if( stateList.get(i - 1).contains(1))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                            stateList.get(i - 1).add(0);
                        } else if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                (verbInflection.VerbRoot.PastTenseRoot.equals("کرد") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("گشت") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("نمود") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("فرمود") || verbInflection.VerbRoot.PastTenseRoot.equals("ساخت")) &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(10))
                                stateList.get(i).add(10);
                            if( stateList.get(i - 1).contains(1))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                            stateList.get(i - 1).add(0);
                        } else if (stateList.get(i - 1).contains(4)) {
                            if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(15))
                                    stateList.get(i).add(15);

                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(14))
                                    stateList.get(i).add(14);

                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(17))
                                    stateList.get(i).add(17);
                                if (
                                        !(verbInflection.VerbRoot.PastTenseRoot.equals("کرد") &&
                                                verbInflection.VerbRoot.Type == VerbType.SADEH)) {
                                    if( stateList.get(i - 1).contains(4))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                    stateList.get(i - 1).add(34);
                                }
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(18))
                                    stateList.get(i).add(18);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(20))
                                    stateList.get(i).add(20);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }

                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(21))
                                    stateList.get(i).add(21);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(6))
                                    stateList.get(i).add(6);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }

                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(47))
                                    stateList.get(i).add(47);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(16))
                                    stateList.get(i).add(16);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(19))
                                    stateList.get(i).add(19);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("خواست")) {
                                if (!stateList.get(i).contains(2))
                                    stateList.get(i).add(2);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(11))
                                    stateList.get(i).add(11);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }

                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(13))
                                    stateList.get(i).add(13);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Positivity == TensePositivity.POSITIVE) {
                                if (!stateList.get(i).contains(1))
                                    stateList.get(i).add(1);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(9))
                                    stateList.get(i).add(9);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Positivity == TensePositivity.NEGATIVE) {
                                if (!stateList.get(i).contains(4))
                                    stateList.get(i).add(4);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                        }
                    }

                    /// state 6
                    if (stateList.get(i - 1).contains(6)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(39))
                                stateList.get(i).add(39);
                            if( stateList.get(i - 1).contains(6))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(6));  //todo different from C#
                        }
                    }

                    /// state 7
                    if (stateList.get(i - 1).contains(7)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(40))
                                stateList.get(i).add(40);
                            if( stateList.get(i - 1).contains(7))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(7));  //todo different from C#
                        }

                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("هست") &&
                                verbInflection.Positivity == TensePositivity.NEGATIVE &&
                                verbInflection.ZamirPeyvasteh == AttachedPronounType.AttachedPronoun_NONE &&
                                verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(40))
                                stateList.get(i).add(40);
                            if( stateList.get(i - 1).contains(7))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(7));  //todo different from C#
                        }
                    }


                    /// state 8

                    if (stateList.get(i - 1).contains(8)) {
                        if (verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(43))
                                stateList.get(i).add(43);
                            if( stateList.get(i - 1).contains(8))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(8));  //todo different from C#
                        }
                        if (verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(44))
                                stateList.get(i).add(44);
                            if( stateList.get(i - 1).contains(8))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(8));  //todo different from C#
                        }
                    }


                    /// state 9

                    if (stateList.get(i - 1).contains(9)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(45))
                                stateList.get(i).add(45);
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        } else if (verbInflection.VerbRoot.PresentTenseRoot.equals("باش") &&
                                verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(49))
                                stateList.get(i).add(49);
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        } else if (verbInflection.VerbRoot.PresentTenseRoot.equals("باش") &&
                                verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(50))
                                stateList.get(i).add(50);
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        } else {

                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!(verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                        verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                        verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                        verbInflection.Positivity == TensePositivity.POSITIVE)) {
                                    if (!stateList.get(i).contains(10))
                                        stateList.get(i).add(10);
                                    if( stateList.get(i - 1).contains(9))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                    stateList.get(i - 1).add(45);
                                }
                            }

                            if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(14))
                                    stateList.get(i).add(14);
                                if (stateList.get(i - 1).contains(9)) {
                                    stateList.get(i - 1).remove(9);
                                    stateList.get(i - 1).add(45);
                                }

                            }
                            if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(15))
                                    stateList.get(i).add(15);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(17))
                                    stateList.get(i).add(17);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(18))
                                    stateList.get(i).add(18);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(20))
                                    stateList.get(i).add(20);
                                stateList.get(i - 1).remove(9);
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(46))
                                    stateList.get(i).add(46);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(21))
                                    stateList.get(i).add(21);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }

                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                    verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(-1))
                                    stateList.get(i).add(-1);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }

                            if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(16))
                                    stateList.get(i).add(16);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(19))
                                    stateList.get(i).add(19);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("خواست")) {
                                if (!stateList.get(i).contains(2))
                                    stateList.get(i).add(2);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(11))
                                    stateList.get(i).add(11);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }

                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(13))
                                    stateList.get(i).add(13);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Positivity == TensePositivity.POSITIVE) {
                                if (!stateList.get(i).contains(1))
                                    stateList.get(i).add(1);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(9))
                                    stateList.get(i).add(9);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Positivity == TensePositivity.NEGATIVE) {
                                if (!stateList.get(i).contains(4))
                                    stateList.get(i).add(4);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (stateList.get(i - 1).contains(9)) {
                                if (!stateList.get(i - 1).contains(45))
                                    stateList.get(i - 1).add(45);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                            }
                        }
                    }


                    /// state -1

                    if (stateList.get(i - 1).contains(-1)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(48))
                                stateList.get(i).add(48);
                            if( stateList.get(i - 1).contains(-1))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                        } else {
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!(verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                        verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                        verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                        verbInflection.Positivity == TensePositivity.POSITIVE)) {
                                    if (!stateList.get(i).contains(10))
                                        stateList.get(i).add(10);
                                    if( stateList.get(i - 1).contains(-1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                    stateList.get(i - 1).add(48);
                                }
                            }

                            if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(14))
                                    stateList.get(i).add(14);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(15))
                                    stateList.get(i).add(15);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);

                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(17))
                                    stateList.get(i).add(17);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(18))
                                    stateList.get(i).add(18);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(20))
                                    stateList.get(i).add(20);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(46))
                                    stateList.get(i).add(46);

                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(21))
                                    stateList.get(i).add(21);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(6))
                                    stateList.get(i).add(6);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                    verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(-1))
                                    stateList.get(i).add(-1);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);

                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(47))
                                    stateList.get(i).add(47);

                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(16))
                                    stateList.get(i).add(16);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(19))
                                    stateList.get(i).add(19);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("خواست")) {
                                if (!stateList.get(i).contains(2))
                                    stateList.get(i).add(2);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(11))
                                    stateList.get(i).add(11);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(12))
                                    stateList.get(i).add(12);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(13))
                                    stateList.get(i).add(13);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Positivity == TensePositivity.POSITIVE) {
                                if (!stateList.get(i).contains(1))
                                    stateList.get(i).add(1);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Positivity == TensePositivity.NEGATIVE) {
                                if (!stateList.get(i).contains(4))
                                    stateList.get(i).add(4);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if( stateList.get(i - 1).contains(-1))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                        }
                    }


                }
                if (stateList.get(i).size() == 0) {
                    stateList.get(i).add(0);
                }
                if (!stateList.get(i).contains(-2))
                    tempPishvand = "";
            }
        }
        return stateList;
    }

    /**
     * Get a very initial data about verbal information of each token : the sentence
     */
    private static HashMap<Integer, Vector<Integer>> getVerbStates(String[] sentence) throws IOException {
        HashMap<Integer, Vector<VerbInflection>> inflectionVector = GetVerbParts(sentence);

        HashMap<Integer, Vector<Integer>> stateList = new HashMap<Integer, Vector<Integer>>();
        stateList.put(-1, new Vector<Integer>());
        stateList.get(-1).add(0);
        String tempPishvand = "";
        for (int i = 0; i < inflectionVector.size(); i++) {
            stateList.put(i, new Vector<Integer>());

            Vector<VerbInflection> valuePair = inflectionVector.get(i);

            if (valuePair == null) {
                if (stateList.get(i - 1).contains(6)) {
                    if (!stateList.get(i - 1).contains(38))
                        stateList.get(i - 1).add(38);
                    if( stateList.get(i - 1).contains(6))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(6));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(-1)) {
                    if (!stateList.get(i - 1).contains(48))
                        stateList.get(i - 1).add(48);
                    if( stateList.get(i - 1).contains(-1))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(-3)) {
                    if (!stateList.get(i - 1).contains(52))
                        stateList.get(i - 1).add(52);
                    if( stateList.get(i - 1).contains(-3))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-3));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(8)) {
                    if (!stateList.get(i - 1).contains(35))
                        stateList.get(i - 1).add(35);
                    if( stateList.get(i - 1).contains(8))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(8));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(5)) {
                    if (!stateList.get(i - 1).contains(35))
                        stateList.get(i - 1).add(35);
                    if( stateList.get(i - 1).contains(5))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(5));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(7)) {
                    if (!stateList.get(i - 1).contains(40))
                        stateList.get(i - 1).add(40);
                    if( stateList.get(i - 1).contains(7))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(7));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(9)) {
                    if (!stateList.get(i - 1).contains(45))
                        stateList.get(i - 1).add(45);
                    if( stateList.get(i - 1).contains(9))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(1)) {
                    if (!stateList.get(i - 1).contains(27))
                        stateList.get(i - 1).add(27);
                    if( stateList.get(i - 1).contains(1))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                }
                if (stateList.get(i - 1).contains(4)) {
                    if (!stateList.get(i - 1).contains(34))
                        stateList.get(i - 1).add(34);
                    if( stateList.get(i - 1).contains(4))
                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                }
                stateList.get(i).add(0);
            } else {
                int counter = 0;
                boolean hasGozashtehSadeh = false;
                for (VerbInflection verbInflection : valuePair) {
                    if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                        hasGozashtehSadeh = true;
                        break;
                    }
                }
                for (VerbInflection verbInflection : valuePair) {
                    counter++;

                        /*
                         * State -3:
                         * State -2:
                         * State -1:
                         * State 0: initial form
                         * State 1: "Payeh Mafooli"
                         * State 2: "Xahaed"
                         * State 3: "Payeh Mafooli" + "Xahaed"
                         * State 4: "Payeh Mafooli"
                         * State 5: "Payeh Mafooli" + "Shodeh"
                         * State 6: "Payeh Mafooli"
                         * State 7: "Payeh Mafooli" + "Shodeh"
                         * State 8: "Payeh Mafooli" + "Shodeh"
                         * State 9: "Payeh Mafooli" + "Shodeh"
                         */
                    if (stateList.get(i - 1).contains(0) || (stateList.get(i - 1).size() == 0 && stateList.get(i).size() == 0) ||
                            (stateList.get(i - 1).size() > 0 && stateList.get(i - 1).get(0) > 9)) {
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(10))
                                stateList.get(i).add(10);
                        }

                        if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(14))
                                stateList.get(i).add(14);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(15))
                                stateList.get(i).add(15);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(17))
                                stateList.get(i).add(17);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(18))
                                stateList.get(i).add(18);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(20))
                                stateList.get(i).add(20);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                            if (!stateList.get(i).contains(46))
                                stateList.get(i).add(46);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                            if (!stateList.get(i).contains(21))
                                stateList.get(i).add(21);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR) {
                            if (!stateList.get(i).contains(6))
                                stateList.get(i).add(6);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR) {
                            if (!stateList.get(i).contains(3) && !stateList.get(i).contains(5) && !stateList.get(i).contains(7)) {
                                if (!stateList.get(i).contains(-1))
                                    stateList.get(i).add(-1);
                            }

                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                            if (!stateList.get(i).contains(47))
                                stateList.get(i).add(47);
                        }

                        if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(16))
                                stateList.get(i).add(16);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(19))
                                stateList.get(i).add(19);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("خواست")) {
                            if (!stateList.get(i).contains(2))
                                stateList.get(i).add(2);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                verbInflection.VerbRoot.Type == VerbType.AYANDEH_PISHVANDI) {
                            if (!stateList.get(i).contains(-2))
                                stateList.get(i).add(-2);
                            tempPishvand = verbInflection.VerbRoot.Prefix;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(11))
                                stateList.get(i).add(11);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(3) && !stateList.get(i).contains(5) && !stateList.get(i).contains(7)) {
                                if (!stateList.get(i).contains(12))
                                    stateList.get(i).add(12);
                            }
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(13))
                                stateList.get(i).add(13);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(1))
                                stateList.get(i).add(1);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!stateList.get(i).contains(9))
                                stateList.get(i).add(9);
                        }
                        if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Positivity == TensePositivity.NEGATIVE) {
                            if (!stateList.get(i).contains(4))
                                stateList.get(i).add(4);
                        }
                    }

                    if (stateList.get(i - 1).contains(1)) {
                        boolean find1 = false;
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(22))
                                stateList.get(i).add(22);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(23))
                                stateList.get(i).add(23);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(24))
                                stateList.get(i).add(24);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(25))
                                stateList.get(i).add(25);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(26))
                                stateList.get(i).add(26);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(29))
                                stateList.get(i).add(29);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(30))
                                stateList.get(i).add(30);
                            find1 = true;
                        }
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.Positivity == TensePositivity.POSITIVE &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(27))
                                stateList.get(i).add(27);
                            find1 = true;
                        }
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("هست") &&
                                verbInflection.Positivity == TensePositivity.NEGATIVE &&
                                verbInflection.ZamirPeyvasteh == AttachedPronounType.AttachedPronoun_NONE &&
                                verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(27))
                                stateList.get(i).add(27);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(5))
                                stateList.get(i).add(5);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(-3))
                                stateList.get(i).add(-3);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(51))
                                stateList.get(i).add(51);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(7))
                                stateList.get(i).add(7);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(28))
                                stateList.get(i).add(28);
                            find1 = true;
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("خواست") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(3))
                                stateList.get(i).add(3);
                            find1 = true;
                        }
                        if (!find1) {
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    (verbInflection.VerbRoot.PastTenseRoot.equals("کرد") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("گشت") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("نمود") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("فرمود") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("ساخت")) &&
                                    verbInflection.VerbRoot.Type == VerbType.SADEH) {
                                if (!stateList.get(i).contains(17))
                                    stateList.get(i).add(17);
                                if( stateList.get(i - 1).contains(1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(0);
                            } else if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                    (verbInflection.VerbRoot.PastTenseRoot.equals("کرد") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("گشت") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("نمود") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("فرمود") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("ساخت")) &&
                                    verbInflection.VerbRoot.Type == VerbType.SADEH) {
                                if (!stateList.get(i).contains(14))
                                    stateList.get(i).add(14);
                                if( stateList.get(i - 1).contains(1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(0);
                            } else if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    (verbInflection.VerbRoot.PastTenseRoot.equals("کرد") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("گشت") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("نمود") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("فرمود") ||
                                            verbInflection.VerbRoot.PastTenseRoot.equals("ساخت")) &&
                                    verbInflection.VerbRoot.Type == VerbType.SADEH) {
                                if (!stateList.get(i).contains(10))
                                    stateList.get(i).add(10);
                                if( stateList.get(i - 1).contains(1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(0);
                            } else if (counter == valuePair.size()) {
                                if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!(verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                            verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                            verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                            verbInflection.Positivity == TensePositivity.POSITIVE)) {
                                        if (!stateList.get(i).contains(10))
                                            stateList.get(i).add(10);
                                        if( stateList.get(i - 1).contains(1))
                                            stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                        stateList.get(i - 1).add(27);
                                    }
                                }
                                if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!(verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                            verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                            verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                            verbInflection.Positivity == TensePositivity.POSITIVE)) {
                                        if (!stateList.get(i).contains(14))
                                            stateList.get(i).add(14);
                                        if( stateList.get(i - 1).contains(1))
                                            stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                        stateList.get(i - 1).add(27);
                                    }
                                }
                                if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(15))
                                        stateList.get(i).add(15);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(17))
                                        stateList.get(i).add(17);
                                    if (
                                            !(verbInflection.VerbRoot.PastTenseRoot.equals("کرد") &&
                                                    verbInflection.VerbRoot.Type == VerbType.SADEH)) {

                                        if( stateList.get(i - 1).contains(1))
                                            stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                        stateList.get(i - 1).add(27);
                                    }
                                }
                                if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(18))
                                        stateList.get(i).add(18);
                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(20))
                                        stateList.get(i).add(20);
                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm ==
                                        TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                        verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                    if (!stateList.get(i).contains(21))
                                        stateList.get(i).add(21);
                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm ==
                                        TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                        verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                        verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                    if (!stateList.get(i).contains(6))
                                        stateList.get(i).add(6);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }

                                if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                        verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(16))
                                        stateList.get(i).add(16);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                        verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(19))
                                        stateList.get(i).add(19);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                        verbInflection.VerbRoot.PastTenseRoot.equals("خواست")) {
                                    if (!stateList.get(i).contains(2))
                                        stateList.get(i).add(2);
                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);

                                }
                                if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                        verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(11))
                                        stateList.get(i).add(11);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                        verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(13))
                                        stateList.get(i).add(13);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);

                                }
                                if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                        verbInflection.Positivity == TensePositivity.POSITIVE) {
                                    if (!stateList.get(i).contains(1))
                                        stateList.get(i).add(1);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                                if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                        verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                    if (!stateList.get(i).contains(9))
                                        stateList.get(i).add(9);
                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);

                                }
                                if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                        !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                        verbInflection.Positivity == TensePositivity.NEGATIVE) {
                                    if (!stateList.get(i).contains(4))
                                        stateList.get(i).add(4);

                                    if( stateList.get(i - 1).contains(1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                    stateList.get(i - 1).add(27);
                                }
                            }
                        } else {
                            if( stateList.get(i - 1).contains(1))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                            if( stateList.get(i - 1).contains(4))
                                 stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));   //todo different from C#

                        }
                    }

                    if (stateList.get(i - 1).contains(5)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(35))
                                stateList.get(i).add(35);
                            if( stateList.get(i - 1).contains(5))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(5));  //todo different from C#
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        }
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("هست") &&
                                verbInflection.Positivity == TensePositivity.NEGATIVE &&
                                verbInflection.ZamirPeyvasteh == AttachedPronounType.AttachedPronoun_NONE &&
                                verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(35))
                                stateList.get(i).add(35);
                            if( stateList.get(i - 1).contains(5))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(5));  //todo different from C#
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(36))
                                stateList.get(i).add(36);
                            if( stateList.get(i - 1).contains(5))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(5));  //todo different from C#
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        }
                        //ToCheck
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(52))
                                stateList.get(i).add(52);
                            if( stateList.get(i - 1).contains(5))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(5));  //todo different from C#
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        }
                        if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(-3))
                                stateList.get(i).add(-3);
                            if( stateList.get(i - 1).contains(5))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(5));  //todo different from C#
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        }
                        if ((verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH || verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI) &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(37))
                                stateList.get(i).add(37);
                            if( stateList.get(i - 1).contains(5))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(5));  //todo different from C#
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        }
                    }

                    if (stateList.get(i - 1).contains(2)) {
                        if (verbInflection.IsPayehFelMasdari() && !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH && verbInflection.Positivity == TensePositivity.POSITIVE) {
                            stateList.get(i).clear();
                            if (!stateList.get(i).contains(31))
                                stateList.get(i).add(31);
                            stateList.get(i - 1).clear();
                        }
                        if (verbInflection.IsPayehFelMasdari() && verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            stateList.get(i).clear();
                            if (!stateList.get(i).contains(32))
                                stateList.get(i).add(32);
                            stateList.get(i - 1).clear();
                        }
                    }


                    /// state -2
                    if (stateList.get(i - 1).contains(-2)) {
                        if (verbInflection.IsPayehFelMasdari() && !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                VerbList.VerbPishvandiDic.get(tempPishvand).contains(verbInflection.VerbRoot.PastTenseRoot +
                                        "|" +
                                        verbInflection.VerbRoot.PresentTenseRoot)) {
                            stateList.get(i).clear();
                            if (!stateList.get(i).contains(31))
                                stateList.get(i).add(31);
                            stateList.get(i - 1).clear();
                        } else if (verbInflection.IsPayehFelMasdari() && verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                VerbList.VerbPishvandiDic.get(tempPishvand).contains(
                                        verbInflection.VerbRoot.PastTenseRoot + "|" +
                                                verbInflection.VerbRoot.PresentTenseRoot)) {
                            stateList.get(i).clear();
                            if (!stateList.get(i).contains(32))
                                stateList.get(i).add(32);
                            stateList.get(i - 1).clear();
                        } else {
                            //TO Check

                            stateList.get(i - 1).clear();
                            stateList.get(i).clear();
                            stateList.get(i - 1).add(0);
                            if (hasGozashtehSadeh)
                                stateList.get(i - 1).add(-2);
                            stateList.get(i).add(0);
                        }
                    }


                    /// state -3

                    if (stateList.get(i - 1).contains(-3)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(52))
                                stateList.get(i).add(52);
                            if( stateList.get(i - 1).contains(-3))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-3));  //todo different from C#

                        }
                    }


                    /// state 3

                    if (stateList.get(i - 1).contains(3)) {
                        if (verbInflection.IsPayehFelMasdari() && verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            stateList.get(i).clear();
                            if (!stateList.get(i).contains(33))
                                stateList.get(i).add(33);
                            stateList.get(i - 1).clear();
                        }
                    }


                    /// state 4

                    if (stateList.get(i - 1).contains(4)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(34))
                                stateList.get(i).add(34);
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                        }
                        if (verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(41))
                                stateList.get(i).add(41);
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                        }
                        if (verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(42))
                                stateList.get(i).add(42);
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                        }
                        if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(-3))
                                stateList.get(i).add(-3);
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                        }
                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(51))
                                stateList.get(i).add(51);
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                        }
                        if (verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            if (!stateList.get(i).contains(8))
                                stateList.get(i).add(8);
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                        }
                        if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                            if (!(verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                    verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                    verbInflection.Positivity == TensePositivity.POSITIVE)) {
                                if (!stateList.get(i).contains(10))
                                    stateList.get(i).add(10);

                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                        }

                        if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                (verbInflection.VerbRoot.PastTenseRoot.equals("کرد") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("گشت") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("نمود") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("فرمود") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("ساخت")) &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(17))
                                stateList.get(i).add(17);
                            if( stateList.get(i - 1).contains(1))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                            
                            stateList.get(i - 1).add(0);
                        } else if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                (verbInflection.VerbRoot.PastTenseRoot.equals("کرد") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("گشت") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("نمود") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("فرمود") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("ساخت")) &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(14))
                                stateList.get(i).add(14);
                            if( stateList.get(i - 1).contains(1))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                            stateList.get(i - 1).add(0);
                        } else if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                (verbInflection.VerbRoot.PastTenseRoot.equals("کرد") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("گشت") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("نمود") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("فرمود") ||
                                        verbInflection.VerbRoot.PastTenseRoot.equals("ساخت")) &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(10))
                                stateList.get(i).add(10);
                            if( stateList.get(i - 1).contains(1))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                            if( stateList.get(i - 1).contains(4))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                            stateList.get(i - 1).add(0);
                        } else if (stateList.get(i - 1).contains(4)) {
                            if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(15))
                                    stateList.get(i).add(15);

                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(14))
                                    stateList.get(i).add(14);

                                if( stateList.get(i - 1).contains(1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(1));  //todo different from C#
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(17))
                                    stateList.get(i).add(17);
                                if (
                                        !(verbInflection.VerbRoot.PastTenseRoot.equals("کرد") &&
                                                verbInflection.VerbRoot.Type == VerbType.SADEH)) {

                                    if( stateList.get(i - 1).contains(4))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                    stateList.get(i - 1).add(34);

                                }
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(18))
                                    stateList.get(i).add(18);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(20))
                                    stateList.get(i).add(20);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }

                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(21))
                                    stateList.get(i).add(21);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(6))
                                    stateList.get(i).add(6);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }

                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(47))
                                    stateList.get(i).add(47);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(16))
                                    stateList.get(i).add(16);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(19))
                                    stateList.get(i).add(19);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("خواست")) {
                                if (!stateList.get(i).contains(2))
                                    stateList.get(i).add(2);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(11))
                                    stateList.get(i).add(11);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }

                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(13))
                                    stateList.get(i).add(13);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Positivity == TensePositivity.POSITIVE) {
                                if (!stateList.get(i).contains(1))
                                    stateList.get(i).add(1);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(9))
                                    stateList.get(i).add(9);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Positivity == TensePositivity.NEGATIVE) {
                                if (!stateList.get(i).contains(4))
                                    stateList.get(i).add(4);
                                if( stateList.get(i - 1).contains(4))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(4));  //todo different from C#
                                stateList.get(i - 1).add(34);
                            }

                        }
                    }


                    /// state 6

                    if (stateList.get(i - 1).contains(6)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(39))
                                stateList.get(i).add(39);
                            if( stateList.get(i - 1).contains(6))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(6));  //todo different from C#
                        }
                    }


                    /// state 7

                    if (stateList.get(i - 1).contains(7)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(40))
                                stateList.get(i).add(40);
                            if( stateList.get(i - 1).contains(7))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(7));  //todo different from C#
                        }

                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("هست") &&
                                verbInflection.Positivity == TensePositivity.NEGATIVE &&
                                verbInflection.ZamirPeyvasteh == AttachedPronounType.AttachedPronoun_NONE &&
                                verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH) {
                            if (!stateList.get(i).contains(40))
                                stateList.get(i).add(40);
                            if( stateList.get(i - 1).contains(7))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(7));  //todo different from C#
                        }
                    }


                    /// state 8

                    if (stateList.get(i - 1).contains(8)) {
                        if (verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(43))
                                stateList.get(i).add(43);
                            if( stateList.get(i - 1).contains(8))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(8));  //todo different from C#
                        }
                        if (verbInflection.VerbRoot.PastTenseRoot.equals("بود") &&
                                verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(44))
                                stateList.get(i).add(44);
                            if( stateList.get(i - 1).contains(8))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(8));  //todo different from C#
                        }
                    }


                    /// state 9

                    if (stateList.get(i - 1).contains(9)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(45))
                                stateList.get(i).add(45);
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        } else if (verbInflection.VerbRoot.PresentTenseRoot.equals("باش") &&
                                verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(49))
                                stateList.get(i).add(49);
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        } else if (verbInflection.VerbRoot.PresentTenseRoot.equals("باش") &&
                                verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(50))
                                stateList.get(i).add(50);
                            if( stateList.get(i - 1).contains(9))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                        } else {

                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!(verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                        verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                        verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                        verbInflection.Positivity == TensePositivity.POSITIVE)) {
                                    if (!stateList.get(i).contains(10))
                                        stateList.get(i).add(10);
                                    if( stateList.get(i - 1).contains(9))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                    stateList.get(i - 1).add(45);
                                }
                            }

                            if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(14))
                                    stateList.get(i).add(14);
                                if (stateList.get(i - 1).contains(9)) {
                                    if( stateList.get(i - 1).contains(9))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                    stateList.get(i - 1).add(45);
                                }

                            }
                            if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(15))
                                    stateList.get(i).add(15);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(17))
                                    stateList.get(i).add(17);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(18))
                                    stateList.get(i).add(18);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(20))
                                    stateList.get(i).add(20);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(46))
                                    stateList.get(i).add(46);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(21))
                                    stateList.get(i).add(21);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }

                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                    verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(-1))
                                    stateList.get(i).add(-1);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }

                            if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(16))
                                    stateList.get(i).add(16);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(19))
                                    stateList.get(i).add(19);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("خواست")) {
                                if (!stateList.get(i).contains(2))
                                    stateList.get(i).add(2);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(11))
                                    stateList.get(i).add(11);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }

                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(13))
                                    stateList.get(i).add(13);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Positivity == TensePositivity.POSITIVE) {
                                if (!stateList.get(i).contains(1))
                                    stateList.get(i).add(1);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(9))
                                    stateList.get(i).add(9);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Positivity == TensePositivity.NEGATIVE) {
                                if (!stateList.get(i).contains(4))
                                    stateList.get(i).add(4);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                                stateList.get(i - 1).add(45);
                            }
                            if (stateList.get(i - 1).contains(9)) {
                                if (!stateList.get(i - 1).contains(45))
                                    stateList.get(i - 1).add(45);
                                if( stateList.get(i - 1).contains(9))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(9));  //todo different from C#
                            }
                        }
                    }


                    /// state -1

                    if (stateList.get(i - 1).contains(-1)) {
                        if (verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                verbInflection.Positivity == TensePositivity.POSITIVE) {
                            if (!stateList.get(i).contains(48))
                                stateList.get(i).add(48);
                            if( stateList.get(i - 1).contains(-1))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                        } else {
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!(verbInflection.VerbRoot.PresentTenseRoot.equals("است") &&
                                        verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                        verbInflection.VerbRoot.Type == VerbType.SADEH &&
                                        verbInflection.Positivity == TensePositivity.POSITIVE)) {
                                    if (!stateList.get(i).contains(10))
                                        stateList.get(i).add(10);
                                    if( stateList.get(i - 1).contains(-1))
                                        stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                    stateList.get(i - 1).add(48);
                                }
                            }

                            if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(14))
                                    stateList.get(i).add(14);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(15))
                                    stateList.get(i).add(15);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);

                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(17))
                                    stateList.get(i).add(17);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(18))
                                    stateList.get(i).add(18);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(20))
                                    stateList.get(i).add(20);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(46))
                                    stateList.get(i).add(46);

                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(21))
                                    stateList.get(i).add(21);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                    verbInflection.Person != PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(6))
                                    stateList.get(i).add(6);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR &&
                                    verbInflection.Person == PersonType.THIRD_PERSON_SINGULAR) {
                                if (!stateList.get(i).contains(-1))
                                    stateList.get(i).add(-1);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);

                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(47))
                                    stateList.get(i).add(47);

                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.AMR &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(16))
                                    stateList.get(i).add(16);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(19))
                                    stateList.get(i).add(19);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("خواست")) {
                                if (!stateList.get(i).contains(2))
                                    stateList.get(i).add(2);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(11))
                                    stateList.get(i).add(11);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.HAAL_ELTEZAMI &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(12))
                                    stateList.get(i).add(12);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.GOZASHTEH_SADEH &&
                                    verbInflection.VerbRoot.PastTenseRoot.equals("شد")) {
                                if (!stateList.get(i).contains(13))
                                    stateList.get(i).add(13);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Positivity == TensePositivity.POSITIVE) {
                                if (!stateList.get(i).contains(1))
                                    stateList.get(i).add(1);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if (verbInflection.TenseForm == TenseFormationType.PAYEH_MAFOOLI &&
                                    !verbInflection.VerbRoot.PastTenseRoot.equals("شد") &&
                                    verbInflection.Positivity == TensePositivity.NEGATIVE) {
                                if (!stateList.get(i).contains(4))
                                    stateList.get(i).add(4);
                                if( stateList.get(i - 1).contains(-1))
                                    stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                                stateList.get(i - 1).add(48);
                            }
                            if( stateList.get(i - 1).contains(-1))
                                stateList.get(i - 1).remove(stateList.get(i - 1).indexOf(-1));  //todo different from C#
                        }
                    }


                }
                if (stateList.get(i).size() == 0) {
                    stateList.get(i).add(0);
                }
                if (!stateList.get(i).contains(-2))
                    tempPishvand = "";
            }
        }
        return stateList;
    }

    /**
     * get initial data for processing verb tokens : the sentence
     */
    private static HashMap<Integer, KeyValuePair<String, Integer>> GetOutputResult(String[] sentence, String[] posSentence, String[] newPOSTokens, String[] lemmas, String[] outLemmas) throws IOException {
        Vector<String> posTokens = new Vector<String>();
        Vector<String> newLemmas = new Vector<String>();
        HashMap<Integer, Vector<Integer>> list = VerbPartTagger.getVerbStates(sentence, posSentence);
        HashMap<Integer, KeyValuePair<String, Integer>> newVector = new HashMap<Integer, KeyValuePair<String, Integer>>();
        int counter = 0;
        StringBuilder tempStr = new StringBuilder();
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).size() > 0) {
                int value = list.get(i).get(0);
                tempStr.append(sentence[i]);
                newVector.put(counter++, new KeyValuePair<String, Integer>(tempStr.toString(), value));
                if (value == 0) {
                    posTokens.add(posSentence[i]);
                    newLemmas.add(lemmas[i]);
                } else {
                    posTokens.add("V");
                    newLemmas.add(lemmas[i]);
                }
                tempStr = new StringBuilder();
            } else {
                tempStr.append(sentence[i] + " ");
            }
        }
        newPOSTokens = (String[]) posTokens.toArray();
        outLemmas = (String[]) newLemmas.toArray();
        return newVector;
    }

    /**
     * get initial data for processing verb tokens : the sentence
     */
    private static HashMap<Integer, KeyValuePair<String, Integer>> GetOutputResult(String[] sentence) throws IOException {
        HashMap<Integer, Vector<Integer>> list = getVerbStates(sentence);
        HashMap<Integer, KeyValuePair<String, Integer>> newVector = new HashMap<Integer, KeyValuePair<String, Integer>>();
        int counter = 0;
        StringBuilder tempStr = new StringBuilder();
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).size() > 0) {
                int value = list.get(i).get(0);
                tempStr.append(sentence[i]);
                newVector.put(counter++, new KeyValuePair<String, Integer>(tempStr.toString(), value));
                tempStr = new StringBuilder();
            } else {
                tempStr.append(sentence[i] + " ");
            }
        }
        return newVector;
    }

    /**
     * Get the verb tokens with their corresponding details
     */
    public static HashMap<Integer, KeyValuePair<String, VerbInflection>> GetVerbTokens(String[] sentence, String[] posTokens, String[] newPosTokens, String[] lemmas, String[] outLemmas) throws IOException {
        HashMap<Integer, KeyValuePair<String, VerbInflection>> outputResults = new HashMap<Integer, KeyValuePair<String, VerbInflection>>();
        HashMap<Integer, KeyValuePair<String, Integer>> output = GetOutputResult(sentence, posTokens, newPosTokens, lemmas, outLemmas);
        for (int i = 0; i < output.size(); i++) {
            KeyValuePair<String, Integer> values = output.get(i);
            VerbInflection inflection;
            TensePassivity passivity;
            TensePositivity positivity;
            Verb verb;
            PersonType shakhsType;
            TenseFormationType tenseFormationType;
            AttachedPronounType zamirPeyvastehType;
            String zamirString;
            Vector<VerbInflection> tempInflecVector;
            VerbInflection tempInflec;
            String[] tokens = output.get(i).getKey().split(" ");
            switch (values.getValue()) {
                /// 10

                case 10:
                    tenseFormationType = TenseFormationType.HAAL_SAADEH_EKHBARI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 53

                case 53:
                    tenseFormationType = TenseFormationType.HAAL_SAADEH;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH && (inflectionIter.VerbRoot.PastTenseRoot.equals("بایست") || inflectionIter.VerbRoot.PresentTenseRoot.equals("توان"))) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 14

                case 14:
                    tenseFormationType = TenseFormationType.HAAL_ELTEZAMI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 15

                case 15:
                    tenseFormationType = TenseFormationType.AMR;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.AMR) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirString = tempInflec.AttachedPronounString;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 17

                case 17:
                    tenseFormationType = TenseFormationType.GOZASHTEH_SADEH;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 18

                case 18:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ESTEMRAARI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 20

                case 20:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 21

                case 21:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 11
                case 11:
                    tenseFormationType = TenseFormationType.HAAL_SAADEH_EKHBARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb.PastTenseRoot = "کرد";
                    verb.PresentTenseRoot = "کن";
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 16
                case 16:
                    tenseFormationType = TenseFormationType.AMR;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.AMR) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb.PastTenseRoot = "کرد";
                    verb.PresentTenseRoot = "کن";
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 12
                case 12:
                    tenseFormationType = TenseFormationType.HAAL_ELTEZAMI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb.PastTenseRoot = "کرد";
                    verb.PresentTenseRoot = "کن";
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 13
                case 13:
                    tenseFormationType = TenseFormationType.GOZASHTEH_SADEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb.PastTenseRoot = "کرد";
                    verb.PresentTenseRoot = "کن";
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 19
                case 19:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ESTEMRAARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb.PastTenseRoot = "کرد";
                    verb.PresentTenseRoot = "کن";
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 22
                case 22:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 23
                case 23:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ESTEMRAARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 24
                case 24:
                    tenseFormationType = TenseFormationType.HAAL_SAADEH_EKHBARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 25
                case 25:
                    tenseFormationType = TenseFormationType.HAAL_ELTEZAMI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 26
                case 26:
                    tenseFormationType = TenseFormationType.GOZASHTEH_SADEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 27
                case 27:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();

                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                    if (tokens.length == 2) {
                        if (tokens[1].equals("نیست")) {
                            positivity = TensePositivity.NEGATIVE;
                        } else {
                            positivity = TensePositivity.POSITIVE;
                        }
                    } else {
                        positivity = TensePositivity.POSITIVE;
                    }
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 28
                case 28:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 29
                case 29:
                    tenseFormationType = TenseFormationType.GOZASHTEH_BAEED;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 30
                case 30:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ELTEZAMI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 31
                case 31:
                    tenseFormationType = TenseFormationType.AAYANDEH;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    verb = new Verb("", tempInflec.VerbRoot.PastTenseRoot, tempInflec.VerbRoot.PresentTenseRoot,
                            tempInflec.VerbRoot.Prefix, "", VerbTransitivity.Transitive,
                            tempInflec.VerbRoot.Type, true,
                            tempInflec.VerbRoot.PresentRootConsonantVowelEndStem,
                            tempInflec.VerbRoot.PastRootVowelStart,
                            tempInflec.VerbRoot.PresentRootVowelStart);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.IsPayehFelMasdari() && inflectionIter.Positivity == TensePositivity.POSITIVE) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb.PastTenseRoot = tempInflec.VerbRoot.PastTenseRoot;
                    verb.PresentTenseRoot = tempInflec.VerbRoot.PresentTenseRoot;
                    verb.Transitivity = tempInflec.VerbRoot.Transitivity;
                    verb.CanBeImperative = tempInflec.VerbRoot.CanBeImperative;
                    verb.PresentRootConsonantVowelEndStem = tempInflec.VerbRoot.PresentRootConsonantVowelEndStem;
                    verb.PastRootVowelStart = tempInflec.VerbRoot.PastRootVowelStart;
                    verb.PresentRootVowelStart = tempInflec.VerbRoot.PresentRootVowelStart;
                    if (zamirPeyvastehType == AttachedPronounType.AttachedPronoun_NONE)
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 32
                case 32:
                    tenseFormationType = TenseFormationType.AAYANDEH;
                    passivity = TensePassivity.PASSIVE; //ToCheck
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    verb = new Verb("", tempInflec.VerbRoot.PastTenseRoot, tempInflec.VerbRoot.PresentTenseRoot,
                            tempInflec.VerbRoot.Prefix, "", VerbTransitivity.Transitive,
                            tempInflec.VerbRoot.Type, true,
                            tempInflec.VerbRoot.PresentRootConsonantVowelEndStem,
                            tempInflec.VerbRoot.PastRootVowelStart,
                            tempInflec.VerbRoot.PresentRootVowelStart);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.IsPayehFelMasdari()) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb.PastTenseRoot = "کرد";
                    verb.PresentTenseRoot = "کن";
                    verb.Transitivity = VerbTransitivity.Transitive;
                    verb.CanBeImperative = tempInflec.VerbRoot.CanBeImperative;
                    verb.PresentRootConsonantVowelEndStem = tempInflec.VerbRoot.PresentRootConsonantVowelEndStem;
                    verb.PastRootVowelStart = tempInflec.VerbRoot.PastRootVowelStart;
                    verb.PresentRootVowelStart = tempInflec.VerbRoot.PresentRootVowelStart;
                    if (zamirPeyvastehType == AttachedPronounType.AttachedPronoun_NONE)
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 34
                case 34:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;

                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 38,39
                case 38:
                case 39:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;

                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 41
                case 41:
                    tenseFormationType = TenseFormationType.GOZASHTEH_BAEED;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    positivity = tempInflec.Positivity;
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 42
                case 42:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ELTEZAMI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    positivity = tempInflec.Positivity;
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 33
                case 33:
                    tenseFormationType = TenseFormationType.AAYANDEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;

                    tempInflecVector = VerbList.VerbShapes.get(tokens[2]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    if (shakhsType == PersonType.PERSON_NONE)
                        shakhsType = tempInflec.Person;

                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 35
                case 35:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                    if (tokens.length == 3) {
                        if (tokens[2].equals("نیست")) {
                            positivity = TensePositivity.NEGATIVE;
                        } else {
                            positivity = TensePositivity.POSITIVE;
                        }
                    } else {
                        positivity = TensePositivity.POSITIVE;
                    }
                    zamirPeyvastehType = AttachedPronounType.AttachedPronoun_NONE;
                    zamirString = "";

                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 36
                case 36:
                    tenseFormationType = TenseFormationType.GOZASHTEH_BAEED;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[2]);


                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = TensePositivity.POSITIVE;

                    tempInflecVector = VerbList.VerbShapes.get(tokens[2]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 37
                case 37:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ELTEZAMI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[2]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH || inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = TensePositivity.POSITIVE;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 40
                case 40:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                    if (tokens.length == 3) {
                        if (tokens[2].equals("نیست")) {
                            positivity = TensePositivity.NEGATIVE;
                        } else {
                            positivity = TensePositivity.POSITIVE;
                        }
                    } else {
                        positivity = TensePositivity.POSITIVE;
                    }
                    zamirPeyvastehType = AttachedPronounType.AttachedPronoun_NONE;
                    zamirString = "";

                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 43
                case 43:
                    tenseFormationType = TenseFormationType.GOZASHTEH_BAEED;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[2]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = TensePositivity.NEGATIVE;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 44
                case 44:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ELTEZAMI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[2]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = TensePositivity.NEGATIVE;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 45
                case 45:
                    tenseFormationType = TenseFormationType.GOZASHTEH_SADEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");

                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 46
                case 46:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 47
                case 47:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 48
                case 48:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 49
                case 49:
                    tenseFormationType = TenseFormationType.GOZASHTEH_BAEED;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 50
                case 50:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ELTEZAMI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 51
                case 51:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ABAD;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;

                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }

                    shakhsType = tempInflec.Person;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;

                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 52
                case 52:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ABAD;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;

                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    if (tempInflec != null) {
                        positivity = tempInflec.Positivity;
                        passivity = TensePassivity.PASSIVE;
                    }

                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;

                    zamirPeyvastehType = AttachedPronounType.AttachedPronoun_NONE;
                    zamirString = "";

                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// default
                default:
                    VerbInflection nullinflec = null;
                    if (i == output.size() - 1 && output.get(i).getValue() == 1) {
                        tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                        passivity = TensePassivity.ACTIVE;
                        tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        verb = tempInflec.VerbRoot.clone();
                        positivity = tempInflec.Positivity;
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                        zamirString = tempInflec.AttachedPronounString;

                        shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                        inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                                positivity, passivity);
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    } else if (i == output.size() - 1 && output.get(i).getValue() == 5) {
                        tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                        passivity = TensePassivity.PASSIVE;
                        tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        verb = tempInflec.VerbRoot.clone();
                        positivity = tempInflec.Positivity;
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                        zamirString = tempInflec.AttachedPronounString;

                        shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                        inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                                positivity, passivity);
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    } else if (i == output.size() - 1 && output.get(i).getValue() == 4) {
                        tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                        passivity = TensePassivity.ACTIVE;
                        tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        verb = tempInflec.VerbRoot.clone();
                        positivity = tempInflec.Positivity;
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                        zamirString = tempInflec.AttachedPronounString;

                        shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                        inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                                positivity, passivity);
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    } else if (i == output.size() - 1 && output.get(i).getValue() == 7) {
                        tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                        passivity = TensePassivity.PASSIVE;
                        tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        verb = tempInflec.VerbRoot.clone();

                        tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.VerbRoot.PastTenseRoot.equals("شد")) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        positivity = tempInflec.Positivity;
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                        zamirString = tempInflec.AttachedPronounString;

                        shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                        inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                                positivity, passivity);
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    } else if (i == output.size() - 1 && output.get(i).getValue() == 6) {
                        tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                        passivity = TensePassivity.ACTIVE;
                        tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        verb = tempInflec.VerbRoot.clone();
                        positivity = tempInflec.Positivity;
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                        zamirString = tempInflec.AttachedPronounString;

                        shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                        inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                                positivity, passivity);
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    } else if (i == output.size() - 1 && output.get(i).getValue() == 9) {
                        tenseFormationType = TenseFormationType.GOZASHTEH_SADEH;
                        passivity = TensePassivity.PASSIVE;
                        tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        verb = tempInflec.VerbRoot.clone();
                        positivity = tempInflec.Positivity;
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                        zamirString = tempInflec.AttachedPronounString;
                        verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");

                        shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                        inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                                positivity, passivity);
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    } else {
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), nullinflec));
                    }
                    break;
            }
        }
        return outputResults;
    }

    /**
     * Get the verb tokens with their corresponding details
     */
    public static HashMap<Integer, KeyValuePair<String, VerbInflection>> GetVerbTokens(String[] sentence) throws IOException {
        HashMap<Integer, KeyValuePair<String, VerbInflection>> outputResults = new HashMap<Integer, KeyValuePair<String, VerbInflection>>();
        HashMap<Integer, KeyValuePair<String, Integer>> output = GetOutputResult(sentence);
        for (int i = 0; i < output.size(); i++) {
            KeyValuePair<String, Integer> values = output.get(i);
            String zamirString;
            VerbInflection inflection;
            TensePassivity passivity;
            TensePositivity positivity;
            Verb verb;
            PersonType shakhsType;
            TenseFormationType tenseFormationType;
            AttachedPronounType zamirPeyvastehType;
            Vector<VerbInflection> tempInflecVector;
            VerbInflection tempInflec;
            String[] tokens = output.get(i).getKey().split(" ");

            switch (values.getValue()) {
                /// 10

                case 10:
                    tenseFormationType = TenseFormationType.HAAL_SAADEH_EKHBARI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 53

                case 53:
                    tenseFormationType = TenseFormationType.HAAL_SAADEH;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH && (inflectionIter.VerbRoot.PastTenseRoot.equals("بایست") || inflectionIter.VerbRoot.PresentTenseRoot.equals("توان"))) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 14

                case 14:
                    tenseFormationType = TenseFormationType.HAAL_ELTEZAMI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 15

                case 15:
                    tenseFormationType = TenseFormationType.AMR;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.AMR) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 17

                case 17:
                    tenseFormationType = TenseFormationType.GOZASHTEH_SADEH;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 18

                case 18:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ESTEMRAARI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 20

                case 20:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 21

                case 21:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    shakhsType = tempInflec.Person;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 11

                case 11:
                    tenseFormationType = TenseFormationType.HAAL_SAADEH_EKHBARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb.PastTenseRoot = "کرد";
                    verb.PresentTenseRoot = "کن";
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 16

                case 16:
                    tenseFormationType = TenseFormationType.AMR;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.AMR) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb.PastTenseRoot = "کرد";
                    verb.PresentTenseRoot = "کن";
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 12

                case 12:
                    tenseFormationType = TenseFormationType.HAAL_ELTEZAMI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb.PastTenseRoot = "کرد";
                    verb.PresentTenseRoot = "کن";
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 13

                case 13:
                    tenseFormationType = TenseFormationType.GOZASHTEH_SADEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb.PastTenseRoot = "کرد";
                    verb.PresentTenseRoot = "کن";
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 19

                case 19:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ESTEMRAARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb.PastTenseRoot = "کرد";
                    verb.PresentTenseRoot = "کن";
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 22
                case 22:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 23
                case 23:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ESTEMRAARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 24
                case 24:
                    tenseFormationType = TenseFormationType.HAAL_SAADEH_EKHBARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 25
                case 25:
                    tenseFormationType = TenseFormationType.HAAL_ELTEZAMI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 26
                case 26:
                    tenseFormationType = TenseFormationType.GOZASHTEH_SADEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;


                /// 27
                case 27:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();

                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                    if (tokens.length == 2) {
                        if (tokens[1].equals("نیست")) {
                            positivity = TensePositivity.NEGATIVE;
                        } else {
                            positivity = TensePositivity.POSITIVE;
                        }
                    } else {
                        positivity = TensePositivity.POSITIVE;
                    }
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 28
                case 28:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 29
                case 29:
                    tenseFormationType = TenseFormationType.GOZASHTEH_BAEED;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 30
                case 30:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ELTEZAMI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 31
                case 31:
                    tenseFormationType = TenseFormationType.AAYANDEH;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    verb = new Verb("", tempInflec.VerbRoot.PastTenseRoot, tempInflec.VerbRoot.PresentTenseRoot,
                            tempInflec.VerbRoot.Prefix, "", VerbTransitivity.Transitive,
                            tempInflec.VerbRoot.Type, true,
                            tempInflec.VerbRoot.PresentRootConsonantVowelEndStem,
                            tempInflec.VerbRoot.PastRootVowelStart,
                            tempInflec.VerbRoot.PresentRootVowelStart);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.IsPayehFelMasdari() && inflectionIter.Positivity == TensePositivity.POSITIVE) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb.PastTenseRoot = tempInflec.VerbRoot.PastTenseRoot;
                    verb.PresentTenseRoot = tempInflec.VerbRoot.PresentTenseRoot;
                    verb.Transitivity = tempInflec.VerbRoot.Transitivity;
                    verb.CanBeImperative = tempInflec.VerbRoot.CanBeImperative;
                    verb.PresentRootConsonantVowelEndStem = tempInflec.VerbRoot.PresentRootConsonantVowelEndStem;
                    verb.PastRootVowelStart = tempInflec.VerbRoot.PastRootVowelStart;
                    verb.PresentRootVowelStart = tempInflec.VerbRoot.PresentRootVowelStart;
                    if (zamirPeyvastehType == AttachedPronounType.AttachedPronoun_NONE)
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 32
                case 32:
                    tenseFormationType = TenseFormationType.AAYANDEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    verb = new Verb("", tempInflec.VerbRoot.PastTenseRoot, tempInflec.VerbRoot.PresentTenseRoot,
                            tempInflec.VerbRoot.Prefix, "", VerbTransitivity.Transitive,
                            tempInflec.VerbRoot.Type, true,
                            tempInflec.VerbRoot.PresentRootConsonantVowelEndStem,
                            tempInflec.VerbRoot.PastRootVowelStart,
                            tempInflec.VerbRoot.PresentRootVowelStart);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.IsPayehFelMasdari()) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb.PastTenseRoot = "کرد";
                    verb.PresentTenseRoot = "کن";
                    verb.Transitivity = VerbTransitivity.Transitive;
                    verb.CanBeImperative = tempInflec.VerbRoot.CanBeImperative;
                    verb.PresentRootConsonantVowelEndStem = tempInflec.VerbRoot.PresentRootConsonantVowelEndStem;
                    verb.PastRootVowelStart = tempInflec.VerbRoot.PastRootVowelStart;
                    verb.PresentRootVowelStart = tempInflec.VerbRoot.PresentRootVowelStart;
                    if (zamirPeyvastehType == AttachedPronounType.AttachedPronoun_NONE)
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 34
                case 34:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;

                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 38,39
                case 38:
                case 39:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;

                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 41
                case 41:
                    tenseFormationType = TenseFormationType.GOZASHTEH_BAEED;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    positivity = tempInflec.Positivity;
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 42
                case 42:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ELTEZAMI;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    positivity = tempInflec.Positivity;
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 33
                case 33:
                    tenseFormationType = TenseFormationType.AAYANDEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }

                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI || inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH) { //todo the 2nd condition is added by Java
                            tempInflec = inflectionIter;
                            break;
                        }
                    }

                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;

                    tempInflecVector = VerbList.VerbShapes.get(tokens[2]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    if (shakhsType == PersonType.PERSON_NONE)
                        shakhsType = tempInflec.Person;

                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 35
                case 35:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                    if (tokens.length == 3) {
                        if (tokens[2].equals("نیست")) {
                            positivity = TensePositivity.NEGATIVE;
                        } else {
                            positivity = TensePositivity.POSITIVE;
                        }
                    } else {
                        positivity = TensePositivity.POSITIVE;
                    }
                    zamirPeyvastehType = AttachedPronounType.AttachedPronoun_NONE;
                    zamirString = "";

                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 36
                case 36:
                    tenseFormationType = TenseFormationType.GOZASHTEH_BAEED;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = TensePositivity.POSITIVE;
                    if (tempInflec.Positivity == TensePositivity.NEGATIVE)
                        positivity = TensePositivity.NEGATIVE;

                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {

                        tempInflec = inflectionIter;
                        break;

                    }
                    if (tempInflec.Positivity == TensePositivity.NEGATIVE)
                        positivity = TensePositivity.NEGATIVE;

                    tempInflecVector = VerbList.VerbShapes.get(tokens[2]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    if (tempInflec.Positivity == TensePositivity.NEGATIVE)
                        positivity = TensePositivity.NEGATIVE;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 37
                case 37:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ELTEZAMI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[2]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH || inflectionIter.TenseForm == TenseFormationType.HAAL_ELTEZAMI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = TensePositivity.POSITIVE;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 40
                case 40:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                    if (tokens.length == 3) {
                        if (tokens[2].equals("نیست")) {
                            positivity = TensePositivity.NEGATIVE;
                        } else {
                            positivity = TensePositivity.POSITIVE;
                        }
                    } else {
                        positivity = TensePositivity.POSITIVE;
                    }
                    zamirPeyvastehType = AttachedPronounType.AttachedPronoun_NONE;
                    zamirString = "";

                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 43
                case 43:
                    tenseFormationType = TenseFormationType.GOZASHTEH_BAEED;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[2]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = TensePositivity.NEGATIVE;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 44
                case 44:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ELTEZAMI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    tempInflecVector = VerbList.VerbShapes.get(tokens[2]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.HAAL_SAADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    shakhsType = tempInflec.Person;
                    positivity = TensePositivity.NEGATIVE;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 45
                case 45:
                    tenseFormationType = TenseFormationType.GOZASHTEH_SADEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");

                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 46
                case 46:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 47
                case 47:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 48
                case 48:
                    tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 49
                case 49:
                    tenseFormationType = TenseFormationType.GOZASHTEH_BAEED;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 50
                case 50:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ELTEZAMI;
                    passivity = TensePassivity.PASSIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");
                    shakhsType = tempInflec.Person;
                    positivity = tempInflec.Positivity;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;
                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 51
                case 51:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ABAD;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;

                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }

                    shakhsType = tempInflec.Person;
                    zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                    zamirString = tempInflec.AttachedPronounString;

                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// 52
                case 52:
                    tenseFormationType = TenseFormationType.GOZASHTEH_ABAD;
                    passivity = TensePassivity.ACTIVE;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    verb = tempInflec.VerbRoot.clone();
                    positivity = tempInflec.Positivity;
                    tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                    tempInflec = null;
                    for (VerbInflection inflectionIter : tempInflecVector) {
                        if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                            tempInflec = inflectionIter;
                            break;
                        }
                    }
                    if (tempInflec != null) {
                        positivity = tempInflec.Positivity;
                        passivity = TensePassivity.PASSIVE;
                    }
                    shakhsType = PersonType.THIRD_PERSON_SINGULAR;

                    zamirPeyvastehType = AttachedPronounType.AttachedPronoun_NONE;
                    zamirString = "";

                    inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                            positivity, passivity);
                    outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    break;

                /// default
                default:
                    VerbInflection nullinflec = null;
                    if (i == output.size() - 1 && output.get(i).getValue() == 1) {
                        tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                        passivity = TensePassivity.ACTIVE;
                        tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        verb = tempInflec.VerbRoot.clone();
                        positivity = tempInflec.Positivity;
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                        zamirString = tempInflec.AttachedPronounString;

                        shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                        inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                                positivity, passivity);
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    } else if (i == output.size() - 1 && output.get(i).getValue() == 5) {
                        tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                        passivity = TensePassivity.PASSIVE;
                        tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        verb = tempInflec.VerbRoot.clone();
                        positivity = tempInflec.Positivity;
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                        zamirString = tempInflec.AttachedPronounString;

                        shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                        inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                                positivity, passivity);
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    } else if (i == output.size() - 1 && output.get(i).getValue() == 4) {
                        tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
                        passivity = TensePassivity.ACTIVE;
                        tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        verb = tempInflec.VerbRoot.clone();
                        positivity = tempInflec.Positivity;
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                        zamirString = tempInflec.AttachedPronounString;

                        shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                        inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                                positivity, passivity);
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    } else if (i == output.size() - 1 && output.get(i).getValue() == 7) {
                        tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                        passivity = TensePassivity.PASSIVE;
                        tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        verb = tempInflec.VerbRoot.clone();

                        tempInflecVector = VerbList.VerbShapes.get(tokens[1]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.VerbRoot.PastTenseRoot.equals("شد")) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        positivity = tempInflec.Positivity;
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                        zamirString = tempInflec.AttachedPronounString;

                        shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                        inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                                positivity, passivity);
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    } else if (i == output.size() - 1 && output.get(i).getValue() == 6) {
                        tenseFormationType = TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
                        passivity = TensePassivity.ACTIVE;
                        tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        verb = tempInflec.VerbRoot.clone();
                        positivity = tempInflec.Positivity;
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                        zamirString = tempInflec.AttachedPronounString;

                        shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                        inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                                positivity, passivity);
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    } else if (i == output.size() - 1 && output.get(i).getValue() == 9) {
                        tenseFormationType = TenseFormationType.GOZASHTEH_SADEH;
                        passivity = TensePassivity.PASSIVE;
                        tempInflecVector = VerbList.VerbShapes.get(tokens[0]);
                        tempInflec = null;
                        for (VerbInflection inflectionIter : tempInflecVector) {
                            if (inflectionIter.TenseForm == TenseFormationType.PAYEH_MAFOOLI) {
                                tempInflec = inflectionIter;
                                break;
                            }
                        }
                        verb = tempInflec.VerbRoot.clone();
                        positivity = tempInflec.Positivity;
                        zamirPeyvastehType = tempInflec.ZamirPeyvasteh;
                        zamirString = tempInflec.AttachedPronounString;
                        verb = new Verb(verb.PrepositionOfVerb, "کرد", "کن", verb.Prefix, verb.NonVerbalElement, VerbTransitivity.Transitive, verb.Type, true, "?", "@", "!");

                        shakhsType = PersonType.THIRD_PERSON_SINGULAR;
                        inflection = new VerbInflection(verb, zamirPeyvastehType, zamirString, shakhsType, tenseFormationType,
                                positivity, passivity);
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), inflection));
                    } else {
                        outputResults.put(i, new KeyValuePair<String, VerbInflection>(output.get(i).getKey(), nullinflec));
                    }
                    break;
            }
        }
        return outputResults;
    }
}
