package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;
import javax.swing.JLabel;
import java.util.Timer;

public class Arrow extends JLabel {

    TimerTask timerTaskAnimation;
    TimerTask timerTaskMovement;
    String typeArrow = "";
    String direction = "";
    int playerIndex;

    int animationFrame = 1;
    int counterPosition = 0;
    int speed = 4;

    int xPositionLPlayer1 = 10;
    int xPositionDPlayer1 = 70;
    int xPositionUPlayer1 = 140;
    int xPositionRPlayer1 = 200;

    int rightShift = 510;

    int xPositionLPlayer2 = rightShift;
    int xPositionDPlayer2 = rightShift + 70;
    int xPositionUPlayer2 = rightShift + 140;
    int xPositionRPlayer2 = rightShift + 200;

    public Arrow() {

    }

    public Arrow(String typeArrow, String direction, int counterPosition, int playerIndex) {

        this.typeArrow = typeArrow;
        this.direction = direction;
        this.counterPosition = counterPosition;
        this.playerIndex = playerIndex;

        setText("");
        setSize(60, 60);

        timerTaskAnimation = new TimerTask() {
            public void run() {
                try {
                    setIcon(new javax.swing.ImageIcon(getClass().getResource("/arrowSprites/arrow" + typeArrow + "-" + direction + "-" + animationFrame + ".png")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                animationFrame++;
                animationFrame = animationFrame % 5;
                if (animationFrame == 0) {
                    animationFrame = 1;
                }
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTaskAnimation, 0, 200);

        timerTaskMovement = new TimerTask() {
            int innerCounter = counterPosition;

            public void run() {
                setBounds(getXBound(direction, playerIndex), innerCounter, 60, 60);
                innerCounter--;
                if (innerCounter <= 0) {
                    innerCounter = 0;
                }

                if (getBounds().getY() < 10) {
                    destroy();
                }

                //System.out.println(getBounds().getY());
            }
        };

        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(timerTaskMovement, 0, speed);
    }

    public int getXBound(String str, int playerIndex) {
        int result = 10;

        if (playerIndex == 1) {
            if (str.equalsIgnoreCase("L")) {
                result = xPositionLPlayer1;
            } else if (str.equalsIgnoreCase("D")) {
                result = xPositionDPlayer1;
            } else if (str.equalsIgnoreCase("U")) {
                result = xPositionUPlayer1;
            } else if (str.equalsIgnoreCase("R")) {
                result = xPositionRPlayer1;
            } else {
                result = xPositionLPlayer1;
            }
        } else if (playerIndex == 2) {
            if (str.equalsIgnoreCase("L")) {
                result = xPositionLPlayer2;
            } else if (str.equalsIgnoreCase("D")) {
                result = xPositionDPlayer2;
            } else if (str.equalsIgnoreCase("U")) {
                result = xPositionUPlayer2;
            } else if (str.equalsIgnoreCase("R")) {
                result = xPositionRPlayer2;
            } else {
                result = xPositionLPlayer2;
            }
        }

        return result;
    }

    public void destroy() {
        System.out.println("Destroy after");
        timerTaskAnimation.cancel();
        timerTaskMovement.cancel();
        setIcon(null);
    }

    public void destroy2() {
        System.out.println("Destroy on time");
        timerTaskAnimation.cancel();
        timerTaskMovement.cancel();
        setIcon(null);
    }

    public String getDirection() {
        return direction;
    }

}
