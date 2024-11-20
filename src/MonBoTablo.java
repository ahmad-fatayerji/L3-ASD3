import java.awt.Point;
import java.io.IOException;


public class MonBoTablo {
    public static void main(String[] args) {
        try {
            // Définir les points de base du Quadtree
            Point basGauche = new Point(0, 0);
            Point hautDroit = new Point(8, 8);

            // Créer un Quadtree de base avec une couleur par défaut
            Quadtree quadtree = new Quadtree(basGauche, hautDroit,'N');

            // Tester la division du Quadtree
            System.out.println("=== Division ===");
            quadtree.diviser(new Point(4, 4)); 
            System.out.println("Division effectuée : 4 quadrants créés.");

            // Tester la recherche dans le Quadtree
            System.out.println("\n=== Recherche ===");
            Point recherchePoint = new Point(6, 6);
            Quadtree regionTrouvee = quadtree.searchQTree(recherchePoint.x, recherchePoint.y);
            System.out.println("Point trouvé dans la région : (" +
                regionTrouvee.getPointX().getX() + ", " + regionTrouvee.getPointX().getY() + ") -> (" +
                regionTrouvee.getPointY().getX() + ", " + regionTrouvee.getPointY().getY() + ")");
        }
    }
}