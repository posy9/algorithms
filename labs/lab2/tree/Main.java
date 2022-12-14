package by.bsu.algorithms.labs.lab2.tree;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();
//        ArrayList<Integer> treeElements = new ArrayList<>();
//        Random random = new Random();
//        int bound = 5000;
//        int amount = 500;
//
//        for (int i = 0; i < amount; i++) {
//            int nextValue = random.nextInt(bound);
//            treeElements.add(nextValue);
//            tree.insert(nextValue);
//        }
//        System.out.println(tree.getKMinimalKey(1));
////        System.out.println("tree elements " + treeElements);
//        System.out.println("k minimal element " + tree.getKMinimalKey(500));
//        System.out.println("nodes " + tree.getNodeCount());
//        System.out.println("k minimal node value " + tree.getKMinimalNodeValue(50));
//        System.out.println("node count " + tree.getNodeCount());
//        System.out.println("tree size " + tree.getSize());
//        System.out.println("tree height " + tree.getTreeHeight());
//        BinarySearchTree tree1 = new BinarySearchTree();
//        tree1.insert(1);
//        tree1.insert(1);
//        tree1.insert(2);
//        System.out.println(tree1.getKMinimalKey(2));
//        System.out.println(tree1.getKMinimalNodeValue(2));
        tree.insert(5);
        tree.insert(9);
        tree.insert(2);
        tree.insert(6);
        tree.insert(8);
//        tree.insert(3);
//        tree.insert(1);
//        tree.insert(10);
//        tree.insert(4);
        tree.printLevelNodeRepresentation();


    }
}
