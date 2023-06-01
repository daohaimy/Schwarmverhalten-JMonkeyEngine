/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.instancing.InstancedNode;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import com.jme3.math.FastMath;

/**
 * This class controls and manages all boids within a flock (swarm)
 * @author philipp lensing
 */
public class Flock {
    //Binaerbaum kdbaum = new Binaerbaum();
    
    private Material boidMaterial;
    private Mesh boidMesh;
    private Node scene;
    private InstancedNode instancedNode;
    private List<Boid> boids;
    
    
    private int chunkSize = 2;
    private HashMap<Map,ArrayList<Boid>> worldHash = new HashMap<>();
    //private HashMap<Map,ArrayList<Boid>> worldHash2 = new HashMap<>();
    
    private int worldHashSize = 32;
    //private int worldHashSize2 = 16;
    
    private float viewDistance = 2f;
    private float fov = FastMath.cos((FastMath.DEG_TO_RAD*70.0f)/2f);
    
    /*
    public static float separationD = 1f;
    public static float alignmentD = 1f;
    public static float cohesionD = 1;
    public static float force = 1f;
    */
    
    /**
     * 
     * @param scene a reference to the root node of the scene graph (e. g. rootNode from SimpleApplication).
     * @param boidCount number of boids to create.
     * @param boidMesh reference mesh (geometric model) which should be used for a single boid.
     * @param boidMaterial the material controls the visual appearance (e. g. color or reflective behavior) of the surface of the boid model.
     */
    
    public Flock( Node scene, int boidCount, Mesh boidMesh, Material boidMaterial ) {
        this.boidMesh = boidMesh;
        this.boidMaterial = boidMaterial;
        this.scene = scene;
        
        this.boidMaterial.setBoolean("UseInstancing", true);
        this.instancedNode = new InstancedNode("instanced_node");
        this.scene.attachChild(instancedNode);
       
        
        
        boids = createBoids(boidCount);
        
        instancedNode.instance();
        
        for(int x = -worldHashSize; x<=worldHashSize;x++){
            for(int y = -worldHashSize;y<=worldHashSize;y++){
                for(int z = -worldHashSize; z<=worldHashSize;z++){
                    worldHash.put(new Map(x,y,z), new ArrayList<Boid>());
                }
            }
        }
    }

    /**
     * The update method should be called once per frame
     * @param dtime determines the elapsed time in seconds (floating-point) between two consecutive frames
     */
    public void update(float dtime)
    {
        
        
        
        for( Boid boid : boids )
        {           
        Vector3f netAccelarationForBoid = new Vector3f();
            boid.oldChunkPosition.setValues(boid.chunkPosition.x, boid.chunkPosition.y, boid.chunkPosition.z);
            boid.chunkPosition.setValues((int)boid.position.x/chunkSize, (int)boid.position.y/chunkSize, (int)boid.position.z/chunkSize);
  

            if(!boid.oldChunkPosition.equals(boid.chunkPosition)){
                
                
                worldHash.get(boid.oldChunkPosition).remove(boid);
                worldHash.get(boid.chunkPosition).add(boid);
                /*
                worldHash2.get(boid.oldChunkPosition).remove(boid);
                worldHash2.get(boid.chunkPosition).add(boid);*/
            } 
            
            
            // Kohäsion
            Vector3f nachbarBoids = new Vector3f();
            Vector3f centroid = Vector3f.ZERO;
        
            // Trennung
            float kleinsterAbstand = Float.MAX_VALUE;
            Boid nachbar = null;
            Vector3f directionNachbar = new Vector3f();
            Vector3f separation = Vector3f.POSITIVE_INFINITY;
            Vector3f distanzZweiPosition = new Vector3f();
            float quadrat = 0;
        
            // Ausrichtung
            Vector3f alignment = Vector3f.ZERO;
            float wji2 = 0;
            float wji = 0;
            float wi = 0;
            Vector3f vj = new Vector3f();
            Vector3f summeWjiVi = new Vector3f();
            float radius = 1f;
            int theta = 135;
            Vector3f normalizedDistanz = new Vector3f();
            float p2 = 0;

            
            for(int x = -1; x<=1;x++){
                for(int y = -1; y<=1; y++){
                    for(int z = -1; z<=1;z++){   
                        for(Boid boidNachbar : worldHash.get(boid.chunkPosition.add(x,y,z))){
                           
                       
                            if(boid != boidNachbar){
                               
                                //Kohäsion
                                nachbarBoids = nachbarBoids.add(boidNachbar.position);   
                                

                                //Trennung
                                distanzZweiPosition = boid.position.subtract(boidNachbar.position);
                                //Vector3f beschleunigung = distanzZweiPosition.divide(boid2.position.mult(boid2.position));
                                if(distanzZweiPosition.length() < kleinsterAbstand){
                                    kleinsterAbstand = distanzZweiPosition.length();
                                    //nachbar = boidus;
                                    directionNachbar = boid.position.subtract(boidNachbar.position);
                                }  


                                // Ausrichtung
                                if(distanzZweiPosition.length() < radius){
                                    normalizedDistanz = distanzZweiPosition.normalize();
                                    p2 = normalizedDistanz.dot(boid.velocity.normalize());
                                    if(p2 <= Math.cos(theta/2)){ 
                                        wji2 = (boid.position.subtract(boidNachbar.position)).length();
                                        wji2 = wji2 * wji2;
                                        wji2 = 1/wji2;
                                        wi = wi + 1;
                                        wji = wji + wji2;
                                        vj = vj.add(boidNachbar.velocity);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        
            
            //netAccelarationForBoid = boid.position.negate().mult(force);
                          
            //Kohäsion
            centroid = (nachbarBoids.mult(1/boids.size())).subtract(boid.position);
            //netAccelarationForBoid = netAccelarationForBoid.add(centroid.mult(cohesionD));

            //Trennung
            quadrat = kleinsterAbstand*kleinsterAbstand;
            separation = (directionNachbar.divide(quadrat));
            //netAccelarationForBoid = netAccelarationForBoid.add(separation.mult(separationD));

            // Ausrichtung
            if(wi > 0){
                wi = wji * wi;
                wi = 1/wi;
                summeWjiVi = vj.mult(wji);
                alignment = summeWjiVi.mult(wi);
            }
            Vector3f ergebnis = new Vector3f();
            //netAccelarationForBoid = netAccelarationForBoid.add(alignment.mult(alignmentD));
            
            
            ergebnis = separation.add(centroid.add(alignment));    
            netAccelarationForBoid = ergebnis;       
            
        //}
      
        
        boid.update(netAccelarationForBoid, dtime); 
        }
    }
    
    
    /**
     * Creates a list of Boid objects and adds corresponding instanced models (based on boidMesh) to the scene graph
     * @param boidCount The number of boids to create
     * @return A list of Boid objects. For each object a corresponding instanced geometry is added to the scene graph (Boid.geometry)
     */ 
    
    private List<Boid> createBoids(int boidCount)
    {
        List<Boid> boidList = new ArrayList<Boid>();
        
        for(int i=0; i<boidCount; ++i)
        {
            Boid newBoid = new Boid(createInstance());
            boidList.add(newBoid);
        }
        
        return boidList;
    }
    
    
    /**
     * Creates an instanced copy of boidMesh using boidMaterial with individual geometric transform
     * @return The instanced geometry attached to the scene graph
     */
    private Geometry createInstance()
    {
        Geometry geometry = new Geometry("boid", boidMesh);
        geometry.setMaterial(boidMaterial);
        instancedNode.attachChild(geometry);
        return geometry;
    }     
    
    
    
    
}
