package com.github.tth05.minecraftnbtintellijplugin;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public class NBTFileType implements FileType {

	public static final NBTFileType INSTANCE = new NBTFileType();

	@NotNull
	@Override
	public String getName() {
		return "NBT";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Named Binary Tag";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "nbt";
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return NBTTagType.COMPOUND.getIcon();
	}

	@Override
	public boolean isBinary() {
		return false;
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Nullable
	@Override
	public String getCharset(@NotNull VirtualFile file, @NotNull byte[] content) {
		return null;
	}
}
