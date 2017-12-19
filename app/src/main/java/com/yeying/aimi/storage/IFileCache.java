package com.yeying.aimi.storage;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IFileCache {
    public void open() throws FileNotFoundException;

    public void write(byte[] bytes, int offset, int len) throws IOException;

    public void close() throws IOException;
}
