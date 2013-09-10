package com.home.wrm.shared.transport;


public class Directory extends DtoBase {
    private String name;
    private int parentDirectoryId;
    private String fullPath;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getParentDirectoryId() {
        return parentDirectoryId;
    }
    
    public void setParentDirectoryId(int parentDirectoryId) {
        this.parentDirectoryId = parentDirectoryId;
    }
    
    public String getFullPath() {
        return fullPath;
    }
    
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Directory)) {
            return false;
        }
        Directory directory = (Directory) obj;
        return this.getId() == directory.getId();
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (this.getId() ^ (this.getId() >>> 32));
        return hash;
    }
    
    @Override
    public String toString() {
        return getId() + "-" + name + "-" + parentDirectoryId + " - " + fullPath;
    }
}
