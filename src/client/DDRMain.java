/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import sun.misc.BASE64Decoder;

/**
 *
 * @author liwuen
 */
public class DDRMain extends javax.swing.JFrame {

    /**
     * Creates new form DDRMain
     */
    public static ArrayList<Arrow> listOfArrowsPlayer1 = new ArrayList<Arrow>();
    public static ArrayList<Arrow> listOfArrowsPlayer2 = new ArrayList<Arrow>();

    public ArrayList<Integer> listOfArrowIntegers = new ArrayList<>();
    public ArrayList<Integer> listOfArrowIntegersCopy = new ArrayList<>();
    public ArrayList<BufferedImage> listOfImageBackgrounds = new ArrayList<>();
    public int initialPosition = 500;
    public int gapBetweenArrows = 100;

    public static Clip clip;
    public boolean connected;
    public boolean statusLabelConnected = false;
    public boolean statusLabelAdversary = false;

    public int waitingForAdversary = 0;

    public Client client;
    public static int defaultPort = 1500;
    public static String defaultHost = "localhost";

    public static int backgroundIndex = 0;

    public int player1L = KeyEvent.VK_LEFT;
    public int player1D = KeyEvent.VK_DOWN;
    public int player1U = KeyEvent.VK_UP;
    public int player1R = KeyEvent.VK_RIGHT;

    public int player2L = KeyEvent.VK_A;
    public int player2D = KeyEvent.VK_S;
    public int player2U = KeyEvent.VK_W;
    public int player2R = KeyEvent.VK_D;

    public int playerIndex = 0;
    public int numberOfGroups;

    public int indexOfCurrentGroup = 0;

    //Animation variables
    public int numberOfArrowsPerGroup = 8;
    public int indexOfArrowGroup = 1;
    int speed = 4;

    int xPositionLPlayer1 = 10;
    int xPositionDPlayer1 = 70;
    int xPositionUPlayer1 = 140;
    int xPositionRPlayer1 = 200;

    /*int rightShift = 530;

    int xPositionLPlayer2 = rightShift;
    int xPositionDPlayer2 = rightShift + 80;
    int xPositionUPlayer2 = rightShift + 150;
    int xPositionRPlayer2 = rightShift + 210;*/
    int animationFrame = 1;
    int animationFrameIndex = 0;

    //Font for messages
    Font bigFont = new Font("Helvetica", Font.BOLD, 30);
    Font bigFont2 = new Font("Helvetica", Font.BOLD, 30);
    Font bigFont3 = new Font("Helvetica", Font.BOLD, 30);
    Font bigFont4 = new Font("Helvetica", Font.BOLD, 30);

    public int comboPlayer1 = 0;
    public int comboPlayer2 = 0;
    public int animationComboPlayer1 = 0;
    public int animationComboPlayer2 = 0;
    public int memoryMessageP1 = 0;
    public int memoryMessageP2 = 0;

    //dancer
    public int dancerFrame = 0;
    public int dancerCounter = 0;
    public int dancerSpeed = 10;

    public int headerCounter = 0;

    //variablesFromServer
    public String wordToLabel = " ";
    public int numberOfPictures = 0;

    public int counterSong=0;
    
    public ArrayList<Integer> pointsPlayer1 = new ArrayList<Integer>();
    public ArrayList<Integer> pointsPlayer2 = new ArrayList<Integer>();
    
    public DDRMain() {
        initComponents();

        jPanelBackground1.setFocusable(true);
        jPanelBackground1.requestFocusInWindow();

        statusOfConnection();
        initializeLabels();
    }

    public void initializeLabels() {
        jLabelMessageP1.setText("PERFECT!!");
        jLabelMessageP1.setHorizontalAlignment(JLabel.CENTER);
        jLabelMessageP1.setFont(bigFont);
        jLabelMessageP1.setOutlineColor(Color.black);
        jLabelMessageP1.setEffectIndex(JLabel2D.EFFECT_GRADIENT);
        GradientPaint gp = new GradientPaint(0, 0, Color.green, 100, 50, Color.yellow, true);
        jLabelMessageP1.setGradient(gp);

        jLabelMessageP2.setText("PERFECT!!");
        jLabelMessageP2.setHorizontalAlignment(JLabel.CENTER);
        jLabelMessageP2.setFont(bigFont);
        jLabelMessageP2.setOutlineColor(Color.black);
        jLabelMessageP2.setEffectIndex(JLabel2D.EFFECT_GRADIENT);
        GradientPaint gp2 = new GradientPaint(0, 0, Color.green, 100, 50, Color.yellow, true);
        jLabelMessageP2.setGradient(gp2);

        jLabelComboP1.setText("COMBO");
        jLabelComboP1.setHorizontalAlignment(JLabel.LEFT);
        jLabelComboP1.setFont(bigFont);
        jLabelComboP1.setOutlineColor(Color.black);
        jLabelComboP1.setEffectIndex(JLabel2D.EFFECT_GRADIENT);
        GradientPaint gp3 = new GradientPaint(0, 0, Color.green, 100, 50, Color.green, true);
        jLabelComboP1.setGradient(gp3);

        jLabelComboP2.setText("COMBO");
        jLabelComboP2.setHorizontalAlignment(JLabel.LEFT);
        jLabelComboP2.setFont(bigFont);
        jLabelComboP2.setOutlineColor(Color.black);
        jLabelComboP2.setEffectIndex(JLabel2D.EFFECT_GRADIENT);
        GradientPaint gp4 = new GradientPaint(0, 0, Color.green, 100, 50, Color.green, true);
        jLabelComboP2.setGradient(gp4);

        jLabelComboCounterP1.setText("22");
        jLabelComboCounterP1.setHorizontalAlignment(JLabel.RIGHT);
        jLabelComboCounterP1.setFont(bigFont2);
        jLabelComboCounterP1.setOutlineColor(Color.black);
        jLabelComboCounterP1.setEffectIndex(JLabel2D.EFFECT_GRADIENT);
        GradientPaint gp5 = new GradientPaint(0, 0, Color.blue, 100, 50, Color.red, true);
        jLabelComboCounterP1.setGradient(gp5);

        jLabelComboCounterP2.setText("22");
        jLabelComboCounterP2.setHorizontalAlignment(JLabel.RIGHT);
        jLabelComboCounterP2.setFont(bigFont2);
        jLabelComboCounterP2.setOutlineColor(Color.black);
        jLabelComboCounterP2.setEffectIndex(JLabel2D.EFFECT_GRADIENT);
        GradientPaint gp6 = new GradientPaint(0, 0, Color.pink, 100, 50, Color.red, true);
        jLabelComboCounterP2.setGradient(gp6);

        jLabelMessageP1.setText(" ");
        jLabelMessageP2.setText(" ");
        jLabelComboP1.setText(" ");
        jLabelComboP2.setText(" ");
        jLabelComboCounterP1.setText(" ");
        jLabelComboCounterP2.setText(" ");

        jLabelYourWordIs.setText("YOUR WORD IS:");
        jLabelYourWordIs.setHorizontalAlignment(JLabel.CENTER);
        jLabelYourWordIs.setFont(bigFont);
        jLabelYourWordIs.setOutlineColor(Color.black);
        jLabelYourWordIs.setEffectIndex(JLabel2D.EFFECT_GRADIENT);
        GradientPaint gp7 = new GradientPaint(0, 0, Color.green, 100, 50, Color.yellow, true);
        jLabelYourWordIs.setGradient(gp7);

        jLabelWordToLabel.setText(" ");
        jLabelWordToLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabelWordToLabel.setFont(bigFont);
        jLabelWordToLabel.setOutlineColor(Color.black);
        jLabelWordToLabel.setEffectIndex(JLabel2D.EFFECT_GRADIENT);
        GradientPaint gp8 = new GradientPaint(0, 0, Color.blue, 100, 50, Color.yellow, true);
        jLabelWordToLabel.setGradient(gp8);
        
        
        
        jLabelResultP1.setText(" ");
        jLabelResultP1.setHorizontalAlignment(JLabel.CENTER);
        jLabelResultP1.setFont(bigFont);
        jLabelResultP1.setOutlineColor(Color.black);
        jLabelResultP1.setEffectIndex(JLabel2D.EFFECT_GRADIENT);
        GradientPaint gp9 = new GradientPaint(0, 0, Color.green, 100, 50, Color.yellow, true);
        jLabelResultP1.setGradient(gp9);
        
        jLabelResultP2.setText(" ");
        jLabelResultP2.setHorizontalAlignment(JLabel.CENTER);
        jLabelResultP2.setFont(bigFont);
        jLabelResultP2.setOutlineColor(Color.black);
        jLabelResultP2.setEffectIndex(JLabel2D.EFFECT_GRADIENT);
        GradientPaint gp10 = new GradientPaint(0, 0, Color.green, 100, 50, Color.yellow, true);
        jLabelResultP2.setGradient(gp10);
    }

    public void statusOfConnection() {
        Timer timer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                //Change color status
                if (connected) {
                    //Is connected to server
                    if (statusLabelConnected) {
                        statusLabelConnected = false;
                        jLabelServerStatus.setBackground(Color.GREEN);
                    } else {
                        statusLabelConnected = true;
                        jLabelServerStatus.setBackground(Color.WHITE);
                    }
                } else {
                    //player got disconnected
                    jLabelServerStatus.setBackground(Color.red);
                }

                if (waitingForAdversary == 0) {
                    jLabelAdversaryStatus.setBackground(Color.red);
                } else if (waitingForAdversary == 1) {
                    if (statusLabelConnected) {
                        jLabelAdversaryStatus.setBackground(Color.WHITE);
                    } else {
                        jLabelAdversaryStatus.setBackground(Color.YELLOW);
                    }
                } else if (waitingForAdversary == 2) {
                    if (statusLabelConnected) {
                        jLabelAdversaryStatus.setBackground(Color.WHITE);
                    } else {
                        jLabelAdversaryStatus.setBackground(Color.GREEN);
                    }
                }

            }
        };

        timer.schedule(myTask, 500, 500);
    }

    public void createArrows() {

        System.out.println("Number of groups:" + numberOfGroups);
        System.out.println("Number of arrows in listOfArrowIntegersCopy:" + listOfArrowIntegersCopy.size());
        listOfArrowsPlayer1.clear();
        listOfArrowsPlayer2.clear();

        TimerTask timerTaskMovement;
        timerTaskMovement = new TimerTask() {
            //int innerCounter = initialPosition + gapBetweenArrows * numberOfArrowsPerGroup;

            public void run() {

                if (indexOfCurrentGroup >= numberOfGroups) {
                    //TODO: Cancel task
                } else if ((listOfArrowsPlayer1.size() == 0) && (listOfArrowsPlayer2.size()) == 0) { //create arrows

                    //Change background
                    backgroundIndex++;
                    backgroundIndex = backgroundIndex % listOfImageBackgrounds.size();
                    jPanelBackground1.setBackground(listOfImageBackgrounds.get(backgroundIndex));
                    //done changing background

                    listOfArrowsPlayer1.clear();
                    listOfArrowsPlayer2.clear();

                    indexOfCurrentGroup++;
                    //innerCounter = initialPosition;
                    for (int j = 0; j < numberOfArrowsPerGroup; j++) {
                        try {
                            if (listOfArrowIntegersCopy.get(0) > 0) {
                                Arrow arrow1 = new Arrow("A", convertIntegerToTypeOfArrow(listOfArrowIntegersCopy.get(0)), initialPosition + gapBetweenArrows * j, 1);
                                listOfArrowsPlayer1.add(arrow1);
                                jPanelBackground1.add(arrow1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Reached end of arrow list player 1");
                        }
                        try {
                            if (listOfArrowIntegersCopy.get(0) > 0) {
                                Arrow arrow2 = new Arrow("B", convertIntegerToTypeOfArrow(listOfArrowIntegersCopy.get(0)), initialPosition + gapBetweenArrows * j, 2);
                                listOfArrowsPlayer2.add(arrow2);
                                jPanelBackground1.add(arrow2);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Reached end of arrow list");
                        }
                        try {
                            listOfArrowIntegersCopy.remove(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Removed all arrows from group.");
                        }
                    }
                } else {
                    for (int j = 0; j < listOfArrowsPlayer1.size(); j++) {
                        try {
                            listOfArrowsPlayer1.get(j).setBounds((int) (listOfArrowsPlayer1.get(j).getBounds().getX()), (int) (listOfArrowsPlayer1.get(j).getBounds().getY()) - 1, 60, 60);
                        } catch (Exception e) {
                            System.out.println("Reached moving end of arrow list");
                            e.printStackTrace();
                        }
                    }
                    for (int j = 0; j < listOfArrowsPlayer2.size(); j++) {
                        try {
                            listOfArrowsPlayer2.get(j).setBounds((int) (listOfArrowsPlayer2.get(j).getBounds().getX()), (int) (listOfArrowsPlayer2.get(j).getBounds().getY()) - 1, 60, 60);
                        } catch (Exception e) {
                            System.out.println("Reached moving end of arrow list");
                            e.printStackTrace();
                        }
                    }

                    for (int j = 0; j < listOfArrowsPlayer1.size(); j++) {
                        try {
                            //System.out.println("j:" + listOfArrowsPlayer1.get(j).getBounds().getY());
                            if (listOfArrowsPlayer1.get(j).getBounds().getY() < 10) {
                                listOfArrowsPlayer1.get(j).destroy();
                                comboPlayer1 = 0;
                                j--;
                            }
                        } catch (Exception e) {
                            System.out.println("Reached moving end of arrow list2");
                            e.printStackTrace();
                        }
                    }
                    for (int j = 0; j < listOfArrowsPlayer2.size(); j++) {
                        try {
                            if (listOfArrowsPlayer2.get(j).getBounds().getY() < 10) {
                                listOfArrowsPlayer2.get(j).destroy();
                                comboPlayer2 = 0;
                                j--;
                            }
                        } catch (Exception e) {
                            System.out.println("Reached moving end of arrow list2");
                            e.printStackTrace();
                        }
                    }

                    animationFrame++;
                    if (animationFrame > 50) {
                        animationFrame = 1;
                        animationFrameIndex++;
                        animationFrameIndex = animationFrameIndex % 5;
                        for (int i = 0; i < listOfArrowsPlayer1.size(); i++) {
                            try {
                                listOfArrowsPlayer1.get(i).setIcon(new javax.swing.ImageIcon(getClass().getResource("/arrowSprites/arrow" + listOfArrowsPlayer1.get(i).typeArrow + "-" + listOfArrowsPlayer1.get(i).direction + "-" + animationFrameIndex + ".png")));
                            } catch (Exception e) {
                                System.out.println("Error loading animaiton of arrow player 1");
                            }

                        }
                        for (int i = 0; i < listOfArrowsPlayer2.size(); i++) {
                            try {
                                listOfArrowsPlayer2.get(i).setIcon(new javax.swing.ImageIcon(getClass().getResource("/arrowSprites/arrow" + listOfArrowsPlayer2.get(i).typeArrow + "-" + listOfArrowsPlayer2.get(i).direction + "-" + animationFrameIndex + ".png")));
                            } catch (Exception e) {
                                System.out.println("Error loading animaiton of arrow player 1");
                            }

                        }
                    }

                    //Count combo
                    if (comboPlayer1 <= 4) {
                        /*jLabelComboCounterP1.setVisible(false);
                        jLabelComboP1.setVisible(false);*/
                        jLabelComboCounterP1.setText(" ");
                        jLabelComboP1.setText(" ");
                    } else {
                        jLabelComboCounterP1.setText(String.valueOf(comboPlayer1));
                        //jLabelComboCounterP1.setVisible(true);
                        //jLabelComboP1.setVisible(true);
                        jLabelComboP1.setText("COMBO");
                        if (animationComboPlayer1 > 0) {
                            animationComboPlayer1++;
                            animationComboPlayer1 = animationComboPlayer1 % 50;
                            jLabelComboP1.setFont(bigFont3);
                            jLabelComboCounterP1.setFont(bigFont4);
                        } else {
                            jLabelComboP1.setFont(bigFont);
                            jLabelComboCounterP1.setFont(bigFont2);
                        }
                    }

                    if (comboPlayer2 <= 4) {
                        /*jLabelComboCounterP2.setVisible(false);
                        jLabelComboP2.setVisible(false);*/
                        jLabelComboCounterP2.setText(" ");
                        jLabelComboP2.setText(" ");
                    } else {
                        jLabelComboCounterP2.setText(String.valueOf(comboPlayer2));
                        //jLabelComboCounterP2.setVisible(true);
                        //jLabelComboP2.setVisible(true);
                        jLabelComboP2.setText("COMBO");
                        if (animationComboPlayer2 > 0) {
                            animationComboPlayer2++;
                            animationComboPlayer2 = animationComboPlayer2 % 100;
                            jLabelComboP2.setFont(bigFont3);
                            jLabelComboCounterP2.setFont(bigFont4);
                        } else {
                            jLabelComboP2.setFont(bigFont);
                            jLabelComboCounterP2.setFont(bigFont2);
                        }
                    }

                    if (memoryMessageP1 > 0) {
                        memoryMessageP1++;
                        memoryMessageP1 = memoryMessageP1 % 200;
                        if (memoryMessageP1 < 100) {
                            jLabelMessageP1.setFont(bigFont2);
                        } else {
                            jLabelMessageP1.setFont(bigFont);
                        }
                        //jLabelMessageP1.setVisible(true);
                    } else {
                        jLabelMessageP1.setText(" ");
                    }

                    if (memoryMessageP2 > 0) {
                        memoryMessageP2++;
                        memoryMessageP2 = memoryMessageP2 % 200;
                        if (memoryMessageP2 < 100) {
                            jLabelMessageP2.setFont(bigFont2);
                        } else {
                            jLabelMessageP2.setFont(bigFont);
                        }
                        //jLabelMessageP2.setVisible(true);
                    } else {
                        jLabelMessageP2.setText(" ");
                    }

                }

                dancerCounter++;
                if (dancerCounter >= dancerSpeed) {
                    dancerCounter = 0;
                    dancerFrame++;
                    dancerFrame = dancerFrame % 126;
                    jLabelDancer2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dancer/dancer (" + dancerFrame + ").gif")));
                }
                
                counterSong++;
                System.out.println("counterSong:"+counterSong);
                if(counterSong>=21500){
                    clip.stop();
                    analyzeResults();
                    counterSong=0;
                }

            }
        };

        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(timerTaskMovement, 6000, speed);

        TimerTask timerTaskMovementGUIAnimation;
        timerTaskMovementGUIAnimation = new TimerTask() {
            //int innerCounter = initialPosition + gapBetweenArrows * numberOfArrowsPerGroup;

            public void run() {
                headerCounter++;
                headerCounter = headerCounter % 13;
                jLabelHeader.setIcon(new javax.swing.ImageIcon(getClass().getResource("/headers/header" + headerCounter + ".png")));
            }
        };

        Timer timer3 = new Timer();
        timer3.scheduleAtFixedRate(timerTaskMovementGUIAnimation, 0, 100);
    }
    
    public void analyzeResults(){
        
        String result="";
        int pointsPlayer1Total=0;
        int pointsPlayer2Total=0;
        
        for(int i=0;i<listOfImageBackgrounds.size();i++){
            //if both lists have entries, this means the picture needs to be labeled
            if ((pointsPlayer1.get(i)>=(numberOfArrowsPerGroup))&&(pointsPlayer2.get(i)>=(numberOfArrowsPerGroup))){
                //Image needs to be labeled
                pointsPlayer1Total+=pointsPlayer1.get(i);
                pointsPlayer2Total+=pointsPlayer2.get(i);
                result+="1";
            }else if(((pointsPlayer1.get(i)>=(numberOfArrowsPerGroup/2))&&(pointsPlayer2.get(i)<=(numberOfArrowsPerGroup/2))) || ((pointsPlayer1.get(i)<=(numberOfArrowsPerGroup/2))&&(pointsPlayer2.get(i)>=(numberOfArrowsPerGroup/2)))){
                //One player made a mistake
                if(pointsPlayer1.get(i)>pointsPlayer2.get(i)){
                    pointsPlayer1Total-=pointsPlayer1.get(i);
                }else{
                    pointsPlayer2Total-=pointsPlayer2.get(i);
                }
                result+="0";
            }else{
                result+="0";
            }
        }
        if (pointsPlayer1Total>pointsPlayer2Total){
            jLabelResultP1.setText("WINNER");
            jLabelResultP2.setText("LOSER");
        }else if(pointsPlayer1Total<pointsPlayer2Total){
            jLabelResultP2.setText("WINNER");
            jLabelResultP1.setText("LOSER");
        }else{
            jLabelResultP1.setText("DRAW");
            jLabelResultP2.setText("DRAW");
        }
        
        //send result to server
        client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, "RESULTOFGAME:" + result));
        
    }

    /*public int getXBound(String str, int playerIndex) {
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
    }*/
    public void startGame() {
        waitingForAdversary = 2;
        jLabel13.setText("Connected");
        createGUI();
        readSong();

    }

    public void createGUI() {
        //jLabel2.setBounds(10,50,60,60);
        jPanelBackground1.setBackground("/backgrounds/bg2.png");

        try { // TODO: Add sound clips if want to add sound when clicking.
            //AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/songsPackage/loveSugar.wav"));
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/songsPackage/witch.wav"));
            //AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/songsPackage/belovin.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void readSong() {
        //Scanner scanner = new Scanner(getClass().getResourceAsStream("testSong.txt"));
        //Scanner scanner = new Scanner(getClass().getResourceAsStream("/songsPackage/LOVE-LOVE-SUGAR.txt"));
        Scanner scanner = new Scanner(getClass().getResourceAsStream("/songsPackage/WITCH.txt"));
        //Scanner scanner = new Scanner(getClass().getResourceAsStream("/songsPackage/BELOVIN.txt"));        
        String s = new String();
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            s = s + nextLine + "\n";
            //System.out.println("Scanner line:"+nextLine);
            try {
                listOfArrowIntegers.add(Integer.valueOf(nextLine));
                listOfArrowIntegersCopy.add(Integer.valueOf(nextLine));
            } catch (Exception e) {
                listOfArrowIntegers.add(0);
                listOfArrowIntegersCopy.add(0);
            }
        }
        //

        numberOfGroups = listOfArrowIntegersCopy.size() / numberOfArrowsPerGroup;

        //createListOfArrows(listOfArrowIntegers);
        //listOfArrowsPlayer1 = createListOfArrows(listOfArrowIntegers, 1);
        //listOfArrowsPlayer2 = createListOfArrows(listOfArrowIntegers, 2);
        createArrows();
    }

    /*public void createListOfArrows(ArrayList<Integer> listOfArrowsInteger) {
        for (int i = 0; i < listOfArrowsInteger.size(); i++) {
            System.out.println(listOfArrowsInteger.get(i));
            if (listOfArrowsInteger.get(i) > 0) {
                //System.out.println("i=" + i);

                Arrow arrow1 = new Arrow("A", convertIntegerToTypeOfArrow(listOfArrowsInteger.get(i)), initialPosition + gapBetweenArrows * i, 1);
                Arrow arrow2 = new Arrow("B", convertIntegerToTypeOfArrow(listOfArrowsInteger.get(i)), initialPosition + gapBetweenArrows * i, 2);
                listOfArrowsPlayer1.add(arrow1);
                listOfArrowsPlayer2.add(arrow2);
                jPanelBackground1.add(arrow1);
                jPanelBackground1.add(arrow2);

                //System.out.println(listOfArrowsInteger.get(i));
            }
        }
    }*/

 /*public ArrayList<Arrow> createListOfArrows(ArrayList<Integer> listOfArrowsInteger, int playerIndex) {
        ArrayList<Arrow> result = new ArrayList<Arrow>();
        System.out.println(listOfArrowsInteger.size());

        for (int i = 0; i < listOfArrowsInteger.size(); i++) {
            System.out.println(listOfArrowsInteger.get(i));
            if (listOfArrowsInteger.get(i) > 0) {
                System.out.println("i=" + i);
                
                Arrow arrow;
                if(playerIndex==1){
                    arrow= new Arrow("A", convertIntegerToTypeOfArrow(listOfArrowsInteger.get(i)), initialPosition + gapBetweenArrows * i, playerIndex);
                }else{
                    arrow= new Arrow("B", convertIntegerToTypeOfArrow(listOfArrowsInteger.get(i)), initialPosition + gapBetweenArrows * i, playerIndex);
                }
                result.add(arrow);
                jPanelBackground1.add(arrow);
                //System.out.println(listOfArrowsInteger.get(i));
            }
        }
        return result;
    }*/
    public String convertIntegerToTypeOfArrow(Integer n) {
        String result = "";
        if (n == 1) {
            result = "L";
        } else if (n == 2) {
            result = "D";
        } else if (n == 3) {
            result = "U";
        } else if (n == 4) {
            result = "R";
        } else {
            result = "L";
        }
        return result;
    }

    public void append(String str) {
        //ta.append(str);
        //ta.setCaretPosition(ta.getText().length() - 1);
        //System.out.println("Mensaje : " + str);

        if (str.contains("Number of users:")) {
            //A new user connected
            int numberOfUsers = Integer.valueOf(str.substring(16));
            System.out.println("Number of Users in append:" + numberOfUsers);
            if (playerIndex == 0) {
                playerIndex = numberOfUsers;
                if (playerIndex == 1) {
                    jLabelBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/headers/botomP1.png")));
                }
                if (playerIndex == 2) {
                    jLabelBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/headers/botomP2.png")));
                }

            }

            //startGame();
            if (numberOfUsers == 2) {
                //TODO: startGame();
                startGame();
            }
        } else if ((str.contains("SAME WORDS:")) || (str.contains("ERROR IN WORDS"))) {
            //TODO resultOfTest(str);
        } else if (str.contains("END GAME")) {
            connectionFailed();
        } else if (str.contains("IMAGE")) {
            getImageFromServerString(str);
        } else if ((str.contains("PLAYER1")) || (str.contains("PLAYER2"))) {
            replicateKeyPad(str);
        } else if (str.contains("NUMBEROFPICTURES:")) {
            getNumberOfPictures(str);
        } else if (str.contains("LABELTOUSE:")) {
            getWordToLabel(str);
        }
    }

    public void getNumberOfPictures(String str) {
        try {
            numberOfPictures = Integer.valueOf(str.substring(17));
        } catch (Exception e) {
            e.printStackTrace();
            numberOfPictures = 1;
        }
        System.out.println("Number of pictures from server in client:" + numberOfPictures);
    }

    public void getWordToLabel(String str) {
        try {
            wordToLabel = str.substring(11);
        } catch (Exception e) {
            e.printStackTrace();
            wordToLabel = "";
        }
        System.out.println("Word to label from server in client:" + wordToLabel);
        jLabelWordToLabel.setText(wordToLabel);
    }

    public void getImageFromServerString(String str) {
        String result = str.substring(6);
        //System.out.println(result);
        BufferedImage image = decodeToImage(result);
        listOfImageBackgrounds.add(image);
        pointsPlayer1.add(0);
        pointsPlayer2.add(0);

        /*JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);*/
    }

    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    // called by the GUI is the connection failed
    // we reset our buttons, label, textfield
    public void connectionFailed() {
        connected = false;
        jLabel11.setText("Disconnected");
        jLabel13.setText("Disconnected");
        jLabelServerStatus.setBackground(Color.red);
        waitingForAdversary = 0;
        //playerIndex=0;
        JOptionPane.showMessageDialog(this, "At least one player cannot be detected. The program will close.");
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelBackground1 = new client.JPanelBackground();
        jLabelP1L = new javax.swing.JLabel();
        jLabelP1D = new javax.swing.JLabel();
        jLabelP1U = new javax.swing.JLabel();
        jLabelP1R = new javax.swing.JLabel();
        jLabelP2L = new javax.swing.JLabel();
        jLabelP2D = new javax.swing.JLabel();
        jLabelP2U = new javax.swing.JLabel();
        jLabelP2R = new javax.swing.JLabel();
        jLabelMessageP1 = new client.JLabel2D();
        jLabelMessageP2 = new client.JLabel2D();
        jLabelComboCounterP1 = new client.JLabel2D();
        jLabelComboP1 = new client.JLabel2D();
        jLabelComboP2 = new client.JLabel2D();
        jLabelComboCounterP2 = new client.JLabel2D();
        jLabelBottom = new javax.swing.JLabel();
        jLabelDancer2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabelYourWordIs = new client.JLabel2D();
        jLabelWordToLabel = new client.JLabel2D();
        jLabelResultP2 = new client.JLabel2D();
        jLabelResultP1 = new client.JLabel2D();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabelServerStatus = new javax.swing.JLabel();
        jLabelAdversaryStatus = new javax.swing.JLabel();
        jLabelHeader = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanelBackground1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelBackground1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanelBackground1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPanelBackground1KeyReleased(evt);
            }
        });

        jLabelP1L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-L.png"))); // NOI18N

        jLabelP1D.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-D.png"))); // NOI18N

        jLabelP1U.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-U.png"))); // NOI18N

        jLabelP1R.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-R.png"))); // NOI18N

        jLabelP2L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-L.png"))); // NOI18N

        jLabelP2D.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-D.png"))); // NOI18N

        jLabelP2U.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-U.png"))); // NOI18N

        jLabelP2R.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-R.png"))); // NOI18N

        jLabelMessageP1.setText("jLabel2D1");

        jLabelMessageP2.setText("jLabel2D1");

        jLabelComboCounterP1.setText("jLabel2D1");

        jLabelComboP1.setText("jLabel2D2");

        jLabelComboP2.setText("jLabel2D2");

        jLabelComboCounterP2.setText("jLabel2D1");

        jLabelBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/headers/botom.png"))); // NOI18N
        jLabelBottom.setText("   ");

        jLabelDancer2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabelYourWordIs.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelYourWordIs.setText("jLabel2D1");

        jLabelWordToLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelWordToLabel.setText("jLabel2D2");

        jLabelResultP2.setText("   ");

        jLabelResultP1.setText("   ");

        javax.swing.GroupLayout jPanelBackground1Layout = new javax.swing.GroupLayout(jPanelBackground1);
        jPanelBackground1.setLayout(jPanelBackground1Layout);
        jPanelBackground1Layout.setHorizontalGroup(
            jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBackground1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBackground1Layout.createSequentialGroup()
                        .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelBackground1Layout.createSequentialGroup()
                                .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanelBackground1Layout.createSequentialGroup()
                                        .addComponent(jLabelComboCounterP1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabelComboP1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBackground1Layout.createSequentialGroup()
                                .addComponent(jLabelResultP1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(5, 5, 5)))
                        .addComponent(jLabelDancer2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelBackground1Layout.createSequentialGroup()
                                .addComponent(jLabelComboCounterP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelComboP2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelResultP2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBackground1Layout.createSequentialGroup()
                        .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelBackground1Layout.createSequentialGroup()
                                .addComponent(jLabelP1L)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelP1D)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelP1U)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelP1R))
                            .addComponent(jLabelMessageP1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelYourWordIs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelBackground1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabelWordToLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelBackground1Layout.createSequentialGroup()
                                .addComponent(jLabelP2L)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelP2D)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelP2U)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelP2R))
                            .addComponent(jLabelMessageP2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addComponent(jLabelBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelBackground1Layout.setVerticalGroup(
            jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBackground1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelBackground1Layout.createSequentialGroup()
                        .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelP1D)
                            .addComponent(jLabelP1L)
                            .addComponent(jLabelP1U)
                            .addComponent(jLabelP1R)
                            .addComponent(jLabelP2D)
                            .addComponent(jLabelP2L)
                            .addComponent(jLabelP2U)
                            .addComponent(jLabelP2R))
                        .addGap(15, 15, 15))
                    .addGroup(jPanelBackground1Layout.createSequentialGroup()
                        .addComponent(jLabelYourWordIs, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelWordToLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBackground1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelMessageP1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelMessageP2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelBackground1Layout.createSequentialGroup()
                                .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelComboP2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelComboCounterP2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(314, 314, 314)
                                .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelResultP2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelResultP1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanelBackground1Layout.createSequentialGroup()
                                .addGap(231, 231, 231)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelComboCounterP1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelComboP1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelDancer2, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelBackground1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelBottom))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setText("Server:");

        jLabel7.setText("127.0.0.1");

        jLabel8.setText("Port:");

        jLabel9.setText("1500");

        jLabel10.setText("Server Status:");

        jLabel11.setText("Disconnected");

        jLabel12.setText("Adversary status:");

        jLabel13.setText("Disconnected");

        jLabelServerStatus.setBackground(new java.awt.Color(255, 0, 0));
        jLabelServerStatus.setText("   ");
        jLabelServerStatus.setOpaque(true);

        jLabelAdversaryStatus.setBackground(new java.awt.Color(255, 0, 0));
        jLabelAdversaryStatus.setText("   ");
        jLabelAdversaryStatus.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelServerStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelAdversaryStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabelServerStatus)
                    .addComponent(jLabelAdversaryStatus)))
        );

        jLabelHeader.setText("   ");

        jMenu1.setText("File");

        jMenuItem1.setText("Connect...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Connection");

        jMenuItem2.setText("Server IP...");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Port IP...");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelBackground1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelBackground1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanelBackground1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanelBackground1KeyReleased
//        if (waitingForAdversary == 2) {
//            if (playerIndex == 1) {
                if ((evt.getKeyCode() == player1L) || (evt.getKeyCode() == player1D) || (evt.getKeyCode() == player1U) || (evt.getKeyCode() == player1R)) {
                    client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, "PLAYER1RELEASED:" + evt.getKeyCode()));
                }
//            } else if (playerIndex == 2) {
                if ((evt.getKeyCode() == player2L) || (evt.getKeyCode() == player2D) || (evt.getKeyCode() == player2U) || (evt.getKeyCode() == player2R)) {
                    client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, "PLAYER2RELEASED:" + evt.getKeyCode()));
                }
//            }
//        }

        /*if ((40 <= arrow1.getBounds().getY()) && (arrow1.getBounds().getY() <= 65)) {
            if (evt.getKeyCode() == evt.VK_DOWN) {
                System.out.println("YES");
                arrow1.destroy2();
            }
        }    */    // TODO add your handling code here:
    }//GEN-LAST:event_jPanelBackground1KeyReleased

    public void replicateKeyPad(String str) {
        try {
            if (str.contains("PLAYER1RELEASED:")) {
                if (str.equalsIgnoreCase("PLAYER1RELEASED:" + player1L)) {
                    jLabelP1L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-L.png"))); // NOI18N
                }
                if (str.equalsIgnoreCase("PLAYER1RELEASED:" + player1D)) {
                    jLabelP1D.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-D.png"))); // NOI18N
                }
                if (str.equalsIgnoreCase("PLAYER1RELEASED:" + player1U)) {
                    jLabelP1U.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-U.png"))); // NOI18N
                }
                if (str.equalsIgnoreCase("PLAYER1RELEASED:" + player1R)) {
                    jLabelP1R.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-R.png")));
                }
            }
            if (str.contains("PLAYER2RELEASED:")) {
                if (str.equalsIgnoreCase("PLAYER2RELEASED:" + player2L)) {
                    jLabelP2L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-L.png"))); // NOI18N
                }
                if (str.equalsIgnoreCase("PLAYER2RELEASED:" + player2D)) {
                    jLabelP2D.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-D.png"))); // NOI18N
                }
                if (str.equalsIgnoreCase("PLAYER2RELEASED:" + player2U)) {
                    jLabelP2U.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-U.png"))); // NOI18N
                }
                if (str.equalsIgnoreCase("PLAYER2RELEASED:" + player2R)) {
                    jLabelP2R.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-R.png")));
                }
            }
            if (str.contains("PLAYER1PRESSED:")) {
                if (str.equalsIgnoreCase("PLAYER1PRESSED:" + player1L)) {
                    jLabelP1L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-B-L.png"))); // NOI18N
                }
                if (str.equalsIgnoreCase("PLAYER1PRESSED:" + player1D)) {
                    jLabelP1D.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-B-D.png"))); // NOI18N
                }
                if (str.equalsIgnoreCase("PLAYER1PRESSED:" + player1U)) {
                    jLabelP1U.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-B-U.png"))); // NOI18N
                }
                if (str.equalsIgnoreCase("PLAYER1PRESSED:" + player1R)) {
                    jLabelP1R.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-B-R.png")));
                }
            }
            if (str.contains("PLAYER2PRESSED:")) {
                if (str.equalsIgnoreCase("PLAYER2PRESSED:" + player2L)) {
                    jLabelP2L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-B-L.png"))); // NOI18N
                }
                if (str.equalsIgnoreCase("PLAYER2PRESSED:" + player2D)) {
                    jLabelP2D.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-B-D.png"))); // NOI18N
                }
                if (str.equalsIgnoreCase("PLAYER2PRESSED:" + player2U)) {
                    jLabelP2U.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-B-U.png"))); // NOI18N
                }
                if (str.equalsIgnoreCase("PLAYER2PRESSED:" + player2R)) {
                    jLabelP2R.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-B-R.png")));
                }
            }

            //DESTROY ARROWS
            if (str.contains("PLAYER1PRESSED:")) {
                //for (int i = 0; i < listOfArrowsPlayer1.size(); i++) {
                for (int i = 0; i < 6; i++) {
                    if ((40 <= listOfArrowsPlayer1.get(i).getBounds().getY()) && (listOfArrowsPlayer1.get(i).getBounds().getY() <= 90)) {
                        if (listOfArrowsPlayer1.get(i).getDirection().equalsIgnoreCase("L")) {
                            if (str.equalsIgnoreCase("PLAYER1PRESSED:" + player1L)) {
                                System.out.println("YES");
                                listOfArrowsPlayer1.get(i).destroy2();
                                listOfArrowsPlayer1.remove(i);
                                comboPlayer1++;
                                animationComboPlayer1 = 1;
                                showMessagePlayer(1);
                                memoryMessageP1 = 1;
                                int points = pointsPlayer1.get(backgroundIndex)+1;
                                pointsPlayer1.set(backgroundIndex, points);
                            }
                        } else if (listOfArrowsPlayer1.get(i).getDirection().equalsIgnoreCase("D")) {
                            if (str.equalsIgnoreCase("PLAYER1PRESSED:" + player1D)) {
                                System.out.println("YES");
                                listOfArrowsPlayer1.get(i).destroy2();
                                listOfArrowsPlayer1.remove(i);
                                comboPlayer1++;
                                animationComboPlayer1 = 1;
                                showMessagePlayer(1);
                                memoryMessageP1 = 1;
                                int points = pointsPlayer1.get(backgroundIndex)+1;
                                pointsPlayer1.set(backgroundIndex, points);
                            }
                        } else if (listOfArrowsPlayer1.get(i).getDirection().equalsIgnoreCase("U")) {
                            if (str.equalsIgnoreCase("PLAYER1PRESSED:" + player1U)) {
                                System.out.println("YES");
                                listOfArrowsPlayer1.get(i).destroy2();
                                listOfArrowsPlayer1.remove(i);
                                comboPlayer1++;
                                animationComboPlayer1 = 1;
                                showMessagePlayer(1);
                                memoryMessageP1 = 1;
                                int points = pointsPlayer1.get(backgroundIndex)+1;
                                pointsPlayer1.set(backgroundIndex, points);
                            }

                        } else if (listOfArrowsPlayer1.get(i).getDirection().equalsIgnoreCase("R")) {
                            if (str.equalsIgnoreCase("PLAYER1PRESSED:" + player1R)) {
                                System.out.println("YES");
                                listOfArrowsPlayer1.get(i).destroy2();
                                listOfArrowsPlayer1.remove(i);
                                comboPlayer1++;
                                animationComboPlayer1 = 1;
                                showMessagePlayer(1);
                                memoryMessageP1 = 1;
                                int points = pointsPlayer1.get(backgroundIndex)+1;
                                pointsPlayer1.set(backgroundIndex, points);
                            }
                        }

                    }
                }
            }
            if (str.contains("PLAYER2PRESSED:")) {
                //for (int i = 0; i < listOfArrowsPlayer2.size(); i++) {
                for (int i = 0; i < 6; i++) {
                    if ((40 <= listOfArrowsPlayer2.get(i).getBounds().getY()) && (listOfArrowsPlayer2.get(i).getBounds().getY() <= 90)) {
                        if (listOfArrowsPlayer2.get(i).getDirection().equalsIgnoreCase("L")) {
                            if (str.equalsIgnoreCase("PLAYER2PRESSED:" + player2L)) {
                                System.out.println("YES");
                                listOfArrowsPlayer2.get(i).destroy2();
                                listOfArrowsPlayer2.remove(i);
                                comboPlayer2++;
                                animationComboPlayer2 = 1;
                                showMessagePlayer(2);
                                memoryMessageP2 = 1;
                                int points = pointsPlayer2.get(backgroundIndex)+1;
                                pointsPlayer2.set(backgroundIndex, points);
                            }
                        } else if (listOfArrowsPlayer2.get(i).getDirection().equalsIgnoreCase("D")) {
                            if (str.equalsIgnoreCase("PLAYER2PRESSED:" + player2D)) {
                                System.out.println("YES");
                                listOfArrowsPlayer2.get(i).destroy2();
                                listOfArrowsPlayer2.remove(i);
                                comboPlayer2++;
                                animationComboPlayer2 = 1;
                                showMessagePlayer(2);
                                memoryMessageP2 = 1;
                                int points = pointsPlayer2.get(backgroundIndex)+1;
                                pointsPlayer2.set(backgroundIndex, points);
                            }
                        } else if (listOfArrowsPlayer2.get(i).getDirection().equalsIgnoreCase("U")) {
                            if (str.equalsIgnoreCase("PLAYER2PRESSED:" + player2U)) {
                                System.out.println("YES");
                                listOfArrowsPlayer2.get(i).destroy2();
                                listOfArrowsPlayer2.remove(i);
                                comboPlayer2++;
                                animationComboPlayer2 = 1;
                                showMessagePlayer(2);
                                memoryMessageP2 = 1;
                                int points = pointsPlayer2.get(backgroundIndex)+1;
                                pointsPlayer2.set(backgroundIndex, points);
                            }

                        } else if (listOfArrowsPlayer2.get(i).getDirection().equalsIgnoreCase("R")) {
                            if (str.equalsIgnoreCase("PLAYER2PRESSED:" + player2R)) {
                                System.out.println("YES");
                                listOfArrowsPlayer2.get(i).destroy2();
                                listOfArrowsPlayer2.remove(i);
                                comboPlayer2++;
                                animationComboPlayer2 = 1;
                                showMessagePlayer(2);
                                memoryMessageP2 = 1;
                                int points = pointsPlayer2.get(backgroundIndex)+1;
                                pointsPlayer2.set(backgroundIndex, points);
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in removing");
        }

    }

    public void showMessagePlayer(int i) {
        int numberOfMessage = randInt(0, 4);
        String message = "";
        switch (numberOfMessage) {
            case 0:
                message = "PERFECT!!";
                break;
            case 1:
                message = "AWESOME!!";
                break;
            case 2:
                message = "MARVELOUS!!";
                break;
            case 3:
                message = "GOOD";
                break;
            case 4:
                message = "AMAIZING!!";
                break;
            default:
                message = "EXTRAORDINARY!!";
                break;
        }
        if (i == 1) {
            jLabelMessageP1.setText(message);
            //jLabelMessageP1.setVisible(true);
        } else {
            jLabelMessageP2.setText(message);
            //jLabelMessageP2.setVisible(true);
        }
    }

    private void jPanelBackground1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanelBackground1KeyPressed

//        if (waitingForAdversary == 2) {
//            if (playerIndex == 1) {
                if ((evt.getKeyCode() == player1L) || (evt.getKeyCode() == player1D) || (evt.getKeyCode() == player1U) || (evt.getKeyCode() == player1R)) {
                    client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, "PLAYER1PRESSED:" + evt.getKeyCode()));
                }
//            } else if (playerIndex == 2) {
                if ((evt.getKeyCode() == player2L) || (evt.getKeyCode() == player2D) || (evt.getKeyCode() == player2U) || (evt.getKeyCode() == player2R)) {
                    client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, "PLAYER2PRESSED:" + evt.getKeyCode()));
                }
//            }
//        }


    }//GEN-LAST:event_jPanelBackground1KeyPressed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (jMenuItem1.isEnabled()) {
            //verify data
            // try creating a new Client with GUI
            client = new Client(defaultHost, defaultPort, "player", this);
            // test if we can start the Client
            if (!client.start()) {
                return;
            }
            connected = true;
            client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));
            jLabel13.setText("Waiting for adversary...");
            waitingForAdversary = 1;

//            jMenuItem3.setEnabled(false);
//            jMenuItem4.setEnabled(false);
        }
        jMenuItem1.setEnabled(false);
        jMenuItem2.setEnabled(false);
        jMenuItem3.setEnabled(false);
        //jButton2.setEnabled(false);        //System.out.println("Comida");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            defaultHost = JOptionPane.showInputDialog(null, "Enter the game server's IP address");
        } catch (Exception e) {
            e.printStackTrace();
            defaultHost = "127.0.0.1";
            JOptionPane.showMessageDialog(this, "Error in Server's IP address. The default IP address will be used instead.");
        }
        jLabel7.setText(defaultHost);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        try {
            defaultPort = Integer.valueOf(JOptionPane.showInputDialog(null, "Enter the game server's port"));
        } catch (Exception e) {
            e.printStackTrace();
            defaultPort = 1500;
            JOptionPane.showMessageDialog(this, "Error in Server's port number. The default port number will be used instead.");
        }
        jLabel9.setText(String.valueOf(defaultPort));        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DDRMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DDRMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DDRMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DDRMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DDRMain().setVisible(true);

            }
        });

    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel10;
    public javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public static javax.swing.JLabel jLabelAdversaryStatus;
    private javax.swing.JLabel jLabelBottom;
    private client.JLabel2D jLabelComboCounterP1;
    private client.JLabel2D jLabelComboCounterP2;
    private client.JLabel2D jLabelComboP1;
    private client.JLabel2D jLabelComboP2;
    private javax.swing.JLabel jLabelDancer2;
    private javax.swing.JLabel jLabelHeader;
    private client.JLabel2D jLabelMessageP1;
    private client.JLabel2D jLabelMessageP2;
    private javax.swing.JLabel jLabelP1D;
    private javax.swing.JLabel jLabelP1L;
    private javax.swing.JLabel jLabelP1R;
    private javax.swing.JLabel jLabelP1U;
    private javax.swing.JLabel jLabelP2D;
    private javax.swing.JLabel jLabelP2L;
    private javax.swing.JLabel jLabelP2R;
    private javax.swing.JLabel jLabelP2U;
    private client.JLabel2D jLabelResultP1;
    private client.JLabel2D jLabelResultP2;
    public static javax.swing.JLabel jLabelServerStatus;
    private client.JLabel2D jLabelWordToLabel;
    private client.JLabel2D jLabelYourWordIs;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private client.JPanelBackground jPanelBackground1;
    // End of variables declaration//GEN-END:variables
}
