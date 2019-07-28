package com.github.tth05.minecraftnbtintellijplugin.actions;

import com.github.tth05.minecraftnbtintellijplugin.NBTValueTreeNode;
import com.github.tth05.minecraftnbtintellijplugin.editor.ui.NBTFileEditorUI;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class ChangeValueAction extends AnAction {

	public ChangeValueAction() {
		super("Change Value", "Change the value of this node", AllIcons.Actions.Edit);
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		NBTFileEditorUI nbtFileEditorUI = e.getData(NBTFileEditorUI.DATA_KEY);
		if (nbtFileEditorUI != null) {
			NBTValueTreeNode selectedNode = ((NBTValueTreeNode) nbtFileEditorUI.getTree().getLastSelectedPathComponent());

			String inputString = Messages.showInputDialog("Change the value of this tag to:", "Change Value", AllIcons.Actions.Edit, selectedNode.getValueAsString(), new InputValidator() {
				@Override
				public boolean checkInput(String inputString) {
					return selectedNode.getType().getValueValidator().apply(inputString);
				}

				@Override
				public boolean canClose(String inputString) {
					return selectedNode.getType().getValueValidator().apply(inputString);
				}
			});
			if (inputString != null) {
				selectedNode.setValue(inputString);
				nbtFileEditorUI.repaint();
			}
		}
	}
}
