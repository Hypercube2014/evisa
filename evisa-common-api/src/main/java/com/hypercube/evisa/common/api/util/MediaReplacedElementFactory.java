package com.hypercube.evisa.common.api.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

/**
 * Replaced element in order to replace elements like
 * <tt>&lt;div class="media" data-src="image.png" /></tt> with the real media
 * content.
 */
public class MediaReplacedElementFactory implements ReplacedElementFactory {
    //@Value("${app.url.hyp}")
    //private String appUrlHyp;
//    private String appUrlHyp="D:/Hypercube/evisa-deploiement-final-17-07-2025/evisa-deploiement-final-17-07-2025/config/hypercube/";
    private final String appUrlHyp="/home/ubuntu/hypercube/";
    private static final Logger log = LoggerFactory.getLogger(MediaReplacedElementFactory.class);
    private final ReplacedElementFactory superFactory;
    private final String foldername;

    public MediaReplacedElementFactory(ReplacedElementFactory superFactory, String foldername) {
        this.superFactory = superFactory;
        this.foldername = foldername;
    }

    public ReplacedElementFactory getSuperFactory() {
        return superFactory;
    }

    public String getFoldername() {
        return foldername;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ReplacedElement createReplacedElement(LayoutContext layoutContext, BlockBox blockBox,
            UserAgentCallback userAgentCallback, int cssWidth, int cssHeight) {
        Element element = blockBox.getElement();
        if (element == null) {
            return null;
        }
        String nodeName = element.getNodeName();
        String className = element.getAttribute("class");

        if ("div".equals(nodeName) && "media".equals(className)) {
            if (!element.hasAttribute("data-src")) {
                log.error("MediaReplacedElementFactory data-src error");
               
            }
            InputStream qinput = null;
            try {

                qinput = new FileInputStream(appUrlHyp + "evisa/common/rel1_0/barcode/" + foldername + "/"
                        + element.getAttribute("data-src"));
                final byte[] qbytes = IOUtils.toByteArray(qinput);
                final Image qrimage = Image.getInstance(qbytes);
                final FSImage fsImage = new ITextFSImage(qrimage);
                if (fsImage != null) {
                    if ((cssWidth != -1) || (cssHeight != -1)) {
                        fsImage.scale(cssWidth, cssHeight);
                    }
                    return new ITextImageElement(fsImage);
                }
            } catch (IOException | BadElementException e) {
                log.error("MediaReplacedElementFactory IOException block", e);
            } finally {
                IOUtils.closeQuietly(qinput);
            }
        }

        if ("div".equals(nodeName) && "barcode".equals(className)) {
            if (!element.hasAttribute("data-src")) {
                throw new RuntimeException(
                        "An element with class `media` is missing a `data-src` attribute indicating the media file.");
            }
            InputStream input = null;
            try {
                input = new FileInputStream(element.getAttribute("data-src"));
                final byte[] bytes = IOUtils.toByteArray(input);
                final Image image = Image.getInstance(bytes);
                final FSImage fsImage = new ITextFSImage(image);
                if (fsImage != null) {
                    if ((cssWidth != -1) || (cssHeight != -1)) {
                        fsImage.scale(cssWidth, cssHeight);
                    }
                    return new ITextImageElement(fsImage);
                }
            } catch (IOException | BadElementException e) {
                log.error("MediaReplacedElementFactory IOException block", e);
            } finally {
                IOUtils.closeQuietly(input);
            }
        }

        return this.superFactory.createReplacedElement(layoutContext, blockBox, userAgentCallback, cssWidth, cssHeight);
    }

    @Override
    public void reset() {
        superFactory.reset();
    }

    @Override
    public void remove(Element e) {
        superFactory.remove(e);
    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener listener) {
        superFactory.setFormSubmissionListener(listener);
    }

}