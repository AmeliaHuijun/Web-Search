package project1;

public class SimilarityCalculate {
    public static double[] CalEuclidian(Integer[][] resource) {
        try {
            Integer documents = resource.length;
            Integer dictionary_count = resource[0].length;

            double[] euclidian = new double[documents];

            for (Integer i = 0; i < documents; i++) {
                Integer sum = 0;
                for (Integer j = 0; j < dictionary_count; j++) {
                    sum = sum + resource[i][j] * resource[i][j];
                }
                double result = Math.sqrt(sum);
                euclidian[i] = result;
            }
            return euclidian;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static double[][] CalNormal(Integer[][] resource, double[] euclidian) {
        try {
            Integer documents = resource.length;
            Integer dictionary_count = resource[0].length;

            double[][] normalization = new double[documents][dictionary_count];

            for (Integer i = 0; i < documents; i++) {
                for (Integer j = 0; j < dictionary_count; j++) {
                    double result = resource[i][j] / euclidian[i];
                    normalization[i][j] = result;
                }
            }
            return normalization;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static double[] CalScore(double[][] normalization, Integer target) {
        try {
            Integer documents = normalization.length;
            Integer dictionary_count = normalization[0].length;

            double[] score = new double[documents];

            for (Integer i = 0; i < documents; i++) {
                double score_temp = 0;
                if (!i.equals(target)) {
                    for (Integer j = 0; j < dictionary_count; j++) {
                        double per_score = normalization[i][j] * normalization[target][j];
                        score_temp = score_temp + per_score;
                    }
                }
                score[i] = score_temp;
            }
            return score;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static double[] CalSingleNormal(Integer[] query) {
        try {
            Integer dictionary_count = query.length;

            double[] normalization = new double[dictionary_count];

            double euclidian = 0.0;

            Integer sum = 0;
            for (Integer i = 0; i < dictionary_count; i++) {
                sum = sum + query[i] * query[i];
            }

            euclidian = Math.sqrt(sum);

            for (Integer i = 0; i < dictionary_count; i++) {
                normalization[i] = query[i]/euclidian;
            }
            return normalization;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
