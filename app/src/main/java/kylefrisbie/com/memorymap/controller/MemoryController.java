package kylefrisbie.com.memorymap.controller;

import java.util.List;

import kylefrisbie.com.memorymap.listener.OnMemoryChangedListener;
import kylefrisbie.com.memorymap.model.Memory;

public class MemoryController {
    private static MemoryController mInstance;
    private static OnMemoryChangedListener mMemoryChanged;
    /**
     * Private constructor so that you can not create an object of this type
     */
    private MemoryController(){}

    /**
     * Gets an instance of the memory controller
     * @return - the instance
     */
    public static MemoryController getInstance(OnMemoryChangedListener callback){
        if(mInstance == null){
            mInstance = new MemoryController();
            mMemoryChanged = callback;
        }
        return mInstance;
    }

    public void createMemory(Memory newMemory){
        // Create new memory
        newMemory.save();
        // Notify map here
        mMemoryChanged.onMemoryAdded(newMemory);
    }

    public void updateMemory(Memory updatedMemory){
        // Update the model
        Memory memory = Memory.findById(Memory.class, updatedMemory.getId());
        memory.setTitle(updatedMemory.getTitle());
        memory.setDate(updatedMemory.getDate());
        memory.setPeople(updatedMemory.getPeople());
        memory.setPhotoURI(updatedMemory.getPhotoURI());
        memory.setDescription(updatedMemory.getDescription());
        memory.setLatitude(updatedMemory.getLatitude());
        memory.setLongitude(updatedMemory.getLongitude());
        memory.setPlaceName(updatedMemory.getPlaceName());
        memory.save();
        // Notify the map
        mMemoryChanged.onMemoryUpdated(memory);
    }

    public void deleteMemory(Memory memoryToDelete){
        // Update the model
        Memory memory = Memory.findById(Memory.class, memoryToDelete.getId());
        memory.delete();
        // Notify the map
        mMemoryChanged.onMemoryRemoved(memory);
    }

    public List<Memory> getMemories(){
        // Get the memories from the model
        try {
            return Memory.listAll(Memory.class);
        } catch (Exception e) {
            return  null;
        }
    }

    public List<Memory> findMemoryByTitle(String title) {
        // get the memories with a given title
        return Memory.find(Memory.class, "title = ?", title);
    }

    public Memory findMemoryByID(Long id) {
        // get the memory with a given id
        return Memory.findById(Memory.class, id);
    }
}
