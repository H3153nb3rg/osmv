package org.geonames;

import java.net.URL;

import org.outlander.io.JSON.CloudmadeRouteParser;
import org.outlander.model.TurnRoute;
import org.outlander.utils.Ut;

public class CloudmadeRequests {

    private static String BaseURL = "http://routes.cloudmade.com/";
    private static String ApiKey  = "8ee2a50541944fb9bcedded5165f09d9";
    private static String version = "/api/0.3";

    // http://routes.cloudmade.com/8ee2a50541944fb9bcedded5165f09d9/api/0.3/47.25976,9.58423,47.26117,9.59882/bicycle.js
    public static String getRoutingInfoBrutto(final double sourceLat, final double sourceLon, final double destLat, final double destLon, final boolean byFeed) {

        final String requestUrl = BaseURL + ApiKey + version + "/" + sourceLat + "," + sourceLon + "," + destLat + "," + destLon
                + ((byFeed) ? "/foot.js" : "/car.js");

        String result = null;
        try {
            result = WebService.webGetString(new URL(requestUrl));
        }
        catch (Exception x) {
            Ut.e("get Routing Info failed: " + x.toString());
        }
        return result;
    }

    public static TurnRoute getRoutingInfo(final double sourceLat, final double sourceLon, final double destLat, final double destLon, final boolean byFeed) {
        TurnRoute route = null;

        final String result = getRoutingInfoBrutto(sourceLat, sourceLon, destLat, destLon, byFeed);

        if (result != null) {
            route = CloudmadeRouteParser.parseJSONData(result);
        }

        return route;
    }

}
