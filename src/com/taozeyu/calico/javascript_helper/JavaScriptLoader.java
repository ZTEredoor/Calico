package com.taozeyu.calico.javascript_helper;

import com.taozeyu.calico.GlobalConfig;
import com.taozeyu.calico.SystemJavaScriptLog;
import com.taozeyu.calico.util.PathUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by taozeyu on 15/10/7.
 */
public class JavaScriptLoader {

    private static String[] SystemLibNames = new String[] {
            "resource_manager",
            "print_stream",
            "pages_constructor",
            "params_helper",
    };

    private static final int BufferedSize = 1024;

    private static final String LibraryPath = "javascript";
    private static final Charset LibraryCharset = Charset.forName("UTF-8");

    public void loadSystemJavaScriptLib(ScriptEngine engine) throws ScriptException, IOException {

        loadLogObject(engine);
        ClassLoader loader = getClass().getClassLoader();
        File debugWorkingDirectory = GlobalConfig.instance().getFile(
                "debug-working-directory", System.getProperty("user.dir"));
        for (String libName : SystemLibNames) {
            String path = LibraryPath +"/"+ libName +".js";
            InputStream is = loader.getResourceAsStream(path);
            if (is == null) {
                // when class loader can't find resource,
                // it means that the program is not running at jar package.
                File file = PathUtil.getFile(path, debugWorkingDirectory.getPath());
                is = new FileInputStream(file);
            }
            loadJavaScript(is, engine);
        }
    }

    public void loadJavaScript(InputStream inputStream, ScriptEngine engine) throws ScriptException, IOException {
        Reader reader = getReaderFromFile(inputStream);
        try {
            engine.getContext().setWriter(new OutputStreamWriter(System.out, LibraryCharset));
            engine.getContext().setErrorWriter(new OutputStreamWriter(System.err, LibraryCharset));
            engine.eval(reader);
        } finally {
            reader.close();
        }
    }

    private Reader getReaderFromFile(InputStream inputStream) {
        return  new InputStreamReader(new BufferedInputStream(inputStream, BufferedSize), LibraryCharset);
    }

    private void loadLogObject(ScriptEngine engine) {
        engine.put("Log", new SystemJavaScriptLog());
    }
}