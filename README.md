# HashMap in Java - Complete Guide

## 1. Basic Declaration & Initialization

```java
import java.util.*;

// Basic
Map<String, Integer> map = new HashMap<>();

// With initial capacity (good for large maps)
Map<String, Integer> map = new HashMap<>(100);

// With capacity + load factor (default 0.75)
Map<String, Integer> map = new HashMap<>(100, 0.75f);

// Initialize with values (Java 9+)
Map<String, Integer> map = Map.of("a", 1, "b", 2);  // immutable

// Mutable with values
Map<String, Integer> map = new HashMap<>() {{
    put("a", 1);
    put("b", 2);
}};
```

---

## 2. Frequency Counting (MOST USED in LeetCode) ⭐

### The Cleanest Way — `merge()` (Java 8+)
```java
Map<Character, Integer> freq = new HashMap<>();

for (char c : s.toCharArray()) {
    freq.merge(c, 1, Integer::sum);   // increment
}
```

### The Classic Way — `getOrDefault()`
```java
for (char c : s.toCharArray()) {
    freq.put(c, freq.getOrDefault(c, 0) + 1);
}
```

### `compute()` — Full control
```java
freq.compute(c, (k, v) -> v == null ? 1 : v + 1);
```

### `computeIfAbsent()` — For lists/groups
```java
// Group anagrams
Map<String, List<String>> groups = new HashMap<>();
for (String word : words) {
    char[] arr = word.toCharArray();
    Arrays.sort(arr);
    String key = new String(arr);
    groups.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
}
```

---

## 3. Increment / Decrement Patterns

```java
Map<String, Integer> map = new HashMap<>();

// INCREMENT
map.merge("key", 1, Integer::sum);              // safest
map.put("key", map.getOrDefault("key", 0) + 1); // classic

// DECREMENT
map.merge("key", -1, Integer::sum);

// Or explicit
map.computeIfPresent("key", (k, v) -> v - 1);

// DECREMENT WITH REMOVAL WHEN ZERO (sliding window pattern)
map.merge("key", -1, (oldV, one) -> oldV + one == 0 ? null : oldV + one);
// Or
int newVal = map.get("key") - 1;
if (newVal == 0) map.remove("key");
else map.put("key", newVal);
```

---

## 4. Traversal — All Ways

```java
Map<String, Integer> map = new HashMap<>();
map.put("a", 1); map.put("b", 2);

// 1. entrySet() — FASTEST for key+value (industry standard)
for (Map.Entry<String, Integer> e : map.entrySet()) {
    System.out.println(e.getKey() + " = " + e.getValue());
}

// 2. keySet() — when you only need keys
for (String key : map.keySet()) { ... }

// 3. values() — when you only need values
for (Integer v : map.values()) { ... }

// 4. forEach (Java 8+)
map.forEach((k, v) -> System.out.println(k + " = " + v));

// 5. Iterator — SAFE for removal during iteration
Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
while (it.hasNext()) {
    Map.Entry<String, Integer> e = it.next();
    if (e.getValue() == 0) it.remove();
}
```

⚠️ **Never** modify a HashMap while iterating with for-each → `ConcurrentModificationException`.

---

## 5. Common Operations Cheat Sheet

| Operation | Method |
|-----------|--------|
| Add / Update | `put(k, v)` |
| Get | `get(k)` |
| Get with default | `getOrDefault(k, def)` |
| Remove | `remove(k)` |
| Remove if value matches | `remove(k, v)` |
| Contains key | `containsKey(k)` |
| Contains value | `containsValue(v)` (O(n)) |
| Size | `size()` |
| Empty check | `isEmpty()` |
| Clear | `clear()` |
| Keys / Values / Entries | `keySet()`, `values()`, `entrySet()` |

---

## 6. Industry Patterns

### Pattern A: Two Sum (complement lookup)
```java
Map<Integer, Integer> seen = new HashMap<>();
for (int i = 0; i < nums.length; i++) {
    int complement = target - nums[i];
    if (seen.containsKey(complement)) return new int[]{seen.get(complement), i};
    seen.put(nums[i], i);
}
```

### Pattern B: Sliding Window Frequency
```java
Map<Character, Integer> window = new HashMap<>();
for (int r = 0; r < s.length(); r++) {
    window.merge(s.charAt(r), 1, Integer::sum);
    while (window.size() > k) {
        char l = s.charAt(left++);
        window.merge(l, -1, Integer::sum);
        if (window.get(l) == 0) window.remove(l);
    }
}
```

### Pattern C: Group By Key (computeIfAbsent)
```java
Map<Integer, List<Integer>> grouped = new HashMap<>();
for (int x : arr) grouped.computeIfAbsent(x % 10, k -> new ArrayList<>()).add(x);
```

### Pattern D: Cache / Memoization
```java
Map<Integer, Integer> memo = new HashMap<>();
int fib(int n) {
    if (n < 2) return n;
    return memo.computeIfAbsent(n, k -> fib(k - 1) + fib(k - 2));
}
```

### Pattern E: Top-K Frequent
```java
Map<Integer, Integer> freq = new HashMap<>();
for (int x : nums) freq.merge(x, 1, Integer::sum);
PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> freq.get(a) - freq.get(b));
for (int k : freq.keySet()) {
    pq.offer(k);
    if (pq.size() > k) pq.poll();
}
```

---

## 7. Performance Notes (Industry)

- **Time:** O(1) average for get/put/remove. O(log n) worst-case after Java 8 (treeified buckets when >8 collisions).
- **Load factor 0.75** balances time vs space. Increasing reduces memory, increases collisions.
- **Null keys/values allowed** (one null key, multiple null values). `ConcurrentHashMap` disallows nulls.
- **Not thread-safe** → use `ConcurrentHashMap` or `Collections.synchronizedMap()` for concurrency.
- **LinkedHashMap** if insertion/access order matters (LRU cache).
- **TreeMap** if you need sorted keys (O(log n)).

### LRU Cache (Classic Industry + LeetCode 146)
```java
class LRUCache {
    private final int cap;
    private final LinkedHashMap<Integer, Integer> map;
    
    public LRUCache(int capacity) {
        this.cap = capacity;
        this.map = new LinkedHashMap<>(capacity, 0.75f, true) {  // accessOrder = true
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> e) {
                return size() > cap;
            }
        };
    }
    
    public int get(int key) { return map.getOrDefault(key, -1); }
    public void put(int key, int value) { map.put(key, value); }
}
```

---

## 8. Sorting a HashMap

```java
// By values ascending
List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
list.sort(Map.Entry.comparingByValue());

// By values descending
list.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

// By keys
list.sort(Map.Entry.comparingByKey());

// Using streams
map.entrySet().stream()
   .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
   .limit(10)
   .forEach(System.out::println);
```

---

## 9. Pitfalls to Avoid

1. **Modifying during iteration** → use `Iterator.remove()` or `computeIfPresent`.
2. **Using `==` for key comparison of objects** → override `equals()` & `hashCode()`.
3. **Mutable keys** → if key changes after insertion, lookup breaks.
4. **`get()` returning null for existing null-value keys** → use `containsKey()` when needed.
5. **Assuming thread safety** → use `ConcurrentHashMap`.

---

## TL;DR — The 4 Methods You'll Use 95% of the Time

```java
map.getOrDefault(key, 0)           // read with default
map.merge(key, 1, Integer::sum)    // increment
map.computeIfAbsent(key, k -> ...) // build nested structures
map.remove(key, value)             // conditional remove
```

Master these + `entrySet()` traversal and you're covered for both LeetCode and production code.