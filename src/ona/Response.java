package ona;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Response {

    @JsonProperty("number_functional")
    private int numberFunctional;
    @JsonProperty("number_water_points")
    private ArrayList<Wrapper> communityWaterPointsCount;
    @JsonProperty("community_ranking")
    private ArrayList<Wrapper> communityWaterPointsRanking;

    Response(int numberFunctional) {
        this.numberFunctional = numberFunctional;
        this.communityWaterPointsCount = new ArrayList<>();
        this.communityWaterPointsRanking = new ArrayList<>();
    }

    int getNumberFunctional() {
        return numberFunctional;
    }

    void setNumberFunctional(int numberFunctional) {
        this.numberFunctional = numberFunctional;
    }

    ArrayList<Wrapper> getCommunityWaterPointsCount() {
        return communityWaterPointsCount;
    }

    ArrayList<Wrapper> getCommunityWaterPointsRanking() {
        return communityWaterPointsRanking;
    }

    static class Wrapper<T> {

        private Map<String,T> other = new HashMap<>();

        @JsonAnyGetter
        private Map<String, T> getOther() {
            return other;
        }

        @JsonAnySetter
        Wrapper set(String key, T value) {
            other.put(key,value);
            return this;
        }
    }
}
