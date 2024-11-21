import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class Quadtree{

// Attributes
private Point bottomLeft;  // Bottom-left corner of the region
private Point topRight;    // Top-right corner of the region
private Point divisionPoint; // Division point for internal nodes
private char color;        // Color of the region (only for leaf nodes)
private Quadtree[] children; // Four children representing the quadrants

// Constructors

    // Constructor for leaf nodes
    public Quadtree(Point bottomLeft, Point topRight, char color) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.color = color;
        this.children = null; 
        this.divisionPoint = null;
    }

    // Constructor for internal nodes
    public Quadtree(Point bottomLeft, Point topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.color = '\0'; // No color for internal nodes
        this.children = null; // Will be initialized upon division
        this.divisionPoint = null;
    }

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

    public Quadtree[] getChildren() {
        return this.children;
    }

// Setters
    public void setDivisionPoint(Point divisionPoint) {
        this.divisionPoint = divisionPoint;
    }

    public void setColor(char color) {
        this.color = color;
    }

//Methods 

    public boolean estFeuille() {
        return this.children == null;
    }

    //Methode Projet

    public void diviser(Point divisionPoint) {
        if (!this.estFeuille()) {
            // Node is already divided
            return;
        }
    
        this.divisionPoint = divisionPoint;
        this.children = new Quadtree[4];
    
        int midX = divisionPoint.getX();
        int midY = divisionPoint.getY();
    
        // NW Quadrant (children[0])
        children[0] = new Quadtree(
            new Point(this.bottomLeft.getX(), midY),
            new Point(midX, this.topRight.getY()),
            divisionPoint.getC1()
        );
    
        // NE Quadrant (children[1])
        children[1] = new Quadtree(
            new Point(midX, midY),
            this.topRight,
            divisionPoint.getC2()
        );
    
        // SE Quadrant (children[2])
        children[2] = new Quadtree(
            divisionPoint,
            new Point(this.topRight.getX(), midY),
            divisionPoint.getC3()
        );
    
        // SW Quadrant (children[3])
        children[3] = new Quadtree(
            this.bottomLeft,
            new Point(midX, midY),
            divisionPoint.getC4()
        );
    
        // After division, this node is no longer a leaf, so reset color
        this.color = '\0';
    }
    
    //Methode Projet

    public Quadtree searchQTree(int x, int y) {
        if (this.estFeuille() || this.divisionPoint == null) {
            return this;
        }
    
        int midX = this.divisionPoint.getX();
        int midY = this.divisionPoint.getY();
    
        if (x < midX) {
            if (y >= midY) {
                // NW Quadrant
                return children[0].searchQTree(x, y);
            } else {
                // SW Quadrant
                return children[3].searchQTree(x, y);
            }
        } else {
            if (y >= midY) {
                // NE Quadrant
                return children[1].searchQTree(x, y);
            } else {
                // SE Quadrant
                return children[2].searchQTree(x, y);
            }
        }
    }
    
    //Methode Projet

    public void addQTree(Point divisionPoint) {
        Quadtree region = this.searchQTree(divisionPoint.getX(), divisionPoint.getY());
        region.diviser(divisionPoint);
    }
    
    
    //Methode Projet

    public void buildQTree(Point[] divisionPoints) {
        for (Point p : divisionPoints) {
            this.addQTree(p);
        }
    }
    
    //Methode Projet

    public void toImage(String filename, int resolution) throws IOException {
        BufferedImage image = new BufferedImage(resolution, resolution, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();

        // Start drawing from the root node
        this.dessiner(g, 0, 0, resolution);

        // Save the image to a file
        ImageIO.write(image, "png", new File(filename));
    }

    //Methode Projet

    private void dessiner(Graphics g, int x, int y, int size) {
        if (this.estFeuille()) {
            g.setColor(couleurToColor(this.color));
            g.fillRect(x, y, size, size);
        } else {
            int halfSize = size / 2;
    
            // NW Quadrant (children[0])
            children[0].dessiner(g, x, y, halfSize);
    
            // NE Quadrant (children[1])
            children[1].dessiner(g, x + halfSize, y, halfSize);
    
            // SE Quadrant (children[2])
            children[2].dessiner(g, x + halfSize, y + halfSize, halfSize);
    
            // SW Quadrant (children[3])
            children[3].dessiner(g, x, y + halfSize, halfSize);
        }
    }

    private Color couleurToColor(char couleur) {
        switch (couleur) {
            case 'R': return Color.RED;
            case 'B': return Color.BLUE;
            case 'Y': return Color.YELLOW;
            case 'K': return Color.BLACK;
            case 'G': return Color.GRAY;
            default: return Color.WHITE;
        }
    }
    
    //Methode Projet

    public String toText() {
        if (this.estFeuille()) {
            return String.valueOf(this.color);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            for (Quadtree child : children) {
                sb.append(child.toText());
            }
            sb.append(")");
            return sb.toString();
        }
    }
    
    //Methode Projet

    public void reColor(int x, int y, char newColor) {
        Quadtree region = this.searchQTree(x, y);
        if (region != null && region.estFeuille()) {
            region.setColor(newColor);
        }
    }
    
    //Methode Projet

    public void compressQTree() {
        if (!this.estFeuille() && this.children != null) {
            // Recursively compress children
            for (Quadtree child : children) {
                child.compressQTree();
            }
    
            // Check if all children are leaves with the same color
            boolean canCompress = true;
            char commonColor = children[0].getColor();
    
            for (Quadtree child : children) {
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