package prisonersdilemma;

import mvc.*;
import simstation.*;

class Cooperate extends Agent {

    public Cooperate() {
        super();
        heading = Heading.random();
    }

    public void update() {
        heading = Heading.random();
        int steps = Utilities.rng.nextInt(10) + 1;
        move(steps);
    }
}

class RandomlyCooperate extends Agent {

    public RandomlyCooperate() {
        super();
        heading = Heading.random();
    }

    public void update() {
        heading = Heading.random();
        int steps = Utilities.rng.nextInt(10) + 1;
        move(steps);
    }
}

class Cheat extends Agent {

    public Cheat() {
        super();
        heading = Heading.random();
    }

    public void update() {
        heading = Heading.random();
        int steps = Utilities.rng.nextInt(10) + 1;
        move(steps);
    }
}

class TitForTat extends Agent {

    public TitForTat() {
        super();
        heading = Heading.random();
    }

    public void update() {
        heading = Heading.random();
        int steps = Utilities.rng.nextInt(10) + 1;
        move(steps);
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

            addAgent(toAdd);
        }
    }

    public static void main(String[] args) {
        AppPanel panel = new SimulationPanel(new RandomWalkFactory());
        panel.display();
    }
}
