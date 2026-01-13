# Huawei Glide Keyboard

A Python implementation of a glide/swipe keyboard system that predicts words based on swipe gestures across a virtual keyboard.

## Overview

Glide keyboards (also known as swipe keyboards) allow users to type by swiping their finger across the keyboard from letter to letter, rather than tapping individual keys. The system tracks the path and predicts the intended word.

This implementation provides:
- A QWERTY keyboard layout with normalized coordinates
- Path-to-word prediction algorithm
- Customizable word dictionary
- Word matching and ranking system

## Features

- **Keyboard Layout**: Standard QWERTY layout with coordinate-based positioning
- **Path Processing**: Converts swipe paths into key sequences
- **Word Prediction**: Matches paths to dictionary words with ranking
- **Dictionary Management**: Add/remove words dynamically
- **Path Simulation**: Generate realistic glide paths for testing

## Installation

No external dependencies required. Uses only Python standard library.

```bash
git clone <repository-url>
cd huawei-glide-keyboard
```

## Usage

### Basic Example

```python
from glide_keyboard import GlideKeyboard, simulate_glide

# Create a keyboard instance
keyboard = GlideKeyboard()

# Simulate a glide path for the word "hello"
path = simulate_glide(keyboard, "hello")

# Get word predictions from the path
predictions = keyboard.predict_from_path(path)
print(predictions)  # ['hello', ...]
```

### Manual Path Input

```python
# Define a custom path (x, y coordinates in 0-1 range)
manual_path = [
    (0.45, 0.0),   # t
    (0.25, 0.0),   # e
    (0.20, 0.33),  # s
    (0.45, 0.0),   # t
]

predictions = keyboard.predict_from_path(manual_path)
print(predictions)  # ['test', ...]
```

### Dictionary Management

```python
# Add custom words
keyboard.add_word("huawei")
keyboard.add_word("smartphone")

# Remove words
keyboard.remove_word("example")

# Use custom dictionary
custom_dict = {'apple', 'banana', 'cherry'}
keyboard = GlideKeyboard(dictionary=custom_dict)
```

### Running Examples

```bash
python example.py
```

### Running Tests

```bash
python test_glide_keyboard.py
```

## How It Works

### 1. Keyboard Layout

The keyboard uses a normalized coordinate system (0-1 range) for a standard QWERTY layout:

```
Row 1: Q W E R T Y U I O P  (y=0.0)
Row 2:  A S D F G H J K L   (y=0.33)
Row 3:   Z X C V B N M      (y=0.66)
```

### 2. Path Processing

When a user swipes across the keyboard:
1. The path is recorded as a series of (x, y) coordinates
2. Each coordinate is mapped to the nearest key
3. Consecutive duplicate keys are removed
4. Result is a sequence of keys (e.g., "h-e-l-o")

### 3. Word Prediction

The system finds candidate words by:
1. Matching the key sequence against dictionary words
2. Allowing for imperfect matches (partial letter matches)
3. Scoring candidates based on:
   - Letter match percentage
   - Length similarity
   - Path accuracy

### 4. Ranking

Predictions are ranked by:
- How many letters match the path
- How close the word length is to the path length
- Path geometry (future enhancement)

## API Reference

### `GlideKeyboard`

Main keyboard class for word prediction.

**Methods:**
- `__init__(dictionary: Set[str] = None)`: Initialize with optional custom dictionary
- `predict_from_path(path: List[Tuple[float, float]], max_results: int = 5)`: Get word predictions
- `add_word(word: str)`: Add a word to the dictionary
- `remove_word(word: str)`: Remove a word from the dictionary

### `KeyboardLayout`

Represents the physical keyboard layout.

**Methods:**
- `get_position(letter: str)`: Get (x, y) coordinates for a letter
- `get_nearest_key(x: float, y: float)`: Find nearest key to coordinates

### `simulate_glide`

Utility function to generate realistic glide paths.

**Parameters:**
- `keyboard`: GlideKeyboard instance
- `word`: Word to simulate typing
- `noise`: Amount of random noise (0-1, default 0.02)

**Returns:** List of (x, y) coordinates

## Future Enhancements

Potential improvements for the system:

- **Larger Dictionary**: Load comprehensive word lists from external files
- **Path Geometry Scoring**: Consider the actual geometric path shape, not just key sequence
- **N-gram Models**: Use language models for better predictions
- **User Learning**: Adapt to user's typing patterns and vocabulary
- **Multi-language Support**: Support for different keyboard layouts and languages
- **Word Frequency**: Prioritize common words in predictions
- **Auto-correction**: Detect and correct typing errors
- **Next Word Prediction**: Predict the next word based on context

## Contributing

Contributions are welcome! Some areas to explore:

1. Implement advanced path geometry matching
2. Add support for additional keyboard layouts (Dvorak, AZERTY, etc.)
3. Integrate real word frequency data
4. Optimize prediction algorithm performance
5. Add visualization tools for debugging paths

## License

MIT License (or specify your license)

## Credits

Developed as a demonstration of swipe keyboard technology.
