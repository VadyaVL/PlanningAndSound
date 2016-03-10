/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import com.jme3.material.Material;
import com.jme3.scene.Node;
import myprog.Settings;

/**
 *
 * @author Vadym
 */
public class Door {
    
    private Node doorBox;
    private Material doorBoxMat;
    
    public Door(String model, Node node) {
        doorBox = (Node) Settings.assetManager.loadModel(model);
        doorBoxMat = new Material(Settings.assetManager, 
         "Common/MatDefs/Light/Lighting.j3md");
        doorBox.setMaterial(doorBoxMat);
        doorBox.scale(5);
        doorBox.setLocalTranslation(-150.0f, -5.0f, 150.0f);
        node.attachChild(doorBox);
    }
    
    public Node getNode(){
        return doorBox;
    }
}
