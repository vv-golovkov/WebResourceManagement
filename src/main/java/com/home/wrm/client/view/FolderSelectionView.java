package com.home.wrm.client.view;

import java.util.List;

import com.home.wrm.client.presenter.FolderSelectionPresenter;
import com.home.wrm.client.presenter.FolderSelectionPresenter.IAdapter;
import com.home.wrm.client.util.ITransmitter;
import com.home.wrm.client.util.datasource.DirectoryRecord;
import com.home.wrm.client.view.main.AbstractPopupWindowSupportView;
import com.home.wrm.shared.transport.Directory;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

public abstract class FolderSelectionView extends AbstractPopupWindowSupportView<FolderSelectionPresenter> implements IAdapter {
    public static final String SELECTED_OBJECT_ID = "selectedobjectidentifier";
    private ITransmitter transmitter;
    private List<Directory> dirs;
    
    private ListGrid dirGrid;
    private ButtonItem cancel;
    protected ButtonItem move;
    
    public FolderSelectionView(List<Directory> dirs, ITransmitter transmitter) {
        this.transmitter = transmitter;
        this.dirs = dirs;
    }

    @Override
    protected void assignPresenter() {
        setPresenter(new FolderSelectionPresenter(this));
    }

    @Override
    protected void buildUI() {
        dirGrid = new FolderTable();
        dirGrid.setData(getData());
        ListGridField field = new ListGridField(DirectoryRecord.DIR_PATH_FIELD, "Dir path");
        dirGrid.setFields(field);
        dirGrid.setShowAllRecords(true);
        dirGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        dirGrid.setSelectionType(SelectionStyle.SINGLE);
        dirGrid.setShowHeader(false);
        
        dirGrid.setAlternateRecordStyles(true);
        dirGrid.setAlternateRecordFrequency(0);

        move = new ButtonItem("dfdf", "Move");
        move.setAttribute("transmitterAttribute", transmitter);
        move.setAttribute(SELECTED_OBJECT_ID, getObjectId());
        move.setWidth(60);
        move.setHeight(30);
        move.setAlign(Alignment.RIGHT);
        move.setEndRow(false);
        move.addClickHandler(getMoveClickHandler());
        
        cancel = new ButtonItem("asas", "Cancel");
        cancel.setWidth(60);
        cancel.setHeight(30);
        cancel.setStartRow(false);
        
        DynamicForm form = new DynamicForm();
        form.setColWidths("50%", "50%");
        form.setFields(move, cancel);
        
        VLayout layout = new VLayout(10);
        layout.setWidth100();
        layout.setHeight100();
        layout.setMembers(dirGrid, form);
        layout.setMargin(8);
        
        window.addItem(layout);
    }
    
    private class FolderTable extends ListGrid {
        @Override
        protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
            final String cellStyle = "font-family:MS Reference Sans Serif;font-style:normal;font-size:11;";
            String fieldName = getFieldName(colNum);
            if (DirectoryRecord.DIR_PATH_FIELD.equals(fieldName)) {
                Directory dir = asDirectory(record);
                if (dir.getId() == 0 && dir.getFullPath().equalsIgnoreCase("root")) {
                    return cellStyle + "color:red;";
                }
                return cellStyle;
            }
            return super.getCellCSSText(record, rowNum, colNum);
        }
        
        private Directory asDirectory(ListGridRecord record) {
            DirectoryRecord dirrec = (DirectoryRecord) record;
            return dirrec.asDirectory();
        }
    }
    
    public abstract ClickHandler getMoveClickHandler();
    public abstract int getObjectId();

    @Override
    protected void drawUI() {
        window.show();
    }
    
    public static final class MoveResourceSelectionView extends FolderSelectionView {
        private int resourceId;
        public MoveResourceSelectionView(int resourceId, List<Directory> dirs, ITransmitter transmitter) {
            super(dirs, transmitter);
            this.resourceId = resourceId;
        }
        @Override
        public int getObjectId() {
            return resourceId;
        }
        @Override
        public ClickHandler getMoveClickHandler() {
            return getPresenter().getMoveResourceClickHandler();
        }
    }
    
    public static final class MoveFolderSelectionView extends FolderSelectionView {
        private int selDirId;
        public MoveFolderSelectionView(int dirId, List<Directory> dirs, ITransmitter transmitter) {
            super(dirs, transmitter);
            this.selDirId = dirId;
        }
        @Override
        public int getObjectId() {
            return selDirId;
        }
        @Override
        public ClickHandler getMoveClickHandler() {
            return getPresenter().getMoveFolderClickHandler();
        }
    }

    public void closeWindow() {
        closePopupWindow();
    }
    
    private DirectoryRecord[] getData() {
        DirectoryRecord[] data = new DirectoryRecord[dirs.size()];
        for (int index = 0; index < dirs.size(); index++) {
            data[index] = new DirectoryRecord(dirs.get(index));
        }
        return data;
    }
    
    public ButtonItem getMoveButton() {
        return move;
    }
    
    public ButtonItem getCancelButton() {
        return cancel;
    }
    
    public ListGrid getDirGrid() {
        return dirGrid;
    }
}
