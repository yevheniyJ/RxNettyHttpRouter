package rx.netty.http.router.internal;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;
import static rx.netty.http.router.internal.HttpMappingsHelper.getPathVariables;
import static rx.netty.http.router.internal.HttpMappingsHelper.match;

public class HttpMappingsHelperTest {

    private static final String REGION_MAPPING = "/region/:city";

    @Test
    public void matchTest() {
        String target1 = "/region/";
        String target2 = "/region/Lviv/";
        String target3 = "/location/Lviv";
        String target4 = "/region/Lviv/Ukraine";

        assertFalse(match(REGION_MAPPING, target1));
        assertTrue(match(REGION_MAPPING, target2));
        assertFalse(match(REGION_MAPPING, target3));
        assertFalse(match(REGION_MAPPING, target4));
    }

    @Test
    public void getPathVariablesTest() {
        String target1 = "/region/Odessa/";
        String target2 = "/region/Lviv";
        String target3 = "region/Kiev";
        String target4 = "region/Kharkov/";
        Map<String, String> pathsParams1 = getPathVariables(REGION_MAPPING, target1);
        Map<String, String> pathsParams2 = getPathVariables(REGION_MAPPING, target2);
        Map<String, String> pathsParams3 = getPathVariables(REGION_MAPPING, target3);
        Map<String, String> pathsParams4 = getPathVariables(REGION_MAPPING, target4);

        assertEquals(1, pathsParams1.size());
        assertEquals(1, pathsParams2.size());
        assertEquals(1, pathsParams3.size());
        assertEquals(1, pathsParams4.size());
        assertEquals(pathsParams1.get("city"), "Odessa");
        assertEquals(pathsParams2.get("city"), "Lviv");
        assertEquals(pathsParams3.get("city"), "Kiev");
        assertEquals(pathsParams4.get("city"), "Kharkov");
    }
}
