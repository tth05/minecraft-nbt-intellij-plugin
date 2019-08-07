package com.github.tth05.minecraftnbtintellijplugin.actions;

import com.github.tth05.minecraftnbtintellijplugin.NBTTagTreeNode;
import com.github.tth05.minecraftnbtintellijplugin.editor.dialogs.ChooseTypeDialog;
import com.github.tth05.minecraftnbtintellijplugin.editor.ui.NBTFileEditorUI;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultTreeModel;
import java.util.Enumeration;
import java.util.stream.IntStream;

public class ChangeTypeAction extends AnAction {

	public ChangeTypeAction() {
		super("Change Type", "Change the type of this node", null);
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		NBTFileEditorUI nbtFileEditorUI = e.getData(NBTFileEditorUI.DATA_KEY);
		if (nbtFileEditorUI != null) {
			NBTTagTreeNode selectedNode = ((NBTTagTreeNode) nbtFileEditorUI.getTree()
					.getLastSelectedPathComponent());

			ChooseTypeDialog chooseTypeDialog = new ChooseTypeDialog(e.getProject(), selectedNode.getType());
			boolean exitCode = chooseTypeDialog.showAndGet();
			if (!exitCode || chooseTypeDialog.getResult() == selectedNode.getType())
				return;

			int[] childIndices = IntStream.range(0, selectedNode.getChildCount()).toArray();
			Object[] children = new Object[selectedNode.getChildCount()];
			Enumeration enumeration = selectedNode.children();
			for (int i = 0; enumeration.hasMoreElements(); i++) {
				children[i] = enumeration.nextElement();
			}


			selectedNode.removeAllChildren();
			((DefaultTreeModel) nbtFileEditorUI.getTree().getModel())
					.nodesWereRemoved(selectedNode, childIndices, children);
			selectedNode.setType(chooseTypeDialog.getResult());
		}
	}
}
