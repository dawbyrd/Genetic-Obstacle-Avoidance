package v2;

import processing.core.PApplet;

import java.lang.reflect.Array;
import java.util.*;

public class NeuralNet {
    private List<Layer> layers;
    private Layer input;
    private Layer output;
    public boolean fit = false;
    PApplet parent;
    boolean random;

    NeuralNet( PApplet p, boolean random) {
        layers = new ArrayList<Layer>();

        this.random = random;
        parent = p;
    }

    public void addLayer(Layer layer) {
        layers.add(layer);

        if (layers.size() == 1) {
            input = layer;
        }
        if (layers.size() > 1) {
            Layer previousLayer = layers.get(layers.size() - 2);
            previousLayer.setNext(layer);
        }
        output = layers.get(layers.size() - 1);
    }

    public void setInputs(float[] inputs) {
        List<Neuron> neurons = input.getNeurons();

        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).setOutput(inputs[i]);
        }
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public float[] getWeights() {
        ArrayList<Float> weights = new ArrayList<Float>();

        for (Layer layer : layers) {
            for (Neuron n : layer.getNeurons()) {
                for (Edge e : n.getInputs()) {
                    weights.add(e.getWeight());
                }
            }
        }

        float[] weightVec = new float[weights.size()];

        int i = 0;
        for (float w : weights) {
            weightVec[i] = w;
            i++;
        }

        return weightVec;
    }

    public void setWeights(float[] weights) {
        if (weights.length != this.getWeights().length) {
            throw new IllegalArgumentException("Size of desired weight array must match the number of weights in current neural network");
        }

        int i = 0;

        for (Layer layer : layers) {
            for (Neuron n : layer.getNeurons()) {
                for (Edge e : n.getInputs()) {
                    e.setWeight(weights[i]);
                    i++;
                }
            }
        }
    }

    public float[] getOutput() {
        float[] outputs = new float[output.getNeurons().size()];

        for (int i = 1; i < layers.size(); i++) {
            layers.get(i).feedForward();
        }
        int i = 0;
        for (Neuron neuron : output.getNeurons()) {
            outputs[i] = neuron.getOutput();
            i++;
        }
        return outputs;
    }

    public void config(int inC) {
        Layer input = new Layer(parent);

        for (int i=0; i<32; i++){
            input.addNeuron(new Neuron(0));
        }

        this.addLayer(input);

        //add hidden layers with random weights

        for (int i=0; i<2; i++) {
            Layer mid = new Layer(getLayers().get(i), parent);

            int act = (i==0) ? 0:1;

            for (int k=0; k<20; k++) {
                mid.addNeuron(new Neuron(act));
            }
            this.addLayer(mid);
        }

        //adding output layer
        Layer out = new Layer(getLayers().get(2), parent);

        for (int i=0; i<2; i++) {
            out.addNeuron(new Neuron(0));
        }
        addLayer(out);
    }

    public void setFit() {
        fit=true;
    }
}