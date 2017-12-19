/*
Copyright (c) 2007, Sun Microsystems, Inc.

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in
      the documentation and/or other materials provided with the
      distribution.
    * Neither the name of Sun Microsystems, Inc. nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.yeying.aimi.protocol.request;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Part {


    private final byte[] content;
    private final InputStream inputStream;
    private final Arg[] headers;
    private final long size;
    private final boolean bytes;
    private final String fileName;


    public Part(final String filename, final Arg[] headers, long sz) throws FileNotFoundException {
        this.fileName = filename;
        this.inputStream = null;//new FileInputStream(filename);

        this.headers = headers;
        this.content =/*URLEncoder.encode*/(filename).getBytes();
        this.size = sz;
        this.bytes = false;
    }

    public Part(final String data, final Arg[] headers) {

        // data is mandatory, everything else is optional
        if (data == null) {
            throw new IllegalArgumentException("part data must be supplied");
        }
        this.inputStream = null;
        this.fileName = null;
        this.content =/* URLEncoder.encode*/(data).getBytes();
        this.headers = headers;
        this.size = this.content.length;
        this.bytes = true;
    }

    public boolean isBytes() {
        return bytes;
    }

    public byte[] getData() {
        return content;
    }

    public Arg[] getHeaders() {
        return headers;
    }

    public InputStream getStream() {
        InputStream is = null;
        if (this.inputStream == null) {
            //TKLog.d(Part.class.getSimpleName(), "inputstream is null: %s:%d",this.fileName,this.size);
            try {
                is = new FileInputStream(this.fileName);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return is;
    }

    public long getDataSize() {
        return size;
    }
}
