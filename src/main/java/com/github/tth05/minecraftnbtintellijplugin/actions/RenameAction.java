package com.github.tth05.minecraftnbtintellijplugin.actions;

import com.github.tth05.minecraftnbtintellijplugin.NBTTagTreeNode;
import com.github.tth05.minecraftnbtintellijplugin.editor.ui.NBTFileEditorUI;
import com.github.tth05.minecraftnbtintellijplugin.util.NBTFileUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class RenameAction extends AnAction {

	public RenameAction() {
		super("Rename", "Rename", AllIcons.Actions.Edit);
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		NBTFileEditorUI nbtFileEditorUI = e.getData(NBTFileEditorUI.DATA_KEY);
		if (nbtFileEditorUI != null) {
			NBTTagTreeNode selectedNode = ((NBTTagTreeNode) nbtFileEditorUI.getTree()
					.getLastSelectedPathComponent());

			String inputString = Messages.showInputDialog("Rename this tag to:", "Rename", null, selectedNode.getName(),
					new InputValidator() {
						@Override
						public boolean checkInput(String inputString) {
							return inputString != null && !inputString.trim().isEmpty();
						}

						@Override
						public boolean canClose(String inputString) {
							return inputString != null && !inputString.trim().isEmpty();
						}
					});
			if (inputString != null) {
				selectedNode.setName(inputString);
				nbtFileEditorUI.repaint();
				NBTFileUtil.saveTree(e);
			}
		}
	}
}
