package com.github.tth05.minecraftnbtintellijplugin.actions;

import com.github.tth05.minecraftnbtintellijplugin.NBTValueTreeNode;
import com.github.tth05.minecraftnbtintellijplugin.editor.ui.NBTFileEditorUI;
import com.github.tth05.minecraftnbtintellijplugin.util.ChooseTypeDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class ChangeTypeAction extends AnAction {

	public ChangeTypeAction() {
		super("Change Type", "Change the type of this node", null);
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		NBTFileEditorUI nbtFileEditorUI = e.getData(NBTFileEditorUI.DATA_KEY);
		if (nbtFileEditorUI != null) {
			NBTValueTreeNode selectedNode = ((NBTValueTreeNode) nbtFileEditorUI.getTree()
					.getLastSelectedPathComponent());

			ChooseTypeDialog chooseTypeDialog = new ChooseTypeDialog(e.getProject(), selectedNode.getType());
			boolean exitCode = chooseTypeDialog.showAndGet();
			if (!exitCode || chooseTypeDialog.getResult() == selectedNode.getType())
				return;

			selectedNode.removeAllChildren();
			selectedNode.setType(chooseTypeDialog.getResult());
		}
	}
}
