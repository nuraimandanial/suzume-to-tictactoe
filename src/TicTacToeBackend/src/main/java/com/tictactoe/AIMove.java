package com.tictactoe;

public class AIMove {
    private double suboptimalProb;

    public AIMove() {

    }
    public AIMove(double suboptimalProb) {
        this.suboptimalProb = suboptimalProb;
    }

    public double getSuboptimalProb() {
        return suboptimalProb;
    }
}
