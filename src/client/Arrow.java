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
    int counterPosition=300;
    int speed=15;
    
    public Arrow(){
        
    }
    
    public Arrow(String typeArrow, String direction){
        
        this.typeArrow=typeArrow;
        this.direction=direction;
        
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
	         public void run()  
	         { 
                    setBounds(70,counterPosition,60,60);
                    counterPosition--;
                    if (counterPosition<=0){
                        counterPosition=0;
                    }
                    
                    if (getBounds().getY()<10){
                        destroy();
                    }
                    
                    //System.out.println(getBounds().getY());
	         } 
	     }; 
 
        Timer timer2 = new Timer(); 
        timer2.scheduleAtFixedRate(timerTaskMovement, 0, 5);
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
    
    
}
