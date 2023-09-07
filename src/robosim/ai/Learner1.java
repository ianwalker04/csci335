package robosim.ai;

import robosim.core.Action;
import robosim.core.Controller;
import robosim.core.Simulator;
import robosim.reinforcement.QTable;

public class Learner1 implements Controller {

    QTable qTable = new QTable(3, 4, 1, 5, 5, 0.9);
    // 0 - bump, 1 - near, 2 - far

    int newAction;

    Action[] actions = new Action[]{Action.FORWARD, Action.BACKWARD, Action.LEFT, Action.RIGHT};

    @Override
    public void control(Simulator sim) {
        double closestObstacle = sim.findClosestProblem();
        if (closestObstacle < 30) { // if obstacle is near
            newAction = qTable.senseActLearn(1, 1); // change state
        } else if (sim.wasHit()) { // if bot is hit
            newAction = qTable.senseActLearn(0, -10); // change state
        } else { // in any other case
            newAction = qTable.senseActLearn(2, 0); // change state
        }
        actions[newAction].applyTo(sim); // perform action
    }

}
