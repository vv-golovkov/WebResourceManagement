package com.home.wrm.client.presenter;

import java.util.List;

import com.home.wrm.client.util.ITransmitter;
import com.home.wrm.client.util.adapter.PopupAdaptable;
import com.home.wrm.client.util.callback.ISuccessCallbackExecutor;
import com.home.wrm.client.util.datasource.DirectoryRecord;
import com.home.wrm.client.view.FolderSelectionView;
import com.home.wrm.shared.transport.Directory;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class FolderSelectionPresenter extends BasePresenter<FolderSelectionPresenter.IAdapter> {
    
    public FolderSelectionPresenter(IAdapter adapter) {
        super(adapter);
    }

    public interface IAdapter extends PopupAdaptable {
        ListGrid getDirGrid();
        ButtonItem getMoveButton();
        ButtonItem getCancelButton();
    }

    @Override
    public void addPageActions() {
        getAdapter().getCancelButton().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                getAdapter().closeWindow();
            }
        });
    }
    
    private int validateTargetDirectoryId() {
        ListGridRecord selectedRecord = getAdapter().getDirGrid().getSelectedRecord();
        if (selectedRecord == null) {
            showWarning("Select target folder.");
            return -1;
        }
        DirectoryRecord directoryRecord = (DirectoryRecord) selectedRecord;
        int targetDirId = directoryRecord.asDirectory().getId();
        return targetDirId;
    }
    
    public ClickHandler getMoveResourceClickHandler() {
        return new ClickHandler() {
            public void onClick(ClickEvent event) {
                processFlowToMoveResource();
            }
        };
    }
    
    private void processFlowToMoveResource() {
        int targetDirId = validateTargetDirectoryId();
        if (targetDirId == -1) return;
        
        ITransmitter transmitter = (ITransmitter) getAdapter().getMoveButton().getAttributeAsObject("transmitterAttribute");
        Integer resourceId = getAdapter().getMoveButton().getAttributeAsInt(FolderSelectionView.SELECTED_OBJECT_ID);
        
        executeRpcCallToMoveResource(resourceId.intValue(), targetDirId, transmitter);
    }
    
    private void executeRpcCallToMoveResource(int resId, int dirId, final ITransmitter transmitter) {
        showModalPromptToUser("Moving resource...");
        rpc.updateResourceTargetDir(resId, dirId, newSuccessCallback(new ISuccessCallbackExecutor<Void>() {
            public void onSuccessCallbackAction(Void result) {
                getAdapter().closeWindow();
                transmitter.refreshResourceTable();
            }
        }));
    }
    
    /*************************** move resource *********************************/
    public ClickHandler getMoveFolderClickHandler() {
        return new ClickHandler() {
            public void onClick(ClickEvent event) {
                processFlowToMoveFolder();
            }
        };
    }
    
    private void processFlowToMoveFolder() {
        int targetDirId = validateTargetDirectoryId();
        if (targetDirId == -1) return;
        
        ITransmitter transmitter = (ITransmitter) getAdapter().getMoveButton().getAttributeAsObject("transmitterAttribute");
        Integer dirId = getAdapter().getMoveButton().getAttributeAsInt(FolderSelectionView.SELECTED_OBJECT_ID);
        
        executeRpcCallToMoveFolder(dirId, targetDirId, transmitter);
    }
    
    private void executeRpcCallToMoveFolder(int dirId, int targetDirId, final ITransmitter transmitter) {
        showModalPromptToUser("Moving folder...");
        rpc.moveFolderTo(dirId, targetDirId, newSuccessCallback(new ISuccessCallbackExecutor<List<Directory>>() {
            public void onSuccessCallbackAction(List<Directory> dirs) {
                getAdapter().closeWindow();
                transmitter.showDirs(dirs);
            }
        }));
    }
}
