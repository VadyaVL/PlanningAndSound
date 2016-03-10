package edu.objects;

import com.jme3.scene.Spatial;
import myprog.Settings;

/**
 * Описує місцевість.
 * @author Vadym
 */
public class Surface {
    
    /** Змінна для збереження сцени.*/
    private Spatial gameLevel;
    
    /** Конструктор Surface за замвчуванням.*/
    public Surface(){
        this("Scenes/main.j3o");
    }
    
    /** 
     * Конструктор Surface.
     * @param path шлях до місцевості.
     */
    public Surface(String path){
       gameLevel = Settings.assetManager.loadModel(path);
       gameLevel.setLocalTranslation(0, -5.2f, 0);
       gameLevel.setLocalScale(8);
       Settings.rootNode.attachChild(gameLevel);
    }
    
    /** 
     * Дозволяє отримати Spatial, землю. Може бути необхідним у випадку
     * коли потрібно отримати землю, щоб сказти, що вона тверда.
     * @return Spatial gameLevel - сцена
     */
    public Spatial getTerrainSpatial(){
        return gameLevel;
    }
}
