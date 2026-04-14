package org.example.parkplayfinal.ds;

import org.example.parkplayfinal.model.Reservation;
import java.time.LocalDateTime;

public class BinomialHeapNode {
    public Reservation data;
    public LocalDateTime key;
    public int degree;

    public BinomialHeapNode p;
    public BinomialHeapNode child;
    public BinomialHeapNode sibling;

    public BinomialHeapNode(Reservation res) {
        this.data = res;
        this.key = res.getEndTime();
        this.degree = 0;
        this.p = null;
        this.child = null;
        this.sibling = null;
    }
}