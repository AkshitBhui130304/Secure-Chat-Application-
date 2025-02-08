package FullProg;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class TicToeServ extends JFrame {
    private JButton[] buttons = new JButton[9]; // Buttons for the grid
    private boolean isServerTurn = true;       // Server starts first
    private DataOutputStream dout;            // To send data to the client
    private DataInputStream din;              // To receive data from the client
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public TicToeServ() {
        // Setup the UI for the Tic-Tac-Toe grid
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3, 5, 5));
        panel.setLocation(40,40);
        panel.setBackground(Color.GRAY);

        for (int i = 0; i < 9; i++) {
            final int index = i;
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 20));
            buttons[i].setBackground(Color.BLACK);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isServerTurn && buttons[index].getText().equals("")) {
                        buttons[index].setText("X");
                        buttons[index].setEnabled(false);
                        isServerTurn = false; // Switch to client turn
                        checkWin();
                        sendMove(index); // Notify the client about the move
                    }
                }
            });
            panel.add(buttons[i]);
        }

        add(panel);
        setTitle("Tic-Tac-Toe - Server");
        
        setSize(300, 300);
        
        // In TicToeServ constructor:
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (dout != null) {
                        dout.writeUTF("GAME_CLOSED");
                        dout.flush();
                    }
                    if (serverSocket != null && !serverSocket.isClosed()) {
                        serverSocket.close();
                    }
                    if (clientSocket != null && !clientSocket.isClosed()) {
                        clientSocket.close();
                    }} catch (IOException e1) {
                    
                    e1.printStackTrace();
                }  // Close the server when window is closed
            }
        });
       
  

        setLocation(40,40);
        //setLocationRelativeTo(null);
        setVisible(true);

        // Initialize the server
        initializeServer();
    }

    private void initializeServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(1001);
                System.out.println("Server is waiting for a client...");
                JOptionPane.showMessageDialog(this, "waiting for the companion...");
                clientSocket = serverSocket.accept();
                JOptionPane.showMessageDialog(this, "Game is live!");
                System.out.println("Client connected!");
                din = new DataInputStream(clientSocket.getInputStream());
                dout = new DataOutputStream(clientSocket.getOutputStream());
                listenForMoves(); // Start listening in another thread
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMove(int index) {
        try {
            dout.writeUTF("SERVER_MOVE");
            dout.writeInt(index); // Send the button index to the client
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenForMoves() {
        new Thread(() -> {
            try {
                while (true) {
                    
                        // Listen for incoming messages from the client
                        String message = din.readUTF();
                        
                        if ("GAME_CLOSED".equals(message)) {
                            // Handle client closing the game
                            JOptionPane.showMessageDialog(this,"Companion closed game");
                            System.out.println("The client has closed the game.");
                            // Perform any necessary cleanup, such as resetting the game state
                            break;  // Exit the loop and handle disconnection
                        }
            
                       
                        
            
                    else if ("CLIENT_MOVE".equals(message)) {
                    int index = din.readInt(); // Receive the move index from the client
                    SwingUtilities.invokeLater(() -> {
                        buttons[index].setText("O");
                        buttons[index].setEnabled(false);
                        isServerTurn = true; // Switch back to server turn
                        checkWin();
                    });
                }}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void checkWin() {
        // Check for win conditions in Tic-Tac-Toe
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
        isServerTurn = true;
    }

    
}
