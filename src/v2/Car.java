package v2;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Created by dawsonbyrd on 2/18/17.
 */
public class Car {

    PApplet parent;

    int scale;

    PVector pos;

    boolean fit;

    float[] state = new float[3];

    float[] target = new float[2];

    float[] acceleration = new float[2];

    float[] current = new float[2];

    Car(float x, float y, float d, PApplet p, boolean fit) {
        parent = p;

        state[0] = x;
        state[1] = y;
        state[2] = d;

        pos = new PVector(x,y);
        this.fit=fit;
    }


    public void update(float x, float y) {

        target = new float[]{x,y};

        acceleration[0] = 0.5f*(target[0]-current[0]);
        acceleration[1] = 0.5f*(target[1]-current[1]);

        current[0]+=acceleration[0]; current[1] += acceleration[1];

        state[0] += current[0];
        state[1] += current[1];
        state[2] += parent.atan(y/x);

        pos.x =  state[0]; pos.y = state[1];
    }

    public void draw(){

        parent.fill(255, 200);

        if(fit) parent.fill(0,255,0, 200);

        parent.ellipse(state[0],state[1],20,20);
    }


}
