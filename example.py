"""
Example usage of the Glide Keyboard system
"""

from glide_keyboard import GlideKeyboard, simulate_glide


def main():
    print("=" * 60)
    print("Glide Keyboard Demo")
    print("=" * 60)
    print()

    # Create a glide keyboard instance
    keyboard = GlideKeyboard()

    print(f"Loaded dictionary with {len(keyboard.dictionary)} words")
    print()

    # Example 1: Simulate typing "hello"
    print("Example 1: Simulating glide for 'hello'")
    print("-" * 60)
    test_word = "hello"
    path = simulate_glide(keyboard, test_word)
    print(f"Generated path with {len(path)} points")
    print(f"Path coordinates (first 5): {path[:5]}")
    print()

    predictions = keyboard.predict_from_path(path)
    print(f"Predictions: {predictions}")
    print()

    # Example 2: Simulate typing "world"
    print("Example 2: Simulating glide for 'world'")
    print("-" * 60)
    test_word = "world"
    path = simulate_glide(keyboard, test_word)
    predictions = keyboard.predict_from_path(path)
    print(f"Predictions: {predictions}")
    print()

    # Example 3: Simulate typing "keyboard"
    print("Example 3: Simulating glide for 'keyboard'")
    print("-" * 60)
    test_word = "keyboard"
    path = simulate_glide(keyboard, test_word)
    predictions = keyboard.predict_from_path(path)
    print(f"Predictions: {predictions}")
    print()

    # Example 4: Custom path (typing "test")
    print("Example 4: Manual glide path for 'test'")
    print("-" * 60)
    # Create a path that goes through t-e-s-t
    manual_path = [
        (0.45, 0.0),   # t
        (0.25, 0.0),   # e
        (0.20, 0.33),  # s
        (0.45, 0.0),   # t
    ]
    predictions = keyboard.predict_from_path(manual_path)
    print(f"Manual path: {manual_path}")
    print(f"Predictions: {predictions}")
    print()

    # Example 5: Adding a custom word
    print("Example 5: Adding custom word to dictionary")
    print("-" * 60)
    custom_word = "huawei"
    keyboard.add_word(custom_word)
    print(f"Added '{custom_word}' to dictionary")

    path = simulate_glide(keyboard, custom_word)
    predictions = keyboard.predict_from_path(path)
    print(f"Predictions for '{custom_word}': {predictions}")
    print()

    # Example 6: Show keyboard layout
    print("Example 6: Keyboard layout positions")
    print("-" * 60)
    print("Sample key positions:")
    for letter in ['q', 'a', 'z', 'm', 'p']:
        x, y = keyboard.keyboard.get_position(letter)
        print(f"  {letter.upper()}: ({x:.2f}, {y:.2f})")
    print()

    print("=" * 60)
    print("Demo complete!")
    print("=" * 60)


if __name__ == "__main__":
    main()
