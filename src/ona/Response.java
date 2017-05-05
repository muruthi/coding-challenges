package ona;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 001590 on 2017-05-04.
 */
public class Response {

    @JsonProperty("number_functional")
    private int numberFunctional;
    @JsonProperty("number_water_points")
    private ArrayList<Wrapper> communityWaterPointsCount;
    @JsonProperty("community_ranking")
    private ArrayList<Wrapper> communityWaterPointsRanking;

    public Response(int numberFunctional) {
        this.numberFunctional = numberFunctional;
        this.communityWaterPointsCount = new ArrayList<>();
        this.communityWaterPointsRanking = new ArrayList<>();
    }

    public int getNumberFunctional() {
        return numberFunctional;
    }

    public void setNumberFunctional(int numberFunctional) {
        this.numberFunctional = numberFunctional;
    }

    public ArrayList<Wrapper> getCommunityWaterPointsCount() {
        return communityWaterPointsCount;
    }

    public void setCommunityWaterPointsCount(ArrayList<Wrapper> communityWaterPointsCount) {
        this.communityWaterPointsCount = communityWaterPointsCount;
    }

    public ArrayList<Wrapper> getCommunityWaterPointsRanking() {
        return communityWaterPointsRanking;
    }

    public void setCommunityWaterPointsRanking(ArrayList<Wrapper> communityWaterPointsRanking) {
        this.communityWaterPointsRanking = communityWaterPointsRanking;
    }

    public static class Wrapper<T> {

        private Map<String,T> other = new HashMap<>();

        @JsonAnyGetter
        public Map<String, T> getOther() {
            return other;
        }

        @JsonAnySetter
        public Wrapper set(String key, T value) {
            other.put(key,value);
            return this;
        }
    }
}
