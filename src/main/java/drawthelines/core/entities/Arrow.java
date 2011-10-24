package drawthelines.core.entities;

import static playn.core.PlayN.*;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Path;
import playn.core.Surface;

public class Arrow {

    private enum Location {
        LEFT, RIGHT
    }

    private final float x;
    private final float y;
    private final float width;
    private final float height;

    private Arrow(final Surface arrowSurf, final Location location) {
        width = arrowSurf.width() * 0.1f;
        height = arrowSurf.height() * 0.1f;
        
        if (location == Location.RIGHT) {
            x = arrowSurf.width() - width;
            y = 0;
            arrowSurf.translate(arrowSurf.width(), height);
            arrowSurf.rotate((float) ((180 * Math.PI) / 180));
        } else {
            y = 0;
            x = 0;
        }

        final CanvasImage arrow = drawArrow();
        arrowSurf.drawImage(arrow, 0, 0);
    }
    
    public static Arrow createLeft(final Surface arrowSurf) {
        return new Arrow(arrowSurf, Location.LEFT);
    }
    
    public static Arrow createRight(final Surface arrowSurf) {
        return new Arrow(arrowSurf, Location.RIGHT);
    }

    private CanvasImage drawArrow() {
        final CanvasImage arrow =
                graphics().createImage((int) width, (int) height);
        final Canvas arrowCanvas = arrow.canvas();
        arrowCanvas.save();
        arrowCanvas.setFillColor(Color.argb(255, 0, 120, 0));
        arrowCanvas.translate((width - (width * 0.9f)) / 2,
                (height - (height * 0.4f)) / 2);
        arrowCanvas.fillRect(width * 0.2f, 0, width, height * 0.4f);
        arrowCanvas.restore();

        arrowCanvas.save();
        final Path arrowHeadPath = graphics().createPath();
        arrowHeadPath.moveTo(width * 0.3f, height * 0.1f);
        arrowHeadPath.lineTo(width * 0.3f, height * 0.9f);
        arrowHeadPath.lineTo(0, height * 0.5f);
        arrowHeadPath.lineTo(width * 0.3f, height * 0.1f);
        arrowHeadPath.close();

        arrowCanvas.setFillColor(Color.argb(255, 0, 120, 0));
        arrowCanvas.fillPath(arrowHeadPath);
        arrowCanvas.restore();

        return arrow;
    }

    public boolean isClicked(final float x, final float y) {
        if ((x > this.x) && (x < (this.x + width)) && (y > this.y)
                && (y < (this.y + height))) {
            return true;
        }
        return false;
    }
}
