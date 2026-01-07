package io.github.nkolosnjaji.webp;

import io.github.nkolosnjaji.webp.gen.LibWebP;
import io.github.nkolosnjaji.webp.gen.WebPMemoryWriter;
import io.github.nkolosnjaji.webp.gen.WebPPicture;
import io.github.nkolosnjaji.webp.gen.WebPWriterFunction;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

class InternalWriter {

    private final MemorySegment ms;

    private final FileChannel fileChannel;

    public InternalWriter(Arena arena, InternalPicture picture, Path path) throws IOException {
        Objects.requireNonNull(arena, "arena must not be null");
        Objects.requireNonNull(picture, "picture must not be null");
        this.ms = WebPMemoryWriter.allocate(arena);
        LibWebP.WebPMemoryWriterInit(this.ms);
        Files.deleteIfExists(path);
        this.fileChannel = FileChannel.open(path,
                StandardOpenOption.WRITE, StandardOpenOption.APPEND, StandardOpenOption.CREATE_NEW);

        MemorySegment function = WebPWriterFunction.allocate(new FileWriterFunction(this.fileChannel), arena);

        WebPPicture.writer(picture.getMemorySegment(), function);
    }

    public ByteBuffer getEncodedBytes() {
        final long size = WebPMemoryWriter.size(this.ms);
        return WebPMemoryWriter.mem(this.ms).asSlice(0, size).asByteBuffer();
    }

    public void free() throws IOException {
        fileChannel.close();
    }

    public static class FileWriterFunction implements WebPWriterFunction.Function {

        private final FileChannel channel;

        public FileWriterFunction(FileChannel channel) {
            this.channel = channel;
        }

        @Override
        public int apply(MemorySegment data, long dataSize, MemorySegment picture) {
            try {
                return channel.write(data.asSlice(0, dataSize).asByteBuffer());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
