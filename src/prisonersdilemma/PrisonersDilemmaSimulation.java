package prisonersdilemma;

import mvc.*;
import simstation.*;

import java.util.List;
import java.util.Random;

class DilemmaAgent extends Agent {

    DilemmaAgent neighbor;
    public int fitness = 0;
    public boolean played = false, remembered = true;
    public Boolean strategy;

    public DilemmaAgent {
        super();
    }

    public void update() {
        int steps = Utilities.rng.nextInt(10) + 1;
        move(steps);
    }

    public DilemmaAgent getNeighbor() {
        int closest = Integer.MAX_VALUE;
        DilemmaAgent savedAgent = null;
        for (Agent agent : world.getAgents()) {
            DilemmaAgent dagent = (DilemmaAgent) agent;
            if (dagent.neighbor == null && dagent != this) {
                int distance = (int)getDistance(dagent);
                if (distance < closest) {
                    closest = distance;
                    savedAgent = dagent;
                }
            }
        }

        if (savedAgent == null) savedAgent = this;
        return savedAgent;
    }
    public void play(DilemmaAgent a, DilemmaAgent b) {
        if (a == b) {
            a.played = true;
            return;
        }

        if (a.strategy == true && b.strategy == true) {
            a.fitness += 3;
            b.fitness += 3;
            a.remembered = true;
            b.remembered = true;
        }

        if (a.strategy == true && b.strategy == false) {
            b.fitness += 5;
            a.remembered = false;
            b.remembered = true;
        }

        if (a.strategy == false && b.strategy == true) {
            a.fitness += 5;
            a.remembered = true;
            b.remembered = false;
        }

        if (a.strategy == false && b.strategy == false) {
            a.fitness += 1;
            b.fitness += 1;
            a.remembered = false;
            b.remembered = false;
        }

        a.played = true;
        b.played = true;
    }
}

class Cooperate extends DilemmaAgent {

    public Cooperate() {
        super();
    }

    public void update() {
        super.update();
        this.played = false;
        this.neighbor = this.getNeighbor();
        this.strategy = true;
        while (neighbor.strategy == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (played == false) {
            this.play(this, neighbor);
        }
    }
}

class RandomlyCooperate extends DilemmaAgent {

    public RandomlyCooperate() {
        super();
    }

    public void update() {
        super.update();
        this.neighbor = this.getNeighbor();
        int choice = (int) (Math.random() * 2);
        if (choice == 0) {
            this.strategy = false;
        } else {
            this.strategy = true;
        }

        while (neighbor.strategy == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (played == false) {
            this.play(this, neighbor);
        }
    }
}

class Cheat extends DilemmaAgent {

    public Cheat() {
        super();
    }

    public void update() {
        super.update();
        this.neighbor = this.getNeighbor();
        this.strategy = false;
        while (neighbor.strategy == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (played == false) {
            this.play(this, neighbor);
        }
    }
}

class TitForTat extends DilemmaAgent {
    public TitForTat() {
        super();
        this.remembered = true;
    }

    public void update() {
        super.update();
        this.neighbor = this.getNeighbor();
        this.strategy = remembered;
        while (neighbor.strategy == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (played == false) {
            this.play(this, neighbor);
        }
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

    public void populate() {
        for (int i = 0; i < 15; i++) {
            int rand = (int) (Math.random() * 4);
            Agent toAdd = null;
            switch (rand) {
                case 1: {
                    toAdd = new Cooperate();
                }

                case 2: {
                    toAdd = new RandomlyCooperate();
                }

                case 3: {
                    toAdd = new Cheat();
                }

                case 4: {
                    toAdd = new TitForTat();
                }
            }

            if (toAdd != null) {
                addAgent(toAdd);
            }
        }
    }

    public static void main(String[] args) {
        AppPanel panel = new SimulationPanel(new PrisonersDilemmaFactory());
        panel.display();
    }
}
