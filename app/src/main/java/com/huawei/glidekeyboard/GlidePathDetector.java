package com.huawei.glidekeyboard;

import android.content.Context;
import android.graphics.PointF;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Detects glide/swipe gestures on the keyboard
 * Tracks the path and converts it to word predictions
 */
public class GlidePathDetector {

    private GlideKeyboardService keyboardService;
    private WordPredictor wordPredictor;

    private List<PointF> glidePath;
    private boolean isGliding = false;
    private long glideStartTime;

    private static final int MIN_GLIDE_DISTANCE = 50; // pixels
    private static final int MIN_GLIDE_DURATION = 100; // milliseconds

    public GlidePathDetector(GlideKeyboardService service, WordPredictor predictor) {
        this.keyboardService = service;
        this.wordPredictor = predictor;
        this.glidePath = new ArrayList<>();
    }

    public void handleTouchEvent(MotionEvent event, KeyboardView keyboardView, Keyboard keyboard) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startGlide(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                if (isGliding) {
                    continueGlide(event.getX(), event.getY());
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isGliding) {
                    endGlide(keyboard);
                }
                break;
        }
    }

    private void startGlide(float x, float y) {
        isGliding = true;
        glideStartTime = System.currentTimeMillis();
        glidePath.clear();
        glidePath.add(new PointF(x, y));
    }

    private void continueGlide(float x, float y) {
        if (glidePath.size() > 0) {
            PointF lastPoint = glidePath.get(glidePath.size() - 1);
            float distance = (float) Math.sqrt(
                    Math.pow(x - lastPoint.x, 2) + Math.pow(y - lastPoint.y, 2)
            );

            // Only add point if it's far enough from the last one
            if (distance > 10) {
                glidePath.add(new PointF(x, y));
            }
        }
    }

    private void endGlide(Keyboard keyboard) {
        long glideDuration = System.currentTimeMillis() - glideStartTime;

        // Check if this was a valid glide gesture
        if (glidePath.size() >= 2 && glideDuration >= MIN_GLIDE_DURATION) {
            float totalDistance = calculateTotalDistance();

            if (totalDistance >= MIN_GLIDE_DISTANCE) {
                // Convert path to normalized coordinates and get predictions
                List<GlidePoint> normalizedPath = normalizeGlidePath(keyboard);
                List<String> predictions = wordPredictor.predictFromGlidePath(normalizedPath);

                // Notify keyboard service
                keyboardService.onGlideComplete(predictions);
            }
        }

        // Reset state
        isGliding = false;
        glidePath.clear();
    }

    private float calculateTotalDistance() {
        float totalDistance = 0;
        for (int i = 1; i < glidePath.size(); i++) {
            PointF p1 = glidePath.get(i - 1);
            PointF p2 = glidePath.get(i);
            totalDistance += Math.sqrt(
                    Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2)
            );
        }
        return totalDistance;
    }

    private List<GlidePoint> normalizeGlidePath(Keyboard keyboard) {
        List<GlidePoint> normalizedPath = new ArrayList<>();

        // Get keyboard dimensions
        int keyboardWidth = keyboard.getMinWidth();
        int keyboardHeight = keyboard.getHeight();

        // Normalize each point to 0-1 range
        for (PointF point : glidePath) {
            float normalizedX = point.x / keyboardWidth;
            float normalizedY = point.y / keyboardHeight;

            // Find nearest key
            char nearestKey = findNearestKey(point.x, point.y, keyboard);

            normalizedPath.add(new GlidePoint(normalizedX, normalizedY, nearestKey));
        }

        return normalizedPath;
    }

    private char findNearestKey(float x, float y, Keyboard keyboard) {
        List<Keyboard.Key> keys = keyboard.getKeys();
        char nearestChar = ' ';
        float minDistance = Float.MAX_VALUE;

        for (Keyboard.Key key : keys) {
            // Calculate key center
            float keyCenterX = key.x + key.width / 2f;
            float keyCenterY = key.y + key.height / 2f;

            // Calculate distance
            float distance = (float) Math.sqrt(
                    Math.pow(x - keyCenterX, 2) + Math.pow(y - keyCenterY, 2)
            );

            // Check if this is closer and is a letter key
            if (distance < minDistance && key.codes.length > 0) {
                int code = key.codes[0];
                if (code >= 97 && code <= 122) { // a-z
                    nearestChar = (char) code;
                    minDistance = distance;
                }
            }
        }

        return nearestChar;
    }

    /**
     * Represents a point in the glide path with normalized coordinates and nearest key
     */
    public static class GlidePoint {
        public float x;
        public float y;
        public char key;

        public GlidePoint(float x, float y, char key) {
            this.x = x;
            this.y = y;
            this.key = key;
        }
    }
}
