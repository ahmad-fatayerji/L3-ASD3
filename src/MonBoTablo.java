



public class MonBoTablo {
    public static void main(String[] args) {
        // Définir les points de base du Quadtree
        Point basGauche = new Point(0, 0);
        Point hautDroit = new Point(8, 8);

        // Créer un Quadtree de base avec une couleur par défaut
        Quadtree quadtree = new Quadtree(basGauche, hautDroit, 'N');

        // Tester la division du Quadtree
        System.out.println("=== Division ===");
        Point divisionPoint = new Point(4, 4);
        quadtree.diviser(divisionPoint);
        System.out.println("Division effectuée : 4 quadrants créés.");

        // Tester la recherche dans le Quadtree
        System.out.println("\n=== Recherche ===");
        Point recherchePoint = new Point(6, 6);
        Quadtree regionTrouvee = quadtree.searchQTree(recherchePoint.getX(), recherchePoint.getY());
        System.out.println("Point trouvé dans la région : (" +
                regionTrouvee.getPointX().getX() + ", " + regionTrouvee.getPointX().getY() + ") -> (" +
                regionTrouvee.getPointY().getX() + ", " + regionTrouvee.getPointY().getY() + ")");

        // Test supplémentaire
        System.out.println("\n=== Test supplémentaire ===");
        quadtree.reColor(6, 6, 'R');
        System.out.println("Couleur de la région modifiée à 'R'.");
    }
}