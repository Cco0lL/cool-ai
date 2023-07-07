package cool.kolya;

import cool.kolya.graph.layer.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class TestApp extends JFrame implements Runnable {

    private final int w = 28;
    private final int h = 28;
    private final int scale = 32;

    private int mousePressed = 0;
    private int mx = 0;
    private int my = 0;
    private double[][] colors = new double[w][h];

    private BufferedImage img = new BufferedImage(w * scale + 200, h * scale, BufferedImage.TYPE_INT_RGB);
    private BufferedImage pimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

    private NeuralNetwork<BufferedImage, Integer> nn;

    public TestApp(NeuralNetwork<BufferedImage, Integer> nn) {
        this.nn = nn;
        setSize(w * scale + 200 + 16, h * scale + 38);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(50, 50);
        setVisible(true);
        add(new JLabel(new ImageIcon(img)));
        initListeners();
    }

    @Override
    public void run() {
        while (true) {
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (mousePressed != 0) {
                    double dist = (i - mx) * (i - mx) + (j - my) * (j - my);
                    if (dist < 1) dist = 1;
                    dist *= dist;
                    if (mousePressed == 1) colors[i][j] += 0.1 / dist;
                    else colors[i][j] -= 0.1 / dist;
                    if (colors[i][j] > 1) colors[i][j] = 1;
                    if (colors[i][j] < 0) colors[i][j] = 0;
                }
                int color = (int) (colors[i][j] * 255);
                //r == g == b
                color = (color << 16) | (color << 8) | color;
                pimg.setRGB(i, j, color);
            }
        }

        int result = nn.push(pimg);
        double[] outputActivations = nn.getGraph().outputActivations();
        Graphics2D ig = (Graphics2D) img.getGraphics();
        ig.drawImage(pimg, 0, 0, w * scale, h * scale, this);
        ig.setColor(Color.lightGray);
        ig.fillRect(w * scale + 1, 0, 200, h * scale);
        ig.setFont(new Font("TimesRoman", Font.BOLD, 48));
        for (int i = 0; i < 10; i++) {
            Color color = result == i ? Color.RED : Color.GRAY;
            ig.setColor(color);
            ig.drawString(i + ":", w * scale + 20, i * w * scale / 15 + 150);
            Color rectColor = new Color(0, (float)outputActivations[i], 0);
            int rectWidth = (int)(outputActivations[i] * 100);
            ig.setColor(rectColor);
            ig.fillRect(w * scale + 70, i * w * scale / 15 + 122, rectWidth, 30);
        }
        g.drawImage(img, 8, 30, w * scale + 200, h * scale, this);
    }

    private void initListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressed = 1;
                if (e.getButton() == 3) mousePressed = 2;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed = 0;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mx = e.getX() / scale;
                my = e.getY() / scale;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mx = e.getX() / scale;
                my = e.getY() / scale;
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    colors = new double[w][h];
                }
            }
        });
    }
}
