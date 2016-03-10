package myprog;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import edu.build.Square;
import edu.controls.ObjectSelector;
import edu.gui.GUIe;
import edu.objects.SkySun;
import edu.objects.Surface;
import edu.objects.Watcher;
import edu.objects.Water;
import objects.Door;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
   
    /** Тут об'єкти які можна обирати мишкою.*/
    private Node clickables;
    /** Тут об'єкти з якими можна взаємодіяти при перегляді.*/
    private Node usable;
    /** Об'єкт, що додає місяць, сонце, хмари, та керує часом.*/
    private SkySun sky_sun_moon;
    /** Об'єкт, що додає поверхню до простору.*/
    private Surface ground;
    
    /** Об'єкт, що дозволяє переміщатися в просторі як гравець-людина.*/
    private Watcher watcher;
    
    /** Об'єкт, що дозволяє працювати з ділянкою будівництва.*/
    private Square square;
    
    /** 
     * Ініціалізація програми.
     * @param args
     */
    public static void main(String[] args) {
        Main app = new Main();
        app.setShowSettings(false);
        AppSettings newSetting = new AppSettings(true);
        newSetting.setResolution(Settings.winWidth, Settings.winHeight);
        newSetting.setFrameRate(Settings.maxFPS);
        app.setSettings(newSetting);
        app.start();
    }
    
    /**
     * Ініціалізація додатку.
     */
    private void initSettings(){
                
        // Для того, що постійно не передавати дані об'єкти на зовні, збережемо їх в 
        // класі Settings
        Settings.assetManager = assetManager;
        Settings.flyCam = flyCam;
        Settings.inputManager = inputManager;
        Settings.rootNode = rootNode;
        Settings.cam = cam;
        Settings.guiNode = guiNode;
        Settings.guiViewPort = guiViewPort;
        Settings.audioRenderer = audioRenderer;
        Settings.bitmapFont = guiFont;
        Settings.viewPort = viewPort;
        Settings.stateManager = stateManager;
        
        // Встановили дальність прорисовки 3000 умовних метрів
        cam.setFrustumFar(Settings.renderDistance);
    }
    
    /** Ініціалізація програми*/
    private void initProgram(){
        
        clickables = new Node("Clickables");    // Ініціюємо вузол з об'єктами що можна клікати.
        usable = new Node("Usable");    // Ініціюємо вузол з об'єктами з якими можлива взаємодія.
        rootNode.attachChild(clickables);       // Додаємо наш вузол до головного вузла.
        rootNode.attachChild(usable);       // Додаємо наш вузол до головного вузла.
        initKeys();
        
        // Це для відображення 2D інфи/////////////
        GUIe.setGUISettings(false, true, true);
        //GUIe.setMode(true);
        //GUIe.setGUISettings(false, true);
        //////////////////////////////////////////
        
        ground = new Surface();
        watcher = Watcher.getWatcher();     //watcher.setModel(cube2Geo);
        watcher.setSolidSpatial(ground.getTerrainSpatial());
        watcher.enableDebug(true);
        watcher.setUsableNode(usable);
        
       
        // Ініціалізація в режимі перегляду.
        Settings.modeBuild = false;
        watcher.setEnable(true);
        ObjectSelector.objectSelectorOFF();
        GUIe.setCrossHairs(true);
        
        // Перемикаємо на мод будівництва
        changeMode();
        
        sky_sun_moon = SkySun.initializate();
        sky_sun_moon.setTime(15.0f);
        
        Water.WaterON(sky_sun_moon.getLightDir(), -20);
        
        square = new Square(-50, -50, ground.getTerrainSpatial());
        ObjectSelector.enableLimited(square);
    }
    
    /** Тесе*/
    private void initTestBox(){
        for(int i=0; i<1; i++){
        Box cube2Mesh = new Box(1f,1f,1f);
        Geometry cube2Geo = new Geometry("My Textured Box", cube2Mesh);
        cube2Geo.setLocalTranslation(-.5f +2*i, -.5f, -.5f);
        //cube2Geo.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        Material cube2Mat = new Material(assetManager, 
         "Common/MatDefs/Light/Lighting.j3md");
        cube2Mat.setTexture("DiffuseMap", 
        assetManager.loadTexture("Textures/grass.jpg"));
        cube2Geo.setMaterial(cube2Mat);
        
        clickables.attachChild(cube2Geo); 
        }
    }
    
    @Override
    public void simpleInitApp() {        
        initSettings();
        initProgram();
        
        initTestBox();
        Door door = new Door("Models/DoorBox.j3o", usable);
        watcher.setSolidSpatial(door.getNode());
        
    }
    
    /**
     * Головна петля, цикл де відбувається update необхідних об'єктів.
     */
    @Override
    public void simpleUpdate(float tpf) {
       //sky_sun_moon.update(tpf);
       watcher.update(tpf);
       
       if(Settings.modeBuild)
           ObjectSelector.update();
    }
    
//region Корисні функції з якими слід розібратися.
    @Override
    public void simpleRender(RenderManager rm) {
        
    }
//endregion

    private void initKeys(){
        Settings.inputManager.addMapping("ChangeMode", new KeyTrigger(KeyInput.KEY_B));
        Settings.inputManager.addMapping("UPWATER", new KeyTrigger(KeyInput.KEY_U));
        Settings.inputManager.addMapping("DOWNWATER", new KeyTrigger(KeyInput.KEY_J));
        Settings.inputManager.addListener(actionListener, "ChangeMode", "UPWATER", "DOWNWATER");
    }
    
    private void changeMode(){
        if(Settings.modeBuild){
            Settings.modeBuild = false;
            watcher.setEnable(true);
            ObjectSelector.objectSelectorOFF();
            GUIe.setCrossHairs(true);

            if(clickables.hasChild(usable))
                clickables.detachChild(usable);
            rootNode.attachChild(usable);

            clickables.detachChild(usable);
        } else {
            Settings.modeBuild = true;
            watcher.setEnable(false);
            ObjectSelector.objectSelectorON(clickables);
            GUIe.setCrossHairs(false);

            if(rootNode.hasChild(usable))
                rootNode.detachChild(usable);
            clickables.attachChild(usable);
        }
        GUIe.updTextMode();
    }
    
    /** ActionListener - слухач заданих дій. Залежно від події виконує ті чи інші дії. */
    private ActionListener actionListener = new ActionListener() {
 
        public void onAction(String binding, boolean isPressed, float tpf) {
            if(binding.equals("ChangeMode")){
                if(isPressed){
                    changeMode();
                }
            }
            else if(binding.equals("UPWATER")){
                Water.setHeightOfWater(Water.getHeightOfWater()+1);
            }
            else if(binding.equals("DOWNWATER")){
                Water.setHeightOfWater(Water.getHeightOfWater()-1);
            }
        }
    
    };

    
}
