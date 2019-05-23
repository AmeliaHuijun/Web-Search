package project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Divide {
    public static Integer[] leaders = {0, 10, 20, 30, 40};
    public static Integer[] leader_tags;
    public static Integer documents;
    public static Integer dictionary_count;
    public static List<HashMap<Integer, Double>> divideMaps = new ArrayList<HashMap<Integer, Double>>();

    static {
        documents = projectdata.MyDocs.size();
        dictionary_count = projectdata.myDictionary.size();
        leader_tags = new Integer[documents];
        for (Integer i = 0; i < documents; i++) {
            leader_tags[i] = 0;
        }
        for (Integer i = 0; i < leaders.length; i++) {
            leader_tags[leaders[i]] = 1;
        }
        for (Integer i = 0; i < leaders.length; i++) {
            HashMap<Integer, Double> temp = new HashMap<Integer, Double>();
            divideMaps.add(temp);
        }
    }

    public static void DivideDoc (double[][] normalization) {
        for (Integer i = 0; i < documents; i++) {
            if (leader_tags[i] == 0) {
                double[] similarity = SimilarityCalculate.CalScore(normalization, i);
                Integer index = -1;
                double max = -1;
                for (Integer j = 0; j < leaders.length; j++) {
                    if (similarity[leaders[j]] > max) {
                        max = similarity[leaders[j]];
                        index = j;
                    }
                }
                divideMaps.get(index).put(i, similarity[leaders[index]]);
            }
        }
        Output();
    }

    public static void Output() {
        System.out.println("*** Division ***");
        for (Integer i = 0; i < divideMaps.size(); i++) {
            System.out.println("Leader ID: " + leaders[i] + "  Size: " + divideMaps.get(i).size());
            divideMaps.get(i).forEach((k, v) -> System.out.println("Document ID: " + k + "; Similarity: " + v));
        }
    }
}
