# mnist-reader

Classes for reading the MNIST data set.

The classes in this project are in the [Public Domain](https://creativecommons.org/publicdomain/zero/1.0/).


## The MNIST data set

The MNIST data set is published at http://yann.lecun.com/exdb/mnist/

The MNIST files are compressed with GZIP. In order to read these files, they 
have to be decompressed. This can be done manually, using WinZip, 7zip, WinRar 
or any other tool that supports GZIP. Alternatively, the **original** 
(compressed) files can be read and unpacked on the fly.

The classes in this project support *both* approaches.
 
The [examples](/src/test/java/de/javagl/mnist/reader/test) show how to use
the classes in this project.   


### Reading the uncompressed files

The main functionality of the MNIST reader is contained in the following
classes: 
[`MnistDecompressedReader`](/src/main/java/de/javagl/mnist/reader/MnistDecompressedReader.java) and
[`MnistEntry`](/src/main/java/de/javagl/mnist/reader/MnistEntry.java)

These classes have no further dependencies, and can simply be added to
any existing project. In order to read the **uncompressed** MNIST data 
with these classes, the following code can be used: 

    MnistDecompressedReader mnistReader = new MnistDecompressedReader();
    mnistReader.readDecompressedTraining(Paths.get("./data"), mnistEntry -> 
    {
        System.out.println("Read entry " + mnistEntry);
        BufferedImage image = mnistEntry.createImage();
        ...
    });



### Reading the original (compressed) data 

In order to read the **original** (compressed) MNIST data, the 
[`MnistCompressedReader`](/src/main/java/de/javagl/mnist/reader/MnistCompressedReader.java) 
can be used:

    MnistCompressedReader mnistReader = new MnistCompressedReader();
    mnistReader.readCompressedTraining(Paths.get("./data"), mnistEntry -> 
    {
        System.out.println("Read entry " + mnistEntry);
        BufferedImage image = mnistEntry.createImage();
        ...
    });

The decompression is then done on the fly, using 
[Apache Commons Compress](https://commons.apache.org/proper/commons-compress/).
For non-maven builds, the [commons-compress-1.10.jar](http://central.maven.org/maven2/org/apache/commons/commons-compress/1.10/commons-compress-1.10.jar)
can be downloaded and added to the classpath.


