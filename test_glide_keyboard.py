"""
Unit tests for the Glide Keyboard system
"""

import unittest
from glide_keyboard import GlideKeyboard, KeyboardLayout, simulate_glide


class TestKeyboardLayout(unittest.TestCase):
    """Tests for KeyboardLayout class"""

    def setUp(self):
        self.layout = KeyboardLayout()

    def test_layout_contains_all_letters(self):
        """Test that layout contains all 26 letters"""
        for letter in 'abcdefghijklmnopqrstuvwxyz':
            self.assertIn(letter, self.layout.layout)

    def test_get_position(self):
        """Test getting position of a letter"""
        pos = self.layout.get_position('a')
        self.assertIsInstance(pos, tuple)
        self.assertEqual(len(pos), 2)
        self.assertIsInstance(pos[0], float)
        self.assertIsInstance(pos[1], float)

    def test_get_nearest_key(self):
        """Test finding nearest key to coordinates"""
        # Get position of 'a' and verify nearest key is 'a'
        x, y = self.layout.get_position('a')
        nearest = self.layout.get_nearest_key(x, y)
        self.assertEqual(nearest, 'a')

    def test_position_consistency(self):
        """Test that positions are within valid range"""
        for letter, (x, y) in self.layout.layout.items():
            self.assertGreaterEqual(x, 0)
            self.assertLessEqual(x, 1)
            self.assertGreaterEqual(y, 0)
            self.assertLessEqual(y, 1)


class TestGlideKeyboard(unittest.TestCase):
    """Tests for GlideKeyboard class"""

    def setUp(self):
        self.keyboard = GlideKeyboard()

    def test_initialization(self):
        """Test keyboard initializes with dictionary"""
        self.assertIsNotNone(self.keyboard.dictionary)
        self.assertGreater(len(self.keyboard.dictionary), 0)

    def test_add_word(self):
        """Test adding a new word to dictionary"""
        test_word = "testword123"
        initial_count = len(self.keyboard.dictionary)

        self.keyboard.add_word(test_word)
        self.assertIn(test_word, self.keyboard.dictionary)
        self.assertEqual(len(self.keyboard.dictionary), initial_count + 1)

    def test_remove_word(self):
        """Test removing a word from dictionary"""
        test_word = "remove123"
        self.keyboard.add_word(test_word)
        self.assertIn(test_word, self.keyboard.dictionary)

        self.keyboard.remove_word(test_word)
        self.assertNotIn(test_word, self.keyboard.dictionary)

    def test_predict_from_empty_path(self):
        """Test prediction with empty path"""
        predictions = self.keyboard.predict_from_path([])
        self.assertEqual(predictions, [])

    def test_predict_from_simulated_path(self):
        """Test prediction from a simulated glide path"""
        # Ensure 'hello' is in dictionary
        self.keyboard.add_word('hello')

        # Simulate gliding 'hello'
        path = simulate_glide(self.keyboard, 'hello', noise=0.01)

        predictions = self.keyboard.predict_from_path(path)

        # Should have at least one prediction
        self.assertGreater(len(predictions), 0)

        # 'hello' should be among the predictions (top prediction ideally)
        self.assertIn('hello', predictions)

    def test_path_to_keys(self):
        """Test converting path to key sequence"""
        # Create a simple path going through specific keys
        path = [
            self.keyboard.keyboard.get_position('h'),
            self.keyboard.keyboard.get_position('e'),
            self.keyboard.keyboard.get_position('l'),
            self.keyboard.keyboard.get_position('l'),
            self.keyboard.keyboard.get_position('o'),
        ]

        keys = self.keyboard._path_to_keys(path)

        # Should get the sequence h-e-l-o (duplicate 'l' removed)
        self.assertIn('h', keys)
        self.assertIn('e', keys)
        self.assertIn('l', keys)
        self.assertIn('o', keys)

    def test_custom_dictionary(self):
        """Test creating keyboard with custom dictionary"""
        custom_dict = {'apple', 'banana', 'cherry'}
        keyboard = GlideKeyboard(dictionary=custom_dict)

        self.assertEqual(len(keyboard.dictionary), 3)
        self.assertIn('apple', keyboard.dictionary)
        self.assertIn('banana', keyboard.dictionary)
        self.assertIn('cherry', keyboard.dictionary)

    def test_word_matches_sequence(self):
        """Test word matching logic"""
        # Test that 'hello' matches h-e-l-l-o sequence
        result = self.keyboard._word_matches_sequence('hello', ['h', 'e', 'l', 'l', 'o'])
        self.assertTrue(result)

        # Test that 'world' doesn't match h-e-l-l-o sequence
        result = self.keyboard._word_matches_sequence('world', ['h', 'e', 'l', 'l', 'o'])
        self.assertFalse(result)


class TestSimulateGlide(unittest.TestCase):
    """Tests for simulate_glide function"""

    def setUp(self):
        self.keyboard = GlideKeyboard()

    def test_simulate_produces_path(self):
        """Test that simulate_glide produces a valid path"""
        path = simulate_glide(self.keyboard, 'test')

        self.assertIsInstance(path, list)
        self.assertGreater(len(path), 0)

        # Each point should be a tuple of two floats
        for point in path:
            self.assertIsInstance(point, tuple)
            self.assertEqual(len(point), 2)
            self.assertIsInstance(point[0], (int, float))
            self.assertIsInstance(point[1], (int, float))

    def test_simulate_length_matches_word(self):
        """Test that path length roughly matches word length"""
        word = 'hello'
        path = simulate_glide(self.keyboard, word)

        # Path should have one point per letter (roughly)
        self.assertEqual(len(path), len(word))

    def test_simulate_with_noise(self):
        """Test that noise parameter affects coordinates"""
        # Generate two paths with different noise levels
        path1 = simulate_glide(self.keyboard, 'test', noise=0.0)
        path2 = simulate_glide(self.keyboard, 'test', noise=0.1)

        # Paths should be different when noise is added
        # (with very high probability)
        self.assertNotEqual(path1, path2)


def run_tests():
    """Run all tests"""
    unittest.main(argv=[''], verbosity=2, exit=False)


if __name__ == '__main__':
    run_tests()
