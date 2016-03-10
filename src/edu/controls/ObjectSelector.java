package edu.controls;

import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import edu.build.Square;
import myprog.Settings;

/**
 * ObjectSelector призначений для виділення об'єктів в режимі будівництва.
 * @author Vadym
 * @version 1.0
 */
public final class ObjectSelector {
    
    /** Об'єкт даного класу. Для реалізації Singleton.*/
    private static ObjectSelector objS = null;
    /** Змінна що вказує увімкнений чи вимкнений режим вибору елементів.*/
    private static boolean on = false;
    /** Вузол елементів, що можна обрати.*/
    private Node _clickables;
    /** Змінна, що вказує рисуємо чи не рисуємо позначку на місці останнього вибору.*/
    private boolean _showMark = false;
    /** Геометрія позначки.*/
    private Geometry _mark = null;
    /** Поточний обраний елемент, та новий обраний елементи.*/
    private Geometry target, tmp;
    /** Матеріал обраного елементу, та матеріал, що позначає обраний серед інших.*/
    private Material _tmpMaterial, materialForSelected;
    /** Змінна, що вказує чи обраний зараз будь-який елемент.*/
    private boolean selected;
    
    /** Вказує чи встановлений ліміт польоту камери.*/
    private static boolean limited;
    
    /** Політ камери лише в заданих межах.*/
    private static float _maxX, _minX;
    private static float _maxY, _minY;
    private static float _maxZ, _minZ;
    
    /** Геттер, що дозволяє дізнатися увімкнений чи ні режим вибору елемента.*/
    public static boolean isON(){
        return on;
    }
    
    /** Приватний конструктор для реалызацыъ шаблону Singleton!*/
    private ObjectSelector(){ }
    
    /** 
     * Приватний конструктор для реалызацыъ шаблону Singleton!
     * @param clicables вузол об'єктів, які дозволено клікати.
     */
    private ObjectSelector(Node clicables){
        _showMark = false;
        _mark = null;
        _clickables = clicables;
        initKeys();       // load custom key mappings
        setSettings();
        materialForSelected = new Material(Settings.assetManager, 
        "Common/MatDefs/Misc/Unshaded.j3md");
        materialForSelected.setColor("Color", ColorRGBA.White);
        //disableLimited();
    }
    
    /** Налаштування камери та екрану для режиму вибору елемента.*/
    private void setSettings(){
        Settings.flyCam.setEnabled(true);   // Говоримо, щоб камера приймала наші дії.
        Settings.flyCam.setDragToRotate(true);
        Settings.inputManager.setCursorVisible(true);
        Settings.flyCam.setMoveSpeed(Settings.camSpeedBuild);
    }
    
    /** Налаштування камери та екрану для вимкнення режиму вибору елемента.*/
    private void unsetSettings(){
        Settings.flyCam.setDragToRotate(false);
        Settings.inputManager.setCursorVisible(false);
        Settings.flyCam.setMoveSpeed(Settings.camSpeedView);
    }
    
    /** 
     * Увімкнення можливості виділення об'єктів за допомогою мишки.
     * @param clicables вузол об'єктів, які дозволено клікати.
     */
    public static void objectSelectorON(Node clicables){
        if(!on){
            on = true;
            if(objS==null)
                objS = new ObjectSelector(clicables);
            
            Panel.panelON();
            
            Settings.cam.setLocation(new Vector3f(-(_maxX-_minX)/2, 50, -(_maxZ-_minZ)/2));
            
            System.out.println("Селектор увімкнено!");
        } else 
            System.out.println("Селектор вже увімкнено!");
    }
    
    /** 
     * Увімкнення можливості виділення об'єктів за допомогою мишки.
     * @param clicables вузол об'єктів, які дозволено клікати.
     * @param mark Geometry яку рисумає в місці кліку.
     */
    public static void objectSelectorON(Node clicables, Geometry mark){
        objectSelectorON(clicables);
        
        objS._showMark = true;
        
        if(objS._mark == null) {
            if(mark != null)
                objS._mark = mark;
            else
                objS.initMark();
        }
    }
    
    /** Вимкнення можливості виділення об'єктів за допомогою мишки. */
    public static void objectSelectorOFF(){
        if(on) {
            on = false;
            objS.unInitKeys();
            objS.unsetSettings();
            objS = null;
            Panel.panelOFF();
            System.out.println("Селектор вимкнено!");
        } else
            System.out.println("Селектор вже вимкнено!");
    }
    
    /** * Позначка місця виділення по замовчуванню. */
    private void initMark() {
        Sphere sphere = new Sphere(30, 30, 0.2f);
        _mark = new Geometry("Serector Mark", sphere);
        Material mark_mat = new Material(Settings.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", ColorRGBA.Red);
        _mark.setMaterial(mark_mat);
    }
    
    /** Ініціалізація кнопок. */
    private void initKeys() {    
        Settings.inputManager.addMapping("Select", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        Settings.inputManager.addListener(actionListener, "Select");
    }
    
    /** Деініціалізація кнопок. */
    private void unInitKeys() {    
      Settings.inputManager.deleteMapping("Select");
      Settings.inputManager.removeListener(actionListener);
    }
   
    public static boolean isLimited(){
            return limited;
    }
    
    public static void enableLimited(Square s){
        limited = true;

        _minX = s.getCX() - s.getWidth();
        _maxX = s.getCX() + s.getWidth();

        _minZ = s.getCZ() - s.getLenght();
        _maxZ = s.getCZ() + s.getLenght();

        _minY= s.getCY() - 10;
        _maxY = s.getCY() + 100;
    }
    
    private static Vector3f lastCamPosition = new Vector3f();
    private static Vector3f current = new Vector3f();
    
    public static void update(){
        if(limited){
            current = Settings.cam.getLocation();
            
            if(current.x<_minX || current.x>_maxX)
                current.x = lastCamPosition.x;
            
            if(current.y<_minY || current.y>_maxY)
                current.y = lastCamPosition.y;
            
            if(current.z<_minZ || current.z>_maxZ)
                current.z = lastCamPosition.z;
            
            lastCamPosition = current.clone();
            
            Settings.cam.setLocation(current);
        }
    }
    
    
    
    public static void disableLimited(){
        limited = false;
    }
    
    /** ActionListener - слухач заданих дій. Залежно від події виконує ті чи інші дії. */
    private ActionListener actionListener = new ActionListener() {
 
    public void onAction(String name, boolean keyPressed, float tpf) {
     if (name.equals("Select")) {
        CollisionResults results = new CollisionResults();
        // Convert screen click to 3d position
        Vector2f click2d = Settings.inputManager.getCursorPosition();
        Vector3f click3d = Settings.cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = Settings.cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
        _clickables.collideWith(ray, results);

        if (results.size() > 0) {
            tmp = results.getClosestCollision().getGeometry();
            
            if(!tmp.equals(target)){
                if(target!=null && selected)
                    target.setMaterial(_tmpMaterial);

                target = tmp;
                // if{} else{}
                Panel.gotoPanel(PanelID.pBOX);
                selected = true;
                if(_tmpMaterial==null){
                    _tmpMaterial = target.getMaterial();
                    materialForSelected = _tmpMaterial.clone();
                    materialForSelected.setBoolean("UseMaterialColors", true);    
                    materialForSelected.setColor("Ambient", new ColorRGBA(0, 1, 0.4f, 1f));
                }
                target.setMaterial(materialForSelected);
            }
            
             if(_showMark){
                 if(Settings.rootNode.hasChild(_mark))
                    Settings.rootNode.detachChild(_mark);
                
                 _mark.setLocalTranslation(results.getClosestCollision().getContactPoint());
                Settings.rootNode.attachChild(_mark);
            }
        }
        else{
            if(target!=null && selected)
                target.setMaterial(_tmpMaterial);
            
            _tmpMaterial = null;
            target = null;
            selected = false;
            
            if(_showMark)
                Settings.rootNode.detachChild(_mark);
            Panel.gotoPanel(PanelID.pNULL);
        }
      } // else if ...
    }
  };
}