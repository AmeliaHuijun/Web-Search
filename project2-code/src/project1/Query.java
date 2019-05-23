package project1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Query {
    static Integer MAXWORDS = 2000;

    public static ArrayList<Result> QueryThis(String[] query_array, double[][] normalization) {
        Integer documents = normalization.length;
        Integer dictionary_count = normalization[0].length;
        //Integer documents = projectdata.MyDocs.size();
        //Integer dictionary_count = projectdata.myDictionary.size();

        double[] score = new double[documents];

        Integer[] frequency = new Integer[MAXWORDS];
        List newDictionary = new ArrayList<String>();

        try {
            newDictionary = Utils.deepCopy(projectdata.myDictionary);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Integer newDicSize = newDictionary.size();

        /*for (Integer i = 0; i < newDictionary.size(); i++) {
            System.out.println(i + " " + newDictionary.get(i));
        }*/

        for (Integer i = 0; i < MAXWORDS; i++) {
            frequency[i] = 0;
        }

        for (Integer i = 0; i < query_array.length; i++) {
            boolean foundword = false;
            String oneword = query_array[i].toLowerCase();

            for (Integer j = 0; j < newDicSize; j++) {
                if (oneword.equals(newDictionary.get(j))) {
                    frequency[j]++;
                    foundword = true;
                    break;
                }
            }
            if (!foundword) {
                newDictionary.add(oneword);
                frequency[newDicSize] = 1;
                newDicSize++;
            }
        }

        double[] normal_this = SimilarityCalculate.CalSingleNormal(frequency);

        double[][] new_normal = new double[documents + 1][dictionary_count];

        for (Integer i = 0; i < documents; i++) {
            System.arraycopy(normalization[i], 0, new_normal[i], 0, dictionary_count);
        }

        System.arraycopy(normal_this, 0, new_normal[documents], 0, dictionary_count);

        score = SimilarityCalculate.CalScore(new_normal, new_normal.length - 1);

        /*for (Integer i = 0; i < documents; i++) {
            System.out.print(i + " ");
            for (Integer j = 0; j < dictionary_count; j++) {
                System.out.print(new_normal[i][j] + " ");
            }
            System.out.print("\n");
        }*/

        for (Integer i = 0; i < documents; i++) {
            Integer count = 0;
            for (Integer j = 0; j < query_array.length; j++) {
                String titletemp = projectdata.MyDocs.get(i).DocTitle.toLowerCase();
                if (titletemp.contains(query_array[j])) {
                    count++;
                }
            }
            score[i] = score[i] + count * 0.25;
        }

        ArrayList<Integer> index_result = Utils.findBiggest(score);

        ArrayList<Result> result = new ArrayList<Result>();

        for (Integer i = 0; i < index_result.size(); i++) {
            Result tempresult = new Result(index_result.get(i), score[index_result.get(i)]);
            result.add(tempresult);
            //System.out.println(i + " " + index_result.get(i) + " " + score[index_result.get(i)]);
        }

        return result;
    }

    public static void HighestQuery(String[] query_array, double[][] normalization) {
        ArrayList<Result> result = QueryThis(query_array, normalization);

        Output(query_array, result);//output
        Integer count = result.size();
        System.out.println("*** We have " + count + " results of the original query. ***");
        System.out.print("\n");

        if (count < 3) {
            System.out.println("*** We need to check the thesaurus table to revise query. ***");
            System.out.print("\n");
            label: for (Integer i = 0; i < query_array.length; i++) {
                String[] thesaurus = Thesaurus.thesaurusMap.get(query_array[i]);
                if (thesaurus != null) {
                    for (Integer j = 0; j < thesaurus.length; j++) {
                        query_array[i] = thesaurus[j];

                        ArrayList<Result> tempresult = QueryThis(query_array, normalization);

                        Output(query_array, tempresult);//output

                        Integer old_count = count;
                        count = count + tempresult.size();

                        if (count.equals(old_count)) {
                            System.out.println("*** We cannot find any result of this query. ***");
                            System.out.print("\n");
                        }
                        else {
                            System.out.println("*** We have " + count + " results now. ***");
                            System.out.print("\n");
                        }

                        if (count >= 3){
                            System.out.println("*** We have already got over 3 results, so the program will end searching. ***");
                            System.out.print("\n");
                            break label;
                        }
                    }
                }
            }
            if (count == 0) {
                System.out.println("*** We have tried all revised queries but we still cannot find any result. ***");
                System.out.print("\n");
            }
            else if (count < 3) {
                System.out.println("*** We have tried all revised queries. ***");
                System.out.println("*** We finally have " + count + " results in total. ***");
                System.out.print("\n");
            }
            else {
                System.out.println("*** We finally have " + count + " results in total. ***");
                System.out.print("\n");
            }
        }
        else {
            System.out.println("*** Because we have got over 3 results from the original query, we don't need to check thesaurus table. ***");
            System.out.println("*** We finally have " + count + " results in total. ***");
            System.out.print("\n");
        }
    }

    public static void Output (String[] query_array, ArrayList<Result> result) {
        System.out.print("*** The result of query: ");
        for (Integer i = 0; i < query_array.length; i++) {
            System.out.print(query_array[i] + " ");
        }
        System.out.print("***\n");
        for (Integer i = 0; i < result.size(); i++) {
            System.out.println("Result " + (i + 1) + ":");
            System.out.print("Document ID: " + result.get(i).getIndex() + "    ");
            System.out.print("Title: " + projectdata.MyDocs.get(result.get(i).getIndex()).DocTitle + "\n");
            System.out.println("Url: " + projectdata.MyDocs.get(result.get(i).getIndex()).DocURL);
            System.out.println("Score: " + result.get(i).getSim());
            System.out.print("The first twenty words: ");
            Integer length = projectdata.DocumentContent.get(result.get(i).getIndex()).length;
            if (length > 20) {
                for (Integer j = 0; j < 20; j++) {
                    System.out.print(projectdata.DocumentContent.get(result.get(i).getIndex())[j] + " ");
                }
            }
            else {
                for (Integer j = 0; j < length; j++) {
                    System.out.print(projectdata.DocumentContent.get(result.get(i).getIndex())[j] + " ");
                }
            }
            System.out.print("\n\n");
        }
    }
}
