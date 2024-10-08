import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Window extends JFrame {

    public static int WIDTH = 800;
    public static int HEIGHT = 600;
    GamePanel gamePanel;
    JPanel gameOverPanel;
    JButton restartButton;
    JLabel jLabel;
    BoxLayout layout;
    Window windowObj;


    Window() {
        //the panels
        windowObj = this;
        gameOverPanel = new JPanel();
        gamePanel = new GamePanel(windowObj);

        jLabel = new JLabel();
        layout = new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS);
        restartButton = new JButton();
        restartButton.setText("restart");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel = new GamePanel(windowObj); // created a new object gamepanel and named it the same name
                display(gamePanel);
                setTimeout(() -> gamePanel.startGame(), 100);
            }
        });
        gameOverPanel.setBackground(Color.black);
        gameOverPanel.setBorder(new EmptyBorder(new Insets(250, 250, 250, 200)));
        gameOverPanel.setLayout(layout);
        jLabel.setBackground(Color.GREEN);
        jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        jLabel.setPreferredSize(new Dimension(330, 100));
        jLabel.setFont(new Font("arial", Font.BOLD, 50));
        jLabel.setForeground(Color.red);
        jLabel.setText("GAME OVER");
        gameOverPanel.add(jLabel);
        gameOverPanel.add(restartButton);

        add(gameOverPanel);
        add(gamePanel);
        setSize(800, 600);
        setBackground(Color.red);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        gamePanel.startGame();
    }

    public void gameOver(){
        display(gameOverPanel);
    }

    public static void main(String[] args) {
        new Window();
    }



    public void display(JPanel a){

        getContentPane().removeAll();
        getContentPane().add(a);
        repaint();
        printAll(getGraphics());

    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }
}
