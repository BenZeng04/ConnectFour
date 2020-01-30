/*
Ben Zeng
Ms.Krasteva
2019-01-08
ConnectFour (Main Class)
This program is a connect 4 game, where two users can take turns dropping tiles in a 6 by 7 grid. Whoever gets 4 tiles in a row wins!
This file is the main class, which controls the flow of the program and is where the vast majority of the logic occurs.

Screens:
NAME                        DESCRIPTION
Splash Screen               A colourful splash screen that greets the user upon starting the program.
Main Menu                   The main menu of the program, where the user can choose to start a game, view past games, look on how to play, or exit.
Game Option Screen          The menu where both players can decide on a username, aswell as a colour for their connect 4 pieces.
Gameplay Screen             The menu where a given player can choose where to drop their connect four piece, and where their input will be displayed. All gameplay logic is here!
Victory Screen              The menu where information related to a victory displayed, including who won, and how they won. Here, the user can return to the main menu.
Instructions Screen         The menu where the user can find information on how to play the game.
Past Games Screen           The menu where the user can see the 10 most recent games that have been played.
Goodbye Screen              The menu that greets the user when the user decides to exit. 

Flow:
Starts in the splash screen, then immediately goes to the main menu.
From main menu, you can go to the game option screen, instructions screen, past games screen, or goodbye screen.
From instructions, past games, or goodbye, you can return to the main menu.
In the game option screen, you will be taken to the gameplay screen once you fill out information.
In the gameplay screen, you will be taken to the victory screen once you quit, you win, or you tie.
In the victory screen, you can then return to the main menu.

Instance Variables:
NAME                        TYPE                    DESCRIPTION
c                           Console                 The instance of the Console class where all information will be displayed on.
ROWS                        final int               A constant variable for the number of rows in the connect 4 board. Used in a variety of methods.
COLUMNS                     final int               A constant variable for the number of columns in the connect 4 board. Used in a variety of methods.
CONSECUTIVE                 final int               A constant variable for the number of pieces in a row needed to win. Used for processing in one of the black box return methods, and for other methods to interpret the information returned.
playerTurn                  int                     Represents whichever player's turn it currently is. This will be either 1 (Player 1) or 2 (Player 2).
columnInput                 int                     Represents the value the user inputted for where they want to drop their piece. Inputted in the gameInput() method and used in the gameDisplay() method.
grid                        int[][]                 Represents the connect 4 grid. A 0 represents an empty tile, a 1 represents a piece from player 1, and a 2 represents a piece from player 2.
victoryPosition             int[]                   Contains a LOT of information relating the victory state of the game. Used in a variety of gameplay-related methods.
username1                   String                  The username of the first player playing the game.
username2                   String                  The username of the second player playing the game.
pieceColor1                 Color                   The fill color of the first player's connect 4 piece.
pieceColor2                 Color                   The fill color of the second player's connect 4 piece.
pieceBorder1                Color                   The border color of the first player's connect 4 piece.
pieceBorder2                Color                   The border color of the second player's connect 4 piece.
gameFinished                boolean                 Whether or not the game has finished yet. Initialized in the gameplay methods, and used in the main method for control.
mainChoice                  int                     User input for the choice of the user in the main menu. Initialized in the main menu method and used in the main method for control.

Credits:
NAME/SOURCE                 REASON
Kenneth                     Gave me the idea of using getChar for input and drawString for displaying, rather than standard I/O. Also helped me fix my logic regarding the backspace, when inputting players' usernames.
https://www.desmos.com/     Used to help create a path for my Logo thread and my ConnectFourPiece thread.
https://www.geeksforgeeks.org/synchronized-in-java/ Used for information on the syncrhonzied() {} structure. Used in my 3 threads.
*/

import hsa.Console;
import java.awt.*;
import java.io.*;
import javax.swing.JOptionPane;

public class ConnectFour 
{    
    private Console c;
    private final int ROWS = 6, COLUMNS = 7, CONSECUTIVE = 4;
    private int playerTurn, columnInput;
    private int[][] grid;
    private int[] victoryPosition;
    private String username1, username2;
    private Color pieceColor1, pieceColor2, pieceBorder1, pieceBorder2;
    public boolean gameFinished;
    public int mainChoice;
    /*
    public ConnectFour()
    This is the default constructor for the class ConnectFour. This is also where the Console instance is initialized.
    */
    public ConnectFour()
    {
        c = new Console("Ben Zeng's Connect 4");
    }
    /*
    public void splashScreen()
    This method contains all the logic regarding the splash screen. It animates the animation shown at the beginning of the program using threads.
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    l                           Logo                    An instance of the Logo thread, which is responsible for animating my logo/title moving from the bottom of the screen to the top.
    yOffset                     int                     An integer representing the "row" that the current connect 4 piece will land on. Note that there aren't actually any rows here, as these pieces are simply for animation.
    onLeft                      boolean                 Represents whether or not the current connect 4 piece being animated will be on the left side or not. Out of the 8 for-loop iterations, each other piece will be on the left side.
    isRed                       boolean                 Represents whether or not the current connect 4 piece being animated will be red. The colours will alternate: Yellow, Yellow, Red, Red, Yellow, Yellow, Red, Red.
    CFP                         ConnectFourPiece        An instance of the ConnectFourPiece thread, which is responsible for animating one of the 8 falling pieces on the side of the screen. Since this variable is used inside of a for loop, it will cause 8 individual threads to animate.
    b                           Buttons                 An instance of the Buttons thread, which is responsible for animating the buttons fading in.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    for(int i = 7; i >= 0; i--)                         Used in conjunction with the ConnectFourPiece instances to create 8 animated pieces falling from the sides of the screen. The loop variable i is responsible for giving information on the position and colour of the pieces.
    
    Conditionals:
    STRUCTURE                                           DESCRIPTION
    if(onLeft)                                          Used to determine whether or not the animated connect 4 piece will be on the left side or the right. If it is on the left, it will be joined so that the next set (Left and Right) of connect four pieces will drop later.
    
    Other Structures:
    STRUCTURE                                           DESCRIPTION
    try { ... } catch(InterruptedException e)           Used for either creating a delay with Thread.sleep(), or utilizing the Thread.join() method to halt the program until the current thread is over.
    */
    public void splashScreen()
    {
        c.setColor(new Color(82, 113, 255)); // Background color
        c.fillRect(0, 0, 640, 500);
        Logo l = new Logo(c);
        l.start();
        for(int i = 7; i >= 0; i--)
        {
            int yOffset = i / 2;
            boolean onLeft = i % 2 == 0;
            boolean isRed = yOffset % 2 == 0;
            ConnectFourPiece CFP;
            if(onLeft)
                CFP = new ConnectFourPiece(c, 60, 270 + yOffset * 50, isRed);
            else
                CFP = new ConnectFourPiece(c, 580, 270 + yOffset * 50, isRed);
            CFP.start();
            // Delay before next set of pieces start falling
            if(onLeft)
            {
                try
                {
                    CFP.join();
                }
                catch(InterruptedException e) {}
            }
        }
        // Delay before buttons fade in
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException e) {}
        Buttons b = new Buttons(c);
        b.start();
        try
        {
            b.join();
        }
        catch(InterruptedException e) {}
        // Slight delay before 
        try
        {
            Thread.sleep(500);
        }
        catch(InterruptedException e) {}
    }
    /*
    public void mainMenu()
    This method contains all the logic regarding the main menu screen, including allowing the user to input which screen they would like to go next, using getChar() and drawString().
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    lastValidChoice             char                    This variable stores the most recent choice that the user had entered that is considered a valid choice for a menu screen.
    input                       char                    This is a temporary variable used to store the current choice/input that the user has entered. If this input is considered valid, then lastValidChoice will be updated to this.
    leftTriangleX               int[]                   Represents the x-coordinates of the left triangle being displayed to highlight exactly which choice the user entered.
    rightTriangleX              int[]                   Represents the x-coordinates of the right triangle being displayed to highlight exactly which choice the user entered.
    triangleY                   int[]                   Represents the y-coordinates of both triangles.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    for(int i = 0; i <= 240; i += 80)                   Used twice, for drawing the 4 buttons, and the shadow of the buttons.
    for(int i = 1; i <= 4; i++)                         Used for highlighing which key corresponds with which button using drawString(). Used twice for shadow and for actual button.
    while(true)                                         Used for taking input for which menu the user would like to go to. Will keep running forever until the user confirms their choice.
    for(int i = 0; i < triangleY.length; i++)           Used several times for translating the triangles being displayed, so that they line up with the corresponding buttons for their input.
    
    Conditionals:
    STRUCTURE                                           DESCRIPTION
    if(input >= '1' && input <= '4')
    else if(input == ' ' && lastValidChoice != '0') 
    else                                                Used for processing the most recent input that was inputted by the user, and deciding what to do with it. Checks if the input is valid, or if the user is confirming
    
    if(lastValidChoice != '0')                          Used for checking whether or not the user has already entered something valid. If they have, it means that there are triangles currently on the screen, that need to be erased. Then, new triangles can be drawn to highlight the user's current choice.
    */
    public void mainMenu()
    {
        title(); // Title
        /* Button Borders */
        c.setColor(new Color(55, 55, 55)); // Shadow
        for(int i = 0; i <= 240; i += 80)
            c.fillRoundRect(203, 153 + i, 240, 70, 20, 20);
        c.setColor(new Color(84, 90, 255)); // Button Color
        for(int i = 0; i <= 240; i += 80)
            c.fillRoundRect(200, 150 + i, 240, 70, 20, 20);
        
        /* Text on Buttons */
        c.setFont(new Font("Helvetica", Font.BOLD, 30));
        c.setColor(new Color(55, 55, 55)); // Shadow
        c.drawString("PLAY!", 278, 198);
        c.drawString("HELP!", 278, 278);
        c.drawString("PAST GAMES!", 223, 358);
        c.drawString("QUIT!", 278, 438);
        c.setColor(Color.WHITE); // Real Text
        c.drawString("PLAY!", 275, 195);
        c.drawString("HELP!", 275, 275);
        c.drawString("PAST GAMES!", 220, 355);
        c.drawString("QUIT!", 275, 435);
        c.setFont(new Font("Helvetica", Font.BOLD, 10));
        
        /* Information Text */
        c.setColor(new Color(55, 55, 55)); // Shadow
        for(int i = 1; i <= 4; i++)
            c.drawString("(Press " + i + ")", 296, 133 + i * 80);
        c.drawString("Press the spacebar to confirm your choice, once you have selected.", 151, 486);
        c.setColor(Color.WHITE); // Real Text
        for(int i = 1; i <= 4; i++)
            c.drawString("(Press " + i + ")", 295, 132 + i * 80);
        c.drawString("Press the spacebar to confirm your choice, once you have selected.", 150, 485);
        
        /* 
        TAKING USER INPUT:
        Note that I use Console.getChar() to take user input. 
        This is because if the user were to type into the console through standard means of I/O, the user-written text would potentially erase some of the buttons.
        Console.getChar() allows the user to enter a value without any text being displayed on the screen.
        */
        char lastValidChoice = '0';
        while(true)
        {
            char input = c.getChar(); // Setting up a temporary variable for input
            if(input >= '1' && input <= '4') // If the current input is valid
            {
                int[] leftTriangleX = {150, 150, 180}, rightTriangleX = {490, 490, 460}, triangleY = {210, 160, 185};
                // Erasing the old triangle (If there is one)
                if(lastValidChoice != '0')
                {
                    for(int i = 0; i < triangleY.length; i++) // Translating the triangle to where the old triangle was at
                        triangleY[i] += (lastValidChoice - '1') * 80; 
                     
                    c.setColor(new Color(82, 113, 255)); // Background color
                    c.fillPolygon(leftTriangleX, triangleY, 3);
                    c.fillPolygon(rightTriangleX, triangleY, 3);
                    
                    for(int i = 0; i < triangleY.length; i++) // Translating the triangle back
                        triangleY[i] -= (lastValidChoice - '1') * 80;
                }
                // Drawing triangles to indicate where their choice is at
                for(int i = 0; i < triangleY.length; i++) // Translating the triangle to where it is currently
                    triangleY[i] += (input - '1') * 80;
                c.setColor(Color.WHITE);
                c.fillPolygon(leftTriangleX, triangleY, 3);
                c.fillPolygon(rightTriangleX, triangleY, 3);
                lastValidChoice = input;
            }
            else if(input == ' ' && lastValidChoice != '0') // Checking if the user is confirming with their choice
                break;
            else
                JOptionPane.showMessageDialog(null, "Please enter a valid choice!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        mainChoice = lastValidChoice - '0';
    }
    /*
    public void selectGame()
    The method which controls all game customization, including player colours and usernames. All prompts for input, and actual input will be done here.
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    BACKSPACE                   final char              Stores the ASCII value of the backspace character, and is used when taking input for usernames.
    firstColorChoice            char                    Stores the character representation of which colour the first player would like to be. At the end of the this method, this will be used to determine the ACTUAL colour.
    secondColorChoice           char                    Stores the character representation of which colour the second player would like to be. At the end of the this method, this will be used to determine the ACTUAL colour.
    tempUsername                String                  Stores the current username that the user has already entered. This temporary variable will become the user's actual username once the user confirms their choice.
    input                       char                    Temporarily stores the immediate input entered, which will then be processed and checked if this input is valid. Used twice, for username, and for colour.
    yPosition                   int                     The Y-position of the colour bar that is used for displaying the different colours the user is allowed to choose. Calculated based off of a loop counter variable.
    lastValidChoice             char                    This variable stores the most recent choice that the user had entered that is considered a valid choice for a colour their player would like to be.
    triangleX                   int[]                   Stores the x-coordinates of the triangle being displayed to highlight the user's colour choice.
    triangleY                   int[]                   Stores the y-coordinates of the triangle being displayed to highlight the user's colour choice.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    for(int player = 1; player <= 2; player++)          Used to help shorten code, as input must be taken twice, for both players in the game.
    while(true)                                         Used twice within the above for loop, used for taking input for which menu the user would like to go to. Will keep running forever until the user confirms a valid choice.
    for(int row = 1; row <= 4; row++)                   Used for drawing the 4 colour choices that the user can select from.
    for(int i = 0; i < triangleY.length; i++)           Used several times for translating the triangles being displayed, so that they line up with the corresponding colour choices for their input.
    
    Conditionals:
    STRUCTURE                                           DESCRIPTION
    if(input >= '0' && input <= '9' || input >= 'A' && input <= 'Z' || input >= 'a' && input <= 'z')
    else if(input == ' ')
    else if(input == BACKSPACE)
    else                                                Used for processing the user's input for a character they would like to put in their username. If it is an alphanumeric character, try to add it. If it is a space, confirm the choice. If it is a backspace, remove a character from the username. If it is invalid, show an error box. 
    
    if(tempUsername.length() < 8)
    else                                                Checks whether or not the you can still add a character to the username without it exceeding the lengh limit. If you can't, display an error.
    
    if(tempUsername.length() > 0)
    else                                                Used when confirming the username. If the length of the username is 0 (The username is empty), do not let them set that as their username, and then display an error.
    
    if(player == 2 && tempUsername.toLowerCase().equals(username1.toLowerCase()))
    else                                                Used when confirming the username. If the second player's username is equal to the first player's, display an error, as they are not allowed to have the same name. The toLowerCase() is to make this not case sensitive.
    
    if(tempUsername.length() > 0)                       Used in the BACKSPACE part of the first if-structure. Checks whether or not there are characters that can be deleted. If the string is empty, do not allow the user to delete any more characters.
    
    if(player == 1)                                     
    else                                                Used when assigning the variable tempUsername, or the variable lastColorChoice, to the actual player once the input has been confirmed. Checks whether or not it should be assigned to player 1, or player 2.
    
    if(row == 1)
    else if(row == 2)
    else if(row == 3)
    else                                                Used when displaying the different colour choices that the user can pick. Used to determine what the actual colour being displayed as an option will be.
    
    if(input >= '1' && input <= '4')
    else if(input == ' ' && lastValidChoice != '0')
    else                                                Used when processing the input for a colour choice. If it is valid, set the last valid choice as the input. If they are confirming, make sure that the last valid choice is not blank. Otherwise, display an error.
    
    if(lastValidChoice != '0')                          Used for checking whether or not the user has already entered something valid for a colour choice. If they have, it means that there are triangles currently on the screen, that need to be erased. Then, new triangles can be drawn to highlight the user's current choice.
    
    if(firstColorChoice == '1')
    else if(firstColorChoice == '2')
    else if(firstColorChoice == '3')
    else                                                Used for assigning an actual colour to the first player, based off of the character representation of this colour. '1' is red, '2' is orange, '3' is yellow, '4' for green.
    
    if(secondColorChoice == '1')
    else if(secondColorChoice == '2')
    else if(secondColorChoice == '3')
    else                                                Used for assigning an actual colour to the second player, based off of the character representation of this colour. '1' is red, '2' is orange, '3' is yellow, '4' for green.
    */
    public void selectGame()
    {
        final char BACKSPACE = (char) 8; 
        char firstColorChoice = ' ', secondColorChoice = ' '; 
        title();
        /* Customization choices for BOTH users */
        for(int player = 1; player <= 2; player++)
        {
            /* Username Prompts and Input */
            c.setColor(new Color(55, 55, 55)); // Shadow
            c.fillRect(123, 183, 400, 50);
            c.setColor(new Color(84, 90, 255)); // Button Color
            c.fillRect(120, 180, 400, 50);
            // Prompting
            c.setFont(new Font("Helvetica", Font.BOLD, 42));
            c.setColor(new Color(55, 55, 55)); // Shadow
            c.drawString("P L A Y E R   " + player, 178, 223);
            c.setColor(Color.WHITE);
            c.drawString("P L A Y E R   " + player, 175, 220);
            c.setFont(new Font("Helvetica", Font.BOLD, 10));
            c.setColor(new Color(55, 55, 55)); // Shadow
            c.drawString("Please enter your username! Press the spacebar to confirm, once you have typed it.", 121, 486);
            c.setColor(Color.WHITE);
            c.drawString("Please enter your username! Press the spacebar to confirm, once you have typed it.", 120, 485);
        
            c.setFont(new Font("Helvetica", Font.BOLD, 18));
            c.setColor(new Color(55, 55, 55));
            c.fillRect(153, 263, 340, 30);
            c.setColor(Color.WHITE);
            c.fillRect(150, 260, 340, 30);
            /* 
            Taking user input for username:
            Note that I use Console.getChar() to take user input. 
            This is because if the user were to type into the console through standard means of I/O, the user-written text would potentially erase some of the buttons.
            Console.getChar() allows the user to enter a value without any text being displayed on the screen.
            */
            String tempUsername = "";
            while(true)
            {
                char input = c.getChar(); // Setting up a temporary variable for input
                if(input >= '0' && input <= '9' || input >= 'A' && input <= 'Z' || input >= 'a' && input <= 'z') // If the current input is valid (Alphanumeric)
                {
                    if(tempUsername.length() < 8)
                    {
                        tempUsername += input;
                        c.setColor(Color.WHITE);
                        c.fillRect(150, 260, 340, 30);
                        c.setColor(Color.BLACK);
                        c.drawString(tempUsername, 160, 280);
                    }
                    else
                        JOptionPane.showMessageDialog(null, "ERROR: Your username must be between 1 and 8 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(input == ' ') // Checking if the user is confirming with their choice
                { 
                    if(tempUsername.length() > 0)
                    {
                        if(player == 2 && tempUsername.toLowerCase().equals(username1.toLowerCase()))
                            JOptionPane.showMessageDialog(null, "ERROR: Your username must differ from player 1's username.", "Error", JOptionPane.ERROR_MESSAGE);
                        else
                            break;
                    }
                    else
                        JOptionPane.showMessageDialog(null, "ERROR: Your username must be between 1 and 8 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(input == BACKSPACE)
                {
                    if(tempUsername.length() > 0)
                    {
                        tempUsername = tempUsername.substring(0, tempUsername.length() - 1);
                        c.setColor(Color.WHITE);
                        c.fillRect(150, 260, 340, 30);
                        c.setColor(Color.BLACK);
                        c.drawString(tempUsername, 160, 280);
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "Please enter an alphanumeric character!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Setting the username
            if(player == 1)
                username1 = tempUsername;
            else
                username2 = tempUsername;
            
            /* Colour Prompts and input */
            // Erasing the previous input to make space
            c.setColor(new Color(82, 113, 255)); // Background Color
            c.fillRect(110, 240, 420, 260);
            // Main prompt
            c.setFont(new Font("Helvetica", Font.BOLD, 10));
            c.setColor(new Color(55, 55, 55)); // Shadow
            c.drawString("Please select your color! Press the spacebar to confirm, once you have typed it.", 121, 486);
            c.setColor(Color.WHITE);
            c.drawString("Please select your color! Press the spacebar to confirm, once you have typed it.", 120, 485);
            // Drawing the colour options
            for(int row = 1; row <= 4; row++)
            {
                int yPosition = 210 + row * 50;
                c.setColor(new Color(55, 55, 55)); // Shadow
                c.drawString("(Press "+row+"!)", 171, yPosition + 19);
                c.fillRect(243, yPosition + 3, 170, 30);
                
                c.setColor(Color.WHITE);
                c.drawString("(Press "+row+"!)", 170, yPosition + 18);
                c.fillRect(240, yPosition, 170, 30);
                
                if(row == 1)
                    c.setColor(new Color(245, 103, 103));
                else if(row == 2)
                    c.setColor(new Color(255, 178, 54));
                else if(row == 3)
                    c.setColor(new Color(242, 203, 94));
                else 
                    c.setColor(new Color(67, 209, 126));
                c.fillRect(243, yPosition + 3, 164, 24);
            }
            /* 
            Taking user input for user color:
            Note that I use Console.getChar() to take user input. 
            This is because if the user were to type into the console through standard means of I/O, the user-written text would potentially erase some of the buttons.
            Console.getChar() allows the user to enter a value without any text being displayed on the screen.
            */
            char lastValidChoice = '0';
            while(true)
            {
                char input = c.getChar(); // Setting up a temporary variable for input
                if(input >= '1' && input <= '4') // If the current input is valid
                {
                    int[] triangleX = {450, 450, 430}, triangleY = {285, 265, 275};
                    // Erasing the old triangle (If there is one)
                    if(lastValidChoice != '0')
                    {   
                        for(int i = 0; i < triangleY.length; i++) // Translating the triangle to where the old triangle is at
                            triangleY[i] += (lastValidChoice - '1') * 50;
                        
                        c.setColor(new Color(82, 113, 255)); // Background Color
                        c.fillPolygon(triangleX, triangleY, 3);
                        
                        for(int i = 0; i < triangleY.length; i++) // Translating it back
                            triangleY[i] -= (lastValidChoice - '1') * 50;
                    }
                    // Drawing triangles to indicate where they are at
                    for(int i = 0; i < triangleY.length; i++) // Translating the triangle to where the current triangle should be
                        triangleY[i] += (input - '1') * 50;
                    c.setColor(Color.WHITE);
                    c.fillPolygon(triangleX, triangleY, 3);
                    
                    lastValidChoice = input;
                }
                else if(input == ' ' && lastValidChoice != '0') // Checking if the user is confirming with their choice
                {
                    if(player == 2 && lastValidChoice == firstColorChoice)
                        JOptionPane.showMessageDialog(null, "ERROR: Your color must differ from player 1's color.", "Error", JOptionPane.ERROR_MESSAGE);
                    else
                        break;
                }
                else
                    JOptionPane.showMessageDialog(null, "Please enter a valid choice!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if(player == 1)
                firstColorChoice = lastValidChoice;
            else
                secondColorChoice = lastValidChoice;
            // Erasing previous input to make space
            c.setColor(new Color(82, 113, 255));
            c.fillRect(110, 150, 420, 350);
        }
        // Updating the global variables 
        if(firstColorChoice == '1') // Red
        {
            pieceColor1 = new Color(245, 103, 103);
            pieceBorder1 = new Color(189, 30, 30);
        }
        else if(firstColorChoice == '2') // Orange
        {
            pieceColor1 = new Color(255, 178, 54);
            pieceBorder1 = new Color(196, 121, 0);
        }
        else if(firstColorChoice == '3') // Yellow
        {
            pieceColor1 = new Color(242, 203, 94);
            pieceBorder1 = new Color(247, 174, 2);
        }
        else // Green
        {
            pieceColor1 = new Color(67, 209, 126); 
            pieceBorder1 = new Color(0, 130, 39);
        } 
        if(secondColorChoice == '1') // Red
        {
            pieceColor2 = new Color(245, 103, 103);
            pieceBorder2 = new Color(189, 30, 30);
        }
        else if(secondColorChoice == '2') // Orange
        {
            pieceColor2 = new Color(255, 178, 54);
            pieceBorder2 = new Color(196, 121, 0);
        }
        else if(secondColorChoice == '3') // Yellow
        {
            pieceColor2 = new Color(242, 203, 94);
            pieceBorder2 = new Color(247, 174, 2);
        }
        else // Green
        {
            pieceColor2 = new Color(67, 209, 126);
            pieceBorder2 = new Color(0, 130, 39);
        } 
    }
    /*
    public void instructions()
    This method displays instructions on how to play this game to the user.
    */
    public void instructions()
    {
        title();
        /* Header */
        // Header Button
        c.setColor(new Color(55, 55, 55)); // Shadow
        c.fillRect(103, 153, 440, 28);
        c.setColor(new Color(75, 103, 232)); // Real
        c.fillRect(100, 150, 440, 28);
        // Text on Header
        c.setFont(new Font("Helvetica", Font.BOLD, 15));
        c.setColor(new Color(55, 55, 55)); // Shadow
        c.drawString("- How To Play -", 254, 172);
        c.setColor(new Color(242, 203, 94)); // Real
        c.drawString("- How To Play -", 252, 170);
        
        /* Line for decoration */
        c.setColor(new Color(55, 55, 55)); // Shadow
        c.fillRect(121, 306, 400, 2);
        c.setColor(new Color(120, 205, 255)); // Real
        c.fillRect(120, 305, 400, 2);
        
        /* Instructions */
        c.setFont(new Font("Helvetica", Font.BOLD, 16));
        c.setColor(new Color(55, 55, 55)); // Shadow
        c.drawString("Connect 4 is a two-player game, where you and", 132, 217);
        c.drawString("your opponent get to choose a specifc colour", 132, 242);
        c.drawString("and will take turns dropping pieces of your", 132, 267);
        c.drawString("colour on a " + ROWS + " row by " + COLUMNS + " column grid.", 132, 292);
        c.drawString("To WIN, you must strategically drop pieces.", 132, 332);
        c.drawString("First person who manages to get " + CONSECUTIVE + " pieces in a row,", 132, 357);
        c.drawString("will WIN the game!", 132, 382);
        c.setColor(Color.WHITE); // Real Text
        c.drawString("Connect 4 is a two-player game, where you and", 130, 215);
        c.drawString("your opponent get to choose a specifc colour", 130, 240);
        c.drawString("and will take turns dropping pieces of your", 130, 265);
        c.drawString("colour on a " + ROWS + " row by " + COLUMNS + " column grid.", 130, 290);
        c.drawString("To WIN, you must strategically drop pieces.", 130, 330);
        c.drawString("First person who manages to get " + CONSECUTIVE + " pieces in a row,", 130, 355);
        c.drawString("will WIN the game!", 130, 380);
        pauseProgram();
    }
    /*
    public void pastGames()
    This method displays the 10 most recent games played in this program.
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    br                          BufferedReader          The instance of the BufferedReader class that is responsible for all inputting from files.
    data                        String[]                This array stores the old data that was already in the file.
    entryCount                  int                     Represents the number of entries in the file. It will increase per non-null object that is read, and will also be used as the index in the data array. 
    input                       String                  A temporary variable, storing the current line being read.
    displayMessage              String                  The message that will be shown to the user, regarding the current entry, and will give the user information about this game.
    info                        String[]                The raw information from a given entry. Will then be processed into useful information that can be added to the displayMessage. 
    victoryType                 String                  The reason why the game ended. (Win, Tie, or Quit.) Corresponds to index 2 in the info array.
    gameEnder                   String                  The player which caused the game to end. Corresponds to index 3 in the info array.
    username1                   String                  The username of the player which caused the game to end. Corresponds to either index 0 or 1, depending on the gameEnder variable.
    username2                   String                  The username of the other player. Corresponds to either index 0 or 1, depending on the gameEnder variable.
    input                       char                    Temporarily stores the immediate input entered, which will then be processed and checked if this input is valid. 
    lastValidChoice             char                    This variable stores the most recent choice that the user had entered that is considered a valid choice. (Either 1 or 2, for returning to main menu or clearing scores)
    triangleX                   int[]                   Stores the x-coordinates of the triangle being displayed to highlight the user's button choice.
    triangleY                   int[]                   Stores the y-coordinates of the triangle being displayed to highlight the user's button choice.
    pr                          PrintWriter             The instance of the PrintWriter class that is responsible for clearing the file.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    while(input != null)                                Used to read input from a file into an array, until one of the entries is null.
    for(int i = 0; i < data.length; i++)                Used for iterating the data array and processing each entry.
    while(true)                                         Used for taking input on whether or not the user would like to simply exit the menu, or clear all their data. This will run forever until the user confirms a valid choice.
    for(int i = 0; i < triangleX.length; i++)           Used several times for translating the triangles being displayed, so that they line up with the corresponding button for their input.
    
    Conditionals:
    STRUCTURE                                           DESCRIPTION
    if(entryCount == 0)
    else                                                Checks whether or not there are any entries in the file for past games. The text displayed will be adjusted accordingly based off of this.
    
    if(data[i] == null)                                 Checks whether or not the current element in the data array being iterated is null. If it is, break out of the loop and do not display anything after. This way, only "valid" entries will be displayed.
    
    if(gameEnder.equals("1"))         
    else                                                Used to check whether or not player 1 or player 2 caused the game to end. Also used to properly initialize the variables username1 and username2, as username1 should be the username of the player which caused the game to end.
    
    if(victoryType.equals("TIE"))
    else if(victoryType.equals("WIN"))
    else                                                Used to create the message neccesary for being displayed per recently played game.
    
    if(input == '1')
    else if(input == '2')
    else if(input == ' ' && lastValidChoice != '0')
    else                                                Used for processing the user's immediate input and checking if it is valid. Also allows the user to confirm as long as they have entered a valid choice before.    
    
    if(lastValidChoice == '2')                          Used if the current input is '1'. Checks whether or not the last entry was NOT a '1'. If it wasn't, erase the old triangle, which should be located 500 pixels to the right. Also appears at the very end of the method, which is responsible for clearing the past games played if the user has selected to.
    if(entryCount == 0)                                 Checks whether or not there are actual entries that can be cleared. If there aren't, there is no point in letting the user clear their past games.
    if(lastValidChoice == '1')                          Used if the current input is '2'. Checks whether or not the last entry was NOT a '2'. If it wasn't, erase the old triangle, which should be located 500 pixels to the left.
    
    Other Structures:
    STRUCTURE                                           DESCRIPTION
    try { ... } catch(IOException e)                    Used twice, once to read input from the PastGames file and put it into an array, and another time for clearing the file if the user has chose to.
    
    */
    public void pastGames()
    {
        title();
        BufferedReader br;
        String[] data = new String[10];
        int entryCount = 0;
        try
        {
            br = new BufferedReader(new FileReader("PastGames.txt"));
            String input = br.readLine();
            while(input != null)
            {
                data[entryCount] = input;
                entryCount++;
                input = br.readLine();
            }
        }
        catch(IOException e) {}
        /* Header */
        // Border of header
        c.setColor(new Color(55, 55, 55)); // Shadow
        c.fillRect(103, 153, 440, 28);
        c.setColor(new Color(75, 103, 232)); // Real
        c.fillRect(100, 150, 440, 28);
        // Text
        c.setFont(new Font("Helvetica", Font.BOLD, 15));
        c.setColor(new Color(55, 55, 55)); // Shadow
        c.drawString("- Recent Games -", 252, 172);
        c.setColor(new Color(242, 203, 94));
        c.drawString("- Recent Games -", 250, 170);

        /* Information Display */
        if(entryCount == 0)
        {
            c.setFont(new Font("Helvetica", Font.BOLD, 25));
            c.setColor(new Color(55, 55, 55));
            c.drawString("No Entries Availible!", 203, 293);
            c.setColor(Color.WHITE);
            c.drawString("No Entries Availible!", 200, 290);
        }
        else
        {
            c.setFont(new Font("Helvetica", Font.BOLD, 11));
            for(int i = 0; i < data.length; i++)
            {
                if(data[i] == null) 
                    break;
                String displayMessage = "";
                String[] info = data[i].split(" ");
                String victoryType = info[2], gameEnder = info[3];
                String username1, username2;
                if(gameEnder.equals("1"))
                {
                    username1 = info[0];
                    username2 = info[1];
                }
                else
                {
                    username1 = info[1];
                    username2 = info[0];
                }
                if(victoryType.equals("TIE")) 
                    displayMessage = username1 + " has tied against " + username2 + "!";
                else if(victoryType.equals("WIN"))
                    displayMessage = username1 + " has won against " + username2 + "! (" + info[4] + " in a row)";
                else
                    displayMessage += username1 + " has surrendered against " + username2 + "!";
                // Text
                c.setColor(new Color(55, 55, 55));
                c.drawString(displayMessage, 131, 201 + i * 27);
                c.setColor(Color.WHITE);
                c.drawString(displayMessage, 130, 200 + i * 27);
                // Line under text for decoration
                c.setColor(new Color(55, 55, 55)); // Shadow
                c.fillRect(121, 206 + i * 27, 400, 2);
                c.setColor(new Color(120, 205, 255)); // Real 
                c.fillRect(120, 205 + i * 27, 400, 2);
            }
        }
        /* Exit and Clear buttons */
        // Shadow
        c.setColor(new Color(55, 55, 55));
        c.fillRoundRect(23, 83, 100, 45, 15, 15);
        c.fillRoundRect(523, 83, 100, 45, 15, 15);
        // Actual
        c.setColor(new Color(84, 90, 255));
        c.fillRoundRect(20, 80, 100, 45, 15, 15);
        c.fillRoundRect(520, 80, 100, 45, 15, 15);
        /* Text on buttons */
        c.setFont(new Font("Helvetica", Font.BOLD, 10));
        // Shadow
        c.setColor(new Color(55, 55, 55));
        c.drawString("Enter '1' to exit", 31, 101);
        c.drawString("this menu.", 31, 116);
        c.drawString("Enter '2' to clear", 531, 101);
        c.drawString("all games!", 531, 116);
        c.drawString("Press the spacebar to confirm your choice, once you have selected.", 151, 486);
        // Actual
        c.setColor(Color.WHITE);
        c.drawString("Enter '1' to exit.", 30, 100);
        c.drawString("this menu.", 30, 115);
        c.drawString("Enter '2' to clear", 530, 100);
        c.drawString("all games!", 530, 115);
        c.drawString("Press the spacebar to confirm your choice, once you have selected.", 150, 485);
        /* 
        TAKING USER INPUT:
        Note that I use Console.getChar() to take user input. 
        This is because if the user were to type into the console through standard means of I/O, the user-written text would potentially erase some of the buttons.
        Console.getChar() allows the user to enter a value without any text being displayed on the screen.
        */
        // User input for if they would like to clear the highscores
        char lastValidChoice = '0';
        while(true)
        {
            char input = c.getChar(); // Setting up a temporary variable for input
            if(input == '1')
            {
                int[] triangleX = new int[]{55, 70, 85}, triangleY = new int[]{55, 70, 55};
                // Erasing the old triangle (If there is one that needs to be erased!)
                if(lastValidChoice == '2')
                {   
                    for(int i = 0; i < triangleX.length; i++) // Translating the triangle to where the old triangle is at
                        triangleX[i] += 500;

                    c.setColor(new Color(82, 113, 255)); // Background Color
                    c.fillPolygon(triangleX, triangleY, 3);

                    for(int i = 0; i < triangleX.length; i++) // Translating it back
                        triangleX[i] -= 500;
                }
                // Drawing a triangle to indicate the user's choice
                c.setColor(Color.WHITE);
                c.fillPolygon(triangleX, triangleY, 3);

                lastValidChoice = input;
            }
            else if(input == '2')
            {
                if(entryCount == 0)
                    JOptionPane.showMessageDialog(null, "ERROR: No entries availible for clearing!", "Error", JOptionPane.ERROR_MESSAGE);
                else
                {
                    int[] triangleX = new int[]{555, 570, 585}, triangleY = new int[]{55, 70, 55};
                    // Erasing the old triangle (If there is one that needs to be erased!)
                    if(lastValidChoice == '1')
                    {   
                        for(int i = 0; i < triangleX.length; i++) // Translating the triangle to where the old triangle is at
                            triangleX[i] -= 500;

                        c.setColor(new Color(82, 113, 255)); // Background Color
                        c.fillPolygon(triangleX, triangleY, 3);

                        for(int i = 0; i < triangleX.length; i++) // Translating it back
                            triangleX[i] += 500;
                    }
                    // Drawing a triangle to indicate the user's choice
                    c.setColor(Color.WHITE);
                    c.fillPolygon(triangleX, triangleY, 3);
                    lastValidChoice = input;
                }
            }
            else if(input == ' ' && lastValidChoice != '0') // Checking if the user is confirming with their choice
                break;
            else
                JOptionPane.showMessageDialog(null, "Please enter a valid choice!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if(lastValidChoice == '2') // If the user would like to clear the highscores. Otherwise, nothing needs to be done and the method finishes.
        {
            c.setColor(new Color(82, 113, 255)); // Background Color
            c.fillRect(110, 185, 420, 315); // Clearing all old display
            try
            {
                PrintWriter pr = new PrintWriter(new FileWriter("PastGames.txt")); // Using an instance of the PrintWriter class to overwrite the old data
                pr.close();
            }
            catch(IOException e) {}
            // Prompting user that they were successful.
            c.setFont(new Font("Helvetica", Font.BOLD, 25));
            c.setColor(new Color(55, 55, 55));
            c.drawString("Successfully cleared all games!", 133, 293);
            c.setColor(Color.WHITE);
            c.drawString("Successfully cleared all games!", 130, 290);
            pauseProgram();
        }
    }
    /*
    public void goodbye()
    This method displays a exit greeting to the user, and then closes the program.
    */
    public void goodbye()
    {
        title();
        /* Header */
        // Header Button
        c.setColor(new Color(55, 55, 55)); // Shadow
        c.fillRect(103, 153, 440, 28);
        c.setColor(new Color(75, 103, 232)); // Real
        c.fillRect(100, 150, 440, 28);
        // Text on Header
        c.setFont(new Font("Helvetica", Font.BOLD, 15));
        c.setColor(new Color(55, 55, 55)); // Shadow
        c.drawString("- Goodbye! -", 256, 172);
        c.setColor(new Color(242, 203, 94)); // Real
        c.drawString("- Goodbye! -", 254, 170);
        
        /* Line for decoration */
        c.setColor(new Color(55, 55, 55)); // Shadow
        c.fillRect(122, 317, 400, 5);
        c.setColor(new Color(120, 205, 255)); // Real
        c.fillRect(120, 315, 400, 5);
        
        /* Goodbye Text */
        c.setFont(new Font("Helvetica", Font.BOLD, 35));
        c.setColor(new Color(55, 55, 55)); // Shadow
        c.drawString("Thank you for", 183, 238);
        c.drawString("using this program!", 158, 288);
        c.setColor(Color.WHITE); // Real Text
        c.drawString("Thank you for", 180, 235);
        c.drawString("using this program!", 155, 285);
        
        c.setFont(new Font("Helvetica", Font.BOLD, 10));
        c.setColor(new Color(55, 55, 55)); // Shadow
        c.drawString("(This program was made by Ben Zeng. Finished on 1/9/2020)", 151, 356);
        c.setColor(Color.WHITE); // Real Text
        c.drawString("(This program was made by Ben Zeng. Finished on 1/9/2020)", 150, 355);
        pauseProgram();
        c.close();
    }
    /*
    public void startGame()
    This method initializes all the instance variables used for gameplay such as the grid. This method also displays the basic information involving the game, such as the borders of the grid.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    for(int i = 0; i <= COLUMNS; i++)                   This loop is used several times, for drawing the outline of the grid's columns, the shadow of those outlines, and a number under each column corresponding to the column number.
    for(int j = 0; j <= 5; j++)                         Used to draw a slightly thicker vertical line, by running c.drawLine() several times.
    for(int i = 0; i <= 5; i++)                         Used to draw the long horizontal line under all of the vertical columns. Calls the c.drawLine() function several times to draw a slightly thicker line.
    */
    public void startGame()
    {
        // Game logic initialization 
        playerTurn = 1;
        grid = new int[COLUMNS][ROWS];
        
        /* Drawing the gameplay board */
        // Shadow
        c.setColor(new Color(55, 55, 55));
        for(int i = 0; i <= COLUMNS; i++) 
        {
            int top = 178, bottom = 478;
            int xPosition = 128 + i * 55;
            for(int j = 0; j <= 5; j++)
                c.drawLine(xPosition + j, top, xPosition + j, bottom);
        }
        for(int i = 0; i <= 5; i++) 
            c.drawLine(128, 478 + i, 518, 478 + i);
        // Actual Board
        c.setColor(Color.WHITE);
        for(int i = 0; i <= COLUMNS; i++) // Actual Board
        {
            int top = 175, bottom = 475;
            int xPosition = 125 + i * 55;
            for(int j = 0; j <= 5; j++)
                c.drawLine(xPosition + j, top, xPosition + j, bottom);
        }
        for(int i = 0; i <= 5; i++)
            c.drawLine(125, 475 + i, 515, 475 + i);
        
        /* Information Buttons */
        // Shadow
        c.setColor(new Color(55, 55, 55));
        c.fillRoundRect(523, 83, 100, 30, 15, 15);
        c.fillRect(23, 33, 100, 30);
        c.fillRect(13, 73, 120, 30);
        // Actual
        c.setColor(new Color(84, 90, 255));
        c.fillRoundRect(520, 80, 100, 30, 15, 15);
        c.fillRect(20, 30, 100, 30);
        c.setColor(pieceColor1);
        c.fillRect(10, 70, 120, 30);
        
        /* Text on buttons */
        c.setFont(new Font("Helvetica", Font.BOLD, 10));
        // Shadow
        c.setColor(new Color(55, 55, 55));
        c.drawString("Current Player:", 31, 51);
        c.drawString("Enter '0' to quit.", 531, 101);
        // Actual
        c.setColor(Color.WHITE);
        c.drawString("Current Player:", 30, 50);
        c.drawString("Enter '0' to quit.", 530, 100);
        
        /* Username Display (Currently on Player 1) */
        c.setFont(new Font("Helvetica", Font.BOLD, 14));
        c.setColor(new Color(55, 55, 55));
        c.drawString(username1, 21, 91);
        c.setColor(pieceBorder1);
        c.drawString(username1, 20, 90);
        
        /* Column Labels */
        for(int i = 1; i <= COLUMNS; i++)
        {
            c.setColor(new Color(55, 55, 55));
            c.drawString(i + "", 99 + i * 55, 496);
            c.setColor(Color.WHITE);
            c.drawString(i + "", 98 + i * 55, 495);
        }
    }
    /*
    public void gameInput()
    This method is responsible for prompting the user for where they want to drop a piece, as well as taking input.
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    lastValidColumn             char                    Used to store the last input that the user entered that is valid.
    input                       char                    Used as a temporary variable to store the current input the user is entering.
    triangleX                   int[]                   The x-coordinates of the triangle being drawn to highlight the column that the user would want to drop their piece.
    triangleY                   int[]                   The y-coordinates of the triangle being drawn to highlight the column that the user would want to drop their piece.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    while(true)                                         Used for taking input on where the user would like to drop their piece, or if a user would like to quit. This will run forever until the user confirms a valid choice.
    for(int i = 0; i < triangleX.length; i++)           Used for translating the triangle highlighting the column the user selected, to it's appropriate position. This may either be for erasing old input, or highlighting new one.
    
    Conditionals:
    STRUCTURE                                           DESCRIPTION
    if(input >= '1' && input <= '7')
    else if(input == '0')
    else if(input == ' ' && lastValidColumn != '8')
    else                                                Used for processing the user's raw input. If the input is between 1 and 7, it is a valid column. If it is 0, they would like to quit. If it is a spacebar, and the last valid choice is not the default blank choice, confirm. Otherwise, show an error.
    
    if(dropPiece(input - '0' - 1, grid) != -1)
    else                                                Used to make sure the column the user wants to drop their piece in is not already full. Otherwise, show an error.
    
    if(lastValidColumn != '8')                          Used to check whether or not there was an "old choice". If there was an old choice, it means that there is currently a triangle somewhere that needs to be erased.
    
    if(lastValidColumn == '0')
    else                                                Used to differentiate between a column input, and input to exit the program. The button for exiting the program is at the top right corner, while all the columns are next to each other in the middle. This is used to figure out where the triangles must be displayed/erased.
    
    if(playerTurn == 1)     
    else                                                Used when displaying the triangle highlighting a player's choice. The triangle being displayed will always be the colour of the player's connect 4 piece.
    */
    public void gameInput()
    {
        // Erasing old prompts
        c.setColor(new Color(82, 113, 255));
        c.fillRect(0, 143, 640, 30);
        c.setFont(new Font("Helvetica", Font.BOLD, 10));
        c.setColor(new Color(55, 55, 55));
        c.drawString("Press the number corresponding to where you want to drop your piece!", 151, 156);
        c.drawString("(Press the spacebar to confirm your choice)", 211, 168);
        c.setColor(Color.WHITE);
        c.drawString("Press the number corresponding to where you want to drop your piece!", 150, 155);
        c.drawString("(Press the spacebar to confirm your choice)", 210, 167);
        
        char lastValidColumn = '8';
        while(true)
        {
            char input = c.getChar(); // Setting up a temporary variable for input
            if(input >= '1' && input <= '7') // If the current input is valid
            {
                if(dropPiece(input - '0' - 1, grid) != -1)
                {
                    int[] triangleX = {140, 155, 170}, triangleY = {195, 210, 195};
                    // Erasing the old triangle (If there is one)
                    if(lastValidColumn != '8')
                    {   
                        if(lastValidColumn == '0')
                        {
                            triangleX = new int[]{555, 570, 585};
                            triangleY = new int[]{55, 70, 55};
                        }
                        else
                        {
                            for(int i = 0; i < triangleX.length; i++) 
                                triangleX[i] += (lastValidColumn - '1') * 55;
                        }
                        c.setColor(new Color(82, 113, 255));
                        c.fillPolygon(triangleX, triangleY, 3);
                        triangleX = new int[]{140, 155, 170};
                        triangleY = new int[]{195, 210, 195};
                    }
                    // Drawing triangles to indicate where they are at
                    for(int i = 0; i < triangleX.length; i++) 
                        triangleX[i] += (input - '1') * 55;
                    
                    if(playerTurn == 1) 
                        c.setColor(pieceColor1);
                    else
                        c.setColor(pieceColor2);
                    c.fillPolygon(triangleX, triangleY, 3);
                    lastValidColumn = input;
                }
                else
                    JOptionPane.showMessageDialog(null, "This column is already full!", "Error", JOptionPane.ERROR_MESSAGE);           
            }
            else if(input == '0') 
            {
                int[] triangleX = {140, 155, 170}, triangleY = {195, 210, 195};
                // Erasing the old triangle (If there is one)
                if(lastValidColumn != '8')
                {   
                    for(int i = 0; i < triangleY.length; i++) triangleX[i] += (lastValidColumn - '1') * 55;
                    c.setColor(new Color(82, 113, 255));
                    c.fillPolygon(triangleX, triangleY, 3);
                }
                
                triangleX = new int[]{555, 570, 585};
                triangleY = new int[]{55, 70, 55};
                if(playerTurn == 1) 
                    c.setColor(pieceColor1);
                else
                    c.setColor(pieceColor2);
                c.fillPolygon(triangleX, triangleY, 3);
                    
                lastValidColumn = '0';
            }
            else if(input == ' ' && lastValidColumn != '8') // Checking if the user is confirming with their choice
                break;
            else
                JOptionPane.showMessageDialog(null, "Please enter a valid column!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        columnInput = lastValidColumn - '0';
    }
    /*
    public void gameDisplay()
    This method is responsible for taking the user's input and animating a connect four piece falling using a thread. This method is also responsible using black box methods to check whether or not the game has ended.
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    row                         int                     Represents the row that the piece being dropped will land on. This variable is processed using the black box method dropPiece().
    CFP                         ConnectFourPiece        The instance of the ConnectFourPiece class which will animate a connect four piece dropping down, depending on where the user decided to drop their piece.
    longestChain                int[]                   An array containing the "co-ordinates" of the longest consecutive chain of connect 4 pieces currently on the grid. This array is initialized with the black box method checkVictory(), and is used to check if there is a victory or not.
    consecutiveCount            int                     Represents the number of connect four pieces in a row for the current player. Corresponds to to the length of longestChain array / 2, and is used for checking if there is a victory.
    gameWon                     boolean                 Represents whether or not the current player has won. (If there are more than 4 consecutive pieces)
    gameTied                    boolean                 Represents whether or not the game has tied. (If all columns are full)
    gameQuit                    boolean                 Represents whether or not a user has quit. (If the inputted column is 0, corresponding to the button "Press '0' to quit.")
    
    Loops:
    STRUCTURE                                           DESCRIPTION
    for(int column = 0; column < COLUMNS; column++)     Used to iterate through all the columns and check if it is full using one of my black box methods. If all the columns are full, there is most likely a tie game.
    
    Conditionals:
    STRUCTURE                                           DESCRIPTION
    if(columnInput != 0)                                Determines whether or not a connect four piece should be animated, and if grid should be updated. If the user chose to quit the game, DO NOT try and animate a connect four piece falling!
    
    if(playerTurn == 1) 
    else                                                Checks whether or not the animated connect four piece should have the colours of player 1, or of player 2.
    
    if(dropPiece(column, grid) != -1)                   Checks whether or not the current column being iterated through the loop is full or not. If it is not, that means that there are still availible moves and the game has NOT ended in a tie yet.
    
    if(gameWon)
    else if(gameTied)
    else if(gameQuit)                                   Checks for if the game has ended or not. If somebody has won, the victoryPosition array will be set to the longestChain array returned by the black box method, so that in the victory screen, I can display how a player won. If the game has tied, this array will simply be an array containing ONLY the element -1. If someone has quit, the array will be an array with only the element 0.
    
    Other Structures:
    STRUCTURE                                           DESCRIPTION
    try { ... } catch(InterruptedException e) {}        Used to join the animated connect four piece, so that there is a delay before further prompts are done.
    */
    public void gameDisplay()
    {
        if(columnInput != 0)
        {
            int row = dropPiece(columnInput - 1, grid);
            // Placing the piece
            grid[columnInput - 1][row - 1] = playerTurn;
            ConnectFourPiece CFP;
            if(playerTurn == 1)
                CFP = new ConnectFourPiece(c, row, columnInput, pieceColor1, pieceBorder1);     
            else
                CFP = new ConnectFourPiece(c, row, columnInput, pieceColor2, pieceBorder2);     
            CFP.start();
            try
            {
                CFP.join();
            }
            catch(InterruptedException e) {}
        }
        int[] longestChain = checkVictory(playerTurn, grid);
        int consecutiveCount = longestChain.length / 2;
        boolean gameWon = consecutiveCount >= CONSECUTIVE;
        
        boolean gameTied = true;
        for(int column = 0; column < COLUMNS; column++)
            if(dropPiece(column, grid) != -1)
                gameTied = false;
        
        boolean gameQuit = columnInput == 0;
        if(gameWon)
        {
            victoryPosition = longestChain;
            gameFinished = true;
        }
        else if(gameTied)
        {
            victoryPosition = new int[]{-1};
            gameFinished = true;
        }
        else if(gameQuit)
        {
            victoryPosition = new int[]{0};
            gameFinished = true;
        }
    }
    /*
    public void switchTurn()
    This method performs the logic involved in switching a player's turn, as well as displaying a relevant message.
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    usernameDisplay             String                  Stores the name of which player's turn it is switching to.    
    borderDisplay               Color                   Stores the piece color of which player's turn it is switching to.    
    fillDisplay                 Color                   Stores the piece border color of which player's turn it is switching to.  

    Conditionals:
    STRUCTURE                                           DESCRIPTION
    if(playerTurn == 1)
    else                                                Checks whether or not it is currently player 1's turn or player 2's turn. The three local variables, aswell as the playerTurn variable, will be initialized and modified accordingly.
    
    Other Structures:
    STRUCTURE                                           DESCRIPTION
    try { ... } catch(InterruptedException e)           Used to create a delay in between switching turns, and to allow the user to briefly see the message.
    */
    public void switchTurn() 
    {
        // Basic variable logic done upon switching turns.
        String usernameDisplay;
        Color borderDisplay, fillDisplay;
        if(playerTurn == 1)
        {
            playerTurn = 2;
            usernameDisplay = username2;
            borderDisplay = pieceBorder2;
            fillDisplay = pieceColor2;
        }
        else
        {
            playerTurn = 1;
            usernameDisplay = username1;
            borderDisplay = pieceBorder1;
            fillDisplay = pieceColor1;
        }
        // Erasing old prompts
        c.setColor(new Color(82, 113, 255));
        c.fillRect(0, 143, 640, 30);
        c.setFont(new Font("Helvetica", Font.BOLD, 20));
        c.setColor(new Color(55, 55, 55));
        c.drawString("It is now " + usernameDisplay + "'s turn!", 153, 168);
        c.setColor(Color.WHITE);
        c.drawString("It is now " + usernameDisplay + "'s turn!", 150, 165);
        // Creating a delay before the transition to the other player's turn
        try { Thread.sleep(1500); } catch(InterruptedException e) {}
        
        // New Rectangle
        c.setColor(fillDisplay);
        c.fillRect(10, 70, 120, 30);
        
        // New Text
        c.setFont(new Font("Helvetica", Font.BOLD, 14));
        c.setColor(new Color(55, 55, 55));
        c.drawString(usernameDisplay, 21, 91);
        c.setColor(borderDisplay);
        c.drawString(usernameDisplay, 20, 90);
    }
    /*
    public void victoryScreen()
    This method displays a victory message to the players, as well as highlighing how a given player won, if the game ended via a win (And not a tie or a quit)
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    victoryType                 String                  The "Type" of victory (Win, Tie, Quit). Determined using the global array victoryPosition.
    winMessage                  String                  The message that will be displayed to the players regarding the state of the victory.
    pieceColumn                 int                     Used inside of a loop to draw exactly how a player won by displaying where the "4 in a row (Or more than 4)" is. Represents the column of where the current piece that needs to be drawn is, and corresponds to every even index (0, 2, 4, 6...) in the victoryPosition array.                     
    pieceRow                    int                     Used inside of a loop to draw exactly how a player won by displaying where the "4 in a row (Or more than 4)" is. Represents the row of where the current piece that needs to be drawn is, and corresponds to every odd index (1, 3, 5, 7...) in the victoryPosition array.                     
    displayX                    int                     The exact x-coordinate of where the piece highlighting the victory needs to be displayed.
    displayY                    int                     The exact y-coordinate of where the piece highlighting the victory needs to be displayed.
    br                          BufferedReader          The instance of the BufferedReader class responsible for reading the 9 most recent games played. (Not 10, as the 10th one will be deleted to make space for this one!)
    pr                          PrintWriter             The instance of the PrintWriter class responsible for printing out information regarding the current game into the PastGames.txt file.
    output                      String[]                An array storing everything that needs to be outputted, line by line, back into the file. The first line/index of this array will be the current game.
    input                       String                  A temporary variable storing the current element read by the BufferedReader, in the old file. 
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    for(int i = 0; i < victoryPosition.length; i += 2)  Used for iterating the victoryPosition array and displaying how a player won.
    while(input != null && outputIndex < output.length) Used for taking input from the old file and storing at most 9 games into the new output files. If there were 10 games in the old file, the most recent 9 of those would be stores from indecies 1-9, and the current game would be at index 0.
    for(int i = 0; i < output.length; i++)              Used to iterate through the output array and print to a file.
    
    Conditionals:
    STRUCTURE                                           DESCRIPTION
    if(victoryPosition.length / 2 >= CONSECUTIVE)                     
    else                                                Used to determine whether a game ended with someone winning. If the length of this victoryPosition / 2 is greater than or equal to 4, it means that there are 4 consecutive pieces in a row or more, which means that someone won and that there is NO tie or surrender.
    
    if(victoryPosition[0] == 0)                         Used to differentiate between a tie and a quit. A game that was quit will have 0 as its only element.
    
    if(victoryType.equals("WIN"))
    else if(victoryType.equals("QUIT"))
    else                                                Used to generate a message that will be displayed to the user regarding the victory state of the game.
    
    if(playerTurn == 1)                                  
    else                                                Used in conjunction with the above if-structure to generate a message that will be displayed. 
    
    if(victoryType.equals("WIN"))                       This if statement is used twice. Once for determining whether or not I need to display how a given user won, another time for adding the number of consecutive pieces, as a piece of information, to the output. If there is a winner, the number of consecutive pieces in a row will be equal to the length of the array / 2. 
    
    if(output[i] != null)                               Used when printing the current output into the file. Checks whether or not the current element is null or not before printing it.
    
    Other Structures:
    STRUCTURE                                           DESCRIPTION
    try { ... } catch(InterruptedException e)           Used to create a slight delay before the victory screen is shown, so the transition is not too abrupt
    try { ... } catch(IOException e)                    Used to save this game to the file of the most recent games.
    */
    public void victoryScreen()
    {
        try { Thread.sleep(500); } catch(InterruptedException e) {}

        c.setColor(new Color(50, 50, 50, 150));
        c.fillRect(0, 0, 640, 500);
        // Text and Victory prompts
        
        String victoryType = "";
        if(victoryPosition.length > 1)
            victoryType = "WIN";
        else
        {
            if(victoryPosition[0] == 0)
                victoryType = "QUIT";
            else 
                victoryType = "TIE";
        }
        
        String winMessage;
        if(victoryType.equals("WIN"))
        {
            if(playerTurn == 1)
                winMessage = username1 + " has won the game!";
            else
                winMessage = username2 + " has won the game!";
        }
        else if(victoryType.equals("QUIT"))
        {
            if(playerTurn == 1)
                winMessage = username1 + " has quit the game!";
            else
                winMessage = username2 + " has quit the game!";
        }
        else 
            winMessage = "The game has ended in a tie!";
        
        // Border for text
        c.setColor(new Color(55, 55, 55)); // Shadow Color
        c.fillRect(63, 113, 520, 60);
        c.setColor(new Color(75, 103, 232));
        c.fillRect(60, 110, 520, 60);
        // Actual text
        c.setFont(new Font("Helvetica", Font.BOLD, 27));
        c.setColor(new Color(55, 55, 55)); // Shadow Color
        c.drawString(winMessage, 83, 153);
        c.setColor(Color.WHITE);
        c.drawString(winMessage, 80, 150);
        
        // Highlighting victory pieces
        c.setColor(new Color(255, 192, 18, 190)); // Highlight Color
        if(victoryType.equals("WIN")) 
        {
            for(int i = 0; i < victoryPosition.length; i += 2) // Iterating through the array
            {
                int pieceColumn = victoryPosition[i], pieceRow = victoryPosition[i + 1];
                int displayX = pieceColumn * 55 + 155;
                int displayY = 450 - 50 * pieceRow;
                c.fillOval(displayX - 25, displayY - 25, 50, 50);
            }
        }
        // Saving Data
        BufferedReader br;
        PrintWriter pr;
        try
        {
            br = new BufferedReader(new FileReader("PastGames.txt"));
            String[] output = new String[10]; // Saving at most 10 games!
            output[0] = username1 + " " + username2 + " " + victoryType + " " + playerTurn;
            if(victoryType.equals("WIN")) 
                output[0] += " " + victoryPosition.length / 2; // Number of consecutive pieces will be equal to the length / 2, as each 2 elements in the array correspond to a row and column of a piece.
            String input = br.readLine();
            int outputIndex = 1;
            while(input != null && outputIndex < output.length)
            {
                output[outputIndex] = input;
                outputIndex++;
                input = br.readLine();
            }
            pr = new PrintWriter(new FileWriter("PastGames.txt"));
            for(int i = 0; i < output.length; i++)
            {
                if(output[i] != null) // If there is an actual score saved to this index
                    pr.println(output[i]);
            }
            pr.close();
        }
        catch(IOException e) {}
        pauseProgram();
    }
    /*
    private void title()
    Used in almost every method. This method clears the screen, and draws the title/logo at the top of the screen, as well as the connect four piece decorations to the side.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    for(int i = 0; i <= 3; i++)                         Used as a loop to draw the 4 pairs of connect four pieces.
    for(int r = 32; r >= 20; r -= r / 5)                Used to draw the outline and interior darker circles inside of the connect four pieces.
    
    Conditionals:
    STRUCTURE                                           DESCRIPTION
    if(i % 2 == 0)
    else                                                Used to determine whether or not the current connect four piece being drawn should be red or yellow.
    */
    private void title()
    {
        c.setColor(new Color(82, 113, 255));
        c.fillRect(0, 0, 640, 500);
        
        c.setColor(new Color(55, 55, 55)); 
        c.fillRect(143, 23, 360, 120);
        
        c.setColor(new Color(75, 103, 232)); // Title's fill
        c.fillRect(140, 20, 360, 120);
        c.setColor(new Color(120, 205, 255)); // Title's outline
        c.fillRect(140, 20, 30, 120);
        c.fillRect(470, 20, 30, 120);
        c.fillRect(140, 90, 360, 5);

        c.setFont(new Font("Helvetica", Font.BOLD, 24));
        c.setColor(new Color(55, 55, 55));
         c.drawString("Ben Zeng Productions", 193, 128);
        c.drawString("CONNECT", 248, 53);
        c.drawString("4", 378, 53);
        
        // Title text
        c.setColor(Color.WHITE);
        c.drawString("Ben Zeng Productions", 190, 125);
        c.setColor(new Color(242, 203, 94)); // Light yellow
        c.drawString("CONNECT", 245, 50);
        c.setColor(new Color(245, 103, 103)); // Light red
        c.drawString("4", 375, 50);
        
        // Drawing the connect 4 piece decorations
        for(int i = 0; i <= 3; i++)
        {
            c.setColor(new Color(55, 55, 55));
            c.fillOval(42, 252 + i * 50, 40, 40);
            c.fillOval(562, 252 + i * 50, 40, 40);
            if(i % 2 == 0) // Draw an alternating design (Red if even, Yellow if odd)
                c.setColor(new Color(245, 103, 103)); // Light red
            else
                c.setColor(new Color(242, 203, 94)); // Light yellow
                
            c.fillOval(40, 250 + i * 50, 40, 40);
            c.fillOval(560, 250 + i * 50, 40, 40);
            if(i % 2 == 0) // Draw an alternating design (Red if even, Yellow if odd)
                c.setColor(new Color(189, 30, 30)); // Dark red (Outline)
            else
                c.setColor(new Color(247, 174, 2)); // Dark yellow (outline)
                
            for(int r = 32; r >= 20; r -= r / 5)
            {
                c.drawOval(60 - (r / 2), 270 + i * 50 - (r / 2), r, r);
                c.drawOval(580 - (r / 2), 270 + i * 50 - (r / 2), r, r);
            }
        }
    }
    /*
    private void pauseProgram()
    This method temporarily halts the program until the user presses a key, as well as displaying a prompt for the user to press a key to continue.
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    diamondX                    int[]                   The x-coordinates of the diamond bordering the "Press any key" message.
    diamondY                    int[]                   The y-coordinates of the diamond bordering the "Press any key" message.
    */
    private void pauseProgram()
    {
        // Border for text
        int[] diamondX = {633, 553, 473, 553};
        int[] diamondY = {453, 493, 453, 413};
        c.setColor(new Color(55, 55, 55)); // Shadow Color
        c.fillPolygon(diamondX, diamondY, 4);
        c.setColor(new Color(75, 103, 232)); // Fill Color
        for(int i = 0; i < diamondX.length; i++)
        {
            diamondX[i] -= 3;
            diamondY[i] -= 3;
        }
        c.fillPolygon(diamondX, diamondY, 4);
        
        // Text
        c.setFont(new Font("Helvetica", Font.BOLD, 14));
        c.setColor(new Color(55, 55, 55));
        c.drawString("- Press Any Key -", 489, 456);
        c.setColor(Color.WHITE);
        c.drawString("- Press Any Key -", 488, 455);
        c.getChar();
    }
    /*
    private int dropPiece(int pieceX, int[][] grid) 
    This is a black box method that takes in parameters for a given column, a grid, and returns the row that a piece dropped at that column would land on. If that column is full, it will return -1.

    Loops: 
    STRUCTURE                                           DESCRIPTION
    for(int i = 0; i < ROWS; i++)                       Used as an iterator through each row of the array.
    
    Conditionals:
    STRUCTURE                                           DESCRIPTION
    if(grid[pieceX][i] == 0)                            Used inside of the loop, and is used to check for and find the first occurance of a 0. Whatever index this is at + 1 is exactly which row it will be dropped on. (Should be a number from 1 - 6)
    */
    private int dropPiece(int pieceX, int[][] grid) 
    {
        for(int i = 0; i < ROWS; i++)
        {
            if(grid[pieceX][i] == 0)
                return i + 1;
        }
        return -1; // Base case 
    }
    /*
    private int[] checkVictory(int player, int[][] grid)
    This is a black box method that takes in parameters for the current player, the grid, and returns an array with the co-ordinates (column and row) of every piece that is in the longest consecutive chain of pieces for the given player. This information wil be useful in displaying a victory screen, and figuring out whether or not a game has ended.
    The elements in this array should go as follows:
    N * 2 - The x-coordinate/column of the N-th piece in the longest consecutive chain of pieces in a row. (N is a number from 0, to less than the highest number of pieces in a row.) 
    N * 2 + 1 - The y-coordinate/column of the N-th piece in the longest consecutive chain of pieces in a row. (N is a number from 0, to less than the highest number of pieces in a row.) 
    In other words, all the even indecies will correspond to a column/x-position, while the odd ones correspond to a row/y-position.
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    victoryCoordinates          int[]                   Stores just the co-ordinates of the pieces in the longest consecutive chain of pieces. Each even index (0, 2, 4...) will correspond to an column, while each odd one will correspond to a row.
    mostInARow                  int                     Stores the current largest consecutive chain length, while iterating the grid.
    horizontalMovement          int                     Represents the number of units moved horizontally (Change in column) per iteration of the loop used to calculate the number of elements in a row there are. This will vary depending on the direction that the chain of consecutive pieces will go in.
    verticalMovement            int                     Represents the number of units moved vertically (Change in row) per iteration of the loop used to calculate the number of elements in a row there are. This will vary depending on the direction that the chain of consecutive pieces it will go in.
    countConsecutive            int                     A temporary counter to count how many pieces there are in a row at a given position (i, j), when going in a certain direction
    column                      int                     A temporary variable to store the column that the nested for loop is currently on. Will be used for iterating through the grid and finding the number of pieces in a row there are.
    row                         int                     A temporary variable to store the row that the nested for loop is currently on. Will be used for iterating through the grid and finding the number of pieces in a row there are.
    winPositions                int[]                   Temporarily stores the coordinates the pieces in the current consecutive chain of pieces. The victoryCoordinates variable will be set to this one, after this one has been filled in.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    for(int i = 0; i < COLUMNS; i++)                    Used as an iterator for each column in the grid. Used for figuring out the longest number of pieces in a row.
    for(int j = 0; j < ROWS; j++)                       Used as an iterator for each row in the grid. Used for figuring out the longest number of pieces in a row.
    for(int direction = 1; direction <= 4; direction++) Used specifically to shorten code when figuring out how many pieces there are in a row, and represents the direction that the consecutive chain is going in. (Vertical, Horizontal, Diagonal 1, Diagonal 2) 
    while(column < COLUMNS && row < ROWS && column >= 0 && row >= 0) Used to loop through the grid until either the column variable or the row variable is out of bounds. Used specifically when counting the number of pieces in a row
    for(int counter = 0; counter < countConsecutive; counter++) Used to iterate through the winPositions array, and insert co-ordinates into this array.
    
    Conditionals:
    STRUCTURE                                           DESCRIPTION
    if(direction == 1)
    else if(direction == 2)
    else if(direction == 3)
    else                                                Used to initialize the horizontalMovement and the verticalMovement variables, based off of the direction.    
    
    if(grid[i][j] != player)                            Used when finding elements in the grid to check for the number of pieces in a row there are. If the current tile being checked does not belong to the specified player, skip this tile as it is not relevant.
    if(grid[column][row] != player)                     Used directly when counting the number of pieces in a row there are. If the current tile does not belong to this player, exit the loop as the chain has been broken.
    if(countConsecutive > mostInARow)                   Used to check if the current number of consecutive elements is "better" than the one stored. The victoryCoordinates array and the mostInARow variable will be updated accordingly.
    */
    private int[] checkVictory(int player, int[][] grid)
    {
        int[] victoryCoordinates = new int[0]; 
        int mostInARow = 0;
        /* Figuring out number of pieces in a row */
        for(int direction = 1; direction <= 4; direction++)
        {
            int horizontalMovement, verticalMovement;
            if(direction == 1) // Vertical
            {
                verticalMovement = 1;
                horizontalMovement = 0;
            }
            else if(direction == 2) // Horizontal
            {
                verticalMovement = 0;
                horizontalMovement = 1;
            }
            else if(direction == 3) // Diagonal (Down to the right)
            {
                verticalMovement = 1;
                horizontalMovement = 1;
            }
            else // Diagonal (Up to the right)
            {
                verticalMovement = -1;
                horizontalMovement = 1;
            }
            for(int i = 0; i < COLUMNS; i++)
            {
                for(int j = 0; j < ROWS; j++)
                {
                    int countConsecutive = 0;
                    if(grid[i][j] != player) // Only checking victories for the specified player.
                        continue;
                    int column = i, row = j;
                    /* Figuring out how many in a row there are, at the current position */
                    while(column < COLUMNS && row < ROWS && column >= 0 && row >= 0) // Looping until the current position is out of the field
                    {
                        if(grid[column][row] != player) // If the piece belongs to a different player than the one specified, then the consecutive chain has ended.
                            break;
                        countConsecutive++;
                        column += horizontalMovement;
                        row += verticalMovement;
                    }
                    /* Checking if this position has more in a row that the old maximum. Then updating accordingly. */
                    if(countConsecutive > mostInARow) // Found a new best
                    {
                        int[] winPositions = new int[countConsecutive * 2]; 
                        // Resetting column and row
                        column = i;
                        row = j;
                        for(int counter = 0; counter < countConsecutive; counter++) // Filling in the array
                        {
                            winPositions[counter * 2] = column;
                            winPositions[counter * 2 + 1] = row;
                            column += horizontalMovement;
                            row += verticalMovement;
                        }
                        victoryCoordinates = winPositions;
                        mostInARow = countConsecutive;
                    }
                }
            }
        }
        return victoryCoordinates;
    }
    
    /*
    public static void main(String[] args)
    This is the main method of the program, controlling all screen flow to create a fully functional game.
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    CF                          ConnectFour             The instance of the main class that will be responsible for every screen, display, input, etc. within the program.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    while(true)                                         Used twice in the main method. Once that will run forever until the user quits from the main menu, another one that runs until the gameplay is over.
    
    Conditionals:
    STRUCTURE                                           DESCRIPTION
    if(CF.mainChoice == 1)
    else if(CF.mainChoice == 2)
    else if(CF.mainChoice == 3)
    else                                                Used to determine where the user will go next from the main menu.
    
    if(CF.gameFinished)                                 Checks whether or not the current game has finished or not. If it has, the game will end and the victory screen will run.
    */
    public static void main(String[] args)
    {
        ConnectFour CF = new ConnectFour();
        CF.splashScreen();
        while(true)
        {
            CF.mainMenu();
            if(CF.mainChoice == 1) // Gameplay
            {
                CF.gameFinished = false;
                CF.selectGame();
                CF.startGame();
                while(true)
                {
                    CF.gameInput();
                    CF.gameDisplay();
                    if(CF.gameFinished) 
                        break;
                    CF.switchTurn();
                }
                CF.victoryScreen();
            }
            else if(CF.mainChoice == 2)
                CF.instructions();
            else if(CF.mainChoice == 3)
                CF.pastGames();
            else
                break;
        }
        CF.goodbye();
    }
}














