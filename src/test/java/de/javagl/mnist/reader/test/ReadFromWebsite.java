/*
 * MnistReader - Copyright (c) 2016-2018 Marco Hutter - http://www.javagl.de
 */ 
package de.javagl.mnist.reader.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Consumer;

import de.javagl.mnist.reader.MnistCompressedReader;
import de.javagl.mnist.reader.MnistEntry;

/**
 * An example showing how to use the MNIST reader to read the 
 * MNIST data set directly from the website
 */
public class ReadFromWebsite
{
    /**
     * Entry point of this example
     * 
     * @param args Not used
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        readFromWebsite();
    }

    /**
     * Read the MNIST training data from a the original website, and print 
     * information about the data that is read to the console. 
     * 
     * @throws IOException If an IO error occurs
     */
    private static void readFromWebsite() throws IOException
    {
        String baseUrl = "http://yann.lecun.com/exdb/mnist/";

        URL imagesUrl = new URL(baseUrl + "t10k-images-idx3-ubyte.gz");
        URL labelsUrl = new URL(baseUrl + "t10k-labels-idx1-ubyte.gz");

        try (InputStream imagesInputStream = imagesUrl.openStream();
            InputStream labelsInputStream = labelsUrl.openStream())
        {
            MnistCompressedReader mnistReader = new MnistCompressedReader();
            Consumer<MnistEntry> consumer = System.out::println;

            mnistReader.readCompressed(
                imagesInputStream, labelsInputStream, consumer);
        }
    }

}
