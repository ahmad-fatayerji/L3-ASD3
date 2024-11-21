import java.io.IOException;

public class MonBoTablo {

    public static void main(String[] args) {
        // Check for correct number of arguments
        if (args.length != 3) {
            System.err.println("Usage: java MonBoTablo <variant> <input_file_path> <output_directory>");
            return;
        }

        // Parse command-line arguments
        int variant = Integer.parseInt(args[0]);
        String inputFilePath = args[1];
        String outputDirectory = args[2];

        if (variant != 1) {
            System.err.println("Variant " + variant + " is not supported.");
            return;
        }

        // Create a QuadtreeProcessor instance and generate output files
        try {
            QuadtreeProcessor processor = new QuadtreeProcessor(inputFilePath, outputDirectory);
            processor.generateOutputFiles();
        } catch (IOException e) {
            System.err.println("Error processing quadtree: " + e.getMessage());
        }
    }
}
