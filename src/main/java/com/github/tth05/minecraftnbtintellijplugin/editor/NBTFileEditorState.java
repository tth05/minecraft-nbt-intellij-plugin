package com.github.tth05.minecraftnbtintellijplugin.editor;

import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;

import java.util.List;

public class NBTFileEditorState implements FileEditorState {

	private final List<String> openedNodes;

	public NBTFileEditorState(List<String> openedNodes) {
		this.openedNodes = openedNodes;
	}

	@Override
	public boolean canBeMergedWith(FileEditorState otherState, FileEditorStateLevel level) {
		return otherState instanceof NBTFileEditorState;
	}

	public List<String> getOpenedNodes() {
		return openedNodes;
	}
}
