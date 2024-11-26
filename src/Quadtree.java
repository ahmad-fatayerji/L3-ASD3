import java.awt.Color;
import java.io.IOException;
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

    /**
     * Returns the bottom-left corner of the region.
     * @return the bottom-left corner of the region
     */
    public Point getBottomLeft() {
        return this.bottomLeft;
    }

    /**
     * Returns the top-right corner of the region.
     * @return the top-right corner of the region
     */
    public Point getTopRight() {
        return this.topRight;
    }

    /**
     * Returns the division point for this node, or null if this is a leaf node.
     * @return the division point for this node, or null if this is a leaf node
     */
    public Point getDivisionPoint() {
        return this.divisionPoint;
    }



    /**
     * Returns the color of this node, or '\0' if this is an internal node.
     * @return the color of this node, or '\0' if this is an internal node
     */
    public char getColor() {
        return this.color;
    }

    /**
     * Returns the children of this node, or null if this is a leaf node.
     * @return the children of this node, or null if this is a leaf node
     */
    public Quadtree[] getChildren() {
        return this.children;
    }

// Setters

    /**
     * Sets the division point for this node. This should only be called on an
     * internal node. Calling this on a leaf node will have no effect.
     * @param divisionPoint the division point for this node
     */
    public void setDivisionPoint(Point divisionPoint) {
        this.divisionPoint = divisionPoint;
    }


/**
 * Sets the color of this node. This should only be called on a leaf node.
 * Calling this on an internal node will have no effect as internal nodes
 * do not have a color.
 * 
 * @param color the color to set for this node
 */
    public void setColor(char color) {
        this.color = color;
    }

//Methods 





    /**
     * Returns true if this node is a leaf node, false otherwise.
     * A leaf node is a node which has no children, i.e. a node which does not
     * divide the image any further.
     * @return true if this node is a leaf node, false otherwise
     */

    public boolean estFeuille() {
        return this.children == null;
    }

    //Methode Projet


/**
 * Divides the current quadtree node into four quadrants based on the given division point.
 * 
 * This method should only be called on a leaf node. If the node is already divided, the method
 * will return without performing any operation. The division point must lie within the current
 * region; otherwise, an error message will be printed, and the division will not occur.
 * 
 * After a successful division, the current node becomes an internal node with four children,
 * each representing a quadrant of the original region. The color of the current node is reset
 * as internal nodes do not have a color.
 * 
 * @param divisionPoint the point at which to divide the current node, which must be within the
 *                      bounds of the current region.
 */
    public void diviser(Point divisionPoint) {
        if (!this.estFeuille()) {
            // Node is already divided
            return;
        }
    
        // Check if the division point lies within the current region
        if (divisionPoint.getX() <= this.bottomLeft.getX() || divisionPoint.getX() >= this.topRight.getX()
            || divisionPoint.getY() <= this.bottomLeft.getY() || divisionPoint.getY() >= this.topRight.getY()) {
            System.err.println("Division point is outside the current region.");
            return;
        }
    
        this.divisionPoint = divisionPoint;
        this.children = new Quadtree[4];
    
        int dx = divisionPoint.getX();
        int dy = divisionPoint.getY();
    
        // NW Quadrant (children[0]): x0 <= x <= dx, dy <= y <= y1
        children[0] = new Quadtree(
            new Point(this.bottomLeft.getX(), dy),
            new Point(dx, this.topRight.getY()),
            divisionPoint.getC1()
        );
    
        // NE Quadrant (children[1]): dx <= x <= x1, dy <= y <= y1
        children[1] = new Quadtree(
            new Point(dx, dy),
            this.topRight,
            divisionPoint.getC2()
        );
    
        // SE Quadrant (children[2]): dx <= x <= x1, y0 <= y <= dy
        children[2] = new Quadtree(
            new Point(dx, this.bottomLeft.getY()),
            new Point(this.topRight.getX(), dy),
            divisionPoint.getC3()
        );
    
        // SW Quadrant (children[3]): x0 <= x <= dx, y0 <= y <= dy
        children[3] = new Quadtree(
            this.bottomLeft,
            new Point(dx, dy),
            divisionPoint.getC4()
        );
    
        // After division, this node is no longer a leaf, so reset color
        this.color = '\0';
    } 
    
    //Methode Projet


    /**
     * Search for a point in the quadtree.
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return the region in the quadtree that contains the point, or null if no such region exists
     */
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

    /**
     * Adds a division point to the quadtree by locating the corresponding region
     * and dividing it. This method will search for the region containing the 
     * given division point and then initiate a division at that point.
     * 
     * @param divisionPoint the point at which to divide the quadtree, which 
     *                      must lie within one of the quadtree's regions
     */
    public void addQTree(Point divisionPoint) {
        Quadtree region = this.searchQTree(divisionPoint.getX(), divisionPoint.getY());
        region.diviser(divisionPoint);
    }
    
    
    //Methode Projet

    /**
     * Builds the quadtree based on the given division points. This method
     * calls addQTree for each division point, effectively dividing the
     * quadtree at each point.
     * @param divisionPoints the points at which to divide the quadtree
     */
    public void buildQTree(Point[] divisionPoints) {
        for (Point p : divisionPoints) {
            this.addQTree(p);
        }
    }
    
    //Methode Projet


    /**
     * Draws the quadtree on the given image using the given color. If the quadtree
     * is a leaf node, it will draw the corresponding rectangle on the image.
     * If the quadtree is an internal node, it will recursively draw its children.
     * 
     * @param img the image to draw the quadtree on
     * @param imageSize the size of the image
     * @param thickness the thickness of the border to draw around the rectangles
     */
    private void dessiner(Image img, int imageSize, int thickness) {
        if (this.estFeuille()) {
            // Determine the coordinates in quadtree space
            int xStart = this.bottomLeft.getX();
            int yStart = this.bottomLeft.getY();
            int xEnd = this.topRight.getX();
            int yEnd = this.topRight.getY();

            // Map to image coordinates
            int yStartImg = imageSize - yEnd;
            int yEndImg = imageSize - yStart;

            // Ensure coordinates are within image bounds
            xStart = Math.max(0, xStart);
            xEnd = Math.min(imageSize, xEnd);
            yStartImg = Math.max(0, yStartImg);
            yEndImg = Math.min(imageSize, yEndImg);

            // Ensure start < end
            if (xStart >= xEnd || yStartImg >= yEndImg) {
                // Invalid rectangle, skip drawing
                return;
            }

            // Set the rectangle
            Color color = couleurToColor(this.color);
            img.setRectangle(xStart, xEnd, yStartImg, yEndImg, color);

            // Draw borders if thickness > 0
            if (thickness > 0 ) {

                Color borderColor = Color.BLACK;

                // Left border
                int leftBorderEndX = Math.min(xStart + thickness, xEnd);
                if (xStart < leftBorderEndX) {
                    img.setRectangle(xStart, leftBorderEndX, yStartImg, yEndImg, borderColor);
                }

                // Right border
                int rightBorderStartX = Math.max(xEnd - thickness, xStart);
                if (rightBorderStartX < xEnd) {
                    img.setRectangle(rightBorderStartX, xEnd, yStartImg, yEndImg, borderColor);
                }

                // Top border
                int topBorderEndY = Math.min(yStartImg + thickness, yEndImg);
                if (yStartImg < topBorderEndY) {
                    img.setRectangle(xStart, xEnd, yStartImg, topBorderEndY, borderColor);
                }

                // Bottom border
                int bottomBorderStartY = Math.max(yEndImg - thickness, yStartImg);
                if (bottomBorderStartY < yEndImg) {
                    img.setRectangle(xStart, xEnd, bottomBorderStartY, yEndImg, borderColor);
                }
            }
        } else {
            // Recursively draw children
            for (Quadtree child : children) {
                child.dessiner(img, imageSize, thickness);
            }
        }
    }

    //Methode Projet

        /**
         * Generates an image representation of the quadtree and saves it to a file.
         * 
         * This method creates a new image with the specified size, draws the quadtree
         * on it using the specified thickness for the borders, and saves the resulting
         * image to the given filename.
         * 
         * @param filename the name of the file to save the image to
         * @param imageSize the size of the image (both width and height) in pixels
         * @param thickness the thickness of the borders to draw around the rectangles
         * @throws IOException if an error occurs during file writing
         */
    public void toImage(String filename, int imageSize, int thickness ) throws IOException {
        // Create an image
        Image img = new Image(imageSize, imageSize);

        // Start drawing from the root node
        this.dessiner(img, imageSize, thickness);

        // Save the image to a file
        img.save(filename);
    }


/**
 * Converts a character representing a color to a corresponding Color object.
 * 
 * The mapping is as follows:
 * - 'R' -> Color.RED
 * - 'B' -> Color.BLUE
 * - 'J' -> Color.YELLOW
 * - 'N' -> Color.BLACK
 * - 'G' -> Color.GRAY
 * 
 * If the character does not match any of the predefined color codes, it defaults to Color.WHITE.
 * 
 * @param couleur the character representing the color
 * @return the corresponding Color object
 */
    private Color couleurToColor(char couleur) {
        switch (couleur) {
            case 'R': return Color.RED;
            case 'B': return Color.BLUE;
            case 'J': return Color.YELLOW;
            case 'N': return Color.BLACK;
            case 'G': return Color.GRAY;
            default: return Color.WHITE;
        }
    }
    
    //Methode Projet

    /**
     * Converts the quadtree to a text representation.
     * 
     * The text representation is a string of characters where each character represents a color.
     * If the quadtree is a leaf node, it simply returns the color of the node as a string.
     * If the quadtree is an internal node, it recursively generates the text representation of its children and
     * surrounds them with parentheses.
     * @return the text representation of the quadtree
     */
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

    /**
     * Recolors the region containing the given point.
     * 
     * The method first searches for the region containing the given point using the searchQTree method.
     * If the region is found, it checks if the region is a leaf node. If it is, it applies the recoloring operation
     * by setting the color of the region to the given new color.
     * 
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @param newColor the new color to set for the region
     */
    public void reColor(int x, int y, char newColor) {
    Quadtree region = this.searchQTree(x, y);
    if (region != null && region.estFeuille()) {
        // Apply recoloring
        region.setColor(newColor);
    }
}

    
    //Methode Projet

    /**
     * Compresses the quadtree by removing unnecessary internal nodes.
     * 
     * This method recursively checks if all children of an internal node are leaves with the same color.
     * If so, it removes the children and makes the node a leaf with the common color.
     * This process is repeated until the tree is fully compressed.
     */
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