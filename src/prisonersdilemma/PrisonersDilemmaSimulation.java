package prisonersdilemma;

import mvc.*;
import simstation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class Prisoner extends Agent {

    Prisoner neighbor;
    public int fitness = 0;
    public boolean cheated = false;
    public boolean played = false;
    public Strategy strategy;

    public Prisoner() {
        super();
    }

    public void update() {
        int steps = (Utilities.rng.nextInt(10) + 1);
        played = false;
        move(steps);
        int radius = 0;
        while (this.neighbor == null) {
            radius += 10;
            this.neighbor = (Prisoner) world.getNeighbor(this, radius);
        }

        play(this, this.neighbor);
    }
    public void play(Prisoner a, Prisoner b) {
        if (a.played == true || b.played == true) {
            return;
        }

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

        a.played = true;
        b.played = true;
    }
}

abstract class Strategy {
    protected Prisoner myPrisoner;

    public Strategy(Prisoner prisoner) {
        this.myPrisoner = prisoner;
    }

    public abstract boolean Cooperate();

    public abstract Color getColor();
}

class Cooperate extends Strategy {

    public Cooperate(Prisoner myPrisoner) {
        super(myPrisoner);
    }

    public boolean Cooperate() {
        return true;
    }

    public Color getColor() { return Color.GREEN; }
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

    public Color getColor() { return Color.BLUE; }
}

class Cheat extends Strategy {

    public Cheat(Prisoner myPrisoner) {
        super(myPrisoner);
    }

    public boolean Cooperate() {
        return false;
    }

    public Color getColor() { return Color.RED; }
}

class Tit4Tat extends Strategy {
    public Tit4Tat(Prisoner myPrisoner) {
        super(myPrisoner);
    }

    public boolean Cooperate() {
        if (myPrisoner.cheated == true) return false;
        return true;
    }

    public Color getColor() { return Color.YELLOW; }
}

class PrisonersDilemmaFactory extends SimStationFactory {
    public Model makeModel() {
        return new PrisonersDilemmaSimulation();
    }

    public View makeView(Model model) {
        return new PrisonersDilemmaView((PrisonersDilemmaSimulation) model);
    }
    public String getTitle() {
        return "Prisoners Dilemma";
    }
}

class PrisonersDilemmaView extends SimulationView {
    public PrisonersDilemmaView(PrisonersDilemmaSimulation m) {
        super(m);
    }

    @Override
    public void paintComponent(Graphics gc) {
        super.paintComponent(gc);
        Color oldColor = gc.getColor();
        PrisonersDilemmaSimulation simulation = (PrisonersDilemmaSimulation) this.model;
        for (Agent agent : simulation.getAgents()) {
            if (agent instanceof Prisoner) {
                Prisoner prisoner = (Prisoner) agent;
                gc.setColor(prisoner.strategy.getColor());
                gc.fillOval(prisoner.getXc(), prisoner.getYc(), 10, 10);
            }
        }
        gc.setColor(oldColor);
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

        stats.add("Cooperate: " + totalfitness[0]/(double)totalagents[0]);
        stats.add("Randomly Cooperate: " + totalfitness[1]/(double)totalagents[1]);
        stats.add("Cheat: " + totalfitness[2]/(double)totalagents[2]);
        stats.add("Tit4Tat: " + totalfitness[3]/(double)totalagents[3]);

        return stats;
    }

    public static void main(String[] args) {
        AppPanel panel = new SimulationPanel(new PrisonersDilemmaFactory());
        panel.display();
    }
}
