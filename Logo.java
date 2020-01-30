/*
Ben Zeng
Ms.Krasteva
2019-01-08
Logo
This file is the Logo thread, which animates the logo/title that flies up, from the bottom of the screen, during the Splash Screen.

Instance Variables:
NAME                        TYPE                    DESCRIPTION
c                           Console                 The instance of the Console class where all information will be displayed on.
xPosition                   int                     The x-position of the center of the logo.
yPosition                   int                     The y-position of the center of the logo.
*/

import hsa.Console;
import java.awt.*;

public class Logo extends Thread
{
    private Console c;
    private int xPosition, yPosition;
    /*
    public Logo(Console con)
    This is the default constructor for the Logo thread. This is also where the Console instance is set to the parameter variable.
    */
    public Logo(Console con)
    {
        c = con;
    }
    /*
    public void drawLogo()
    This method draws the logo design at the current x and y position.
    */
    public void drawLogo()
    {
        // Logo
        c.setColor(new Color(55, 55, 55)); // Title Shadow
        c.fillRect(xPosition - 177, yPosition - 57, 360, 120);
        
        c.setColor(new Color(75, 103, 232)); // Title's fill
        c.fillRect(xPosition - 180, yPosition - 60, 360, 120);
        c.setColor(new Color(120, 205, 255)); // Title's outline
        c.fillRect(xPosition - 180, yPosition - 60, 30, 120);
        c.fillRect(xPosition + 150, yPosition - 60, 30, 120);
        c.fillRect(xPosition - 180, yPosition + 10, 360, 5);

        c.setFont(new Font("Helvetica", Font.BOLD, 24));
        c.setColor(new Color(55, 55, 55));
        c.drawString("Ben Zeng Productions", xPosition - 127, yPosition + 48);
        c.drawString("CONNECT", xPosition - 72, yPosition - 27);
        c.drawString("4", xPosition + 58, yPosition - 27); 

        // Title text
        c.setColor(Color.WHITE);
        c.drawString("Ben Zeng Productions", xPosition - 130, yPosition + 45);
        c.setColor(new Color(242, 203, 94)); // Light yellow
        c.drawString("CONNECT", xPosition - 75, yPosition - 30);
        c.setColor(new Color(245, 103, 103)); // Light red
        c.drawString("4", xPosition + 55, yPosition - 30);
    }
    /*
    public void eraseLogo()
    This method erases the logo design at the current x and y position.
    */
    public void eraseLogo()
    {
        c.setColor(new Color(82, 113, 255));
        c.fillRect(xPosition - 185, yPosition - 65, 370, 130);
    }
    /*
    public void run()
    This method controls the main part of the animation, including the logic behind where the logo will move to.
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    timer                       int                     The number of loop iterations elapsed. This variable will constantly be incrementing per 16 milliseconds.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    while(yPosition != 80)                              Keeps running the loop until the logo hits its target at yPosition = 80.

    Other Structures:
    STRUCTURE                                           DESCRIPTION
    synchronized(c)                                     Synchronizes the console.
    */
    public void run()
    {
        int timer = 0;
        while(yPosition != 80)
        {
            synchronized(c)
            {
                eraseLogo();
                // Modifying position
                yPosition = 620 - timer * 3;
                xPosition = 320 + (int) Math.round(Math.sin(Math.PI * timer / 60.0) * 40); // Using a sine wave for the X-position
                timer++;
                drawLogo();
            }
            delayFrame();
        }
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
