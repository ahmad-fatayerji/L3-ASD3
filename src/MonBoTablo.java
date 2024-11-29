import java.io.IOException;

public class MonBoTablo {

    // O(m + k) où m est le nombre de points de division et k est le nombre de recolorations.
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java MonBoTablo <variant> <input_file_path> <output_directory>");
            return;
        }

        try {
            int variant = Integer.parseInt(args[0]);
            String inputFilePath = args[1];
            String outputDirectory = args[2];

            TreeProcessor processor = new TreeProcessor(inputFilePath, outputDirectory, variant);
            String[] fileNames = processor.generateOutputFileNames(inputFilePath);
            processor.generateFiles(fileNames);

        } catch (IOException e) {
            System.err.println("Error processing tree: " + e.getMessage());
        }
    }
}
