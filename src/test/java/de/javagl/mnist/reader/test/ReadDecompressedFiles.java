/*
 * MnistReader - Copyright (c) 2016-2018 Marco Hutter - http://www.javagl.de
 */ 
package de.javagl.mnist.reader.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import de.javagl.mnist.reader.MnistDecompressedReader;
import de.javagl.mnist.reader.MnistEntry;

/**
 * An example showing how to use the MNIST reader to read the files
 * that are decompressed (unzipped) from the files that are published at
 * <a href="http://yann.lecun.com/exdb/mnist/">
 * http://yann.lecun.com/exdb/mnist/</a>
 */
public class ReadDecompressedFiles
{
    /**
     * Entry point of this example
     * 
     * @param args Not used
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        readDecompressedFiles();
    }

    /**
     * Read the MNIST data from a local directory, and print information 
     * about the data that is read to the console. The directory is assumed 
     * to contain the uncompressed MNIST files (with their original names, 
     * but without the <code>".gz"</code> extension)
     * 
     * @throws IOException If an IO error occurs
     */
    private static void readDecompressedFiles() throws IOException
    {
        Path inputDirectoryPath = Paths.get("./data");

        MnistDecompressedReader mnistReader = new MnistDecompressedReader();
        Consumer<MnistEntry> consumer = System.out::println;

        System.out.println("Training data:");
        mnistReader.readDecompressedTraining(inputDirectoryPath, consumer);

        System.out.println("Testing data:");
        mnistReader.readDecompressedTesting(inputDirectoryPath, consumer);
    }

}


