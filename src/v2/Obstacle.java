package v2;

import processing.core.PApplet;
import processing.core.PVector;

public class Obstacle {

    PApplet parent;

    float xPos = 0;
    float yPos = 0;
    boolean isGoal = false;

    float t1;

    Obstacle(float x, float y, boolean isG, PApplet p) {
        xPos = x;
        yPos = y;
        isGoal = isG;

        parent = p;
    }

    PVector getVec(){return new PVector(xPos, yPos);}

    void drawIt() {

        if(parent.millis()-t1 < 100){
            if (isGoal) {
                parent.fill(0,255,0,180);
                parent.ellipse(xPos,yPos,40,40);
            }
            else {
                parent.fill(255,0,0,100);
                parent.ellipse(xPos,yPos,40,40);
            }
        }else {
            if (isGoal) {
                parent.fill(250, 240);
                parent.ellipse(xPos, yPos, 40, 40);
            } else {
                parent.fill(100, 200);
                parent.ellipse(xPos, yPos, 40, 40);
            }
        }
    }

    void hit(){t1 = parent.millis();}

}
