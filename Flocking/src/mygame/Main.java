package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;


/**
 * This is the Main Class of your Game. You should only do initialization here.
 */

public class Main extends SimpleApplication {

    private Flock flock;
    private final int boidCount = 800;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        Texture links = assetManager.loadTexture("Sky/uw_lf.jpg");
        Texture rechts = assetManager.loadTexture("Sky/uw_rt.jpg");
        Texture hinten = assetManager.loadTexture("Sky/uw_bk.jpg");
        Texture vorne = assetManager.loadTexture("Sky/uw_ft.jpg");
        Texture hoch = assetManager.loadTexture("Sky/uw_up.jpg");
        Texture runter = assetManager.loadTexture("Sky/uw_dn.jpg");

        final Vector3f normalScale = new Vector3f(-1, 1, 1);
        Spatial skyBox = SkyFactory.createSky(assetManager,
                        links,
                        rechts, 
                        hinten,
                        vorne,
                        hoch,
                        runter,
                        normalScale);
        rootNode.attachChild(skyBox);

     // Lade das Modell
    Spatial model = assetManager.loadModel("Models/fish.obj");
    model.setLocalScale(0.1f); // Setze die Modellskalierung

    // Erhalte die Geometrie und den Mesh
    Geometry geometry = (Geometry) model;
    Mesh mesh = geometry.getMesh();

    // Erstelle das Material und weise es der Geometrie zu
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

    // Lade die Textur
    Texture texture = assetManager.loadTexture("Textures/fish.png");

    // Weise die Textur dem Material zu
    mat.setTexture("ColorMap", texture);

    geometry.setMaterial(mat);

    // Erstelle die Flock-Instanz mit dem rootNode, der Anzahl der Boids, dem Mesh und dem Material
    flock = new Flock(rootNode, boidCount, mesh, mat);

    // Aktiviere die FlyCam und setze die Bewegungsgeschwindigkeit
    flyCam.setEnabled(true);
    flyCam.setMoveSpeed(30);
         
        
      /*
        Mesh mesh = new Box(0.01f, 0.01f, 0.03f); // the geometric model of one boid. Here a cube of size=(x:0.01,y:0.01,z:0.03)

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); // the surface material for the geometric boid model.
        mat.setColor("Color", ColorRGBA.White);

        // instantiation of the flock
        flock = new Flock(rootNode, boidCount, mesh, mat );

        // camera rotation is controlled via mouse movement while the position is controlled via wasd-keys
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(30);
        */
        
        // Erstelle ein AudioNode-Objekt und lade die Musikdatei
   // AudioNode audioNode = new AudioNode(assetManager, "Under-the-Sea-_English_-_-Kingdom-Hearts-HD-2.5-ReMIX-Remastered-OST.ogg", false);


    AudioNode audioNode = new AudioNode(assetManager, "Sounds/Super-Mario-World-Underwater-_Yoshi_-music.ogg", false);
    // Konfiguriere die Eigenschaften der AudioNode
    audioNode.setPositional(false);
    audioNode.setLooping(true);
    audioNode.setVolume(0.5f);

    // Füge die AudioNode zur Szene hinzu
    rootNode.attachChild(audioNode);

    // Starte die Wiedergabe der Musik
    audioNode.play();

    }
   

    @Override
    public void simpleUpdate(float tpf) {
        flock.update(tpf); // called once per frame
    }

    @Override
    public void simpleRender(RenderManager rm) {
        // add here custom rendering stuff if needed
    }
}
