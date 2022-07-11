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
		return "Minecraft NBT";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Minecraft's Named Binary Tag (NBT)";
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
		return true;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Nullable
	@Override
	public String getCharset(@NotNull VirtualFile file, byte @NotNull [] content) {
		return null;
	}
}
