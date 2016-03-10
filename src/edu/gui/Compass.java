package edu.gui;

import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.ColorRGBA;
import myprog.Settings;

/**
 * Об'єкт, що емітує компас, вказує напрям погляду камери.
 * @author Vadym
 * @since 18.02.2016
 */
public class Compass {
    
    /** коеофіцієнт для переводу радіан в градуси.*/
    private static final float K = (float) (180.0/Math.PI);
    /** Зміна кута коли фіксувати що камера здвинулась.*/
    private static final float eps = .0001f;
    
    /** Об'єкт даного класу. Для реалізації Singleton.*/
    private static Compass _compass = null;
    /** Змінна що вказує увімкнений чи вимкнений компас.*/
    private static boolean on = false;
    /** Текст где рисуем компасс.*/
    private BitmapText hudText = null; 
    /** Значення кута, куди дивимося.*/
    private float angleView = 0; // 0 - North;
    
    /** Конструктор класу Compass.*/
    private Compass() {
        hudText = new BitmapText(Settings.bitmapFont, false);          
        hudText.setSize(Settings.bitmapFont.getCharSet().getRenderedSize());      // font size
        hudText.setColor(ColorRGBA.Blue);                             // font color
        hudText.setText("You can write any string here");             // the text
        hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
    }
    
    /** Увімкнений чи вимкнений компас.*/
    public static boolean isEnabled(){
        return on;
    }
    
    /** 
     * Увімкнення/вимкення компасу.
     * @param enabled true, false - увімкнути, вимкнути.
     */
    public static void setEnabled(boolean enabled){
        
        on = enabled;
        
        if(enabled) {
            if(_compass==null)
                _compass = new Compass();
            
            Settings.guiNode.attachChild(_compass.hudText);
            _compass.initKeys();
            
            System.out.println("Компас увімкнено!");
        }
        else{
            Settings.guiNode.detachChild(_compass.hudText);
            _compass.uninitKeys();
            System.out.println("Компас вимкнено!");
        }
    }

    /** Ініціалізація дій на кнопки клавіатури чи дії мишки.*/
    private void initKeys() {
        Settings.inputManager.addMapping("Move", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        Settings.inputManager.addMapping("Move_n", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        Settings.inputManager.addListener(analogListener, "Move", "Move_n");
    }
    
    /** Деініціалізація дій на кнопки клавіатури чи дії мишки.*/
    private void uninitKeys() {
        Settings.inputManager.deleteMapping("Move");
        Settings.inputManager.removeListener(analogListener);
    }
    
    /** ActionListener - слухач заданих дій. Залежно від події виконує ті чи інші дії. */
    private AnalogListener analogListener = new AnalogListener() {
        /** Значення складових ветора наряму погляду {x; z}.*/
        private float xd = 0, zd = 0;
        /** Значення x, z при попередній фіксації здвигу камери.*/
        private float oldX = 0, oldZ = 0;
        /** Змінна для збереження тимчасових значень.*/
        private float tmp = 0;
    
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals("Move") || name.equals("Move_n")) {
                xd = Settings.cam.getDirection().x;
                zd = Settings.cam.getDirection().z;
                tmp = (oldX-xd)*(oldX-xd) + (oldZ-zd)*(oldZ-zd);
                
                if(tmp<0) tmp *= -1;
                
                if(tmp>eps){
                    angleView = (float) (Math.acos(xd/Math.sqrt(xd*xd+zd*zd))*K);

                    if(angleView == Float.NaN)  angleView = 0;
                    if(zd<0)                    angleView = 360.0f - angleView;

                    hudText.setText(String.format("Horizontal angle: %.1f", angleView));

                    oldX = xd;
                    oldZ = zd;
                }
            } // else if ...
        }
    };
}