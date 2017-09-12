package edu.wisc.ece.minilab2;

/**
 * Created by Shyamal Anadkat on 9/11/2017.
 */

/* Represents a stopwatch */
public class Stopwatch {

    private long startT = 0;
    private long currT = 0;
    private boolean running = false;
    private boolean isPaused = false;

    /* Starts the stopwatch*/
    public void start() {
        if(!running) {
            this.startT = System.currentTimeMillis();
            this.running = true;
            this.isPaused = false;
        }
    }

    public void pause() {
        if(running) {
            this.running = false;
            this.isPaused = true;
            currT = System.currentTimeMillis() - startT;
        }
    }

    public void resume() {
        if(!running & isPaused()) {
            this.startT = System.currentTimeMillis() - currT;
            this.running = true;
            this.isPaused = false;
        }
    }

    public boolean isRunning() {
        return this.running;
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public void stop() {
        if(running) {
            this.running = false;
        }
    }

    public String getElapsed() {
        String retVal = Double.toString((System.currentTimeMillis() - startT)/1000.0);
        retVal = retVal.replaceAll("[\\s.]", ":");
        return retVal;
    }
}
