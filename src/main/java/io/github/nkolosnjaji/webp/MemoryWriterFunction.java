package io.github.nkolosnjaji.webp;

import io.github.nkolosnjaji.webp.gen.LibWebP;
import io.github.nkolosnjaji.webp.gen.WebPWriterFunction;

import java.lang.foreign.MemorySegment;

class MemoryWriterFunction implements WebPWriterFunction.Function {

    @Override
    public int apply(MemorySegment data, long dataSize, MemorySegment picture) {
        return dataSize == 0 ? 1 : LibWebP.WebPMemoryWrite(data, dataSize, picture);
    }
}
