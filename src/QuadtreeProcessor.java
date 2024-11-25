import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class QuadtreeProcessor {

    private Quadtree quadtree;
    private int imageSize;
    private int thickness;
    private String outputDirectory;

    public QuadtreeProcessor(String inputFilePath, String outputDirectory) throws IOException {
        this.outputDirectory = outputDirectory;
        processInputFile(inputFilePath);
    }

    private void processInputFile(String inputFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
        String line;
    
        // Read image size
        line = reader.readLine();
        if (line == null) throw new IOException("Input file is empty.");
        imageSize = Integer.parseInt(line.trim());
    
        // Initialize root quadtree
        Point bottomLeft = new Point(0, 0);
        Point topRight = new Point(imageSize, imageSize);
        quadtree = new Quadtree(bottomLeft, topRight);
    
        // Read number of division centers
        line = reader.readLine();
        if (line == null) throw new IOException("Unexpected end of file after image size.");
        int m = Integer.parseInt(line.trim());
    
        // Read division centers
        for (int i = 0; i < m; i++) {
            line = reader.readLine();
            if (line == null) throw new IOException("Unexpected end of file while reading division centers.");
    
            String[] tokens = line.split(",");
            if (tokens.length != 6) {
                System.err.println("Invalid division center format at line " + (3 + i) + ": " + line.trim());
                continue;
            }
            try {
                int x = Integer.parseInt(tokens[0].trim());
                int y = Integer.parseInt(tokens[1].trim());
                char c1 = tokens[2].trim().charAt(0);
                char c2 = tokens[3].trim().charAt(0);
                char c3 = tokens[4].trim().charAt(0);
                char c4 = tokens[5].trim().charAt(0);
    
                quadtree.addQTree(new Point(x, y, c1, c2, c3, c4));
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.err.println("Error parsing division center at line " + (3 + i) + ": " + line.trim());
            }
        }
    
        // Read thickness
        line = reader.readLine();
        if (line == null) throw new IOException("Unexpected end of file after division centers.");
        thickness = Integer.parseInt(line.trim());
        if (thickness < 1 || thickness % 2 == 0) {
            System.err.println("Invalid thickness value. Must be an odd integer >= 1. Defaulting to 1.");
            thickness = 1;
        }
    
        // Read number of recoloring pairs
        line = reader.readLine();
        if (line == null) throw new IOException("Unexpected end of file after thickness.");
        int k = Integer.parseInt(line.trim());
    
        // Read recoloring pairs
        for (int i = 0; i < k; i++) {
            line = reader.readLine();
            if (line == null) throw new IOException("Unexpected end of file while reading recoloring pairs.");
    
            String[] tokens = line.split(",");
            if (tokens.length != 3) {
                System.err.println("Invalid recoloring pair format at line " + (m + 5 + i) + ": " + line.trim());
                continue;
            }
            try {
                int x = Integer.parseInt(tokens[0].trim());
                int y = Integer.parseInt(tokens[1].trim());
                char newColor = tokens[2].trim().charAt(0);
    
                //quadtree.reColor(x, y, newColor);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.err.println("Error parsing recoloring pair at line " + (m + 5 + i) + ": " + line.trim());
            }
        }
    
        reader.close();
    }
    

    public void generateOutputFiles() throws IOException {
        // Ensure the output directory exists
        File dir = new File(outputDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Generate the image
        String imageFilePath = outputDirectory + File.separator + "FatayerjiHalgandFichierEntree_B.png";
        quadtree.toImage(imageFilePath, imageSize, thickness);
        System.out.println("Image generated: " + imageFilePath);

        // Output the quadtree structure to text
        String quadtreeText = quadtree.toText();
        String quadtreeTextFilePath = outputDirectory + File.separator + "FatayerjiHalgandFichierEntree_B.txt";
        FileWriter writer = new FileWriter(quadtreeTextFilePath);
        writer.write(quadtreeText);
        writer.close();
        System.out.println("Quadtree structure written to: " + quadtreeTextFilePath);

        // If other output files are required, generate them here
    }
}
