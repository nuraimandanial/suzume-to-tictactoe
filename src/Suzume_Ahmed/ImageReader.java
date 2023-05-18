package Suzume_Ahmed;

import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImageReader extends JPanel {

    //Creating Images
    private Image createImageWithText() {
        BufferedImage bufferedImage = new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();

        //Adding elements to my space
        g.drawString("www.tutorialspoint.com", 20,20);
        g.drawString("www.tutorialspoint.com", 20,40);
        g.drawString("www.tutorialspoint.com", 20,60);
        g.drawString("www.tutorialspoint.com", 20,80);
        g.drawString("www.tutorialspoint.com", 20,100);

        return bufferedImage;
    }

    @Override
    //Draws the image produced
    //Questions: how to scroll down?
    public void paint(Graphics g) {
        Image img = null;
        try {
            img = ImageIO.read(new File("C:\\Users\\A7M1ST\\Desktop\\Java\\Java\\src\\Suzume\\map\\image 1.png"));
            img = img.getScaledInstance(100*3, 200*3, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ;
        g.drawImage(img, 200, 200, this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        ImageReader image = new ImageReader(); //supposedly created an image
        frame.getContentPane().add(image);

        //I already know this
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setVisible(true);
    }
}
