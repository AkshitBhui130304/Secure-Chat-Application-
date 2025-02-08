package FullProg;

import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
import FullProg.TicToeServ;

class VigenereCipher {
    final String Cipherkey = "#@A10*v"; // Key for encryption

    // Encrypts the text (including emojis)
    public String stringEncryption(String text) {
        StringBuilder cipherText = new StringBuilder();
        String key = expandKey(text.length(), Cipherkey);

        // Process each character
        for (int i = 0; i < text.length(); i++) {
            char plainChar = text.charAt(i);
            char keyChar = key.charAt(i % key.length());

            // Encrypt using the Vigenère cipher with Unicode characters
            int encryptedChar = (plainChar + keyChar) % 65536; // Using 65536 for full Unicode support
            cipherText.append((char) encryptedChar);
        }

        return cipherText.toString();
    }

    // Decrypts the cipher text (including emojis)
    public String stringDecryption(String s) {
        StringBuilder plainText = new StringBuilder();
        String key = expandKey(s.length(), Cipherkey);

        // Process each character
        for (int i = 0; i < s.length(); i++) {
            char cipherChar = s.charAt(i);
            char keyChar = key.charAt(i % key.length());

            // Decrypt using the Vigenère cipher with Unicode characters
            int decryptedChar = (cipherChar - keyChar + 65536) % 65536; // Ensure positive result
            plainText.append((char) decryptedChar);
        }

        return plainText.toString();
    }

    // Expands the key to match the length of the text
    private static String expandKey(int length, String key) {
        StringBuilder expandedKey = new StringBuilder();
        for (int i = 0; i < length; i++) {
            expandedKey.append(key.charAt(i % key.length()));
        }
        return expandedKey.toString();
    }
}

class del_chats{
    JPanel panel;
    String time;
    int flag;
    del_chats(JPanel panel, String time,int flag){
        this.panel = panel;
        this.time = time;
        this.flag = flag;
    }
}

class Server_icons3 {
    Server_icons3(Server s) {
        JFrame frame = new JFrame("Button Example");
        frame.setSize(200, 90);
        frame.setBounds(200, 40, 200, 130);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(4,1)); // Use null layout for manual positioning

        // Button 1: Delete chats
        JButton b1 = new JButton("Delete chats");
        b1.setBackground(new Color(255, 255, 255));
        b1.setBounds(0, 0, 200, 30);
        frame.add(b1);

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                s.MessArea.removeAll();
                s.vertical.removeAll();
                s.MessArea.revalidate();
                s.MessArea.repaint();
                System.out.println("Chats cleared!");
            }
        });

        // Button 2: Block user
        JButton b2 = new JButton("Block user");
        b2.setBackground(new Color(255, 255, 255));
        b2.setBounds(0, 30, 200, 30);
        frame.add(b2);

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                s.text.setEnabled(false);
                s.text.setText("USER BLOCKED");
                s.emoji.setEnabled(false);
                s.send.setEnabled(false);
                s.isBlocked = true;
                System.out.println("User blocked!");
            }
        });

        // Button 3: Unblock user
        JButton b3 = new JButton("Unblock user");
        b3.setBackground(new Color(255, 255, 255));
        b3.setBounds(0, 60, 200, 30);
        frame.add(b3);

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                s.text.setEnabled(true);
                s.text.setText("");
                s.emoji.setEnabled(true);
                s.send.setEnabled(true);
                s.isBlocked = false;
                System.out.println("User unblocked!");
            }
        });
        
        JButton b4 = new JButton("Recover chats");
        b4.setBackground(new Color(255, 255, 255));
        b4.setBounds(0, 90, 200, 30);
        frame.add(b4);
        
        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            JFrame dframe = new JFrame("Recover Chats");
            dframe.setSize(400, 400);
            dframe.setLayout(new BorderLayout());
            dframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Box ver = Box.createVerticalBox();
            
            for (int i = 0; i < s.dchats.size(); i++) {
                del_chats dc = s.dchats.get(i);
                JPanel name = dc.panel; 
                name.setSize(3,3); // You might want to reconsider setting fixed sizes
                name.setBackground(new Color(7, 94, 84));
                String message = dc.time; 
                JLabel msg = new JLabel(message);
                msg.setSize(2,2); // You might want to reconsider setting fixed sizes
                msg.setForeground(Color.WHITE);
                name.add(msg,BorderLayout.SOUTH);
                JPanel container = new JPanel();
                container.setLayout(new BorderLayout());
                container.add(name,dc.flag==0?BorderLayout.LINE_END:BorderLayout.LINE_START);
                 
        
            
                // Add the container to the vertical box 
                ver.add(container); 
            
                // Add small vertical spacing between messages
                ver.add(Box.createVerticalStrut(3)); 
            }
            
            // Add the vertical box to the frame
            dframe.add(ver,BorderLayout.PAGE_START);
              

            
            
            dframe.setSize(400, 400);  // Adjust size as necessary
            dframe.setVisible(true);
            
            
            }
        });
            
        frame.setVisible(true);
       
    }
}
public class Server extends JFrame implements ActionListener {
    JTextField text;
    JPanel MessArea = new JPanel();
    Box vertical = Box.createVerticalBox();
    DataOutputStream dout;
    JLabel status;
    JPanel panel;
    static int count = 0;
    static String[] timings = new String[1000];
    JButton emoji;
    JButton send;
    int count1 = 0;
    ArrayList<Box> verBox = new ArrayList<>(); // Array to store vertical
    Boolean isLight = true;
    ArrayList<del_chats> dchats = new ArrayList<>();
    boolean isBlocked = false;
    
    // Constructor
    Server() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(7, 94, 84));
        panel.setBounds(0, 0, 450, 70);
        add(panel);

        // Add icon to close the window
        ImageIcon Icon1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i1 = Icon1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon icon1 = new ImageIcon(i1);
        JLabel image1 = new JLabel(icon1);
        image1.setBounds(5, 20, 25, 25);
        panel.add(image1);
        image1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        // User profile image
        ImageIcon Icon2 = new ImageIcon(ClassLoader.getSystemResource("icons/Akshit.png"));
        Image originalImage = Icon2.getImage();
        BufferedImage bufferedImage = new BufferedImage(
                originalImage.getWidth(null),
                originalImage.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();

        // Crop the profile image
        int cropX = 40;
        int cropY = 20;
        int cropWidth = 100;
        int cropHeight = 120;
        BufferedImage croppedImage = bufferedImage.getSubimage(cropX, cropY, cropWidth, cropHeight);
        Image scaledImage = croppedImage.getScaledInstance(50, 60, Image.SCALE_DEFAULT);
        ImageIcon icon2 = new ImageIcon(scaledImage);
        JLabel image2 = new JLabel(icon2);
        image2.setBounds(40, 5, 50, 60);
        panel.add(image2);
        image2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Open a new frame to display enlarged image
                JFrame imageFrame = new JFrame("Expanded Image");
                imageFrame.setSize(300, 400);
                imageFrame.setLocationRelativeTo(null);
                imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JLabel enlargedImageLabel = new JLabel(new ImageIcon(
                        croppedImage.getScaledInstance(300, 400, Image.SCALE_DEFAULT)));
                imageFrame.add(enlargedImageLabel);
                imageFrame.setVisible(true);
            }
        });

        // Other icons for video, call, and more
        ImageIcon Icon3 = new ImageIcon(ClassLoader.getSystemResource("icons/tictactoe.png"));
        Image i3 = Icon3.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon icon3 = new ImageIcon(i3);
        JLabel image3 = new JLabel(icon3);
        image3.setBounds(300, 20, 25, 25);
        panel.add(image3);
        image3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(!isBlocked)
             new TicToeServ();
        }});

        ImageIcon Icon4 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i4 = Icon4.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon icon4 = new ImageIcon(i4);
        JLabel image4 = new JLabel(icon4);
        image4.setBounds(340, 20, 25, 25);
        panel.add(image4);

        ImageIcon Icon5 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i5 = Icon5.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon icon5 = new ImageIcon(i5);
        JLabel image5 = new JLabel(icon5);
        image5.setBounds(380, 20, 25, 25);
        panel.add(image5);
        image5.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    // Relevant code for handling button clicks or socket communication
                    
                    new Server_icons3(Server.this);
                } catch (Exception ex) {
                    ex.printStackTrace(); // Log the exception for better debugging
                }
                
            }
        });

        ImageIcon Icon6 = new ImageIcon(ClassLoader.getSystemResource("icons/mode.png"));
        Image i6 = Icon6.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon icon6 = new ImageIcon(i6);
        JLabel image6 = new JLabel(icon6);
        image6.setBounds(420, 20, 25, 25);
        panel.add(image6);
        image6.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFrame mode = new JFrame("Mode");
                mode.setSize(30, 80);
                mode.setBounds(40, 40, 30, 80);
                mode.setLayout(new GridLayout(2, 1));
                mode.setLocationRelativeTo(null);
                mode.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JButton b1 = new JButton("Dark Mode");
                b1.setBounds(0, 0, 30, 40);
                b1.setBackground(Color.WHITE);
                mode.add(b1);
                b1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        isLight = false;
                        panel.setBackground(new Color(18, 18, 18));
                        MessArea.setBackground(new Color(34, 34, 34));
                        text.setBackground(new Color(50, 50, 50));
                        text.setForeground(Color.WHITE);
                        emoji.setBackground(new Color(50, 50, 50));
                        emoji.setForeground(Color.WHITE);
                        send.setBackground(new Color(50, 50, 50));
                        send.setForeground(Color.WHITE);

                        getContentPane().setBackground(new Color(34, 34, 34));
                        for (int i = 0; i < verBox.size(); i++) {
                            Box box = verBox.get(i);
                            for (Component component : box.getComponents()) {
                                if (component instanceof JPanel) {
                                    JPanel p = (JPanel) component;
                                    p.setBackground(new Color(34, 34, 34));
                                    for (Component innerComp : p.getComponents()) {
                                        if (innerComp instanceof JLabel) {
                                            JLabel label = (JLabel) innerComp;
                                            label.setForeground(Color.WHITE); // Set text color to white
                                        }
                                    }
                                }
                            }
                        }

                    }
                });
                JButton b2 = new JButton("Light Mode");
                b2.setBounds(0, 0, 30, 40);
                b2.setBackground(Color.WHITE);
                mode.add(b2);
                b2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        isLight = true;
                        panel.setBackground(new Color(7, 94, 84));
                        MessArea.setBackground(new Color(238, 238, 238));
                        emoji.setBackground(new Color(7, 94, 84));
                        emoji.setForeground(Color.WHITE);
                        send.setBackground(new Color(7, 94, 84));

                        send.setForeground(Color.WHITE);
                        text.setBackground(new Color(238, 238, 238));
                        text.setForeground(Color.BLACK);
                        getContentPane().setBackground(Color.WHITE);

                        for (int i = 0; i < verBox.size(); i++) {
                            Box box = verBox.get(i);
                            for (Component component : box.getComponents()) {
                                if (component instanceof JPanel) {
                                    JPanel p = (JPanel) component;
                                    p.setBackground(new Color(238, 238, 238));
                                    for (Component innerComp : p.getComponents()) {
                                        if (innerComp instanceof JLabel) {
                                            JLabel label = (JLabel) innerComp;
                                            label.setForeground(Color.BLACK); // Set text color to white
                                        }
                                    }
                                }
                            }
                        }
                    }

                });
                mode.setVisible(true);
            }
        });

        // Add user name and status
        JLabel name = new JLabel("Akshit");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 16));
        panel.add(name);

        status = new JLabel("Waiting");
        status.setBounds(110, 35, 100, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 10));
        panel.add(status);

        // Message area to display chat

        MessArea.setBounds(5, 75, 425, 570);
        MessArea.setBackground(new Color(238, 238, 238));
        add(MessArea);

        // Text field to type the message
        emoji = new JButton("Emoji");
        emoji.setBounds(5, 655, 90, 40);
        emoji.setBackground(new Color(7, 94, 84));
        emoji.setForeground(Color.WHITE);
        emoji.addActionListener(this);
        add(emoji);

        text = new JTextField();
        text.setBounds(100, 655, 240, 40);
        text.setBackground(new Color(238, 238, 238));
        add(text);

        // Send button to send the message
        send = new JButton("Send");
        send.setBounds(350, 655, 95, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        add(send);

        // Set frame properties
        setSize(450, 700);
        setLayout(null);
        setLocation(100, 50);
        setUndecorated(true);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
        MessArea.setLayout(new BorderLayout());
    }

    // Action performed when the Send button is clicked
    public void actionPerformed(ActionEvent ae) {
        String comm = ae.getActionCommand();
        if (comm.equals("Send")) {

            try {
                if(text.getText().equals("")) {
                    return;
                }
                String msgout = text.getText();
                
                // Create the message label
                Format(msgout, 0);

                // Update MessArea
                VigenereCipher vc = new VigenereCipher();
                msgout = vc.stringEncryption(msgout);
                System.out.println("Encrypted Plain Text is: " + msgout);
                 dout.writeUTF(msgout);
                text.setText("");
                repaint();
                invalidate(); // Ensures the panel is correctly updated
                validate();

            } catch (Exception e1) {
                // e1.printStackTrace();
            }
        }

        else if (comm.equals("Emoji")) {
            JFrame emojiFrame = new JFrame("Emoji");
            emojiFrame.setSize(5, 200);

            emojiFrame.setLocationRelativeTo(null);
            emojiFrame.setBounds(5, 550, 5, 200);
            // Set GridLayout with one row and as many columns as emojis
            emojiFrame.setLayout(new GridLayout(5, 1, 1, 1)); // 1 row, 5 columns, 10px spacing

            JLabel[] emojiLabels = new JLabel[5];
            emojiLabels[0] = new JLabel("😀");
            emojiLabels[1] = new JLabel("😂");
            emojiLabels[2] = new JLabel("😍");
            emojiLabels[3] = new JLabel("😡");
            emojiLabels[4] = new JLabel("😎");

            for (int i = 0; i < emojiLabels.length; i++) {
                emojiLabels[i].setFont(new Font("SAN_SERIF", Font.PLAIN, 30));
                emojiLabels[i].setForeground(Color.BLACK);

                // Capture the emoji text in a final variable
                final String emoji = emojiLabels[i].getText();

                emojiLabels[i].addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        text.setText(text.getText() + emoji); // Use the captured emoji text
                    }
                });

                emojiFrame.add(emojiLabels[i]);
            }

            emojiFrame.setVisible(true);

            emojiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }

    // Format message display
    void Format(String msgout, int flag) {
        JLabel output = new JLabel(msgout);
        output.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        output.setForeground(Color.WHITE);

        // Create the timestamp label
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("hh:mm");
        String timestamp = s.format(cal.getTime());
        timings[count++] = timestamp;
        JLabel time = new JLabel(s.format(cal.getTime()));
        time.setForeground(Color.BLACK);
        time.setFont(new Font("SAN_SERIF", Font.PLAIN, 8)); // Optional: smaller font for timestamp

        // Create a panel for the message and timestamp
        JPanel out = new JPanel();
        out.setLayout(new BorderLayout());
        out.add(output, BorderLayout.CENTER); // Add message in the center
        out.setBackground(isLight ? new Color(7, 94, 84) : new Color(7, 84, 84));
        JPanel right = new JPanel(new BorderLayout());
        JPanel rightTime = new JPanel(new BorderLayout());
        if (isLight == false) {

            right.setBackground(new Color(34, 34, 34));
            rightTime.setBackground(new Color(34, 34, 34));
            time.setForeground(Color.WHITE);
        }

        // Wrap everything in the right panel for alignment
        if (flag == 0) {
            right.add(out, BorderLayout.LINE_END); // Align to the right
            rightTime.add(time, BorderLayout.LINE_END);
        } else {
            right.add(out, BorderLayout.LINE_START); // Align to the left
            rightTime.add(time, BorderLayout.LINE_START);
        }

        // Add to the vertical Box
        vertical.add(right);
        
        vertical.add(rightTime);
        right.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                
                 
                        dchats.add(new del_chats(right,s.format(cal.getTime()),flag));
                        MessArea.remove(right);
                        MessArea.remove(rightTime);
                        vertical.remove(right);
                        vertical.remove(rightTime);
                        vertical.revalidate();
                        vertical.repaint();
                        MessArea.repaint();
                    
                }
                 
                }
            });
        
        verBox.add(vertical);
        vertical.add(Box.createVerticalStrut(15)); // Optional: add space between messages, not between message and
                                               // timestamp
        MessArea.add(vertical, BorderLayout.PAGE_START);
          
    }

    // Main method to run the server
    public static void main(String[] args) {
        Server server = new Server();

        
        try {
            ServerSocket skt = new ServerSocket(6001);
            while (true) {
                Socket s = skt.accept();
                server.status.setText("Online");
                server.panel.repaint();
        
                DataInputStream din = new DataInputStream(s.getInputStream());
                server.dout = new DataOutputStream(s.getOutputStream());
        
                // Start a new thread to read messages from the client
                while (true) {
                    VigenereCipher vc = new VigenereCipher();
        
                    String message = din.readUTF();
                    String msgout = vc.stringDecryption(message);
                    System.out.println("Decrypted Plain Text is: " + msgout);
        
                    server.Format(msgout, 1);
                    server.MessArea.updateUI();
                    server.repaint();
                    server.validate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }
}
