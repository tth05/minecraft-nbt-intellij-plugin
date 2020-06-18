package com.github.tth05.minecraftnbtintellijplugin;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.impl.FileTypeOverrider;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NBTFileTypeOverrider implements FileTypeOverrider {
	@Nullable
	@Override
	public FileType getOverriddenFileType(@NotNull VirtualFile file) {
		return "nbt".equals(file.getExtension()) ? NBTFileType.INSTANCE : null;
	}
}
