package com.baohulu.framework.common.utils.file.pdf;

import com.itextpdf.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

/**
 * @author heqing
 * @date 2022/09/19 13:48
 */
@Slf4j
public class ITextRendererObjectFactory extends BasePooledObjectFactory {

    private static GenericObjectPool itextRendererObjectPool = null;

    private String[] resourcePaths;

    public ITextRendererObjectFactory(String... resourcePaths) {
        this.resourcePaths = resourcePaths;
    }

    @Override
    public Object create() throws Exception {
        ITextRenderer renderer = createTextRenderer(resourcePaths);
        return renderer;
    }

    @Override
    public PooledObject wrap(Object o) {
        return new DefaultPooledObject<>(o);
    }

    public static GenericObjectPool getObjectPool(String... resourcePaths) {
        if (itextRendererObjectPool == null) {
            itextRendererObjectPool = new GenericObjectPool(new ITextRendererObjectFactory(resourcePaths));
            GenericObjectPoolConfig config = new GenericObjectPoolConfig<>();
            config.setLifo(false);
            config.setMaxTotal(15);
            config.setMaxIdle(5);
            config.setMinIdle(1);
            config.setMaxWait(Duration.ofMillis(5000L));
            itextRendererObjectPool.setConfig(config);
        }

        return itextRendererObjectPool;
    }


    public static synchronized ITextRenderer createTextRenderer(String... resourcePaths) throws DocumentException, IOException {
        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        addFonts(fontResolver, resourcePaths);
        return renderer;
    }

    public static ITextFontResolver addFonts(ITextFontResolver fontResolver, String... resourcePaths) throws DocumentException, IOException {
        File fontsDir = null;
        if (resourcePaths != null && resourcePaths.length > 0 && StringUtils.isNotBlank(resourcePaths[0])) {
            fontsDir = new File(resourcePaths[0]);
        }

        if (fontsDir != null && fontsDir.isDirectory()) {
            File[] files = fontsDir.listFiles();

            for(int i = 0; i < files.length; ++i) {
                File f = files[i];
                if (f == null || f.isDirectory()) {
                    break;
                }

                fontResolver.addFont(f.getAbsolutePath(), "Identity-H", false);
                log.info("加载字体>>>字体加载成功,路径={}", f.getAbsolutePath());
            }
        }

        return fontResolver;
    }
}
