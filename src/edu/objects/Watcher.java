package edu.objects;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import myprog.Settings;

/**
 * Клас, що описує спостерігача. Може пересуватися по місцевості.
 * @author Vadym
 */
public class Watcher  {

    /** Об'єкт даного класу. Для реалізації Singleton.*/
    public static Watcher _watcher = null;
    /** Змінна що вказує увімкнений чи вимкнений режим перегляду.*/
    private boolean on = false;
    
    
    private Node usable = null;
    private boolean ifSetUsable = false;
    
    private BulletAppState bulletAppState;  // Об'єкт для роботи з фізикою.
    private RigidBodyControl landscape;     // Об'єкт яки на основі Spatial робить місця непроходимими.
    private CharacterControl player;        // Об'єкт гравець-спостерігач.
    private Geometry pl = null;             // Геометрія гравця. Кулька чи кубик.
    
    public void setModel(Geometry pl){
        this.pl = pl;
    }
    
    public void setUsableNode(Node usable){
        if(usable!=null) {
            this.usable = usable;
            ifSetUsable = true;
        }
        else {
            ifSetUsable = false;
        }
    }
    
    /** Поля для навігації в просторі. */
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
 
    //Temporary vectors used on each frame.
    //They here to avoid instanciating new vectors on each frame
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    
    /**
     * Вказуємо Spatial, по якому ходимо, але не забуваємо для його відображення 
     * помістити його в rootNode
     * @param s ділянка для затверділості
     */
    public void setSolidSpatial(Spatial s){
       // We set up collision detection for the scene by creating a
       // compound collision shape and a static RigidBodyControl with mass zero.
       CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(s);
       landscape = new RigidBodyControl(sceneShape, 0);
       s.addControl(landscape);
       bulletAppState.getPhysicsSpace().add(landscape);
    }
    
    public void setSolidSpatial(TerrainQuad s){
        CollisionShape sceneShape =
                CollisionShapeFactory.createMeshShape(s);
        landscape = new RigidBodyControl(sceneShape, 0);
        s.addControl(landscape);
        bulletAppState.getPhysicsSpace().add(landscape);
    }
    
    /** Отримати екземляр класу Watcher.*/
    public static Watcher getWatcher(){
        
        if(_watcher==null)
            _watcher = new Watcher();
        
        return _watcher;
    }
    
    /** Встановити переглядача, вимкнути переглядача.*/
    public void setEnable(boolean on){
        this.on = on;
        
        if(on){
            setUpKeys();
            setupCam();
        } else{
            unsetUpKeys();
            unsetupCam();
        }
    }
    
    /** Встановлення камери для переглядача.*/
    private void setupCam(){
        Settings.flyCam.setDragToRotate(false);
        Settings.inputManager.setCursorVisible(false);
        Settings.flyCam.setMoveSpeed(Settings.camSpeedView);
    }
    
    /** Скидання камери для переглядача.*/
    private void unsetupCam(){
        Settings.flyCam.setEnabled(true);   // Говоримо, щоб камера приймала наші дії.
        //Settings.flyCam.setDragToRotate(true);
        Settings.inputManager.setCursorVisible(true);
        Settings.flyCam.setMoveSpeed(Settings.camSpeedBuild);
    }
    
    /** Увімкнений режим спостерігача чи ні.*/
    public boolean isEnable(){
        return this.on;
    }
    
    /** Конструктор класу Watcher.*/
    private Watcher(){
        
        /** Set up Physics */
        bulletAppState = new BulletAppState();  // Ініціалізація об'єкту обробки сили та зіткнень
        Settings.stateManager.attach(bulletAppState);
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
 
        // We set up collision detection for the player by creating
        // a capsule collision shape and a CharacterControl.
        // The CharacterControl offers extra settings for
        // size, stepheight, jumping, falling, and gravity.
        // We also put the player in its starting position.
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.2f, 1.8f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(-200, 10, 200));
        //bulletAppState.getPhysicsSpace().enableDebug(_assetManager);
        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.
        bulletAppState.getPhysicsSpace().add(player);   

    }
    
    public void enableDebug(boolean turn){
        bulletAppState.setDebugEnabled(turn);
    }
    
    /** 
     * Установка значень дій на клавіші.
     */
    private void setUpKeys() {
        Settings.inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        Settings.inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        Settings.inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        Settings.inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        Settings.inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        Settings.inputManager.addMapping("Act", new KeyTrigger(KeyInput.KEY_E));
        Settings.inputManager.addListener(actionListener, "Left", "Right", "Up", "Down", "Jump", "Act");
        
    }

    /** 
     * Скидання налаштувань клавіатури.
     */
    private void unsetUpKeys() {
        Settings.inputManager.deleteMapping("Left");
        Settings.inputManager.deleteMapping("Right");
        Settings.inputManager.deleteMapping("Up");
        Settings.inputManager.deleteMapping("Down");
        Settings.inputManager.deleteMapping("Jump");
        Settings.inputManager.deleteMapping("Act");
        Settings.inputManager.removeListener(actionListener);
    }
  
    public void update(float tpf) {
        if(on){
            camDir.set(Settings.cam.getDirection()).multLocal(0.6f);
            camLeft.set(Settings.cam.getLeft()).multLocal(0.4f);
            walkDirection.set(0, 0, 0);
            if (left) {
                walkDirection.addLocal(camLeft);
            }
            if (right) {
                walkDirection.addLocal(camLeft.negate());
            }
            if (up) {
                walkDirection.addLocal(camDir);
            }
            if (down) {
                walkDirection.addLocal(camDir.negate());
            }
            player.setWalkDirection(walkDirection);
            Settings.cam.setLocation(player.getPhysicsLocation().
                    subtract(Settings.cam.getDirection().mult(5f).
                    add(new Vector3f(0, -1.5f, 0))));

            if(pl!=null){
                pl.setLocalTranslation(player.getPhysicsLocation());
            }
        }
    }
  
    /** ActionListener - слухач заданих дій. Залежно від події виконує ті чи інші дії. */
    private ActionListener actionListener = new ActionListener() {
 
        public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("Left")) {
          left = isPressed;
        } else if (binding.equals("Right")) {
          right= isPressed;
        } else if (binding.equals("Up")) {
          up = isPressed;
        } else if (binding.equals("Down")) {
          down = isPressed;
        } else if (binding.equals("Jump")) {
          if (isPressed) { player.jump(); }
        } else if (binding.equals("Act") && isPressed && ifSetUsable) {
            
            CollisionResults results = new CollisionResults();
            Ray ray = new Ray(Settings.cam.getLocation(), Settings.cam.getDirection());
            usable.collideWith(ray, results);
        
            if (results.size() > 0) {
                Geometry g = results.getClosestCollision().getGeometry();
                Vector3f v = g.getWorldTranslation();
                v = v.subtract(player.getPhysicsLocation());

                if(Math.sqrt(v.x*v.x + v.y*v.y+v.z*v.z)<4.0f){
                    System.out.println("Make animation with result!");
                }
            }
            
        }
    }
    
  };

}