package io.github.nkolosnjaji.webp;

import io.github.nkolosnjaji.webp.exceptions.WebPException;
import io.github.nkolosnjaji.webp.gen.WebPWriterFunction;

import javax.imageio.stream.ImageOutputStream;
import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

class ImageOutputStreamWriterFunction implements WebPWriterFunction.Function {

    private final ImageOutputStream imageOutputStream;

    public ImageOutputStreamWriterFunction(ImageOutputStream imageOutputStream) {
        this.imageOutputStream = imageOutputStream;
    }

    @Override
    public int apply(MemorySegment data, long dataSize, MemorySegment picture) {
        if (dataSize > 0) {
            ByteBuffer byteBuffer = data.asSlice(0, dataSize).asByteBuffer();
            byte[] bytes = new byte[Math.toIntExact(dataSize)];
            byteBuffer.get(bytes);
            try {
                imageOutputStream.write(bytes);
            } catch (IOException e) {
                throw new WebPException("Error during writing file", e);
            }
        }
        return 1;
    }
}
