package com.github.tth05.minecraftnbtintellijplugin.actions;

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

	}
}
