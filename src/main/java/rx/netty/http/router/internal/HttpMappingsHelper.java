package rx.netty.http.router.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class for verifying http request uri to mapping and retrieving from request path variables
 */
public final class HttpMappingsHelper {

    private static final char PATH_VARIABLE_CHAR = ':';
    private static final String PATH_SEPARATOR = "/";

    private HttpMappingsHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param mapping mapping from map
     * @param target  request uri
     * @return true if request uri match mapping from map, for example /region/:city will match for uri /region/Lviv and won't match for uri region/Ukraine/Lviv or /location/Lviv
     */
    public static boolean match(String mapping, String target) {
        mapping = uriStabilization(mapping, target);
        target = uriStabilization(target, mapping);
        String[] mappingsSplit = mapping.split(PATH_SEPARATOR);
        String[] targetSplit = target.split(PATH_SEPARATOR);
        if (mappingsSplit.length == targetSplit.length) {
            for (int i = 0; i < mappingsSplit.length; i++) {
                String subMapping = mappingsSplit[i];
                if ((subMapping.length() == 0 || subMapping.charAt(0) != PATH_VARIABLE_CHAR) && !subMapping.equals(targetSplit[i])) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * @param mapping mapping from map
     * @param target  request uri
     * @return map of path params, for example mapping = /region/:city and target = /region/Lviv will create map with single entry : key = city and value = Lviv
     */
    public static Map<String, String> getPathVariables(String mapping, String target) {
        Map<String, String> map = new HashMap<>();
        mapping = uriStabilization(mapping, target);
        target = uriStabilization(target, mapping);
        String[] splitMapping = mapping.split(PATH_SEPARATOR);
        String[] splitTarget = target.split(PATH_SEPARATOR);
        for (int i = 0; i < splitMapping.length; i++) {
            String subPath = splitMapping[i];
            if (subPath.length() > 0 && subPath.charAt(0) == PATH_VARIABLE_CHAR) {
                String key = subPath.substring(1);
                String value = splitTarget[i];
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * @param uri1 first uri
     * @param uri2 second uri
     * @return {@code urr1} with first char {@value #PATH_SEPARATOR} if it did not starts with him and {@code urr2} starts with {@value #PATH_SEPARATOR}, otherwise {@code uri1}
     */
    private static String uriStabilization(String uri1, String uri2) {
        if (!uri1.startsWith(PATH_SEPARATOR) && uri2.startsWith("/")) {
            return "/" + uri1;
        } else {
            return uri1;
        }
    }
}
