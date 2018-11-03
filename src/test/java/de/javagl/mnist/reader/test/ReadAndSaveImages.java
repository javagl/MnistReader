/*
 * MnistReader - Copyright (c) 2016-2018 Marco Hutter - http://www.javagl.de
 */ 
package de.javagl.mnist.reader.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import de.javagl.mnist.reader.MnistCompressedReader;
import de.javagl.mnist.reader.MnistEntry;

/**
 * An example showing how to use the MNIST reader to read the MNIST
 * input files from a local directory, and write the images as PNG
 * files to a local directory.<br>
 * <br>
 * Warning: This will write 70000 images, which is quite a lot, and
 * may take a while...
 */
public class ReadAndSaveImages
{
    /**
     * Entry point of this example
     * 
     * @param args Not used
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        readAndSaveImages();
    }

    /**
     * Read the MNIST data from a local directory, and store the
     * MNIST images in the given output directory, separated
     * into training and testing images.
     * The directory is assumed to contain the original MNIST files.<br>
     * <br>
     * 
     * @param inputDirectoryPath The input directory
     * @param outputDirectoryPath The output directory
     * @throws IOException If an IO error occurs
     */
    private static void readAndSaveImages() throws IOException
    {
        Path inputDirectoryPath = Paths.get("./data/mnist");
        Path outputDirectoryPath = Paths.get("./data/mnistImages");

        MnistCompressedReader mnistReader = new MnistCompressedReader();

        System.out.println("Creating training images...");
        Path trainOutputPath = outputDirectoryPath.resolve("train");
        trainOutputPath.toFile().mkdirs();
        Consumer<MnistEntry> trainConsumer = 
            mnistEntry -> saveAsImageUnchecked(mnistEntry, trainOutputPath);
            mnistReader.readCompressedTraining(inputDirectoryPath, trainConsumer);

            System.out.println("Creating testing images...");
            Path testOutputPath = outputDirectoryPath.resolve("test");
            testOutputPath.toFile().mkdirs();
            Consumer<MnistEntry> testConsumer = 
                mnistEntry -> saveAsImageUnchecked(mnistEntry, testOutputPath);
                mnistReader.readCompressedTesting(inputDirectoryPath, testConsumer);

    }

    /**
     * Save the given {@link MnistEntry} as a PNG image file in the specified 
     * output directory. The file name of the resulting image will be created
     * based on the index and the label of the entry.
     *  
     * @param mnistEntry The {@link MnistEntry}
     * @param outputDirectoryPath The output directory
     * @throws IOException If an IO error occurs
     */
    private static void saveAsImage(
        MnistEntry mnistEntry, Path outputDirectoryPath) throws IOException
    {
        // Construct the file name, containing the index and the label
        String indexString = String.format("%05d", mnistEntry.getIndex());
        String outputFileName =
            "digit-" + indexString + "-label-" + mnistEntry.getLabel() + ".png";

        // Write the resulting image file
        Path outputFilePath = outputDirectoryPath.resolve(outputFileName);
        File outputFile = outputFilePath.toFile();
        System.out.println("Writing " + outputFile);
        BufferedImage image = mnistEntry.createImage();
        ImageIO.write(image, "png", outputFile);
    }

    /**
     * Passes the call to {@link #saveAsImage(MnistEntry, Path)}, printing
     * an error message if an exception is thrown.
     *  
     * @param mnistEntry The {@link MnistEntry}
     * @param outputDirectoryPath The output directory
     */
    private static void saveAsImageUnchecked(
        MnistEntry mnistEntry, Path outputDirectoryPath)
    {
        try
        {
            saveAsImage(mnistEntry, outputDirectoryPath);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }



}
