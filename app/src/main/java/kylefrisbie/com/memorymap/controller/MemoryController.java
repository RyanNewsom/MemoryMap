package kylefrisbie.com.memorymap.controller;

import java.util.ArrayList;
import java.util.List;

import kylefrisbie.com.memorymap.Model.Memory;

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
        // Create new memory
        newMemory.save();

        // Notify map here
    }

    public void updateMemory(Memory updatedMemory){
        // Update the model
        Memory memory = Memory.findById(Memory.class, updatedMemory.getId());
        memory.setTitle(updatedMemory.getTitle());
        memory.setDate(updatedMemory.getDate());
        memory.setPeople(updatedMemory.getPeople());
        memory.setPhotoURI(updatedMemory.getPhotoURI());
        memory.setDescription(updatedMemory.getDescription());
        memory.setLocation(updatedMemory.getLocation());
        memory.save();
        // Notify the map
    }

    public void deleteMemory(Memory memoryToDelete){
        // Update the model
        Memory memory = Memory.findById(Memory.class, memoryToDelete.getId());
        memory.delete();

        // Notify the map
    }

    public List<Memory> getMemories(){
        //get the memories from the model
        return Memory.listAll(Memory.class);
    }

    public List<Memory> findMemoryByTitle(String title) {
        // get the memories with a given title
        return Memory.find(Memory.class, "title = ?", title);
    }
}
