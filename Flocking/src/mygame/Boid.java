/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

/**
 * Boid represents one individual boid in the flock.
 * It's motion is integrated within the update method, which should be called once per frame.
 * @author philipp lensing
 */
public class Boid {
    public static float spawnVolumeSize = 10.0f;
    public Vector3f position;
    public Vector3f velocity;
    private Geometry geometry;
    
    public Map chunkPosition = new Map();
    public Map oldChunkPosition = new Map();

    /**
     * The constructor instantiates a Boid a random position p within -spawnVolumeSize/2 <= p <= spawnVolumeSize/2.
     * The initial velocity is set to random 3D-vector with a magnitude of one.
     * @param geom corresponds to a geometry object within the scene graph and has to exist.
     */
    public Boid(Geometry geom) {
        this.geometry = geom;
        velocity = new Vector3f();
        position = new Vector3f();
        position.x = (FastMath.nextRandomFloat() -0.5f) * spawnVolumeSize;
        position.y = (FastMath.nextRandomFloat() -0.5f) * spawnVolumeSize;
        position.z = (FastMath.nextRandomFloat() -0.5f) * spawnVolumeSize;
        velocity.x = (FastMath.nextRandomFloat() -0.5f);
        velocity.y = (FastMath.nextRandomFloat() -0.5f);
        velocity.z = (FastMath.nextRandomFloat() -0.5f);
        velocity.normalizeLocal();
    }
    
    float maxSpeed = 5f;
    /**
     * update calculates the new position of the Boid based on its current position and velocity influenced by accelaration. update should be called once per frame
     * @param accelaration The net accelaration of all forces that influence the boid
     * @param dtime The elapsed time in seconds between two consecutive frames
     */
    public void update(Vector3f accelaration, float dtime)
    {
        //integrate velocity: v = v + a*dt; keep in mind: [m/s^2 * s = m/s]
        //integrate position: p = p + v*dt; keep in mind: [m/s * s = m]
        velocity = velocity.add(accelaration.mult(dtime));
        
        if(velocity.length() > maxSpeed){
            velocity = velocity.normalize().mult(maxSpeed);
        }
        
        position = position.add(velocity.mult(dtime));        
        //update scene instance
        geometry.setLocalTranslation(position);
        geometry.lookAt(position.add(velocity), Vector3f.UNIT_Y);
  
    }
}

