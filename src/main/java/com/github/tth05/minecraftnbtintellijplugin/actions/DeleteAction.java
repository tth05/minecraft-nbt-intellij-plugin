package com.github.tth05.minecraftnbtintellijplugin.actions;

import com.github.tth05.minecraftnbtintellijplugin.editor.ui.NBTFileEditorUI;
import com.github.tth05.minecraftnbtintellijplugin.util.NBTFileUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

public class DeleteAction extends AnAction {

	public DeleteAction() {
		super("Delete", "Delete this node", AllIcons.Actions.Cancel);
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		NBTFileEditorUI nbtFileEditorUI = e.getData(NBTFileEditorUI.DATA_KEY);
		if (nbtFileEditorUI != null) {
			DefaultTreeModel treeModel = ((DefaultTreeModel) nbtFileEditorUI.getTree().getModel());
			for (TreePath path : nbtFileEditorUI.getTree().getSelectionModel().getSelectionPaths())
				treeModel.removeNodeFromParent((MutableTreeNode) path.getLastPathComponent());
			NBTFileUtil.saveTree(e);
		}
	}
}
