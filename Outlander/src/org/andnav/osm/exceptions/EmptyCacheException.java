// Created by plusminus on 14:29:37 - 12.10.2008
package org.andnav.osm.exceptions;

public class EmptyCacheException extends Exception {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final long serialVersionUID = -6096533745569312071L;

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public EmptyCacheException() {
        super();
    }

    public EmptyCacheException(final String detailMessage, final Throwable throwable) {
        super(detailMessage, throwable);
    }

    public EmptyCacheException(final String detailMessage) {
        super(detailMessage);
    }

    public EmptyCacheException(final Throwable throwable) {
        super(throwable);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
