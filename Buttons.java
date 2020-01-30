/*
Ben Zeng
Ms.Krasteva
2019-01-08
Logo
This file is the Buttons thread, which animates the buttons that will fade in, during the Splash Screen.

Instance Variables:
NAME                        TYPE                    DESCRIPTION
c                           Console                 The instance of the Console class where all information will be displayed on.
transparency                int                     The current transparency/alpha value of the buttons.
*/

import hsa.Console;
import java.awt.*;

public class Buttons extends Thread
{
    private Console c;
    private int transparency;
    /*
    public Buttons(Console con)
    This is the default constructor for the Buttons thread. This is also where the Console instance is set to the parameter variable.
    */
    public Buttons(Console con)
    {
        c = con;
        transparency = 0;
    }
    /*
    public void drawTranslucentButtons()
    This method draws the buttons at the given transparency level, by first drawing the opaque buttons and then overlaying a translucent background-coloured rectangle to emulate transparency with less stuttering.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    for(int i = 0; i <= 240; i += 80)                   Used twice, for drawing the 4 buttons, and the shadow of the buttons.
    */
    public void drawTranslucentButtons()
    {
        // Erasing old buttons
        c.setColor(new Color(82, 113, 255));
        c.fillRect(200, 150, 240, 350);
        
        // Buttons
        c.setColor(new Color(55, 55, 55)); // Shadow
        for(int i = 0; i <= 240; i += 80)
            c.fillRoundRect(203, 153 + i, 240, 70, 20, 20);
        c.setColor(new Color(84, 90, 255)); // Button Color
        for(int i = 0; i <= 240; i += 80)
            c.fillRoundRect(200, 150 + i, 240, 70, 20, 20);
        
        // Drawing a translucent background-coloured rectangle over the buttons to emulate transparency
        c.setColor(new Color(82, 113, 255, 255 - transparency));
        c.fillRect(200, 150, 250, 350);
    }
    /*
    public void run()
    This method controls the main part of the animation, including the logic behind how the buttons will fade in.
    
    Variables:
    NAME                        TYPE                    DESCRIPTION
    timer                       int                     The number of loop iterations elapsed. This variable will constantly be incrementing per 16 milliseconds.
    
    Loops: 
    STRUCTURE                                           DESCRIPTION
    while(transparency <= 255)                          Keeps running until the buttons have fully faded in.

    Other Structures:
    STRUCTURE                                           DESCRIPTION
    synchronized(c)                                     Synchronizes the console.
    */
    public void run()
    {
        int timer = 0;
        while(transparency <= 255)
        {
            synchronized(c)
            {
                drawTranslucentButtons();
                transparency += timer; // Increasing in a non-linear rate that accelerates
                timer++;
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