/*
 * Copyright 2011 Greg Milette and Adam Stroud
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package at.the.gogo.parkoid.sensors.proximity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Factory to create a pending intent to use with proximity alerts.
 * 
 * @author Adam Stroud &#60;<a
 *         href="mailto:adam.stroud@gmail.com">adam.stroud@gmail.com</a>&#62;
 */
public class ProximityPendingIntentFactory {
    public static final String PROXIMITY_ACTION = "at.the.gogo.parkoid.sensors.PROXIMITY";
    private static final int   REQUEST_CODE     = 0;

    public static PendingIntent createPendingIntent(final Context context) {
        return PendingIntent.getBroadcast(context, REQUEST_CODE, new Intent(
                PROXIMITY_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
