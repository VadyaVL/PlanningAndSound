package edu.objects;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.water.WaterFilter;
import myprog.Settings;

/**
 * Реалізація до сцени води.
 * @author Vadym
 * @since 27.02.2016 
 */
public class Water {
    
    /** Об'єкт даного класу. Для реалізації Singleton.*/
    private static Water water = null;
    /** Змінна що вказує увімкнений чи вимкнений режим води.*/
    private static boolean isWater = false;
    
    private FilterPostProcessor fpp;    // Обробка води
    private WaterFilter waterf;         // Фільтр фоди - фізика води
    
    /**
     * Конструктор класу вода.
     * @param lightDir напрям води
     * @param height висота води
     */
    private Water(Vector3f lightDir, float height){
        fpp = new FilterPostProcessor(Settings.assetManager);        
        waterf = new WaterFilter(Settings.rootNode, lightDir.multLocal(-400));
        waterf.setWaterHeight(height);
        waterf.setUseFoam(false);
        waterf.setUseRipples(false);
        waterf.setDeepWaterColor(ColorRGBA.Brown);
        waterf.setWaterColor(ColorRGBA.Brown.mult(2.0f));
        waterf.setWaterTransparency(0.2f);
        waterf.setMaxAmplitude(0.3f);
        waterf.setWaveScale(0.008f);
        waterf.setSpeed(0.3f);
        waterf.setShoreHardness(1.0f);
        waterf.setRefractionConstant(0.2f);
        waterf.setShininess(0.3f);
        waterf.setSunScale(1.0f);
        waterf.setColorExtinction(new Vector3f(10.0f, 20.0f, 30.0f));
        fpp.addFilter(waterf);
    }
    
    /**
     * Вмикаємо воду.
     * @param lightDir напрям води
     * @param height висота води
     */
    public static void WaterON(Vector3f lightDir, float height){
        
        if(!isWater){
            if(water == null)
                water = new Water(lightDir, height);
            
            Settings.viewPort.addProcessor(water.fpp);
            water.waterf.setWaterHeight(height);
            isWater = true;
        }
    }
    
    /**
     * Вимикаємо воду.
     */
    public static void WaterOFF(){
        
        if(isWater){
            Settings.viewPort.removeProcessor(water.fpp);
            isWater = false;
        }
        
    }
    
    /**
     * Встановлення висоти води.
     * @param h висота води.
     */
    public static void setHeightOfWater(float h){
        if(isWater)
            water.waterf.setWaterHeight(h);
    }
    
    public static float getHeightOfWater(){
        if(isWater)
            return water.waterf.getWaterHeight();
        else 
            return 0;
    }
}
