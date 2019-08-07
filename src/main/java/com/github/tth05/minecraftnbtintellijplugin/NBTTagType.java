package com.github.tth05.minecraftnbtintellijplugin;

import com.intellij.openapi.util.IconLoader;

import javax.swing.Icon;
import java.util.function.Function;

public enum NBTTagType {

	COMPOUND((byte) 10, IconLoader.getIcon("/icons/TAG_Compound.png"), true, false, null, null, null),
	LIST((byte) 9, IconLoader.getIcon("/icons/TAG_List.png"), true, false, null, null, null),
	BYTE((byte) 1, IconLoader.getIcon("/icons/TAG_Byte.png"), false, true, (byte) 0, (v) -> {
		try {
			Byte.parseByte(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}, Byte::parseByte),
	SHORT((byte) 2, IconLoader.getIcon("/icons/TAG_Short.png"), false, true, (short) 0, (v) -> {
		try {
			Short.parseShort(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}, Short::parseShort),
	FLOAT((byte) 5, IconLoader.getIcon("/icons/TAG_Float.png"), false, true, 0.0F, (v) -> {
		try {
			Float.parseFloat(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}, Float::parseFloat),
	INT((byte) 3, IconLoader.getIcon("/icons/TAG_Int.png"), false, true, 0, (v) -> {
		try {
			Integer.parseInt(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}, Integer::parseInt),
	LONG((byte) 4, IconLoader.getIcon("/icons/TAG_Long.png"), false, true, 0L, (v) -> {
		try {
			Long.parseLong(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}, Long::parseLong),
	DOUBLE((byte) 6, IconLoader.getIcon("/icons/TAG_Double.png"), false, true, 0.0D, (v) -> {
		try {
			Double.parseDouble(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}, Double::parseDouble),
	BYTE_ARRAY((byte) 7, IconLoader.getIcon("/icons/TAG_Byte_Array.png"), true, false, null, null, null),
	INT_ARRAY((byte) 11, IconLoader.getIcon("/icons/TAG_Int_Array.png"), true, false, null, null, null),
	LONG_ARRAY((byte) 12, IconLoader.getIcon("/icons/TAG_Int_Array.png"), true, false, null, null, null),
	STRING((byte) 8, IconLoader.getIcon("/icons/TAG_String.png"), false, true, "",
			(v) -> v != null && v.length() < 32767,
			(s) -> s);

	private final byte id;
	private final Icon icon;
	private final boolean allowsChildren;
	private final boolean hasValue;
	private final Object defaultValue;
	private final Function<String, Boolean> valueValidator;
	private final Function<String, Object> stringToValueConverter;

	NBTTagType(byte id,
	           Icon icon,
	           boolean allowsChildren,
	           boolean hasValue,
	           Object defaultValue,
	           Function<String, Boolean> valueValidator,
	           Function<String, Object> stringToValueConverter) {
		this.id = id;
		this.icon = icon;
		this.allowsChildren = allowsChildren;
		this.hasValue = hasValue;
		this.defaultValue = defaultValue;
		this.valueValidator = valueValidator;
		this.stringToValueConverter = stringToValueConverter;
	}

	public boolean allowsChildren() {
		return allowsChildren;
	}

	public boolean hasValue() {
		return hasValue;
	}

	public byte getId() {
		return id;
	}

	public Icon getIcon() {
		return icon;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public Function<String, Boolean> getValueValidator() {
		return valueValidator;
	}

	public Function<String, Object> getStringToValueConverter() {
		return stringToValueConverter;
	}
}
