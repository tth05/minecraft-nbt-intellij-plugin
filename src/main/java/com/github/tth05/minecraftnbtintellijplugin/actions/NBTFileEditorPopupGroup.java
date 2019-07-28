package com.github.tth05.minecraftnbtintellijplugin.actions;

import com.github.tth05.minecraftnbtintellijplugin.NBTTagType;
import com.github.tth05.minecraftnbtintellijplugin.NBTValueTreeNode;
import com.github.tth05.minecraftnbtintellijplugin.editor.ui.NBTFileEditorUI;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Separator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NBTFileEditorPopupGroup extends ActionGroup {
	@NotNull
	@Override
	public AnAction[] getChildren(@Nullable AnActionEvent e) {
		if (e == null)
			return new AnAction[0];

		NBTFileEditorUI nbtFileEditorUI = e.getData(NBTFileEditorUI.DATA_KEY);
		if (nbtFileEditorUI != null) {
			if (nbtFileEditorUI.getTree().getSelectionModel().getSelectionCount() > 1)
				//Only delete on multi-select
				return new AnAction[] {new DeleteAction()};

			List<AnAction> actions = new ArrayList<>();
			NBTValueTreeNode node = (NBTValueTreeNode) nbtFileEditorUI.getTree().getLastSelectedPathComponent();
			NBTValueTreeNode parent = (NBTValueTreeNode) node.getParent();

			if (parent == null)
				return new AnAction[] {new RenameAction(), new Separator(), new AddChildAction()};

			if (parent.getType() != NBTTagType.BYTE_ARRAY &&
					parent.getType() != NBTTagType.INT_ARRAY &&
					parent.getType() != NBTTagType.LONG_ARRAY &&
					parent.getType() != NBTTagType.LIST) {
				actions.add(new RenameAction());
				actions.add(new ChangeTypeAction());
			}

			if (node.getType().hasValue())
				actions.add(new ChangeValueAction());

			actions.add(new Separator());

			if (node.getType().allowsChildren())
				actions.add(new AddChildAction());

			actions.add(new DeleteAction());

			return actions.toArray(new AnAction[0]);
		}
		return new AnAction[0];
	}
}
