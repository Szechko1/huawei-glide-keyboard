package com.huawei.glidekeyboard;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

/**
 * Settings Activity for Glide Keyboard
 * Allows users to enable the keyboard and configure options
 */
public class SettingsActivity extends AppCompatActivity {

    private Button btnEnableKeyboard;
    private Button btnSelectKeyboard;
    private SwitchCompat switchGlideEnabled;
    private SwitchCompat switchWordSuggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views
        btnEnableKeyboard = findViewById(R.id.btn_enable_keyboard);
        btnSelectKeyboard = findViewById(R.id.btn_select_keyboard);
        switchGlideEnabled = findViewById(R.id.switch_glide_enabled);
        switchWordSuggestions = findViewById(R.id.switch_word_suggestions);

        // Set up button listeners
        btnEnableKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInputMethodSettings();
            }
        });

        btnSelectKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputMethodPicker();
            }
        });

        // Load saved preferences
        loadPreferences();

        // Set up switch listeners
        switchGlideEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePreference("glide_enabled", isChecked);
        });

        switchWordSuggestions.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePreference("word_suggestions_enabled", isChecked);
        });
    }

    /**
     * Open Android input method settings
     */
    private void openInputMethodSettings() {
        Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
        startActivity(intent);
    }

    /**
     * Show input method picker to select keyboard
     */
    private void showInputMethodPicker() {
        InputMethodManager imeManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imeManager != null) {
            imeManager.showInputMethodPicker();
        }
    }

    /**
     * Load preferences from SharedPreferences
     */
    private void loadPreferences() {
        android.content.SharedPreferences prefs = getSharedPreferences("GlideKeyboardPrefs", MODE_PRIVATE);
        boolean glideEnabled = prefs.getBoolean("glide_enabled", true);
        boolean suggestionsEnabled = prefs.getBoolean("word_suggestions_enabled", true);

        switchGlideEnabled.setChecked(glideEnabled);
        switchWordSuggestions.setChecked(suggestionsEnabled);
    }

    /**
     * Save preference to SharedPreferences
     */
    private void savePreference(String key, boolean value) {
        android.content.SharedPreferences prefs = getSharedPreferences("GlideKeyboardPrefs", MODE_PRIVATE);
        prefs.edit().putBoolean(key, value).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload preferences when activity resumes
        loadPreferences();
    }
}
