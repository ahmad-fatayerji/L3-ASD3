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
    private Point[] divisionPoints; // Array of division points
    private RecoloringInstruction[] recoloringPairs; // Array of recoloring instructions

    public QuadtreeProcessor(String inputFilePath, String outputDirectory) throws IOException {
        this.outputDirectory = outputDirectory;
        processInputFile(inputFilePath);
    }

    /**
     * Reads the input file and stores the values in the class attributes.
     * @param inputFilePath the path to the input file
     * @throws IOException if the file cannot be read
     */
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
    
        // Store division centers
        Point[] divisionPoints = new Point[m];
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
    
                divisionPoints[i] = new Point(x, y, c1, c2, c3, c4);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.err.println("Error parsing division center at line " + (3 + i) + ": " + line.trim());
            }
        }
    
        // Store division points in the class attribute
        this.divisionPoints = divisionPoints;
    
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
    
    

/**
 * Generates output file names for base and recolored images and texts based on the input file name.
 * 
 * This method extracts the base name from the input file, removes its extension, and constructs
 * four file paths using the given output directory. The generated file names include:
 * - A base image file with a "_B.png" suffix
 * - A base text file with a "_B.txt" suffix
 * - A recolored image file with a "_R.png" suffix
 * - A recolored text file with a "_R.txt" suffix
 * 
 * @param inputFileName the name of the input file from which to derive the base name
 * @return an array of four strings representing the generated file paths
 */
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
    
    /**
     * Generates four output files based on the given file names.
     * 
     * The method first builds the quadtree based on the division points. Then, it generates two pairs of output files: a base image and text pair, and a recolored image and text pair. The base pair is generated before any recoloring operations, and the recolored pair is generated after all recoloring operations have been applied. The generated files are written to the specified output directory.
     * 
     * @param fileNames an array of four strings representing the output file names. The order of the file names is important: the first two elements represent the base image and text files, and the last two elements represent the recolored image and text files.
     * @throws IOException if the output directory does not exist or if any of the files cannot be written.
     */
    public void generateFiles(String[] fileNames) throws IOException {
        // Ensure the output directory exists
        File dir = new File(outputDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    
        // Build the quadtree
        quadtree.buildQTree(divisionPoints);
    
        // Generate the base outputs
        quadtree.toImage(fileNames[0], imageSize, thickness); // Base image
        System.out.println("Base image generated: " + fileNames[0]);
    
        String baseQuadtreeText = quadtree.toText();
        try (FileWriter writer = new FileWriter(fileNames[1])) { // Base text
            writer.write(baseQuadtreeText);
        }
        System.out.println("Base quadtree text written to: " + fileNames[1]);
    
        // Apply recoloring operations
        for (RecoloringInstruction instruction : recoloringPairs) {
            quadtree.reColor(instruction.getX(), instruction.getY(), instruction.getNewColor());
            quadtree.compressQTree();
        }
    
        // Generate the recolored outputs
        quadtree.toImage(fileNames[2], imageSize, thickness); // Recolored image
        System.out.println("Recolored image generated: " + fileNames[2]);
    
        String recoloredQuadtreeText = quadtree.toText();
        try (FileWriter writer = new FileWriter(fileNames[3])) { // Recolored text
            writer.write(recoloredQuadtreeText);
        }
        System.out.println("Recolored quadtree text written to: " + fileNames[3]);
    }
     
}
