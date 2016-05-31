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
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
    public ArrayList<Arrow> listOfArrowsPlayer1 = new ArrayList<Arrow>();
    public ArrayList<Arrow> listOfArrowsPlayer2 = new ArrayList<Arrow>();

    public ArrayList<Integer> listOfArrowIntegers = new ArrayList<>();
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
    //public static String defaultHost = "localhost";
    public static String defaultHost = "192.168.48.103";

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

    public DDRMain() {
        initComponents();
        jLabelPanelPlayer1.setBounds(0,0, 100,100);
        //jLabelPanelPlayer1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        

        jPanelBackground1.setFocusable(true);
        jPanelBackground1.requestFocusInWindow();

        statusOfConnection();

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

    public void startGame() {
        waitingForAdversary = 2;
        jLabel13.setText("Connected");
        changeBackgrounds();
        createGUI();
        readSong();

    }

    public void createGUI() {
        //jLabel2.setBounds(10,50,60,60);
        //jPanelBackground1.setBackground("/backgrounds/bg2.png");

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

    public void changeBackgrounds() {
        Timer timer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                backgroundIndex++;
                backgroundIndex = backgroundIndex % listOfImageBackgrounds.size();
                jPanelBackground1.setBackground(listOfImageBackgrounds.get(backgroundIndex));
            }
        };

        timer.schedule(myTask, 500, 500);
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
            } catch (Exception e) {
                listOfArrowIntegers.add(0);
            }
        }
        //
        createListOfArrows(listOfArrowIntegers);
        //listOfArrowsPlayer1 = createListOfArrows(listOfArrowIntegers, 1);
        //listOfArrowsPlayer2 = createListOfArrows(listOfArrowIntegers, 2);
    }
    public void createListOfArrows(ArrayList<Integer> listOfArrowsInteger){
        for (int i = 0; i < listOfArrowsInteger.size(); i++) {
            System.out.println(listOfArrowsInteger.get(i));
            if (listOfArrowsInteger.get(i) > 0) {
                //System.out.println("i=" + i);
                
                Arrow arrow1 = new Arrow("A", convertIntegerToTypeOfArrow(listOfArrowsInteger.get(i)), initialPosition + gapBetweenArrows * i, 1);
                Arrow arrow2= new Arrow("B", convertIntegerToTypeOfArrow(listOfArrowsInteger.get(i)), initialPosition + gapBetweenArrows * i, 2);
                listOfArrowsPlayer1.add(arrow1);
                listOfArrowsPlayer2.add(arrow2);
                jPanelBackground1.add(arrow1);
                jPanelBackground1.add(arrow2);
                
            
                
                //System.out.println(listOfArrowsInteger.get(i));
            }
        }
    }

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
        }

    }

    public void getImageFromServerString(String str) {
        String result = str.substring(6);
        //System.out.println(result);
        BufferedImage image = decodeToImage(result);
        listOfImageBackgrounds.add(image);

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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
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
        jLabelPanelPlayer1 = new javax.swing.JLabel();
        jLabelPanelPlayer2 = new javax.swing.JLabel();
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

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

        javax.swing.GroupLayout jPanelBackground1Layout = new javax.swing.GroupLayout(jPanelBackground1);
        jPanelBackground1.setLayout(jPanelBackground1Layout);
        jPanelBackground1Layout.setHorizontalGroup(
            jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBackground1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBackground1Layout.createSequentialGroup()
                        .addComponent(jLabelP1L)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelP1D)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelP1U)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelP1R)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelP2L)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelP2D)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelP2U)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelP2R))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBackground1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabelPanelPlayer1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(jLabelPanelPlayer2, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelBackground1Layout.setVerticalGroup(
            jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBackground1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelP1D)
                    .addComponent(jLabelP1L)
                    .addComponent(jLabelP1U)
                    .addComponent(jLabelP1R)
                    .addComponent(jLabelP2D)
                    .addComponent(jLabelP2L)
                    .addComponent(jLabelP2U)
                    .addComponent(jLabelP2R))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPanelPlayer2, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                    .addComponent(jLabelPanelPlayer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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
                .addGap(0, 61, Short.MAX_VALUE))
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

        jMenu1.setText("File");

        jMenuItem1.setText("Connect...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelBackground1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelBackground1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>                        

    private void jPanelBackground1KeyReleased(java.awt.event.KeyEvent evt) {                                              
        if (waitingForAdversary == 2) {
            if (playerIndex == 1) {
                if ((evt.getKeyCode() == player1L) || (evt.getKeyCode() == player1D) || (evt.getKeyCode() == player1U) || (evt.getKeyCode() == player1R)) {
                    client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, "PLAYER1RELEASED:" + evt.getKeyCode()));
                }
            } else if (playerIndex == 2) {
                if ((evt.getKeyCode() == player2L) || (evt.getKeyCode() == player2D) || (evt.getKeyCode() == player2U) || (evt.getKeyCode() == player2R)) {
                    client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, "PLAYER2RELEASED:" + evt.getKeyCode()));
                }
            }
        }

        /*if ((40 <= arrow1.getBounds().getY()) && (arrow1.getBounds().getY() <= 65)) {
            if (evt.getKeyCode() == evt.VK_DOWN) {
                System.out.println("YES");
                arrow1.destroy2();
            }
        }    */    // TODO add your handling code here:
    }                                             

    public void replicateKeyPad(String str) {
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
            for (int i = 0; i < listOfArrowsPlayer1.size(); i++) {
            //for (int i = 0; i < 6; i++) {
                if ((40 <= listOfArrowsPlayer1.get(i).getBounds().getY()) && (listOfArrowsPlayer1.get(i).getBounds().getY() <= 90)) {
                    if (listOfArrowsPlayer1.get(i).getDirection().equalsIgnoreCase("L")) {
                        if (str.equalsIgnoreCase("PLAYER1PRESSED:" + player1L)) {
                            System.out.println("YES");
                            listOfArrowsPlayer1.get(i).destroy2();
                            listOfArrowsPlayer1.remove(i);
                        }
                    } else if (listOfArrowsPlayer1.get(i).getDirection().equalsIgnoreCase("D")) {
                        if (str.equalsIgnoreCase("PLAYER1PRESSED:" + player1D)) {
                            System.out.println("YES");
                            listOfArrowsPlayer1.get(i).destroy2();
                            listOfArrowsPlayer1.remove(i);
                        }
                    } else if (listOfArrowsPlayer1.get(i).getDirection().equalsIgnoreCase("U")) {
                        if (str.equalsIgnoreCase("PLAYER1PRESSED:" + player1U)) {
                            System.out.println("YES");
                            listOfArrowsPlayer1.get(i).destroy2();
                            listOfArrowsPlayer1.remove(i);
                        }

                    } else if (listOfArrowsPlayer1.get(i).getDirection().equalsIgnoreCase("R")) {
                        if (str.equalsIgnoreCase("PLAYER1PRESSED:" + player1R)) {
                            System.out.println("YES");
                            listOfArrowsPlayer1.get(i).destroy2();
                            listOfArrowsPlayer1.remove(i);
                        }
                    }

                }
            }
        }
        if (str.contains("PLAYER2PRESSED:")) {
            for (int i = 0; i < listOfArrowsPlayer2.size(); i++) {
            //for (int i = 0; i < 6; i++) {
                if ((40 <= listOfArrowsPlayer2.get(i).getBounds().getY()) && (listOfArrowsPlayer2.get(i).getBounds().getY() <= 90)) {
                    if (listOfArrowsPlayer2.get(i).getDirection().equalsIgnoreCase("L")) {
                        if (str.equalsIgnoreCase("PLAYER2PRESSED:" + player2L)) {
                            System.out.println("YES");
                            listOfArrowsPlayer2.get(i).destroy2();
                            listOfArrowsPlayer2.remove(i);
                        }
                    } else if (listOfArrowsPlayer2.get(i).getDirection().equalsIgnoreCase("D")) {
                        if (str.equalsIgnoreCase("PLAYER2PRESSED:" + player2D)) {
                            System.out.println("YES");
                            listOfArrowsPlayer2.get(i).destroy2();
                            listOfArrowsPlayer2.remove(i);
                        }
                    } else if (listOfArrowsPlayer2.get(i).getDirection().equalsIgnoreCase("U")) {
                        if (str.equalsIgnoreCase("PLAYER2PRESSED:" + player2U)) {
                            System.out.println("YES");
                            listOfArrowsPlayer2.get(i).destroy2();
                            listOfArrowsPlayer2.remove(i);
                        }

                    } else if (listOfArrowsPlayer2.get(i).getDirection().equalsIgnoreCase("R")) {
                        if (str.equalsIgnoreCase("PLAYER2PRESSED:" + player2R)) {
                            System.out.println("YES");
                            listOfArrowsPlayer2.get(i).destroy2();
                            listOfArrowsPlayer2.remove(i);
                        }
                    }

                }
            }
        }
    }

    private void jPanelBackground1KeyPressed(java.awt.event.KeyEvent evt) {                                             

        if (waitingForAdversary == 2) {
            if (playerIndex == 1) {
                if ((evt.getKeyCode() == player1L) || (evt.getKeyCode() == player1D) || (evt.getKeyCode() == player1U) || (evt.getKeyCode() == player1R)) {
                    client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, "PLAYER1PRESSED:" + evt.getKeyCode()));
                }
            } else if (playerIndex == 2) {
                if ((evt.getKeyCode() == player2L) || (evt.getKeyCode() == player2D) || (evt.getKeyCode() == player2U) || (evt.getKeyCode() == player2R)) {
                    client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, "PLAYER2PRESSED:" + evt.getKeyCode()));
                }
            }
        }


    }                                            

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
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
        //jButton2.setEnabled(false);        //System.out.println("Comida");
    }                                          

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

    // Variables declaration - do not modify                     
    private javax.swing.JLabel jLabel10;
    public javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public static javax.swing.JLabel jLabelAdversaryStatus;
    private javax.swing.JLabel jLabelP1D;
    private javax.swing.JLabel jLabelP1L;
    private javax.swing.JLabel jLabelP1R;
    private javax.swing.JLabel jLabelP1U;
    private javax.swing.JLabel jLabelP2D;
    private javax.swing.JLabel jLabelP2L;
    private javax.swing.JLabel jLabelP2R;
    private javax.swing.JLabel jLabelP2U;
    private javax.swing.JLabel jLabelPanelPlayer1;
    private javax.swing.JLabel jLabelPanelPlayer2;
    public static javax.swing.JLabel jLabelServerStatus;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private client.JPanelBackground jPanelBackground1;
    // End of variables declaration                   
}
