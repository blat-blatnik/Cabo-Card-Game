package view;

import model.card.Card;
import model.card.CaboCard;
import util.ResizableImage;

import java.awt.*;
import java.io.InputStream;
import java.util.EnumMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * @version 2.0
 *
 * Contains static methods to load and access card textures for Cabo - which consist of card front and back textures.
 * The textures are loaded in a lazy manner - they will be loaded from disk the first time they are requested. After
 * that the images will be cached inside of the CardTextures class and don't need to be loaded further.
 *
 * @see Card
 * @see ResizableImage
 */
public class CardTextures {

    private static final Map<String, ResizableImage> allTextures = new HashMap<>();
    private static final EnumMap<CaboCard, ResizableImage> cardTextures = new EnumMap<>(CaboCard.class);
    private static final ResizableImage cardBackTexture = loadTexture("CARD_BACK.png");

    /**
     * This method tries to lookup the card texture in the internal texture cache of CardTextures - if an image
     * matching the given card exists in the cache it is immediately returned, otherwise the image is loaded from disk,
     * placed into the cache, and then returned. The texture is internally stored in a ResizableImage, so it is scaled
     * lazily to the desired dimensions as well before being returned.
     *
     * If the texture cannot be loaded from disk for whatever reason - this method will instead return the back-face texture
     * of cards. If the back-face texture also couldn't load, then this method returns null.
     *
     * @param card The card whose front-face texture to fetch.
     * @param desiredWidth The width to which the fetched texture should be stretched.
     * @param desiredHeight The height to which the fetched texture should be stretched.
     * @return The scaled version of the card front-texture matching the given card and dimensions. Or null if it couldn't be loaded.
     * @see Card
     * @see ResizableImage
     */
    public static Image getCardFrontTexture(Card card, int desiredWidth, int desiredHeight) {
        CaboCard key = card.getBackingCard();
        ResizableImage texture = cardTextures.getOrDefault(key, null);

        if (texture == null) {
            texture = loadTexture(key.value + "_" + key.discardAbility + ".png");
            cardTextures.put(key, texture);
        }

        if (texture == null)
            return getCardBackTexture(desiredWidth, desiredHeight);
        else
            return texture.getResized(desiredWidth, desiredHeight);
    }

    /**
     * Fetches the back-face texture of Cabo cards. Unlike getCardFrontTexture() this method will never load an image
     * from disk - the back-face texture is always statically loaded from disk.
     *
     * @param desiredWidth The width to which the fetched texture should be stretched.
     * @param desiredHeight The height to which the fetched texture should be stretched.
     * @return The back-face texture of Cabo cards - stretched to the desired dimensions, or null if it couldn't be loaded.
     */
    public static Image getCardBackTexture(int desiredWidth, int desiredHeight) {
        if (cardBackTexture == null)
            return null;
        else
            return cardBackTexture.getResized(desiredWidth, desiredHeight);
    }

    /**
     * Loads a texture from the "textures" resources directory, with the given filename. If a texture with the same
     * filename was already loaded, then it is no re-loaded from disk, but rather from an internal cache of textures.
     * Otherwise, the texture is loaded from the file and stored in the internal cache. If a problem occurs during
     * loading, the cardBackTexture is returned, the the texture from this filename will always return cardBackTexture
     * from that point onwards until the the program is restarted.
     *
     * @param filename The name of a texture file inside of the texture resources directory from which to load the texture.
     * @return The loaded image if the load from disk was successful, or cardBackTexture if not.
     */
    private static ResizableImage loadTexture(String filename) {
        if (allTextures.containsKey(filename))
            return allTextures.get(filename);

        ResizableImage image;
        try {
            InputStream imageFile = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    "textures/" + filename);
            if (imageFile == null)
                throw new IOException("Couldn't find textures/" + filename);

            image = new ResizableImage(ImageIO.read(imageFile));
        } catch (IOException io) {
            System.err.println("Couldn't load texture: " + io.getLocalizedMessage());
            image = cardBackTexture;
        }

        allTextures.put(filename, image);
        return image;
    }

}
