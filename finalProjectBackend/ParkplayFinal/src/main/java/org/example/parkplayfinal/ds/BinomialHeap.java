package org.example.parkplayfinal.ds;

import org.example.parkplayfinal.model.Reservation;
import java.time.LocalDateTime;

public class BinomialHeap {
    private BinomialHeapNode head;

    public BinomialHeap() {
        this.head = null;
    }

    public BinomialHeapNode minimum() {
        BinomialHeapNode y = null;
        BinomialHeapNode x = this.head;
        LocalDateTime minVal = null;

        while (x != null) {
            if (minVal == null || x.key.isBefore(minVal)) {
                minVal = x.key;
                y = x;
            }
            x = x.sibling;
        }
        return y;
    }

    private void binomialLink(BinomialHeapNode y, BinomialHeapNode z) {
        y.p = z;
        y.sibling = z.child;
        z.child = y;
        z.degree += 1;
    }

    private BinomialHeapNode binomialHeapMerge(BinomialHeapNode h1, BinomialHeapNode h2) {
        if (h1 == null) return h2;
        if (h2 == null) return h1;

        BinomialHeapNode headMerge = null;
        BinomialHeapNode current = null;

        if (h1.degree <= h2.degree) {
            headMerge = h1;
            h1 = h1.sibling;
        } else {
            headMerge = h2;
            h2 = h2.sibling;
        }

        current = headMerge;

        while (h1 != null && h2 != null) {
            if (h1.degree <= h2.degree) {
                current.sibling = h1;
                h1 = h1.sibling;
            } else {
                current.sibling = h2;
                h2 = h2.sibling;
            }
            current = current.sibling;
        }

        if (h1 != null) current.sibling = h1;
        if (h2 != null) current.sibling = h2;

        return headMerge;
    }

    private BinomialHeapNode binomialHeapUnion(BinomialHeap h1, BinomialHeap h2) {
        BinomialHeapNode newHead = binomialHeapMerge(h1.head, h2.head);
        if (newHead == null) return null;

        BinomialHeapNode prevX = null;
        BinomialHeapNode x = newHead;
        BinomialHeapNode nextX = x.sibling;

        while (nextX != null) {
            if ((x.degree != nextX.degree) ||
                    (nextX.sibling != null && nextX.sibling.degree == x.degree)) {
                prevX = x;
                x = nextX;
            } else if (x.key.compareTo(nextX.key) <= 0) {
                x.sibling = nextX.sibling;
                binomialLink(nextX, x);
            } else {
                if (prevX == null) {
                    newHead = nextX;
                } else {
                    prevX.sibling = nextX;
                }
                binomialLink(x, nextX);
                x = nextX;
            }
            nextX = x.sibling;
        }
        return newHead;
    }

    public void insert(Reservation res) {
        BinomialHeap hPrime = new BinomialHeap();
        hPrime.head = new BinomialHeapNode(res);
        this.head = binomialHeapUnion(this, hPrime);
    }

    public BinomialHeapNode extractMin() {
        if (this.head == null) return null;

        BinomialHeapNode minNode = this.head;
        BinomialHeapNode minPrev = null;
        BinomialHeapNode x = this.head;
        BinomialHeapNode prevX = null;

        while (x != null) {
            if (x.key.isBefore(minNode.key)) {
                minNode = x;
                minPrev = prevX;
            }
            prevX = x;
            x = x.sibling;
        }

        if (minPrev == null) {
            this.head = minNode.sibling;
        } else {
            minPrev.sibling = minNode.sibling;
        }

        BinomialHeapNode child = minNode.child;
        BinomialHeapNode reversedChild = null;

        while (child != null) {
            BinomialHeapNode next = child.sibling;
            child.sibling = reversedChild;
            child.p = null;
            reversedChild = child;
            child = next;
        }

        BinomialHeap hPrime = new BinomialHeap();
        hPrime.head = reversedChild;
        this.head = binomialHeapUnion(this, hPrime);

        return minNode;
    }
}