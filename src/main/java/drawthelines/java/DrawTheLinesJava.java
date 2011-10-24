package drawthelines.java;

import playn.core.PlayN;
import playn.java.JavaAssetManager;
import playn.java.JavaPlatform;
import drawthelines.core.DrawTheLines;

public class DrawTheLinesJava {

    public static void main(final String[] args) {
        final JavaAssetManager assets = JavaPlatform.register().assetManager();
        assets.setPathPrefix("src/main/resources/drawthelines/resources");
        PlayN.run(new DrawTheLines(true));
    }
}
