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
- ✅ Implements **AI Bots** that you can play with
  
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


### Milestone 3

**Ahmad**
- Fixed some bugs related to the draw card and next player buttons being enabled/disabled when they shouldn't have been enabled/disabled,
- Developed the UI popup for choosing whether a player is AI and integrated the logic that marks players as AI in the game model.
- Added missing Javadocs and comments
  

**Aryan**
- After TA discussion regarding milestone 2's feedback on missing event model. Changed the MVC to not use an deprecated Observer pattern
  but instead utilizing one notifyViews() method in the model that creates a GameEvent (event object) that packages all relevant info
  while not needing to know how the view actually implements it.
- Milestone 2 feedback on UNO_Controller class handling game logic in methods handleWildColorSelection, handleCardPlay, and handleDrawCard
  has now been removed or reduced. I made a "thinner" controller that makes calls to the model mainly now.
- Implemented the AI strategy interface and coded a basic strategy implementation which has the rules of placing the first valid card (otherwise
  draw) and choosing a color for wild cards based on which color appears most frequently
- Implemented the handling of AI turns to automatically make decisions based on its assigned strategy
  
**Atik**
- Did code quality checks.
- Added test cases for AI Players to ensure moves are valid.
- Added test cases for UNO Flip to ensure both sides are implemented.
- Added javadoc to every method in ./tests to improve readability.

**Jonathan**
- Updated README
- Updated UML diagram
- Fixed bugs with the ai bots and the UI
- Implemented quality of life improvements to the gameplay loop to be more understandable for the player

### Milestone 5

**Ahmad**
- 

**Aryan**
- 

**Atik**
- 

**Jonathan**
- 

---

**Explanation of AI Player Strategy:**
All AI strategy implements the AIStrategy interface for methods for choosing a card and choosing a colour for wild cards, which enables for multiple strategies to be developed in future revisions. Milestone 3 utilizes a basic AI strategy that employs a straightforward decision making of choosing the first valid card (from left to right) from in the player’s hand to play. 

**I. Card Selection Strategy**
  i. Sequential Hand Scanning
  - The AI scans its hand from first to last card (index 0 to hand size-1)
  - It plays the first legally playable card it encounters that matches the current top card
  - This creates predictable but consistent gameplay behavior
    
  ii. Simple Playability Check
  - For each card in hand, the AI calls card.playableOnTop(topCard)
  - This method checks if the card matches the top card by either:
      Same color
      Same type/number
      Or if it's a Wild card (always playable)

  iii. Draw Decision
  - If no playable cards are found after scanning the entire hand, the AI chooses to draw a card (returns 0)
  - The drawn card is automatically evaluated for playability in the next turn
 
**II. Wild Card Color Selection**
  i. Color Frequency Analysis
  - The AI counts how many cards of each color it holds in its hand
  - It excludes Wild cards from this count since they don’t contribute to colour strategy\
  ii. Majority
  - The AI chooses the color that appears most frequently in its hand
  - This maximizes the probability of having follow-up plays in subsequent turns


## Issues

- The resoucre folder assets/ is not included in the jar file, making it not load the images. Please run the program by downloading all files and compile using the given commands.
