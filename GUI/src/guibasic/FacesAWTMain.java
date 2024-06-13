package guibasic;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FacesAWTMain {

    public static void main(String[] args) {
        new FacesAWTMain();
    }

    FacesAWTMain() {
        FaceFrame f = new FaceFrame();
        f.setSize(800, 800);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setVisible(true);
    }

    // インナークラスを定義
    class FaceFrame extends Frame {

        FaceFrame() {
        }

        public void paint(Graphics g) {
            super.paint(g);

            // 3x3の顔を描画
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int mood = (i == 0) ? 1 : (i == 1) ? 0 : 2; // 1列目: 笑顔、2列目: 普通の顔、3列目: 怒っている顔
                    FaceObj fobj = new FaceObj(50 + j * 250, 50 + i * 250, mood);
                    fobj.drawFace(g);
                }
            }
        }
    }

    // FaceObjクラスを定義
    private class FaceObj {
        private int xStart;
        private int yStart;
        private int w = 200; // 顔の幅
        private int h = 200; // 顔の高さ
        private int mood;

        public FaceObj(int x, int y, int mood) {
            xStart = x;
            yStart = y;
            this.mood = mood;
        }

        public void drawFace(Graphics g) {
            drawRim(g);
            drawBrow(g);
            drawEye(g);
            drawNose(g);
            drawMouth(g);
        }

        private void drawRim(Graphics g) {
            g.setColor(Color.BLACK);
            g.drawRect(xStart, yStart, w, h);
        }

        private void drawBrow(Graphics g) {
            int xMiddle = xStart + w / 2;
            int yMiddle = yStart + h / 4;
            int browWidth = w / 4;
            int browHeight = 10;

            g.setColor(Color.BLACK);
            if (mood == 1) { // happy
                g.drawLine(xMiddle - browWidth - 30, yMiddle - 10, xMiddle - browWidth + 20, yMiddle - browHeight - 10); // 左眉
                g.drawLine(xMiddle + browWidth - 20, yMiddle - browHeight - 10, xMiddle + browWidth + 30, yMiddle - 10); // 右眉
            } else if (mood == 2) { // angry
                g.drawLine(xMiddle - browWidth - 30, yMiddle - browHeight + 10, xMiddle - browWidth + 20, yMiddle + 10); // 左眉
                g.drawLine(xMiddle + browWidth - 20, yMiddle + 10, xMiddle + browWidth + 30, yMiddle - browHeight + 10); // 右眉
            } else { // neutral
                g.drawLine(xMiddle - browWidth - 30, yMiddle, xMiddle - browWidth + 20, yMiddle - browHeight); // 左眉
                g.drawLine(xMiddle + browWidth - 20, yMiddle - browHeight, xMiddle + browWidth + 30, yMiddle); // 右眉
            }
        }

        private void drawNose(Graphics g) {
            int xMiddle = xStart + w / 2;
            int yMiddle = yStart + h / 2;
            int noseWidth = w / 10;
            int noseHeight = h / 6;

            g.setColor(new Color(255, 200, 200));
            g.fillOval(xMiddle - noseWidth / 2, yMiddle - noseHeight / 2, noseWidth, noseHeight); // 鼻を描画
        }

        private void drawEye(Graphics g) {
            int xMiddle = xStart + w / 2;
            int yMiddle = yStart + h / 3;
            int eyeRadius = 20;

            g.setColor(Color.WHITE);
            g.fillOval(xMiddle - w / 4 - eyeRadius / 2, yMiddle - eyeRadius / 2, eyeRadius, eyeRadius);
            g.fillOval(xMiddle + w / 4 - eyeRadius / 2, yMiddle - eyeRadius / 2, eyeRadius, eyeRadius);

            g.setColor(Color.BLACK);
            g.fillOval(xMiddle - w / 4 - eyeRadius / 4, yMiddle - eyeRadius / 4, eyeRadius / 2, eyeRadius / 2);
            g.fillOval(xMiddle + w / 4 - eyeRadius / 4, yMiddle - eyeRadius / 4, eyeRadius / 2, eyeRadius / 2);
        }

        private void drawMouth(Graphics g) {
            int xMiddle = xStart + w / 2;
            int yMiddle = yStart + h - 50;
            int mouthWidth = 100;
            int mouthHeight = 20;

            g.setColor(Color.RED);
            if (mood == 1) { // happy
                g.drawArc(xMiddle - mouthWidth / 2, yMiddle - 20, mouthWidth, mouthHeight, 0, -180);
            } else if (mood == 2) { // angry
                g.drawArc(xMiddle - mouthWidth / 2, yMiddle, mouthWidth, mouthHeight, 0, 180);
            } else { // neutral
                g.drawLine(xMiddle - mouthWidth / 2, yMiddle, xMiddle + mouthWidth / 2, yMiddle);
            }
        }
    }
}
