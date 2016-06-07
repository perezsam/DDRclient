package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import sun.misc.BASE64Encoder;

/*
 * The server as a GUI
 */
public class ServerGUI extends JFrame implements ActionListener, WindowListener {

    private static final long serialVersionUID = 1L;
    // the stop and start buttons
    private JButton stopStart;
    // JTextArea for the chat room and the events
    private JTextArea chat, event;
    // The port number
    private JTextField tPortNumber;
    // my server
    private Server server;

    ArrayList<String> listOfPicturesToSend = new ArrayList<String>();
    ArrayList<String> listOfResultsOfGame = new ArrayList<String>();
    ArrayList<BufferedImage> listOfPicturesToSendBuffered = new ArrayList<BufferedImage>();
    ArrayList<String> listOfPicturesToSendEncoded = new ArrayList<String>();
    int numberOfPicturesToSend = 3;
    
    String wordToLabel="";
    String pathOfResultFile="";

    // server constructor that receive the port to listen to for connection as parameter
    ServerGUI(int port) {
        super("Game Server");
        server = null;
        // in the NorthPanel the PortNumber the Start and Stop buttons
        JPanel north = new JPanel();
        north.add(new JLabel("Port number: "));
        tPortNumber = new JTextField("  " + port);
        north.add(tPortNumber);
        // to stop or start the server, we start with "Start"
        stopStart = new JButton("Start");
        stopStart.addActionListener(this);
        north.add(stopStart);
        add(north, BorderLayout.NORTH);

        // the event and chat room
        JPanel center = new JPanel(new GridLayout(2, 1));
        chat = new JTextArea(80, 80);
        chat.setEditable(false);
        appendRoom("Play room.\n");
        center.add(new JScrollPane(chat));
        event = new JTextArea(80, 80);
        event.setEditable(false);
        appendEvent("Events log.\n");
        center.add(new JScrollPane(event));
        add(center);

        // need to be informed when the user click the close button on the frame
        addWindowListener(this);
        setSize(400, 600);
        setVisible(true);
    }

    // append message to the two JTextArea
    // position at the end
    void appendRoom(String str) {
        chat.append(str);
        chat.setCaretPosition(chat.getText().length() - 1);
        if (str.contains("END GAME")) {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    void appendEvent(String str) {
        event.append(str);
        event.setCaretPosition(chat.getText().length() - 1);
        if (str.contains("END GAME")) {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

    }
    
    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    // start or stop where clicked
    public void actionPerformed(ActionEvent e) {
        // if running we have to stop
        if (server != null) {
            server.stop();
            server = null;
            tPortNumber.setEditable(true);
            stopStart.setText("Start");
            return;
        }
        
        try{
            wordToLabel= String.valueOf(JOptionPane.showInputDialog ( "Input the word to label in the pictures." )); 
        }catch(Exception e1){
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid word format. Please try again.");
            return;
        }
        
        try{
            numberOfPicturesToSend= Integer.valueOf(JOptionPane.showInputDialog ( "Input the number of pictures to label." )); 
        }catch(Exception e2){
            e2.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid number of images to use. Please try again.");
            return;
        }
        
        //Get information of pictures
        for (int i = 0; i < numberOfPicturesToSend; i++) {
            JFrame parentFrame = new JFrame();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify picture");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("png, jpg", "png","png","jpg","jpg","jpg");
            fileChooser.setFileFilter(filter);
            int userSelection = fileChooser.showSaveDialog(parentFrame);

            try {
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    String fileToSavePath = fileToSave.getAbsolutePath();
                    System.out.println("Save as file: " + fileToSavePath);
                    listOfPicturesToSend.add(fileToSavePath);
                    listOfResultsOfGame.add(fileToSavePath+"; Result: ");
                    BufferedImage bimg =ImageIO.read(new File(fileToSavePath));
                    listOfPicturesToSendBuffered.add(bimg);
                    listOfPicturesToSendEncoded.add(encodeToString(bimg, fileToSavePath.substring(fileToSavePath.indexOf(".")+1)));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid image file.");
                return;
            }
        }

        if (listOfPicturesToSendEncoded.size() < numberOfPicturesToSend) {
            JOptionPane.showMessageDialog(this, "You need to select exactly " + numberOfPicturesToSend + " pictures to send to the players.");
            return;
        }
        
        try{
            JOptionPane.showMessageDialog(this, "Select destination to save result.");
            JFrame parentFrame = new JFrame();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt", "txt");
            fileChooser.setFileFilter(filter);
            int userSelection = fileChooser.showSaveDialog(parentFrame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                System.out.println("Save as file: " + fileToSave.getAbsolutePath() + ".txt");
                pathOfResultFile = fileToSave.getAbsolutePath() + ".txt";
            }
        }catch(Exception e3){
            e3.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid path. Please try again.");
            return;
        }

        // OK start the server	
        int port;
        try {
            port = Integer.parseInt(tPortNumber.getText().trim());
        } catch (Exception er) {
            appendEvent("Invalid port number");
            return;
        }
        // ceate a new Server
        server = new Server(port, this);
        // and start it as a thread
        new ServerRunning().start();
        stopStart.setText("Stop");
        tPortNumber.setEditable(false);
    }

    // entry point to start the Server
    public static void main(String[] arg) {
        // start server default port 1500
        new ServerGUI(1500);
    }

    /*
	 * If the user click the X button to close the application
	 * I need to close the connection with the server to free the port
     */
    public void windowClosing(WindowEvent e) {
        // if my Server exist
        if (server != null) {
            try {
                server.stop();			// ask the server to close the conection
            } catch (Exception eClose) {
            }
            server = null;
        }
        // dispose the frame
        dispose();
        System.exit(0);
    }
    // I can ignore the other WindowListener method

    public void windowClosed(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    /*
	 * A thread to run the Server
     */
    class ServerRunning extends Thread {

        public void run() {
            server.start();         // should execute until if fails
            // the server failed
            stopStart.setText("Start");
            tPortNumber.setEditable(true);
            appendEvent("Server terminated\n");
            server = null;
        }
    }

}
