package guibasic;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

public class MovingBallAWT {
    public static void main(String[] args) {
        FFrame f = new FFrame();
        f.setSize(500, 500);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.show();
    }

    static class FFrame extends Frame implements Runnable {

        Thread th;
        ArrayList<Ball> balls;
        private boolean enable = true;
        private int counter = 0;

        FFrame() {
            th = new Thread(this);
            th.start();
        }

        public void run() {
            balls = new ArrayList<>();
            Random rand = new Random();

            for (int i = 0; i < 5; i++) {
                Ball ball = new Ball();
                ball.setPosition(rand.nextInt(400), rand.nextInt(400));
                ball.setR(10 + rand.nextInt(10));
                ball.setColor(new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
                balls.add(ball);
            }

            while (enable) {
                try {
                    th.sleep(100);
                    counter++;
                    if (counter >= 200) enable = false;
                } catch (InterruptedException e) {
                }

                for (int i = 0; i < balls.size(); i++) {
                    Ball ball = balls.get(i);
                    ball.move();
                    checkCollision(ball);
                }

                repaint();
            }
        }

        private void checkCollision(Ball ball) {
            for (int i = 0; i < balls.size(); i++) {
                Ball other = balls.get(i);
                if (ball != other && ball.intersects(other)) {
                    balls.add(ball.split());
                    balls.add(other.split());
                    balls.remove(ball);
                    balls.remove(other);
                    break;
                }
            }
        }

        public void paint(Graphics g) {
            for (Ball ball : balls) {
                ball.draw(g);
            }
        }

        class Ball {
            int x, y, r;
            Color c;
            int xDir = 1;
            int yDir = 1;

            void setColor(Color c) {
                this.c = c;
            }

            void move() {
                if ((xDir == 1 && x >= 500 - 2 * r) || (xDir == -1 && x <= 0)) {
                    xDir = -xDir;
                    r += 5; // 壁に当たるとボールの半径を増やす
                }
                if ((yDir == 1 && y >= 500 - 2 * r) || (yDir == -1 && y <= 0)) {
                    yDir = -yDir;
                    r += 5; // 壁に当たるとボールの半径を増やす
                }
                x += xDir * 10;
                y += yDir * 10;
            }

            boolean intersects(Ball other) {
                int dx = this.x - other.x;
                int dy = this.y - other.y;
                int distance = dx * dx + dy * dy;
                int radiusSum = this.r + other.r;
                return distance <= radiusSum * radiusSum;
            }

            Ball split() {
                Ball newBall = new Ball();
                newBall.setPosition(this.x, this.y);
                newBall.setR(this.r / 2);
                newBall.setColor(this.c);
                newBall.xDir = -this.xDir;
                newBall.yDir = -this.yDir;
                return newBall;
            }

            void setPosition(int x, int y) {
                this.x = x;
                this.y = y;
            }

            void setR(int r) {
                this.r = r;
            }

            void draw(Graphics g) {
                g.setColor(c);
                g.fillOval(x, y, 2 * r, 2 * r);
            }
        }
    }
}
