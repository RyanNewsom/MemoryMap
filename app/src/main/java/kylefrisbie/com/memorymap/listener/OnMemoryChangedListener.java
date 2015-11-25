package kylefrisbie.com.memorymap.listener;

import kylefrisbie.com.memorymap.model.Memory;

public interface OnMemoryChangedListener {
    void onMemoryAdded(Memory memory);
    void onMemoryUpdated(Memory memory);
    void onMemoryRemoved(Memory memory, String marker);
}