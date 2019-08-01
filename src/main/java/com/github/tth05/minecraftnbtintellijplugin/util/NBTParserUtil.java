package com.github.tth05.minecraftnbtintellijplugin.util;

import com.github.tth05.minecraftnbtintellijplugin.NBTTagType;
import com.github.tth05.minecraftnbtintellijplugin.NBTValueTreeNode;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

public class NBTParserUtil {

	private static ByteBuffer uncompress(byte[] input, VirtualFile file) throws IOException {
		try {
			//Get uncompressed file size to know the size of the ByteBuffer
			RandomAccessFile raf = new RandomAccessFile(VfsUtil.virtualToIoFile(file), "r");
			raf.seek(raf.length() - 4);
			int b4 = raf.read();
			int b3 = raf.read();
			int b2 = raf.read();
			int b1 = raf.read();
			int fileSize = (b1 << 24) | (b2 << 16) + (b3 << 8) + b4;
			raf.close();

			final GZIPInputStream inputGzipStream = new GZIPInputStream(new ByteArrayInputStream(input));
			ByteBuffer buffer = ByteBuffer.allocate(fileSize);
			Channels.newChannel(inputGzipStream).read(buffer);
			buffer.compact();
			return buffer;
		} catch (ZipException e) {
			//Data is not compressed
			return ByteBuffer.wrap(input);
		}
	}

	@Nullable
	public static DefaultMutableTreeNode loadNBTFileIntoTree(VirtualFile file) {
		ByteBuffer data = null;
		try {
			data = uncompress(file.contentsToByteArray(), file);
		} catch (IOException e) {
			return null;
		}

		//Get tag id
		byte type = data.get();

		//Root has to be a compound
		if (type != 10)
			return null;

		String name = "";
		//Get next two bytes being the length of the name
		int nameLength = data.getShort();
		byte[] nameBytes = new byte[nameLength];
		for (int i = 0; i < nameBytes.length; i++) {
			nameBytes[i] = data.get();
		}
		name = new String(nameBytes);

		NBTValueTreeNode root = new NBTValueTreeNode(NBTTagType.COMPOUND, name, null);
		loadNBTDataOfCompound(root, data);
		return root;
	}

	private static void loadNBTDataOfCompound(DefaultMutableTreeNode root, ByteBuffer data) {
		while (data.hasRemaining()) {
			//Get tag id
			byte type = data.get();

			String name = "";
			if (type != 0) {
				//Get next two bytes being the length of the name
				int nameLength = data.getShort();
				byte[] nameBytes = new byte[nameLength];
				for (int i = 0; i < nameBytes.length; i++) {
					nameBytes[i] = data.get();
				}
				name = new String(nameBytes);
			} else {
				return;
			}

			root.add(createNode(type, name, data));
		}
	}

	private static NBTValueTreeNode createNode(byte type, String name, ByteBuffer data) {
		switch (type) {
			case 1:
				return new NBTValueTreeNode(NBTTagType.BYTE, name, data.get());
			case 2:
				return new NBTValueTreeNode(NBTTagType.SHORT, name, data.getShort());
			case 3:
				return new NBTValueTreeNode(NBTTagType.INT, name, data.getInt());
			case 4:
				return new NBTValueTreeNode(NBTTagType.LONG, name, data.getLong());
			case 5:
				return new NBTValueTreeNode(NBTTagType.FLOAT, name, data.getFloat());
			case 6:
				return new NBTValueTreeNode(NBTTagType.DOUBLE, name, data.getDouble());
			case 8:
				String stringValue = "";
				short stringValueLength = data.getShort();
				byte[] stringValueBytes = new byte[stringValueLength];
				for (int i = 0; i < stringValueBytes.length; i++) {
					stringValueBytes[i] = data.get();
				}
				stringValue = new String(stringValueBytes);

				return new NBTValueTreeNode(NBTTagType.STRING, name, stringValue);
			case 9:
				byte listType = data.get();
				int listSize = data.getInt();
				NBTValueTreeNode listNode = new NBTValueTreeNode(NBTTagType.LIST, name, listSize);
				for (int i = 0; i < listSize; i++) {
					listNode.add(createNode(listType, i + "", data));
				}
				return listNode;
			case 10:
				NBTValueTreeNode compoundNode = new NBTValueTreeNode(NBTTagType.COMPOUND, name, null);
				loadNBTDataOfCompound(compoundNode, data);
				return compoundNode;
			default:
				//TODO: Throw exception
				return new NBTValueTreeNode(NBTTagType.COMPOUND, "Error", null);
		}
	}
}
