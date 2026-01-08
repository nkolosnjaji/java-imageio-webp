# JAVA ImageIO WebP

This is a [Java ImageIO plugin](http://docs.oracle.com/javase/7/docs/api/javax/imageio/package-summary.html) for reading
and
writing WebP [images](https://developers.google.com/speed/webp) using JAVA Foreign Function and Memory API (FFM).

## Requirements

- JAVA 25 (first LTS version which supports JAVA FFM)
- Correct architecture for [java-imageio-webp-lib](https://github.com/nkolosnjaji/java-imageio-webp-lib) (using Maven
  Artifact Classifiers)

## Usage

1. Add the following dependencies to your project:

```
    <dependency>
        <groupId>io.github.nkolosnjaji</groupId>
        <artifactId>java-imageio-webp</artifactId>
        <version>LATEST_VERSION</version>
    </dependency>
    <dependency>
        <groupId>io.github.nkolosnjaji</groupId>
        <artifactId>java-imageio-webp-lib</artifactId>
        <version>LATEST_LIB_VERSION</version>
        <classifier>ARCHITECTURE</classifier>
    </dependency>
```

**Possible values for architecture:**

- unix_amd64 (x86-64)
- mac_x86_64 (MacOS Intel)
- mac_aarch64 (MacOS Silicon)

**Compatibility Matrix:**

| java-imageio-webp | java-imageio-webp-lib | WebP Version  |
|-------------------|-----------------------|---------------|
| 1.0.0             | 0.0.4                 | 1.6.0         | 

2. Use like any other Image I/O reader and writer.

## Reading WebP image:

```
BufferedImage image = ImageIO.read(new File("input.webp"));
```

In order to customize reading settings use:

```
ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();

// Configure reading settings
WebPReadParam readParam = new WebPReadParam();
readParam.setFlip(true);

// Configure the input
reader.setInput(new FileImageInputStream(new File("input.webp")));

BufferedImage image = reader.read(0, readParam);
```

## Writing WebP image:

```
// Read image
BufferedImage image = ImageIO.read(new File("input.png"));

// Write WebP image using default settings
ImageIO.write(image, "webp", new File("output.webp"));
```

In order to customize writing settings use:

```
BufferedImage image = ImageIO.read(new File("input.png"));

ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

// Configure writing settings
WebPWriterParam writeParam = new WebPWriterParam();
writeParam.setCompressionQuality(1.0f); // default is 0.75
writeParam.setCompressionType(param.setCompressionType(WebPWriterParam.COMPRESSION_LOSSLESS)); // default is COMPRESSION_LOSSY

// Configure the output
writer.setOutput(Paths.get("output.webp"));

// Encode
writer.write(null, new IIOImage(image, null, null), writeParam);
```