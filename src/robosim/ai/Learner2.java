package robosim.ai;

import robosim.core.*;
import robosim.reinforcement.QTable;

import java.util.Optional;

public class Learner2 implements Controller {

    QTable qTable = new QTable(7, 4, 2, 20, 5, 0.9);
    // 0 - bump, 1 - near, 2 - far, 3 - dirt in front, 4 - dirt on left, 5 - dirt on right, 6 - dirt found
    int dirtCount = 0;
    int newAction;
    Action[] actions = new Action[]{Action.FORWARD, Action.BACKWARD, Action.LEFT, Action.RIGHT};

    @Override
    public void control(Simulator sim) {
        double closestObstacle = sim.findClosestProblem();
        Optional<Polar> closestDirt = sim.findDirt();
        if (sim.getDirt() > dirtCount) { // if there is dirt
            dirtCount = sim.getDirt(); // update dirtCount
            newAction = qTable.senseActLearn(6, 15); // reward bot for finding dirt :)
        } else if (closestDirt.isPresent()) { // if dirt is near
            double dirtAngle = Math.abs(closestDirt.get().getTheta()) - Robot.ANGULAR_VELOCITY; // angle of the dirt in comparison to the bot
            if (Math.abs(dirtAngle) < 2) { // if dirt is in front
                newAction = qTable.senseActLearn(3, 0); // change state
            } else if (dirtAngle < 0) { // if dirt is on the left
                newAction = qTable.senseActLearn(4, 0); // change state
            } else { // in any other case
                newAction = qTable.senseActLearn(5, 0); // change state
            }
        } else if (closestObstacle < 30) { // if obstacle is near
            newAction = qTable.senseActLearn(1, 0); // change state
        } else if (sim.wasHit()) { // if bot is hit
            newAction = qTable.senseActLearn(0, -10); // punish bot for being hit :(
        } else { // in any other case
            newAction = qTable.senseActLearn(2, 2);
        }
        actions[newAction].applyTo(sim); // perform action
    }

}
