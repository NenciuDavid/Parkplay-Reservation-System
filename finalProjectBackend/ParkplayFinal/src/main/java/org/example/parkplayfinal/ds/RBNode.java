package org.example.parkplayfinal.ds;

import org.example.parkplayfinal.model.Reservation;
import java.time.LocalDateTime;

public class RBNode {
    public Reservation data;
    public LocalDateTime key; // startTime-ul va fi cheia de sortare

    public RBNode p;
    public RBNode left;
    public RBNode right;

    public enum Color { RED, BLACK }
    public Color col;

    public static final RBNode Nil = new RBNode();

    private RBNode() {
        this.col = Color.BLACK;
    }

    public RBNode(Reservation res, RBNode l, RBNode r, RBNode parent, Color c) {
        this.data = res;
        this.key = res.getStartTime();
        this.left = (l != null) ? l : Nil;
        this.right = (r != null) ? r : Nil;
        this.p = (parent != null) ? parent : Nil;
        this.col = c;
    }

    public boolean isNil() {
        return this == Nil;
    }
}