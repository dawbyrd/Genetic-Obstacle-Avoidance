package v2;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawsonbyrd on 2/20/17.
 */
public class Layer {

    PApplet parent;

    private List<Neuron> neurons;

    private Layer prev; private Layer next;

    public Layer(PApplet p) {
        neurons = new ArrayList<Neuron>();
        prev = null;
        parent = p;
    }

    public Layer(Layer prev, PApplet p){
        neurons = new ArrayList<Neuron>();

        parent = p; this.prev = prev;
    }

    public List<Neuron> getNeurons() {
        return this.neurons;
    }


    public void addNeuron(Neuron neuron) {

        if(prev != null) {

            List<Neuron> inputs = prev.getNeurons();

            for(int i = 0; i < inputs.size(); i++) {

                neuron.addInput(new Edge(inputs.get(i), parent.random(-2, 2) ));

            }
        }
        neurons.add(neuron);
    }

    public void feedForward() {

        for(int i = 0; i < neurons.size(); i++) {
            neurons.get(i).activate();
        }
    }

    public Layer getPrev() {
        return prev;
    }

    void setPrev(Layer previousLayer) {
        this.prev = previousLayer;
    }

    public Layer getNext() {
        return next;
    }

    void setNext(Layer next) {
        this.next = next;
    }

    public boolean isOutputLayer() {return next == null;}

}
