package com.home.wrm.shared.transport;

public class Resource extends DtoBase {
    private String name;
    private String link;
    private String savingDate;
    private String savingTime;
    private int directoryId;
    
    /******* RELATED FIELDS *******/
    private String fullDirPath;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLink() {
        return link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public String getSavingDate() {
        return savingDate;
    }
    
    public void setSavingDate(String savingDate) {
        this.savingDate = savingDate;
    }
    
    public String getSavingTime() {
        return savingTime;
    }
    
    public void setSavingTime(String savingTime) {
        this.savingTime = savingTime;
    }
    
    public int getDirectoryId() {
        return directoryId;
    }
    
    public void setDirectoryId(int directoryId) {
        this.directoryId = directoryId;
    }
    
    public String getFullDirPath() {
        return fullDirPath;
    }
    
    public void setFullDirPath(String fullDirPath) {
        this.fullDirPath = fullDirPath;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Resource)) {
            return false;
        }
        Resource directory = (Resource) obj;
        return this.getId() == directory.getId();
    }
    
    @Override
    public int hashCode() {
        int hash = 6;
        hash = 16 * hash + (int) (this.getId() ^ (this.getId() >>> 32));
        return hash;
    }
    
    @Override
    public String toString() {
        return getId() + "-" + name + "-" + link;
    }
}
