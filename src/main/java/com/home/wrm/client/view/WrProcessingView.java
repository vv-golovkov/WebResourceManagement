package com.home.wrm.client.view;

import com.google.gwt.user.client.Window;
import com.home.wrm.client.presenter.WrProcessingPresenter;
import com.home.wrm.client.presenter.WrProcessingPresenter.IAdapter;
import com.home.wrm.client.util.datasource.DirectoryTreeNode;
import com.home.wrm.client.util.datasource.ResourceDatasource;
import com.home.wrm.client.util.datasource.ResourceDatasource.ResourceRecord;
import com.home.wrm.client.view.main.AbstractHeaderSupportView;
import com.home.wrm.client.view.widget.WrmTextItem;
import com.home.wrm.shared.transport.Directory;
import com.home.wrm.shared.transport.Resource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.RowSpacerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class WrProcessingView extends AbstractHeaderSupportView<WrProcessingPresenter> implements IAdapter {
    private TextItem resourceField;
    private TextItem nameField;
	private ButtonItem saveButton;
	private HLayout treeFormLayout;
	private TreeGrid directoriesTree;
	private ListGrid listGrid;
	private DynamicForm form;
	
	@Override
	protected void assignPresenter() {
		setPresenter(new WrProcessingPresenter(this));
	}
	
	@Override
	protected void buildUI() {
	    directoriesTree = new DirTreeGrid();
	    directoriesTree.setWidth("100%");
//	    directoriesTree.setHeight("80%");
	    directoriesTree.setSelectionType(SelectionStyle.SINGLE);
	    directoriesTree.setShowAllRecords(true);
	    directoriesTree.setShowConnectors(true);
	    directoriesTree.setNodeIcon("folder_open_closed.png");
	    directoriesTree.setShowHeader(false);
	    
	    TreeGridField gridField = new TreeGridField(DirectoryTreeNode.DIR_NAME_FIELD);
	    gridField.setCanEdit(true);
	    directoriesTree.setFields(gridField);
	    
	    depriveHeaderPrivileges(directoriesTree);
	    directoriesTree.setShowCellContextMenus(true);
	    
//	    directoriesTree.setCanAcceptDrop(true);
//	    directoriesTree.setCanAcceptDroppedRecords(true);
//	    directoriesTree.setCanReparentNodes(true);
//	    directoriesTree.setCanDropOnLeaves(true);
	    
		final Label separateLabel = createDefaultSeparateLabel("Web resource information");
		form = createForm();
		
		ResourceDatasource dataSource = ResourceDatasource.getInstance();
		listGrid = new ResourceGrid(WrProcessingView.this);
        listGrid.setID("abc_123_");
        listGrid.setCellHeight(27);
        listGrid.setShowAllRecords(true);
        listGrid.setFixedRecordHeights(false);
        listGrid.setWrapCells(true);
        listGrid.setSelectionType(SelectionStyle.NONE);
        listGrid.setDataFetchMode(FetchMode.BASIC);
//        listGrid.setCanDragRecordsOut(true);
        
        listGrid.setShowHeader(false);
        listGrid.setShowRecordComponents(true);
        listGrid.setShowRecordComponentsByCell(true);
        
        listGrid.setModalEditing(true);
        listGrid.setAutoSaveEdits(true);
        listGrid.setEditByCell(true);
        
//        listGrid.setGroupStartOpen(GroupStartOpen.NONE);
        listGrid.setDataSource(dataSource);
//        listGrid.setGroupByField(ResourceDatasource.FULL_DIR_PATH_FIELD);
        depriveHeaderPrivileges(listGrid);
		
		/****************** layouts ******************/
		/*HLayout buttonsLayout = new HLayout(50);
		buttonsLayout.setMembers();
		buttonsLayout.setAlign(Alignment.CENTER);*/
		
		final VLayout formActionLayout = new VLayout(0);
		formActionLayout.setMembers(separateLabel, form, listGrid);
		
		final VLayout treeActionLayout = new VLayout(5);
		treeActionLayout.setWidth("35%");
		treeActionLayout.setMembers(directoriesTree);
		
		treeFormLayout = new HLayout(15);
		treeFormLayout.setMembers(treeActionLayout, formActionLayout);
		
		/****************** borders ******************/
		/*directoriesTree.setBorder("1px solid red");
		formActionLayout.setBorder("1px solid blue");
		treeActionLayout.setBorder("1px solid green");
		treeFormLayout.setBorder("1px solid black");*/
	}
	
	@Override
	protected void drawUIWithMembers(VLayout mainLayout) {
	    mainLayout.addMember(treeFormLayout);
	}
	
	@Override
	protected void drawUI() {
	    super.drawUI();
	    getPresenter().loadAllDirectories();
	}
	
	/*-----------------------------------------------------------*/
	
	private final class DirTreeGrid extends TreeGrid {
	    @Override
	    protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
	        final String STYLE = "font-family:MS Reference Sans Serif;font-style:normal;font-size:11;";
	        String fieldName = getFieldName(colNum);
	        if (DirectoryTreeNode.DIR_NAME_FIELD.equals(fieldName)) {
	            DirectoryTreeNode node = (DirectoryTreeNode) record;
	            if (node.asDirectory().getParentDirectoryId() == 0) {
	                return STYLE + "background-color:#EEEEEE;";
	            }
	            return STYLE;
	        }
	        return super.getCellCSSText(record, rowNum, colNum);
	    }
	}
	
	private final class ResourceGrid extends ListGrid {
        private WrProcessingView view;
        public ResourceGrid(WrProcessingView view) {
            this.view = view;
        }
        
        private ImgButton prepareImgButton(String icon) {
            ImgButton img = new ImgButton();
            img.setWidth(16);
            img.setHeight(16);
            img.setSrc(icon);
            img.setShowRollOver(false);
            img.setShowDown(false);
            return img;
        }
        
        private Resource recordToResource(ListGridRecord record) {
            ResourceRecord resourceRecord = (ResourceRecord) record;
            return resourceRecord.asResource();
        }
        
        @Override
        protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
            final String style1 = "font-family:MS Reference Sans Serif;font-style:normal;font-size:11;";
            final String style2 = "font-size:3px;";
            String fieldName = getFieldName(colNum);
            if (ResourceDatasource.LINK_FIELD.equals(fieldName)) {
                return style1;
            } else if (ResourceDatasource.NAME_FIELD.equals(fieldName)) {
                return style1;
            } else if (ResourceDatasource.SAVING_DATE_FIELD.equals(fieldName)) {
                return style1;
            } else if (ResourceDatasource.SAVING_TIME_FIELD.equals(fieldName)) {
                return style1;
            } else if (ResourceDatasource.FULL_DIR_PATH_FIELD.equals(fieldName)) {
                return style2;
            }
            return super.getCellCSSText(record, rowNum, colNum);
        }
        
        @Override
        protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
            String fieldName = this.getFieldName(colNum);
            if (ResourceDatasource.GO_BUTTON_FIELD.equals(fieldName)) {
                IButton button = new IButton("Go");
                button.setHeight(23);
                button.setWidth(50);
                button.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        Resource resource = recordToResource(record);
                        Window.open(resource.getLink(), "", "");
                    }
                });
                return button;
            } else if (ResourceDatasource.REMOVE_FIELD.equals(fieldName)) {
                ImgButton img = prepareImgButton("removeIcon.gif");
                img.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        SC.ask("Are you realy want to remove this web resource?", new BooleanCallback() {
                            public void execute(Boolean yes) {
                                if (yes != null && yes.booleanValue()) {
                                    Resource resource = recordToResource(record);
                                    view.getPresenter().removeResourceAction(resource, record);
                                }
                            }
                        });
                    }
                });
                return img;
            } else if (ResourceDatasource.MOVE_FIELD.equals(fieldName)) {
                ImgButton img = prepareImgButton("movePic.png");
                img.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        Resource resource = recordToResource(record);
                        int dirIdOfResource = resource.getDirectoryId();
                        getPresenter().loadPreferDirs(resource.getId(), dirIdOfResource, (ResourceRecord) record);
                    }
                });
                return img;
            } else {
                return null;
            }
        }
    }
	
	private DynamicForm createForm() {
		resourceField = new WrmTextItem("res", "Resource");
		nameField = new WrmTextItem("nam", "Name");
		saveButton = new ButtonItem("sav", "Save");
		
//		saveButton.setBaseStyle("aaa");
//		saveButton.setTextBoxStyle("bbb");
//		saveButton.setPrintTextBoxStyle("ccc");
//		saveButton.setPrintTitleStyle("ccc");
		
//		saveButton.setPickerIconName(null);
//		saveButton.setDefaultIconSrc(null);
//		
//		saveButton.setShowDisabled(false);
//		saveButton.setShowErrorIcon(false);
//		saveButton.setShowErrorStyle(false);
//		saveButton.setShowFocused(false);
//		saveButton.setShowFocusedIcons(false);
//		saveButton.setShowFocusedPickerIcon(false);
//		saveButton.setShowHint(false);
//		saveButton.setShowIcons(false);
//		saveButton.setShowOverIcons(false);
//		saveButton.setShowPickerIcon(false);
//		saveButton.setShowTitle(false);
//		saveButton.setShowValueIconOnly(false);
//		saveButton.setApplyPromptToCanvas(false);
//		saveButton.setCanFocus(false);
		
		resourceField.setValue("https://www.google.com.ua/");
		nameField.setValue("Google Ukr");
		
		resourceField.setRequired(true);
		nameField.setRequired(true);
		
//		resourceField.setShowHint(true);
//		resourceField.setShowHintInField(true);
//		resourceField.setHint("Input resource link...");
		
		resourceField.setColSpan(2);
		saveButton.setAlign(Alignment.RIGHT);
		saveButton.setHeight(25);
		
		nameField.setStartRow(true);
		saveButton.setStartRow(false);
		
		resourceField.setWidth("100%");
		saveButton.setWidth("100%");
		nameField.setWidth("100%");
		

		DynamicForm form = new DynamicForm();
		form.setColWidths("10%", "*", "15%");
		form.setTitleSuffix("");
		form.setShowErrorIcons(false);
		form.setWrapItemTitles(false);
		form.setWidth("100%");
		form.setNumCols(3);
		form.setCellPadding(4);
		form.setFields(resourceField, nameField, saveButton, new RowSpacerItem());
		return form;
	}
	
	public ButtonItem getSaveButton() {
		return saveButton;
	}
	
	public TreeGrid getDirectoriesTree() {
        return directoriesTree;
    }
	
	public boolean validateResourceInsertion() {
	    boolean isSelectedFolder = getSelectedDirectory() != null;
	    if (!isSelectedFolder) {
	        SC.warn("Folder is not selected.");
	        return false;
	    }
	    return saveButton.getForm().validate();
	}
	
	public Tree getTree(TreeNode[] nodes) {
	    Tree tree = new Tree();
        tree.setModelType(TreeModelType.PARENT);
        tree.setIdField(DirectoryTreeNode.DIR_ID_FIELD);
        tree.setParentIdField(DirectoryTreeNode.PAR_DIR_ID_FIELD);
        tree.setNameProperty(DirectoryTreeNode.DIR_NAME_FIELD);
        tree.setData(nodes);
        return tree;
    }
	
	public Resource composeUserData() {
	    int directoryId = getSelectedDirectory().getId();
	    String link = resourceField.getValueAsString();
        String name = nameField.getValueAsString();
        
        Resource resource = new Resource();
        resource.setName(name);
        resource.setLink(link);
        resource.setDirectoryId(directoryId);
        
        return resource;
	}
	
	public Directory getSelectedDirectory() {
	    ListGridRecord selectedFolder = directoriesTree.getSelectedRecord();
	    if (selectedFolder != null) {
	        DirectoryTreeNode dirNode = (DirectoryTreeNode) selectedFolder;
	        return dirNode.asDirectory();
	    } else return null;
	}
	
	public void clearUI() {
	    nameField.clearValue();
	    resourceField.clearValue();
//	    directoriesTree.deselectRecord(directoriesTree.getSelectedRecord());
	}
	
	public DynamicForm getAddResourceForm() {
	    return form;
	}
	
	public ListGrid getResourceGrid() {
	    return listGrid;
	}
	
	public TextItem getSearchingField() {
	    return searchField;
	}
}
