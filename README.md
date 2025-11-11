# SYSC 3110 UNO Flip

## Overview
**UNO FLIP!** plays just like classic UNO, but with a twist! Every card has **two sides**: a **Light Side** and a **Dark Side**.

The game begins on the **Light Side**, but when a player plays a **FLIP** card,**deck**, **discard pile**, and every player’s **hand** are flipped to the opposite side.

The **Dark Side** introduces new action cards with **tougher penalties** and higher stakes. Players continue playing on this side until another **FLIP** card is played, switching the game back to the **Light Side**.

Play alternates between both sides until a player has played all the cards in their hand, ending the game.

---
## Rules

The offical **UNO FLIP!** rules can be found here: https://service.mattel.com/instruction_sheets/GDR44-English.pdf

---
## Features

- ✅ Supports **2–4 players**  
- ✅ Implements **light and dark decks** with all **special action cards**
- ✅ Follows **official UNO Flip rules**  
- ✅ Fully **GUI-based** interface 
- ✅ Implements **MVC pattern** 
- ✅ Includes **error handling**, and **JUnit-tested game logic**  
- ✅ Provides **UML diagrams** and **sequence diagrams** for main gameplay scenarios
  
---
## How to Run

### Requirements
- **Java 25** or newer
- A terminal or IDE that can compile and run Java files

### Steps
```bash
# Compile all the Files
javac *.java

# Run the game
java UNO_Frame
```

---
## Contributions

### Milestone 1

**Ahmad**
- Implemented the action() method of the cards, so the cards act as intended
- Created javadocs for all classes/methods and added comments when necessary to help keep the code understandable
  

**Aryan**
- Implemente card subtypes (Flip, Number, Wild, DrawX, Wild Draw): indiviudal behaviours when played and validity of playing on play pile's top card (refactored by Ahmad)
- Added dark and light mode attributes for preparation of future milestones
- Implemented flip mechanic of switching light cards to their dark counterparts, and vice versa
- Implemented the skip player feature present in skip/skip everyone cards

**Atik**
- Helped implement the main game loop
- Created all the test cases

**Jonathan**
- Created README
- Created UML diagram
- Documented data structure usage
- Created Sequence diagram
- Implemented some game logic

### Milestone 2

**Ahmad**
- Helped implement the UNO_Frame class
- Added the Next Player button
- Updated all Java docs/comments
  

**Aryan**
- Maintained a dynamic to-do list and delegated group members deliverables and git operations
- Modified the UNO_Game (model) to notify the view using collection of UNO_Views
- Drew draw.io sequence diagrams with approval of finalized diagrams from gorup
- Corrected the round and winner handling system
- Created the initial GUI and setup of panels in the frame
- Created the view representation of cards (i.e. class CardComponent) with associated controller handling when used
- Performed all manual testing of gameplay

**Atik**
- Helped implement UNO_Controller class
- Created test cases for UNO_Game (the model)
- Integrated error handling (try-catch blocks) into the codel.

**Jonathan**
- Updated README
- Updated UML diagram
- Implemented the player's hand
- Implemented the top card
- Adjusted some game logic
- Implemented GUI to follow game logic
