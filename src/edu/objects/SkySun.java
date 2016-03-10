package edu.objects;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import java.util.Calendar;
import jme3utilities.sky.SkyControl;
import myprog.Settings;

/**
 * Обгортка, що дозволяє керувати доповненням SkyControl. Додає небо, хмари, сонце, місяць.
 * @author Vadym
 * @since 27.03.2016
 */
public class SkySun {
    
    /** Об'єкт даного класу. Для реалізації Singleton.*/
    private static SkySun _obj = null;
    /** Об'єкт SkyControl, власне основа класу.*/
    private SkyControl sc;
    /** Об'єкт DirectionalLight, описує джерело направленого світла.*/
    private DirectionalLight _sunL;
    /** Об'єкт AmbientLight, описує світло навколишнього середовища.*/
    private AmbientLight al;
    /** Поточна година і хвилина, а також додаткова змінна для тимчасових обчислень.*/
    private int hour = 0, minute = 0, tmp;
    
    /** Конструктор SkySun.*/
    private SkySun()
    {
        hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        minute = Calendar.getInstance().get(Calendar.MINUTE);
        
        
        al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        Settings.rootNode.addLight(al);
        
        AmbientLight al2 = new AmbientLight();
        Settings.rootNode.addLight(al2);
        
        _sunL = new DirectionalLight();
        Settings.rootNode.addLight(_sunL);
        
        sc = new SkyControl(Settings.assetManager, Settings.cam, 0.7f, true, true);
        Settings.rootNode.addControl(sc);
        sc.getUpdater().setMainLight(_sunL);
        sc.getUpdater().setAmbientLight(al2);
        sc.getSunAndStars().setObserverLatitude((float) (0.9));
        sc.setCloudiness(0.4f);
        sc.setEnabled(true);
    }
    
    /** Отримати напрям світла.*/
    public Vector3f getLightDir(){
        return _sunL.getDirection();
    }
    
    /** Встановлюємо час доби.*/
    public void setTime(float hour){
        sc.getSunAndStars().setHour(hour%24);
    }
    
    /** Ініціалізація SkySunControl.*/
    public static SkySun initializate(){
        
        if(_obj==null)
            _obj = new SkySun();
        
        return _obj;
    }
    
    /**
     * Оновлення стану неба... Не потрібно, адже це робиться для контролю часу,
     * і постійно вантажить систему.
     */
    public void update(float tpf){
        
        tmp = Calendar.getInstance().get(Calendar.MINUTE);
        
        if(minute-tmp != 0){
            if(minute == 59 && tmp == 0)
                hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            
            minute = tmp;
            sc.getSunAndStars().setHour(hour + minute/60.0f);
            //sc.update(tpf);
        }
        
        
    }
}
