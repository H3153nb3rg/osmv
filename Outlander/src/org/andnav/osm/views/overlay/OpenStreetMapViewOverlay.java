// Created by plusminus on 20:32:01 - 27.09.2008
package org.andnav.osm.views.overlay;

import org.andnav.osm.util.constants.OpenStreetMapConstants;
import org.andnav.osm.views.OpenStreetMapView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Base class representing an overlay which may be displayed on top of a
 * {@link OpenStreetMapView}. To add an overlay, subclass this class, create an
 * instance, and add it to the list obtained from getOverlays() of
 * {@link OpenStreetMapView}.
 * 
 * @author Nicolas Gramlich
 */
public abstract class OpenStreetMapViewOverlay implements OpenStreetMapConstants {

    // ===========================================================
    // Constants
    // ===========================================================

    public final static String REFRESH          = "REFRESH";

    // ===========================================================
    // Fields
    // ===========================================================
    private BroadcastReceiver  mMessageReceiver = new BroadcastReceiver() {

                                                    @Override
                                                    public void onReceive(Context context, Intent intent) {

                                                        OpenStreetMapViewOverlay.this.messageReceived(context, intent);

                                                    }

                                                };

    // ===========================================================
    // Constructors
    // ===========================================================

    public OpenStreetMapViewOverlay() {
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    protected void registerMessageReceiver(Context ctx, String filter) {
        LocalBroadcastManager.getInstance(ctx).registerReceiver(mMessageReceiver, new IntentFilter(filter));

    }

    // ===========================================================
    // Methods for SuperClass/Interfaces
    // ===========================================================

    /**
     * for asynchronous communication from different activities back to our
     * overlays
     * 
     * @param context
     * @param intent
     */
    protected void messageReceived(Context context, Intent intent) {

    }

    /**
     * Managed Draw calls gives Overlays the possibility to first draw manually
     * and after that do a final draw. This is very useful, i sth. to be drawn
     * needs to be <b>topmost</b>.
     */
    public void onManagedDraw(final Canvas c, final OpenStreetMapView osmv) {
        onDraw(c, osmv);
        onDrawFinished(c, osmv);
    }

    protected abstract void onDraw(final Canvas c, final OpenStreetMapView osmv);

    protected abstract void onDrawFinished(final Canvas c, final OpenStreetMapView osmv);

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * By default does nothing (<code>return false</code>). If you handled the
     * Event, return <code>true</code>, otherwise return <code>false</code>. If
     * you returned <code>true</code> none of the following Overlays or the
     * underlying {@link OpenStreetMapView} has the chance to handle this event.
     */
    public boolean onKeyDown(final int keyCode, final KeyEvent event, final OpenStreetMapView mapView) {
        return false;
    }

    /**
     * By default does nothing (<code>return false</code>). If you handled the
     * Event, return <code>true</code>, otherwise return <code>false</code>. If
     * you returned <code>true</code> none of the following Overlays or the
     * underlying {@link OpenStreetMapView} has the chance to handle this event.
     */
    public boolean onKeyUp(final int keyCode, final KeyEvent event, final OpenStreetMapView mapView) {
        return false;
    }

    /**
     * <b>You can prevent all(!) other Touch-related events from happening!</b><br />
     * By default does nothing (<code>return false</code>). If you handled the
     * Event, return <code>true</code>, otherwise return <code>false</code>. If
     * you returned <code>true</code> none of the following Overlays or the
     * underlying {@link OpenStreetMapView} has the chance to handle this event.
     */
    public boolean onTouchEvent(final MotionEvent event, final OpenStreetMapView mapView) {
        return false;
    }

    /**
     * By default does nothing (<code>return false</code>). If you handled the
     * Event, return <code>true</code>, otherwise return <code>false</code>. If
     * you returned <code>true</code> none of the following Overlays or the
     * underlying {@link OpenStreetMapView} has the chance to handle this event.
     */
    public boolean onTrackballEvent(final MotionEvent event, final OpenStreetMapView mapView) {
        return false;
    }

    /**
     * By default does nothing (<code>return false</code>). If you handled the
     * Event, return <code>true</code>, otherwise return <code>false</code>. If
     * you returned <code>true</code> none of the following Overlays or the
     * underlying {@link OpenStreetMapView} has the chance to handle this event.
     */
    public boolean onSingleTapUp(final MotionEvent e, final OpenStreetMapView openStreetMapView) {
        return false;
    }

    /**
     * By default does nothing (<code>return false</code>). If you handled the
     * Event, return <code>true</code>, otherwise return <code>false</code>. If
     * you returned <code>true</code> none of the following Overlays or the
     * underlying {@link OpenStreetMapView} has the chance to handle this event.
     */
    public boolean onLongPress(final MotionEvent e, final OpenStreetMapView openStreetMapView) {
        return false;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
