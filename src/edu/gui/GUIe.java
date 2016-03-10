/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gui;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import myprog.Settings;

/**
 * Відображення інформації на площині екрану.
 * @author Vadym
 */
public final class GUIe {
    
    /** Текст по центру екрану. +*/
    private static BitmapText ch = null;
    /** Текст що відображає режим.*/
    private static BitmapText textMode = null;
    /** Відображення/-не тексту по центру екрану +*/
    private static boolean onCrossHairs = false;
    /** Відображення/-не інформації компасу.*/
    private static boolean onCompass = false;
    /** Відображення/-не режиму.*/
    private static boolean modeView = false;
   
    /** Об'єкт, що дозволяє переглянутиградусну міру напрямку зору.*/
    Compass compass;
    
    /** 
     * Налаштовуємо, що бачимо на екрані.
     * @param onCS Видимість +
     * @param _onCompass Видимість кута погляду
     */
    public static void setGUISettings(boolean onCS, boolean _onCompass, boolean _howMode){
        try {
            setCompass(_onCompass);
            setCrossHairs(onCS);
            setMode(_howMode);
        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Ініціалізація відображення текстової інформації.
     */
    public static void setGUISettings(){
        setGUISettings(false, false, false);
    }
    
    /** Увімкнений + по центру чи ні.*/
    public static boolean isCrossHairs(){
        return onCrossHairs;
    }
    
    /** Увімкнений компас чи ні.*/
    public static boolean isCompass(){
        return onCompass;
    }
    
    /** Увімкнений показ режиму чи ні.*/
    public static boolean isMode(){
        return modeView;
    }
    
    //*******************************************************************
    /** Увімкнути/вимкнути відображення +.*/
    public static void setCrossHairs(boolean b){
        onCrossHairs = b;
        
        if(b) {
            if(ch == null){
                ch = new BitmapText(Settings.assetManager.loadFont("Interface/Fonts/Default.fnt"), false);
                ch.setSize(40);
                ch.setText("+"); // crosshairs
                ch.setLocalTranslation( Settings.winWidth / 2 - ch.getLineWidth()/2, 
                                        Settings.winHeight / 2 + ch.getLineHeight()/2, 0);
            }
            if(!Settings.guiNode.hasChild(ch))
                Settings.guiNode.attachChild(ch);

        }
        else if(Settings.guiNode.hasChild(ch))
                Settings.guiNode.detachChild(ch);
    }
    
    /** Увімкнути/вимкнути відображення компасу.*/
    public static void setCompass(boolean b){
        onCompass = b;
        Compass.setEnabled(onCompass);
    }
    
    /** Увімкнути/вимкнути відображення режиму.*/
    public static void setMode(boolean b){
        modeView = b;
        
        if(b) {
            if(textMode == null){
                textMode = new BitmapText(Settings.bitmapFont, false);          
                textMode.setSize(Settings.bitmapFont.getCharSet().getRenderedSize());      // font size
                textMode.setColor(ColorRGBA.Blue);    
                updTextMode();
                textMode.setLocalTranslation(500, textMode.getLineHeight(), 0);
            }
            if(!Settings.guiNode.hasChild(textMode))
                Settings.guiNode.attachChild(textMode);
        }
        else if(Settings.guiNode.hasChild(textMode))
                Settings.guiNode.detachChild(textMode);
    }
    
    /** В залежності від того в якому режимі оновлюємо відображення назви режиму.*/
    public static void updTextMode(){
        if(modeView && textMode!=null){
            if(Settings.modeBuild)
                textMode.setText("Building mode!");
            else
                textMode.setText("View mode!");
        }
        else
            System.out.println("Отображение не активировано!");
    }
    
    /** Конструктор класу GUIe.*/
    private GUIe() { }
}
