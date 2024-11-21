import java.io.IOException;

public class MonBoTablo {

    public static void main(String[] args) {
        // Define the size of the image (e.g., 1000 x 1000 pixels)
        int imageSize = 1000;

        // Create the root quadtree node representing the entire image
        Point bottomLeft = new Point(0, 0);
        Point topRight = new Point(imageSize, imageSize);
        Quadtree quadtree = new Quadtree(bottomLeft, topRight);

        // Define division points with their colors
        // For testing purposes, we'll hardcode some division points
        // Each Point includes x, y, c1, c2, c3, c4 (colors for NW, NE, SE, SW)
        Point[] divisionPoints = new Point[] {
            new Point(600, 500, 'R', 'G', 'J', 'B'),
            new Point(900, 400, 'J', 'B', 'N', 'B'),
            new Point(800, 300, 'G', 'R', 'B', 'J'),
            new Point(850, 350, 'R', 'J', 'R', 'J'),
            new Point(540, 120, 'B', 'R', 'J', 'G')
        };

        // Build the quadtree using the division points
        quadtree.buildQTree(divisionPoints);

        // Generate the initial image
        try {
            quadtree.toImage("initial_image.png", imageSize);
            System.out.println("Initial image generated: initial_image.png");
        } catch (IOException e) {
            System.err.println("Error generating image: " + e.getMessage());
        }

        // Output the quadtree structure to text
        String quadtreeText = quadtree.toText();
        System.out.println("Quadtree structure:");
        System.out.println(quadtreeText);

        // Test reColor method
        // Change the color of the region containing point (50, 50) to 'N' (Black)
        quadtree.reColor(50, 50, 'N');

        // Generate the recolored image
        try {
            quadtree.toImage("recolored_image.png", imageSize);
            System.out.println("Recolored image generated: recolored_image.png");
        } catch (IOException e) {
            System.err.println("Error generating recolored image: " + e.getMessage());
        }

        // Compress the quadtree
        quadtree.compressQTree();

        // Output the compressed quadtree structure to text
        String compressedQuadtreeText = quadtree.toText();
        System.out.println("Compressed quadtree structure:");
        System.out.println(compressedQuadtreeText);

        // Generate the compressed image
        try {
            quadtree.toImage("compressed_image.png", imageSize);
            System.out.println("Compressed image generated: compressed_image.png");
        } catch (IOException e) {
            System.err.println("Error generating compressed image: " + e.getMessage());
        }
    }
}
