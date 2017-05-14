package v2;

import processing.core.PApplet;
import processing.core.PVector;

public class Main extends PApplet {

    public Obstacle[] obstacles;
    public float[] start = new float[2];
    float[] goal = new float[2];

    NeuralNet[] nets;
    Generation gen;
    int genCount = 1;
    int lifeSpan = 20000;

    float carSpeed = 2.25f;

    float range = 150;
    int N = 100;
    Sim[] tests;

    public static void main(String[] args){PApplet.main("v2.Main");}

    public void settings(){
        size(1080, 720);
    }

    public void setup() {

        size(1080, 720);

        start[0] = width/4;
        start[1] = height/4;

        obstacles = new Obstacle[N];

        boolean works = false;

        int j=0;

        while (!works) {
            goal[0] = random(100, 1000);
            goal[1] = random(20, 720);
            works = Math.abs(goal[0] - start[0])>30 && Math.abs(goal[1] - start[1])>30;
        }

        works = true;

        while (j<N-1) {
            obstacles[j] = new Obstacle(random(1080), random(720),
                    false, this);

            works = Math.abs(obstacles[j].xPos - goal[0])>30 && Math.abs(obstacles[j].yPos - goal[1])>30;
            works = works && Math.abs(obstacles[j].xPos - start[0])>50 && Math.abs(obstacles[j].yPos - start[1])>50;

            if (works) {
                j++;
            }
        }

        obstacles[N-1] = new Obstacle(goal[0], goal[1], true, this);

        nets = new NeuralNet[50];

        for (int i=0; i<50; i++) {
            nets[i]= new NeuralNet(this, true);
            nets[i].config(60);
        }

        gen = new Generation(nets, this, obstacles, start, lifeSpan, carSpeed, range);

        tests = new Sim[50];

        for (int i=0; i<50; i++) {
            tests[i] = new Sim(obstacles, start, this, nets[i], false, carSpeed, range);
        }
    }


    public void draw() {


        background(0, 0, 35);

        fill(255);
        text("Generation: " + genCount, 980, 35);
        text("Press Space to go to the next Generation.", 825, 20);


        for (Obstacle o : obstacles) {
            o.drawIt();
        }

//        fill(255);
//
//        PVector inter = intersect(obstacles[N-1], new PVector(0,0), new PVector(goal[0]+10,goal[1]+10), 0);
//
//        ellipse(0,0, 10, 10); ellipse(goal[0]+10, goal[1]+10, 10, 10);
//
//        fill(255,0,0);
//
//        if(inter!=null) ellipse(inter.x, inter.y, 10, 10);


        gen.run();

        if (gen.done) {
            if (genCount % 20 == 19) {
                //will switch obstacle and goal sets every 20 generations

                int j = 0;
                boolean works = false;

                while (!works) {
                    goal[0] = random(width/2, width - 100);
                    goal[1] = random(height/2, height - 50);
                    works = Math.abs(goal[0] - start[0])>30 && Math.abs(goal[1] - start[1])>30;
                }

                obstacles[N-1] = new Obstacle(goal[0], goal[1], true, this);

                works = true;

                while (j<N-1) {
                    obstacles[j] = new Obstacle(random(1080), random(720),
                            false, this);


                    works = Math.abs(obstacles[j].xPos - goal[0])>30 && Math.abs(obstacles[j].yPos - goal[1])>30;

                    works = works && Math.abs(obstacles[j].xPos - start[0])>50 && Math.abs(obstacles[j].yPos - start[1])>50;

                    if (works) {
                        j++;
                    }
                }
            }

            nets = gen.getChildren(); //given that the generation has finished, this will recieve the children array produced by gen
            gen = new Generation(nets, this, obstacles, start, lifeSpan, carSpeed, range); //will set gen to a new generation based on the new set of neural networks
            genCount++;
        }
    }



}
