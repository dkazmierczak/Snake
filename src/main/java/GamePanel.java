import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


/**
 * The class Game panel extends J panel implements action listener, mouse listener
 */
public class GamePanel extends JPanel implements ActionListener, MouseListener {

    static final int SCREEN_WIDTH = 1300;
    static final int SCREEN_HEIGHT = 750;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    static final int DELAY = 175;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    private FontMetrics playAgainMetrics = getFontMetrics(new Font("Ink Free",Font.BOLD, 40));
    private Color playColor = new Color(0, 153, 255);

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.addMouseListener(this);
        startGame();
    }


    /**
     *
     * Start game
     *
     */
    public void startGame() {

        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }


    /**
     *
     * Paint component
     *
     * @param g  the g
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        draw(g);
    }


    /**
     *
     * Draw
     *
     * @param g  the g
     */
    public void draw(Graphics g) {


        if(running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < bodyParts; i++) {
                if(i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free",Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }


    /**
     *
     * New apple
     *
     */
    public void newApple(){

        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }


    /**
     *
     * Move
     *
     */
    public void move(){

        for(int i = bodyParts; i > 0; i--) {
            if (running) {
                x[i] = x[i - 1];
                y[i] = y[i - 1];
            }
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }


    /**
     *
     * Check apple
     *
     */
    public void checkApple() {

        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }


    /**
     *
     * Check collisions
     *
     */
    public void checkCollisions() {

        //checks if head collides with body
        for(int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        //check if head touches left border
        if(x[0] < 0) {
            running = false;
        }
        //check if head touches right border
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //check if head touches top border
        if(y[0] < 0) {
            running = false;
        }
        //check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }


    /**
     *
     * Game over
     *
     * @param g  the g
     */
    public void gameOver(Graphics g) {

        //Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        //Play again button
        g.setColor(playColor);
        g.setFont(new Font("Ink Free",Font.BOLD, 40));
        g.drawString("Play Again", (SCREEN_WIDTH - playAgainMetrics.stringWidth("Play Again")) / 2, SCREEN_HEIGHT / 3 * 2);
    }


    /**
     *
     * Action performed
     *
     * @param e  the e
     */
    public void actionPerformed(ActionEvent e) {


        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }


    /**
     *
     * Mouse clicked
     *
     * @param e  the e
     */
    public void mouseClicked(MouseEvent e) {

        int startDrawingPlayAgainX = (SCREEN_WIDTH - playAgainMetrics.stringWidth("Play Again")) / 2;
        int startDrawingPlayAgainY = SCREEN_HEIGHT / 3 * 2;

        if (!running) {
            if (e.getX() >= startDrawingPlayAgainX
                    && e.getX() <= startDrawingPlayAgainX + playAgainMetrics.stringWidth("Play Again")
                    && e.getY() <= startDrawingPlayAgainY + playAgainMetrics.getHeight()
                    && e.getY() >= startDrawingPlayAgainY - playAgainMetrics.getHeight()
            ) {
                GameFrame frame = new GameFrame();
            }
        }
    }


    /**
     *
     * Mouse pressed
     *
     * @param e  the e
     */
    public void mousePressed(MouseEvent e) {


    }


    /**
     *
     * Mouse released
     *
     * @param e  the e
     */
    public void mouseReleased(MouseEvent e) {


    }


    /**
     *
     * Mouse entered
     *
     * @param e  the e
     */
    public void mouseEntered(MouseEvent e) {


    }


    /**
     *
     * Mouse exited
     *
     * @param e  the e
     */
    public void mouseExited(MouseEvent e) {


    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override

/**
 *
 * Key pressed
 *
 * @param e  the e
 */
        public void keyPressed(KeyEvent e) {

            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
