// Copyright (c) 2010, Freddy Martens (http://atstechlab.wordpress.com),
// MindTheRobot (http://mindtherobot.com/blog/) and contributors
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification,
// are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following disclaimer in the documentation
// and/or other materials provided with the distribution.
// * Neither the name of Ondrej Zara nor the names of its contributors may be
// used
// to endorse or promote products derived from this software without specific
// prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED.
// IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
// DIRECT,
// INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING,
// BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
// NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
// EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.outlander.views;

import org.outlander.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public final class GaugeView extends View {

    private static final String TAG                    = GaugeView.class.getSimpleName();

    // drawing tools
    private RectF               rimRect;
    private Paint               rimPaint;
    private Paint               rimCirclePaint;

    private RectF               faceRect;
    private Bitmap              faceTexture;
    private Paint               facePaint;
    private Paint               rimShadowPaint;

    private Paint               scalePaint;
    private RectF               scaleRect;

    private RectF               valueRect;
    private RectF               rangeRect;

    private Paint               rangeOkPaint;
    private Paint               rangeWarningPaint;
    private Paint               rangeErrorPaint;
    private Paint               rangeAllPaint;

    private Paint               valueOkPaint;
    private Paint               valueWarningPaint;
    private Paint               valueErrorPaint;
    private Paint               valueAllPaint;

    private Paint               unitPaint;
    private Path                unitPath;
    private RectF               unitRect;

    private Paint               lowerTitlePaint;
    private Paint               upperTitlePaint;
    private Path                lowerTitlePath;
    private Path                upperTitlePath;
    private RectF               titleRect;

    private Paint               handPaint;
    private Path                handPath;

    private Paint               handScrewPaint;

    private Paint               backgroundPaint;
    // end drawing tools

    private Bitmap              background;                                              // holds
                                                                                          // the
                                                                                          // cached
                                                                                          // static
                                                                                          // part

    // scale configuration
    // Values passed as property. Defaults are set here.
    private boolean             showHand               = false;
    private boolean             showGauge              = false;
    private boolean             showRange              = false;

    private int                 totalNotches           = 120;                            // Total
                                                                                          // number
                                                                                          // of
                                                                                          // notches
                                                                                          // on
                                                                                          // the
                                                                                          // scale.
    private int                 incrementPerLargeNotch = 10;
    private int                 incrementPerSmallNotch = 2;

    private int                 scaleColor             = 0x9f004d0f;
    private int                 scaleCenterValue       = 0;                              // the
                                                                                          // one
                                                                                          // in
                                                                                          // the
                                                                                          // top
                                                                                          // center
                                                                                          // (12
                                                                                          // o'clock),
                                                                                          // this
                                                                                          // corresponds
                                                                                          // with
                                                                                          // -90
                                                                                          // degrees
    private int                 scaleMinValue          = -90;
    private int                 scaleMaxValue          = 120;
    private float               degreeMinValue         = 0;
    private float               degreeMaxValue         = 0;

    private int                 rangeOkColor           = 0x9f00ff00;
    private int                 rangeOkMinValue        = scaleMinValue;
    private int                 rangeOkMaxValue        = 45;
    private float               degreeOkMinValue       = 0;
    private float               degreeOkMaxValue       = 0;

    private int                 rangeWarningColor      = 0x9fff8800;
    private int                 rangeWarningMinValue   = rangeOkMaxValue;
    private int                 rangeWarningMaxValue   = 80;
    private float               degreeWarningMinValue  = 0;
    private float               degreeWarningMaxValue  = 0;

    private int                 rangeErrorColor        = 0x9fff0000;
    private int                 rangeErrorMinValue     = rangeWarningMaxValue;
    private int                 rangeErrorMaxValue     = 120;
    private float               degreeErrorMinValue    = 0;
    private float               degreeErrorMaxValue    = 0;

    private String              lowerTitle             = "Outlander";
    private String              upperTitle             = "Speedometer";
    private String              unitTitle              = "\u2103";

    // Fixed values.
    private static final float  scalePosition          = 0.10f;                          // The
                                                                                          // distance
                                                                                          // from
                                                                                          // the
                                                                                          // rim
                                                                                          // to
                                                                                          // the
                                                                                          // scale
    private static final float  valuePosition          = 0.285f;                         // The
                                                                                          // distance
                                                                                          // from
                                                                                          // the
                                                                                          // rim
                                                                                          // to
                                                                                          // the
                                                                                          // ranges
    private static final float  rangePosition          = 0.122f;                         // The
                                                                                          // distance
                                                                                          // from
                                                                                          // the
                                                                                          // rim
                                                                                          // to
                                                                                          // the
                                                                                          // ranges
    private static final float  titlePosition          = 0.145f;                         // The
                                                                                          // Distance
                                                                                          // from
                                                                                          // the
                                                                                          // rim
                                                                                          // to
                                                                                          // the
                                                                                          // titles
    private static final float  unitPosition           = 0.300f;                         // The
                                                                                          // distance
                                                                                          // from
                                                                                          // the
                                                                                          // rim
                                                                                          // to
                                                                                          // the
                                                                                          // unit
    private static final float  rimSize                = 0.02f;

    private final float         degreesPerNotch        = 360.0f / totalNotches;
    private static final int    centerDegrees          = -90;                            // the
                                                                                          // one
                                                                                          // in
                                                                                          // the
                                                                                          // top
                                                                                          // center
                                                                                          // (12
                                                                                          // o'clock),
                                                                                          // this
                                                                                          // corresponds
                                                                                          // with
                                                                                          // -90
                                                                                          // degrees

    // hand dynamics
    private boolean             dialInitialized        = false;
    private float               currentValue           = scaleCenterValue;
    private float               targetValue            = scaleCenterValue;
    private float               dialVelocity           = 0.0f;
    private float               dialAcceleration       = 0.0f;
    private long                lastDialMoveTime       = -1L;

    public GaugeView(final Context context) {
        super(context);
        init(context, null);
    }

    public GaugeView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GaugeView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        final Bundle bundle = (Bundle) state;
        final Parcelable superState = bundle.getParcelable("superState");
        super.onRestoreInstanceState(superState);

        dialInitialized = bundle.getBoolean("dialInitialized");
        currentValue = bundle.getFloat("currentValue");
        targetValue = bundle.getFloat("targetValue");
        dialVelocity = bundle.getFloat("dialVelocity");
        dialAcceleration = bundle.getFloat("dialAcceleration");
        lastDialMoveTime = bundle.getLong("lastDialMoveTime");
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();

        final Bundle state = new Bundle();
        state.putParcelable("superState", superState);
        state.putBoolean("dialInitialized", dialInitialized);
        state.putFloat("currentValue", currentValue);
        state.putFloat("targetValue", targetValue);
        state.putFloat("dialVelocity", dialVelocity);
        state.putFloat("dialAcceleration", dialAcceleration);
        state.putLong("lastDialMoveTime", lastDialMoveTime);
        return state;
    }

    private void init(final Context context, final AttributeSet attrs) {
        // Get the properties from the resource file.
        if ((context != null) && (attrs != null)) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Dial);
            showRange = a.getBoolean(R.styleable.Dial_showRange, showRange);
            showGauge = a.getBoolean(R.styleable.Dial_showGauge, showGauge);
            showHand = a.getBoolean(R.styleable.Dial_showHand, showHand);

            totalNotches = a.getInt(R.styleable.Dial_totalNotches, totalNotches);
            incrementPerLargeNotch = a.getInt(R.styleable.Dial_incrementPerLargeNotch, incrementPerLargeNotch);
            incrementPerSmallNotch = a.getInt(R.styleable.Dial_incrementPerSmallNotch, incrementPerSmallNotch);
            scaleCenterValue = a.getInt(R.styleable.Dial_scaleCenterValue, scaleCenterValue);
            scaleColor = a.getInt(R.styleable.Dial_scaleColor, scaleColor);
            scaleMinValue = a.getInt(R.styleable.Dial_scaleMinValue, scaleMinValue);
            scaleMaxValue = a.getInt(R.styleable.Dial_scaleMaxValue, scaleMaxValue);
            rangeOkColor = a.getInt(R.styleable.Dial_rangeOkColor, rangeOkColor);
            rangeOkMinValue = a.getInt(R.styleable.Dial_rangeOkMinValue, rangeOkMinValue);
            rangeOkMaxValue = a.getInt(R.styleable.Dial_rangeOkMaxValue, rangeOkMaxValue);
            rangeWarningColor = a.getInt(R.styleable.Dial_rangeWarningColor, rangeWarningColor);
            rangeWarningMinValue = a.getInt(R.styleable.Dial_rangeWarningMinValue, rangeWarningMinValue);
            rangeWarningMaxValue = a.getInt(R.styleable.Dial_rangeWarningMaxValue, rangeWarningMaxValue);
            rangeErrorColor = a.getInt(R.styleable.Dial_rangeErrorColor, rangeErrorColor);
            rangeErrorMinValue = a.getInt(R.styleable.Dial_rangeErrorMinValue, rangeErrorMinValue);
            rangeErrorMaxValue = a.getInt(R.styleable.Dial_rangeErrorMaxValue, rangeErrorMaxValue);
            final String unitTitle = a.getString(R.styleable.Dial_unitTitle);
            final String lowerTitle = a.getString(R.styleable.Dial_lowerTitle);
            final String upperTitle = a.getString(R.styleable.Dial_upperTitle);
            if (unitTitle != null) {
                this.unitTitle = unitTitle;
            }
            if (lowerTitle != null) {
                this.lowerTitle = lowerTitle;
            }
            if (upperTitle != null) {
                this.upperTitle = upperTitle;
            }
        }
        degreeMinValue = valueToAngle(scaleMinValue) + GaugeView.centerDegrees;
        degreeMaxValue = valueToAngle(scaleMaxValue) + GaugeView.centerDegrees;
        degreeOkMinValue = valueToAngle(rangeOkMinValue) + GaugeView.centerDegrees;
        degreeOkMaxValue = valueToAngle(rangeOkMaxValue) + GaugeView.centerDegrees;
        degreeWarningMinValue = valueToAngle(rangeWarningMinValue) + GaugeView.centerDegrees;
        degreeWarningMaxValue = valueToAngle(rangeWarningMaxValue) + GaugeView.centerDegrees;
        degreeErrorMinValue = valueToAngle(rangeErrorMinValue) + GaugeView.centerDegrees;
        degreeErrorMaxValue = valueToAngle(rangeErrorMaxValue) + GaugeView.centerDegrees;

        initDrawingTools();
    }

    private void initDrawingTools() {
        rimRect = new RectF(0.0f, 0.0f, 1.0f, 1.0f);

        faceRect = new RectF();
        faceRect.set(rimRect.left + GaugeView.rimSize, rimRect.top + GaugeView.rimSize, rimRect.right - GaugeView.rimSize, rimRect.bottom - GaugeView.rimSize);

        scaleRect = new RectF();
        scaleRect.set(faceRect.left + GaugeView.scalePosition, faceRect.top + GaugeView.scalePosition, faceRect.right - GaugeView.scalePosition,
                faceRect.bottom - GaugeView.scalePosition);

        rangeRect = new RectF();
        rangeRect.set(faceRect.left + GaugeView.rangePosition, faceRect.top + GaugeView.rangePosition, faceRect.right - GaugeView.rangePosition,
                faceRect.bottom - GaugeView.rangePosition);

        valueRect = new RectF();
        valueRect.set(faceRect.left + GaugeView.valuePosition, faceRect.top + GaugeView.valuePosition, faceRect.right - GaugeView.valuePosition,
                faceRect.bottom - GaugeView.valuePosition);

        titleRect = new RectF();
        titleRect.set(faceRect.left + GaugeView.titlePosition, faceRect.top + GaugeView.titlePosition, faceRect.right - GaugeView.titlePosition,
                faceRect.bottom - GaugeView.titlePosition);

        unitRect = new RectF();
        unitRect.set(faceRect.left + GaugeView.unitPosition, faceRect.top + GaugeView.unitPosition, faceRect.right - GaugeView.unitPosition, faceRect.bottom
                - GaugeView.unitPosition);

        faceTexture = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gauge_back);
        final BitmapShader paperShader = new BitmapShader(faceTexture, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
        final Matrix paperMatrix = new Matrix();
        paperMatrix.setScale(1.0f / faceTexture.getWidth(), 1.0f / faceTexture.getHeight());

        paperShader.setLocalMatrix(paperMatrix);

        rimShadowPaint = new Paint();
        rimShadowPaint.setShader(new RadialGradient(0.5f, 0.5f, faceRect.width() / 2.0f, new int[] { 0x00000000, 0x00000500, 0x50000500 }, new float[] { 0.96f,
                0.96f, 0.99f }, Shader.TileMode.MIRROR));
        rimShadowPaint.setStyle(Paint.Style.FILL);

        // the linear gradient is a bit skewed for realism
        rimPaint = new Paint();
        rimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        rimPaint.setShader(new LinearGradient(0.40f, 0.0f, 0.60f, 1.0f, Color.rgb(0xf0, 0xf5, 0xf0), Color.rgb(0x30, 0x31, 0x30), Shader.TileMode.CLAMP));

        rimCirclePaint = new Paint();
        rimCirclePaint.setAntiAlias(true);
        rimCirclePaint.setStyle(Paint.Style.STROKE);
        rimCirclePaint.setColor(Color.argb(0x4f, 0x33, 0x36, 0x33));
        rimCirclePaint.setStrokeWidth(0.005f);

        facePaint = new Paint();
        facePaint.setFilterBitmap(true);
        facePaint.setStyle(Paint.Style.FILL);
        facePaint.setShader(paperShader);

        scalePaint = new Paint();
        scalePaint.setStyle(Paint.Style.STROKE);
        scalePaint.setColor(scaleColor);
        scalePaint.setStrokeWidth(0.005f);
        scalePaint.setAntiAlias(true);
        scalePaint.setTextSize(0.045f);
        scalePaint.setTypeface(Typeface.SANS_SERIF);
        scalePaint.setTextScaleX(0.8f);
        scalePaint.setTextAlign(Paint.Align.CENTER);

        rangeOkPaint = new Paint();
        rangeOkPaint.setStyle(Paint.Style.STROKE);
        rangeOkPaint.setColor(rangeOkColor);
        rangeOkPaint.setStrokeWidth(0.012f);
        rangeOkPaint.setAntiAlias(true);

        rangeWarningPaint = new Paint();
        rangeWarningPaint.setStyle(Paint.Style.STROKE);
        rangeWarningPaint.setColor(rangeWarningColor);
        rangeWarningPaint.setStrokeWidth(0.012f);
        rangeWarningPaint.setAntiAlias(true);

        rangeErrorPaint = new Paint();
        rangeErrorPaint.setStyle(Paint.Style.STROKE);
        rangeErrorPaint.setColor(rangeErrorColor);
        rangeErrorPaint.setStrokeWidth(0.012f);
        rangeErrorPaint.setAntiAlias(true);

        rangeAllPaint = new Paint();
        rangeAllPaint.setStyle(Paint.Style.STROKE);
        rangeAllPaint.setColor(0xcff8f8f8);
        rangeAllPaint.setStrokeWidth(0.012f);
        rangeAllPaint.setAntiAlias(true);
        rangeAllPaint.setShadowLayer(0.005f, -0.002f, -0.002f, 0x7f000000);

        valueOkPaint = new Paint();
        valueOkPaint.setStyle(Paint.Style.STROKE);
        valueOkPaint.setColor(rangeOkColor);
        valueOkPaint.setStrokeWidth(0.20f);
        valueOkPaint.setAntiAlias(true);

        valueWarningPaint = new Paint();
        valueWarningPaint.setStyle(Paint.Style.STROKE);
        valueWarningPaint.setColor(rangeWarningColor);
        valueWarningPaint.setStrokeWidth(0.20f);
        valueWarningPaint.setAntiAlias(true);

        valueErrorPaint = new Paint();
        valueErrorPaint.setStyle(Paint.Style.STROKE);
        valueErrorPaint.setColor(rangeErrorColor);
        valueErrorPaint.setStrokeWidth(0.20f);
        valueErrorPaint.setAntiAlias(true);

        valueAllPaint = new Paint();
        valueAllPaint.setStyle(Paint.Style.STROKE);
        valueAllPaint.setColor(0xcff8f8f8);
        valueAllPaint.setStrokeWidth(0.20f);
        valueAllPaint.setAntiAlias(true);
        valueAllPaint.setShadowLayer(0.005f, -0.002f, -0.002f, 0x7f000000);

        unitPaint = new Paint();
        unitPaint.setColor(0xaf0c0c0c);
        unitPaint.setAntiAlias(true);
        unitPaint.setTypeface(Typeface.DEFAULT_BOLD);
        unitPaint.setTextAlign(Paint.Align.CENTER);
        unitPaint.setTextSize(0.05f);
        unitPaint.setTextScaleX(0.8f);

        upperTitlePaint = new Paint();
        upperTitlePaint.setColor(0xaf0c0c0c);
        upperTitlePaint.setAntiAlias(true);
        upperTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        upperTitlePaint.setTextAlign(Paint.Align.CENTER);
        upperTitlePaint.setTextSize(0.04f);
        upperTitlePaint.setTextScaleX(0.8f);

        lowerTitlePaint = new Paint();
        lowerTitlePaint.setColor(0xaf0c0c0c);
        lowerTitlePaint.setAntiAlias(true);
        lowerTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        lowerTitlePaint.setTextAlign(Paint.Align.CENTER);
        lowerTitlePaint.setTextSize(0.04f);
        lowerTitlePaint.setTextScaleX(0.8f);

        handPaint = new Paint();
        handPaint.setAntiAlias(true);
        handPaint.setColor(0xff392f2c);
        handPaint.setShadowLayer(0.01f, -0.005f, -0.005f, 0x7f000000);
        handPaint.setStyle(Paint.Style.FILL);

        handScrewPaint = new Paint();
        handScrewPaint.setAntiAlias(true);
        // handScrewPaint.setColor(0xff493f3c);
        handScrewPaint.setColor(0xffff3f3c);
        handScrewPaint.setStyle(Paint.Style.FILL);

        backgroundPaint = new Paint();
        backgroundPaint.setFilterBitmap(true);

        unitPath = new Path();
        unitPath.addArc(unitRect, 180.0f, 180.0f);

        upperTitlePath = new Path();
        upperTitlePath.addArc(titleRect, 180.0f, 180.0f);

        lowerTitlePath = new Path();
        lowerTitlePath.addArc(titleRect, -180.0f, -180.0f);

        // The hand is drawn with the tip facing up. That means when the image
        // is not rotated, the tip
        // faces north. When the the image is rotated -90 degrees, the tip is
        // facing west and so on.
        handPath = new Path(); // X Y
        handPath.moveTo(0.5f, 0.5f + 0.2f); // 0.500, 0.700
        handPath.lineTo(0.5f - 0.010f, (0.5f + 0.2f) - 0.007f); // 0.490, 0.630
        handPath.lineTo(0.5f - 0.002f, 0.5f - 0.40f); // 0.498, 0.100
        handPath.lineTo(0.5f + 0.002f, 0.5f - 0.40f); // 0.502, 0.100
        handPath.lineTo(0.5f + 0.010f, (0.5f + 0.2f) - 0.007f); // 0.510, 0.630
        handPath.lineTo(0.5f, 0.5f + 0.2f); // 0.500, 0.700
        handPath.addCircle(0.5f, 0.5f, 0.025f, Path.Direction.CW);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        Log.d(GaugeView.TAG, "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
        Log.d(GaugeView.TAG, "Height spec: " + MeasureSpec.toString(heightMeasureSpec));

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        final int chosenWidth = chooseDimension(widthMode, widthSize);
        final int chosenHeight = chooseDimension(heightMode, heightSize);

        final int chosenDimension = Math.min(chosenWidth, chosenHeight);

        setMeasuredDimension(chosenDimension, chosenDimension);
    }

    private int chooseDimension(final int mode, final int size) {
        if ((mode == MeasureSpec.AT_MOST) || (mode == MeasureSpec.EXACTLY)) {
            return size;
        }
        else { // (mode == MeasureSpec.UNSPECIFIED)
            return getPreferredSize();
        }
    }

    // in case there is no size specified
    private int getPreferredSize() {
        return 250;
    }

    private void drawRim(final Canvas canvas) {
        // first, draw the metallic body
        canvas.drawOval(rimRect, rimPaint);
        // now the outer rim circle
        canvas.drawOval(rimRect, rimCirclePaint);
    }

    private void drawFace(final Canvas canvas) {
        canvas.drawOval(faceRect, facePaint);
        // draw the inner rim circle
        canvas.drawOval(faceRect, rimCirclePaint);
        // draw the rim shadow inside the face
        canvas.drawOval(faceRect, rimShadowPaint);
    }

    private void drawBackground(final Canvas canvas) {
        if (background == null) {
            Log.w(GaugeView.TAG, "Background not created");
        }
        else {
            canvas.drawBitmap(background, 0, 0, backgroundPaint);
        }
    }

    private void drawScale(final Canvas canvas) {
        // Draw the circle
        canvas.drawOval(scaleRect, scalePaint);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        for (int i = 0; i < totalNotches; ++i) {
            final float y1 = scaleRect.top;
            final float y2 = y1 - 0.015f;
            final float y3 = y1 - 0.025f;

            final int value = notchToValue(i);

            if ((i % (incrementPerLargeNotch / incrementPerSmallNotch)) == 0) {
                if ((value >= scaleMinValue) && (value <= scaleMaxValue)) {
                    // draw a nick
                    canvas.drawLine(0.5f, y1, 0.5f, y3, scalePaint);

                    final String valueString = Integer.toString(value);
                    // Draw the text 0.15 away from y3 which is the long nick.
                    canvas.drawText(valueString, 0.5f, y3 - 0.015f, scalePaint);
                }
            }
            else {
                if ((value >= scaleMinValue) && (value <= scaleMaxValue)) {
                    // draw a nick
                    canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaint);
                }
            }

            canvas.rotate(degreesPerNotch, 0.5f, 0.5f);
        }
        canvas.restore();
    }

    private void drawScaleRanges(final Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.drawArc(rangeRect, degreeMinValue, degreeMaxValue - degreeMinValue, false, rangeAllPaint);
        canvas.drawArc(rangeRect, degreeOkMinValue, degreeOkMaxValue - degreeOkMinValue, false, rangeOkPaint);
        canvas.drawArc(rangeRect, degreeWarningMinValue, degreeWarningMaxValue - degreeWarningMinValue, false, rangeWarningPaint);
        canvas.drawArc(rangeRect, degreeErrorMinValue, degreeErrorMaxValue - degreeErrorMinValue, false, rangeErrorPaint);
        canvas.restore();
    }

    private void drawTitle(final Canvas canvas) {
        // Use a vertical offset when printing the upper title. The upper and
        // lower title
        // use the same rectangular but the spacing between the title and the
        // ranges
        // is not equal for the upper and lower title and therefore, the upper
        // title is
        // moved downwards.
        canvas.drawTextOnPath(upperTitle, upperTitlePath, 0.0f, 0.02f, upperTitlePaint);
        canvas.drawTextOnPath(lowerTitle, lowerTitlePath, 0.0f, 0.0f, lowerTitlePaint);
        canvas.drawTextOnPath(unitTitle, unitPath, 0.0f, 0.0f, unitPaint);
    }

    private void drawHand(final Canvas canvas) {
        if (dialInitialized) {
            final float angle = valueToAngle(currentValue);
            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.rotate(angle, 0.5f, 0.5f);
            canvas.drawPath(handPath, handPaint);
            canvas.restore();

            canvas.drawCircle(0.5f, 0.5f, 0.01f, handScrewPaint);
        }
    }

    private void drawGauge(final Canvas canvas) {
        if (dialInitialized) {
            // When currentValue is not rotated, the tip of the hand points
            // to n -90 degrees.
            final float angle = valueToAngle(currentValue) - 90;

            if (targetValue <= rangeOkMaxValue) {
                canvas.drawArc(valueRect, degreeMinValue, angle - degreeMinValue, false, valueOkPaint);
            }
            if ((targetValue > rangeOkMaxValue) && (targetValue <= rangeWarningMaxValue)) {
                canvas.drawArc(valueRect, degreeMinValue, degreeOkMaxValue - degreeMinValue, false, valueOkPaint);
                canvas.drawArc(valueRect, degreeWarningMinValue, angle - degreeWarningMinValue, false, valueWarningPaint);
            }
            if ((targetValue > rangeWarningMaxValue) && (targetValue <= rangeErrorMaxValue)) {
                canvas.drawArc(valueRect, degreeMinValue, degreeOkMaxValue - degreeMinValue, false, valueOkPaint);
                canvas.drawArc(valueRect, degreeWarningMinValue, degreeWarningMaxValue - degreeWarningMinValue, false, valueWarningPaint);
                canvas.drawArc(valueRect, degreeErrorMinValue, angle - degreeErrorMinValue, false, valueErrorPaint);
            }
        }
    }

    private void drawBezel(final Canvas canvas) {
        // Draw the bevel in which the value is draw.
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.drawArc(valueRect, degreeMinValue, degreeMaxValue - degreeMinValue, false, valueAllPaint);
        canvas.restore();
    }

    /*
     * Translate a notch to a value for the scale. The notches are evenly spread
     * across the scale, half of the notches on the left hand side and the other
     * half on the right hand side. The raw value calculation uses a constant so
     * that each notch represents a value n + 2.
     */
    private int notchToValue(final int notch) {
        final int rawValue = ((notch < (totalNotches / 2)) ? notch : (notch - totalNotches)) * incrementPerSmallNotch;
        final int shiftedValue = rawValue + scaleCenterValue;
        return shiftedValue;
    }

    private float valueToAngle(final float value) {
        // scaleCenterValue represents an angle of -90 degrees.
        return ((value - scaleCenterValue) / 2.0f) * degreesPerNotch;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        drawBackground(canvas);

        final float scale = getWidth();
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(scale, scale);
        // Draw the needle using the updated value
        if (showGauge) {
            drawGauge(canvas);
        }

        // Draw the needle using the updated value
        if (showHand) {
            drawHand(canvas);
        }

        canvas.restore();

        // Calculate a new current value.
        calculateCurrentValue();
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        Log.d(GaugeView.TAG, "Size changed to " + w + "x" + h);
        regenerateBackground();
    }

    private void regenerateBackground() {
        // free the old bitmap
        if (background != null) {
            background.recycle();
        }

        background = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas backgroundCanvas = new Canvas(background);
        final float scale = getWidth();
        backgroundCanvas.scale(scale, scale);

        drawRim(backgroundCanvas);
        drawFace(backgroundCanvas);
        drawScale(backgroundCanvas);
        if (showRange) {
            drawScaleRanges(backgroundCanvas);
        }
        if (showGauge) {
            drawBezel(backgroundCanvas);
        }
        drawTitle(backgroundCanvas);
    }

    // Move the hand slowly to the new position.
    private void calculateCurrentValue() {
        if (!(Math.abs(currentValue - targetValue) > 0.01f)) {
            return;
        }

        if (lastDialMoveTime != -1L) {
            final long currentTime = System.currentTimeMillis();
            final float delta = (currentTime - lastDialMoveTime) / 1000.0f;

            final float direction = Math.signum(dialVelocity);
            if (Math.abs(dialVelocity) < 90.0f) {
                dialAcceleration = 5.0f * (targetValue - currentValue);
            }
            else {
                dialAcceleration = 0.0f;
            }
            currentValue += dialVelocity * delta;
            dialVelocity += dialAcceleration * delta;
            if (((targetValue - currentValue) * direction) < (0.01f * direction)) {
                currentValue = targetValue;
                dialVelocity = 0.0f;
                dialAcceleration = 0.0f;
                lastDialMoveTime = -1L;
            }
            else {
                lastDialMoveTime = System.currentTimeMillis();
            }
            invalidate();
        }
        else {
            lastDialMoveTime = System.currentTimeMillis();
            calculateCurrentValue();
        }
    }

    public void setValue(float value) {
        if (value < scaleMinValue) {
            value = scaleMinValue;
        }
        else if (value > scaleMaxValue) {
            value = scaleMaxValue;
        }

        targetValue = value;
        dialInitialized = true;

        invalidate(); // forces onDraw() to be called.
    }

    public float getValue() {
        return targetValue;
    }

}
