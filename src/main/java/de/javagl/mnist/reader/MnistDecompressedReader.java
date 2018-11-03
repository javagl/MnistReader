/*
 * MnistReader - Copyright (c) 2016-2018 Marco Hutter - http://www.javagl.de
 */ 
package de.javagl.mnist.reader;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A class for reading the MNIST data set from the <b>decompressed</b> 
 * (unzipped) files that are published at
 * <a href="http://yann.lecun.com/exdb/mnist/">
 * http://yann.lecun.com/exdb/mnist/</a>. 
 */
public class MnistDecompressedReader
{
    /**
     * Default constructor
     */
    public MnistDecompressedReader()
    {
        // Default constructor
    }
    
    /**
     * Read the MNIST training data from the given directory. The data is 
     * assumed to be located in files with their default names,
     * <b>decompressed</b> from the original files: 
     * extension) : 
     * <code>train-images.idx3-ubyte</code> and
     * <code>train-labels.idx1-ubyte</code>.
     * 
     * @param inputDirectoryPath The input directory
     * @param consumer The consumer that will receive the resulting 
     * {@link MnistEntry} instances
     * @throws IOException If an IO error occurs
     */
    public void readDecompressedTraining(Path inputDirectoryPath, 
        Consumer<? super MnistEntry> consumer) throws IOException
    {
        String trainImagesFileName = "train-images.idx3-ubyte";
        String trainLabelsFileName = "train-labels.idx1-ubyte";
        Path imagesFilePath = inputDirectoryPath.resolve(trainImagesFileName);
        Path labelsFilePath = inputDirectoryPath.resolve(trainLabelsFileName);
        readDecompressed(imagesFilePath, labelsFilePath, consumer);
    }
    
    /**
     * Read the MNIST training data from the given directory. The data is 
     * assumed to be located in files with their default names,
     * <b>decompressed</b> from the original files: 
     * extension) : 
     * <code>t10k-images.idx3-ubyte</code> and
     * <code>t10k-labels.idx1-ubyte</code>.
     * 
     * @param inputDirectoryPath The input directory
     * @param consumer The consumer that will receive the resulting 
     * {@link MnistEntry} instances
     * @throws IOException If an IO error occurs
     */
    public void readDecompressedTesting(Path inputDirectoryPath, 
        Consumer<? super MnistEntry> consumer) throws IOException
    {
        String testImagesFileName = "t10k-images.idx3-ubyte";
        String testLabelsFileName = "t10k-labels.idx1-ubyte";
        Path imagesFilePath = inputDirectoryPath.resolve(testImagesFileName);
        Path labelsFilePath = inputDirectoryPath.resolve(testLabelsFileName);
        readDecompressed(imagesFilePath, labelsFilePath, consumer);
    }
    
    
    /**
     * Read the MNIST data from the specified (decompressed) files.
     * 
     * @param imagesFilePath The path of the images file
     * @param labelsFilePath The path of the labels file
     * @param consumer The consumer that will receive the resulting 
     * {@link MnistEntry} instances
     * @throws IOException If an IO error occurs
     */
    public void readDecompressed(Path imagesFilePath, Path labelsFilePath, 
        Consumer<? super MnistEntry> consumer) throws IOException
    {
        try (InputStream decompressedImagesInputStream = 
                new FileInputStream(imagesFilePath.toFile());
            InputStream decompressedLabelsInputStream = 
                new FileInputStream(labelsFilePath.toFile()))
        {
            readDecompressed(
                decompressedImagesInputStream, 
                decompressedLabelsInputStream, 
                consumer);
        }
    }
    
    /**
     * Read the MNIST data from the given (decompressed) input streams.
     * The caller is responsible for closing the given streams.
     * 
     * @param decompressedImagesInputStream The decompressed input stream
     * containing the image data 
     * @param decompressedLabelsInputStream The decompressed input stream
     * containing the label data
     * @param consumer The consumer that will receive the resulting 
     * {@link MnistEntry} instances
     * @throws IOException If an IO error occurs
     */
    public void readDecompressed(
        InputStream decompressedImagesInputStream, 
        InputStream decompressedLabelsInputStream, 
        Consumer<? super MnistEntry> consumer) throws IOException
    {
        Objects.requireNonNull(consumer, "The consumer may not be null");
        
        DataInputStream imagesDataInputStream = 
            new DataInputStream(decompressedImagesInputStream);
        DataInputStream labelsDataInputStream = 
            new DataInputStream(decompressedLabelsInputStream);
        
        int magicImages = imagesDataInputStream.readInt();
        if (magicImages != 0x803)
        {
            throw new IOException("Expected magic header of 0x803 "
                + "for images, but found " + magicImages);
        }
        
        int magicLabels = labelsDataInputStream.readInt();
        if (magicLabels != 0x801)
        {
            throw new IOException("Expected magic header of 0x801 "
                + "for labels, but found " + magicLabels);
        }
        
        int numberOfImages = imagesDataInputStream.readInt();
        int numberOfLabels = labelsDataInputStream.readInt();

        if (numberOfImages != numberOfLabels)
        {
            throw new IOException("Found " + numberOfImages 
                + " images but " + numberOfLabels + " labels");
        }

        int numRows = imagesDataInputStream.readInt();
        int numCols = imagesDataInputStream.readInt();

        for (int n = 0; n < numberOfImages; n++)
        {
            byte label = labelsDataInputStream.readByte();
            byte imageData[] = new byte[numRows * numCols];
            read(imagesDataInputStream, imageData);
            
            MnistEntry mnistEntry = new MnistEntry(
                n, label, numRows, numCols, imageData);
            consumer.accept(mnistEntry);
        }
    }
    
    /**
     * Read bytes from the given input stream, filling the given array
     * 
     * @param inputStream The input stream
     * @param data The array to be filled
     * @throws IOException If the input stream does not contain enough bytes
     * to fill the array, or any other IO error occurs
     */
    private static void read(InputStream inputStream, byte data[]) 
        throws IOException
    {
        int offset = 0;
        while (true)
        {
            int read = inputStream.read(
                data, offset, data.length - offset);
            if (read < 0)
            {
                break;
            }
            offset += read;
            if (offset == data.length)
            {
                return;
            }
        }
        throw new IOException("Tried to read " + data.length
            + " bytes, but only found " + offset);
    }

    
}
