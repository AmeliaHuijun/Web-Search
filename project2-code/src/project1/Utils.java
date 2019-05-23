package project1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    public static ArrayList<Integer> findBiggest(double[] args) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        int[] access = new int[args.length];

        for(Integer j = 0; j < 6; j++) {
            double max = -1;
            Integer index = -1;
            for(Integer i = 0;i < args.length; i++){
                if (access[i] == 0) {
                    if (args[i] > max) {
                        max = args[i];
                        index = i;
                    }
                }
            }
            if (args[index] != 0)
                list.add(index);
            access[index] = -1;
        }

        return list;
    }
}
