package com.yeying.aimi.protocol.request;


import com.yeying.aimi.utils.Tools;

import java.io.File;
import java.net.URLEncoder;


public class PartParam {

    Arg[] args;

    public PartParam(String name, String field, boolean isFile) {
        this.initialize(name, field, isFile);
    }

    public PartParam(String field, boolean isFile) {
        this.initialize("pic", field, isFile);

    }

    private void initialize(String name, String field, boolean isFile) {
        args = new Arg[2];
        if (!isFile) {
            args[0] = new Arg("Content-Disposition", String.format("form-data; name=\"%s\"", field));
            args[1] = new Arg("Content-Type", "content/unknown");
        } else {
            int lastpos = field.lastIndexOf(File.separator);
            String filename = field.substring(lastpos + 1);
            args[0] = new Arg("Content-Disposition", String.format("form-data; name=\"%s\";filename=\"%s\"", name, URLEncoder.encode(filename)));
            String ctype = "application/application/octet-stream";
//			if(filename.endsWith("jpg")){
//				ctype="image/jpg";
//			}else if(filename.endsWith("jpeg")){
//				ctype="image/jpeg";
//			}else if(filename.endsWith("zip")){
//				ctype="application/octet-stream";
//			}
            ctype = Tools.getContentType(filename);
            args[1] = new Arg("Content-Type", ctype);
        }
    }

    public Arg[] getArgs() {
        return args;
    }
    //public static final String dispositionField="";

}
