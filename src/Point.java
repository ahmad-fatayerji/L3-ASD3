public class Point {

    private int x; // Coordonnée X
    private int y; // Coordonnée Y
    private char c1, c2, c3, c4; // Couleurs pour les quadrants NO, NE, SE, SO

    // Constructeur
    // O(1)
    public Point(int x, int y, char c1, char c2, char c3, char c4) {
        this.x = x;
        this.y = y;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.c4 = c4;
    }

    // Constructeur
    // O(1)
    public Point(int x, int y, char c1, char c2, char c3) {
        this.x = x;
        this.y = y;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

    // Constructeur
    // O(1)
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters
    // O(1) pour tous ici
    public int getX() { return x; }
    public int getY() { return y; }
    public char getC1() { return c1; }
    public char getC2() { return c2; }
    public char getC3() { return c3; }
    public char getC4() { return c4; }
}
