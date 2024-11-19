
public class Point{
    //test
    private int x; // Coordonnée X
    private int y; // Coordonnée Y
    private char couleurNO; // Couleur pour la région Nord-Ouest
    private char couleurNE; // Couleur pour la région Nord-Est
    private char couleurSE; // Couleur pour la région Sud-Est
    private char couleurSO; // Couleur pour la région Sud-Ouest

    // Constructeur
    public Point(int x, int y, char couleurNO, char couleurNE, char couleurSE, char couleurSO) {
        this.x = x;
        this.y = y;
        this.couleurNO = couleurNO;
        this.couleurNE = couleurNE;
        this.couleurSE = couleurSE;
        this.couleurSO = couleurSO;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getCouleurNO() {
        return couleurNO;
    }

    public char getCouleurNE() {
        return couleurNE;
    }

    public char getCouleurSE() {
        return couleurSE;
    }

    public char getCouleurSO() {
        return couleurSO;
    }
}