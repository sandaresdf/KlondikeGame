# KlondikeGame

# Changes from Previous Assignments

## Assignment 4 Changes

### 1. Refactored BasicKlondike to extend AKlondike
- Moved common functionality to abstract base class AKlondike in hw04 package
- BasicKlondike now extends AKlondike and implements only variant-specific rules
- This change allows code reuse between BasicKlondike and WhiteheadKlondike
- BasicKlondike remains in hw02 package to maintain backward compatibility

### 2. Moved Pile class to hw04 package
- The Pile helper class is now in klondike.model.hw04 package
- Changed access modifier to public so both BasicKlondike (hw02) and WhiteheadKlondike (hw04) can use it
- This was necessary to share the class between different packages

### 3. Created Abstract Methods in AKlondike
The following abstract methods define variant-specific behavior:
- `setupInitialVisibility(int numPiles)` - Controls which cards are face-up initially
- `isValidCascadeMove(KlondikeCard, KlondikeCard)` - Defines color/suit rules for cascade moves
- `isValidBuildToMove(List<KlondikeCard>)` - Validates if multiple cards can be moved together
- `canMoveToEmptyPile(KlondikeCard)` - Determines what cards can go in empty cascade piles

### 4. BasicKlondike Implementation Details
BasicKlondike now implements the abstract methods with original game rules:
- Only bottom card visible initially
- Alternating colors required (red on black, black on red)
- Any visible build can be moved together
- Only Kings can move to empty piles

### 5. WhiteheadKlondike Implementation
New class implementing Whitehead variant rules:
- All cards visible from start
- Same color required (red on red, black on black)
- Multiple cards must be same suit to move together
- Any card can move to empty piles

### 6. No Changes to Controller or View
- KlondikeController interface remains unchanged
- KlondikeTextualController works with both variants without modification
- KlondikeTextualView works with both variants without modification
- This demonstrates proper MVC separation

### 7. Protected Fields in AKlondike
- Changed fields from private to protected so subclasses can access them
- Fields: cascadePiles, foundationPiles, drawPile, numDraw, gameStarted

### 8. Helper Methods Made Protected
- Methods like validateDeck(), countAces(), checkGameStarted(), etc. are now protected
- This allows subclasses to use common validation logic
