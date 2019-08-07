package com.github.tth05.minecraftnbtintellijplugin.actions;

import com.github.tth05.minecraftnbtintellijplugin.NBTTagTreeNode;
import com.github.tth05.minecraftnbtintellijplugin.NBTTagType;
import com.github.tth05.minecraftnbtintellijplugin.editor.dialogs.CreateNewNodeDialog;
import com.github.tth05.minecraftnbtintellijplugin.editor.ui.NBTFileEditorUI;
import com.github.tth05.minecraftnbtintellijplugin.util.NBTFileUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultTreeModel;

public class AddChildAction extends AnAction {

	public AddChildAction() {
		super("Add Child", "Add a child to this node", AllIcons.General.Add);
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		NBTFileEditorUI nbtFileEditorUI = e.getData(NBTFileEditorUI.DATA_KEY);
		if (nbtFileEditorUI != null) {
			NBTTagTreeNode selectedNode = ((NBTTagTreeNode) nbtFileEditorUI.getTree()
					.getLastSelectedPathComponent());

			//Everything except for a compound and a list with no children can only have specific types
			if (selectedNode.getType() != NBTTagType.COMPOUND &&
					!(selectedNode.getType() == NBTTagType.LIST && selectedNode.getChildCount() < 1)) {
				NBTTagType type = null;
				switch (selectedNode.getType()) {
					case LIST:
						type = ((NBTTagTreeNode) selectedNode.getFirstChild()).getType();
						break;
					case INT_ARRAY:
						type = NBTTagType.INT;
						break;
					case LONG_ARRAY:
						type = NBTTagType.LONG;
						break;
					case BYTE_ARRAY:
						type = NBTTagType.BYTE;
						break;
				}

				if (type == null)
					throw new IllegalStateException("Type of child could not be determined!");

				((DefaultTreeModel) nbtFileEditorUI.getTree().getModel()).insertNodeInto(
						new NBTTagTreeNode(type, selectedNode.getChildCount() + "", type.getDefaultValue()),
						selectedNode, selectedNode.getChildCount());
				NBTFileUtil.saveTree(e);
				return;
			}

			CreateNewNodeDialog createNewNodeDialog = new CreateNewNodeDialog(e.getProject(),
					selectedNode.getType() != NBTTagType.COMPOUND ?
							selectedNode.getChildCount() + "" :
							null);
			boolean exitCode = createNewNodeDialog.showAndGet();
			if (!exitCode)
				return;


			((DefaultTreeModel) nbtFileEditorUI.getTree().getModel()).insertNodeInto(
					new NBTTagTreeNode(createNewNodeDialog.getType(), createNewNodeDialog.getName(),
							createNewNodeDialog.getType().getDefaultValue()),
					selectedNode, selectedNode.getChildCount());
			NBTFileUtil.saveTree(e);
		}
	}
}
