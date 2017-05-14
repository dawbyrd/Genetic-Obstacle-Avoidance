package v2;

public class Edge{

    private Neuron src;
    private float weight;

    public Edge(Neuron src, float weight) {
        this.src = src;
        this.weight = weight;
    }

    public Neuron getSrc() {
        return src;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}