
package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;
import javax.swing.JLabel;
import java.util.Timer;

public class Arrow extends JLabel{
    
    TimerTask timerTaskAnimation;
    TimerTask timerTaskMovement;
    String typeArrow="";
    String direction="";
    int animationFrame=1;
    int counterPosition=0;
    int speed=4;
    
    int xPositionL=10;
    int xPositionD=70;
    int xPositionU=140;
    int xPositionR=200;
    
    public Arrow(){
        
    }
    
    public Arrow(String typeArrow, String direction, int counterPosition){
        
        this.typeArrow=typeArrow;
        this.direction=direction;
        this.counterPosition=counterPosition;
        
        setText("");
        setSize(60,60);
        
        timerTaskAnimation = new TimerTask() 
	     { 
	         public void run()  
	         { 
                    try {
                        setIcon(new javax.swing.ImageIcon(getClass().getResource("/arrowSprites/arrow"+typeArrow+"-"+direction+"-"+animationFrame+".png")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                        animationFrame++;
                        animationFrame = animationFrame%5;
                        if (animationFrame==0){
                            animationFrame=1;
                        }
	         } 
	     }; 
 
        Timer timer = new Timer(); 
        timer.scheduleAtFixedRate(timerTaskAnimation, 0, 200);
        
        timerTaskMovement = new TimerTask() 
	     { 
	         int innerCounter=counterPosition;
                 
                 public void run()  
	         { 
                    setBounds(getXBound(direction),innerCounter,60,60);
                    innerCounter--;
                    if (innerCounter<=0){
                        innerCounter=0;
                    }
                    
                    if (getBounds().getY()<10){
                        destroy();
                    }
                    
                    //System.out.println(getBounds().getY());
	         } 
	     }; 
 
        Timer timer2 = new Timer(); 
        timer2.scheduleAtFixedRate(timerTaskMovement, 0, speed);
    }
    
    public int getXBound(String str){
        int result=10;
        if (str.equalsIgnoreCase("L")){
            result=xPositionL;
        }else if(str.equalsIgnoreCase("D")){
            result=xPositionD;
        }else if(str.equalsIgnoreCase("U")){
            result=xPositionU;
        }else if(str.equalsIgnoreCase("R")){
            result=xPositionR;
        }else{
            result=xPositionL;
        }
        return result;
    }
    
    public void destroy(){
        System.out.println("Destroy after");
        timerTaskAnimation.cancel();
        timerTaskMovement.cancel();
        setIcon(null);
    }
    
    public void destroy2(){
        System.out.println("Destroy on time");
        timerTaskAnimation.cancel();
        timerTaskMovement.cancel();
        setIcon(null);
    }
    
    public String getDirection(){
        return direction;
    }
    
}
