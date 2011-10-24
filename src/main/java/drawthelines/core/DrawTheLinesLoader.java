package drawthelines.core;

import static playn.core.PlayN.*;

import java.util.ArrayList;
import java.util.List;

import playn.core.AssetWatcher;
import playn.core.Image;
import playn.core.Json;
import playn.core.Json.Array;
import playn.core.ResourceCallback;
import playn.core.Sound;
import playn.core.SurfaceLayer;
import drawthelines.core.entities.Dot;

public class DrawTheLinesLoader {

    public static void
            load(final ResourceCallback<List<Level>> resourceCallback) {

        final List<Level> levels = new ArrayList<Level>();
        final Sound[] sounds =
                {
                        assetManager().getSound("sounds/ams-en"),
                        assetManager().getSound("sounds/ams-to"),
                        assetManager().getSound("sounds/ams-tre"),
                        assetManager().getSound("sounds/ams-fire"),
                        assetManager().getSound("sounds/ams-fem"),
                        assetManager().getSound("sounds/ams-seks"),
                        assetManager().getSound("sounds/ams-syv"),
                        assetManager().getSound("sounds/ams-aatte"),
                        assetManager().getSound("sounds/ams-ni"),
                        assetManager().getSound("sounds/ams-ti"),
                        assetManager().getSound("sounds/ams-elleve"),
                        assetManager().getSound("sounds/ams-tolv"),
                        assetManager().getSound("sounds/ams-tretten"),
                        assetManager().getSound("sounds/ams-fjorten"),
                        assetManager().getSound("sounds/ams-femten"),
                        assetManager().getSound("sounds/ams-seksten"),
                        assetManager().getSound("sounds/ams-sytten"),
                        assetManager().getSound("sounds/ams-atten"),
                        assetManager().getSound("sounds/ams-nitten"),
                        assetManager().getSound("sounds/ams-tjue")};
        
        assetManager().getImage("images/background.png");

        final AssetWatcher assetWatcher =
                new AssetWatcher(new AssetWatcher.Listener() {
                    @Override
                    public void error(final Throwable e) {
                        log().error("error loading assets ", e);
                    }

                    @Override
                    public void done() {
                        resourceCallback.done(levels);
                    }
                });

        assetManager().getText("levels/levels.json",
                new ResourceCallback<String>() {
                    @Override
                    public void error(final Throwable e) {
                        log().error("error loading levels ", e);
                    }

                    @Override
                    public void done(final String resource) {
                        final Json.Object document = json().parse(resource);
                        final Json.Array jsonLevels =
                                document.getArray("levels");
                        deserializeLevels(jsonLevels);
                        assetWatcher.start();
                    }

                    private void deserializeLevels(final Array jsonLevels) {

                        for (int i = 0; i < jsonLevels.length(); i++) {
                            final Json.Object jsonLevel =
                                    jsonLevels.getObject(i);
                            final String name = jsonLevel.getString("name");
                            final String imagePath =
                                    jsonLevel.getString("image");
                            final Image image =
                                    assetManager().getImage(
                                            "images/" + imagePath);

                            assetWatcher.add(image);

                            final SurfaceLayer dotsLayer =
                                    graphics().createSurfaceLayer(
                                            graphics().width(),
                                            graphics().height());

                            final Json.Array jsonDots =
                                    jsonLevel.getArray("dots");

                            final Dot[] dots =
                                    deserializeDots(jsonDots, dotsLayer);

                            final Level level =
                                    new Level(name, image, null, dotsLayer,
                                            dots);

                            levels.add(level);
                        }
                    }

                    private Dot[] deserializeDots(final Array jsonDots,
                            final SurfaceLayer surfLayer) {
                        final Dot[] dots = new Dot[jsonDots.length()];

                        Dot previousDot = null;

                        for (int i = 0; i < jsonDots.length(); i++) {
                            final Json.Object jsonDot = jsonDots.getObject(i);
                            final float x =
                                    (float) jsonDot.getNumber("x")
                                            * graphics().width();
                            final float y =
                                    (float) jsonDot.getNumber("y")
                                            * graphics().height();

                            final Dot dot =
                                    new Dot(surfLayer.surface(), x, y, 7,
                                            i + 1, sounds[i % 20], previousDot);
                            dots[i] = dot;

                            previousDot = dot;
                        }

                        return dots;
                    }
                });
    }
}
