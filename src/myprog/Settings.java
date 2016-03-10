package myprog;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.font.BitmapFont;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

/**
 * Статичний клас для збереження об'єктів необхідних для роботи програми.
 * В проекті є безліч класів які потребують дані об'єкти для повноцінної роботи,
 * і щоб декілька разів не передавати ці об'єкти було прийнято рішення створити даний клас.
 * @author Vadym
 * @version 1.0
 * @since 29.02.2016
 */
public final class Settings {
    public static AssetManager assetManager = null;
    public static FlyByCamera flyCam = null;
    public static Node rootNode = null;
    public static Camera cam = null;
    public static InputManager inputManager = null;
    public static Node guiNode = null;
    public static ViewPort viewPort = null;
    
    public static ViewPort guiViewPort = null;
    public static AudioRenderer audioRenderer = null;
    public static BitmapFont bitmapFont = null;
    public static AppStateManager stateManager = null;
     
    /** Розміри вікна додатку.*/
    public static int winWidth = 1200, winHeight = 600;
    /** Максимальна кількість кадрів на секунду.*/
    public static int maxFPS = 100;
    /** Максимальна дальність прорисовки.*/
    public static int renderDistance = 3000;
    /** Розмір карти terrainSize*terrainSize.*/
    public static float terrainSize = 256*8;
    
    public static float camSpeedBuild = 100;
    public static float camSpeedView = 20;
    
    /** Змінна яка говорить в якому режимі ми знаходимося.*/
    public static boolean modeBuild = false;    // false - режим перегляду
                                                // true - режим будівництва
}
