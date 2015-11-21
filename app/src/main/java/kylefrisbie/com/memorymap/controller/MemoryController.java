package kylefrisbie.com.memorymap.controller;

import java.util.ArrayList;

import kylefrisbie.com.memorymap.Memory;

/**
 * Created by Ryan on 11/21/2015.
 */
public class MemoryController {
    private static MemoryController mInstance;

    /**
     * Private constructor so that you can not create an object of this type
     */
    private MemoryController(){}

    /**
     * Gets an instance of the memory controller
     * @return - the instance
     */
    public static MemoryController getInstance(){
        if(mInstance == null){
            mInstance = new MemoryController();
        }
        return mInstance;
    }

    public void createMemory(Memory newMemory){
        //Update the model, and notify the map
    }

    public void updateMemory(Memory updatedMemory){
        //Update the model, and notify the map
    }

    public void deleteMemory(Memory memoryToDelete){
        //Update the model, notify the map
    }

    public ArrayList<Memory> getMemories(){
        //get the memories from the model
        return null;
    }
}
