/*
 * MnistReader - Copyright (c) 2016-2018 Marco Hutter - http://www.javagl.de
 */ 
package de.javagl.mnist.reader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Consumer;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

/**
 * A class for reading the MNIST data set from the original (compressed)
 * files that are published at
 * <a href="http://yann.lecun.com/exdb/mnist/">
 * http://yann.lecun.com/exdb/mnist/</a>
 */
public class MnistCompressedReader
{
    /**
     * The {@link MnistDecompressedReader} that will read the decompressed data
     */
    private final MnistDecompressedReader mnistDecompressedReader;
    
    /**
     * Default constructor
     */
    public MnistCompressedReader()
    {
        this.mnistDecompressedReader = new MnistDecompressedReader();
    }
    
    /**
     * Read the MNIST training data from the given directory. The data is 
     * assumed to be located in files with their default names,
     * <code>train-images-idx3-ubyte.gz</code> and
     * <code>train-labels-idx1-ubyte.gz</code>.
     * 
     * @param inputDirectoryPath The input directory
     * @param consumer The consumer that will receive the resulting 
     * {@link MnistEntry} instances
     * @throws IOException If an IO error occurs
     */
    public void readCompressedTraining(Path inputDirectoryPath, 
        Consumer<? super MnistEntry> consumer) throws IOException
    {
        String trainImagesFileName = "train-images-idx3-ubyte.gz";
        String trainLabelsFileName = "train-labels-idx1-ubyte.gz";
        Path imagesFilePath = inputDirectoryPath.resolve(trainImagesFileName);
        Path labelsFilePath = inputDirectoryPath.resolve(trainLabelsFileName);
        readCompressed(imagesFilePath, labelsFilePath, consumer);
    }
    
    /**
     * Read the MNIST training data from the given directory. The data is 
     * assumed to be located in files with their default names,
     * <code>t10k-images-idx3-ubyte.gz</code> and
     * <code>t10k-labels-idx1-ubyte.gz</code>.
     * 
     * @param inputDirectoryPath The input directory
     * @param consumer The consumer that will receive the resulting 
     * {@link MnistEntry} instances
     * @throws IOException If an IO error occurs
     */
    public void readCompressedTesting(Path inputDirectoryPath, 
        Consumer<? super MnistEntry> consumer) throws IOException
    {
        String testImagesFileName = "t10k-images-idx3-ubyte.gz";
        String testLabelsFileName = "t10k-labels-idx1-ubyte.gz";
        Path imagesFilePath = inputDirectoryPath.resolve(testImagesFileName);
        Path labelsFilePath = inputDirectoryPath.resolve(testLabelsFileName);
        readCompressed(imagesFilePath, labelsFilePath, consumer);
    }
    
    
    /**
     * Read the MNIST data from the specified (compressed) files.
     * 
     * @param imagesFilePath The path of the images file
     * @param labelsFilePath The path of the labels file
     * @param consumer The consumer that will receive the resulting 
     * {@link MnistEntry} instances
     * @throws IOException If an IO error occurs
     */
    public void readCompressed(Path imagesFilePath, Path labelsFilePath, 
        Consumer<? super MnistEntry> consumer) throws IOException
    {
        try (InputStream compressedImagesInputStream = 
                new FileInputStream(imagesFilePath.toFile());
            InputStream compressedLabelsInputStream = 
                new FileInputStream(labelsFilePath.toFile()))
        {
            readCompressed(
                compressedImagesInputStream, 
                compressedLabelsInputStream, 
                consumer);
        }
    }
    
    /**
     * Read the MNIST data from the given (compressed) input streams.
     * The caller is responsible for closing the given streams.
     * 
     * @param compressedImagesInputStream The compressed input stream
     * containing the image data 
     * @param compressedLabelsInputStream The compressed input stream
     * containing the label data
     * @param consumer The consumer that will receive the resulting 
     * {@link MnistEntry} instances
     * @throws IOException If an IO error occurs
     */
    public void readCompressed(
        InputStream compressedImagesInputStream, 
        InputStream compressedLabelsInputStream, 
        Consumer<? super MnistEntry> consumer) throws IOException
    {
        mnistDecompressedReader.readDecompressed(
            decompress(compressedImagesInputStream), 
            decompress(compressedLabelsInputStream),
            consumer);
    }

    /**
     * Create a decompressed input stream from the given compressed 
     * input stream.
     * 
     * @param compressedInputStream The compressed input stream
     * @return The decompressed input stream
     * @throws IOException If the compression type of the given input stream
     * cannot be determined, or any other IO error occurs 
     */
    private static InputStream decompress(InputStream compressedInputStream) 
        throws IOException
    {
        BufferedInputStream bufferedInputStream = 
            new BufferedInputStream(compressedInputStream);
        CompressorStreamFactory compressorStreamFactory = 
            new CompressorStreamFactory();
        try
        {
            CompressorInputStream decompressedInputStream = 
                compressorStreamFactory.createCompressorInputStream(
                    bufferedInputStream);
            return decompressedInputStream;
        }
        catch (CompressorException e)
        {
            throw new IOException(e);
        }
    }
    
}
