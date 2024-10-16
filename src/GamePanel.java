import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel implements MouseMotionListener {

    Random random = new Random();
    String circleCollision;
    final int PLAYER_WIDTH = 30;
    final int PLAYER_HEIGHT = 30;

    int deltaDirection;
    int speed;
    int circleYCords;
    int circleXCords;
    int playerX;
    int playerY;
    boolean gameOver;
    boolean isReachedRightEnd;
    boolean isReachedLeftEnd;
    boolean isGameRunning;
    boolean isReachedBottom;
    boolean isReachedTop = true;
    Ellipse2D ellipse;
    String[] sides = {"TOP", "BOTTOM", "RIGHT", "LEFT"};
    JButton button;
    int level;
    Window window;
    int paintCount = 120;
    int minutes = 2;
    int seconds = 0;
    Thread countThread;
    int lvlSpeed;
    boolean isFruitEaten;
    Graphics2D graphic2d;
    Random randomObj;
    double fruitCordsX;
    double fruitCordsY;
    Shape starObj;
    int score = -1;

    GamePanel(Window window) {
        this.window = window;
        isReachedLeftEnd = true;
        button = new JButton();
        randomObj = new Random();
        circleXCords = Window.WIDTH / 2;
        level = 1;
        circleYCords = 75;
//        System.out.println(circleXCords);
        ellipse = new Ellipse2D.Float(circleXCords, circleYCords, 50, 50);
        speed = 5;
        deltaDirection = 5;
        circleCollision = "BOTTOM";
        playerX = 400 - PLAYER_WIDTH;
        playerY = 300 - PLAYER_HEIGHT;
        lvlSpeed = 35;
        isFruitEaten = true;
        isGameRunning = true;
        addMouseMotionListener(this);
    }




    public void ballMovement() {
        if (isReachedBottom) {
            circleYCords = circleYCords - speed;// Going UP
            if (circleYCords <= 65) {
                isReachedBottom = false;
                isReachedTop = true;
                changeSpeed(); // method call
            }
            if (isReachedRightEnd) {
                circleXCords = circleXCords - deltaDirection;
            }
            if (isReachedLeftEnd) {
                circleXCords = circleXCords + deltaDirection;
            }
        }
        if (isReachedTop) {
            circleYCords = circleYCords + speed;   // GOING Down
            if (circleYCords >= 450) {
                isReachedBottom = true;
                isReachedTop = false;
                changeSpeed();
            }
            if (isReachedRightEnd) {
                circleXCords = circleXCords - deltaDirection;
            }
            if (isReachedLeftEnd) {
                circleXCords = circleXCords + deltaDirection;
            }
        }
        if (circleXCords >= 700) {
            isReachedRightEnd = true;
            isReachedLeftEnd = false;
            changeSpeed();
        }
        if (isReachedRightEnd) {
            circleXCords = circleXCords - deltaDirection;
        }
        if (circleXCords <= 30) {
            isReachedLeftEnd = true;
            isReachedRightEnd = false;
            changeSpeed();
        }
        if (isReachedLeftEnd) {
            circleXCords = circleXCords + deltaDirection;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        graphic2d = (Graphics2D) g;
        g.setColor(Color.green);
        g.setFont(new Font("ARIAL", Font.BOLD, 30));
        g.drawString("Score: " + score, 100, 30);
        g.drawString("timer: " + minutes + ":" + seconds, 600, 30);
        g.drawString("LEVEL " + level, Window.WIDTH / 2 - 50, 30);
        ellipse.setFrame(circleXCords, circleYCords, 50, 50);
        gameOver = ellipse.intersects(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);


        //g.drawLine(20, 500, circleXCords, circleYCords);

        setBackground(Color.black);
        //sides
        g.setColor(Color.red);
        g.fillRect(20, 50, 750, 15); //TOP
        g.fillRect(20, 500, 750, 15);//BOTTOM
        g.fillRect(755, 65, 15, 450);//RIGHT
        g.fillRect(20, 65, 15, 450);//LEFT

        //will generate a fruit in a random area
        fruitGenerator(g);

        //ball movement
        if (!gameOver) {
            g.setColor(Color.red);
            g.fillOval(circleXCords, circleYCords, 50, 50);
            ballMovement();

            //player
            g.setColor(Color.green);
            g.fillRect(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);
            //System.out.println(playerX + ", " + playerY);

        }
        //Game over
        else {
            isGameRunning = false;
            countThread.interrupt();
            window.gameOver();
        }

        //System.out.println("Speed:" + speed);
        //System.out.println(isReachedLeftEnd + " " + isReachedRightEnd);


    }

    // this is a method definition
    public void changeSpeed() {
        speed = random.nextInt(10, 15);
        deltaDirection = random.nextInt(5, 10);
    }


    public void startGame() {
        isGameRunning = true;
        gameOver = false;
        startCountdown();

        while (isGameRunning) {
            repaint();
            try {
                Thread.sleep(lvlSpeed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void startCountdown() {
        countThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!countThread.isInterrupted()) {

//                    System.out.println("minutes "+minutes+" seconds "+seconds);
                    if (minutes <= 0 && seconds <= 0) {
                        level = level + 1;
                        minutes = 2;
                        seconds = 0;
                        if(lvlSpeed <= 5) {
                            // TODO: User wins the gmae: wins permanent trophy and its stored in file (hdd)
                            // FILE HANDLING IN JAVA
                        }
                        else{
                            lvlSpeed = lvlSpeed - 1;
                        }
                    }
                    if (seconds % 60 == 0) {
                        minutes = minutes - 1;
                        seconds = 59;
                    }


                    else {
                        seconds = seconds - 1;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        countThread.start();
    }

    public void fruitGenerator(Graphics g){
        g.setColor(Color.YELLOW);
        System.out.println(fruitCordsX + "," + fruitCordsY);
        if(isFruitEaten){
            fruitCordsX = randomObj.nextInt(50,750);
            fruitCordsY = randomObj.nextInt(80, 480);
            starObj = createStar(fruitCordsX, fruitCordsY, 10, 20, 10, 50);
            score += 1;
            isFruitEaten = false;
        }
        if(starObj.intersects(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT)){
            isFruitEaten = true;
        }
        graphic2d.fill(starObj);
    }

    private static Shape createStar(double centerX, double centerY,
                                    double innerRadius, double outerRadius, int numRays,
                                    double startAngleRad)
    {
        Path2D path = new Path2D.Double();
        double deltaAngleRad = Math.PI / numRays;
        for (int i = 0; i < numRays * 2; i++)
        {
            double angleRad = startAngleRad + i * deltaAngleRad;
            double ca = Math.cos(angleRad);
            double sa = Math.sin(angleRad);
            double relX = ca;
            double relY = sa;
            if ((i & 1) == 0)
            {
                relX *= outerRadius;
                relY *= outerRadius;
            }
            else
            {
                relX *= innerRadius;
                relY *= innerRadius;
            }
            if (i == 0)
            {
                path.moveTo(centerX + relX, centerY + relY);
            }
            else
            {
                path.lineTo(centerX + relX, centerY + relY);
            }
        }
        path.closePath();
        return path;
    }


    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getY() >= 65 && e.getY() <= 470) {
            playerY = e.getY();

        }
        if (e.getX() <= 725 && e.getX() >= 35) {
            playerX = e.getX();
        }
    }
}
