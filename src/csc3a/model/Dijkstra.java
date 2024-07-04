package csc3a.model;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import csc3a.model.Graph.CostPathPair;
public class Dijkstra {

    public Dijkstra() { }

    public static Map<Graph.Vertex<MobileClinic>, Graph.CostPathPair<MobileClinic>> getShortestPaths(Graph<MobileClinic> graph, Graph.Vertex<MobileClinic> start) {
        final Map<Graph.Vertex<MobileClinic>, List<Graph.Edge<MobileClinic>>> paths = new HashMap<Graph.Vertex<MobileClinic>, List<Graph.Edge<MobileClinic>>>();
        final Map<Graph.Vertex<MobileClinic>, Graph.CostVertexPair<MobileClinic>> costs = new HashMap<Graph.Vertex<MobileClinic>, Graph.CostVertexPair<MobileClinic>>();

        getShortestPath(graph, start, null, paths, costs);

        final Map<Graph.Vertex<MobileClinic>, Graph.CostPathPair<MobileClinic>> map = new HashMap<Graph.Vertex<MobileClinic>, Graph.CostPathPair<MobileClinic>>();
        for (Graph.CostVertexPair<MobileClinic> pair : costs.values()) {
            int cost = pair.getCost();
            Graph.Vertex<MobileClinic> vertex = pair.getVertex();
            List<Graph.Edge<MobileClinic>> path = paths.get(vertex);
            map.put(vertex, new Graph.CostPathPair<MobileClinic>(cost, path));
        }
        return map;
    }

    public static CostPathPair<MobileClinic> getShortestPath(Graph<MobileClinic> graph, Graph.Vertex<MobileClinic> start, Graph.Vertex<MobileClinic> end) {
        if (graph == null)
            throw (new NullPointerException("Graph must be non-NULL."));

        final boolean hasNegativeEdge = checkForNegativeEdges(graph.getVertices());
        if (hasNegativeEdge)
            throw (new IllegalArgumentException("Negative cost Edges are not allowed."));

        final Map<Graph.Vertex<MobileClinic>, List<Graph.Edge<MobileClinic>>> paths = new HashMap<Graph.Vertex<MobileClinic>, List<Graph.Edge<MobileClinic>>>();
        final Map<Graph.Vertex<MobileClinic>, Graph.CostVertexPair<MobileClinic>> costs = new HashMap<Graph.Vertex<MobileClinic>, Graph.CostVertexPair<MobileClinic>>();
        return getShortestPath(graph, start, end, paths, costs);
    }

    private static CostPathPair<MobileClinic> getShortestPath(Graph<MobileClinic> graph, 
                                                              Graph.Vertex<MobileClinic> start, Graph.Vertex<MobileClinic> end,
                                                              Map<Graph.Vertex<MobileClinic>, List<Graph.Edge<MobileClinic>>> paths,
                                                              Map<Graph.Vertex<MobileClinic>, Graph.CostVertexPair<MobileClinic>> costs) {
        if (graph == null)
            throw (new NullPointerException("Graph must be non-NULL."));
        if (start == null)
            throw (new NullPointerException("start must be non-NULL."));

        boolean hasNegativeEdge = checkForNegativeEdges(graph.getVertices());
        if (hasNegativeEdge)
            throw (new IllegalArgumentException("Negative cost Edges are not allowed."));

        for (Graph.Vertex<MobileClinic> v : graph.getVertices())
            paths.put(v, new ArrayList<Graph.Edge<MobileClinic>>());

        for (Graph.Vertex<MobileClinic> v : graph.getVertices()) {
            if (v.equals(start))
                costs.put(v, new Graph.CostVertexPair<MobileClinic>(0, v));
            else
                costs.put(v, new Graph.CostVertexPair<MobileClinic>(Integer.MAX_VALUE, v));
        }

        final Queue<Graph.CostVertexPair<MobileClinic>> unvisited = new PriorityQueue<Graph.CostVertexPair<MobileClinic>>();
        unvisited.add(costs.get(start));

        while (!unvisited.isEmpty()) {
            final Graph.CostVertexPair<MobileClinic> pair = unvisited.remove();
            final Graph.Vertex<MobileClinic> vertex = pair.getVertex();

            for (Graph.Edge<MobileClinic> e : vertex.getEdges()) {
                final Graph.CostVertexPair<MobileClinic> toPair = costs.get(e.getToVertex()); // O(1)
                final Graph.CostVertexPair<MobileClinic> lowestCostToThisVertex = costs.get(vertex); // O(1)
                final int cost = lowestCostToThisVertex.getCost() + e.getCost();
                if (toPair.getCost() == Integer.MAX_VALUE) {
                   
                    unvisited.remove(toPair);
                    toPair.setCost(cost);
                    unvisited.add(toPair); 

                    // Update the paths
                    List<Graph.Edge<MobileClinic>> set = paths.get(e.getToVertex()); // O(log n)
                    set.addAll(paths.get(e.getFromVertex())); // O(log n)
                    set.add(e);
                } else if (cost < toPair.getCost()) {
                    // Found a shorter path to a reachable vertex

                    unvisited.remove(toPair);
                    toPair.setCost(cost);
                    unvisited.add(toPair); 

                    // Update the paths
                    List<Graph.Edge<MobileClinic>> set = paths.get(e.getToVertex()); 
                    set.clear();
                    set.addAll(paths.get(e.getFromVertex()));
                    set.add(e);
                }
            }

            // Termination conditions
            if (end != null && vertex.equals(end)) {
                // We are looking for shortest path to a specific vertex, we found it.
                break;
            }
        }

        if (end != null) {
            final Graph.CostVertexPair<MobileClinic> pair = costs.get(end);
            final List<Graph.Edge<MobileClinic>> set = paths.get(end);
            return (new Graph.CostPathPair<MobileClinic>(pair.getCost(), set));
        }
        return null;
    }

    private static boolean checkForNegativeEdges(Collection<Graph.Vertex<MobileClinic>> vertitices) {
        for (Graph.Vertex<MobileClinic> v : vertitices) {
            for (Graph.Edge<MobileClinic> e : v.getEdges()) {
                if (e.getCost() < 0)
                    return true;
            }
        }
        return false;
    }
}