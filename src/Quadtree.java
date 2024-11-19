import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Quadtree{

    // Attributs
    private Point pointX;//point en bas a droite de la region
    private Point pointY; //point en haut a gauche de la region
    private Point point; // Point de division pour ce nœud
    private char couleur; // Couleur de la région (uniquement pour les feuilles)
    private Quadtree[] enfants; // Quatre enfants représentant les quadrants
    
    // Constructeur pour les feuilles
    public Quadtree(Point pointX,Point pointY char couleur) {
        this.niveau = 0;
        this.pointX = pointX;
        this.pointY = pointY;
        this.couleur = couleur;
        this.enfants = null; // Les feuilles n'ont pas d'enfants
    }

    // Constructeur pour les nœuds non-feuilles
    public Quadtree(Point pointX,Point pointY) {
        this.niveau = 0;
        this.pointX = pointX;
        this.pointY = pointY;       
        this.enfants = new Quadtree[4];
    }

    //Methodes

    /**
     * Recherche la région divisible contenant le point donné.
     * Complexité : O(h), où h est la hauteur de l'arbre.
     *
     * @param px Coordonnée X du point.
     * @param py Coordonnée Y du point.
     * @return Le Quadtree correspondant à la région divisible.
     */
    public Quadtree searchQTree(int px, int py) {
        
        if (this.estFeuille()) {
            return this;
        }

        if (px < point.getX()) {
            if (py < point.getY()) {
                return enfants[3].searchQTree(px, py); // Sud-Ouest
            } else {
                return enfants[0].searchQTree(px, py); // Nord-Ouest
            }
        } else {
            if (py < point.getY()) {
                return enfants[2].searchQTree(px, py); // Sud-Est
            } else {
                return enfants[1].searchQTree(px, py); // Nord-Est
            }
        }
    }

    /**
     * Ajoute un point au Quadtree en divisant la région correspondante.
     * Complexité : O(h), où h est la hauteur de l'arbre.
     *
     * @param nouveauPoint Le point à ajouter.
     */
    public void addQTree(Point nouveauPoint) {
        Quadtree region = this.searchQTree(nouveauPoint.getX(), nouveauPoint.getY());
        region.diviser(nouveauPoint);
    }

    public void diviser(Point nouveauPoint){
        this.point = nouveauPoint;
        this.enfants = new Quadtree[4];
        Point point01 = new Point(this.getpointX().getX(), nouveauPoint.getY());
        Point point02 = new Point(nouveauPoint.getX(), this.getpointY().getY());
        Point point21 = new Point(nouveauPoint.getX(), this.getpointX().getY());
        Point point22 = new Point(this.getpointY().getX(), nouveauPoint.getY());
        this.enfants[0] = new Quadtree(point01, point02);
        this.enfants[1] = new Quadtree(this.point, this.pointY);
        this.enfants[2] = new Quadtree(point21, point22);
        this.enfants[3] = new Quadtree(this.pointX, this.point);
    
    }

    /**
     * Construit le Quadtree en utilisant une liste de points.
     * Complexité : O(m * h), où m est le nombre de points et h la hauteur de l'arbre.
     *
     * @param points Liste de points à insérer dans le Quadtree.
     */
    public void buildQTree(Point[] points) {
        for (Point p : points) {
            this.addQTree(p);
        }
    }

    /**
     * Génère une image PNG à partir du Quadtree.
     * Complexité : O(n²), où n est la résolution de l'image.
     *
     * @param chemin Chemin du fichier PNG de sortie.
     * @param resolution Résolution de l'image (en pixels).
     * @throws IOException En cas d'erreur d'écriture du fichier.
     */
    public void toImage(String chemin, int resolution) throws IOException {
        BufferedImage image = new BufferedImage(resolution, resolution, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();

        this.dessiner(g, 0, 0, resolution);

        ImageIO.write(image, "png", new File(chemin));
    }

    /**
     * Dessine le quadtree sur un objet Graphics.
     *
     * @param g L'objet Graphics pour dessiner.
     * @param x Origine X du dessin.
     * @param y Origine Y du dessin.
     * @param taille Taille de la région à dessiner.
     */
    private void dessiner(Graphics g, int x, int y, int taille) {
        if (this.estFeuille()) {
            g.setColor(couleurToColor(this.couleur));
            g.fillRect(x, y, taille, taille);
        } else {
            int demiTaille = taille / 2;
            enfants[0].dessiner(g, x, y, demiTaille); // Nord-Ouest
            enfants[1].dessiner(g, x + demiTaille, y, demiTaille); // Nord-Est
            enfants[2].dessiner(g, x + demiTaille, y + demiTaille, demiTaille); // Sud-Est
            enfants[3].dessiner(g, x, y + demiTaille, demiTaille); // Sud-Ouest
        }
    }

    /**
     * Convertit une couleur en char en un objet Color.
     *
     * @param couleur La couleur sous forme de char.
     * @return Un objet Color correspondant.
     */
    private Color couleurToColor(char couleur) {
        switch (couleur) {
            case 'R': return Color.RED;
            case 'B': return Color.BLUE;
            case 'J': return Color.YELLOW;
            case 'G': return Color.LIGHT_GRAY;
            case 'N': return Color.BLACK;
            default: return Color.WHITE;
        }
    }

    /**
     * Génère une représentation textuelle parenthésée du Quadtree.
     * Complexité : O(n), où n est le nombre de nœuds.
     *
     * @return Une chaîne de caractères représentant l'arbre.
     */
    public String toText() {
        if (this.estFeuille()) {
            return String.valueOf(this.couleur);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Quadtree enfant : enfants) {
            sb.append(enfant.toText());
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Recolore une région contenant le point donné.
     * Complexité : O(h), où h est la hauteur de l'arbre.
     *
     * @param px Coordonnée X du point.
     * @param py Coordonnée Y du point.
     * @param nouvelleCouleur La nouvelle couleur à appliquer.
     */
    public void reColor(int px, int py, char nouvelleCouleur) {
        Quadtree region = this.searchQTree(px, py);
        region.couleur = nouvelleCouleur;
    }


    /**
     * Compresse le Quadtree en combinant les régions de même couleur.
     * Complexité : O(n), où n est le nombre de nœuds.
     */
    public void compressQTree() {
        if (!this.estFeuille() && enfants != null) {
            for (Quadtree enfant : enfants) {
                enfant.compressQTree();
            }

            // Vérifie si tous les enfants ont la même couleur
            char couleurCommune = enfants[0].couleur;
            boolean memeCouleur = true;

            for (Quadtree enfant : enfants) {
                if (enfant.couleur != couleurCommune || !enfant.estFeuille()) {
                    memeCouleur = false;
                    break;
                }
            }

            // Compresse si tous les enfants ont la même couleur
            if (memeCouleur) {
                this.enfants = null;
                this.couleur = couleurCommune;
            }
        }
    }

}