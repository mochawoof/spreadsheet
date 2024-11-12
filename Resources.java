import java.net.URL;

import javax.swing.ImageIcon;
import java.awt.Image;

import java.io.InputStream;

class Resources {
    public static URL get(String path) {
        return Resources.class.getResource(path);
    }
    public static ImageIcon getAsImageIcon(String path) {
        return new ImageIcon(get(path));
    }
    public static Image getAsImage(String path) {
        return getAsImageIcon(path).getImage();
    }
    public static String getAsString(String path) {
        InputStream stream = Resources.class.getResourceAsStream(path);
        int i;
        String str = "";
        try {
            while ((i = stream.read()) != -1) {
                str += (char) i;
            }
        } catch (Exception e) {
            System.err.println("IO error while reading resource as string!");
            e.printStackTrace();
            str = "ERROR";
        }
        return str;
    }
}