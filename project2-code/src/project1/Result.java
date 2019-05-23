package project1;

public class Result {
    public Integer index;
    public double similarity;
    Result(Integer i, double s) {
        index = i;
        similarity = s;
    }

    public Integer getIndex() {
        return index;
    }

    public double getSim() {
        return similarity;
    }
}
