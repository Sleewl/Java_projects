package pauk;

public class SpiderWeb {
    public static void main(String[] args) {
        SpiderWebPathfinder pathfinder = new SpiderWebPathfinder();

        try {
            System.out.println("Кратчайший путь от H3 до E2: " +
                    pathfinder.findShortestPath("H3", "E2"));
            System.out.println("Кратчайший путь от A4 до B2: " +
                    pathfinder.findShortestPath("A4", "B2"));
            System.out.println("Кратчайший путь от E3 до B2: " +
                    pathfinder.findShortestPath("E3", "B2"));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}






