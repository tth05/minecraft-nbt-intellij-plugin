package com.github.tth05.minecraftnbtintellijplugin.actions;

import com.github.tth05.minecraftnbtintellijplugin.NBTTagType;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CreateNBTFileAction extends CreateFileFromTemplateAction {
	public CreateNBTFileAction() {
		super("NBT File", "Create a new NBT file", NBTTagType.COMPOUND.getIcon());
	}

	@Override
	protected void buildDialog(@NotNull Project project, @NotNull PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
		builder.setTitle("Create NBT File").addKind("NBT file", NBTTagType.COMPOUND.getIcon(), "NBT File");
	}

	@Override
	protected String getActionName(PsiDirectory directory, @NotNull String newName, String templateName) {
		return "NBT File YEET";
	}

	@Override
	protected PsiFile createFile(String name, String templateName, PsiDirectory dir) {
		//File templates didn't work for this because Intellij changes the bytes of the template and also adds a few
		// bytes when copying it over, which makes the file not follow the NBT format anymore

		PsiFile file = dir.createFile(name + ".nbt");
		try {
			//The bytes represent a root component without a name followed by an END tag, in a compressed format
			file.getVirtualFile().setBinaryContent(
					new byte[] {31, -117, 8, 0, 0, 0, 0, 0, 0, 3, -29, 98, 96, 96, 0, 0, 120, 63, -7, 78, 4, 0, 0, 0});
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file;
	}
}
