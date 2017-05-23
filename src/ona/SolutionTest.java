package ona;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by 001590 on 2017-05-23.
 */
class SolutionTest {
    @Test
    void testGetCalculateSplitKey() {
        String json = "[{\"road_available\": \"no\"},{\"road_available\": \"yes\"},{\"road_available\": \"unknown\"},{\"road_available\": \"yes\"}]";
        String splitKey = "road_available";

        Solution solution = new Solution();
        String result = solution.getCalculateSplitKey(json, splitKey);
        assertEquals("{no=25.0, unknown=25.0, yes=50.0}", result);

    }
}