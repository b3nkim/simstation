package simstation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.*;

import mvc.*;

public class SimulationPanel extends AppPanel {
    private JButton Start, Suspend, Resume, Stop, Stats;

    public SimulationPanel(AppFactory factory) {
        super(factory);

        Start = new JButton("Start");
        Suspend = new JButton("Suspend");
        Resume = new JButton("Resume");
        Stop = new JButton("Stop");
        Stats = new JButton("Stats");

        buttonAdder(Start, this, controlPanel);
        buttonAdder(Suspend, this, controlPanel);
        buttonAdder(Resume, this, controlPanel);
        buttonAdder(Stop, this, controlPanel);
        buttonAdder(Stats, this, controlPanel);

        controlPanel.setVisible(true);
    }

    public static void buttonAdder(JButton b, AppPanel p, JPanel c) {
        b.addActionListener(p);
        c.add(b);
        b.setPreferredSize(new Dimension(100, 50));
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        Simulation simulation = (Simulation) model;

        try {
            switch (actionCommand) {
                case "Save": {
                    if (simulation.getAgents() == null) {
                        Utilities.error("There is no simulation to save!");
                        return;
                    }

                    if (simulation.running || simulation.suspended) {
                        Utilities.error("Cannot save a simulation while threads are still active!");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }

        super.actionPerformed(ae);
    }

    public static void main(String args[]) {
        AppFactory factory = new SimStationFactory();
        AppPanel panel = new SimulationPanel(factory);
        panel.display();
    }
}