package v2;

import java.util.*;
import processing.core.PApplet;

public class Neuron {
    private float output;
    List<Edge> inputs;
    int activation;

    public Neuron(int act) {
        this.activation = act;
        inputs = new ArrayList<Edge>();
    }

    public void addInput(Edge input) {
        inputs.add(input);
    }

    public List<Edge> getInputs() {
        return this.inputs;
    }

    public float[] getWeights(){
        float[] weights = new float[inputs.size()];

        for(int i=0; i<inputs.size(); i++){
            weights[i] = inputs.get(i).getWeight();
        }

        return weights;
    }

    //This gets the output of the neuron
    public void activate() {
        float sum = 0;

        for(Edge e : inputs){
            float src =  e.getSrc().getOutput();

            if(activation==0 || activation==1) {
                sum += e.getWeight() * src;
            }

            if(activation==2) {
                sum += e.getWeight()/(src*src);
            }
        }
        if(activation==1) {
            output = 1/(1+PApplet.exp((float)sum));
        } else {
            output = sum;
        }
    }

    public float getOutput(){
        return output;
    }

    public void setOutput(float output) {
        this.output = output;
    }
}