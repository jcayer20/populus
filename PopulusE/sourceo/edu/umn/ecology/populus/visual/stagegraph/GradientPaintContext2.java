package edu.umn.ecology.populus.visual.stagegraph;

import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.PaintContext;
import java.awt.Color;

/**
 * the big deal about this file is that one line was changed (and most the comments deleted).
 * all that was changed was the little bit of code at the end of the constructor. the modification causes
 * the gradient to "flow".
 *
 * <p>Title: Component for Stage Structured Growth</p>
 * <p>Description: a program for designing the stage structured growth for Populus</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Amos Anderson
 * @version 1.0
 */

public class GradientPaintContext2 implements PaintContext {
    static ColorModel xrgbmodel =
        new DirectColorModel(24, 0x00ff0000, 0x0000ff00, 0x000000ff);

    double x1;
    double y1;
    double dx;
    double dy;
    boolean cyclic;
    int interp[];
    WritableRaster saved;
    ColorModel model;

    public GradientPaintContext2(Point2D p1, Point2D p2, AffineTransform xform,
                                Color c1, Color c2, boolean cyclic, int shift) {
        // First calculate the distance moved in user space when
        // we move a single unit along the X & Y axes in device space.
        Point2D xvec = new Point2D.Double(1, 0);
        Point2D yvec = new Point2D.Double(0, 1);
        try {
            AffineTransform inverse = xform.createInverse();
            inverse.deltaTransform(xvec, xvec);
            inverse.deltaTransform(yvec, yvec);
        } catch (NoninvertibleTransformException e) {
            xvec.setLocation(0, 0);
            yvec.setLocation(0, 0);
        }

        // Now calculate the (square of the) user space distance
        // between the anchor points. This value equals:
        //     (UserVec . UserVec)
        double udx = p2.getX() - p1.getX();
        double udy = p2.getY() - p1.getY();
        double ulenSq = udx * udx + udy * udy;

        if (ulenSq <= Double.MIN_VALUE) {
            dx = 0;
            dy = 0;
        } else {
            // Now calculate the proportional distance moved along the
            // vector from p1 to p2 when we move a unit along X & Y in
            // device space.
            //
            // The length of the projection of the Device Axis Vector is
            // its dot product with the Unit User Vector:
            //     (DevAxisVec . (UserVec / Len(UserVec))
            //
            // The "proportional" length is that length divided again
            // by the length of the User Vector:
            //     (DevAxisVec . (UserVec / Len(UserVec))) / Len(UserVec)
            // which simplifies to:
            //     ((DevAxisVec . UserVec) / Len(UserVec)) / Len(UserVec)
            // which simplifies to:
            //     (DevAxisVec . UserVec) / LenSquared(UserVec)
            dx = (xvec.getX() * udx + xvec.getY() * udy) / ulenSq;
            dy = (yvec.getX() * udx + yvec.getY() * udy) / ulenSq;

            if (cyclic) {
                dx = dx % 1.0;
                dy = dy % 1.0;
            } else {
                // We are acyclic
                if (dx < 0) {
                    // If we are using the acyclic form below, we need
                    // dx to be non-negative for simplicity of scanning
                    // across the scan lines for the transition points.
                    // To ensure that constraint, we negate the dx/dy
                    // values and swap the points and colors.
                    Point2D p = p1; p1 = p2; p2 = p;
                    Color c = c1; c1 = c2; c2 = c;
                    dx = -dx;
                    dy = -dy;
                }
            }
        }

        Point2D dp1 = xform.transform(p1, null);
        this.x1 = dp1.getX();
        this.y1 = dp1.getY();

        this.cyclic = cyclic;
        int rgb1 = c1.getRGB();
        int rgb2 = c2.getRGB();
        int a1 = (rgb1 >> 24) & 0xff;
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >>  8) & 0xff;
        int b1 = (rgb1      ) & 0xff;
        int da = ((rgb2 >> 24) & 0xff) - a1;
        int dr = ((rgb2 >> 16) & 0xff) - r1;
        int dg = ((rgb2 >>  8) & 0xff) - g1;
        int db = ((rgb2      ) & 0xff) - b1;
        if (((rgb1 & rgb2) >>> 24) == 0xff) {
            model = xrgbmodel;
        } else {
            model = ColorModel.getRGBdefault();
        }
        interp = new int[cyclic ? 513 : 257];
        for (int i = 0; i <= 256; i++) {
            float rel = i / 256.0f;
            int rgb =
                (((int) (a1 + da * rel)) << 24) |
                (((int) (r1 + dr * rel)) << 16) |
                (((int) (g1 + dg * rel)) <<  8) |
                (((int) (b1 + db * rel))      );
            interp[i] = rgb;
            if (cyclic) {
                interp[512 - i] = rgb;
            }
        }
        if(cyclic && shift != 0){
           int[] temp = new int[interp.length];
           System.arraycopy(interp,shift,temp,0,temp.length-shift);
           System.arraycopy(interp,0,temp,temp.length-shift,shift);
           interp = temp;
        }
    }

    /**
     * Release the resources allocated for the operation.
     */
    public void dispose() {
        saved = null;
    }

    /**
     * Return the ColorModel of the output.
     */
    public ColorModel getColorModel() {
        return model;
    }

    /**
     * Return a Raster containing the colors generated for the graphics
     * operation.
     * @param x,y,w,h The area in device space for which colors are
     * generated.
     */
    public Raster getRaster(int x, int y, int w, int h) {
        double rowrel = (x - x1) * dx + (y - y1) * dy;

        WritableRaster rast = saved;
        if (rast == null || rast.getWidth() < w || rast.getHeight() < h) {
            rast = getColorModel().createCompatibleWritableRaster(w, h);
            saved = rast;
        }
        /*
         * LROE TODO: This code should be updated to work with below...
         * OLD CODE!!!
         * Also, previously imported sun.awt.image.IntegerComponentRaster;
        IntegerComponentRaster irast = (IntegerComponentRaster) rast;
        int off = irast.getDataOffset(0);
        int adjust = irast.getScanlineStride() - w;
        int[] pixels = irast.getDataStorage();
        
        if (cyclic) {
            cycleFillRaster(pixels, off, adjust, w, h, rowrel, dx, dy);
        } else {
            clipFillRaster(pixels, off, adjust, w, h, rowrel, dx, dy);
        }
        */

        /*
         * LROE: TODO new code!
         */
        //final SinglePixelPackedSampleModel sppsm = (SinglePixelPackedSampleModel)rast.getSampleModel();


        return rast;
    }

    void cycleFillRaster(int[] pixels, int off, int adjust, int w, int h,
                         double rowrel, double dx, double dy) {
        rowrel = rowrel % 2.0;
        int irowrel = ((int) (rowrel * (1 << 30))) << 1;
        int idx = (int) (-dx * (1 << 31));
        int idy = (int) (-dy * (1 << 31));
        while (--h >= 0) {
            int icolrel = irowrel;
            for (int j = w; j > 0; j--) {
                pixels[off++] = interp[icolrel >>> 23];
                icolrel += idx;
            }

            off += adjust;
            irowrel += idy;
        }
    }

    void clipFillRaster(int[] pixels, int off, int adjust, int w, int h,
                        double rowrel, double dx, double dy) {
        while (--h >= 0) {
            double colrel = rowrel;
            int j = w;
            if (colrel <= 0.0) {
                int rgb = interp[0];
                do {
                    pixels[off++] = rgb;
                    colrel += dx;
                } while (--j > 0 && colrel <= 0.0);
            }
            while (colrel < 1.0 && --j >= 0) {
                pixels[off++] = interp[(int) (colrel * 256)];
                colrel += dx;
            }
            if (j > 0) {
                int rgb = interp[256];
                do {
                    pixels[off++] = rgb;
                } while (--j > 0);
            }

            off += adjust;
            rowrel += dy;
        }
    }
}

