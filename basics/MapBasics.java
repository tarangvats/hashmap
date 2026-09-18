
import java.util.HashMap;
import java.util.Map;

class MapBasics{
    public MapBasics(){

    }
    public static void printMap1(Map<String, Integer> hashMap){
        for(Map.Entry<String, Integer> e : hashMap.entrySet()){
            System.out.println(e.getKey()+ " : " + e.getValue());
        }
    }
    public static void printMap2(Map<Character, Integer> hashMap){
        for(Map.Entry<Character, Integer> e : hashMap.entrySet()){
            System.out.println(e.getKey()+ " : " + e.getValue());
        }
    }

    public static void main(String[] args){
        Map<Character, Integer> map1 = new HashMap<>();
        String s = "aabdsdndkdksdmkdmkdcn";
        for(int i = 0;i<s.length(); i++){
            map1.merge(s.charAt(i),1,Integer::sum);
        }
        Map<String, Integer>map2 = new HashMap<>();
        for(int i = 0; i<s.length()-1 ;i=i+2 ){
            String str = ""+s.charAt(i)+s.charAt(i+1); 
            map2.put(str, map2.getOrDefault(str,0)+1);
        }
        System.out.println("Map1 : ");
        printMap2(map1);
        System.out.println("Map2 : ");
        printMap1(map2);


        System.out.println("Hello World");
    }

}