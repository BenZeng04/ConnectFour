/*
Ben Zeng
Ms.Krasteva
2019-01-08
ConnectFourPiece
This file is the ConnectFourPiece thread, which animates the connect four piece that bounces down, either in the splash screen, or in the actual game. 

Instance Variables:
NAME                        TYPE                    DESCRIPTION
c                           Console                 The instance of the Console class where all information will be displayed on.
xPosition                   int                     The x-position of the center of the piece.
yPosition                   int                     The y-position of the center of the piece.
yDestination                int                     The position where the piece will eventually stop at.
pieceColor                  Color                   The fill colour of the piece.
pieceBorder                 Color                   The darker outline and detail colour of the piece.
*/


import hsa.Console;
import java.awt.*;

public class ConnectFourPiece extends Thread
{
    private Console c;
    private int xPosition, yPosition, yDestination;
    private Color pieceColor, pieceBorder;
    /*
    public ConnectFourPiece(Console con, int row, int column, Color fill, Color border)
    This is the constructor used when animating ingame. Here, the x and y position is accepted based on row and column, rather than specific coordinates
    */
    public ConnectFourPiece(Console con, int row, int column, Color fill, Color border) // In-game
    {
        c = con;
        yDestination = 500 - 50 * row;
        yPosition = 200;
        xPosition = column * 55 + 100;
        pieceColor = fill;
        pieceBorder = border;
    }
    /*
    public ConnectFourPiece(Console con, int x, int y, boolean isRed)
    This is the constructor used when animating in the splash screen. Here, since the piece can only either be yellow or red, the colour is represented as a single boolean value.
    */
    public ConnectFourPiece(Console con, int x, int y, boolean isRed) // Splash Screen
    {
        c = con;
        xPosition = x;
        yPosition = -50;
        yDestination = y;
        if(isRed)
        {
            pieceColor = new Color(245, 103, 103);
            pieceBorder = new Color(189, 30, 30);
        }
        else
        {
            pieceColor = new Color(242, 203, 94);
            pieceBorder = new Color(247, 174, 2);
        }
    }
    /*
    public void drawPiece()
    This method draws the connect four piece at the current x and y position.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    for(int r = 32; r >= 20; r -= r / 5)                Used to draw the outline and interior darker circles inside of the connect four pieces.
    */
    public void drawPiece()
    {
        c.setColor(new Color(55, 55, 55)); // Shadow Color
        c.fillOval(xPosition - 18, yPosition - 18, 40, 40);
        c.setColor(pieceColor);
        c.fillOval(xPosition - 20, yPosition - 20, 40, 40);
        
        c.setColor(pieceBorder);
        for(int r = 32; r >= 20; r -= r / 5)
            c.drawOval(xPosition - (r / 2), yPosition - (r / 2), r, r);
    }
    /*
    public void erasePiece()
    This method erases the connect four piece at the current x and y position.
    */
    public void erasePiece()
    {
        c.setColor(new Color(82, 113, 255)); // Background Color
        c.fillRect(xPosition - 21, yPosition - 25, 46, 50);
    }
    /*
    public void run()
    This method controls the main part of the animation, including the logic behind where the connect four piece will bounce to.
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    velocity                    int                     The number of pixels that the piece will move down, every frame.                  
    bounceStrength              double                  How much the velocity will reduce by when the piece bounces.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    do
    while(velocity != 0 || yPosition != yDestination)   Keeps running the loop until the connect four piece stops bouncing and has hit its destination.

    Conditionals:
    STRUCTURE                                           DESCRIPTION
    if(yPosition >= yDestination)                       Checks if the connect 4 piece has hit its target. It will then reverse direction and start to bounce.
    
    Other Structures:
    STRUCTURE                                           DESCRIPTION
    synchronized(c)                                     Synchronizes the console.
    */
    public void run()
    {
        int velocity = 0;
        double bounceStrength = 0.6;
        do
        {
            synchronized(c)
            {
                erasePiece();
                yPosition += velocity;
                velocity++;
                if(yPosition >= yDestination)
                {
                    yPosition = yDestination;
                    velocity = (int) (velocity * -bounceStrength);
                    bounceStrength -= 0.2;
                }
                drawPiece();
            }
            delayFrame();
        }
        while(velocity != 0 || yPosition != yDestination);
    }
    /*
    private void delayFrame()
    This method is simply a shortcut for pausing the program by 16 milliseconds. (1 frame)

    Other Structures:
    STRUCTURE                                           DESCRIPTION
    try { ... } catch(InterruptedException e)           Used for halting the program with Thread.sleep().
    */
    private void delayFrame()
    {
        try
        {
            Thread.sleep(16);
        }
        catch(InterruptedException e) {}
    }
}


