package klondike.view;

import java.io.IOException;

/**
 * A marker interface for all text-based views, to be used in the Klondike game.
 *
 * Marker interfaces have no methods; their purpose is to "tag" a class
 * so it can be recognized as a text-based view.
 */
public interface TextualView {

    /**
     * Renders a model in some manner (e.g. as text, or as graphics, etc.).
     * @throws IOException if the rendering fails for some reason
     */
    void render() throws IOException;
}
