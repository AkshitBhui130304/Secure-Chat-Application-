package FullProg;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class TicToeClient extends JFrame {
    private JButton[] buttons = new JButton[9]; // Buttons for the grid
    private boolean isClientTurn = false;      // Client starts second
    private DataOutputStream dout;            // To send data to the server
    private DataInputStream din;              // To receive data from the server
    private Socket socket;

    public TicToeClient() {
        // Setup the UI for the Tic-Tac-Toe grid
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3, 5, 5));
        panel.setBackground(Color.GRAY);

        for (int i = 0; i < 9; i++) {
            final int index = i;
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 20));
            buttons[i].setBackground(Color.BLACK);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].addActionListener(e -> {
                if (isClientTurn && buttons[index].getText().equals("")) {
                    buttons[index].setText("O");
                    buttons[index].setEnabled(false);
                    isClientTurn = false; // Switch to server's turn
                    checkWin();
                    sendMove(index); // Notify the server about the move
                }
            });
            panel.add(buttons[i]);
        }

        add(panel);
        setTitle("Tic-Tac-Toe - Client");
        setSize(300, 300);
        setLocation(700,40);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (dout != null) 
                        dout.writeUTF("GAME_CLOSED");
                    dout.flush();
                    socket.close();
                } catch (IOException e1) {
                    
                    e1.printStackTrace();
                }  // Close the server when window is closed
            }
        });
        //setLocationRelativeTo(null);
        setVisible(true);

        // Initialize the client connection
        initializeClient();
    }

    private void initializeClient() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 1001); // Connect to the server
                System.out.println("Connected to the server!");
                din = new DataInputStream(socket.getInputStream());
                dout = new DataOutputStream(socket.getOutputStream());
                listenForMoves(); // Start listening for moves
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Unable to connect to the server!");
                this.dispose();
                e.printStackTrace();
            }
        }).start();
    }
    
    private void sendMove(int index) {
        try {
            dout.writeUTF("CLIENT_MOVE");
            dout.writeInt(index); // Send the button index to the server
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error sending move to the server!");
            e.printStackTrace();
        }
    }
    
    private void listenForMoves() {
        new Thread(() -> {
            try {
                while (true) {
                    // Check for any incoming message, like game closed or server move
                    String message = din.readUTF();
    
                    if ("GAME_CLOSED".equals(message)) {
                        // Handle client closing the game
                        JOptionPane.showMessageDialog(this,"Companion closed the game");
                        System.out.println("The client has closed the game.");
                        break;  // Exit the loop and handle disconnection
                    } else if ("SERVER_MOVE".equals(message)) {
                        // Read the move index from the server
                        int index = din.readInt(); // Receive the move index from the server
                        SwingUtilities.invokeLater(() -> {
                            // Update the button with "X"
                            buttons[index].setText("X");
                            buttons[index].setEnabled(false);  // Disable the button after the move
                            isClientTurn = true; // Switch to client's turn
                            checkWin();  // Check for winner or draw
                        });
                    }
                }
            } catch (IOException e) {
                //JOptionPane.showMessageDialog(this, "Server disconnected!");
                e.printStackTrace();
            }
        }).start();
    }
    
    

    private void checkWin() {
        int[][] winPatterns = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}             // Diagonals
        };

        for (int[] pattern : winPatterns) {
            if (!buttons[pattern[0]].getText().isEmpty() &&
                buttons[pattern[0]].getText().equals(buttons[pattern[1]].getText()) &&
                buttons[pattern[1]].getText().equals(buttons[pattern[2]].getText())) {
                String winner = buttons[pattern[0]].getText();
                JOptionPane.showMessageDialog(this, winner + " wins!");
                resetGame();
                return;
            }
        }

        // Check for draw
        boolean draw = true;
        for (JButton button : buttons) {
            if (button.getText().isEmpty()) {
                draw = false;
                break;
            }
        }
        if (draw) {
            JOptionPane.showMessageDialog(this, "It's a draw!");
            resetGame();
        }
    }

    private void resetGame() {
        for (JButton button : buttons) {
            button.setText("");
            button.setEnabled(true);
        }
        isClientTurn = false;
    }

    
}
