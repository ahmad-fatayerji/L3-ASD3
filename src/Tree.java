import java.io.IOException;

public abstract class Tree {
    // Shared Attributes
    protected Point bottomLeft;   // Bottom-left corner of the region
    protected Point topRight;     // Top-right corner of the region
    protected Point divisionPoint; // Division point for internal nodes
    protected char color;         // Color of the region 
    protected Tree[] children;    // Children nodes 

    // Getters
    public Point getBottomLeft() {
        return this.bottomLeft;
    }

    public Point getTopRight() {
        return this.topRight;
    }

    public Point getDivisionPoint() {
        return this.divisionPoint;
    }

    public char getColor() {
        return this.color;
    }

    public Tree[] getChildren() {
        return this.children;
    }

    // Setters
    public void setDivisionPoint(Point divisionPoint) {
        this.divisionPoint = divisionPoint;
    }

    public void setColor(char color) {
        this.color = color;
    }

    // Methods
    public boolean estFeuille() {
        return this.children == null;
    }

    // Abstract Methods
    public abstract void diviser(Point divisionPoint);

    public abstract Tree searchQTree(int x, int y);

    protected abstract void dessiner(Image img, int imageSize, int thickness);

    // Common Methods

    // O(log(n)) pour trouver la région appropriée et diviser.
    public void addQTree(Point divisionPoint) {
        Tree region = this.searchQTree(divisionPoint.getX(), divisionPoint.getY());
        region.diviser(divisionPoint);
    }

    // O(m * log(n)), où m est le nombre de points de division.
    public void buildQTree(Point[] divisionPoints) {
        for (Point p : divisionPoints) {
            this.addQTree(p);
        }
    }

    //  O(n), où n est le nombre de feuilles.
    public void toImage(String filename, int imageSize, int thickness) throws IOException {
        
        Image img = new Image(imageSize, imageSize);

        this.dessiner(img, imageSize, thickness);

        img.save(filename);
    }

    // O(n), où n est le nombre de nœuds dans l'arbre.
    public String toText() {
        if (this.estFeuille()) {
            return String.valueOf(this.color);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            for (Tree child : children) {
                sb.append(child.toText());
            }
            sb.append(")");
            return sb.toString();
        }
    }

    // O(log(n)) pour trouver une région et recolorer.
    public void reColor(int x, int y, char newColor) {
        Tree region = this.searchQTree(x, y);
        if (region != null && region.estFeuille()) {
            region.setColor(newColor);
        }
    }

    //  O(n) pour vérifier et compresser les feuilles.
    public void compressQTree() {
        if (!this.estFeuille() && this.children != null) {
            for (Tree child : children) {
                child.compressQTree();
            }

            // Check if all children are leaves with the same color
            boolean canCompress = true;
            char commonColor = children[0].getColor();

            for (Tree child : children) {
                if (!child.estFeuille() || child.getColor() != commonColor) {
                    canCompress = false;
                    break;
                }
            }

            // If can compress, make this node a leaf
            if (canCompress) {
                this.children = null;
                this.color = commonColor;
                this.divisionPoint = null;
            }
        }
    }
}
