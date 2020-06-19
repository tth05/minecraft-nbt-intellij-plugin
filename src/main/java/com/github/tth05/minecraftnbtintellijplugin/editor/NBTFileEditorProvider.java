package com.github.tth05.minecraftnbtintellijplugin.editor;

import com.github.tth05.minecraftnbtintellijplugin.NBTFileType;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NBTFileEditorProvider implements FileEditorProvider, DumbAware {
	@Override
	public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
		return FileTypeRegistry.getInstance().isFileOfType(file, NBTFileType.INSTANCE);
	}

	@NotNull
	@Override
	public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
		return new NBTFileEditor(file, project);
	}

	@NotNull
	@Override
	public FileEditorState readState(@NotNull Element sourceElement, @NotNull Project project,
	                                 @NotNull VirtualFile file) {
		List<String> openedNodes = new ArrayList<>();

		Element rootElement = sourceElement.getChild("openedNodes");
		for (int i = 0; ; i++) {
			String text = rootElement.getChildText("index" + i);
			if (text == null)
				break;
			openedNodes.add(text);
		}

		return new NBTFileEditorState(openedNodes);
	}

	@Override
	public void writeState(@NotNull FileEditorState state, @NotNull Project project, @NotNull Element targetElement) {
		if (state instanceof NBTFileEditorState) {
			NBTFileEditorState editorState = (NBTFileEditorState) state;
			List<String> openedNodes = editorState.getOpenedNodes();

			Element rootElement = new Element("openedNodes");
			for (int i = 0; i < openedNodes.size(); i++) {
				Element element = new Element("index" + i);
				element.setText(openedNodes.get(i));
				rootElement.addContent(element);
			}
			targetElement.addContent(rootElement);
		}
	}

	@NotNull
	@Override
	public FileEditorPolicy getPolicy() {
		return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
	}

	@NotNull
	@Override
	public String getEditorTypeId() {
		return "nbt";
	}
}
