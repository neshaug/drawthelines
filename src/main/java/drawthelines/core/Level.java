package drawthelines.core;

import playn.core.Image;
import playn.core.Sound;
import playn.core.Surface;
import playn.core.SurfaceLayer;
import drawthelines.core.entities.Dot;

public class Level {

    private final Image image;
    private final Sound sound;
    @SuppressWarnings("unused")
    private final String name;
    private final SurfaceLayer surfaceLayer;
    private final Dot[] dots;

    private boolean complete;
    private final Dot lastDot;

    public Level(final String name, final Image image, final Sound sound,
            final SurfaceLayer surfLayer, final Dot[] dots) {
        super();
        this.image = image;
        this.sound = sound;
        this.name = name;
        this.surfaceLayer = surfLayer;
        this.dots = dots;
        lastDot = dots[dots.length - 1];
    }

    private void complete() {
        complete = true;
        final Surface surface = surfaceLayer.surface();
        surface.clear();
        surface.drawImage(image, 20, 20, surface.width(), surface.height());
        if (sound != null) {
            sound.play();
        }
    }

    public void handleClick(final float x, final float y) {
        for (final Dot dot : dots) {
            dot.handleClick(x, y);

            if (lastDot.isActive()) {
                complete();
            }
        }
    }

    public Image getImage() {
        return image;
    }

    public SurfaceLayer getSurfaceLayer() {
        return surfaceLayer;
    }

    public boolean isComplete() {
        return complete;
    }

    public void paint(final float alpha) {
        for (final Dot dot : dots) {
            dot.paint(alpha);
        }
    }

    public void reset() {
        complete = false;

        surfaceLayer.surface().clear();
        for (final Dot dot : dots) {
            dot.reset();
        }
    }

    public void update(final float delta) {
        for (final Dot dot : dots) {
            dot.update(delta);
        }
    }
}
