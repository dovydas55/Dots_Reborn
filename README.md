# Dots_Reborn
Simple android puzzle based on "Dots" Game

#### Features:
Main menu with selection of options, highscores, gameplay based on moves and gameplay based on time

##### Options:
* Selection between three board sizes (4x4, 6x6, 8x8)
* Selection between two themes, the default is Dovy's Theme, Gunnhildur's theme is light

##### Moves Mode:
* Fully functioning gameplay as in the original Dots game
* Making squares erases all dots of same color from the board and gives player one extra move 
* Each dot counts for 10 points and scores are displayed
* Player has 30 moves at start and they are decremented by one (except if player makes a square) and displayed 
* 3 types of cheats are available and player can cheat 3 times in total:
- Clicking the explosion icon and then double clicking a dot explodes all dots of that color from the board
- Shuffle repopulates the board with new random colors but other elements of the game stay intact
- Cross wipes out a cross shape of dots from the board when player double clicks on the center dot

##### Timed Mode:
* Same functionality as in moves mode but now the game ends after a displayed countdown of 30 seconds

##### High Scores:
* Player can select between displaying high scores from moves mode or timed mode
* In the display, player can switch between high scores for different board sizes
* Player can clear high scores for each board size 
* High scores are stored in SQLite database

##### Additional features:
* At the end of each game a view with player's score is displayed along with current high score in that mode & board size. The view gives player easy access to a new game or home menu
* Action bar settings allow user to turn on and off vibration (set to vibrate each time score increases), music during gameplay and sound effects
* All icons are homemade by developers

#### Knows bugs:
* There is a glitch in the animation when a vertical line of dots is erased. This however does not influence gameplay in any way, all the right colored dots end up in the right places.
