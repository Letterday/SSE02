/*
Copyright (c) 2012, Ulrik Pagh Schultz, University of Southern Denmark
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the University of Southern Denmark.
*/

package examples.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JTextField;

import examples.communication.Driver;
import quickqui.QuickGUI;
import shadowrundomain.Roller;
import shadowrundomain.succes;

/**
 * Extension of the robot driver server to display information in GUI and make
 * control of bumper sensor available as a user interface
 * @author ups
 */
public class ShadowGUI extends Driver implements ActionListener {
    
    /**
     * The specific GUI to use for this application
     * @author ups
     */
    public static class ControlGUI extends QuickGUI.GUIModel {
        
        @Override 
        public void build() {
            frame("Shadowrun",Layout.VERTICAL,
                
                 
                  button(name("roll"),text("roll")),
                  textfield(name("dice"),text("number of dice")),
                  checkbox(name("edge"),text("edge")),
                  label(name("succes"),text("succes=number")),
                  label(name("glits"),text("glits=false")),
                  label(name("error"),text("nothing to report"))
                )
                
              
            ;
        }
    }
    /**
     * Create GUI and then activate robot server functionality
     */
    public static void main(String argv[]) throws IOException {
    	ShadowGUI driver = new ShadowGUI();
        driver.gui = new QuickGUI(new ControlGUI(),driver);
       
    }

    /**
     * Bumper sensor memory: contains last value of bumper sensor triggered, "NONE" otherwise
     */
    private boolean edge = false;
    
    /**
     * The GUI 
     */
    private QuickGUI gui;
    private Roller roller=new Roller();
    /**
     * Respond to GUI events
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getActionCommand().equals("roll")) roll();
        else if(event.getActionCommand().equals("edge")) edge();
        
        else System.err.println("Warning: unknown event "+event);
    }

    
    protected void roll() {
    	JTextField dice =(JTextField) gui.getComponent("dice");
    	try{
    	succes result = roller.makeroll(Integer.parseInt(dice.getText()),edge);

    	JLabel succes = (JLabel)gui.getComponent("succes");
        JLabel glits = (JLabel)gui.getComponent("glits");
        succes.setText("succes="+result.getSucces());
        glits.setText("glits="+result.isGlits());
    	} catch (NumberFormatException e) {
    	JLabel error = (JLabel)gui.getComponent("error");
    	error.setText("Thats not a int");
    	}
    	
    }
    
    protected void edge() {
    	if (edge){
    		edge=false;
    	}
    	else
    		edge=true;

    }
    
    
}
