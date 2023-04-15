package plague;


import mvc.*;
import simstation.*;
import java.awt.*;

class PlagueAgent extends Agent {
    private boolean infected;

    public PlagueAgent() {
        super();
        this.infected = false;
    }

    public void update() {
        if (infected) {
            PlagueSimulation simulation = (PlagueSimulation) world;
            Agent neighbor = simulation.getNeighbor(this, 10);

            if (neighbor != null && neighbor instanceof PlagueAgent) {
                PlagueAgent plagueNeighbor = (PlagueAgent) neighbor;
                if (!plagueNeighbor.infected && Utilities.rng.nextInt(100) < PlagueSimulation.VIRULENCE) {
                    if (Utilities.rng.nextInt(100) >= PlagueSimulation.RESISTANCE) {
                        plagueNeighbor.infected = true;
                    }
                }
            }
        }
        int turn = Utilities.rng.nextInt(2) * 2 - 1;
        int nextOrdinal = (heading.ordinal() + turn + 4) % 4;
        heading = Heading.values()[nextOrdinal];
        move(4);
    }

    public Color getColor() {
        return (infected) ? Color.RED : Color.GREEN;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    public boolean isInfected() {
        return infected;
    }
}

class PlagueFactory extends SimStationFactory {
    public Model makeModel() {
        return new PlagueSimulation();
    }

    public View makeView(Model model) {
        return new PlagueView((PlagueSimulation) model);
    }
    public String getTitle() {
        return "Plague Simulator";
    }
}

class PlagueView extends SimulationView {
    public PlagueView(PlagueSimulation m) {
        super(m);
    }

    @Override
    public void paintComponent(Graphics gc) {
        super.paintComponent(gc);
        Color oldColor = gc.getColor();
        PlagueSimulation simulation = (PlagueSimulation) this.model;
        for (Agent agent : simulation.getAgents()) {
            if (agent instanceof PlagueAgent) {
                PlagueAgent plagueAgent = (PlagueAgent) agent;
                gc.setColor(plagueAgent.getColor());
                gc.fillOval(plagueAgent.getXc(), plagueAgent.getYc(), 10, 10);
            }
        }
        gc.setColor(oldColor);
    }
}
public class PlagueSimulation extends Simulation {

    public static int VIRULENCE = 50; // % chance of infection
    public static int RESISTANCE = 2; // % chance of resisting infection

    public void populate() {
        for (int i = 0; i < 50; i++) {
            PlagueAgent agent = new PlagueAgent();
            if (i == 0) {
                agent.setInfected(true);
            }
            addAgent(agent);
        }
    }
    public String[] getStats() {
        String[] stats = new String[3];
        int totalAgents = getAgents().size();
        int infectedAgents = 0;

        for (Agent agent : getAgents()) {
            if (agent instanceof PlagueAgent) {
                PlagueAgent plagueAgent = (PlagueAgent) agent;
                if (plagueAgent.isInfected()) {
                    infectedAgents++;
                }
            }
        }
        double infectionPercentage = (double) infectedAgents / totalAgents * 100;

        stats[0] = "#agents = " + totalAgents;
        stats[1] = "clock = " + getClock();
        stats[2] = "% infected = " + infectionPercentage;

        return stats;
    }

    public static void main(String[] args) {
        AppFactory factory = new PlagueFactory();
        AppPanel panel = new SimulationPanel(factory);
        panel.display();
    }
}
