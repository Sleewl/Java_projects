package pauk;
import java.util.*;

public class SpiderWebPathfinder {

    private static final int SECTORS = 8;
    private static final char[] RINGS = "ABCDEFGH".toCharArray();

    public String findShortestPath(String spider, String fly) throws IllegalArgumentException {
        try {
            validatePoint(spider);
            validatePoint(fly);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Некорректная точка: " + e.getMessage());
        }

        Set<String> visited = new HashSet<>();
        Queue<PathNode> queue = new LinkedList<>();
        queue.add(new PathNode(spider, null));
        visited.add(spider);

        while (!queue.isEmpty()) {
            PathNode current = queue.poll();

            if (current.point.equals(fly)) {
                return buildPath(current);
            }

            List<String> neighbors = getNeighbors(current.point);
            String nextPoint = getClosestNeighbor(current.point, fly, neighbors);

            if (!visited.contains(nextPoint)) {
                queue.add(new PathNode(nextPoint, current));
                visited.add(nextPoint);
            }
        }

        return "Путь не найден";
    }

    private void validatePoint(String point) throws IllegalArgumentException {
        if (point == null || point.length() != 2) {
            throw new IllegalArgumentException("Точка должна быть в формате XY (например, A1).");
        }
        char ring = point.charAt(0);
        int sector;
        try {
            sector = Integer.parseInt(point.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Сектор должен быть числом.");
        }

        if (ring < 'A' || ring > 'H') {
            throw new IllegalArgumentException("Кольцо должно быть от A до H.");
        }
        if (sector < 1 || sector > SECTORS) {
            throw new IllegalArgumentException("Сектор должен быть от 1 до " + SECTORS + ".");
        }
    }

    private List<String> getNeighbors(String point) {
        List<String> neighbors = new ArrayList<>();
        char ring = point.charAt(0);
        int sector = Integer.parseInt(point.substring(1));

        neighbors.add(ring + String.valueOf((sector == 1) ? SECTORS : sector - 1)); // Сосед слева
        neighbors.add(ring + String.valueOf((sector == SECTORS) ? 1 : sector + 1)); // Сосед справа

        if (ring > 'A') neighbors.add((char) (ring - 1) + String.valueOf(sector)); // Сосед по кольцу вверх
        if (ring < 'H') neighbors.add((char) (ring + 1) + String.valueOf(sector)); // Сосед по кольцу вниз

        return neighbors;
    }

    private String getClosestNeighbor(String current, String fly, List<String> neighbors) {
        double minDistance = Double.MAX_VALUE;
        String closest = null;

        for (String neighbor : neighbors) {
            double distance = calculateEuclideanDistance(neighbor, fly); // Вычисляем геометрическое расстояние
            if (distance < minDistance) {
                minDistance = distance;
                closest = neighbor;
            }
        }

        return closest;
    }

    private String buildPath(PathNode node) {
        List<String> path = new ArrayList<>();
        while (node != null) {
            path.add(node.point);
            node = node.previous;
        }
        Collections.reverse(path);
        return String.join(" => ", path);
    }

    private double calculateEuclideanDistance(String point1, String point2) {
        double[] coord1 = pointToCoordinates(point1);
        double[] coord2 = pointToCoordinates(point2);
        return Math.sqrt(Math.pow(coord2[0] - coord1[0], 2) + Math.pow(coord2[1] - coord1[1], 2));
    }

    private double[] pointToCoordinates(String point) {
        int x = point.charAt(0) - 'A'; // Кольца (A - H)
        int y = Integer.parseInt(point.substring(1)) - 1; // Сектора (1 - 8)
        return new double[] { x, y };
    }
}



