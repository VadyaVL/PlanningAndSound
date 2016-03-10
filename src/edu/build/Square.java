/*
 * Square.java
 * 
 * 09.03.2016
 * 
 * vadyavl@gmail.com
 */
package edu.build;

import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import myprog.Settings;

/**
 * Square - описує площу будівництва.
 * Приймаємо, 1 одиниця JME = 1 фізичний метр.
 * @author Vadym
 */
public class Square {
    
    // _y - неважливий - ВЖЕ ВАЖЛИВИЙ
    private float _x, _z, _y;             // центр площі
    private float width, length, height;    // ширина і довжина площі, висота забору
    
    private Node border;    // Для малювання границі
    
    /**
     * Square -конструктор класу площа.
     */
    public Square(Spatial s){
        this(0f, 0f, 50f, 50f, s);
    }
    
     /**
     * Square -конструктор класу площа.
     * @param centerx центр площі по x
     * @param centerz центр площі по z
     */
    public Square(float centerx, float centerz, Spatial s){
        this(centerx, centerz, 50f, 50f, s);
    }
    
    /**
     * Square -конструктор класу площа.
     * @param centerx центр площі по x
     * @param centerz центр площі по z
     * @param wid ширина площі
     * @param len довжина площі
     */
    public Square(float centerx, float centerz, float wid, float len, Spatial s){
        _x = centerx;
        _z = centerz;
        width = wid;
        length = len;
        height = 4.0f;
        _y = 0.0f;
        
        findHeight(s);
        
        System.out.println(height);
        System.out.println(_y);
        border = new Node("border");
        //initBorder();
        initMatrix();
        Settings.rootNode.attachChild(border);
    }
    
    private void initBorder(){
        float thickness = 0.2f; // товщина границі.
        
        Box bleft  = new Box(thickness, height, length);   // лівий
        Box bup  = new Box(width, height, thickness);     // верхній
        Material gM = new Material(Settings.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        
        Geometry gL = new Geometry("leftBorder", bleft);
        gL.setLocalTranslation(_x - (width + thickness), _y, _z);
        gL.setMaterial(gM);
        
        Geometry gR = new Geometry("rightBorder", bleft.clone());
        gR.setLocalTranslation(_x + (width + thickness), _y, _z);
        gR.setMaterial(gM);
        
        Geometry gU = new Geometry("leftBorder", bup);
        gU.setLocalTranslation(_x, _y, _z - (length + thickness));
        gU.setMaterial(gM);
        
        Geometry gB = new Geometry("leftBorder", bup.clone());
        gB.setLocalTranslation(_x, _y, _z + (length + thickness));
        gB.setMaterial(gM);
        
        border.attachChild(gL);   
        border.attachChild(gR);  
        border.attachChild(gU); 
        border.attachChild(gB);   
        
    }
    
    private float size = 1.0f; // По умолчания длина одного квадрата...
    
    private void initMatrix(){
        
        Node lines = new Node("Line");
        Line line;
        Geometry geometry;
        Material orange = new Material(Settings.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                orange.setColor("Color", new ColorRGBA(1f, 1f, 1f, 0.2f));
                
        orange.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
         for(float i = -length+_z; i<=length+_z; i+=size){
                line = new Line(new Vector3f(-width+_x, _y+2, i), new Vector3f(width+_x, _y+2, i));
                line.setLineWidth(1);
                geometry = new Geometry("Line0"+i, line);
                geometry.setMaterial(orange); 
                lines.attachChild(geometry);
         }
         
          for(float j = -width+_x; j<=width+_x; j+=size){
                line = new Line(new Vector3f(j, _y+2, -length+_z), new Vector3f(j, _y+2, length+_z));
                line.setLineWidth(1);
                geometry = new Geometry("Line1" + j, line);
                geometry.setMaterial(orange); 
                lines.attachChild(geometry);
         }
          Settings.rootNode.attachChild(lines);
    }
    
    private void findHeight(Spatial s){
        float min = 100;
        float max = -100;
        CollisionResults results;
        Ray ray;
        float currentHeight = 0;
        for(float i = -length/2+_z; i<=length/2+_z; i+=0.2f){
           for(float j = -width/2+_x; j<=width/2+_x; j+=0.2f){
               // 1. Reset results list.
               results = new CollisionResults();
               ray = new Ray(new Vector3f(i, 200.0f, j), new Vector3f(0, -1, 0));
               s.collideWith(ray, results);
               if(results.size() == 1) {
                   currentHeight = results.getCollision(0).getContactPoint().getY();
                   
                   if(max<currentHeight)    max = currentHeight;
                   if(min>currentHeight)    min = currentHeight;
               }
           }    
        }
        height = max - min;
        _y = -height/2;
        
        
        
        // 2. Aim the ray from cam loc to cam direction.
        
        // 3. Collect intersections between Ray and Shootables in results list.
        // DO NOT check collision with the root node, or else ALL collisions will hit the skybox! Always make a separate node for objects you want to collide with.
        
        // 4. Print the results
        
    }
    
    /** Отримати центр площі по x*/
    public float getCX(){
        return  _x;
    }
    
    /** Отримати центр площі по y*/
    public float getCY(){
        return  _y;
    }
    
    /** Отримати центр площі по z*/
    public float getCZ(){
        return  _z;
    }
    
    /** Отримати довжину, по z*/
    public float getLenght(){
        return  length;
    }
    
    /** Отримати ширину, по x*/
    public float getWidth(){
        return  width;
    }
}
