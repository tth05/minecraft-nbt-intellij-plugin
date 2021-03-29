package com.github.tth05.minecraftnbtintellijplugin.editor;

import com.github.tth05.minecraftnbtintellijplugin.editor.ui.NBTFileEditorUI;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.tree.TreePath;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class NBTFileEditor implements FileEditor {

	private final NBTFileEditorUI component;
	private final VirtualFile file;

	public NBTFileEditor(VirtualFile file, Project project) {
		this.file = file;
		this.component = new NBTFileEditorUI(file, project);
	}

	@NotNull
	@Override
	public JComponent getComponent() {
		return this.component;
	}

	@Nullable
	@Override
	public JComponent getPreferredFocusedComponent() {
		return null;
	}

	@NotNull
	@Override
	public String getName() {
		return "NBTFileEditor";
	}

	@Override
	public void setState(@NotNull FileEditorState state) {
		if (!(state instanceof NBTFileEditorState) || this.component.getTree() == null)
			return;
		NBTFileEditorState editorState = (NBTFileEditorState) state;
		for (int i = 0; i < this.component.getTree().getRowCount(); i++) {
			TreePath treePath = this.component.getTree().getPathForRow(i);
			if (editorState.getOpenedNodes().contains(treePath.toString())) {
				this.component.getTree().expandRow(i);
			}
		}
	}

	@NotNull
	@Override
	public FileEditorState getState(@NotNull FileEditorStateLevel level) {
		if (this.component.getTree() == null)
			return new NBTFileEditorState(new ArrayList<>());

		List<String> expanded = new ArrayList<>();
		for (int i = 0; i < this.component.getTree().getRowCount(); i++) {
			TreePath currPath = this.component.getTree().getPathForRow(i);
			TreePath nextPath = this.component.getTree().getPathForRow(i + 1);
			if (currPath.isDescendant(nextPath)) {
				expanded.add(currPath.toString());
			}
		}
		return new NBTFileEditorState(expanded);
	}

	@NotNull
	@Override
	public VirtualFile getFile() {
		return this.file;
	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void selectNotify() {

	}

	@Override
	public void deselectNotify() {

	}

	@Override
	public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {

	}

	@Override
	public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {

	}

	@Nullable
	@Override
	public BackgroundEditorHighlighter getBackgroundHighlighter() {
		return null;
	}

	@Nullable
	@Override
	public FileEditorLocation getCurrentLocation() {
		return null;
	}

	@Override
	public void dispose() {
	}

	@Nullable
	@Override
	public <T> T getUserData(@NotNull Key<T> key) {
		return null;
	}

	@Override
	public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {

	}
}
