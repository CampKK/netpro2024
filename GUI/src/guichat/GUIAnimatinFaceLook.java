package guichat;

import java.awt.Color;
import java.awt.Graphics;

class GUIAnimatinFaceLook {
    int h = 100;
    int w = 100;
    int xStart = 0;
    int yStart = 0;

    public void setXY(int x, int y) {
        this.xStart = x;
        this.yStart = y;
    }

    public void setSize(int h, int w) {
        this.h = h;
        this.w = w;
    }

    public GUIAnimatinFaceLook() {}

    public void makeFace(Graphics g, String emotion) {
        if (emotion.equals("normal")) {
            makeEyes(g, w / 5, 0);
            makeNose(g, h / 5);
            makeMouth(g, w / 2, 0);
        } else if (emotion.equals("smile")) {
            makeEyes(g, w / 5, -5);
            makeNose(g, h / 5);
            makeMouth(g, w / 2, 10);
        } else if (emotion.equals("angry")) {
            makeEyes(g, w / 5, 10);
            makeNose(g, h / 5);
            makeMouth(g, w / 2, -10);
        }
    }

    private void makeEyes(Graphics g, int eyeSize, int browShift) {
        g.setColor(Color.WHITE);
        g.fillOval(xStart + w / 5, yStart + h / 5, eyeSize, eyeSize);
        g.fillOval(xStart + w - w / 3, yStart + h / 5, eyeSize, eyeSize);
        g.setColor(Color.BLACK);
        g.fillOval(xStart + w / 5 + eyeSize / 4, yStart + h / 5 + eyeSize / 4, eyeSize / 2, eyeSize / 2);
        g.fillOval(xStart + w - w / 3 + eyeSize / 4, yStart + h / 5 + eyeSize / 4, eyeSize / 2, eyeSize / 2);

        // 眉毛の描画 (怒りの表現のために角度をつける)
        if (browShift == 10) { // 怒り
            g.drawLine(xStart + w / 5, yStart + h / 5, xStart + w / 5 + eyeSize, yStart + h / 5 - browShift);
            g.drawLine(xStart + w - w / 3, yStart + h / 5 - browShift, xStart + w - w / 3 + eyeSize, yStart + h / 5);
        } else {
            g.drawLine(xStart + w / 5, yStart + h / 5 + browShift, xStart + w / 5 + eyeSize, yStart + h / 5 + browShift);
            g.drawLine(xStart + w - w / 3, yStart + h / 5 + browShift, xStart + w - w / 3 + eyeSize, yStart + h / 5 + browShift);
        }
    }

    private void makeNose(Graphics g, int noseSize) {
        g.setColor(Color.BLACK);
        g.fillOval(xStart + w / 2 - noseSize / 2, yStart + h / 2 - noseSize / 2, noseSize, noseSize);
    }

    private void makeMouth(Graphics g, int mouthSize, int mouthShift) {
        g.setColor(Color.RED);
        if (mouthShift > 0) { // 笑っている
            g.drawArc(xStart + w / 3, yStart + h - h / 3 - mouthShift, mouthSize, mouthSize / 2, 0, -180);
        } else if (mouthShift < 0) { // 怒っている
            g.drawArc(xStart + w / 3, yStart + h - h / 3 - mouthShift, mouthSize, mouthSize / 2, 0, 180);
        } else { // 普通の表情
            g.drawLine(xStart + w / 3, yStart + h - h / 3, xStart + w / 3 + mouthSize, yStart + h - h / 3);
        }
    }
}
//