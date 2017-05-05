package ona;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by 001590 on 2017-05-04.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Community {
    private String name;
    private String waterFunctioning;

    @JsonCreator
    public Community(
            @JsonProperty("communities_villages") String name,
            @JsonProperty("water_functioning") String waterFunctioning) {
        this.name = name;
        this.waterFunctioning = waterFunctioning;
    }

    public String getName() {
        return name;
    }

    public String getWaterFunctioning() {
        return waterFunctioning;
    }
}
