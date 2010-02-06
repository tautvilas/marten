package marten.age.graphics.image;

import java.io.IOException;

public class ImageLoader {
    public static ImageData loadImage(String path) {
        ImageData data = null;
        try {
            data = new ImageData(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not load image '" + path + "'");
        }
        return data;
    }
}
