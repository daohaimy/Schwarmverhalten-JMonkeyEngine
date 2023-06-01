/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mygame;

import java.util.Objects;


/**
 *
 * @author tjhen
 */
public class Map {
    public int x;
    public int y;
    public int z;

    private int hashCode;

    public Map(){
        setValues(0,0,0);
    }

    public Map(int x, int y, int z){
        setValues(x,y,z);
    }

    @Override
    public boolean equals(Object other){

        if(this == other){
            return true;
        }
        if(other == null || other.getClass() != this.getClass()){
            return false;
        }

        Map otherV = (Map)other;
        return x == otherV.x && y == otherV.y && z == otherV.z;
    }

    @Override
    public int hashCode(){
        return hashCode;
    }
    
    
    @Override
    public String toString(){
        return "" + x + "," + y + "," + z;
    }

    public Map add(Map other){
        return new Map(other.x + this.x, other.y + this.y, other.z + this.z);
    }

    public Map add(int x, int y, int z){
        return new Map(x+this.x, y+this.y, z+this.z);
    }

    public void setValues(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.hashCode = Objects.hash(x,y,z);
    }
}