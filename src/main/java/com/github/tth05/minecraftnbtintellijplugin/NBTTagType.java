package com.github.tth05.minecraftnbtintellijplugin;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.function.Function;

public enum NBTTagType {

	COMPOUND(IconLoader.getIcon("/icons/TAG_Compound.png"), true, false, null, null),
	LIST(IconLoader.getIcon("/icons/TAG_List.png"), true, false, null, null),
	BYTE(IconLoader.getIcon("/icons/TAG_Byte.png"), false, true, 0, (v) -> {
		try {
			Byte.parseByte(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}),
	SHORT(IconLoader.getIcon("/icons/TAG_Short.png"), false, true, 0, (v) -> {
		try {
			Short.parseShort(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}),
	FLOAT(IconLoader.getIcon("/icons/TAG_Float.png"), false, true, 0, (v) -> {
		try {
			Float.parseFloat(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}),
	INT(IconLoader.getIcon("/icons/TAG_Int.png"), false, true, 0, (v) -> {
		try {
			Integer.parseInt(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}),
	LONG(IconLoader.getIcon("/icons/TAG_Long.png"), false, true, 0, (v) -> {
		try {
			Long.parseLong(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}),
	DOUBLE(IconLoader.getIcon("/icons/TAG_Double.png"), false, true, 0, (v) -> {
		try {
			Double.parseDouble(v);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}),
	BYTE_ARRAY(IconLoader.getIcon("/icons/TAG_Byte_Array.png"), true, false, null, null),
	INT_ARRAY(IconLoader.getIcon("/icons/TAG_Int_Array.png"), true, false, null, null),
	LONG_ARRAY(IconLoader.getIcon("/icons/TAG_Int_Array.png"), true, false, null, null),
	STRING(IconLoader.getIcon("/icons/TAG_String.png"), false, true, "", (v) -> v != null && v.length() < 32767);

	private final Icon icon;
	private final boolean allowsChildren;
	private final boolean hasValue;
	private final @Nullable
	Object defaultValue;
	private final @Nullable
	Function<String, Boolean> valueValidator;

	NBTTagType(Icon icon, boolean allowsChildren, boolean hasValue, @Nullable Object defaultValue, @Nullable Function<String, Boolean> valueValidator) {
		this.icon = icon;
		this.allowsChildren = allowsChildren;
		this.hasValue = hasValue;
		this.defaultValue = defaultValue;
		this.valueValidator = valueValidator;
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

	@Nullable
	public Object getDefaultValue() {
		return defaultValue;
	}

	@Nullable
	public Function<String, Boolean> getValueValidator() {
		return valueValidator;
	}
}
