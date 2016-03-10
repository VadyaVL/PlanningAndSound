package edu.controls;

import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.controls.window.builder.WindowBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import myprog.Settings;

/**
 * PanelController - дозволяє управляти панелями(вікнами) інструметів в режимі вибору.
 * При виборі певного елементу відображає його властивості, що можна змінювати.
 * @author Vadym
 * @version 1.0
 * @since 26.02.2015
 */
public class Panel {
    
    /** Об'єкт даного класу. Для реалізації Singleton.*/
    private static Panel pc = null;
    /** Змінна що вказує увімкнена чи вимкнена панель.*/
    private static boolean on = false;
    /** Об'єкт Nifty.*/
    private Nifty nifty;
    private NiftyJmeDisplay niftyDisplay;
    
    /** Конструктор класу PanelController. Ініціалізує вікно.*/
    private Panel() {
        niftyDisplay = new NiftyJmeDisplay(Settings.assetManager, 
                                                            Settings.inputManager, 
                                                            Settings.audioRenderer, 
                                                            Settings.guiViewPort);
        nifty = niftyDisplay.getNifty();
        Settings.guiViewPort.addProcessor(niftyDisplay);

        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");

        // <screen>
        nifty.addScreen("Screen_Empty", new ScreenBuilder("EmptyScreen"){{
            controller(new pController()); // Screen properties       

            // <layer>
            layer(new LayerBuilder("Layer_Empty") {{
                childLayoutAbsolute(); // layer properties, add more...
              }
            });
            // </layer>
          }}.build(nifty));
        // </screen>
        
        // <screen>
        nifty.addScreen("Screen_Box", new ScreenBuilder("Hello Nifty Screen"){{
            controller(new pController()); // Screen properties       

            // <layer>
            layer(new LayerBuilder("Layer_ID") {{
                childLayoutAbsolute(); // layer properties, add more...

                control(new WindowBuilder("Window_ID", "Hello Nifty"){{ 
                        width("150px"); // windows will need a size
                        height("400px");
                    // <panel>
                    panel(new PanelBuilder("Panel_ID") {{
                       childLayoutVertical(); // panel properties, add more...               

                        // GUI elements
                       control(new TextFieldBuilder("TF_1", "15"){{
                            alignCenter();
                            valignCenter();
                        }});
                       control(new TextFieldBuilder("TF_2", "15"){{
                            alignCenter();
                            valignCenter();
                        }});
                       
                        control(new ButtonBuilder("Button_ID", "Ok!"){{
                            alignCenter();
                            valignCenter();
                            interactOnClick("Click()");
                        }});

                        //.. add more GUI elements here              

                    }});
                    // </panel>
                }});
                
              }
            });
            // </layer>
          }}.build(nifty));
        // </screen>

    }

    /** Геттер, що дозволяє дізнатися увімкнений чи ні режим вибору елемента.*/
    public static boolean isON(){
        return on;
    }
    
    /** Закриття панелі.*/
    private void closePanel(){
        //Settings.guiViewPort.removeProcessor(niftyDisplay);
        //nifty = null;
        //niftyDisplay = null;
    }
    
    /** Увімкнення панелі.*/
    public static void panelON(){
        
        if(!on){
            on = true;
            if(pc == null)
                pc = new Panel();
            
            gotoPanel(PanelID.pNULL);
            
            System.out.println("PanelController yвімкнено!");
        } else {
            System.out.println("PanelController вже увімкнено!");
        }
        
    } 
    
    /** Вимкнення панелі.*/
    public static void panelOFF(){
        if(on){
            on = false;
            pc.nifty.gotoScreen("Screen_Empty");
            //pc.closePanel();
            //pc = null;
            System.out.println("PanelController вимкнено!");
        }
        else {
            System.out.println("PanelController вже вимкнено!");
        }        
    } 
    
    /** 
     * Перехід до  визначеного екрану.
     * @param pID ідентифікатор екрану чи панелі.
     */
    public static void gotoPanel(PanelID pID){
        if(on){
            if(pID == PanelID.pBOX){
                pc.nifty.gotoScreen("Screen_Box");
            } else if(pID == PanelID.pNULL){
                pc.nifty.gotoScreen("Screen_Empty");
            }
        } else {
            System.out.println("PanelController вимкнено! Увімкніть для використання.");
        } 
    }
    
    /** Контроллер для панелі.*/
    public class pController implements ScreenController{
        
        public pController(){}
        
        public void bind(Nifty nifty, Screen screen) {
            System.out.println("Bind " + screen.getScreenId());
        }

        public void onStartScreen() {
            System.out.println("Start screen");
        }

        public void onEndScreen() {
            System.out.println("End screen");
        }

        public void Click(){
            System.out.println("Click ");
            System.out.println(nifty.getCurrentScreen().findNiftyControl("TF_1", TextField.class).getRealText());

        }
        
    }
}
