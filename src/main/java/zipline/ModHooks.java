package zipline;

import java.util.HashMap;

public class ModHooks<K, V> {
    private HashMap<K, V> modList = new HashMap<K, V>();

    public void registerModifier(K obj, V obj1) {
        modList.put(obj, obj1);
    }

    public V getModifier(K key) {
        return modList.get(key);
    }
}