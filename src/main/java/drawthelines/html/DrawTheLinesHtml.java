package drawthelines.html;

import playn.core.PlayN;
import playn.html.HtmlAssetManager;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;
import drawthelines.core.DrawTheLines;

public class DrawTheLinesHtml extends HtmlGame {

    @Override
    public void start() {
        final HtmlAssetManager assets = HtmlPlatform.register().assetManager();
        final double screenWidth = PlayN.graphics().screenWidth() * 0.95;
        final double screenHeight = PlayN.graphics().screenHeight() * 0.95;
        double gameWidth = 0, gameHeight = 0;
        
        final double diagonal =
                Math.sqrt(Math.pow(screenWidth, 2.0)
                        + Math.pow(screenHeight, 2.0));
        
        gameWidth = (diagonal / Math.sqrt((1 / 3.2) + 1));
        gameHeight = (diagonal / Math.sqrt(3.2 + 1));
        
        if(gameHeight > screenHeight) {
            final double  difference = gameHeight - screenHeight;
            gameHeight -= difference;
            gameWidth = gameWidth - (difference * (16/9));
        } else {
            final double difference = gameWidth - screenWidth;
            gameWidth -= difference;
            gameHeight = gameHeight - (difference * (16/9));
        }

        assets.setPathPrefix("drawthelines/");
        PlayN.graphics().setSize((int) gameWidth, (int) gameHeight);
        PlayN.run(new DrawTheLines());
    }
}
