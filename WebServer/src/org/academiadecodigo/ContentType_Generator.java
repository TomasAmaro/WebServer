package org.academiadecodigo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by codecadet on 08/11/16.
 */
public class ContentType_Generator {

    private BufferedReader reader;
    private String mime ="";


    public ContentType_Generator(String file) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(file));
    }


    public String getMime(String extension) throws IOException {

        System.out.println("This is a extension of getMime method: " + extension);
        String fileType;


        while ((fileType = reader.readLine()) != null) {

            String[] str = fileType.split(";");


            if (extension.equals(str[0])) {
                return this.mime = str[1];
            }
        }
        return this.mime;
    }
}
