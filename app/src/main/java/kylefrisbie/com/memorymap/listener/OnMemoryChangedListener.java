package kylefrisbie.com.memorymap.listener;

public interface OnMemoryChangedListener {
    void onMemoryAdded();
    void onMemoryUpdated();
    void onMemoryRemoved();
}