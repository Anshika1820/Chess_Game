import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Chess extends JFrame {
    private final int TILE_SIZE = 80;
    private final int BOARD_SIZE = 8;
    private Piece[][] board = new Piece[BOARD_SIZE][BOARD_SIZE];
    private int selectedRow = -1, selectedCol = -1;
    private boolean whiteTurn = true;

    public Chess() {
        setTitle("Chess Game - Java Swing");
        setSize(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE + 40);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initializeBoard();
        ChessBoardPanel boardPanel = new ChessBoardPanel();
        add(boardPanel);

        boardPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / TILE_SIZE;
                int col = e.getX() / TILE_SIZE;

                if (selectedRow == -1 && board[row][col] != null &&
                        board[row][col].isWhite == whiteTurn) {
                    selectedRow = row;
                    selectedCol = col;
                } else if (selectedRow != -1) {
                    if (isValidMove(selectedRow, selectedCol, row, col)) {
                        board[row][col] = board[selectedRow][selectedCol];
                        board[selectedRow][selectedCol] = null;
                        whiteTurn = !whiteTurn;
                    }
                    selectedRow = selectedCol = -1;
                }
                repaint();
            }
        });

        setVisible(true);
    }

    private void initializeBoard() {
        // Pawns
        for (int col = 0; col < BOARD_SIZE; col++) {
            board[1][col] = new Piece(false, "\u265F"); // Black pawn
            board[6][col] = new Piece(true, "\u2659");  // White pawn
        }

        // Rooks
        board[0][0] = board[0][7] = new Piece(false, "\u265C");
        board[7][0] = board[7][7] = new Piece(true, "\u2656");

        // Knights
        board[0][1] = board[0][6] = new Piece(false, "\u265E");
        board[7][1] = board[7][6] = new Piece(true, "\u2658");

        // Bishops
        board[0][2] = board[0][5] = new Piece(false, "\u265D");
        board[7][2] = board[7][5] = new Piece(true, "\u2657");

        // Queens
        board[0][3] = new Piece(false, "\u265B");
        board[7][3] = new Piece(true, "\u2655");

        // Kings
        board[0][4] = new Piece(false, "\u265A");
        board[7][4] = new Piece(true, "\u2654");
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow == toRow && fromCol == toCol) return false;
        if (toRow < 0 || toRow >= BOARD_SIZE || toCol < 0 || toCol >= BOARD_SIZE) return false;

        Piece source = board[fromRow][fromCol];
        Piece dest = board[toRow][toCol];
        if (dest != null && dest.isWhite == source.isWhite) return false;

        return true; // Basic move (no rule checking yet)
    }

    private class ChessBoardPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    boolean light = (row + col) % 2 == 0;
                    g2.setColor(light ? new Color(240, 217, 181) : new Color(181, 136, 99));
                    g2.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                    if (selectedRow == row && selectedCol == col) {
                        g2.setColor(Color.YELLOW);
                        g2.setStroke(new BasicStroke(3));
                        g2.drawRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }

                    Piece p = board[row][col];
                    if (p != null) {
                        g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 48));
                        g2.setColor(p.isWhite ? Color.WHITE : Color.BLACK);
                        g2.drawString(p.symbol, col * TILE_SIZE + 22, row * TILE_SIZE + 55);
                    }
                }
            }

            // Draw turn info
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            String turnText = whiteTurn ? "White's Turn (♙)" : "Black's Turn (♟)";
            g2.drawString(turnText, 10, BOARD_SIZE * TILE_SIZE + 30);
        }
    }

    class Piece {
        boolean isWhite;
        String symbol;

        public Piece(boolean isWhite, String symbol) {
            this.isWhite = isWhite;
            this.symbol = symbol;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Chess::new);
    }
}
