package com.allandroidprojects.ecomsample.startup;

import android.app.Application;
import android.content.SharedPreferences;

import com.allandroidprojects.ecomsample.cache.ImagePipelineConfigFactory;
import com.facebook.drawee.backends.pipeline.Fresco;
import java.util.logging.Level;

import jade.util.Logger;

/**
 * Created by 06peng on 2015/6/24.
 */
public class FrescoApplication extends Application {
    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
        SharedPreferences settings = getSharedPreferences("jadeChatPrefsFile", 0);

        String defaultHost = settings.getString("defaultHost", "");
        String defaultPort = settings.getString("defaultPort", "");
        if (defaultHost.isEmpty() || defaultPort.isEmpty()) {
            logger.log(Level.INFO, "Create default properties");
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("defaultHost", "10.0.2.1");
            editor.putString("defaultPort", "1099");
            editor.commit();
        }

    }
}
