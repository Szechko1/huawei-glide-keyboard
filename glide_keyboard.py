"""
Glide Keyboard - A swipe-based text input system
Allows users to type by swiping across keyboard letters
"""

import math
from typing import List, Tuple, Dict, Set
from collections import defaultdict


class KeyboardLayout:
    """Represents a QWERTY keyboard layout with coordinate positions"""

    def __init__(self):
        # Standard QWERTY layout with normalized coordinates (0-1 scale)
        # Row 1: QWERTYUIOP
        # Row 2: ASDFGHJKL
        # Row 3: ZXCVBNM
        self.layout = {
            'q': (0.05, 0.0), 'w': (0.15, 0.0), 'e': (0.25, 0.0), 'r': (0.35, 0.0),
            't': (0.45, 0.0), 'y': (0.55, 0.0), 'u': (0.65, 0.0), 'i': (0.75, 0.0),
            'o': (0.85, 0.0), 'p': (0.95, 0.0),

            'a': (0.08, 0.33), 's': (0.20, 0.33), 'd': (0.32, 0.33), 'f': (0.44, 0.33),
            'g': (0.56, 0.33), 'h': (0.68, 0.33), 'j': (0.80, 0.33), 'k': (0.92, 0.33),
            'l': (1.0, 0.33),

            'z': (0.15, 0.66), 'x': (0.28, 0.66), 'c': (0.41, 0.66), 'v': (0.54, 0.66),
            'b': (0.67, 0.66), 'n': (0.80, 0.66), 'm': (0.93, 0.66),
        }

        # Reverse mapping for finding nearest keys
        self.positions = {v: k for k, v in self.layout.items()}

    def get_position(self, letter: str) -> Tuple[float, float]:
        """Get the coordinate position of a letter"""
        return self.layout.get(letter.lower(), (0, 0))

    def get_nearest_key(self, x: float, y: float) -> str:
        """Find the nearest key to given coordinates"""
        min_dist = float('inf')
        nearest = None

        for letter, (lx, ly) in self.layout.items():
            dist = math.sqrt((x - lx) ** 2 + (y - ly) ** 2)
            if dist < min_dist:
                min_dist = dist
                nearest = letter

        return nearest


class GlideKeyboard:
    """Main glide keyboard system for word prediction from swipe paths"""

    def __init__(self, dictionary: Set[str] = None):
        self.keyboard = KeyboardLayout()
        self.dictionary = dictionary or self._load_default_dictionary()
        self._build_word_signatures()

    def _load_default_dictionary(self) -> Set[str]:
        """Load a default dictionary of common English words"""
        # Common English words for demonstration
        words = {
            'hello', 'world', 'keyboard', 'glide', 'swipe', 'type', 'word', 'text',
            'phone', 'mobile', 'input', 'gesture', 'touch', 'screen', 'device',
            'message', 'chat', 'send', 'receive', 'write', 'read', 'user',
            'system', 'software', 'hardware', 'code', 'program', 'application',
            'test', 'demo', 'example', 'sample', 'data', 'file', 'save', 'load',
            'quick', 'fast', 'slow', 'speed', 'time', 'date', 'year', 'month',
            'day', 'hour', 'minute', 'second', 'now', 'then', 'when', 'where',
            'what', 'who', 'why', 'how', 'which', 'this', 'that', 'these', 'those',
            'good', 'bad', 'best', 'worst', 'better', 'worse', 'great', 'amazing',
            'awesome', 'cool', 'nice', 'fine', 'okay', 'yes', 'no', 'maybe',
            'please', 'thanks', 'thank', 'welcome', 'sorry', 'excuse', 'help',
        }
        return words

    def _build_word_signatures(self):
        """Build signatures for each word based on key positions"""
        self.word_signatures = {}

        for word in self.dictionary:
            signature = self._get_word_signature(word)
            if signature not in self.word_signatures:
                self.word_signatures[signature] = []
            self.word_signatures[signature].append(word)

    def _get_word_signature(self, word: str) -> Tuple[str, ...]:
        """Get a simplified signature for a word (sequence of keys)"""
        return tuple(letter.lower() for letter in word if letter.isalpha())

    def predict_from_path(self, path: List[Tuple[float, float]], max_results: int = 5) -> List[str]:
        """
        Predict words from a glide path

        Args:
            path: List of (x, y) coordinates representing the swipe path
            max_results: Maximum number of predictions to return

        Returns:
            List of predicted words, sorted by relevance
        """
        if not path:
            return []

        # Convert path to sequence of keys
        key_sequence = self._path_to_keys(path)

        # Find matching words
        candidates = self._find_candidates(key_sequence)

        # Rank candidates by how well they match the path
        ranked = self._rank_candidates(candidates, key_sequence, path)

        return ranked[:max_results]

    def _path_to_keys(self, path: List[Tuple[float, float]]) -> List[str]:
        """Convert a coordinate path to a sequence of keys"""
        keys = []
        prev_key = None

        for x, y in path:
            key = self.keyboard.get_nearest_key(x, y)
            # Only add if different from previous key (avoid duplicates)
            if key != prev_key:
                keys.append(key)
                prev_key = key

        return keys

    def _find_candidates(self, key_sequence: List[str]) -> List[str]:
        """Find candidate words that match the key sequence"""
        candidates = []

        for word in self.dictionary:
            if self._word_matches_sequence(word, key_sequence):
                candidates.append(word)

        return candidates

    def _word_matches_sequence(self, word: str, key_sequence: List[str]) -> bool:
        """Check if a word could be formed from the key sequence"""
        word = word.lower()

        # Simple matching: check if word letters are subset of key sequence
        # and in roughly the right order
        if len(word) > len(key_sequence) * 2:
            return False

        word_idx = 0
        for key in key_sequence:
            if word_idx < len(word) and word[word_idx] == key:
                word_idx += 1

        # Word matches if we found most of its letters
        return word_idx >= len(word) * 0.6

    def _rank_candidates(self, candidates: List[str], key_sequence: List[str],
                        path: List[Tuple[float, float]]) -> List[str]:
        """Rank candidates by how well they match the path"""
        scored = []

        for word in candidates:
            score = self._calculate_match_score(word, key_sequence, path)
            scored.append((score, word))

        # Sort by score (higher is better)
        scored.sort(reverse=True)

        return [word for score, word in scored]

    def _calculate_match_score(self, word: str, key_sequence: List[str],
                               path: List[Tuple[float, float]]) -> float:
        """Calculate how well a word matches the glide path"""
        score = 0.0
        word = word.lower()

        # Score based on letter match
        matched_letters = 0
        word_idx = 0
        for key in key_sequence:
            if word_idx < len(word) and word[word_idx] == key:
                matched_letters += 1
                word_idx += 1

        letter_score = matched_letters / len(word) if word else 0
        score += letter_score * 10

        # Bonus for exact length match
        if len(word) == len(key_sequence):
            score += 2

        # Penalty for length difference
        length_diff = abs(len(word) - len(key_sequence))
        score -= length_diff * 0.5

        return max(0, score)

    def add_word(self, word: str):
        """Add a new word to the dictionary"""
        word = word.lower()
        if word not in self.dictionary:
            self.dictionary.add(word)
            signature = self._get_word_signature(word)
            if signature not in self.word_signatures:
                self.word_signatures[signature] = []
            self.word_signatures[signature].append(word)

    def remove_word(self, word: str):
        """Remove a word from the dictionary"""
        word = word.lower()
        if word in self.dictionary:
            self.dictionary.remove(word)
            signature = self._get_word_signature(word)
            if signature in self.word_signatures and word in self.word_signatures[signature]:
                self.word_signatures[signature].remove(word)


def simulate_glide(keyboard: GlideKeyboard, word: str, noise: float = 0.02) -> List[Tuple[float, float]]:
    """
    Simulate a glide path for typing a word

    Args:
        keyboard: The GlideKeyboard instance
        word: The word to simulate typing
        noise: Amount of random noise to add (0-1)

    Returns:
        List of coordinates representing the glide path
    """
    import random

    path = []
    for letter in word.lower():
        if letter.isalpha():
            x, y = keyboard.keyboard.get_position(letter)
            # Add some noise to simulate imperfect human input
            x += random.uniform(-noise, noise)
            y += random.uniform(-noise, noise)
            path.append((x, y))

    return path
