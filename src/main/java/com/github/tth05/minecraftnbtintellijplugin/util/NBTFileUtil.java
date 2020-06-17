package com.github.tth05.minecraftnbtintellijplugin.util;

import com.github.tth05.minecraftnbtintellijplugin.NBTTagTreeNode;
import com.github.tth05.minecraftnbtintellijplugin.NBTTagType;
import com.github.tth05.minecraftnbtintellijplugin.editor.ui.NBTFileEditorUI;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipException;

// https://minecraft.gamepedia.com/NBT_format
public class NBTFileUtil {

	/**
	 * Uses the event to get the current project and file and then calls {@link #saveTreeToFile(Tree, VirtualFile, Project)}
	 * This method is only used for auto-saving and only called by actions
	 *
	 * @param event The event
	 */
	public static void saveTree(AnActionEvent event) {
		NBTFileEditorUI nbtFileEditorUI = event.getData(NBTFileEditorUI.DATA_KEY);
		Project project = event.getProject();
		VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);

		if (nbtFileEditorUI == null || nbtFileEditorUI.getTree() == null || file == null) {
			new Notification("NBTSaveError",
					"Error Saving NBT File",
					"Due to an unknown error the file could not be saved.",
					NotificationType.WARNING).notify(project);
			return;
		}

		if (!nbtFileEditorUI.isAutoSaveEnabled())
			return;

		saveTreeToFile(nbtFileEditorUI.getTree(), file, project);
	}

	/**
	 * Serializes the given tree and writes the bytes to the file. If the saving failed, a notification will be shown.
	 * @param tree The tree to be serialized
	 * @param file The file to write the bytes to
	 * @param project The current project to show the notification in
	 */
	public static void saveTreeToFile(Tree tree, VirtualFile file, Project project) {
		ApplicationManager.getApplication().runWriteAction(() -> {
			try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(file.getOutputStream(tree));
			     DataOutputStream outputStream = new DataOutputStream(gzipOutputStream)) {

				writeNodeToStream((NBTTagTreeNode) tree.getModel().getRoot(), outputStream, true);
			} catch (IOException ex) {
				new Notification("NBTSaveError",
						"Error saving NBT file",
						"Due to an unknown error the file could not be saved.",
						NotificationType.WARNING).notify(project);
			}
		});
	}

	private static void writeNodeToStream(NBTTagTreeNode node,
	                                      DataOutputStream stream,
	                                      boolean writeName) throws IOException {
		if (writeName) {
			stream.writeByte(node.getType().getId());
			stream.writeUTF(node.getName());
		}
		switch (node.getType()) {
			case BYTE:
				stream.writeByte((Byte) node.getValue());
				break;
			case SHORT:
				stream.writeShort((Short) node.getValue());
				break;
			case LONG:
				stream.writeLong((Long) node.getValue());
				break;
			case INT:
				stream.writeInt((Integer) node.getValue());
				break;
			case FLOAT:
				stream.writeFloat((Float) node.getValue());
				break;
			case DOUBLE:
				stream.writeDouble((Double) node.getValue());
				break;
			case BYTE_ARRAY:
				stream.writeInt(node.getChildCount());
				Enumeration byteArrayChildren = node.children();
				while (byteArrayChildren.hasMoreElements()) {
					NBTTagTreeNode child = (NBTTagTreeNode) byteArrayChildren.nextElement();
					stream.writeByte((Byte) (child).getValue());
				}
				break;
			case STRING:
				stream.writeUTF((String) node.getValue());
				break;
			case LIST:
				if (node.getChildCount() > 0)
					stream.writeByte(((NBTTagTreeNode) node.getFirstChild()).getType().getId());
				else
					stream.writeByte(0);
				stream.writeInt(node.getChildCount());
				Enumeration listChildren = node.children();
				while (listChildren.hasMoreElements())
					writeNodeToStream((NBTTagTreeNode) listChildren.nextElement(), stream, false);
				break;
			case COMPOUND:
				Enumeration compoundChildren = node.children();
				while (compoundChildren.hasMoreElements())
					writeNodeToStream((NBTTagTreeNode) compoundChildren.nextElement(), stream, true);
				stream.writeByte(0);
				break;
			case INT_ARRAY:
				stream.writeInt(node.getChildCount());
				Enumeration intArrayChildren = node.children();
				while (intArrayChildren.hasMoreElements())
					stream.writeInt((Integer) ((NBTTagTreeNode) intArrayChildren.nextElement()).getValue());
				break;
			case LONG_ARRAY:
				stream.writeInt(node.getChildCount());
				Enumeration longArrayChildren = node.children();
				while (longArrayChildren.hasMoreElements())
					stream.writeLong((Long) ((NBTTagTreeNode) longArrayChildren.nextElement()).getValue());
				break;
		}
	}

	@Nullable
	public static DefaultMutableTreeNode loadNBTFileIntoTree(VirtualFile file) {
		try (DataInputStream data = uncompress(file.getInputStream())) {
			//Get tag id
			byte type = data.readByte();

			//Root has to be a compound
			if (type != 10)
				return null;

			NBTTagTreeNode root = new NBTTagTreeNode(NBTTagType.COMPOUND, data.readUTF(), null);
			loadNBTDataOfCompound(root, data);
			return root;
		} catch (IOException e) {
			return null;
		}
	}

	private static void loadNBTDataOfCompound(DefaultMutableTreeNode root, DataInputStream data) throws IOException {
		while (true) {
			//Get tag id
			byte type = data.readByte();

			if (type != 0)
				root.add(createNode(type, data.readUTF(), data));
			else
				return;
		}
	}

	private static NBTTagTreeNode createNode(byte type, String name, DataInputStream data) throws IOException {
		switch (type) {
			case 1:
				return new NBTTagTreeNode(NBTTagType.BYTE, name, data.readByte());
			case 2:
				return new NBTTagTreeNode(NBTTagType.SHORT, name, data.readShort());
			case 3:
				return new NBTTagTreeNode(NBTTagType.INT, name, data.readInt());
			case 4:
				return new NBTTagTreeNode(NBTTagType.LONG, name, data.readLong());
			case 5:
				return new NBTTagTreeNode(NBTTagType.FLOAT, name, data.readFloat());
			case 6:
				return new NBTTagTreeNode(NBTTagType.DOUBLE, name, data.readDouble());
			case 7:
				int byteArraySize = data.readInt();
				NBTTagTreeNode byteArrayNode = new NBTTagTreeNode(NBTTagType.BYTE_ARRAY, name, byteArraySize);
				for (int i = 0; i < byteArraySize; i++)
					byteArrayNode.add(createNode((byte) 1, i + "", data));
				return byteArrayNode;
			case 8:
				return new NBTTagTreeNode(NBTTagType.STRING, name, data.readUTF());
			case 9:
				byte listType = data.readByte();
				int listSize = data.readInt();
				NBTTagTreeNode listNode = new NBTTagTreeNode(NBTTagType.LIST, name, listSize);
				for (int i = 0; i < listSize; i++)
					listNode.add(createNode(listType, i + "", data));
				return listNode;
			case 10:
				NBTTagTreeNode compoundNode = new NBTTagTreeNode(NBTTagType.COMPOUND, name, null);
				loadNBTDataOfCompound(compoundNode, data);
				return compoundNode;
			case 11:
				int intArraySize = data.readInt();
				NBTTagTreeNode intArrayNode = new NBTTagTreeNode(NBTTagType.INT_ARRAY, name, intArraySize);
				for (int i = 0; i < intArraySize; i++)
					intArrayNode.add(createNode((byte) 3, i + "", data));
				return intArrayNode;
			case 12:
				int longArraySize = data.readInt();
				NBTTagTreeNode longArrayNode = new NBTTagTreeNode(NBTTagType.LONG_ARRAY, name, longArraySize);
				for (int i = 0; i < longArraySize; i++)
					longArrayNode.add(createNode((byte) 4, i + "", data));
				return longArrayNode;
			default:
				throw new IOException("Unknown tag id found!");
		}
	}

	private static DataInputStream uncompress(InputStream input) throws IOException {
		try {
			final GZIPInputStream inputGzipStream = new GZIPInputStream(input);
			return new DataInputStream(inputGzipStream);
		} catch (ZipException e) {
			//Data is not compressed
			input.reset();
			return new DataInputStream(input);
		}
	}
}
