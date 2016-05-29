/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

/**
 *
 * @author liwuen
 */
public class DDRMain extends javax.swing.JFrame {

    /**
     * Creates new form DDRMain
     */
    public ArrayList<Arrow> listOfArrows = new ArrayList<Arrow>();
    public ArrayList<Integer> listOfArrowIntegers = new ArrayList<>();
    public int initialPosition = 500;
    public int gapBetweenArrows = 100;

    public static Clip clip;
    public static boolean connected;
    public static boolean statusLabelConnected=false;
    public static boolean statusLabelAdversary=false;
    
    public static int waitingForAdversary=0;

    public Client client;
    public static int defaultPort = 1500;
    public static String defaultHost = "localhost";

    
    public DDRMain() {
        initComponents();

        jPanelBackground1.setFocusable(true);
        jPanelBackground1.requestFocusInWindow();
        createGUI();
        readSong();
        
        Timer timer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                //Change color status
                if (connected){
                    //Is connected to server
                    if(statusLabelConnected){
                        statusLabelConnected=false;
                        jLabelServerStatus.setBackground(Color.GREEN);
                    }else{
                        statusLabelConnected=true;
                        jLabelServerStatus.setBackground(Color.WHITE);
                    }
                }else{
                    //player got disconnected
                    jLabelServerStatus.setBackground(Color.red);
                }
                
                if (waitingForAdversary==0){
                    jLabelAdversaryStatus.setBackground(Color.red);
                }else if (waitingForAdversary==1){
                    if(statusLabelConnected){
                        jLabelAdversaryStatus.setBackground(Color.WHITE);
                    }else{
                        jLabelAdversaryStatus.setBackground(Color.YELLOW);
                    }
                }else if (waitingForAdversary==2){
                    if(statusLabelConnected){
                        jLabelAdversaryStatus.setBackground(Color.WHITE);
                    }else{
                        jLabelAdversaryStatus.setBackground(Color.GREEN);
                    }
                }
                
            }
        };

        timer.schedule(myTask, 500, 500);

    }

    public void createGUI() {
        //jLabel2.setBounds(10,50,60,60);
        jPanelBackground1.setBackground("/backgrounds/bg2.png");

        try { // TODO: Add sound clips if want to add sound when clicking.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/songsPackage/loveSugar.wav"));
            //AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/songsPackage/witch.wav"));
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
        Scanner scanner = new Scanner(getClass().getResourceAsStream("/songsPackage/LOVE-LOVE-SUGAR.txt"));
        //Scanner scanner = new Scanner(getClass().getResourceAsStream("/songsPackage/WITCH.txt"));        
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
        listOfArrows = createListOfArrows(listOfArrowIntegers);
    }

    public ArrayList<Arrow> createListOfArrows(ArrayList<Integer> listOfArrowsInteger) {
        ArrayList<Arrow> result = new ArrayList<Arrow>();
        System.out.println(listOfArrowsInteger.size());

        for (int i = 0; i < listOfArrowsInteger.size(); i++) {
            System.out.println(listOfArrowsInteger.get(i));
            if (listOfArrowsInteger.get(i) > 0) {
                System.out.println("i=" + i);
                Arrow arrow = new Arrow("A", convertIntegerToTypeOfArrow(listOfArrowsInteger.get(i)), initialPosition + gapBetweenArrows * i);
                result.add(arrow);
                jPanelBackground1.add(arrow);
                //System.out.println(listOfArrowsInteger.get(i));
            }
        }
        return result;
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
        System.out.println("Mensaje : " + str);

        if (str.contains("Number of users:")) {
            //A new user connected
            int numberOfUsers = Integer.valueOf(str.substring(16));
            System.out.println("Number of Users in append:" + numberOfUsers);

            //startGame();
            if (numberOfUsers == 2) {
                //TODO: startGame();
            }
        } else if ((str.contains("SAME WORDS:")) || (str.contains("ERROR IN WORDS"))) {
            //TODO resultOfTest(str);
        } else if(str.contains("END GAME")){
            connectionFailed();
        }

    }
    // called by the GUI is the connection failed
    // we reset our buttons, label, textfield

    public void connectionFailed() {
        connected = false;
        jLabel11.setText("Disconnected");
        jLabel13.setText("Disconnected");
        jLabelServerStatus.setBackground(Color.red);
        waitingForAdversary=0;
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
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

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-L.png"))); // NOI18N

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-D.png"))); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-U.png"))); // NOI18N

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-R.png"))); // NOI18N

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelBackground1Layout = new javax.swing.GroupLayout(jPanelBackground1);
        jPanelBackground1.setLayout(jPanelBackground1Layout);
        jPanelBackground1Layout.setHorizontalGroup(
            jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBackground1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(698, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBackground1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanelBackground1Layout.setVerticalGroup(
            jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBackground1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(7, 7, 7)
                .addGroup(jPanelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addContainerGap(458, Short.MAX_VALUE))
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
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-D.png"))); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-L.png"))); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-U.png"))); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-A-R.png")));

        /*if ((40 <= arrow1.getBounds().getY()) && (arrow1.getBounds().getY() <= 65)) {
            if (evt.getKeyCode() == evt.VK_DOWN) {
                System.out.println("YES");
                arrow1.destroy2();
            }
        }    */    // TODO add your handling code here:
    }                                             

    private void jPanelBackground1KeyPressed(java.awt.event.KeyEvent evt) {                                             
        if (evt.getKeyCode() == evt.VK_LEFT) {
            jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-B-L.png"))); // NOI18N
        }
        if (evt.getKeyCode() == evt.VK_DOWN) {
            jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-B-D.png"))); // NOI18N
        }
        if (evt.getKeyCode() == evt.VK_UP) {
            jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-B-U.png"))); // NOI18N
        }
        if (evt.getKeyCode() == evt.VK_RIGHT) {
            jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keypadSprites/keypad-B-R.png")));
        }        // TODO add your handling code here:

        for (int i = 0; i < listOfArrows.size(); i++) {
            if ((40 <= listOfArrows.get(i).getBounds().getY()) && (listOfArrows.get(i).getBounds().getY() <= 90)) {
                if (listOfArrows.get(i).getDirection().equalsIgnoreCase("L")) {
                    if (evt.getKeyCode() == evt.VK_LEFT) {
                        System.out.println("YES");
                        listOfArrows.get(i).destroy2();
                        listOfArrows.remove(i);
                    }
                } else if (listOfArrows.get(i).getDirection().equalsIgnoreCase("D")) {
                    if (evt.getKeyCode() == evt.VK_DOWN) {
                        System.out.println("YES");
                        listOfArrows.get(i).destroy2();
                        listOfArrows.remove(i);
                    }
                } else if (listOfArrows.get(i).getDirection().equalsIgnoreCase("U")) {
                    if (evt.getKeyCode() == evt.VK_UP) {
                        System.out.println("YES");
                        listOfArrows.get(i).destroy2();
                        listOfArrows.remove(i);
                    }

                } else if (listOfArrows.get(i).getDirection().equalsIgnoreCase("R")) {
                    if (evt.getKeyCode() == evt.VK_RIGHT) {
                        System.out.println("YES");
                        listOfArrows.get(i).destroy2();
                        listOfArrows.remove(i);
                    }
                }

            }
        }
    }                                            

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        Arrow arrow = new Arrow("A", "L", 300);
        jPanelBackground1.add(arrow);
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
            waitingForAdversary=1;
            
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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    public javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public static javax.swing.JLabel jLabelAdversaryStatus;
    public static javax.swing.JLabel jLabelServerStatus;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private client.JPanelBackground jPanelBackground1;
    // End of variables declaration                   
}
