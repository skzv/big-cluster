package dev.skz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String file = "input.txt";
        int numBits = 0;
        int numVertices = 0;
        Set<Integer> vertices = new HashSet<>();

        try (
                BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                String[] tokens = line.split(" ");
                numVertices = Integer.parseInt(tokens[0]);
                vertices = new HashSet<>(numVertices);
                numBits = Integer.parseInt(tokens[1]);
            }
            while ((line = reader.readLine()) != null) {
                String vertexString = String.join("", line.split(" "));
                int vertex = Integer.parseInt(vertexString, 2);
                vertices.add(vertex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        UnionFind unionFind = new UnionFind(vertices.size(),1 << numBits);
        List<Integer> bitmasks = generateBitMasks(numBits);

        for (int vertex : vertices) {
            for (int bitmask : bitmasks) {
                int neighbour = vertex ^ bitmask;
                if (vertices.contains(neighbour)) {
                    unionFind.union(vertex, neighbour);
                }
            }
        }

        System.out.println(unionFind.getNumClusters());
    }

    /** Generates all bit masks with hamming distances of 1 and 2. */
    public static List<Integer> generateBitMasks(int numBits) {
        List<Integer> bitmasks = new ArrayList<>();

        for (int i = 0; i < numBits; i++) {
            int bitmask = 1 << i;
            // add hamming distance = 1 bitmask
            bitmasks.add(bitmask);
            for (int j = i + 1; j < numBits; j++) {
                int bitmask2 = bitmask ^ 1 << j;
                // add hamming distance = 2 bitmask
                bitmasks.add(bitmask2);
            }
        }

        return bitmasks;
    }

    public static class UnionFind {
        private final int[] parent;
        private final int[] size;
        private int numClusters;

        public UnionFind(int numNodes, int maxNode) {
            parent = new int[maxNode + 1];
            size = new int[maxNode + 1];
            Arrays.fill(size, 1);
            for (int i = 1; i <= maxNode; i++) {
                parent[i] = i;
            }
            numClusters = numNodes;
        }

        /* Returns root parent of node n. */
        public int find(int n) {
            int p = parent[n];
            while (p != parent[p]) {
                p = parent[p];
            }
            return p;
        }

        public void union(int x, int y) {
            // Invoke Find twice to locate the positions i and j of the roots of the parent graph trees that contain
            // x and y, respectively. If i = j, return.
            int i = find(x);
            int j = find(y);
            if (i == j) {
                return;
            }

            // If size(i) >= size(j), set parent(j) := i and size(i) := size(i) + size(j).
            if (size[i] >= size[j]) {
                parent[j] = i;
                size[i] = size[i] + size[j];
            } else {
                // If size(i) < size(j), set parent(i) := j and size(j) := size(i) + size(j).
                parent[i] = j;
                size[j] = size[i] + size[j];
            }

            numClusters--;
        }

        public int getNumClusters() {
            return numClusters;
        }

        // TODO: implement
        public List<List<Integer>> getClusters() {
            return new ArrayList<>(new ArrayList<>());
        }
    }
}
