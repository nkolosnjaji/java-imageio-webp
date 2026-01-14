package io.github.nkolosnjaji.webp;

import io.github.nkolosnjaji.webp.exceptions.WebPException;
import io.github.nkolosnjaji.webp.gen.WebPWriterFunction;

import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.nio.channels.FileChannel;

class FileWriterFunction implements WebPWriterFunction.Function {

    private final FileChannel channel;

    public FileWriterFunction(FileChannel channel) {
        this.channel = channel;
    }

    @Override
    public int apply(MemorySegment data, long dataSize, MemorySegment picture) {
        try {
            if (dataSize > 0) {
                return channel.write(data.asSlice(0, dataSize).asByteBuffer());
            }
            return 1;
        } catch (IOException e) {
            throw new WebPException("Error during writing file", e);
        }
    }
}
