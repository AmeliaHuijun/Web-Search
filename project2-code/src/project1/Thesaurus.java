package project1;

import java.util.HashMap;
import java.util.Map;

public class Thesaurus {
    public static final Map<String, String[]> thesaurusMap;
    static {
        thesaurusMap = new HashMap<String, String[]>();

        String[] temp = {"nice", "fancy"};
        thesaurusMap.put("beautiful", temp);

        temp = new String[] {"chpt"};
        thesaurusMap.put("chapter", temp);

        temp = new String[] {"chapter"};
        thesaurusMap.put("chpt", temp);

        temp = new String[] {"owner", "accountable"};
        thesaurusMap.put("responsible", temp);

        temp = new String[] {"freeman", "moore"};
        thesaurusMap.put("freemanmoore", temp);

        temp = new String[] {"department"};
        thesaurusMap.put("dept", temp);

        temp = new String[] {"beige", "tan", "auburn"};
        thesaurusMap.put("brown", temp);

        temp = new String[] {"Tuesday"};
        thesaurusMap.put("tues", temp);

        temp = new String[] {"owner", "single", "shoe", "boot"};
        thesaurusMap.put("sole", temp);

        temp = new String[] {"hmwk", "home", "work"};
        thesaurusMap.put("homework", temp);

        temp = new String[] {"book", "unique"};
        thesaurusMap.put("novel", temp);

        temp = new String[] {"cse"};
        thesaurusMap.put("computer", temp);

        temp = new String[] {"novel", "book"};
        thesaurusMap.put("story", temp);

        temp = new String[] {"magic", "abracadabra"};
        thesaurusMap.put("hocuspocus", temp);

        temp = new String[] {"this", "work"};
        thesaurusMap.put("thisworks", temp);
    }
}
