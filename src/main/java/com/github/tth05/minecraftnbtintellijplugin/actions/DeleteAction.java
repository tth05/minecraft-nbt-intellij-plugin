package com.github.tth05.minecraftnbtintellijplugin.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class DeleteAction extends AnAction {

	public DeleteAction() {
		super("Delete", "Delete this node", AllIcons.Actions.Cancel);
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {

	}
}
