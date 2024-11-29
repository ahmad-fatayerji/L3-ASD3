import java.awt.Color;

public class Ternarytree extends Tree {

    // Constructors

    // Constructor for leaf nodes
    public Ternarytree(Point bottomLeft, Point topRight, char color) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.color = color;
        this.children = null;
        this.divisionPoint = null;
    }

    // Constructor for internal nodes
    public Ternarytree(Point bottomLeft, Point topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.color = '\0'; // No color for internal nodes
        this.children = null; // Will be initialized upon division
        this.divisionPoint = null;
    }

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
        this.children = new Tree[3]; // Ternary tree has exactly three children

        int dx = divisionPoint.getX();
        int dy = divisionPoint.getY();

        // West (W) Region: x0 <= x <= dx, y0 <= y <= y1
        children[0] = new Ternarytree(
            this.bottomLeft,
            new Point(dx, this.topRight.getY()),
            divisionPoint.getC1()
        );

        // Northeast (NE) Region: dx <= x <= x1, dy <= y <= y1
        children[1] = new Ternarytree(
            new Point(dx, dy),
            this.topRight,
            divisionPoint.getC2()
        );

        // Southeast (SE) Region: dx <= x <= x1, y0 <= y <= dy
        children[2] = new Ternarytree(
            new Point(dx, this.bottomLeft.getY()),
            new Point(this.topRight.getX(), dy),
            divisionPoint.getC3()
        );

        // After division, this node is no longer a leaf, so reset color
        this.color = '\0';
    }

    @Override
    public Tree searchQTree(int x, int y) {
        if (this.estFeuille() || this.divisionPoint == null) {
            return this;
        }

        int dx = this.divisionPoint.getX();
        int dy = this.divisionPoint.getY();

        if (x <= dx) {
            // West (W) region
            return children[0].searchQTree(x, y);
        } else if (y >= dy) {
            // Northeast (NE) region
            return children[1].searchQTree(x, y);
        } else {
            // Southeast (SE) region
            return children[2].searchQTree(x, y);
        }
    }

    @Override
    protected void dessiner(Image img, int imageSize, int thickness) {
        if (this.estFeuille()) {
            // Determine the coordinates in ternary tree space
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
            if (thickness > 0) {
                thickness = (thickness - 1) / 2 + 1;
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
            for (Tree child : children) {
                child.dessiner(img, imageSize, thickness);
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
