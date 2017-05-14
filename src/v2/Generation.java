package v2;
import processing.core.PApplet;

import java.util.*;

public class Generation { //used to store each generation
    int pop;
    PApplet parent; //reference to geneticSim2, which is the main PApplet where all drawing and graphics occur

    Sim[] members; //stores the simulations running in the current generation

    Sim[] selection;

    private NeuralNet[] children = new NeuralNet[50];

    boolean done = false;
    float t1;
    int lifeSpan;

    int success= 0;
    int term = 0;

    Obstacle[] obstacles;

    public Generation(NeuralNet[] nets, PApplet p, Obstacle[] obstacles, float[] start, int l, float sp, float r) {

        parent=p;
        pop = nets.length;
        members = new Sim[pop];

        for (int i=0; i<pop; i++) {
            members[i] = new Sim(obstacles, start, parent, nets[i], nets[i].fit, sp, r); //creates a new simulation
        }

        this.obstacles = obstacles;

        selection = new Sim[pop/5]; //the selection array will contain the set of member simulations that will be chosen for reproduction

        t1 = parent.millis(); //stores the time (in milliseconds) when the generation began to run

        lifeSpan = l;
    }

    public NeuralNet[] getChildren() {
        return children;
    }

    public void run() {

        boolean space = (parent.keyPressed && parent.key==' ');

        if (parent.millis()-t1 > lifeSpan ||((space && parent.millis()-t1 > 500) || (term>45 & success!=0))){
            done = true; //will stop generation after (lifeSpan) milliseconds or after space bar is pressed
        }

        if (!done) {

            success=0; term=0;

            for (Sim m : members) {
                m.update(false);
                success += (m.sucess) ? 1:0;
                term += (m.terminate) ? 1:0;
            }

        } else {
            for (Sim m : members) {
                m.update(true); //will update simulation with override set to true (each simulation will stop running).
            }

            //sorts the array of simulations according to fitness level at termination
            Arrays.sort(members, new Comparator<Sim>() {
                public int compare(Sim a, Sim b) {
                    return Float.compare(a.fitness, b.fitness);}});

            float[] fits = new float[pop]; //stores fitness of each sim

            for (int i = 0; i < pop; i++) {
                fits[i] = members[i].fitness;
            }

            for (int i=0; i<5; i++) {
                selection[i] = members[pop-1-i]; //takes the top five simulations
            }

            for (int i = 5; i < selection.length; i++) {
                int index = FPS(fits);
                selection[i] = members[index];
                fits[index] = 0;
            }
            reproduction();
        }
    }

    void reproduction() {

        //asexual reproduction from the absolute top five in FPS pool

        for(int i=0; i< 40; i++){
            children[i] = new NeuralNet(parent, false);
            children[i].config(60);
            children[i].setWeights(selection[i%5].net.getWeights());
            children[i].setFit();
        }

        //for (int i=0; i<5; i++) {
        //  children[i] = new NeuralNet(parent, false);
        //  children[i].config(10);
        //  children[i].setWeights(selection[i].net.getWeights());
        //  children[i].setFit();
        //}

        ////uniform crossover operation

        int m = 40; //starting index for cross-over generated nets

        for (int i=0; i<selection.length; i++) {
            for (int k=i; k<selection.length; k++) {

                float[] a = selection[i].net.getWeights();
                float[] b = selection[k].net.getWeights();

                if (a.length != b.length) {
                    throw new IllegalArgumentException("Neural nets of each simulation must have same number of total weights");
                }

                float[] w = new float[a.length]; //float array to store weights

                for (int j=0; j<a.length; j++) {

                    float p2 = parent.random(1);

                    if (p2>=1/2) {
                        w[j] = a[j];
                    } else {
                        w[j]=b[j];
                    }

                    //chooses either set of weights with a 50% chance
                }

                if (m<50) {//will keep adding resulting weight away to children as long as index <50
                    children[m] = new NeuralNet(parent, false);
                    children[m].config(60);

                    for (int r=0; r<a.length; r++) {
                        float p = parent.random(1);

                        if (p<0.80) {
                            w[r] = parent.random(-2, 2); //will make weight random with 80% probablity, otherwise set to crossover
                        }
                    }

                    children[m].setWeights(w);
                    m++;
                }
            }
        }

        //apply minor mutation to each neural net

        int index=0;

        for (NeuralNet n : children) {
            float[] weights = n.getWeights();

            int i=0;

            while (i<weights.length) {
                float p = parent.random(1);
                float t = (index>4) ? 0.7f : 0.9f;

                if (p<t) {weights[i] *= parent.random(0.8f+0.17f*(success/25), 1.2f-0.17f*(success/25));}
                i++;

            }

            children[index].setWeights(weights);

            index++;
        }
    }

    int FPS(float[] fits) {

        float sum =0;

        for (int i=0; i<pop; i++) {
            sum += fits[i];
        }

        float value = parent.random(sum);

        // locate the random value based on the weights
        for (int i=0; i<pop; i++) {
            value -= fits[i];

            if (value <= 0) {
                return i;
            }
        }
        // when rounding errors occur, we return the last item's index
        return fits.length - 1;
    }
}