package com.github.tth05.minecraftnbtintellijplugin;

import com.intellij.openapi.util.IconLoader;

import javax.swing.Icon;
import java.util.function.Function;

public enum NBTTagType {

	COMPOUND(IconLoader.getIcon("/icons/TAG_Compound.png"), true, false, null, null, null),
	LIST(IconLoader.getIcon("/icons/TAG_List.png"), true, false, null, null, null),
	BYTE(IconLoader.getIcon("/icons/TAG_Byte.png"), false, true, 0, (v) -> {
		try {
			Byte.parseByte(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}, Byte::valueOf),
	SHORT(IconLoader.getIcon("/icons/TAG_Short.png"), false, true, 0, (v) -> {
		try {
			Short.parseShort(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}, Short::valueOf),
	FLOAT(IconLoader.getIcon("/icons/TAG_Float.png"), false, true, 0.0F, (v) -> {
		try {
			Float.parseFloat(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}, Float::valueOf),
	INT(IconLoader.getIcon("/icons/TAG_Int.png"), false, true, 0, (v) -> {
		try {
			Integer.parseInt(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}, Integer::valueOf),
	LONG(IconLoader.getIcon("/icons/TAG_Long.png"), false, true, 0, (v) -> {
		try {
			Long.parseLong(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}, Long::valueOf),
	DOUBLE(IconLoader.getIcon("/icons/TAG_Double.png"), false, true, 0.0D, (v) -> {
		try {
			Double.parseDouble(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}, Double::valueOf),
	BYTE_ARRAY(IconLoader.getIcon("/icons/TAG_Byte_Array.png"), true, false, null, null, null),
	INT_ARRAY(IconLoader.getIcon("/icons/TAG_Int_Array.png"), true, false, null, null, null),
	LONG_ARRAY(IconLoader.getIcon("/icons/TAG_Int_Array.png"), true, false, null, null, null),
	STRING(IconLoader.getIcon("/icons/TAG_String.png"), false, true, "", (v) -> v != null && v.length() < 32767,
			(s) -> s);

	private final Icon icon;
	private final boolean allowsChildren;
	private final boolean hasValue;
	private final Object defaultValue;
	private final Function<String, Boolean> valueValidator;
	private final Function<String, Object> stringToValueConverter;

	NBTTagType(Icon icon,
	           boolean allowsChildren,
	           boolean hasValue,
	           Object defaultValue,
	           Function<String, Boolean> valueValidator,
	           Function<String, Object> stringToValueConverter) {
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
