package zipline;

import java.util.HashMap;
import java.util.Map;

public class ModHooks {
    public static Map modList = new HashMap();

    public static void registerModifier(String s, Object obj, Object obj1) {
        if (!modList.containsKey(s)) {
            modList.put(s, new HashMap());
        }
        HashMap hashmap = (HashMap) modList.get(s);
        hashmap.put(obj, obj1);
    }
}