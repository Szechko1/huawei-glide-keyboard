package com.huawei.glidekeyboard;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Word prediction engine based on glide path
 * Implements the algorithm from the Python glide keyboard
 */
public class WordPredictor {

    private Context context;
    private Set<String> dictionary;
    private Map<String, List<String>> wordSignatures;

    // QWERTY keyboard layout with normalized coordinates (0-1 scale)
    private static final Map<Character, float[]> KEYBOARD_LAYOUT = new HashMap<>();

    static {
        // Row 1: QWERTYUIOP
        KEYBOARD_LAYOUT.put('q', new float[]{0.05f, 0.0f});
        KEYBOARD_LAYOUT.put('w', new float[]{0.15f, 0.0f});
        KEYBOARD_LAYOUT.put('e', new float[]{0.25f, 0.0f});
        KEYBOARD_LAYOUT.put('r', new float[]{0.35f, 0.0f});
        KEYBOARD_LAYOUT.put('t', new float[]{0.45f, 0.0f});
        KEYBOARD_LAYOUT.put('y', new float[]{0.55f, 0.0f});
        KEYBOARD_LAYOUT.put('u', new float[]{0.65f, 0.0f});
        KEYBOARD_LAYOUT.put('i', new float[]{0.75f, 0.0f});
        KEYBOARD_LAYOUT.put('o', new float[]{0.85f, 0.0f});
        KEYBOARD_LAYOUT.put('p', new float[]{0.95f, 0.0f});

        // Row 2: ASDFGHJKL
        KEYBOARD_LAYOUT.put('a', new float[]{0.08f, 0.33f});
        KEYBOARD_LAYOUT.put('s', new float[]{0.20f, 0.33f});
        KEYBOARD_LAYOUT.put('d', new float[]{0.32f, 0.33f});
        KEYBOARD_LAYOUT.put('f', new float[]{0.44f, 0.33f});
        KEYBOARD_LAYOUT.put('g', new float[]{0.56f, 0.33f});
        KEYBOARD_LAYOUT.put('h', new float[]{0.68f, 0.33f});
        KEYBOARD_LAYOUT.put('j', new float[]{0.80f, 0.33f});
        KEYBOARD_LAYOUT.put('k', new float[]{0.92f, 0.33f});
        KEYBOARD_LAYOUT.put('l', new float[]{1.0f, 0.33f});

        // Row 3: ZXCVBNM
        KEYBOARD_LAYOUT.put('z', new float[]{0.15f, 0.66f});
        KEYBOARD_LAYOUT.put('x', new float[]{0.28f, 0.66f});
        KEYBOARD_LAYOUT.put('c', new float[]{0.41f, 0.66f});
        KEYBOARD_LAYOUT.put('v', new float[]{0.54f, 0.66f});
        KEYBOARD_LAYOUT.put('b', new float[]{0.67f, 0.66f});
        KEYBOARD_LAYOUT.put('n', new float[]{0.80f, 0.66f});
        KEYBOARD_LAYOUT.put('m', new float[]{0.93f, 0.66f});
    }

    public WordPredictor(Context context) {
        this.context = context;
        this.dictionary = new HashSet<>();
        this.wordSignatures = new HashMap<>();
        loadDictionary();
    }

    private void loadDictionary() {
        // Load default common English words
        String[] commonWords = {
                "hello", "world", "keyboard", "glide", "swipe", "type", "word", "text",
                "phone", "mobile", "input", "gesture", "touch", "screen", "device",
                "message", "chat", "send", "receive", "write", "read", "user",
                "system", "software", "hardware", "code", "program", "application",
                "test", "demo", "example", "sample", "data", "file", "save", "load",
                "quick", "fast", "slow", "speed", "time", "date", "year", "month",
                "day", "hour", "minute", "second", "now", "then", "when", "where",
                "what", "who", "why", "how", "which", "this", "that", "these", "those",
                "good", "bad", "best", "worst", "better", "worse", "great", "amazing",
                "awesome", "cool", "nice", "fine", "okay", "yes", "no", "maybe",
                "please", "thanks", "thank", "welcome", "sorry", "excuse", "help",
                "love", "like", "want", "need", "have", "make", "know", "think",
                "take", "come", "give", "find", "tell", "work", "call", "feel",
                "leave", "keep", "begin", "seem", "show", "talk", "start", "move",
                "and", "the", "for", "are", "but", "not", "you", "all", "can", "her",
                "was", "one", "our", "out", "day", "get", "has", "him", "his", "how",
                "man", "new", "now", "old", "see", "two", "way", "who", "boy", "did",
                "its", "let", "put", "say", "she", "too", "use"
        };

        for (String word : commonWords) {
            addWord(word);
        }
    }

    public void addWord(String word) {
        word = word.toLowerCase();
        dictionary.add(word);

        String signature = getWordSignature(word);
        if (!wordSignatures.containsKey(signature)) {
            wordSignatures.put(signature, new ArrayList<String>());
        }
        wordSignatures.get(signature).add(word);
    }

    private String getWordSignature(String word) {
        return word.toLowerCase();
    }

    /**
     * Get predictions based on partial typed word
     */
    public List<String> getPredictions(String partialWord) {
        if (partialWord == null || partialWord.isEmpty()) {
            return new ArrayList<>();
        }

        partialWord = partialWord.toLowerCase();
        List<String> predictions = new ArrayList<>();

        // Find words that start with the partial word
        for (String word : dictionary) {
            if (word.startsWith(partialWord)) {
                predictions.add(word);
            }
        }

        // Sort by length (prefer shorter words)
        Collections.sort(predictions, new Comparator<String>() {
            @Override
            public int compare(String w1, String w2) {
                return Integer.compare(w1.length(), w2.length());
            }
        });

        // Return top 3
        return predictions.subList(0, Math.min(3, predictions.size()));
    }

    /**
     * Predict words from a glide path
     */
    public List<String> predictFromGlidePath(List<GlidePathDetector.GlidePoint> path) {
        if (path == null || path.isEmpty()) {
            return new ArrayList<>();
        }

        // Convert path to key sequence
        List<Character> keySequence = pathToKeys(path);

        if (keySequence.isEmpty()) {
            return new ArrayList<>();
        }

        // Find candidate words
        List<String> candidates = findCandidates(keySequence);

        // Rank candidates
        List<WordScore> rankedCandidates = rankCandidates(candidates, keySequence, path);

        // Convert to string list and return top predictions
        List<String> predictions = new ArrayList<>();
        for (int i = 0; i < Math.min(5, rankedCandidates.size()); i++) {
            predictions.add(rankedCandidates.get(i).word);
        }

        return predictions;
    }

    private List<Character> pathToKeys(List<GlidePathDetector.GlidePoint> path) {
        List<Character> keys = new ArrayList<>();
        char prevKey = '\0';

        for (GlidePathDetector.GlidePoint point : path) {
            char key = point.key;
            // Only add if different from previous key
            if (key != prevKey && key != ' ') {
                keys.add(key);
                prevKey = key;
            }
        }

        return keys;
    }

    private List<String> findCandidates(List<Character> keySequence) {
        List<String> candidates = new ArrayList<>();

        for (String word : dictionary) {
            if (wordMatchesSequence(word, keySequence)) {
                candidates.add(word);
            }
        }

        return candidates;
    }

    private boolean wordMatchesSequence(String word, List<Character> keySequence) {
        word = word.toLowerCase();

        if (word.length() > keySequence.size() * 2) {
            return false;
        }

        int wordIdx = 0;
        for (char key : keySequence) {
            if (wordIdx < word.length() && word.charAt(wordIdx) == key) {
                wordIdx++;
            }
        }

        // Word matches if we found most of its letters
        return wordIdx >= word.length() * 0.6;
    }

    private List<WordScore> rankCandidates(List<String> candidates,
                                          List<Character> keySequence,
                                          List<GlidePathDetector.GlidePoint> path) {
        List<WordScore> scored = new ArrayList<>();

        for (String word : candidates) {
            float score = calculateMatchScore(word, keySequence, path);
            scored.add(new WordScore(word, score));
        }

        // Sort by score (higher is better)
        Collections.sort(scored, new Comparator<WordScore>() {
            @Override
            public int compare(WordScore w1, WordScore w2) {
                return Float.compare(w2.score, w1.score);
            }
        });

        return scored;
    }

    private float calculateMatchScore(String word, List<Character> keySequence,
                                     List<GlidePathDetector.GlidePoint> path) {
        float score = 0.0f;
        word = word.toLowerCase();

        // Score based on letter match
        int matchedLetters = 0;
        int wordIdx = 0;
        for (char key : keySequence) {
            if (wordIdx < word.length() && word.charAt(wordIdx) == key) {
                matchedLetters++;
                wordIdx++;
            }
        }

        float letterScore = word.length() > 0 ? (float) matchedLetters / word.length() : 0;
        score += letterScore * 10;

        // Bonus for exact length match
        if (word.length() == keySequence.size()) {
            score += 2;
        }

        // Penalty for length difference
        int lengthDiff = Math.abs(word.length() - keySequence.size());
        score -= lengthDiff * 0.5f;

        return Math.max(0, score);
    }

    private static class WordScore {
        String word;
        float score;

        WordScore(String word, float score) {
            this.word = word;
            this.score = score;
        }
    }
}
