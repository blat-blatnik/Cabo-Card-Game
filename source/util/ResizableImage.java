package util;

import view.CardTextures;

import java.awt.*;

/**
 * @version 1.0
 *
 * Stores an image that is meant to be scaled to some particular dimensions with high-quality scaling. If you just
 * try to paint an image with Graphics2D and it's actually scaled up or down on the screen, Swing uses nearest-neighbor
 * filtering to scale the image - and it looks absolutely terrible! This class is meant as a fix for that, it stores
 * the original image, but then lazily scales the image with a higher-quality filter when you request the image with a
 * different size. The scaled image is cached and a new one is only created if needed.
 *
 * @see Image
 * @see Graphics2D
 * @see CardTextures
 */
public class ResizableImage {

    private final Image originalImage;
    private Image resizedImage;

    /**
     * Constructs a ResizableImage from a given image. The given image is not actually resized until this is needed
     * later - so this constructor is an O(1) operation.
     *
     * @param image The image to create a ResizableImage from.
     * @see Image
     */
    public ResizableImage(Image image) {
        originalImage = image;
        resizedImage = image;
    }

    /**
     * Gets a resized version of the original image with the given desired dimensions. If the cached version of the
     * resized image has different dimensions than those desired - then the original image is rescaled to the dimensions
     * using a higher quality filter. This can be a rather expensive operation. However, if the dimensions match, then
     * the image is not rescaled again and the cached image is simply returned - which is an O(1) operation.
     *
     * @param desiredWidth The width to scale the original image to.
     * @param desiredHeight The height to scale the original image to.
     * @return A high-quality scaled version of the original image.
     * @see Image
     */
    public Image getResized(int desiredWidth, int desiredHeight) {
        if (resizedImage.getWidth(null) != desiredWidth || resizedImage.getHeight(null) != desiredHeight)
            resizedImage = originalImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
        return resizedImage;
    }

}