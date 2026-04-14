package org.example.parkplayfinal.ds;

import org.example.parkplayfinal.model.Reservation;
import java.time.LocalDateTime;

public class RBTree {
    public RBNode root;

    public RBTree() {
        root = RBNode.Nil;
    }

    public boolean hasOverlap(LocalDateTime newStart, LocalDateTime newEnd) {
        return checkOverlapRecursive(root, newStart, newEnd);
    }

    private boolean checkOverlapRecursive(RBNode node, LocalDateTime newStart, LocalDateTime newEnd) {
        if (node.isNil()) return false;

        LocalDateTime nodeStart = node.data.getStartTime();
        LocalDateTime nodeEnd = node.data.getEndTime();

        if (newStart.isBefore(nodeEnd) && newEnd.isAfter(nodeStart)) {
            return true;
        }

        if (newStart.isBefore(nodeStart)) {
            return checkOverlapRecursive(node.left, newStart, newEnd);
        } else {
            return checkOverlapRecursive(node.right, newStart, newEnd);
        }
    }

    public void leftRotate(RBNode x) {
        RBNode y = x.right;
        x.right = y.left;
        if (y.left != RBNode.Nil) {
            y.left.p = x;
        }
        y.p = x.p;
        if (x.p == RBNode.Nil) {
            this.root = y;
        } else if (x == x.p.left) {
            x.p.left = y;
        } else {
            x.p.right = y;
        }
        y.left = x;
        x.p = y;
    }

    public void rightRotate(RBNode x) {
        RBNode y = x.left;
        x.left = y.right;
        if (y.right != RBNode.Nil) {
            y.right.p = x;
        }
        y.p = x.p;
        if (x.p == RBNode.Nil) {
            this.root = y;
        } else if (x == x.p.right) {
            x.p.right = y;
        } else {
            x.p.left = y;
        }
        y.right = x;
        x.p = y;
    }

    public void RBInsert(Reservation res) {
        RBNode z = new RBNode(res, RBNode.Nil, RBNode.Nil, RBNode.Nil, RBNode.Color.RED);
        RBNode y = RBNode.Nil;
        RBNode x = this.root;

        while (x != RBNode.Nil) {
            y = x;
            if (z.key.isBefore(x.key)) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        z.p = y;
        if (y == RBNode.Nil) {
            this.root = z;
        } else if (z.key.isBefore(y.key)) {
            y.left = z;
        } else {
            y.right = z;
        }

        insertFixup(z);
    }

    private void insertFixup(RBNode z) {
        while (z.p.col == RBNode.Color.RED) {
            if (z.p == z.p.p.left) {
                RBNode y = z.p.p.right;
                if (y.col == RBNode.Color.RED) {
                    z.p.col = RBNode.Color.BLACK;
                    y.col = RBNode.Color.BLACK;
                    z.p.p.col = RBNode.Color.RED;
                    z = z.p.p;
                } else {
                    if (z == z.p.right) {
                        z = z.p;
                        leftRotate(z);
                    }
                    z.p.col = RBNode.Color.BLACK;
                    z.p.p.col = RBNode.Color.RED;
                    rightRotate(z.p.p);
                }
            } else {
                RBNode y = z.p.p.left;
                if (y.col == RBNode.Color.RED) {
                    z.p.col = RBNode.Color.BLACK;
                    y.col = RBNode.Color.BLACK;
                    z.p.p.col = RBNode.Color.RED;
                    z = z.p.p;
                } else {
                    if (z == z.p.left) {
                        z = z.p;
                        rightRotate(z);
                    }
                    z.p.col = RBNode.Color.BLACK;
                    z.p.p.col = RBNode.Color.RED;
                    leftRotate(z.p.p);
                }
            }
        }
        this.root.col = RBNode.Color.BLACK;
    }
}