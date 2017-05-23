package ona;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Solution {

    private static final String URL_ONA_RAW_DATA = "https://raw.githubusercontent.com/onaio/ona-tech/master/data/water_points.json";
    private static final String STATE_WATER_FUNCTIONING_YES = "yes";
    private static final String STATE_WATER_FUNCTIONING_NO = "no";

    private static final String KEY_WATER_FUNCTIONING_YES = "water_function";
    private static final String KEY_WATER_FUNCTIONING_NO = "no_water_function";
    private static final String KEY_WATER_FUNCTIONING_UNKNOWN = "unknown_water_function";

    private static final String DEFAULT_SPLIT_KEY = "road_available";

    private void calculate(String stringUrl){
        if(stringUrl.isEmpty()) stringUrl = URL_ONA_RAW_DATA;
        try {
            URL url = new URL(stringUrl);
            ObjectMapper mapper = new ObjectMapper();
            List<Community> communities = mapper.readValue(url,new TypeReference<List<Community>>(){});
            Map<String,HashMap<String,Integer>> multiMap = new HashMap<>();

            for(Community community:communities){
                if(!multiMap.containsKey(community.getName())){
                    //Initialize HashMap with appropriate keys
                    HashMap<String,Integer> hashMap = new HashMap<String,Integer>(3){{
                        put(KEY_WATER_FUNCTIONING_YES,0);
                        put(KEY_WATER_FUNCTIONING_NO,0);
                        put(KEY_WATER_FUNCTIONING_UNKNOWN,0);
                    }};
                    multiMap.put(community.getName(),hashMap);
                }
                if(STATE_WATER_FUNCTIONING_YES.equals(community.getWaterFunctioning())){
                    multiMap.get(community.getName()).put(KEY_WATER_FUNCTIONING_YES,multiMap.get(community.getName()).get(KEY_WATER_FUNCTIONING_YES)+1);
                }
                else if(STATE_WATER_FUNCTIONING_NO.equals(community.getWaterFunctioning())){
                    multiMap.get(community.getName()).put(KEY_WATER_FUNCTIONING_NO,multiMap.get(community.getName()).get(KEY_WATER_FUNCTIONING_NO)+1);
                }
                else {
                    multiMap.get(community.getName()).put(KEY_WATER_FUNCTIONING_UNKNOWN,multiMap.get(community.getName()).get(KEY_WATER_FUNCTIONING_UNKNOWN)+1);
                }
            }

            Response response = new Response(0);
            for(Map.Entry<String, HashMap<String, Integer>> entry : multiMap.entrySet()) {
                int waterPointCount = entry.getValue().get(KEY_WATER_FUNCTIONING_YES) + entry.getValue().get(KEY_WATER_FUNCTIONING_NO) + entry.getValue().get(KEY_WATER_FUNCTIONING_UNKNOWN);
                response.setNumberFunctional(response.getNumberFunctional() + entry.getValue().get(KEY_WATER_FUNCTIONING_YES));
                response.getCommunityWaterPointsCount().add(new Response.Wrapper<>().set(entry.getKey(),waterPointCount));
                response.getCommunityWaterPointsRanking().add(new Response.Wrapper().set(entry.getKey(), calculatePercentage(entry.getValue().get(KEY_WATER_FUNCTIONING_YES),waterPointCount)));
            }

            String jsonResponse = mapper.writeValueAsString(response);
            System.out.println(jsonResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private float calculatePercentage(int count, int totalCount) {
        if(count==0) return 0;
        return (count * 100.0f) / totalCount;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.getCalculateSplitKey(getInput("Enter your raw data url and press enter (optional) : "),getInput("Enter your split key (defaults to 'road_available') : "));
    }

    private static String getInput(String hint) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(hint);
        return scanner.nextLine();
    }

    String getCalculateSplitKey(String json, String splitKey){
        if(json.isEmpty()) json = URL_ONA_RAW_DATA;
        if(splitKey.isEmpty()) splitKey = DEFAULT_SPLIT_KEY;
        try {
            URL url = null;
            if(json.startsWith("http")) url = new URL(json);

            ObjectMapper mapper = new ObjectMapper();

            ArrayList<Map<String, String>> jsonArray = url != null ? mapper.readValue(url,new TypeReference<ArrayList<Map>>(){}) : mapper.readValue(json,new TypeReference<ArrayList<Map>>(){});

            Map<String,MutableInt> splitKeyCountMap = new HashMap<>();
            int total=0;
            for(Map<String, String> jsonObject: jsonArray){
                if(jsonObject.containsKey(splitKey)){
                    MutableInt count = splitKeyCountMap.get(jsonObject.get(splitKey));
                    if (count == null) {
                        splitKeyCountMap.put(jsonObject.get(splitKey), new MutableInt());
                    }
                    else {
                        count.increment();
                    }
                    ++total;
                }
            }

            Map<String,Float> response = new TreeMap<>();

            for(Map.Entry<String,MutableInt> mapEntry : splitKeyCountMap.entrySet()){
                response.put(mapEntry.getKey(),calculatePercentage(mapEntry.getValue().get(),total));
            }

            System.out.println(response);
            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    class MutableInt {
        int value = 1; // note that we start at 1 since we're counting
        void increment() { ++value;      }
        int get()       { return value; }

        @Override
        public String toString() {
            return String.valueOf(get());
        }
    }
}
