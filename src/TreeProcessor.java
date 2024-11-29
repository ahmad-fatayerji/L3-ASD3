import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TreeProcessor {

    private Tree tree; 
    private int imageSize;
    private int thickness;
    private String outputDirectory;
    private Point[] divisionPoints; 
    private RecoloringInstruction[] recoloringPairs; 

    public TreeProcessor(String inputFilePath, String outputDirectory, int variant) throws IOException {
        this.outputDirectory = outputDirectory;
        processInputFile(inputFilePath, variant);
    }

    // O(m + k), où m est le nombre de points de division et k le nombre de recolorations.
    private void processInputFile(String inputFilePath, int variant) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
        String line;
    
        // Read image size
        line = reader.readLine();
        if (line == null) throw new IOException("Input file is empty.");
        imageSize = Integer.parseInt(line.trim());
    
        // Initialize the tree based on the variant
        Point bottomLeft = new Point(0, 0);
        Point topRight = new Point(imageSize, imageSize);
        if (variant == 1) {
            tree = new Quadtree(bottomLeft, topRight);
        } else if (variant == 2) {
            tree = new Ternarytree(bottomLeft, topRight);
        } else {
            throw new IllegalArgumentException("Unsupported tree variant: " + variant);
        }
    
        // Read number of division centers
        line = reader.readLine();
        if (line == null) throw new IOException("Unexpected end of file after image size.");
        int m = Integer.parseInt(line.trim());
    
        // Store division centers
        divisionPoints = new Point[m];
        for (int i = 0; i < m; i++) {
            line = reader.readLine();
            if (line == null) throw new IOException("Unexpected end of file while reading division centers.");
            String[] tokens = line.split(",");
            if (variant == 1 && tokens.length == 6) { // Quadtree requires 4 colors
                int x = Integer.parseInt(tokens[0].trim());
                int y = Integer.parseInt(tokens[1].trim());
                char c1 = tokens[2].trim().charAt(0);
                char c2 = tokens[3].trim().charAt(0);
                char c3 = tokens[4].trim().charAt(0);
                char c4 = tokens[5].trim().charAt(0);
                divisionPoints[i] = new Point(x, y, c1, c2, c3, c4);
            } else if (variant == 2 && tokens.length == 5) { // Ternarytree requires 3 colors
                int x = Integer.parseInt(tokens[0].trim());
                int y = Integer.parseInt(tokens[1].trim());
                char c1 = tokens[2].trim().charAt(0);
                char c2 = tokens[3].trim().charAt(0);
                char c3 = tokens[4].trim().charAt(0);
                divisionPoints[i] = new Point(x, y, c1, c2, c3);
            } else {
                throw new IOException("Invalid number of color tokens for the given variant at line " + (i + 3));
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
    
        // Store recoloring pairs
        RecoloringInstruction[] recoloringPairs = new RecoloringInstruction[k];
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
    
                recoloringPairs[i] = new RecoloringInstruction(x, y, newColor);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.err.println("Error parsing recoloring pair at line " + (m + 5 + i) + ": " + line.trim());
            }
        }
    
        // Store recoloring pairs in the class attribute
        this.recoloringPairs = recoloringPairs;
    
        reader.close();
    }
    
    // O(1)
    public String[] generateOutputFileNames(String inputFileName) {
        // Extract the base file name without the extension
        String baseName = new File(inputFileName).getName();
        int extensionIndex = baseName.lastIndexOf(".");
        if (extensionIndex > 0) {
            baseName = baseName.substring(0, extensionIndex);
        }
    
        // Construct file paths
        String baseImageFile = outputDirectory + File.separator + "FatayerjiHalgand" + baseName + "_B.png";
        String baseTextFile = outputDirectory + File.separator + "FatayerjiHalgand" + baseName + "_B.txt";
        String recoloredImageFile = outputDirectory + File.separator + "FatayerjiHalgand" + baseName + "_R.png";
        String recoloredTextFile = outputDirectory + File.separator + "FatayerjiHalgand" + baseName + "_R.txt";
    
        return new String[] { baseImageFile, baseTextFile, recoloredImageFile, recoloredTextFile };
    }
    
    //  O(m * log(n) + k * log(n)), où m est le nombre de divisions et k est le nombre de recolorations.
    public void generateFiles(String[] fileNames) throws IOException {
        // Ensure the output directory exists
        File dir = new File(outputDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    
        // build the quadtree
        tree.buildQTree(divisionPoints);
    
        // Generate the base outputs
        tree.toImage(fileNames[0], imageSize, thickness); // Base image
        System.out.println("Base image generated: " + fileNames[0]);
    
        String treeText = tree.toText();
        try (FileWriter writer = new FileWriter(fileNames[1])) { // Base text
            writer.write(treeText);
        }
        System.out.println("Base quadtree text written to: " + fileNames[1]);
    
        // Apply recoloring operations
        for (RecoloringInstruction instruction : recoloringPairs) {
            tree.reColor(instruction.getX(), instruction.getY(), instruction.getNewColor());
            tree.compressQTree();
        }
    
        // Generate the recolored outputs
        tree.toImage(fileNames[2], imageSize, thickness); // Recolored image
        System.out.println("Recolored image generated: " + fileNames[2]);
    
        String recoloredQuadtreeText = tree.toText();
        try (FileWriter writer = new FileWriter(fileNames[3])) { // Recolored text
            writer.write(recoloredQuadtreeText);
        }
        System.out.println("Recolored quadtree text written to: " + fileNames[3]);
    }
     
}
