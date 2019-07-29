package com.github.tth05.minecraftnbtintellijplugin;

import javax.swing.tree.DefaultMutableTreeNode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class NBTValueTreeNode extends DefaultMutableTreeNode {

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();
	static {
		DECIMAL_FORMAT.setGroupingUsed(false);
		DecimalFormatSymbols symbols = DECIMAL_FORMAT.getDecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DECIMAL_FORMAT.setDecimalFormatSymbols(symbols);
	}

	private NBTTagType type;
	private String name;
	private Object value;

	public NBTValueTreeNode(NBTTagType type, String name, Object value) {
		super();
		this.type = type;
		this.name = name;
		this.value = value;
		update();
	}

	private void update() {
		setUserObject(this.name + ": " + getValueAsString());
	}

	public NBTTagType getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	public void setType(NBTTagType type) {
		this.type = type;
		setValue(type.getDefaultValue());
	}

	public void setName(String name) {
		this.name = name;
		update();
	}

	public void setValue(Object value) {
		this.value = value;
		update();
	}

	public String getValueAsString() {
		String stringValue = "Unknown value type";
		if (this.value == null)
			stringValue = "";
		else if (this.value instanceof String)
			stringValue = "\"" + this.value.toString() + "\"";
		else if (this.value instanceof Number)
			stringValue = DECIMAL_FORMAT.format(this.value);
		return stringValue;
	}
}
