package com.huawei.glidekeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;

import java.util.List;

/**
 * Main Input Method Service for Glide Keyboard
 * Handles keyboard display, input processing, and glide gestures
 */
public class GlideKeyboardService extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView keyboardView;
    private Keyboard qwertyKeyboard;
    private Keyboard symbolsKeyboard;
    private Keyboard currentKeyboard;

    private GlidePathDetector glidePathDetector;
    private WordPredictor wordPredictor;

    private TextView suggestion1, suggestion2, suggestion3;
    private boolean isShifted = false;
    private boolean glideEnabled = true;
    private boolean suggestionsEnabled = true;

    @Override
    public void onCreate() {
        super.onCreate();
        wordPredictor = new WordPredictor(this);
        glidePathDetector = new GlidePathDetector(this, wordPredictor);
    }

    @Override
    public View onCreateInputView() {
        View inputView = getLayoutInflater().inflate(R.layout.keyboard_view, null);

        keyboardView = inputView.findViewById(R.id.keyboard);
        suggestion1 = inputView.findViewById(R.id.suggestion1);
        suggestion2 = inputView.findViewById(R.id.suggestion2);
        suggestion3 = inputView.findViewById(R.id.suggestion3);

        // Initialize keyboards
        qwertyKeyboard = new Keyboard(this, R.xml.qwerty);
        symbolsKeyboard = new Keyboard(this, R.xml.symbols);
        currentKeyboard = qwertyKeyboard;

        keyboardView.setKeyboard(currentKeyboard);
        keyboardView.setOnKeyboardActionListener(this);
        keyboardView.setPreviewEnabled(true);

        // Set up suggestion click listeners
        setupSuggestionListeners();

        // Set up touch listener for glide detection
        keyboardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (glideEnabled && currentKeyboard == qwertyKeyboard) {
                    glidePathDetector.handleTouchEvent(event, keyboardView, currentKeyboard);
                }
                return false;
            }
        });

        return inputView;
    }

    private void setupSuggestionListeners() {
        View.OnClickListener suggestionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                String word = textView.getText().toString();
                if (!word.isEmpty()) {
                    commitWord(word);
                }
            }
        };

        suggestion1.setOnClickListener(suggestionClickListener);
        suggestion2.setOnClickListener(suggestionClickListener);
        suggestion3.setOnClickListener(suggestionClickListener);
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        // Reset keyboard state
        currentKeyboard = qwertyKeyboard;
        if (keyboardView != null) {
            keyboardView.setKeyboard(currentKeyboard);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;

        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                updateSuggestions("");
                break;

            case Keyboard.KEYCODE_SHIFT:
                isShifted = !isShifted;
                currentKeyboard.setShifted(isShifted);
                keyboardView.invalidateAllKeys();
                break;

            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;

            case Keyboard.KEYCODE_MODE_CHANGE:
                // Switch between QWERTY and symbols
                if (currentKeyboard == qwertyKeyboard) {
                    currentKeyboard = symbolsKeyboard;
                } else {
                    currentKeyboard = qwertyKeyboard;
                }
                keyboardView.setKeyboard(currentKeyboard);
                break;

            case -3: // Custom code for returning to QWERTY from symbols
                currentKeyboard = qwertyKeyboard;
                keyboardView.setKeyboard(currentKeyboard);
                break;

            default:
                char code = (char) primaryCode;
                if (isShifted) {
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code), 1);

                // Get current word for predictions
                CharSequence currentWord = getCurrentWord(ic);
                updateSuggestions(currentWord.toString());
                break;
        }
    }

    /**
     * Called when glide gesture is completed
     */
    public void onGlideComplete(List<String> predictions) {
        if (predictions != null && !predictions.isEmpty()) {
            updateSuggestionStrip(predictions);

            // Auto-commit the first prediction
            String bestPrediction = predictions.get(0);
            commitWord(bestPrediction);
        }
    }

    private void commitWord(String word) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            // Delete current partial word if any
            CharSequence currentWord = getCurrentWord(ic);
            if (currentWord.length() > 0) {
                ic.deleteSurroundingText(currentWord.length(), 0);
            }

            // Commit the predicted word with a space
            ic.commitText(word + " ", 1);

            // Clear suggestions
            updateSuggestions("");
        }
    }

    private CharSequence getCurrentWord(InputConnection ic) {
        CharSequence textBeforeCursor = ic.getTextBeforeCursor(50, 0);
        if (textBeforeCursor == null) return "";

        // Find the last word
        String text = textBeforeCursor.toString();
        int lastSpace = text.lastIndexOf(' ');
        if (lastSpace >= 0) {
            return text.substring(lastSpace + 1);
        }
        return text;
    }

    private void updateSuggestions(String currentWord) {
        if (!suggestionsEnabled || currentWord.isEmpty()) {
            clearSuggestions();
            return;
        }

        List<String> predictions = wordPredictor.getPredictions(currentWord);
        updateSuggestionStrip(predictions);
    }

    private void updateSuggestionStrip(List<String> predictions) {
        suggestion1.setText(predictions.size() > 0 ? predictions.get(0) : "");
        suggestion2.setText(predictions.size() > 1 ? predictions.get(1) : "");
        suggestion3.setText(predictions.size() > 2 ? predictions.get(2) : "");
    }

    private void clearSuggestions() {
        suggestion1.setText("");
        suggestion2.setText("");
        suggestion3.setText("");
    }

    @Override
    public void onPress(int primaryCode) {
        // Key press feedback can be added here
    }

    @Override
    public void onRelease(int primaryCode) {
        // Key release feedback can be added here
    }

    @Override
    public void onText(CharSequence text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.commitText(text, 1);
        }
    }

    @Override
    public void swipeLeft() {
        // Optional: implement swipe gestures
    }

    @Override
    public void swipeRight() {
        // Optional: implement swipe gestures
    }

    @Override
    public void swipeDown() {
        // Optional: implement swipe gestures
    }

    @Override
    public void swipeUp() {
        // Optional: implement swipe gestures
    }
}
