import java.awt.Color;
public class Quadtree extends Tree {

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


    // O(1)
    @Override
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
        this.children = new Tree[4];

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


    // O(log(n)) dans le pire des cas, où n est la taille de l'arbre.
    @Override
    public Tree searchQTree(int x, int y) {
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

    // O(n) dans le pire des cas, où n est le nombre de feuilles.
    @Override
    protected void dessiner(Image img, int imageSize, int thickness) {
        if (this.estFeuille()) {
            // Leaf node: Fill its region with its color.

            // Map quadtree coordinates to image coordinates
            int xStart = Math.max(0, this.bottomLeft.getX());
            int xEnd = Math.min(imageSize, this.topRight.getX());
            int yStartImg = Math.max(0, imageSize - this.topRight.getY());
            int yEndImg = Math.min(imageSize, imageSize - this.bottomLeft.getY());

            if (xStart >= xEnd || yStartImg >= yEndImg) {
                // Invalid rectangle, skip drawing
                return;
            }

            // Fill the rectangle with the leaf node's color
            Color color = couleurToColor(this.color);
            img.setRectangle(xStart, xEnd, yStartImg, yEndImg, color);

            // Do not draw any borders
        } else {
            // First, recursively draw the children
            for (Tree child : children) {
                child.dessiner(img, imageSize, thickness);
            }

            // Now, draw the division lines at the division point
            if (this.divisionPoint != null) {
                int xDiv = this.divisionPoint.getX();
                int yDiv = this.divisionPoint.getY();

                // Map division point to image coordinates
                int xDivImg = xDiv;
                int yDivImg = imageSize - yDiv;

                // Calculate half of the thickness
                int halfThickness = thickness / 2;

                Color lineColor = Color.BLACK;

                // Get the bounds of the current region in image coordinates
                int xStartRegion = Math.max(0, this.bottomLeft.getX());
                int xEndRegion = Math.min(imageSize, this.topRight.getX());
                int yStartRegionImg = Math.max(0, imageSize - this.topRight.getY());
                int yEndRegionImg = Math.min(imageSize, imageSize - this.bottomLeft.getY());

                // Draw vertical line at x = xDiv, within the current region's bounds
                int xLineStart = xDivImg - halfThickness;
                int xLineEnd = xDivImg + halfThickness + 1; // +1 because end index is exclusive

                xLineStart = Math.max(xLineStart, xStartRegion);
                xLineEnd = Math.min(xLineEnd, xEndRegion);

                if (xLineStart < xLineEnd) {
                    img.setRectangle(
                        xLineStart,
                        xLineEnd,
                        yStartRegionImg,
                        yEndRegionImg,
                        lineColor
                    );
                }

                // Draw horizontal line at y = yDiv, within the current region's bounds
                int yLineStart = yDivImg - halfThickness;
                int yLineEnd = yDivImg + halfThickness + 1; // +1 because end index is exclusive

                yLineStart = Math.max(yLineStart, yStartRegionImg);
                yLineEnd = Math.min(yLineEnd, yEndRegionImg);

                if (yLineStart < yLineEnd) {
                    img.setRectangle(
                        xStartRegion,
                        xEndRegion,
                        yLineStart,
                        yLineEnd,
                        lineColor
                    );
                }
            }
        }
    }


    // Utility method to convert character to Color
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
}
