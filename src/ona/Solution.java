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

    private static void calculate(String stringUrl){
        if(stringUrl.isEmpty()) stringUrl = URL_ONA_RAW_DATA;
        try {
            URL url = new URL(stringUrl);
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("Retrieving data");
            List<Community> communities = mapper.readValue(url,new TypeReference<List<Community>>(){});
            System.out.println("Data Retrieved");
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
                response.getCommunityWaterPointsRanking().add(new Response.Wrapper().set(entry.getKey(),communityRanking(entry.getValue().get(KEY_WATER_FUNCTIONING_YES),waterPointCount)));
            }

            String jsonResponse = mapper.writeValueAsString(response);
            System.out.println(jsonResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static float communityRanking(int functioningWaterPoints, int waterPointCount) {
        if(functioningWaterPoints==0) return 0;
        return (functioningWaterPoints * 100.0f) / waterPointCount;
    }

    public static void main(String[] args) {
        calculate(getStringUrl());
    }

    private static String getStringUrl() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your raw data url and press enter (optional) : ");
        return scanner.nextLine();
    }
}
