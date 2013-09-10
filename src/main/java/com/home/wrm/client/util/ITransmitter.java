package com.home.wrm.client.util;

import java.util.List;

import com.home.wrm.shared.transport.Directory;


public interface ITransmitter {
    public void refreshResourceTable();
    public void showDirs(List<Directory> dirs);
}
