package com.home.wrm.shared.transport;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DtoBase implements IsSerializable {
    private int id;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
}
