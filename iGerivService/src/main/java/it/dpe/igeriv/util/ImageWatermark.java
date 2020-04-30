package it.dpe.igeriv.util;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * The tool to adding watermark to images,recommended markImage () methods to
 * create watermark image.
 * 
 */
public class ImageWatermark {
	
	public static void watermark(BufferedImage original, String watermarkText) {
		// create graphics context and enable anti-aliasing
		Graphics2D g2d = original.createGraphics();
		g2d.scale(1, 1);
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// create watermark text shape for rendering
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
		GlyphVector fontGV = font.createGlyphVector(g2d.getFontRenderContext(), watermarkText);
		Rectangle size = fontGV.getPixelBounds(g2d.getFontRenderContext(), 0, 0);
		Shape textShape = fontGV.getOutline();
		double textWidth = size.getWidth();
		double textHeight = size.getHeight();
		AffineTransform rotate45 = AffineTransform.getRotateInstance(Math.PI / 4d);
		Shape rotatedText = rotate45.createTransformedShape(textShape);

		// use a gradient that repeats 4 times
		GradientPaint gradientPaint = new GradientPaint(0, 0, new Color(0f, 0f, 0f, 0.1f), original.getWidth() / 2, original.getHeight() / 2, new Color(1f, 1f, 1f, 0.1f));
		g2d.setPaint(gradientPaint);
		g2d.setStroke(new BasicStroke(0.5f));
		// Aggiungo un'ulteriore trasparenza al testo
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f));
		
		// step in y direction is calc'ed using pythagoras + 5 pixel padding
		double yStep = Math.sqrt(textWidth * textWidth / 2) + 5;

		// step over image rendering watermark text
		for (double x = -textHeight * 3; x < original.getWidth(); x += (textHeight * 3)) {
			double y = -yStep;
			for (; y < original.getHeight(); y += yStep) {
				g2d.draw(rotatedText);
				g2d.fill(rotatedText);
				g2d.translate(0, yStep);
			}
			g2d.translate(textHeight * 3, -(y + yStep));
		}
	}

}
