1. Input UI for 1 Text with settings for speed, border and blink effect
2. Render bytestream
   1. Implement bitmap font for 11x44, if possible with complete Unicode support
   2. Render text
   3. Render border
   4. Add blink effect?
3. Add preview
   1. Compile GIF image from bytestream
   2. Add view to display GIF preview
4. Add USB programming logic
   1. USB Host API
   2. Detect correct device
   3. Write bytestream to HID
   4. Display progress somehow
5. Add Save logic to store/manage configurations (sqlite? xml?)
6. Add Export button to save GIF to gallery
7. Add Share button to share GIF
8. Add animations
   1. Add selector for animation mode 
   2. Render different animations in preview
   3. Use animation in programming bytestream
9. Add setting for two different display sizes
10. Look for security / data protection settings
11. Code cleanup
12. Refactor text rendering to support monospaced and variable-width bitmap fonts
    1. Implement variable-width bitmap font
    2. Refactor text rendering




Modes:
- Ants makes animated border, independent from text animation, not affected by speed
- Blink makes animated text blink, constant frequency, about 450ms on, 450ms off, not affected by speed
- Modes: scroll left, scroll right, scroll up, scroll down, still centered, animation, drop down, curtain, laser
- Speeds: 1.2, 1.3, 2.0, 2.4, 2.8, 4.5, 7.5, 15 fps