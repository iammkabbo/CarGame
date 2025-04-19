import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CarGame extends JPanel implements ActionListener, KeyListener {
    // Game settings
    final int WIDTH = 400;
    final int HEIGHT = 600;
    final int PLAYER_WIDTH = 50;
    final int PLAYER_HEIGHT = 100;

    // Player
    int playerX = WIDTH / 2 - PLAYER_WIDTH / 2;
    int playerY = HEIGHT - PLAYER_HEIGHT - 20;

    // Obstacles
    ArrayList<Rectangle> obstacles = new ArrayList<>();
    Random random = new Random();

    // Game State
    boolean gameOver = false;
    Timer timer;
    JButton restartButton;

    public CarGame() {
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        setLayout(null); // Manual layout for restart button

        // Timer
        timer = new Timer(15, this);
        timer.start();

        // Add initial obstacle
        addObstacle();

        // Restart button setup
        restartButton = new JButton("Restart");
        restartButton.setBounds(WIDTH / 2 - 50, HEIGHT / 2, 100, 40);
        restartButton.setVisible(false);
        restartButton.addActionListener(e -> restartGame());
        add(restartButton);
    }

    private void addObstacle() {
        int x = random.nextInt(WIDTH - PLAYER_WIDTH);
        obstacles.add(new Rectangle(x, 0, PLAYER_WIDTH, PLAYER_HEIGHT));
    }

    private void restartGame() {
        playerX = WIDTH / 2 - PLAYER_WIDTH / 2;
        obstacles.clear();
        addObstacle();
        gameOver = false;
        restartButton.setVisible(false);
        timer.start();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Road stripes
        g.setColor(Color.WHITE);
        for (int i = 0; i < HEIGHT; i += 40) {
            g.fillRect(WIDTH / 2 - 5, i, 10, 20);
        }

        // Player car
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);

        // Obstacles
        g.setColor(Color.RED);
        for (Rectangle obs : obstacles) {
            g.fillRect(obs.x, obs.y, obs.width, obs.height);
        }

        // Game Over Text
        if (gameOver) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over!", WIDTH / 2 - 100, HEIGHT / 2 - 40);
            restartButton.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            Iterator<Rectangle> it = obstacles.iterator();
            while (it.hasNext()) {
                Rectangle obs = it.next();
                obs.y += 5;

                if (obs.y > HEIGHT) {
                    it.remove();
                }

                if (obs.intersects(new Rectangle(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT))) {
                    gameOver = true;
                    timer.stop();
                }
            }

            if (random.nextInt(100) < 3) {
                addObstacle();
            }

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT && playerX > 0) {
                playerX -= 20;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerX < WIDTH - PLAYER_WIDTH) {
                playerX += 20;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Car Dodging Game");
        CarGame game = new CarGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
