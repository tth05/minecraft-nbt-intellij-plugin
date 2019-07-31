package com.github.tth05.minecraftnbtintellijplugin.actions;

import com.github.tth05.minecraftnbtintellijplugin.NBTTagType;
import com.github.tth05.minecraftnbtintellijplugin.NBTValueTreeNode;
import com.github.tth05.minecraftnbtintellijplugin.editor.dialogs.CreateNewNodeDialog;
import com.github.tth05.minecraftnbtintellijplugin.editor.ui.NBTFileEditorUI;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class AddChildAction extends AnAction {

	public AddChildAction() {
		super("Add Child", "Add a child to this node", AllIcons.General.Add);
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		NBTFileEditorUI nbtFileEditorUI = e.getData(NBTFileEditorUI.DATA_KEY);
		if (nbtFileEditorUI != null) {
			NBTValueTreeNode selectedNode = ((NBTValueTreeNode) nbtFileEditorUI.getTree()
					.getLastSelectedPathComponent());

			CreateNewNodeDialog createNewNodeDialog = new CreateNewNodeDialog(e.getProject(),
					selectedNode.getType() != NBTTagType.COMPOUND ?
							selectedNode.getChildCount() + "" :
							null);
			boolean exitCode = createNewNodeDialog.showAndGet();
			if (!exitCode)
				return;

			selectedNode.add(new NBTValueTreeNode(createNewNodeDialog.getType(), createNewNodeDialog.getName(),
					createNewNodeDialog.getType().getDefaultValue()));
		}
	}
}
