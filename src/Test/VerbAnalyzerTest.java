package Test;

import VerbInflector.DependencyBasedToken;
import VerbInflector.SentenceAnalyzer;
import VerbInflector.VerbBasedSentence;

import java.io.IOException;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 11:58 PM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class VerbAnalyzerTest {
    public static void main(String[] args) throws IOException {
        SentenceAnalyzer analyzer = new SentenceAnalyzer(args[0]);
        String sentence =
                "علی در این فاصله زمین خورد و نتوانست حرکت کند .";
        String sentence2=
                "می‌خواهیم به همهٔ دنیا ثابت کنیم که ملتی غیور داریم داشته‌ایم و خواهیم داشت ."
                ;
        VerbBasedSentence result = SentenceAnalyzer.MakeVerbBasedSentence(sentence);
        StringBuilder output = new StringBuilder();
        for (DependencyBasedToken dependencyBasedToken : result.SentenceTokens)
        {
            output.append(dependencyBasedToken.WordForm + "\t" + dependencyBasedToken.Lemma + "\t" +
                    dependencyBasedToken.CPOSTag
                    + "\t" + (dependencyBasedToken.HeadNumber+1) + "\t" +
                    dependencyBasedToken.DependencyRelation+'\n');
        }
        System.out.println(output.toString());

        long startTime = System.currentTimeMillis();

        for(int i=0;i<1000000;i++){
         result = SentenceAnalyzer.MakeVerbBasedSentence(sentence2);
         output = new StringBuilder();
        for (DependencyBasedToken dependencyBasedToken : result.SentenceTokens)
        {
            output.append(dependencyBasedToken.WordForm + "\t" + dependencyBasedToken.Lemma + "\t" +
                    dependencyBasedToken.CPOSTag
                    + "\t" + (dependencyBasedToken.HeadNumber+1) + "\t" +
                    dependencyBasedToken.DependencyRelation+'\n');
        }
       // System.out.println(i);
        }
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println((double)duration/1000000);
    }
}
