package com.github.tth05.minecraftnbtintellijplugin.util;

import com.github.tth05.minecraftnbtintellijplugin.NBTTagType;
import com.github.tth05.minecraftnbtintellijplugin.NBTValueTreeNode;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
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

	public static void saveTreeToFile(Tree tree, VirtualFile file, Project project) {
		ApplicationManager.getApplication().runWriteAction(() -> {
			try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(file.getOutputStream(tree));
			     DataOutputStream outputStream = new DataOutputStream(gzipOutputStream)) {

				writeNodeToStream((NBTValueTreeNode) tree.getModel().getRoot(), outputStream, true);
			} catch (IOException ex) {
				new Notification("NBTSaveError",
						"Error Saving NBT File",
						"Due to an unknown error the file could not be saved.",
						NotificationType.WARNING).notify(project);
			}
		});
	}

	private static void writeNodeToStream(NBTValueTreeNode node,
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
					NBTValueTreeNode child = (NBTValueTreeNode) byteArrayChildren.nextElement();
					stream.writeByte((Byte) (child).getValue());
				}
				break;
			case STRING:
				stream.writeUTF((String) node.getValue());
				break;
			case LIST:
				if (node.getChildCount() > 0)
					stream.writeByte(((NBTValueTreeNode) node.getFirstChild()).getType().getId());
				else
					stream.writeByte(0);
				stream.writeInt(node.getChildCount());
				Enumeration listChildren = node.children();
				while (listChildren.hasMoreElements())
					writeNodeToStream((NBTValueTreeNode) listChildren.nextElement(), stream, false);
				break;
			case COMPOUND:
				Enumeration compoundChildren = node.children();
				while (compoundChildren.hasMoreElements())
					writeNodeToStream((NBTValueTreeNode) compoundChildren.nextElement(), stream, true);
				stream.writeByte(0);
				break;
			case INT_ARRAY:
				stream.writeInt(node.getChildCount());
				Enumeration intArrayChildren = node.children();
				while (intArrayChildren.hasMoreElements())
					stream.writeInt((Integer) ((NBTValueTreeNode) intArrayChildren.nextElement()).getValue());
				break;
			case LONG_ARRAY:
				stream.writeInt(node.getChildCount());
				Enumeration longArrayChildren = node.children();
				while (longArrayChildren.hasMoreElements())
					stream.writeLong((Long) ((NBTValueTreeNode) longArrayChildren.nextElement()).getValue());
				break;
		}
	}

	@Nullable
	public static DefaultMutableTreeNode loadNBTFileIntoTree(VirtualFile file) {
		try (DataInputStream data = uncompress(file.getInputStream(), file)) {
			//Get tag id
			byte type = data.readByte();

			//Root has to be a compound
			if (type != 10)
				return null;

			NBTValueTreeNode root = new NBTValueTreeNode(NBTTagType.COMPOUND, data.readUTF(), null);
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

	private static NBTValueTreeNode createNode(byte type, String name, DataInputStream data) throws IOException {
		switch (type) {
			case 1:
				return new NBTValueTreeNode(NBTTagType.BYTE, name, data.readByte());
			case 2:
				return new NBTValueTreeNode(NBTTagType.SHORT, name, data.readShort());
			case 3:
				return new NBTValueTreeNode(NBTTagType.INT, name, data.readInt());
			case 4:
				return new NBTValueTreeNode(NBTTagType.LONG, name, data.readLong());
			case 5:
				return new NBTValueTreeNode(NBTTagType.FLOAT, name, data.readFloat());
			case 6:
				return new NBTValueTreeNode(NBTTagType.DOUBLE, name, data.readDouble());
			case 7:
				int byteArraySize = data.readInt();
				NBTValueTreeNode byteArrayNode = new NBTValueTreeNode(NBTTagType.BYTE_ARRAY, name, byteArraySize);
				for (int i = 0; i < byteArraySize; i++)
					byteArrayNode.add(createNode((byte) 1, i + "", data));
				return byteArrayNode;
			case 8:
				return new NBTValueTreeNode(NBTTagType.STRING, name, data.readUTF());
			case 9:
				byte listType = data.readByte();
				int listSize = data.readInt();
				NBTValueTreeNode listNode = new NBTValueTreeNode(NBTTagType.LIST, name, listSize);
				for (int i = 0; i < listSize; i++)
					listNode.add(createNode(listType, i + "", data));
				return listNode;
			case 10:
				NBTValueTreeNode compoundNode = new NBTValueTreeNode(NBTTagType.COMPOUND, name, null);
				loadNBTDataOfCompound(compoundNode, data);
				return compoundNode;
			case 11:
				int intArraySize = data.readInt();
				NBTValueTreeNode intArrayNode = new NBTValueTreeNode(NBTTagType.INT_ARRAY, name, intArraySize);
				for (int i = 0; i < intArraySize; i++)
					intArrayNode.add(createNode((byte) 3, i + "", data));
				return intArrayNode;
			case 12:
				int longArraySize = data.readInt();
				NBTValueTreeNode longArrayNode = new NBTValueTreeNode(NBTTagType.LONG_ARRAY, name, longArraySize);
				for (int i = 0; i < longArraySize; i++)
					longArrayNode.add(createNode((byte) 4, i + "", data));
				return longArrayNode;
			default:
				throw new IOException("Unknown tag id found!");
		}
	}

	private static DataInputStream uncompress(InputStream input, VirtualFile file) throws IOException {
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
