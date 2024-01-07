import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.function.Consumer;

class CloudsAndPonds extends Pane implements Updatable {
    public Clouds clouds;
    private Ponds ponds;
    private static int callCounter = 0;

    public CloudsAndPonds() {
        clouds = new Clouds();
        ponds = new Ponds();
        getChildren().addAll(ponds, clouds);
    }

    public void update(double delta) {
        for (Object o : clouds) {
            if (o instanceof Cloud c) {
                c.update(delta);

                //      increase cloud saturation every 60 counters
                if (c.getPercent() > 0) {
                    callCounter++;
                    if (callCounter % 70 == 0) {
                        callCounter = 0;
                        ponds.collectRain(c);
                    }
                }
            }
        }
        clouds.deleteDeadClouds();
        clouds.makeNewClouds();
    }

    public void seedCloud(Cloud cloud) {
        cloud.increaseSaturation();
    }

    private static boolean withinCollectionDistance(Pond pond,
                                                    Point2D cloudCenter,
                                                    Point2D pondCenter) {
        double distance = Math.abs(pondCenter.distance(cloudCenter));
        if (distance < (pond.getDiameter() * 4))
            return true;
        else
            return false;
    }

    public boolean allPondsFull() {
        Iterator itr = ponds.iterator();
        while (itr.hasNext()) {
            Object o = itr.next();
            if (o instanceof Pond p) {
                if (p.getPercent() < 100)
                    return false;
            }
        }
        return true;
    }

    public int getScore() {
        int score = 0;
        Iterator itr = ponds.iterator();
        while (itr.hasNext()) {
            Pond p = (Pond) itr.next();
            score += p.getPercent();
        }
        return score/3;
    }


    class Clouds extends Pane implements Iterable {
        //            List<Cloud> cloudsList = new ArrayList<Cloud>();
        Timer checkCloudCollectionTimer;

        public Clouds() {
            this.getChildren().addAll(
                    new Cloud(GameObject.randSpawnX(),
                            GameObject.randSpawnY()),
                    new Cloud(GameObject.randSpawnX(),
                            GameObject.randSpawnY()),
                    new Cloud(GameObject.randSpawnX(),
                            GameObject.randSpawnY()));
        }

        @Override
        public Iterator iterator() {
            return this.getChildren().iterator();
        }

        @Override
        public void forEach(Consumer action) {
            Iterable.super.forEach(action);
        }

        public void deleteDeadClouds() {
            ArrayList<Cloud> deadClouds = new ArrayList<Cloud>();
            Iterator itr = this.iterator();
            while (itr.hasNext()) {
                Object o = itr.next();
                if (o instanceof Cloud c) {
                    if (c.state instanceof Cloud.CloudDead) {
                        deadClouds.add(c);
                    }
                }
            }
            for (Cloud c : deadClouds) {
                this.getChildren().remove(c);
            }
        }


        public void makeNewClouds() {
            int numberOfClouds = this.getChildren().size();

            switch (numberOfClouds) {
                case 1:
                    // 100% chance of spawning 2
                    this.getChildren().addAll(new Cloud(-50,
                                    GameObject.randSpawnY()),
                            new Cloud(-50,
                                    GameObject.randSpawnY()));
                    break;
                case 2:
                    // 100% chance of spawning 1
                    // 5% chance of spawning 2
                    this.getChildren().add(new Cloud(-50,
                            GameObject.randSpawnY()));
                    if (GameObject.rollD200() < 10)
                        this.getChildren().add(new Cloud(-50,
                                GameObject.randSpawnY()));
                    break;
                case 3:
                    // 2.5% chance of spawning 1
                    // 1% chance of spawning 2
                    if (GameObject.rollD200() < 5)
                        this.getChildren().add(new Cloud(-50,
                                GameObject.randSpawnY()));
                    if (GameObject.rollD200() < 2)
                        this.getChildren().add(new Cloud(-50,
                                GameObject.randSpawnY()));
                    break;
                case 4:
                    // 0.5% chance of spawning 1
                    if (GameObject.rollD200() < 1)
                        this.getChildren().add(new Cloud(-50,
                                GameObject.randSpawnY()));
                    break;
                default:
                    // spawn nothing
                    break;
            }
        }
    }


    class Ponds extends Pane implements Iterable {

        public Ponds() {
            // In order to prevent intersecting ponds,
            // I have made an ugly constructor.
            Pond pond1 = new Pond();
            Pond pond2 = new Pond();
            Pond pond3 = new Pond();
            while (pond1.intersects(pond2) ||
                   pond2.intersects(pond3) ||
                   pond1.intersects(pond3)) {
                pond1 = new Pond();
                pond2 = new Pond();
                pond3 = new Pond();
            }
            this.getChildren().addAll(pond1,pond2,pond3);
        }

        @Override
        public Iterator iterator() {
            return this.getChildren().iterator();
        }

        @Override
        public void forEach(Consumer action) {
            Iterable.super.forEach(action);
        }

        public void collectRain(Cloud cloud) {
            Point2D cloudCenter = new Point2D(
                    cloud.getBoundsInParent().getCenterX(),
                    cloud.getBoundsInParent().getCenterY());
            Iterator itr = this.iterator();
            while (itr.hasNext()) {
                Object o = itr.next();
                if (o instanceof Pond p) {
                    Point2D pondCenter = new Point2D(
                            p.getBoundsInParent().getCenterX(),
                            p.getBoundsInParent().getCenterY());
                    if (CloudsAndPonds.withinCollectionDistance(p,
                            pondCenter, cloudCenter)) {
                        p.update(Math.abs(cloudCenter.distance(pondCenter)));
                    }
                }
            }
        }

    }


}
