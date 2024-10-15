# Use an OpenJDK image compatible with Java 1.7
FROM openjdk:7

# Set the working directory inside the container
WORKDIR /app

# Copy the project source files into the container
COPY ./src /app/src

# Copy the input data files
COPY ./data /app/data

# Ensure that the bin and output directories exist
RUN mkdir -p /app/bin /app/output

# Compile the project to the bin folder
RUN javac -source 1.7 -target 1.7 -d /app/bin /app/src/MonBoTablo.java

# Default command to execute the program (adjust if MonBoTablo class package is used)
CMD ["java", "-classpath", "/app/bin", "MonBoTablo", "1", "/app/data/input.txt", "/app/output"]
