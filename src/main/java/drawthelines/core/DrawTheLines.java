package drawthelines.core;

import static drawthelines.core.DrawTheLinesLoader.*;
import static playn.core.PlayN.*;

import java.util.List;

import playn.core.CanvasLayer;
import playn.core.Font;
import playn.core.Game;
import playn.core.Image;
import playn.core.Pointer;
import playn.core.ResourceCallback;
import playn.core.SurfaceLayer;
import playn.core.TextFormat;
import playn.core.Font.Style;
import playn.core.TextFormat.Alignment;
import playn.core.TextLayout;
import drawthelines.core.entities.Arrow;

public class DrawTheLines implements Game, Pointer.Listener {

    private boolean loaded;

    private List<Level> levels;
    private Level currentLevel;
    private final boolean editmode;
    private int levelCounter;
    private boolean didChangeLevelClick;

    private Arrow leftArrow;

    private Arrow rightArrow;

    private CanvasLayer backgroundLayer;

    public DrawTheLines() {
        editmode = false;
    }

    public DrawTheLines(final boolean editmode) {
        this.editmode = editmode;
    }

    private void changeLevel() {
        if (!didChangeLevelClick) {
            didChangeLevelClick = true;
        } else {
            nextLevel();
            didChangeLevelClick = false;
        }
    }

    private void nextLevel() {
        if (currentLevel != null) {
            currentLevel.reset();
            graphics().rootLayer().remove(currentLevel.getSurfaceLayer());
        }

        currentLevel = levels.get(levelCounter);
        if (levelCounter == levels.size() - 1) {
            levelCounter = 0;
        } else {
            levelCounter++;
        }
        graphics().rootLayer().add(currentLevel.getSurfaceLayer());
    }

    private void previousLevel() {
        if (currentLevel != null) {
            currentLevel.reset();
            graphics().rootLayer().remove(currentLevel.getSurfaceLayer());
        }
        currentLevel = levels.get(levelCounter);
        if (levelCounter == 0) {
            levelCounter = levels.size() - 1;
        } else {
            levelCounter--;
        }
        graphics().rootLayer().add(currentLevel.getSurfaceLayer());
    }

    @Override
    public void init() {
        if (pointer() != null) {
            pointer().setListener(this);
        }

        backgroundLayer =
                graphics().createCanvasLayer(graphics().width(),
                        graphics().height());
        graphics().rootLayer().add(backgroundLayer);

        load(new ResourceCallback<List<Level>>() {

            @Override
            public void done(final List<Level> resource) {
                levels = resource;

                // this image is cached by the browser
                final Image bgImage =
                        assetManager().getImage("images/background.jpg");

                backgroundLayer.canvas().clear();
                backgroundLayer.canvas().drawImage(bgImage, 0, 0,
                        graphics().width(), graphics().height());

                final SurfaceLayer arrowsLayer =
                        graphics().createSurfaceLayer(graphics().width(),
                                graphics().height());

                leftArrow = Arrow.createLeft(arrowsLayer.surface());
                rightArrow = Arrow.createRight(arrowsLayer.surface());

                graphics().rootLayer().add(arrowsLayer);

                if (levels.size() > 0) {
                    nextLevel();
                }

                loaded = true;
            }

            @Override
            public void error(final Throwable err) {
                log().error("error loading game ", err);
            }

        });
    }

    @Override
    public void update(final float delta) {
        if (loaded) {
            currentLevel.update(delta);
        }
    }

    @Override
    public void paint(final float alpha) {
        if (loaded) {
            currentLevel.paint(alpha);
        } else {
            paintLoading();
        }
    }

    private void paintLoading() {
        if (assetManager().getPendingRequestCount() > 0) {
            backgroundLayer.canvas().clear();
            final TextFormat format =
                    new TextFormat().withAlignment(Alignment.LEFT).withFont(
                            graphics().createFont("loading", Style.PLAIN, 32));
            final TextLayout layout =
                    graphics().layoutText(
                            assetManager().getPendingRequestCount()
                                    + " assets to load...", format);
            backgroundLayer.canvas().drawText(layout,
                    (graphics().width() / 2) - layout.width(),
                    graphics().height() / 2);
        }
    }

    @Override
    public int updateRate() {
        return 25;
    }

    @Override
    public void onPointerStart(final playn.core.Pointer.Event event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPointerEnd(final playn.core.Pointer.Event event) {
        final float x = event.x();
        final float y = event.y();

        if (loaded) {
            currentLevel.handleClick(x, y);

            if (currentLevel.isComplete()) {
                changeLevel();
            }
        }

        if (leftArrow.isClicked(x, y)) {
            previousLevel();
        }

        if (rightArrow.isClicked(x, y)) {
            nextLevel();
        }

        // simple way of outputting coordinates in json
        if (editmode) {
            System.out.print("{ \"x\": ");
            System.out.print(x / graphics().width());
            System.out.print(", \"y\": ");
            System.out.print(y / graphics().height());
            System.out.print("},\n");
        }
    }

    @Override
    public void onPointerDrag(final playn.core.Pointer.Event event) {
        // TODO Auto-generated method stub

    }
}
