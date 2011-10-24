package drawthelines.core.entities;

import static playn.core.PlayN.*;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Sound;
import playn.core.Surface;

public class Dot implements IsEntity {

    private static int FILL_COLOR = Color.argb(255, 0, 0, 0); // black
    private static int FILL_COLOR_HINT_ON = Color.argb(255, 255, 255, 255); // white

    private final float x, y;
    private final float radius;

    private boolean hint;
    private boolean activated;

    private final Dot previousDot;
    private final CanvasImage regularDot;
    private final CanvasImage hintDot;
    private final Surface surf;
    @SuppressWarnings("unused")
    private final int number;
    private final Sound sound;

    public Dot(final Surface dotsSurf, final float x, final float y,
            final float radius, final int number, final Sound sound,
            final Dot previousDot) {
        surf = dotsSurf;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.number = number;
        this.sound = sound;
        this.previousDot = previousDot;

        regularDot = graphics().createImage((int) radius * 2, (int) radius * 2);

        regularDot.canvas().setFillColor(FILL_COLOR);
        regularDot.canvas().fillCircle(radius, radius, radius);

        dotsSurf.drawImageCentered(regularDot, x, y);

        hintDot = graphics().createImage((int) radius * 2, (int) radius * 2);

        hintDot.canvas().setFillColor(FILL_COLOR);
        hintDot.canvas().fillCircle(radius, radius, radius);

        hintDot.canvas().setFillColor(FILL_COLOR_HINT_ON);
        hintDot.canvas().fillCircle(radius, radius, radius * 0.8f);
    }

    private void hintOn() {
        surf.drawImageCentered(hintDot, x, y);
        hint = true;
    }

    private void activate() {
        if (!activated && hint) {
            sound.play();
            surf.drawImageCentered(regularDot, x, y);

            if ((previousDot != null) && previousDot.activated) {
                surf.save();
                surf.setFillColor(FILL_COLOR);
                surf.drawLine(x, y, previousDot.x, previousDot.y, 3);
                surf.restore();
            }

            hint = false;
            activated = true;
        }
    }

    public void handleClick(final float x, final float y) {
        double radius = this.radius * 2;
        if (((x <= (this.x + radius)) && (x >= (this.x - radius)))
                && (y <= (this.y + radius)) && (y >= (this.y - radius))) {
            activate();
        }
    }

    @Override
    public void paint(final float alpha) {

    }

    @Override
    public void update(final float delta) {
        if (((previousDot == null) || previousDot.activated) && !activated
                && !hint) {
            hintOn();
        }
    }

    public void reset() {
        activated = false;
        hint = false;
        surf.drawImageCentered(regularDot, x, y);
    }

    public boolean isActive() {
        return activated;
    }
}