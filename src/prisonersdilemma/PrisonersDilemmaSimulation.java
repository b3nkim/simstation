package prisonersdilemma;

import mvc.*;
import simstation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Prisoner extends Agent {

    Prisoner neighbor;
    public int fitness = 0;
    public boolean cheated = false;
    public Strategy strategy;

    public Prisoner() {
        super();
    }

    public void update() {
        int steps = Utilities.rng.nextInt(10) + 1;
        move(steps);
        int radius = 0;
        while (this.neighbor == null) {
            radius += 10;
            this.neighbor = (Prisoner) world.getNeighbor(this, radius);
        }

        play(this, this.neighbor); //they play each other twice, but w/e
    }
    public void play(Prisoner a, Prisoner b) {
        if (a.strategy.Cooperate() == true && b.strategy.Cooperate() == true) {
            a.fitness += 3;
            b.fitness += 3;
            a.cheated = true;
            b.cheated = true;
        }

        if (a.strategy.Cooperate() == true && b.strategy.Cooperate() == false) {
            b.fitness += 5;
            a.cheated = false;
            b.cheated = true;
        }

        if (a.strategy.Cooperate() == false && b.strategy.Cooperate() == true) {
            a.fitness += 5;
            a.cheated = true;
            b.cheated = false;
        }

        if (a.strategy.Cooperate() == false && b.strategy.Cooperate() == false) {
            a.fitness += 1;
            b.fitness += 1;
            a.cheated = false;
            b.cheated = false;
        }
    }
}

abstract class Strategy {
    protected Prisoner myPrisoner;

    public Strategy(Prisoner prisoner) {
        this.myPrisoner = prisoner;
    }

    public abstract boolean Cooperate();
}

class Cooperate extends Strategy {

    public Cooperate(Prisoner myPrisoner) {
        super(myPrisoner);
    }

    public boolean Cooperate() {
        return true;
    }
}

class RandomlyCooperate extends Strategy {

    public RandomlyCooperate(Prisoner myPrisoner) {
        super(myPrisoner);
    }

    public boolean Cooperate() {
        int rand = (int)(Math.random() * 2);
        if (rand == 0) return true;
        return false;
    }
}

class Cheat extends Strategy {

    public Cheat(Prisoner myPrisoner) {
        super(myPrisoner);
    }

    public boolean Cooperate() {
        return false;
    }
}

class Tit4Tat extends Strategy {
    public Tit4Tat(Prisoner myPrisoner) {
        super(myPrisoner);
    }

    public boolean Cooperate() {
        if (myPrisoner.cheated == true) return false;
        return true;
    }
}

class PrisonersDilemmaFactory extends SimStationFactory {
    public Model makeModel() {
        return new PrisonersDilemmaSimulation();
    }

    public String getTitle() {
        return "Prisoners Dilemma";
    }
}

public class PrisonersDilemmaSimulation extends Simulation {

    static final int POPULATION = 10;
    public void populateRandomly() {
        for (int i = 0; i < POPULATION; i++) {
            int rand = (int) (Math.random() * 4);
            Prisoner agent = new Prisoner();
            switch (rand) {
                case 1: {
                    agent.strategy = new Cooperate(agent);
                }

                case 2: {
                    agent.strategy = new RandomlyCooperate(agent);
                }

                case 3: {
                    agent.strategy = new Cheat(agent);
                }

                case 4: {
                    agent.strategy = new Tit4Tat(agent);
                }
            }

            addAgent(agent);
        }
    }

    public void populate() {
        for (int i = 0; i < POPULATION; i++) {
            Prisoner cooperate = new Prisoner();
            Prisoner random = new Prisoner();
            Prisoner cheat = new Prisoner();
            Prisoner tit4tat = new Prisoner();
            cooperate.strategy = new Cooperate(cooperate);
            random.strategy = new RandomlyCooperate(random);
            cheat.strategy = new Cheat(cheat);
            tit4tat.strategy = new Tit4Tat(tit4tat);
            addAgent(cooperate);
            addAgent(random);
            addAgent(cheat);
            addAgent(tit4tat);
        }
    }

    public List<String> getStatsArray() {
        List<String> stats = new ArrayList<String>();
        int[] totalfitness = new int[4];
        int[] totalagents = new int[4];
        List<Agent> agents = getAgents();
        for (int i = 0; i < agents.size(); i++) {
            Prisoner agent = (Prisoner) agents.get(i);
            if (agent.strategy instanceof Cooperate) {
                totalagents[0]++;
                totalfitness[0] += agent.fitness;
            }

            if (agent.strategy instanceof RandomlyCooperate) {
                totalagents[1]++;
                totalfitness[1] += agent.fitness;
            }

            if (agent.strategy instanceof Cheat) {
                totalagents[2]++;
                totalfitness[2] += agent.fitness;
            }

            if (agent.strategy instanceof Tit4Tat) {
                totalagents[3]++;
                totalfitness[3] += agent.fitness;
            }
        }

        stats.add("Cooperate: " + (double)totalfitness[0]/totalagents[0]);
        stats.add("Randomly Cooperate: " + (double)totalfitness[1]/totalagents[1]);
        stats.add("Cheat: " + (double)totalfitness[2]/totalagents[2]);
        stats.add("Tit4Tat: " + (double)totalfitness[3]/totalagents[3]);

        return stats;
    }

    public static void main(String[] args) {
        AppPanel panel = new SimulationPanel(new PrisonersDilemmaFactory());
        panel.display();
    }
}
